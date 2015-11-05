/* main changelog file
 *
 * When adding changelogs, add the app.base.version to the dbVersion list and name
 * the changelog "changelog_${app.base.version}.groovy".
 *
 * When using the dbm-* liquibase plugin scripts you can now specify which
 * changelogs you want included by using one of the following command line
 * arguments:
 *
 *      -DdbmVersion=someVersion to include changelogs for up to and including someVersion
 *      -DdbmChangelog=someVersion to include the changelog for that version only
 *
 * Use only one or the other, or neither. Using neither (the default) will include all changelogs.
 */

def dbmVersion = System.properties.dbmVersion
def dbmChangelog = System.properties.dbmChangelog

databaseChangeLog = {
    def includeChangelogs = { filePrefix ->
        def dbmVersions = ['2.3.1', '2.4.0', '2.5.0', '5.0',
                '5.0_aml', '1.0_franchise', '1.2_franchise',
                '7.1', '7.2', '7.3.0', '7.5.0', '7.6.0',
                '7.9.0', '7.10.0', '7.11.0', '7.12.0', '7.16.0']

        if(dbmVersion && dbmVersions.contains(dbmVersion)) {
            dbmVersions[0..dbmVersions.indexOf(dbmVersion)].each { version ->
                include file: "${filePrefix}_${version}.groovy"
            }
        } else if(dbmChangelog && dbmVersions.contains(dbmChangelog)) {
            include file: "${filePrefix}_${dbmChangelog}.groovy"
        }
        else {
            dbmVersions.each { version ->
                include file: "${filePrefix}_${version}.groovy"
            }
        }
    }

    // On MS SQL Server, we use numeric(19, 0) for the profile id, but we use bigint everywhere else. Use this property like:
    // 	    column(name: "edited_by_id", type: '${marketplace.profileId}')
    // but only use SINGLE QUOTES around the ${}, because Spring needs to do the interpretation, not Groovy.
    property([name:"marketplace.profileId", value:"java.sql.Types.BIGINT", dbms:"postgresql, mysql, oracle, hsqldb, h2"])
    property([name:"marketplace.profileId", value:"numeric(19,0)", dbms:"mssql"])
    property([name:"marketplace.legacyIdField", value:"java.sql.Types.BIGINT", dbms:"postgresql, mysql, oracle, hsqldb, h2"])
    property([name:"marketplace.legacyIdField", value:"numeric(19,0)", dbms:"mssql"])
    property([name:"appconfig.valColumn", value:"VALUE", dbms:"hsqldb"])
    property([name:"appconfig.valColumn", value:"value", dbms:"mysql, oracle, postgresql, mssql, h2"])
	property([name:"currentDateFunction", value:"SYSTIMESTAMP", dbms:"oracle"])
	property([name:"currentDateFunction", value:"NOW()", dbms:"mysql, postgresql, h2"])
	property([name:"currentDateFunction", value:"GetDate()", dbms:"mssql"])


    includeChangelogs('changelog')
    include file: 'default_data.groovy'
}
