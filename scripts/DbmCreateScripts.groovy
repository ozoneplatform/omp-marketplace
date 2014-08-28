import groovy.sql.Sql

includeTargets << new File("$databaseMigrationPluginDir/scripts/_DatabaseMigrationCommon.groovy")

target(dbmCreateScripts: "Convenience script to generate the sql scripts for current environment") {
    depends dbmInit
    if (!okToWrite()) return

    def dsConfig = config.dataSource
    def sql = Sql.newInstance(dsConfig.url, dsConfig.username, dsConfig.password, dsConfig.driverClassName)
    def scripts = ['create.sql', 'upgrade.sql', 'temp.sql', 'internal.sql', 'defaultData.sql']
    def (createFile, upgradeFile, tempFile, internalFile, dataCreateFile) = scripts
    def upgradeVersion = metadata['app.base.version']

    ant.delete(quiet: true) { fileset(dir: '.') { scripts.each { include(name: it) }}}

    doAndClose {
        liquibase.dropAll()
        sql.execute('drop table DATABASECHANGELOG')
        sql.execute('drop table DATABASECHANGELOGLOCK')
        liquibase.update 'create', new PrintWriter(new PrintStream(createFile))
        liquibase.changeLogSync null, new PrintWriter(new PrintStream(internalFile))
        liquibase.clearCheckSums() //regenerates the liquibase tables
        liquibase.update 'defaultData', new PrintWriter(new PrintStream(dataCreateFile))
        liquibase.changeLogSync null, new PrintWriter(new PrintStream(internalFile))
        liquibase.clearCheckSums() //regenerates the liquibase tables
        liquibase.changeLogSync 'upgrade-only', new PrintWriter(new PrintStream(tempFile))
        liquibase.update "${upgradeVersion}", new PrintWriter(new PrintStream(upgradeFile))
    }

    //temp file contains the sync statements for upgrade-only changesets - this marks them as ran in the create script
    ant.concat(destfile: createFile, append: 'yes') { fileset(file: tempFile) }

    //file not empty if it contains lines that are neither comments nor blank
    def fileNotEmpty = { fileName ->
        new File(fileName).withReader { it.readLines() }.grep { it && !it.startsWith('--') } as Boolean
    }

    scripts.each {
        if(!fileNotEmpty(it)) ant.delete(file: it)
    }

    ant.delete(file: tempFile, quiet: true)
}

setDefaultTarget(dbmCreateScripts)
