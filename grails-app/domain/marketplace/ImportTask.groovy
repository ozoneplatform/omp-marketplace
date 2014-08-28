package marketplace


@gorm.AuditStamp
class ImportTask implements Serializable {

    String name
    String updateType // Full or Delta
    String cronExp
    Integer execInterval
    String url
    String extraUrlParams
    Boolean enabled = true
    ImportTaskResult lastRunResult
    InterfaceConfiguration interfaceConfig
    String keystorePath
    String keystorePass
    String truststorePath
    transient def json  // Use this in place of a url when this is all we have
    //    e.g. file-based imports

    static transients = ['inHours', 'inDays', 'json', 'remoteImport']
    static hasMany = [runs: ImportTaskResult]

    static constraints = {
        name(blank: false, nullable: false, unique: true, maxSize: 50)
        updateType(maxSize: 7, inList: [Constants.IMPORT_TYPE_FULL, Constants.IMPORT_TYPE_DELTA])
        url(blank: true, nullable: true)
        cronExp(blank: true, nullable: true)
        execInterval(blank: true, nullable: true)
        lastRunResult(nullable: true)
        extraUrlParams(nullable: true, maxSize: 512)
        keystorePath(blank: false, nullable: true, maxSize: 2048)
        keystorePass(blank: false, nullable: true, maxSize: 2048)
        truststorePath(blank: false, nullable: true, maxSize: 2048)
    }
    static mapping = {
        cache true
        batchSize 50
        sort name: "asc"
        interfaceConfig lazy: false
        runs sort: "runDate", order: "desc"
    }

    String toString() { "$name - ${interfaceConfig?.name ?: ''}: ${url ?: 'no url'}" }

    /**
     * Does the ImportTask define an import which is connecting to a remote warehouse?
     * This would imply rules of import which may vary from a local export/import.
     * @return
     */
    def isRemoteImport() {
        return (name != Constants.FILE_BASED_IMPORT_TASK)
    }

    Integer getInDays() {
        if (execInterval && (execInterval % 1440) == 0) {
            return (execInterval / 1440)
        } else {
            return null;
        }
    }

    Integer getInHours() {
        if (execInterval && (execInterval % 60) == 0) {
            return (execInterval / 60)
        } else {
            return null;
        }
    }

    /**
     * Return all related ImportTaskResults
     * @return
     def getRuns() {
     if (!id) return []
     def results
     try {
     log.debug "Searching ImportTaskResult by Task::"
     def criteria = ImportTaskResult.createCriteria()
     results = criteria {
     eq('task', id)
     order('runDate', 'desc')
     }
     }catch (Exception e) {
     log.error "Error accessing ImportTaskResults: ${e.message}"
     }return results
     }
     */

    def prettyPrint() {
        def out = []
        out << "ImportTask::"
        this.metaClass.properties.each {
            if (it.modifiers == 1 && it.name != 'metaClass' &&
                it.name != 'properties' && it.name != 'constraints' &&
                it.name != 'runs') {
                def v = this."${it.name}"
                out << "${it.name} : $v"
            }
        }
        return out.join("\n\t")
    }

    /**
     * Does this task provide enough information to use full client
     * authentication?
     * @return
     */
    public boolean useClientAuthentication() {
        return (keystorePath?.size() > 0 &&
            keystorePass?.size() > 0 &&
            truststorePath?.size() > 0)
    }

    public static void main(String[] args) {
        ImportTask t = new ImportTask(name: 'Test Name', url: 'http://yahoo.com')
        println t.prettyPrint()
    }


}
