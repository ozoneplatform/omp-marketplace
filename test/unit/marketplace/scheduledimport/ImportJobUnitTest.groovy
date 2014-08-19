package marketplace.scheduledimport

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.commons.InstanceFactoryBean

import org.quartz.JobExecutionContext
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.impl.JobDetailImpl

@TestMixin(GrailsUnitTestMixin)
class ImportJobUnitTest {
    void testExecute() {
        def jobId = 12
        def passedId

        def scheduledImportServiceMock = mockFor(ScheduledImportService)
        scheduledImportServiceMock.demand.executeScheduledImport(1..1) { id ->
            passedId = id
        }

        defineBeans {
            scheduledImportService(InstanceFactoryBean, scheduledImportServiceMock.createMock(),
                ScheduledImportService)
        }

        def context = [
            getMergedJobDataMap: { ->
                new JobDataMap(importTaskId: jobId)
            },
            getJobDetail: { ->
                new JobDetailImpl('Test Job', ImportJob.class)
            }
        ] as JobExecutionContext

        new ImportJob().execute(context)

        assert passedId == jobId
    }
}
