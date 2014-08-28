
/**
 * This changelog include the changesets for the 1.2 release of the Franchise Store.
 * Use the following conventions in this changelog:
 *
 * 	author: franchise-store
 *  id: 1.2_franchise-1*
 *  context: create|ugrade-only
 */

databaseChangeLog = {

		
	changeSet(author: "franchise-store", dbms: "mysql, oracle, postgresql", id: "1.2_franchise-1", context: "create, 1.2_franchise") {
		createTable(tableName: "application_configuration") {

			column(autoIncrement: "true", name: "id", type: "java.sql.Types.BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "application_configurationPK")
			}

			column(name: "version", type: "java.sql.Types.BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "java.sql.Types.BIGINT")

			column(name: "created_date", type: "java.sql.Types.DATE")

			column(name: "edited_by_id", type: "java.sql.Types.BIGINT")

			column(name: "edited_date", type: "java.sql.Types.DATE")

			column(name: "code", type: "java.sql.Types.VARCHAR(250)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "value", type: "java.sql.Types.VARCHAR(2000)") {
				constraints(nullable: "true")
			}

			column(name: "title", type: "java.sql.Types.VARCHAR(250)") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "java.sql.Types.VARCHAR(2000)") {
				constraints(nullable: "true")
			}
						
			column(name: "type", type: "java.sql.Types.VARCHAR(250)") {
				constraints(nullable: "false")
			}
			
			column(name: "group_name", type: "java.sql.Types.VARCHAR(250)") {
				constraints(nullable: "false")
			}

			column(name: "sub_group_name", type: "java.sql.Types.VARCHAR(250)") {
				constraints(nullable: "true")
			}
			
			column(name: "mutable", type: "java.sql.Types.BOOLEAN") {
				constraints(nullable: "false")
			}
			
			column(name: "sub_group_order", type: "java.sql.Types.BIGINT")
						
		}

	}

    changeSet(author: "franchise-store", dbms: "mssql", id: "1.2_franchise-1", context: "create, 1.2_franchise") {
        createTable(tableName: "application_configuration") {

            column(autoIncrement: "true", name: "id", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "application_configurationPK")
            }

            column(name: "version", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "numeric(19, 0)")

            column(name: "created_date", type: "java.sql.Types.DATE")

            column(name: "edited_by_id", type: "numeric(19, 0)")

            column(name: "edited_date", type: "java.sql.Types.DATE")

            column(name: "code", type: "java.sql.Types.VARCHAR(250)") {
                constraints(nullable: "false", unique: "true")
            }

            column(name: "value", type: "java.sql.Types.VARCHAR(2000)") {
                constraints(nullable: "true")
            }

            column(name: "title", type: "java.sql.Types.VARCHAR(250)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "java.sql.Types.VARCHAR(2000)") {
                constraints(nullable: "true")
            }

            column(name: "type", type: "java.sql.Types.VARCHAR(250)") {
                constraints(nullable: "false")
            }

            column(name: "group_name", type: "java.sql.Types.VARCHAR(250)") {
                constraints(nullable: "false")
            }

            column(name: "sub_group_name", type: "java.sql.Types.VARCHAR(250)") {
                constraints(nullable: "true")
            }

            column(name: "mutable", type: "java.sql.Types.BOOLEAN") {
                constraints(nullable: "false")
            }
			
			column(name: "sub_group_order", type: "java.sql.Types.BIGINT")
        }
    }

    changeSet(author: "franchise-store", id: "1.2_franchise-2", dbms: "mssql, mysql, oracle, postgresql", context: "create, 1.2_franchise") {
        createIndex(indexName: "FKFC9C0477666C6D2", tableName: "application_configuration") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FKFC9C047E31CB353", tableName: "application_configuration") {
            column(name: "edited_by_id")
        }

        createIndex(tableName: "application_configuration", indexName: "app_config_group_name_idx") {
            comment("Create index for application_configuration.group_name")
            column(name: "group_name")
        }

        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "application_configuration", constraintName: "FKFC9C0477666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "application_configuration", constraintName: "FKFC9C047E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }
	
}