package marketplace.scheduledimport

import org.quartz.JobKey
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder


/**
 * This class re-implements Scheduled Import for OMP 7.16.  It is separate from
 * the other import code (ImportExecutorService, etc) so that it can avoid the
 * complexities of interactive metadata mapping
 */
class ScheduledImportService {
    GrailsApplication grailsApplication
    QuartzScheduler quartzScheduler

    private static final String JOB_GROUP = 'ompImport'

    /**
     * Scheduled import is currently configuration driven - it gets its settings from
     * the config files, not a UI.  This function reads the config in and creates or
     * updates the ImportTasks to match
     */
    public void updateImportTasksFromConfig() {
        def scheduledImportConfig = grailsApplication.config.marketplace.scheduledImport

        if (scheduledImportConfig)
            scheduledImportConfig.each { conf ->
                ImportTask task = ImportTask.findByName(conf.name) ?: new ImportTask()

                task.with {
                    name = conf.name
                    enabled = conf.enabled
                    url = conf.url
                    keystorePath = conf.keyStore?.file ?: System['javax.net.ssl.keyStore']
                    keystorePass = conf.keyStore?.password ?:
                        System['javax.net.ssl.keyStorePassword']
                    truststorePath = conf.trustStore?.file ?: System['javax.net.ssl.trustStore']
                    updateType = conf.partial ? Constants.IMPORT_TYPE_DELTA :
                        Constants.IMPORT_TYPE_FULL
                    setExecInterval(conf.frequency.count, conf.frequency.unit)
                }

                task.save(failOnError:true)
            }
        }

        scheduleImportTasks()
    }

    /**
     * Ensures that all existing ImportTasks are currently scheduled in ImportJobs.
     * Note: this currently works by unscheduling all existing import jobs and the rescheduling
     * everything, so calling it could result in a blip in the interval of existing jobs
     */
    public void scheduleImportTasks() {
        Collection<ImportTask> tasks = ImportTask.list()

        tasks.each { unschedule(it) }
        tasks.each { schedule(it) }
    }

    private JobKey getJobKey(ImportTask task) {
        new JobKey(task.name, JOB_GROUP)
    }

    private void unschedule(ImportTask task) {
        quartzScheduler.deleteJob(getJobKey(task))
    }

    private void schedule(ImportTask task) {
        JobDetail jobDetail = JobBuilder.newJob(this.class)
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

        log.debug "Scheduling ImportJob [$name]"
        quartzScheduler.scheduleJob(triggerBuilder.build())
    }
}
