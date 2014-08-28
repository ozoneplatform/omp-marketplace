/**
 * This changelog include the changesets for Apps Mall development performed in parallel
 * with 5.0 release of marketplace. It includes a context "aml" in order to provide a hook
 * for creating a 5.0 to 5.0 AML upgrade script. Use the following conventions in this log:
 * 
 * 	author: aml-marketplace
 *  id: aml_5.0-*
 *  context: create|ugrade-only, aml_5.0
 */

databaseChangeLog = {

	changeSet(author: "aml-marketplace", dbms: "mysql, oracle, postgresql", id: "aml_5.0-1", context: "create, aml_5.0") {
		createTable(tableName: "score_card") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "score_cardPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "bigint")

			column(name: "created_date", type: "datetime")

			column(name: "edited_by_id", type: "bigint")

			column(name: "edited_date", type: "datetime")

			column(name: "score", type: "float(19)") {
				constraints(nullable: "false")
			}
		}
	}
	
	//Need to match the datatypes in the foreign key columns for SQL Server
	changeSet(author: "aml-marketplace", dbms: "mssql", id: "aml_5.0-1", context: "create, aml_5.0") {
		createTable(tableName: "score_card") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "score_cardPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "numeric(19, 0)")

			column(name: "created_date", type: "datetime")

			column(name: "edited_by_id", type: "numeric(19, 0)")

			column(name: "edited_date", type: "datetime")

			column(name: "score", type: "float(19)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "aml-marketplace", dbms: "mysql, oracle, postgresql", id: "aml_5.0-2", context: "create, aml_5.0") {
		createTable(tableName: "score_card_item") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sc_itemPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "bigint")

			column(name: "created_date", type: "datetime")

			column(name: "description", type: "varchar(500)") {
				constraints(nullable: "false")
			}

			column(name: "edited_by_id", type: "bigint")

			column(name: "edited_date", type: "datetime")

			column(name: "is_standard_question", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "question", type: "varchar(250)") {
				constraints(nullable: "false")
			}

			column(name: "weight", type: "float(19)")
		}
	}
	
	//Need to match the datatypes in the foreign key columns for SQL Server
	changeSet(author: "aml-marketplace", dbms: "mssql", id: "aml_5.0-2", context: "create, aml_5.0") {
		createTable(tableName: "score_card_item") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sc_itemPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "numeric(19, 0)")

			column(name: "created_date", type: "datetime")

			column(name: "description", type: "varchar(500)") {
				constraints(nullable: "false")
			}

			column(name: "edited_by_id", type: "numeric(19, 0)")

			column(name: "edited_date", type: "datetime")

			column(name: "is_standard_question", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "question", type: "varchar(250)") {
				constraints(nullable: "false")
			}

			column(name: "weight", type: "float(19)")
		}
	}

	changeSet(author: "aml-marketplace", dbms: "mysql, oracle, postgresql", id: "aml_5.0-3", context: "create, aml_5.0") {
		createTable(tableName: "score_card_item_response") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sc_responsePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "bigint")

			column(name: "created_date", type: "datetime")

			column(name: "edited_by_id", type: "bigint")

			column(name: "edited_date", type: "datetime")

			column(name: "is_satisfied", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "score_card_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "score_card_item_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}
	
	//Need to match the datatypes in the foreign key columns for SQL Server
	changeSet(author: "aml-marketplace", dbms: "mssql", id: "aml_5.0-3", context: "create, aml_5.0") {
		createTable(tableName: "score_card_item_response") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sc_responsePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "numeric(19, 0)")

			column(name: "created_date", type: "datetime")

			column(name: "edited_by_id", type: "numeric(19, 0)")

			column(name: "edited_date", type: "datetime")

			column(name: "is_satisfied", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "score_card_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "score_card_item_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "aml-marketplace", id: "aml_5.0-4", dbms: "mssql, mysql, oracle, postgresql", context: "create, aml_5.0") {
		addColumn(tableName: "service_item") {
			column(name: "score_card_id", type: "bigint")
		}
	}
	
	changeSet(author: "aml-marketplace", id: "aml_5.0-5", dbms: "mssql, mysql, oracle, postgresql", context: "create, aml_5.0") {
		createIndex(indexName: "FK5E60409D7666C6D2", tableName: "score_card") {
			column(name: "created_by_id")
		}
		
		createIndex(indexName: "FK5E60409DE31CB353", tableName: "score_card") {
			column(name: "edited_by_id")
		}
		
		createIndex(indexName: "FKE51CCD757666C6D2", tableName: "score_card_item") {
			column(name: "created_by_id")
		}
		
		createIndex(indexName: "FKE51CCD75E31CB353", tableName: "score_card_item") {
			column(name: "edited_by_id")
		}
		
		createIndex(indexName: "FK80A6CBCB190E00BC", tableName: "score_card_item_response") {
			column(name: "score_card_id")
		}
		
		createIndex(indexName: "FK80A6CBCB7666C6D2", tableName: "score_card_item_response") {
			column(name: "created_by_id")
		}
		
		createIndex(indexName: "FK80A6CBCBE31CB353", tableName: "score_card_item_response") {
			column(name: "edited_by_id")
		}
		
		createIndex(indexName: "FK80A6CBCBEF469C97", tableName: "score_card_item_response") {
			column(name: "score_card_item_id")
		}
		
		createIndex(indexName: "FK1571565D190E00BC", tableName: "service_item") {
			column(name: "score_card_id")
		}

	}

	changeSet(author: "aml-marketplace", id: "aml_5.0-6", dbms: "mssql, mysql, oracle, postgresql", context: "create, aml_5.0") {
		addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "score_card", constraintName: "FK5E60409D7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "score_card", constraintName: "FK5E60409DE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "score_card_item", constraintName: "FKE51CCD757666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "score_card_item", constraintName: "FKE51CCD75E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "score_card_item_response", constraintName: "FK80A6CBCB7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "score_card_item_response", constraintName: "FK80A6CBCBE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "score_card_id", baseTableName: "score_card_item_response", constraintName: "FK80A6CBCB190E00BC", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "score_card", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "score_card_item_id", baseTableName: "score_card_item_response", constraintName: "FK80A6CBCBEF469C97", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "score_card_item", referencesUniqueColumn: "false")
		addForeignKeyConstraint(baseColumnNames: "score_card_id", baseTableName: "service_item", constraintName: "FK1571565D190E00BC", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "score_card", referencesUniqueColumn: "false")
	}

}
