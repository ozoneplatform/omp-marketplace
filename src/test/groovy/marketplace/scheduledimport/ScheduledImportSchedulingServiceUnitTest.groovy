package marketplace.scheduledimport

import grails.config.Config
import grails.test.mixin.services.ServiceUnitTestMixin
import grails.test.mixin.TestMixin
import grails.test.mixin.TestFor
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.quartz.SimpleTrigger
import org.quartz.core.QuartzScheduler

import marketplace.ImportTask
import marketplace.InterfaceConfiguration
import marketplace.Constants

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@Mock([ImportTask, InterfaceConfiguration])
//@TestFor(ScheduledImportSchedulingService)
//@TestMixin(ServiceUnitTestMixin)
class ScheduledImportSchedulingServiceSpec extends Specification implements DataTest, ServiceUnitTest<ScheduledImportSchedulingService>{
    def cnf = [
            [
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
            ],
            [
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
            ]
    ]

    void setup() {
//        FakeAuditTrailHelper.install()

        //can't re-use the same exact domain object between tests so make and save copies
        mockDomain(InterfaceConfiguration)
       // mockDomain(ImportTask)
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
        setup:
        int scheduledImportTasksCallCount = 0
        def execIntervalCalls = []

        service.metaClass.scheduleImportTasks { -> scheduledImportTasksCallCount++}

        mockDomain(ImportTask)

        ImportTask.metaClass.setExecInterval = { Integer num, String unit ->
            execIntervalCalls << [num:num, unit: unit]
        }

//        Mock(ImportTask){
//            setExecInterval(*_) >> { Integer num, String unit ->
//                execIntervalCalls << [num:num, unit: unit]}
//        }

        //mock the other public method in the service, which this method calls at the end
//        service.metaClass.scheduleImportTasks = { -> scheduledImportTasksCallCount++ }
//        ImportTask.metaClass.setExecInterval = { Integer num, String unit ->
//            execIntervalCalls << [num: num, unit: unit]
//        }

        grailsApplication.config.marketplace.scheduledImports = cnf

        assert ImportTask.list() == []

        when:
        service.updateImportTasksFromConfig()

        def importTasks = ImportTask.list()

        then:
        importTasks.size() == 2
        scheduledImportTasksCallCount == 1

        importTasks[0].name == cnf[0].name
        importTasks[0].enabled == cnf[0].enabled
        importTasks[0].url == cnf[0].url
        importTasks[0].keystorePath == System.properties['javax.net.ssl.keyStore']
        importTasks[0].keystorePass == System.properties['javax.net.ssl.keyStorePassword']
        importTasks[0].truststorePath == System.properties['javax.net.ssl.trustStore']
        importTasks[0].updateType == 'Partial'
        importTasks[0].interfaceConfig == InterfaceConfiguration.ompInterface
        ///importTasks[0].execInterval
        execIntervalCalls[-2].num == cnf[0].frequency.count
        execIntervalCalls[-2].unit == cnf[0].frequency.unit

        importTasks[1].name == cnf[1].name
        importTasks[1].enabled == cnf[1].enabled
        importTasks[1].url == cnf[1].url
        importTasks[1].keystorePath == cnf[1].keyStore.file
        importTasks[1].keystorePass == cnf[1].keyStore.password
        importTasks[1].truststorePath == cnf[1].trustStore.file
        importTasks[1].updateType == 'Entire'
        importTasks[1].interfaceConfig == InterfaceConfiguration.ompInterface
        execIntervalCalls[-1].num == cnf[1].frequency.count
        execIntervalCalls[-1].unit == cnf[1].frequency.unit

        when:
        grailsApplication.config.marketplace.scheduledImports = []
        service.updateImportTasksFromConfig()
        then:
        ImportTask.list() == []
        scheduledImportTasksCallCount == 2

        when:
        grailsApplication.config.marketplace.scheduledImports = cnf
        service.updateImportTasksFromConfig()
        then:
        ImportTask.list().size() == 2
        scheduledImportTasksCallCount == 3

        when:
        grailsApplication.config.marketplace.scheduledImports = null
        service.updateImportTasksFromConfig()
        then:
        ImportTask.list() == []
        scheduledImportTasksCallCount == 4
    }

    void testScheduleImportTasks() {
        setup:
        mockDomain(ImportTask)
        def scheduledImportTasks = [new ImportTask(
            name: 'task 1',
            execInterval: 1,
            updateType: Constants.IMPORT_TYPE_DELTA,
            interfaceConfig: InterfaceConfiguration.ompInterface
        ), new ImportTask(
            name: 'task 2',
            execInterval: 1000,
            enabled: false,
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

        when:
        //this is the call being tested
        service.scheduleImportTasks()

        then:
        deletedKeys.size() == 2

        //only the first job should be scheduled since the other one is disabled
        scheduledJobDetails.size() == 1
        scheduledTriggers.size() == 1

        deletedKeys.collect { it.name }  == scheduledImportTasks.collect { it.name }

        [0].each { i ->
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
