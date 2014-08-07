package marketplace.scheduledimport

/**
 * This class re-implements Scheduled Import for OMP 7.16.  It is separate from
 * the other import code (ImportExecutorService, etc) so that it can avoid the
 * complexities of interactive metadata mapping
 */
class ScheduledImportService {

    /**
     * Executes the import task with the corresponding id
     */
    public void executeScheduledImport(int importTaskId) {
        executeScheduledImport(ImportTask.get(importTaskId))
    }

    public void executeScheduledImport(ImportTask task) {

    }
}
