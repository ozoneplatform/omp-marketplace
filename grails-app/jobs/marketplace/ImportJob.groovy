package marketplace

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobKey
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder

/**
 * <pre>
 * To run this ImportJob you need to:
 * - provide the ImportTask in the constructor
 * - provide the Quartz Scheduler instance for launching
 *
 * The Scheduler can be pulled from the app context via something like
 *
 *    Scheduler quartzScheduler = ctx.getBean('quartzScheduler')
 *
 *  but this is more appropriately done from a service or controller class.
 * </pre>
 * @author kbutler
 *
 */
class ImportJob implements Job {

    def sessionFactory // because Job executes 'offline', ensure we are connected to HBM
    def importService
    def importTask
    def name
    JobKey jobKey
    static def random = new Random()

    private static final String JOB_ID_KEY = 'importTaskId'
    private static final String JOB_GROUP = 'ompImport'

    // include no Triggers as this will be dynamically launched
    static triggers = {
    }


    public ImportJob() {
    }

    /**
     * Build a schedulable ImportJob given an ImportTask.
     * NOTE: When building outside of Spring we do not get autowiring, so
     * we need to get that by other means.
     *
     * @param task
     */
    public ImportJob(ImportTask _task) {
        name = _task.name
        importTask = _task

        jobKey = new JobKey(name, JOB_GROUP)
    }

    def generateTriggerName ()  {
        long r = random.nextLong()
        if (r < 0) {
            r = -r;
        }
        return "GRAILS_" + Long.toString(r, 30 + (int) (System.currentTimeMillis() % 7));
    }

    def schedule(def quartzScheduler) {
        assert importTask != null
        assert quartzScheduler != null

        // Pass in the ImportTaskId to the Job for lookup
        def job = JobBuilder.newJob(this.class).withIdentity(jobKey).build()
        job.jobDataMap.putAll([(JOB_ID_KEY):importTask.id])
        // Trigger may be simple or Cron-based
        def triggerBuilder = TriggerBuilder.newTrigger()
            .withIdentity(generateTriggerName(), 'OMP_Triggers')


        if(importTask.cronExp) {
            triggerBuilder = triggerBuilder
                .withSchedule(CronScheduleBuilder.cronSchedule(importTask.cronExp))
        } else if(importTask.execInterval){
            // Convert execution interval from minutes to milliseconds(ms)
            def ms = importTask.execInterval * 60 * 1000;
            // Delay start for the length of the interval
            def startTime = new java.util.Date(new java.util.Date().getTime() + ms)

            // Create Trigger -
            //     startTime as calculated above; null is endTime, i.e. no specific end;
            //     -1 means repeat indefinitely;  ms is the interval as calculated
            triggerBuilder = triggerBuilder
            .startAt(startTime)
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(ms)
                .repeatForever()
            )
        } else {
            // Somehow we let a bad ImportTask be defined!
            def msg = "Unable to schedule ImportJob [$name] -- Neither interval or cron is defined"
            log.error msg
            throw new IllegalArgumentException(msg)
        }

        log.debug "Scheduling ImportJob [$name]"
        quartzScheduler.scheduleJob (job, triggerBuilder.build())
    }

    def cancel(def quartzScheduler, def jobName = null) {
        if (!importTask || ! quartzScheduler) {
            // Fail softly
            log.warn "Trying to cancel ImportJob without a${importTask?' Scheduler':'n ImportTask'}"
            return
        }

        def cancelName = jobName ?: name

        log.debug "Unscheduling ImportJob: [$cancelName]"
        //StdSchedulerFactory.getDefaultScheduler().deleteJob(importTaskInstance.name, null)
        quartzScheduler.deleteJob(cancelName, JOB_GROUP)
    }
    /**
     * Execute this Job.
     * @param context
     * @return
     */
    public void execute(JobExecutionContext context) {
        def jobMap = context.mergedJobDataMap
        log.debug "Executing ImportJob [${context?.jobDetail?.name}] with data: $jobMap"

        try {
            if (!importService || !sessionFactory) {
                def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
                if (!ctx) throw new RuntimeException('Unable to execute ImportJob: AppContext is unavailable')
                if (!importService) {
                    log.debug "Pulling importService from AppContext (${ctx==null?'null':'non-null'})"
                    importService = ctx?.importService
                }
                // We are running offline - i.e. outside of request threads -- ensure our HBM Session
                if (!sessionFactory) {
                    log.debug "No sessionFactory; pulling from the AppContext"
                    sessionFactory = ctx?.sessionFactory
                }
            }
            log.debug "Logger class:: ${log.class.name}"

            // Ensure we have a Hibernate Session bound to the thread
            def managingHbmSession = TransactionUtils.ensureSession(sessionFactory, log)

            // Pull the scheduled ImportTask from the JobMap
            def importTaskId = jobMap[JOB_ID_KEY]
            def importTask

            if (!importTaskId) {
                log.error "Error running scheduled Job [${context?.jobDetail?.name}]: no ImportTaskId provided"
                return
            }
            else {
                importTask = ImportTask.get(importTaskId)
                if (!importTask) {
                    log.error "Error running scheduled Job [${context?.jobDetail?.name}]: could not locate ImportTask [$importTaskId]"
                    return
                }
            }

            importService.execute(importTask)
        }
        catch (Exception e) {
            log.error e
            if (log.isDebugEnabled()) {
                e.printStackTrace()
            }
            throw new JobExecutionException(e)
        }
    }

}
