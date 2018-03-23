package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import grails.testing.mixin.integration.Integration
import org.quartz.JobKey

import marketplace.DisableInactiveAccountsJob
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class DisableInactiveAccountsTest extends Specification {
	def quartzScheduler
	def grailsApplication
	def JOB_NAME = "deleteInactiveAccounts"
	def JOB_GROUP = "ompDeleteInactiveAccounts"
    def jobKey = new JobKey(JOB_NAME, JOB_GROUP)

	void setupScheduler() {
		quartzScheduler = grailsApplication.getBean('quartzScheduler')
	}
    //Test that scheduling works
    void testScheduleJob() {
		when:
    	def job = new DisableInactiveAccountsJob()
		job.schedule(quartzScheduler)
		then:
		quartzScheduler.getJobDetail(jobKey) != null
    }

    //Test that canceling deletes the job
    void testCancelJob() {
    	when:
		def job = new DisableInactiveAccountsJob()
		job.schedule(quartzScheduler)
		job.cancel(quartzScheduler)
		then:
		quartzScheduler.getJobDetail(jobKey) == null
    }

    //Test that scheduling the same job twice doesn't produce an error
    void testDuplicateScheduleJob() {
    	when:
		def job = new DisableInactiveAccountsJob()
		job.schedule(quartzScheduler)
		job.schedule(quartzScheduler)
		then:
		quartzScheduler.getJobDetail(jobKey) != null
    }
}
