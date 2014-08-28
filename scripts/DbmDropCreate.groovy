includeTargets << new File("$databaseMigrationPluginDir/scripts/_DatabaseMigrationCommon.groovy")

target(dbmCreateDrop: "Convenience script to drop and create a new schema for the current environment") {
	ant.delete(dir: "${basedir}/data")

    depends dbmInit

    doAndClose {
        liquibase.dropAll()
        liquibase.update 'create'
				liquibase.update 'defaultData'
    }

    ant.delete(dir: "${basedir}/data")
}

setDefaultTarget(dbmCreateDrop)
