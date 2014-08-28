databaseChangeLog = {

	// use 'create' context for changesets that should be part of creation script
	// use version number context (e.g., 2.5.0) to denote the changesets which should go into upgrade scripts for that version 
	// favor making database agnostic changeSets - however if needed the dbms attribute can be set to make a db specific changeset

	// Add columns for import/export
	changeSet(author: "marketplace", dbms: "mysql", id: "2.5.0-1", context: "create, 2.5.0") {
		addColumn(tableName: "category") {
			column(name: "uuid", type: "VARCHAR(255)") {
				constraints(nullable: "true")
			}
		}

		addColumn(tableName: "state") {
			column(name: "uuid", type: "VARCHAR(255)") {
				constraints(nullable: "true")
			}
		}

		addColumn(tableName: "types") {
			column(name: "uuid", type: "VARCHAR(255)") {
				constraints(nullable: "true")
			}
		}

		createIndex(indexName: "category_uuid_idx", tableName: "category", unique: "true") {
			column(name: "uuid")
		}

		createIndex(indexName: "state_uuid_idx", tableName: "state", unique: "true") {
			column(name: "uuid")
		}

		createIndex(indexName: "types_uuid_idx", tableName: "types", unique: "true") {
			column(name: "uuid")
		}		
		addColumn(tableName: "profile") {
			column(name: "uuid", type: "VARCHAR(255)") {
				constraints(nullable: "true")
			}
		}

		addColumn(tableName: "custom_field_definition") {
			column(name: "uuid", type: "VARCHAR(255)") {
				constraints(nullable: "true")
			}
		}

		createIndex(indexName: "profile_uuid_idx", tableName: "profile", unique: "true") {
			column(name: "uuid")
		}

		createIndex(indexName: "cfd_uuid_idx", tableName: "custom_field_definition", unique: "true") {
			column(name: "uuid")
		}
	}   // end of mysql

	changeSet(author: "marketplace", dbms: "oracle", id: "2.5.0-1", context: "create, 2.5.0") {
		addColumn(tableName: "category") {
				column(name: "uuid", type: "varchar(255 char)") {
					constraints(nullable: "true")
				}
		}

        addColumn(tableName: "state") {
            column(name: "uuid", type: "varchar(255 char)") {
                constraints(nullable: "true")
            }
        }

        addColumn(tableName: "types") {
            column(name: "uuid", type: "varchar(255 char)") {
                constraints(nullable: "true")
            }
        }

        createIndex(indexName: "category_uuid_idx", tableName: "category", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "state_uuid_idx", tableName: "state", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "types_uuid_idx", tableName: "types", unique: "true") {
            column(name: "uuid")
        }
        addColumn(tableName: "profile") {
            column(name: "uuid", type: "varchar(255 char)") {
                constraints(nullable: "true")
            }
        }

        addColumn(tableName: "custom_field_definition") {
            column(name: "uuid", type: "varchar(255 char)") {
                constraints(nullable: "true")
            }
        }

        createIndex(indexName: "profile_uuid_idx", tableName: "profile", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "cfd_uuid_idx", tableName: "custom_field_definition", unique: "true") {
            column(name: "uuid")
        }
	}   // end of oracle

    changeSet(author: "marketplace", dbms: "postgresql", id: "2.5.0-1", context: "create, 2.5.0") {
        addColumn(tableName: "category") {
            column(name: "uuid", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }

        addColumn(tableName: "state") {
            column(name: "uuid", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }

        addColumn(tableName: "types") {
            column(name: "uuid", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }

        createIndex(indexName: "category_uuid_idx", tableName: "category", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "state_uuid_idx", tableName: "state", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "types_uuid_idx", tableName: "types", unique: "true") {
            column(name: "uuid")
        }
        addColumn(tableName: "profile") {
            column(name: "uuid", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }

        addColumn(tableName: "custom_field_definition") {
            column(name: "uuid", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }

        createIndex(indexName: "profile_uuid_idx", tableName: "profile", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "cfd_uuid_idx", tableName: "custom_field_definition", unique: "true") {
            column(name: "uuid")
        }
	}   // end of postgres

    changeSet(author: "marketplace", dbms: "mssql", id: "2.5.0-1", context: "create, 2.5.0") {
        addColumn(tableName: "category") {
            column(name: "uuid", type: "nvarchar(255)") {
                constraints(nullable: "true")
            }
        }

        sql ( """
			UPDATE [dbo].[category] SET [uuid] = NEWID() WHERE [uuid] is NULL
		""")


        addColumn(tableName: "state") {
            column(name: "uuid", type: "nvarchar(255)") {
                constraints(nullable: "true")
            }
        }

        sql ( """
			UPDATE [dbo].[state] SET [uuid] = NEWID() WHERE [uuid] is NULL
		""")

        addColumn(tableName: "types") {
            column(name: "uuid", type: "nvarchar(255)") {
                constraints(nullable: "true")
            }
        }

        sql ( """
			UPDATE [dbo].[types] SET [uuid] = NEWID() WHERE [uuid] is NULL
		""")

        createIndex(indexName: "category_uuid_idx", tableName: "category", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "state_uuid_idx", tableName: "state", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "types_uuid_idx", tableName: "types", unique: "true") {
            column(name: "uuid")
        }

        addColumn(tableName: "profile") {
            column(name: "uuid", type: "nvarchar(255)") {
                constraints(nullable: "true")
            }
        }

        sql ( """
			UPDATE [dbo].[profile] SET [uuid] = NEWID() WHERE [uuid] is NULL
		""")

        addColumn(tableName: "custom_field_definition") {
            column(name: "uuid", type: "nvarchar(255)") {
                constraints(nullable: "true")
            }
        }

        sql ( """
			UPDATE [dbo].[custom_field_definition] SET [uuid] = NEWID() WHERE [uuid] is NULL
		""")

        createIndex(indexName: "profile_uuid_idx", tableName: "profile", unique: "true") {
            column(name: "uuid")
        }

        createIndex(indexName: "cfd_uuid_idx", tableName: "custom_field_definition", unique: "true") {
            column(name: "uuid")
        }
	}   // end of SQLServer

	// New owf property "background"
	changeSet(author: "marketplace", id: "2.5.0-3", dbms: "mssql, mysql, oracle, postgresql", context: "create, 2.5.0") {
			addColumn(tableName: "owf_properties") {
				column(name: "background", type: "boolean", defaultValueBoolean: "false") {
					constraints(nullable: "false")
				}
			}
	}

	// New tables for import module intergation
	changeSet(author: "marketplace", id: "2.5.0-4", dbms: "mysql", context: "create, 2.5.0") {
		createTable(tableName: "import_task") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_taskPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "bigint")

			column(name: "created_date", type: "datetime")

			column(name: "cron_exp", type: "varchar(255)")

			column(name: "download_images", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "edited_by_id", type: "bigint")

			column(name: "edited_date", type: "datetime")

			column(name: "enabled", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "exec_interval", type: "integer")

			column(name: "extra_url_params", type: "varchar(512)")

			column(name: "interface_config_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "last_run_result_id", type: "bigint")

			column(name: "name", type: "varchar(50)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "update_type", type: "varchar(7)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255)")

			column(name: "keystore_pass", type: "varchar(2048)")

			column(name: "keystore_path", type: "varchar(2048)")

			column(name: "truststore_path", type: "varchar(2048)")
		}

		createTable(tableName: "import_task_result") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_task_rPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(4000)") {
				constraints(nullable: "false")
			}

			column(name: "result", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "run_date", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "task_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}

		createTable(tableName: "interface_configuration") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "interface_conPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "allow_truncate", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "auto_create_meta_data", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "default_large_icon_url", type: "varchar(2048)")

			column(name: "default_small_icon_url", type: "varchar(2048)")

			column(name: "delta_since_time_param", type: "varchar(64)")

			column(name: "delta_static_parameters", type: "varchar(2048)")

			column(name: "full_static_parameters", type: "varchar(2048)")

			column(name: "loose_match", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(256)") {
				constraints(nullable: "false")
			}

			column(name: "query_date_format", type: "varchar(32)")

			column(name: "response_date_format", type: "varchar(32)")
		}
	} // End mysql
	 
	changeSet(author: "marketplace", id: "2.5.0-4", dbms: "oracle", context: "create, 2.5.0") {
		createTable(tableName: "import_task") {
			column(name: "id", type: "number(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_taskPK")
			}

			column(name: "version", type: "number(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "number(19,0)")

			column(name: "created_date", type: "timestamp(6)")

			column(name: "cron_exp", type: "varchar(255 char)")

			column(name: "download_images", type: "number(1,0)") {
				constraints(nullable: "false")
			}

			column(name: "edited_by_id", type: "number(19,0)")

			column(name: "edited_date", type: "timestamp(6)")

			column(name: "enabled", type: "number(1,0)") {
				constraints(nullable: "false")
			}

			column(name: "exec_interval", type: "number(19,0)")

			column(name: "extra_url_params", type: "varchar(512 char)")

			column(name: "interface_config_id", type: "number(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "last_run_result_id", type: "number(19,0)")

			column(name: "name", type: "varchar(50 char)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "update_type", type: "varchar(7 char)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255 char)")
			
			column(name: "keystore_pass", type: "varchar(2048 char)")

			column(name: "keystore_path", type: "varchar(2048 char)")
			
			column(name: "truststore_path", type: "varchar(2048 char)")
		}
		
		createTable(tableName: "import_task_result") {
			column(name: "id", type: "number(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_task_rPK")
			}

			column(name: "version", type: "number(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(4000 char)") {
				constraints(nullable: "false")
			}

			column(name: "result", type: "number(1,0)") {
				constraints(nullable: "false")
			}

			column(name: "run_date", type: "timestamp(6)") {
				constraints(nullable: "false")
			}

			column(name: "task_id", type: "number(19,0)") {
				constraints(nullable: "false")
			}
		}
		
		createTable(tableName: "interface_configuration") {
			column(name: "id", type: "number(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "interface_conPK")
			}

			column(name: "version", type: "number(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "allow_truncate", type: "number(1,0)") {
				constraints(nullable: "false")
			}

			column(name: "auto_create_meta_data", type: "number(1,0)") {
				constraints(nullable: "false")
			}

			column(name: "default_large_icon_url", type: "varchar(2048 char)")

			column(name: "default_small_icon_url", type: "varchar(2048 char)")

			column(name: "delta_since_time_param", type: "varchar(64 char)")

			column(name: "delta_static_parameters", type: "varchar(2048 char)")

			column(name: "full_static_parameters", type: "varchar(2048 char)")

			column(name: "loose_match", type: "number(1,0)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(256 char)") {
				constraints(nullable: "false")
			}

			column(name: "query_date_format", type: "varchar(32 char)")

			column(name: "response_date_format", type: "varchar(32 char)")
		}
	} // End oracle

	changeSet(author: "marketplace", id: "2.5.0-4", dbms: "postgresql", context: "create, 2.5.0") {
		createTable(tableName: "import_task") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_taskPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "int8")

			column(name: "created_date", type: "timestamp")

			column(name: "cron_exp", type: "varchar(255)")

			column(name: "download_images", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "edited_by_id", type: "int8")

			column(name: "edited_date", type: "timestamp")

			column(name: "enabled", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "exec_interval", type: "int4")

			column(name: "extra_url_params", type: "varchar(512)")

			column(name: "interface_config_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "last_run_result_id", type: "int8")

			column(name: "name", type: "varchar(50)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "update_type", type: "varchar(7)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255)")

			column(name: "keystore_pass", type: "varchar(2048)")

			column(name: "keystore_path", type: "varchar(2048)")

			column(name: "truststore_path", type: "varchar(2048)")
		}

		createTable(tableName: "import_task_result") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_task_rPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(4000)") {
				constraints(nullable: "false")
			}

			column(name: "result", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "run_date", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "task_id", type: "int8") {
				constraints(nullable: "false")
			}
		}

		createTable(tableName: "interface_configuration") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "interface_conPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "allow_truncate", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "auto_create_meta_data", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "default_large_icon_url", type: "varchar(2048)")

			column(name: "default_small_icon_url", type: "varchar(2048)")

			column(name: "delta_since_time_param", type: "varchar(64)")

			column(name: "delta_static_parameters", type: "varchar(2048)")

			column(name: "full_static_parameters", type: "varchar(2048)")

			column(name: "loose_match", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(256)") {
				constraints(nullable: "false")
			}

			column(name: "query_date_format", type: "varchar(32)")

			column(name: "response_date_format", type: "varchar(32)")
		}

	} // End postgressql

	changeSet(author: "marketplace", id: "2.5.0-4", dbms: "mssql", context: "create, 2.5.0") {
		createTable(tableName: "import_task") {
			column(autoIncrement: "true", name: "id", type: "numeric(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_taskPK")
			}

			column(name: "version", type: "numeric(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "created_by_id", type: "numeric(19,0)")

			column(name: "created_date", type: "datetime")

			column(name: "cron_exp", type: "nvarchar(255)")

			column(name: "download_images", type: "tinyint") {
				constraints(nullable: "false")
			}

			column(name: "edited_by_id", type: "numeric(19,0)")

			column(name: "edited_date", type: "datetime")

			column(name: "enabled", type: "tinyint") {
				constraints(nullable: "false")
			}

			column(name: "exec_interval", type: "int")

			column(name: "extra_url_params", type: "nvarchar(512)")

			column(name: "interface_config_id", type: "numeric(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "last_run_result_id", type: "numeric(19,0)")

			column(name: "name", type: "nvarchar(50)") {
				constraints(nullable: "false", unique: "true")
			}

			column(name: "update_type", type: "nvarchar(7)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "nvarchar(255)")
			
			column(name: "keystore_pass", type: "nvarchar(2048)")

			column(name: "keystore_path", type: "nvarchar(2048)")
			
			column(name: "truststore_path", type: "nvarchar(2048)")
		}
		
		createTable(tableName: "import_task_result") {
			column(autoIncrement: "true", name: "id", type: "numeric(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_task_rPK")
			}

			column(name: "version", type: "numeric(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "nvarchar(4000)") {
				constraints(nullable: "false")
			}

			column(name: "result", type: "tinyint") {
				constraints(nullable: "false")
			}

			column(name: "run_date", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "task_id", type: "numeric(19,0)") {
				constraints(nullable: "false")
			}
		}
		
		createTable(tableName: "interface_configuration") {
			column(autoIncrement: "true", name: "id", type: "numeric(19,0)") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "interface_conPK")
			}

			column(name: "version", type: "numeric(19,0)") {
				constraints(nullable: "false")
			}

			column(name: "allow_truncate", type: "tinyint") {
				constraints(nullable: "false")
			}

			column(name: "auto_create_meta_data", type: "tinyint") {
				constraints(nullable: "false")
			}

			column(name: "default_large_icon_url", type: "nvarchar(2048)")

			column(name: "default_small_icon_url", type: "nvarchar(2048)")

			column(name: "delta_since_time_param", type: "nvarchar(64)")

			column(name: "delta_static_parameters", type: "nvarchar(2048)")

			column(name: "full_static_parameters", type: "nvarchar(2048)")

			column(name: "loose_match", type: "tinyint") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "nvarchar(256)") {
				constraints(nullable: "false")
			}

			column(name: "query_date_format", type: "nvarchar(32)")

			column(name: "response_date_format", type: "nvarchar(32)")
		}
	} // End sqlserver
		
	// New constrarints for import module intergation
	changeSet(author: "marketplace", id: "2.5.0-5", dbms: "mssql, mysql, oracle, postgresql", context: "create, 2.5.0") {
		addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "import_task", constraintName: "FK578EF9DF7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		
		addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "import_task", constraintName: "FK578EF9DFE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
		
		addForeignKeyConstraint(baseColumnNames: "interface_config_id", baseTableName: "import_task", constraintName: "FK578EF9DFA31F8712", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "interface_configuration", referencesUniqueColumn: "false")
		
		addForeignKeyConstraint(baseColumnNames: "last_run_result_id", baseTableName: "import_task", constraintName: "FK578EF9DF919216CA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "import_task_result", referencesUniqueColumn: "false")
		
		addForeignKeyConstraint(baseColumnNames: "task_id", baseTableName: "import_task_result", constraintName: "FK983AC27D11D7F882", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "import_task", referencesUniqueColumn: "false")
	}
	
	changeSet(author: "marketplace", id: "2.5.0-6", dbms: "mysql", context: "create, 2.5.0") {
		modifyDataType(columnName: "title", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "version_name", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "organization", newDataType: "varchar(256)", tableName: "service_item")
        modifyDataType(columnName: "tech_poc", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "username", newDataType: "varchar(256)", tableName: "profile")
		modifyDataType(columnName: "display_name", newDataType: "varchar(256)", tableName: "profile")
		modifyDataType(columnName: "email", newDataType: "varchar(256)", tableName: "profile")
		modifyDataType(columnName: "value", newDataType: "varchar(256)", tableName: "text_cf")
		modifyDataType(columnName: "system_uri", newDataType: "varchar(256)", tableName: "ext_service_item")
		modifyDataType(columnName: "external_id", newDataType: "varchar(256)", tableName: "ext_service_item")
	}		

	changeSet(author: "marketplace", id: "2.5.0-6", dbms: "oracle", context: "create, 2.5.0") {
		modifyDataType(columnName: "title", newDataType: "varchar(256 CHAR)", tableName: "service_item")
		modifyDataType(columnName: "version_name", newDataType: "varchar(256 CHAR)", tableName: "service_item")
		modifyDataType(columnName: "organization", newDataType: "varchar(256 CHAR)", tableName: "service_item")
		modifyDataType(columnName: "tech_poc", newDataType: "varchar(256 CHAR)", tableName: "service_item")
		modifyDataType(columnName: "username", newDataType: "varchar(256 CHAR)", tableName: "profile")
		modifyDataType(columnName: "display_name", newDataType: "varchar(256 CHAR)", tableName: "profile")
		modifyDataType(columnName: "email", newDataType: "varchar(256 CHAR)", tableName: "profile")
		modifyDataType(columnName: "value", newDataType: "varchar(256 CHAR)", tableName: "text_cf")
		modifyDataType(columnName: "system_uri", newDataType: "varchar(256 CHAR)", tableName: "ext_service_item")
		modifyDataType(columnName: "external_id", newDataType: "varchar(256 CHAR)", tableName: "ext_service_item")
	}

	changeSet(author: "marketplace", id: "2.5.0-6", dbms: "postgresql", context: "create, 2.5.0") {
		modifyDataType(columnName: "title", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "version_name", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "organization", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "tech_poc", newDataType: "varchar(256)", tableName: "service_item")
		modifyDataType(columnName: "username", newDataType: "varchar(256)", tableName: "profile")
		modifyDataType(columnName: "display_name", newDataType: "varchar(256)", tableName: "profile")
		modifyDataType(columnName: "email", newDataType: "varchar(256)", tableName: "profile")
		modifyDataType(columnName: "value", newDataType: "varchar(256)", tableName: "text_cf")
		modifyDataType(columnName: "system_uri", newDataType: "varchar(256)", tableName: "ext_service_item")
		modifyDataType(columnName: "external_id", newDataType: "varchar(256)", tableName: "ext_service_item")
	}

	changeSet(author: "marketplace", id: "2.5.0-6", dbms: "mssql", context: "create, 2.5.0") {
		modifyDataType(columnName: "title", newDataType: "nvarchar(256)", tableName: "service_item")
		modifyDataType(columnName: "version_name", newDataType: "nvarchar(256)", tableName: "service_item")
		modifyDataType(columnName: "organization", newDataType: "nvarchar(256)", tableName: "service_item")
		modifyDataType(columnName: "tech_poc", newDataType: "nvarchar(256)", tableName: "service_item")
		modifyDataType(columnName: "username", newDataType: "nvarchar(256)", tableName: "profile")
		modifyDataType(columnName: "display_name", newDataType: "nvarchar(256)", tableName: "profile")
		modifyDataType(columnName: "email", newDataType: "nvarchar(256)", tableName: "profile")
		modifyDataType(columnName: "value", newDataType: "nvarchar(256)", tableName: "text_cf")
		modifyDataType(columnName: "system_uri", newDataType: "nvarchar(256)", tableName: "ext_service_item")
		modifyDataType(columnName: "external_id", newDataType: "nvarchar(256)", tableName: "ext_service_item")
	}


}