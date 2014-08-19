package marketplace.scheduledimport

import org.quartz.JobKey
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.core.QuartzScheduler

import org.springframework.scheduling.quartz.SchedulerFactoryBean

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.ImportTask
import marketplace.InterfaceConfiguration
import marketplace.Constants

/**
 * This class manages the conversion of scheduled import configs into domain objects and
 * the scheduling of scheduled import jobs
 */
class ScheduledImportSchedulingService {
    GrailsApplication grailsApplication
    ScheduledImportService scheduledImportService
    def quartzScheduler

    private static final String JOB_GROUP = 'ompImport'

    /**
     * Scheduled import is currently configuration driven - it gets its settings from
     * the config files, not a UI.  This function reads the config in and creates or
     * updates the ImportTasks to match
     */
    public void updateImportTasksFromConfig() {
        def scheduledImportConfig = grailsApplication.config.marketplace.scheduledImports

        InterfaceConfiguration ompInterface = InterfaceConfiguration.getOmpInterface()

        //This collection will ultimately contain only the ImportTasks that are not
        //currently in the configuration and which should be deleted
        Collection<ImportTask> toDelete = ImportTask.findAllByInterfaceConfig(ompInterface)

        scheduledImportConfig?.each { conf ->
            ImportTask task = ImportTask.findByName(conf.name) ?: new ImportTask()

            task.name = conf.name
            task.enabled = conf.enabled
            task.url = conf.url
            task.keystorePath = conf.keyStore?.file ?:
                System.getProperty('javax.net.ssl.keyStore')
            task.keystorePass = conf.keyStore?.password ?:
                System.getProperty('javax.net.ssl.keyStorePassword')
            task.truststorePath = conf.trustStore?.file ?:
                System.getProperty('javax.net.ssl.trustStore')
            task.updateType = conf.partial ? Constants.IMPORT_TYPE_DELTA :
                Constants.IMPORT_TYPE_FULL
            task.interfaceConfig = ompInterface
            task.setExecInterval(conf.frequency.count as Integer,
                conf.frequency.unit as String)

            task.save(failOnError:true)
            toDelete.remove(task)
        }

        //delete tasks that are no longer in the configuration
        toDelete.each { it.delete() }

        scheduleImportTasks()
    }

    /**
     * Ensures that all existing (enabled) ImportTasks are currently scheduled in ImportJobs.
     * Note: this currently works by unscheduling all existing import jobs and then rescheduling
     * everything, so calling it could result in a blip in the interval of existing jobs
     */
    public void scheduleImportTasks() {
        Collection<ImportTask> tasks =
            ImportTask.findAllByInterfaceConfig(InterfaceConfiguration.getOmpInterface())

        tasks.each { unschedule(it) }
        tasks.grep { it.enabled }.each { schedule(it) }
    }

    private JobKey getJobKey(ImportTask task) {
        new JobKey(task.name, JOB_GROUP)
    }

    private void unschedule(ImportTask task) {
        quartzScheduler.deleteJob(getJobKey(task))
    }

    private void schedule(ImportTask task) {
        JobDetail jobDetail = JobBuilder.newJob(ImportJob.class)
            .withIdentity(getJobKey(task))
            .usingJobData(Constants.JOB_ID_KEY, task.id)
            .build()

        // Convert execution interval from minutes to milliseconds(ms)
        int ms = task.execInterval * 60 * 1000;
        // Delay start for the length of the interval
        Date startTime = new Date(new Date().getTime() + ms)

        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .startAt(startTime)
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(ms)
                .repeatForever()
            )

        if (!task.execInterval) {
            // Somehow we let a bad ImportTask be defined!
            String msg = "Unable to schedule ImportJob [$name] -- Interval is not defined"
            log.error msg
            throw new IllegalArgumentException(msg)
        }

        quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build())
    }
}
