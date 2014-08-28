databaseChangeLog = {

	//use 'upgrade' context for new change sets which change the schema or manipulating data related to a schema change
	//use 'create' context creating a new database
	//favor making database agnostic changeSets - however if needed the dbms attribute can be set to make a db specific changeset

	changeSet(author: "marketplace", id: "2.4.0-1", dbms: "mysql", context: "create, upgrade, 2.4.0") {
		comment("Create affiliated_marketplace table")
		createTable(tableName: "affiliated_marketplace") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true")
			}
			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}
			column(name: "active", type: "integer") {
				constraints(nullable: "false")
			}
			column(name: "created_by_id", type: "bigint")
		
			column(name: "created_date", type: "datetime")
		
			column(name: "edited_by_id", type: "bigint")
		
			column(name: "edited_date", type: "datetime")
		
			column(name: "icon_id", type: "bigint")
		
			column(name: "name", type: "varchar(50)") {
				constraints(nullable: "false")
			}
			column(name: "server_url", type: "varchar(2083)") {
				constraints(nullable: "false")
			}
			column(name: "timeout", type: "bigint")
		}
	}

	changeSet(author: "marketplace", id: "2.4.0-1", dbms: "oracle", context: "create, upgrade, 2.4.0") {
		comment("Create affiliated_marketplace table")
		createTable(tableName: "affiliated_marketplace") {
			column(autoIncrement: "true", name: "id", type: "number(19,0)") {
				constraints(nullable: "false", primaryKey: "true")
			}
			column(name: "version", type: "number(19,0)") {
				constraints(nullable: "false")
			}
			column(name: "active", type: "number(10,0)") {
				constraints(nullable: "false")
			}
			column(name: "created_by_id", type: "number(19,0)")
		
			column(name: "created_date", type: "TIMESTAMP (6)")
		
			column(name: "edited_by_id", type: "number(19,0)")
		
			column(name: "edited_date", type: "TIMESTAMP (6)")
		
			column(name: "icon_id", type: "number(19,0)")
		
			column(name: "name", type: "varchar(50 char)") {
				constraints(nullable: "false")
			}
			column(name: "server_url", type: "varchar(2083 char)") {
				constraints(nullable: "false")
			}
			column(name: "timeout", type: "number(19,0)")
		}
	}

	changeSet(author: "marketplace", id: "2.4.0-1", dbms: "postgresql", context: "create, upgrade, 2.4.0") {
		comment("Create affiliated_marketplace table")
		createTable(tableName: "affiliated_marketplace") {
			column(autoIncrement: "false", name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true")
			}
			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}
			column(name: "active", type: "integer") {
				constraints(nullable: "false")
			}
			column(name: "created_by_id", type: "int8")
		
			column(name: "created_date", type: "timestamp")
		
			column(name: "edited_by_id", type: "int8")
		
			column(name: "edited_date", type: "timestamp")
		
			column(name: "icon_id", type: "int8")
		
			column(name: "name", type: "varchar(50)") {
				constraints(nullable: "false")
			}
			column(name: "server_url", type: "varchar(2083)") {
				constraints(nullable: "false")
			}
			column(name: "timeout", type: "int8")
		}
		
		modifySql {
			replace (replace: "WITH", with: "WITHOUT")
		}
	}

	changeSet(author: "marketplace", id: "2.4.0-1", dbms: "mssql", context: "create, upgrade, 2.4.0") {
		comment("Create affiliated_marketplace table")
		createTable(tableName: "affiliated_marketplace") {
			column(autoIncrement: "true", name: "id", type: "numeric(19,0)") {
				constraints(nullable: "false", primaryKey: "true")
			}
			column(name: "version", type: "numeric(19,0)") {
				constraints(nullable: "false")
			}
			column(name: "active", type: "integer") {
				constraints(nullable: "false")
			}
			column(name: "created_by_id", type: "numeric(19,0)")
		
			column(name: "created_date", type: "datetime")
		
			column(name: "edited_by_id", type: "numeric(19,0)")
		
			column(name: "edited_date", type: "datetime")
		
			column(name: "icon_id", type: "numeric(19,0)")
		
			column(name: "name", type: "nvarchar(50)") {
				constraints(nullable: "false")
			}
			column(name: "server_url", type: "nvarchar(2083)") {
				constraints(nullable: "false")
			}
			column(name: "timeout", type: "numeric(19,0)")
		}
	}
	
	// Handle disabling foreign key check for mysql when modifying FK constraints, bring it up after
	changeSet(author: "marketplace", id: "2.4.0-2.1", dbms: "mysql", context: "create, upgrade, 2.4.0") {
		comment( "Disable foreign key check for mysql when modifying FK constraints, Enable after done modifying." )
		sql ( """
			SET FOREIGN_KEY_CHECKS = 0;
		""")
	}

	// Create affiliated_marketplace table
	changeSet(author: "marketplace", id: "2.4.0-2", dbms: "mssql, mysql, oracle, postgresql", context: "create, upgrade, 2.4.0") {
		
		comment("Create FK constraints for affiliated_marketplace table ")
		
		addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "affiliated_marketplace", constraintName: "FKA6EB2C37666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		
		addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "affiliated_marketplace", constraintName: "FKA6EB2C3E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		
		addForeignKeyConstraint(baseColumnNames: "icon_id", baseTableName: "affiliated_marketplace", constraintName: "FKA6EB2C3EA25263C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "images", referencesUniqueColumn: "false")
	}
	
	changeSet(author: "marketplace", id: "2.4.0-5", dbms: "mysql", context: "create, upgrade, 2.4.0") {
		comment( "Add not-null constraints for ext_profile and ext_service_item. Update profile.bio column type." )
		addNotNullConstraint(columnDataType: "VARCHAR(255)", columnName: "system_uri", tableName: "ext_profile")
		addNotNullConstraint(columnDataType: "VARCHAR(255)", columnName: "system_uri", tableName: "ext_service_item")
		modifyDataType(columnName: "bio", newDataType: "VARCHAR(1000)", tableName: "profile")
	}
	
	changeSet(author: "marketplace", id: "2.4.0-5", dbms: "oracle", context: "create, upgrade, 2.4.0") {
		comment( "Add not-null constraints for ext_profile and ext_service_item." )
		addNotNullConstraint(columnName: "system_uri", tableName: "ext_profile")
		addNotNullConstraint(columnName: "system_uri", tableName: "ext_service_item")
	}
	
	changeSet(author: "marketplace", id: "2.4.0-5", dbms: "postgresql", context: "create, upgrade, 2.4.0") {
		comment( "Add not-null constraints for ext_profile and ext_service_item." )
		addNotNullConstraint(columnName: "system_uri", tableName: "ext_profile")
		addNotNullConstraint(columnName: "system_uri", tableName: "ext_service_item")
	}
	
	// Handle enabling foreign key check for mysql when DONE modifying FK constraints
	changeSet(author: "marketplace", id: "2.4.0-2.2", dbms: "mysql", context: "create, upgrade, 2.4.0") {
		comment( "Enable foreign key check for mysql when DONE modifying FK constraints." )
		sql ( """
			SET FOREIGN_KEY_CHECKS = 1;
		""")
	}

	changeSet(author: "marketplace", id: "2.4.0-3", dbms: "mssql, mysql, oracle, postgresql", context: "create, upgrade, 2.4.0") {
		comment("Drop types.is_widget column")
		dropColumn(columnName: "is_widget", tableName: "types")
	}

	changeSet(author: "marketplace", id: "2.4.0-4", dbms: "mssql, mysql, oracle, postgresql", context: "fix") {
		comment("Rename column owf_properties.hide_in_launch to owf_properties.visible_in_launch if necessary")
		preConditions (onFail:"MARK_RAN", onUpdateSQL:"FAIL") {
			not {
				columnExists(tableName:"owf_properties", columnName:"visible_in_launch")
			}
		}
		renameColumn(tableName:"owf_properties", oldColumnName:"hide_in_launch", newColumnName:"visible_in_launch", columnDataType:"BIT")
	}

}