package marketplace.scheduledimport

import grails.test.mixin.services.ServiceUnitTestMixin
import grails.test.mixin.TestMixin
import grails.test.mixin.TestFor

import org.quartz.SimpleTrigger
import org.quartz.core.QuartzScheduler

import marketplace.ImportTask
import marketplace.InterfaceConfiguration
import marketplace.Constants

import marketplace.testutil.FakeAuditTrailHelper

@Mock([ImportTask, InterfaceConfiguration])
@TestFor(ScheduledImportSchedulingService)
@TestMixin(ServiceUnitTestMixin)
class ScheduledImportSchedulingServiceUnitTest {
    def config = [[
        name: 'Test Import',
        enabled: true,
        url: 'https://localhost:8443/marketplace/public/exportAll',
        keyStore: [
            file: null,
            password: null
        ],
        trustStore: [
            file: null
        ],
        partial: true,
        frequency: [
            count: 1,
            unit: 'minutes'
        ]
    ], [
        name: 'Test Import 2',
        enabled: false,
        url: 'https://localhost:9443/marketplace/public/exportAll',
        keyStore: [
            file: 'some/directory/secrets.jks',
            password: "pAsSwOrD"
        ],
        trustStore: [
            file: 'some/directory/trust.jks'
        ],
        partial: false,
        frequency: [
            count: 10,
            unit: 'days'
        ]
    ]]

    void setUp() {
        FakeAuditTrailHelper.install()

        //can't re-use the same exact domain object between tests so make and save copies
        new InterfaceConfiguration(InterfaceConfiguration.OMP_INTERFACE.properties)
            .save(failOnError: true)
        new InterfaceConfiguration(InterfaceConfiguration.FILE_IMPORT.properties
            ).save(failOnError: true)

        assert InterfaceConfiguration.list().size() == 2
        assert InterfaceConfiguration.ompInterface != null
        assert InterfaceConfiguration.fileInterface != null

        System.properties['javax.net.ssl.keyStore'] = 'standard-keystore.jks'
        System.properties['javax.net.ssl.keyStorePassword'] = 'password'
        System.properties['javax.net.ssl.trustStore'] = 'standard-truststore.jks'
    }

    void tearDown() {
        System.properties.remove('javax.net.ssl.keyStore')
        System.properties.remove('javax.net.ssl.keyStorePassword')
        System.properties.remove('javax.net.ssl.trustStore')
    }

    void testUpdateImportTasksFromConfig() {
        int scheduledImportTasksCallCount = 0
        def execIntervalCalls = []

        //mock the other public method in the service, which this method calls at the end
        service.metaClass.scheduleImportTasks = { -> scheduledImportTasksCallCount++ }
        ImportTask.metaClass.setExecInterval = { Integer num, String unit ->
            execIntervalCalls << [num: num, unit: unit]
        }

        grailsApplication.config.marketplace.scheduledImports = config

        assert ImportTask.list() == []

        service.updateImportTasksFromConfig()

        def importTasks = ImportTask.list()
        assert importTasks.size() == 2
        assert scheduledImportTasksCallCount == 1

        assert importTasks[0].name == config[0].name
        assert importTasks[0].enabled == config[0].enabled
        assert importTasks[0].url == config[0].url
        assert importTasks[0].keystorePath == System.properties['javax.net.ssl.keyStore']
        assert importTasks[0].keystorePass == System.properties['javax.net.ssl.keyStorePassword']
        assert importTasks[0].truststorePath == System.properties['javax.net.ssl.trustStore']
        assert importTasks[0].updateType == 'Partial'
        assert importTasks[0].interfaceConfig == InterfaceConfiguration.ompInterface
        assert execIntervalCalls[-2].num == config[0].frequency.count
        assert execIntervalCalls[-2].unit == config[0].frequency.unit

        assert importTasks[1].name == config[1].name
        assert importTasks[1].enabled == config[1].enabled
        assert importTasks[1].url == config[1].url
        assert importTasks[1].keystorePath == config[1].keyStore.file
        assert importTasks[1].keystorePass == config[1].keyStore.password
        assert importTasks[1].truststorePath == config[1].trustStore.file
        assert importTasks[1].updateType == 'Entire'
        assert importTasks[1].interfaceConfig == InterfaceConfiguration.ompInterface
        assert execIntervalCalls[-1].num == config[1].frequency.count
        assert execIntervalCalls[-1].unit == config[1].frequency.unit

        grailsApplication.config.marketplace.scheduledImports = []
        service.updateImportTasksFromConfig()
        assert ImportTask.list() == []
        assert scheduledImportTasksCallCount == 2

        grailsApplication.config.marketplace.scheduledImports = config
        service.updateImportTasksFromConfig()
        assert ImportTask.list().size() == 2
        assert scheduledImportTasksCallCount == 3

        grailsApplication.config.marketplace.scheduledImports = null
        service.updateImportTasksFromConfig()
        assert ImportTask.list() == []
        assert scheduledImportTasksCallCount == 4
    }

    void testScheduleImportTasks() {
        def scheduledImportTasks = [new ImportTask(
            name: 'task 1',
            execInterval: 1,
            updateType: Constants.IMPORT_TYPE_DELTA,
            interfaceConfig: InterfaceConfiguration.ompInterface
        ), new ImportTask(
            name: 'task 2',
            execInterval: 1000,
            updateType: Constants.IMPORT_TYPE_DELTA,
            interfaceConfig: InterfaceConfiguration.ompInterface
        )]

        //add a file import task as well to ensure that it isn't (un)scheduled
        def tasks = scheduledImportTasks + [new ImportTask(
            name: 'file import',
            execInterval: 200,
            updateType: Constants.IMPORT_TYPE_DELTA,
            interfaceConfig: InterfaceConfiguration.fileInterface
        )]

        def deletedKeys = []
        def scheduledJobDetails = []
        def scheduledTriggers = []

        service.quartzScheduler = [
            deleteJob: { key ->
                deletedKeys << key
            },
            scheduleJob: { jobDetail, trigger ->
                scheduledJobDetails << jobDetail
                scheduledTriggers << trigger
            }
        ]

        tasks.each { it.save(failOnError:true) }

        //this is the call being tested
        service.scheduleImportTasks()

        assert deletedKeys.size() == 2
        assert scheduledJobDetails.size() == 2
        assert scheduledTriggers.size() == 2

        assert deletedKeys.collect { it.name }  == scheduledImportTasks.collect { it.name }
        [0,1].each { i ->
            def jobDetail = scheduledJobDetails[i]

            assert jobDetail.jobClass == ImportJob
            assert jobDetail.key.name == tasks[i].name
            assert jobDetail.jobDataMap[Constants.JOB_ID_KEY] == tasks[i].id

            def trigger = scheduledTriggers[i]

            assert trigger instanceof SimpleTrigger
            assert trigger.repeatInterval == 1000  * 60 * tasks[i].execInterval
            assert trigger.repeatCount == SimpleTrigger.REPEAT_INDEFINITELY
        }
    }
}
