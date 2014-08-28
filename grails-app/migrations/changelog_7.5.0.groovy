/**
 * This changelog include the changesets for the 7.5 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.5.0-1*
 *  context: create|upgrade-only, 7.5.0
 */

databaseChangeLog = {
    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb', id: '7.5.0-1',
            context: 'create, 7.5.0') {
        createTable(tableName: 'agency') {
            column(name: 'id', autoIncrement: 'true', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'agencyPK')
            }

            column(name: 'title', type: 'varchar(255)') {
                constraints(unique: 'true')
            }

            column(name: 'icon_url', type: 'varchar(2083)') {
                constraints(nullable: false)
            }

            column(name: "created_by_id", type: '${marketplace.profileId}')
            column(name: "created_date", type: "java.sql.Types.DATE")
            column(name: "edited_by_id", type: '${marketplace.profileId}')
            column(name: "edited_date", type: "java.sql.Types.DATE")
            column(name: "version", type: "java.sql.Types.BIGINT",
                    defaultValueNumeric: 0) {
                constraints(nullable: "false")
            }

        }

        addColumn(tableName: 'service_item') {
            column(name: 'agency_id', type: 'java.sql.Types.BIGINT')
        }

        addForeignKeyConstraint(baseTableName: 'service_item', baseColumnNames: 'agency_id',
            referencedTableName: 'agency', referencedColumnNames: 'id',
            onDelete: 'SET NULL', constraintName: 'SERVICE_ITEM_AGENCY_FK')

    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql', id: '7.5.0-2',
            context: 'upgrade-only, 7.5.0') {
        //insert existing agencies from the service items
        //NOTE: data from application configurations is not carried over
        sql("""
            INSERT INTO agency (title, icon_url)
                SELECT agency, MIN(agency_icon)
                FROM service_item
                WHERE agency IS NOT NULL AND agency <> ''
                GROUP BY agency
        """)

        //link the service items to the correct new Agency rows
        sql("""
            UPDATE service_item AS svc
                    INNER JOIN agency AS a ON svc.agency = a.title
                SET svc.agency_id = a.id
        """)

    }

    changeSet(author: 'marketplace', dbms: 'postgresql', id: '7.5.0-2',
            context: 'upgrade-only, 7.5.0') {
        //insert existing agencies from the service items
        //NOTE: data from application configurations is not carried over
        sql("""
            INSERT INTO agency (title, icon_url)
                SELECT agency, MIN(agency_icon)
                FROM service_item
                WHERE agency IS NOT NULL AND agency <> ''
                GROUP BY agency
        """)

        //link the service items to the correct new Agency rows
        sql("""
            UPDATE service_item
                SET agency_id = a.id
                FROM agency AS a
                WHERE agency = a.title;
        """)
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, hsqldb', id: '7.5.0-3',
            context: 'create, 7.5.0') {
        dropColumn(tableName: 'service_item', columnName: 'agency')
        dropColumn(tableName: 'service_item', columnName: 'agency_icon')
    }
}
