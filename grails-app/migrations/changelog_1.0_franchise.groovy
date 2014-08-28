/**
 * This changelog include the changesets for the 1.0 release of the Franchise Store.
 * Use the following conventions in this changelog:
 *
 * 	author: franchise-store
 *  id: 1.0_franchise-*
 *  context: create|ugrade-only
 */

databaseChangeLog = {
    // Add service item agency fields    AML-680

    changeSet(author: "franchise-store", id: "1.0_franchise-1", dbms: "mssql, mysql, oracle, postgresql", context: "create, 1.0_franchise") {
        addColumn(tableName: "service_item") {
            column(name: "agency", type: "varchar(255)") {
            }
        }

        addColumn(tableName: "service_item") {
            column(name: "agency_icon", type: "varchar(255)")
        }
    }

    // Add service item is_outside field    mysql  AML-679
    changeSet(author: "franchise-store", dbms: "mysql", id: "1.0_franchise-2", context: "create, 1.0_franchise") {

        addColumn(tableName: "service_item") {
            column(name: "is_outside", type: "bit")
        }

    }

    // Add service item is_outside field  oracle  AML-679
    changeSet(author: "franchise-store", dbms: "oracle", id: "1.0_franchise-2", context: "create, 1.0_franchise") {

        addColumn(tableName: "service_item") {
            column(name: "is_outside", type: "number(1,0)")
        }

    }

    // Add service item is_outside field  mssql  AML-679
    changeSet(author: "franchise-store", dbms: "mssql", id: "1.0_franchise-2", context: "create, 1.0_franchise") {

        addColumn(tableName: "service_item") {
            column(name: "is_outside", type: "tinyint")
        }

    }

    // Add service item is_outside field  postgres  AML-679
    changeSet(author: "franchise-store", dbms: "postgresql", id: "1.0_franchise-2", context: "create, 1.0_franchise") {

        addColumn(tableName: "service_item") {
            column(name: "is_outside", type: "bool")
        }

    }

    // Misc changes - tech debt
    changeSet(author: "franchise-store", id: "1.0_franchise-3", dbms: "mssql, mysql, oracle", context: "create, 1.0_franchise") {
        dropIndex(indexName: "change_detail_object_idx", tableName: "change_detail")
        dropIndex(indexName: "change_log_object_idx", tableName: "change_log")
        dropIndex(indexName: "cfd_types_cfd_idx", tableName: "custom_field_definition_types")
        dropIndex(indexName: "rel_act_log_mod_rel_act_idx", tableName: "relationship_activity_log")
        dropIndex(indexName: "rel_si_rel_items_id_idx", tableName: "relationship_service_item")
        dropIndex(indexName: "si_act_si_ver_idx", tableName: "service_item_activity")
    }

    // Update custom fields to handle multiple values for drop-down, add flag to mark a custom field as permanent
    // (i.e., not deletable)  AML-726   mysql
    changeSet(author: "franchise-store", dbms: "mysql", id: "1.0_franchise-4", context: "create, 1.0_franchise") {
        createTable(tableName: "drop_down_cf_field_value") {
            column(name: "drop_down_cf_field_value_id", type: "bigint")
            column(name: "field_value_id", type: "bigint")
            column(name: "field_value_list_idx", type: "integer")
        }

        addColumn(tableName: "custom_field_definition") {
            column(name: "is_permanent", type: "bit") {
            }
        }

        addColumn(tableName: "drop_down_cfd") {
            column(name: "is_multi_select", type: "bit")
        }

        createIndex(indexName: "FK2627FFDDA5BD888", tableName: "drop_down_cf_field_value") {
            column(name: "field_value_id")
        }

        addForeignKeyConstraint(baseColumnNames: "field_value_id", baseTableName: "drop_down_cf_field_value", constraintName: "FK2627FFDDA5BD888", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "field_value", referencesUniqueColumn: "false")

    }


    // Update custom fields to handle multiple values for drop-down, add flag to mark a custom field as permanent
    // (i.e., not deletable)  AML-726  oracle
    changeSet(author: "franchise-store", dbms: "oracle", id: "1.0_franchise-4", context: "create, 1.0_franchise") {
        createTable(tableName: "drop_down_cf_field_value") {
            column(name: "drop_down_cf_field_value_id", type: "number(19,0)")
            column(name: "field_value_id", type: "number(19,0)")
            column(name: "field_value_list_idx", type: "number(10,0)")
        }

        addColumn(tableName: "custom_field_definition") {
            column(name: "is_permanent", type: "number(1,0)") {
            }
        }

        addColumn(tableName: "drop_down_cfd") {
            column(name: "is_multi_select", type: "number(1,0)")
        }

        createIndex(indexName: "FK2627FFDDA5BD888", tableName: "drop_down_cf_field_value") {
            column(name: "field_value_id")
        }

        addForeignKeyConstraint(baseColumnNames: "field_value_id", baseTableName: "drop_down_cf_field_value", constraintName: "FK2627FFDDA5BD888", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "field_value", referencesUniqueColumn: "false")

    }

    // Update custom fields to handle multiple values for drop-down, add flag to mark a custom field as permanent
    // (i.e., not deletable)  AML-726  mssql
    changeSet(author: "franchise-store", dbms: "mssql", id: "1.0_franchise-4", context: "create, 1.0_franchise") {
        createTable(tableName: "drop_down_cf_field_value") {
            column(name: "drop_down_cf_field_value_id", type: "numeric(19,0)")
            column(name: "field_value_id", type: "numeric(19,0)")
            column(name: "field_value_list_idx", type: "int")
        }

        addColumn(tableName: "custom_field_definition") {
            column(name: "is_permanent", type: "boolean") {
            }
        }

        addColumn(tableName: "drop_down_cfd") {
            column(name: "is_multi_select", type: "boolean")
        }

        createIndex(indexName: "FK2627FFDDA5BD888", tableName: "drop_down_cf_field_value") {
            column(name: "field_value_id")
        }

        addForeignKeyConstraint(baseColumnNames: "field_value_id", baseTableName: "drop_down_cf_field_value", constraintName: "FK2627FFDDA5BD888", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "field_value", referencesUniqueColumn: "false")

    }

    // Update custom fields to handle multiple values for drop-down, add flag to mark a custom field as permanent
    // (i.e., not deletable)  AML-726   postgres
    changeSet(author: "franchise-store", dbms: "postgresql", id: "1.0_franchise-4", context: "create, 1.0_franchise") {
        createTable(tableName: "drop_down_cf_field_value") {
            column(name: "drop_down_cf_field_value_id", type: "int8")
            column(name: "field_value_id", type: "int8")
            column(name: "field_value_list_idx", type: "int4")
        }

        addColumn(tableName: "custom_field_definition") {
            column(name: "is_permanent", type: "bool") {
            }
        }

        addColumn(tableName: "drop_down_cfd") {
            column(name: "is_multi_select", type: "bool")
        }

        createIndex(indexName: "FK2627FFDDA5BD888", tableName: "drop_down_cf_field_value") {
            column(name: "field_value_id")
        }

        addForeignKeyConstraint(baseColumnNames: "field_value_id", baseTableName: "drop_down_cf_field_value", constraintName: "FK2627FFDDA5BD888", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "field_value", referencesUniqueColumn: "false")

    }

    // Misc changes - tech debt
    changeSet(author: "franchise-store", dbms: "mysql", id: "1.0_franchise-5", context: "create, 1.0_franchise") {

        addNotNullConstraint(columnDataType: "varchar(256)", columnName: "username", tableName: "profile")
        addNotNullConstraint(columnDataType: "varchar(256)", columnName: "tech_poc", tableName: "service_item")
        addNotNullConstraint(columnDataType: "varchar(256)", columnName: "title", tableName: "service_item")
        addNotNullConstraint(columnDataType: "varchar(256)", columnName: "version_name", tableName: "service_item")

    }

    // Misc changes - tech debt
    changeSet(author: "franchise-store", dbms: "mssql", id: "1.0_franchise-5", context: "create, 1.0_franchise") {

        /**  The commented out portions in this changeset are one way to handle the remaining missing
         *   constraints, however it has been deemed at this time to add too much of a testing burden
         *   for too little benefit
        sql("""
            declare @sqlDropConstraint NVARCHAR(1000)
            select @sqlDropConstraint = 'ALTER TABLE ' + tbl.name + ' DROP CONSTRAINT ' + idx.name
            from sys.indexes idx inner join
                sys.tables tbl on idx.object_id = tbl.object_id inner join
                sys.index_columns idxCol on idx.index_id = idxCol.index_id inner join
                sys.columns col on idxCol.column_id = col.column_id
            where tbl.name = 'profile' and col.name = 'username' and is_unique_constraint = 1
            group by idx.name, tbl.name
            order by idx.name

            exec sp_executeSql @sqlDropConstraint
        """)

        sql("""
            declare @sqlDropConstraint NVARCHAR(1000)
            select @sqlDropConstraint = 'ALTER TABLE ' + tbl.name + ' DROP CONSTRAINT ' + idx.name
            from sys.indexes idx inner join
                sys.tables tbl on idx.object_id = tbl.object_id inner join
                sys.index_columns idxCol on idx.index_id = idxCol.index_id inner join
                sys.columns col on idxCol.column_id = col.column_id
            where tbl.name = 'ext_profile' and col.name = 'system_uri' and is_unique_constraint = 1
            group by idx.name, tbl.name
            order by idx.name

            exec sp_executeSql @sqlDropConstraint
        """)  */

        //addNotNullConstraint(columnDataType: "nvarchar(256)", columnName: "username", tableName: "profile")
        addNotNullConstraint(columnDataType: "nvarchar(256)", columnName: "tech_poc", tableName: "service_item")
        addNotNullConstraint(columnDataType: "nvarchar(256)", columnName: "title", tableName: "service_item")
        addNotNullConstraint(columnDataType: "nvarchar(256)", columnName: "version_name", tableName: "service_item")
        //addNotNullConstraint(columnDataType: "nvarchar(255)", columnName: "system_uri", tableName: "ext_profile")
        //addUniqueConstraint(tableName: "profile", columnNames: "username", constraintName: "uq_constraint_username")
        //addUniqueConstraint(tableName: "ext_profile", columnNames: "system_uri, external_id", constraintName: "uq_constraint_system_uri")

    }

    // Misc changes - tech debt
    changeSet(author: "franchise-store", dbms: "oracle, postgresql", id: "1.0_franchise-6", context: "create, 1.0_franchise") {
        dropNotNullConstraint(tableName: "ext_service_item", columnName: "system_uri")
    }
}