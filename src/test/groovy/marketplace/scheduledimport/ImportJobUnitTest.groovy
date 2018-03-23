package marketplace.scheduledimport

import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.testing.gorm.DataTest
import org.grails.spring.beans.factory.InstanceFactoryBean

import org.quartz.JobExecutionContext
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.impl.JobDetailImpl
import spock.lang.Specification

//@TestMixin(GrailsUnitTestMixin)
class ImportJobUnitTest extends Specification implements DataTest{

    void testExecute() {
        setup:
        def jobId = 12
        def passedId

        def scheduledImportServiceMock = Mock(ScheduledImportService) {
            executeScheduledImport(*_) >> { id -> passedId = id}
        }
//        scheduledImportServiceMock.demand.executeScheduledImport(1..1) { id ->
//            passedId = id
//        }

        defineBeans {
            scheduledImportService(InstanceFactoryBean, scheduledImportServiceMock,
                ScheduledImportService)
        }

        when:
        def context = [
            getMergedJobDataMap: { ->
                new JobDataMap(importTaskId: jobId)
            },
            getJobDetail: { ->
                new JobDetailImpl('Test Job', ImportJob.class)
            }
        ] as JobExecutionContext

        new ImportJob().execute(context)
        passedId = passedId[0]

        then:
        passedId == jobId
    }
}
