package marketplace.scheduledimport

import grails.util.Holders as AH

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.Job

import marketplace.Constants

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

    /**
     * Execute this Job.
     * @param context
     */
    public void execute(JobExecutionContext context) {
        def jobMap = context.mergedJobDataMap
        log.debug "Executing ImportJob [${context?.jobDetail?.name}] with data: $jobMap"


        try {
            //have to look up the service dynamically because autowiring doesn't work,
            //event though it should
            ScheduledImportService scheduledImportService =
                AH.getGrailsApplication().mainContext.getBean('scheduledImportService')

            scheduledImportService.executeScheduledImport(jobMap[Constants.JOB_ID_KEY])
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
