package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.quartz.JobKey

import marketplace.DisableInactiveAccountsJob

@TestMixin(IntegrationTestMixin)
class DisableInactiveAccountsTest {
	def quartzScheduler
	def JOB_NAME = "deleteInactiveAccounts"
	def JOB_GROUP = "ompDeleteInactiveAccounts"
    def jobKey = new JobKey(JOB_NAME, JOB_GROUP)

    //Test that scheduling works
    void testScheduleJob() {
    	def job = new DisableInactiveAccountsJob()
		job.schedule(quartzScheduler)
		assert quartzScheduler.getJobDetail(jobKey) != null
    }

    //Test that canceling deletes the job
    void testCancelJob() {
    	def job = new DisableInactiveAccountsJob()
		job.schedule(quartzScheduler)
		job.cancel(quartzScheduler)
		assert quartzScheduler.getJobDetail(jobKey) == null
    }

    //Test that scheduling the same job twice doesn't produce an error
    void testDuplicateScheduleJob() {
    	def job = new DisableInactiveAccountsJob()
		job.schedule(quartzScheduler)
		job.schedule(quartzScheduler)
		assert quartzScheduler.getJobDetail(jobKey) != null
    }
}
