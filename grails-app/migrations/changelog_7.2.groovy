/**
 * This changelog include the changesets for the 7.2 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.2-1*
 *  context: create|upgrade-only, 7.2
 */

databaseChangeLog = {

	changeSet(author: "marketplace", dbms: "mssql, mysql, oracle, postgresql", id: "7.2-1", context: "create, 7.2") {
		createTable(tableName: "intent_action") {
			column(autoIncrement: "true", name: "id", type: "java.sql.Types.BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intent_actionPK")
			}

			column(name: "version", type: "java.sql.Types.BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: '${marketplace.profileId}')

			column(name: "created_date", type: "java.sql.Types.DATE")

			column(name: "description", type: "java.sql.Types.VARCHAR(256)")

			column(name: "edited_by_id", type: '${marketplace.profileId}')

			column(name: "edited_date", type: "java.sql.Types.DATE")

			column(name: "title", type: "java.sql.Types.VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "uuid", type: "java.sql.Types.VARCHAR(255)")
		}

        createIndex(indexName: "FKEBCDD397666C6D2", tableName: "intent_action") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FKEBCDD39E31CB353", tableName: "intent_action") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "uuid_unique_1366321689429", tableName: "intent_action", unique: "true") {
            column(name: "uuid")
        }

        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent_action", constraintName: "FKEBCDD397666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent_action", constraintName: "FKEBCDD39E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
	}


	changeSet(author: "marketplace", dbms: "mysql, oracle, postgresql, mssql", id: "7.2-2", context: "create, 7.2") {
		createTable(tableName: "intent_direction") {
            column(name: "created_by_id", type: '${marketplace.profileId}')

            column(name: "created_date", type: "java.sql.Types.DATE")

            column(name: "description", type: "java.sql.Types.VARCHAR(250)")

            column(name: "edited_by_id", type: '${marketplace.profileId}')

            column(name: "edited_date", type: "java.sql.Types.DATE")

            column(autoIncrement: "true", name: "id", type: "java.sql.Types.BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intent_directPK")
			}

			column(name: "title", type: "java.sql.Types.VARCHAR(7)") {
				constraints(nullable: "false")
			}

			column(name: "uuid", type: "java.sql.Types.VARCHAR(255)") {
			}

            column(name: "version", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false")
            }
		}

        createIndex(indexName: "FKC723A59C7666C6D2", tableName: "intent_direction") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FKC723A59CE31CB353", tableName: "intent_direction") {
            column(name: "edited_by_id")
        }

		createIndex(indexName: "title_unique_1366386256451", tableName: "intent_direction", unique: "true") {
			column(name: "title")
		}

		createIndex(indexName: "uuid_unique_1366386256451", tableName: "intent_direction", unique: "true") {
			column(name: "uuid")
		}

        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent_direction", constraintName: "FKC723A59C7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent_direction", constraintName: "FKC723A59CE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")

	}

    changeSet(author: "marketplace", dbms: "mssql, mysql, oracle, postgresql", id: "7.2-3", context: "create, 7.2") {
        createTable(tableName: "intent_data_type") {
            column(name: "id", autoIncrement: "true", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intent_data_tPK")
            }

            column(name: "version", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: '${marketplace.profileId}')

            column(name: "created_date", type: "java.sql.Types.DATE")

            column(name: "description", type: "java.sql.Types.VARCHAR(256)")

            column(name: "edited_by_id", type: '${marketplace.profileId}')

            column(name: "edited_date", type: "java.sql.Types.DATE")

            column(name: "title", type: "java.sql.Types.VARCHAR(256)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "java.sql.Types.VARCHAR(255)")
        }

        createIndex(indexName: "FKEADB30CC7666C6D2", tableName: "intent_data_type") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FKEADB30CCE31CB353", tableName: "intent_data_type") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "uuid_unique_1366398847848", tableName: "intent_data_type", unique: "true") {
            column(name: "uuid")
        }

        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent_data_type", constraintName: "FKEADB30CC7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")

        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent_data_type", constraintName: "FKEADB30CCE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, oracle, postgresql", id: "7.2-4", context: "create, 7.2") {
   		createTable(tableName: "owf_widget_types") {
   			column(name: "id", autoIncrement: "true", type: "java.sql.Types.BIGINT") {
   				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "owf_widget_typePK")
   			}

   			column(name: "version", type: "java.sql.Types.BIGINT") {
   				constraints(nullable: "false")
   			}

   			column(name: "created_by_id", type: '${marketplace.profileId}')

   			column(name: "created_date", type: "java.sql.Types.DATE")

   			column(name: "description", type: "java.sql.Types.VARCHAR(255)") {
   				constraints(nullable: "false")
   			}

   			column(name: "edited_by_id", type: '${marketplace.profileId}')

   			column(name: "edited_date", type: "java.sql.Types.DATE")

   			column(name: "title", type: "java.sql.Types.VARCHAR(256)") {
   				constraints(nullable: "false")
   			}

   			column(name: "uuid", type: "java.sql.Types.VARCHAR(255)")
   		}

   		addColumn(tableName: "owf_properties") {
   			column(name: "owf_widget_type", type: "java.sql.Types.VARCHAR(255)", defaultValue: "standard") {
   				constraints(nullable: "false")
   			}
   		}

   		createIndex(indexName: "FK6AB6A9DF7666C6D2", tableName: "owf_widget_types") {
   			column(name: "created_by_id")
   		}

   		createIndex(indexName: "FK6AB6A9DFE31CB353", tableName: "owf_widget_types") {
   			column(name: "edited_by_id")
   		}

   		createIndex(indexName: "uuid_unique_1366666109930", tableName: "owf_widget_types", unique: "true") {
   			column(name: "uuid")
   		}

   		addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "owf_widget_types", constraintName: "FK6AB6A9DF7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
   		addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "owf_widget_types", constraintName: "FK6AB6A9DFE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
   	}

    changeSet(author: "marketplace", id: "7.2-5", dbms: "mssql, mysql, oracle, postgresql", context: "create, 7.2") {
        addColumn(tableName: "owf_properties") {
            column(name: "universal_name", type: "java.sql.Types.VARCHAR(255)")
        }

        addColumn(tableName: "owf_properties") {
            column(name: "stack_context", type: "java.sql.Types.VARCHAR(200)")
        }

        addColumn(tableName: "owf_properties") {
            column(name: "stack_descriptor", type: "java.sql.Types.CLOB")
        }
    }
}