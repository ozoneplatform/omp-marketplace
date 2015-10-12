/**
 * This changelog include the changesets for the 7.9.0 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.9.0-1*
 *  context: create|upgrade-only, 7.9.0
 */

databaseChangeLog = {

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb', id: '7.9.0-2', context: 'create, 7.9.0') {
        createTable(tableName: 'contact_type') {
            column(autoIncrement: 'true', name: 'id', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'contact_typePK')
            }

            column(name: 'version', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }

            column(name: 'created_by_id', type: '${marketplace.profileId}')

            column(name: 'created_date', type: 'java.sql.Types.DATE')

            column(name: 'edited_by_id', type: '${marketplace.profileId}')

            column(name: 'edited_date', type: 'java.sql.Types.DATE')

            column(name: 'required', type: 'java.sql.Types.BOOLEAN') {
                constraints(nullable: 'false')
            }

            column(name: 'title', type: 'java.sql.Types.VARCHAR(50)') {
                constraints(nullable: 'false')
            }
        }
    }

    changeSet(author: 'marketplace', dbms: 'mysql, oracle, postgresql, hsqldb', id: '7.9.0-3', context: 'create, 7.9.0') {

        createTable(tableName: "contact") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "contactPK")
            }

            column(name: "version", type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }

            column(name: "email", type: 'java.sql.Types.VARCHAR(100)') {
                constraints(nullable: "false")
            }

            column(name: "name", type: 'java.sql.Types.VARCHAR(100)') {
                constraints(nullable: "false")
            }

            column(name: "organization", type: 'java.sql.Types.VARCHAR(100)')

            column(name: "secure_phone", type: 'java.sql.Types.VARCHAR(50)')

            column(name: "type_id", type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "unsecure_phone", type: 'java.sql.Types.VARCHAR(50)')

            column(name: 'created_by_id', type: '${marketplace.profileId}')

            column(name: 'created_date', type: 'java.sql.Types.DATE')

            column(name: 'edited_by_id', type: '${marketplace.profileId}')

            column(name: 'edited_date', type: 'java.sql.Types.DATE')
        }

        createIndex(indexName: "FK4C2BB7F97666C6D2", tableName: "contact_type") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FK4C2BB7F9E31CB353", tableName: "contact_type") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "FK38B72420C7E5C662", tableName: "contact") {
            column(name: "service_item_id")
        }

        createIndex(indexName: "FK38B72420BA3FC877", tableName: "contact") {
            column(name: "type_id")
        }

        createIndex(indexName: "FK38B72420E31CB353", tableName: "contact") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "FK38B724207666C6D2", tableName: "contact") {
            column(name: "created_by_id")
        }

        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "contact_type", constraintName: "FK4C2BB7F97666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "contact_type", constraintName: "FK4C2BB7F9E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "contact", constraintName: "FK38B724207666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "contact", constraintName: "FK38B72420E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "contact", constraintName: "FK38B72420BA3FC877", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "contact_type", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "contact", constraintName: "FK38B72420C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }
	
    changeSet(author: 'marketplace', dbms: 'mssql', id: '7.9.0-3', context: 'create, 7.9.0') {

        createTable(tableName: "contact") {
			column(autoIncrement: "true", name: "id", type: "numeric(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "contactPK")
			}

            column(name: "version", type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }

            column(name: "email", type: 'java.sql.Types.VARCHAR(100)') {
                constraints(nullable: "false")
            }

            column(name: "name", type: 'java.sql.Types.VARCHAR(100)') {
                constraints(nullable: "false")
            }

            column(name: "organization", type: 'java.sql.Types.VARCHAR(100)')

            column(name: "secure_phone", type: 'java.sql.Types.VARCHAR(50)')

            column(name: "type_id", type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: 'numeric(19,0)') {
                constraints(nullable: "false")
            }

            column(name: "unsecure_phone", type: 'java.sql.Types.VARCHAR(50)')

            column(name: 'created_by_id', type: '${marketplace.profileId}')

            column(name: 'created_date', type: 'java.sql.Types.DATE')

            column(name: 'edited_by_id', type: '${marketplace.profileId}')

            column(name: 'edited_date', type: 'java.sql.Types.DATE')
        }

        createIndex(indexName: "FK4C2BB7F97666C6D2", tableName: "contact_type") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FK4C2BB7F9E31CB353", tableName: "contact_type") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "FK38B72420C7E5C662", tableName: "contact") {
            column(name: "service_item_id")
        }

        createIndex(indexName: "FK38B72420BA3FC877", tableName: "contact") {
            column(name: "type_id")
        }

        createIndex(indexName: "FK38B72420E31CB353", tableName: "contact") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "FK38B724207666C6D2", tableName: "contact") {
            column(name: "created_by_id")
        }

        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "contact_type", constraintName: "FK4C2BB7F97666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "contact_type", constraintName: "FK4C2BB7F9E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "contact", constraintName: "FK38B724207666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "contact", constraintName: "FK38B72420E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "contact", constraintName: "FK38B72420BA3FC877", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "contact_type", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "contact", constraintName: "FK38B72420C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, oracle, hsqldb', id: '7.9.0-4', context: 'create, 7.9.0') {
        createIndex(indexName: "title_unique_1389723125532", tableName: "contact_type", unique: "true") {
            column(name: "title")
        }
    }
}
