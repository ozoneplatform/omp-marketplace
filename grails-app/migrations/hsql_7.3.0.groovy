databaseChangeLog = {

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-1") {
		createTable(tableName: "AFFILIATED_MARKETPLACE") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_52")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "ACTIVE", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "ICON_ID", type: "BIGINT")

			column(name: "NAME", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "SERVER_URL", type: "VARCHAR(2083)") {
				constraints(nullable: "false")
			}

			column(name: "TIMEOUT", type: "BIGINT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-2") {
		createTable(tableName: "APPLICATION_CONFIGURATION") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_54")
			}

			column(name: "CODE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "DESCRIPTION", type: "VARCHAR(2000)")

			column(name: "GROUP_NAME", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "HELP", type: "VARCHAR(2000)")

			column(name: "MUTABLE", type: "BOOLEAN")

			column(name: "SUB_GROUP_NAME", type: "VARCHAR(255)")

			column(name: "SUB_GROUP_ORDER", type: "INT")

			column(name: "TITLE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "TYPE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "VALUE", type: "VARCHAR(2000)")

			column(defaultValueNumeric: "0", name: "VERSION", type: "BIGINT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-3") {
		createTable(tableName: "AVATAR") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_56")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CONTENT_TYPE", type: "VARCHAR(255)")

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IS_DEFAULT", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "PIC", type: "VARBINARY(10485760)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-4") {
		createTable(tableName: "CATEGORY") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_60")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(250)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "TITLE", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-5") {
		createTable(tableName: "CHANGE_DETAIL") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_63")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "FIELD_NAME", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "NEW_VALUE", type: "VARCHAR(4000)")

			column(name: "OBJECT_CLASS_NAME", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "OBJECT_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "OBJECT_VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "OLD_VALUE", type: "VARCHAR(4000)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-6") {
		createTable(tableName: "CHANGE_LOG") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_65")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CHANGE_DATE", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "CHANGED_BY_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "DESCRIPTION", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "OBJECT_CLASS_NAME", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "OBJECT_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "OBJECT_VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-7") {
		createTable(tableName: "CHECK_BOX_CF") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_67")
			}

			column(name: "VALUE", type: "BOOLEAN")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-8") {
		createTable(tableName: "CHECK_BOX_CFD") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_69")
			}

			column(name: "SELECTED_BY_DEFAULT", type: "BOOLEAN")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-9") {
		createTable(tableName: "CUSTOM_FIELD") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_71")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "CUSTOM_FIELD_DEFINITION_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-10") {
		createTable(tableName: "CUSTOM_FIELD_DEFINITION") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_74")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "ALL_TYPES", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(250)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IS_PERMANENT", type: "BOOLEAN")

			column(name: "IS_REQUIRED", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "LABEL", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "NAME", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "SECTION", type: "VARCHAR(255)")

			column(name: "STYLE_TYPE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "TOOLTIP", type: "VARCHAR(50)")

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-11") {
		createTable(tableName: "CUSTOM_FIELD_DEFINITION_TYPES") {
			column(name: "CF_DEFINITION_TYPES_ID", type: "BIGINT")

			column(name: "TYPES_ID", type: "BIGINT")

			column(name: "TYPES_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-12") {
		createTable(tableName: "DEFAULT_IMAGES") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_78")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DEFINED_DEFAULT_TYPE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IMAGE_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-13") {
		createTable(tableName: "DROP_DOWN_CF") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_80")
			}

			column(name: "VALUE_ID", type: "BIGINT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-14") {
		createTable(tableName: "DROP_DOWN_CF_FIELD_VALUE") {
			column(name: "DROP_DOWN_CF_FIELD_VALUE_ID", type: "BIGINT")

			column(name: "FIELD_VALUE_ID", type: "BIGINT")

			column(name: "FIELD_VALUE_LIST_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-15") {
		createTable(tableName: "DROP_DOWN_CFD") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_83")
			}

			column(name: "IS_MULTI_SELECT", type: "BOOLEAN")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-16") {
		createTable(tableName: "EXT_PROFILE") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_86")
			}

			column(name: "EXTERNAL_EDIT_URL", type: "VARCHAR(2083)")

			column(name: "EXTERNAL_ID", type: "VARCHAR(255)")

			column(name: "EXTERNAL_VIEW_URL", type: "VARCHAR(2083)")

			column(name: "SYSTEM_URI", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-17") {
		createTable(tableName: "EXT_SERVICE_ITEM") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_90")
			}

			column(name: "EXTERNAL_EDIT_URL", type: "VARCHAR(2083)")

			column(name: "EXTERNAL_ID", type: "VARCHAR(256)")

			column(name: "EXTERNAL_VIEW_URL", type: "VARCHAR(2083)")

			column(name: "SYSTEM_URI", type: "VARCHAR(256)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-18") {
		createTable(tableName: "FIELD_VALUE") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_93")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "CUSTOM_FIELD_DEFINITION_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "DISPLAY_TEXT", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IS_ENABLED", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "FIELD_VALUES_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-19") {
		createTable(tableName: "IMAGE_URL_CF") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_97")
			}

			column(name: "VALUE", type: "VARCHAR(2083)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-20") {
		createTable(tableName: "IMAGE_URL_CFD") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_99")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-21") {
		createTable(tableName: "IMAGES") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_101")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "BYTES", type: "VARBINARY(10485760)") {
				constraints(nullable: "false")
			}

			column(name: "CONTENT_TYPE", type: "VARCHAR(255)")

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IMAGE_SIZE", type: "float")

			column(name: "IS_DEFAULT", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "TYPE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-22") {
		createTable(tableName: "IMPORT_TASK") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_103")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "CRON_EXP", type: "VARCHAR(255)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "ENABLED", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "EXEC_INTERVAL", type: "INT")

			column(name: "EXTRA_URL_PARAMS", type: "VARCHAR(512)")

			column(name: "INTERFACE_CONFIG_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "KEYSTORE_PASS", type: "VARCHAR(2048)")

			column(name: "KEYSTORE_PATH", type: "VARCHAR(2048)")

			column(name: "LAST_RUN_RESULT_ID", type: "BIGINT")

			column(name: "NAME", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "TRUSTSTORE_PATH", type: "VARCHAR(2048)")

			column(name: "UPDATE_TYPE", type: "VARCHAR(7)") {
				constraints(nullable: "false")
			}

			column(name: "URL", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-23") {
		createTable(tableName: "IMPORT_TASK_RESULT") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_106")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "MESSAGE", type: "VARCHAR(4000)") {
				constraints(nullable: "false")
			}

			column(name: "RESULT", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "RUN_DATE", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "TASK_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-24") {
		createTable(tableName: "INTENT") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_110")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "ACTION_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DATA_TYPE_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "RECEIVE", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "SEND", type: "BOOLEAN") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-25") {
		createTable(tableName: "INTENT_ACTION") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_112")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(256)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "TITLE", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-26") {
		createTable(tableName: "INTENT_DATA_TYPE") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_115")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(256)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "TITLE", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-27") {
		createTable(tableName: "INTENT_DIRECTION") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_119")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(250)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "TITLE", type: "VARCHAR(7)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-28") {
		createTable(tableName: "INTERFACE_CONFIGURATION") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_123")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "ALLOW_TRUNCATE", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "AUTO_CREATE_META_DATA", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "DEFAULT_LARGE_ICON_URL", type: "VARCHAR(2048)")

			column(name: "DEFAULT_SMALL_ICON_URL", type: "VARCHAR(2048)")

			column(name: "DELTA_SINCE_TIME_PARAM", type: "VARCHAR(64)")

			column(name: "DELTA_STATIC_PARAMETERS", type: "VARCHAR(2048)")

			column(name: "DOWNLOAD_IMAGES", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "FULL_STATIC_PARAMETERS", type: "VARCHAR(2048)")

			column(name: "LOOSE_MATCH", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "NAME", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "QUERY_DATE_FORMAT", type: "VARCHAR(32)")

			column(name: "RESPONSE_DATE_FORMAT", type: "VARCHAR(32)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-29") {
		createTable(tableName: "ITEM_COMMENT") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_125")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "AUTHOR_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "RATE", type: "float")

			column(name: "SERVICE_ITEM_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "TEXT", type: "VARCHAR(4000)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-30") {
		createTable(tableName: "MODIFY_RELATIONSHIP_ACTIVITY") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_127")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-31") {
		createTable(tableName: "OWF_PROPERTIES") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_129")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "BACKGROUND", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTOR_URL", type: "VARCHAR(2083)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "HEIGHT", type: "BIGINT")

			column(name: "OWF_WIDGET_TYPE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "SINGLETON", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "STACK_CONTEXT", type: "VARCHAR(200)")

			column(name: "STACK_DESCRIPTOR", type: "LONGVARCHAR")

			column(name: "UNIVERSAL_NAME", type: "VARCHAR(255)")

			column(name: "VISIBLE_IN_LAUNCH", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "WIDTH", type: "BIGINT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-32") {
		createTable(tableName: "OWF_PROPERTIES_INTENT") {
			column(name: "OWF_PROPERTIES_INTENTS_ID", type: "BIGINT")

			column(name: "INTENT_ID", type: "BIGINT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-33") {
		createTable(tableName: "OWF_WIDGET_TYPES") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_136")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "TITLE", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-34") {
		createTable(tableName: "PROFILE") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_139")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "AVATAR_ID", type: "BIGINT")

			column(name: "BIO", type: "VARCHAR(1000)")

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "DISPLAY_NAME", type: "VARCHAR(256)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "EMAIL", type: "VARCHAR(256)")

			column(name: "USERNAME", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-35") {
		createTable(tableName: "REJECTION_ACTIVITY") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_149")
			}

			column(name: "REJECTION_LISTING_ID", type: "BIGINT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-36") {
		createTable(tableName: "REJECTION_JUSTIFICATION") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_151")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(250)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "TITLE", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-37") {
		createTable(tableName: "REJECTION_LISTING") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_157")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "AUTHOR_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(2000)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "JUSTIFICATION_ID", type: "BIGINT")

			column(name: "SERVICE_ITEM_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-38") {
		createTable(tableName: "RELATIONSHIP") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_167")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "OWNING_ENTITY_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "RELATIONSHIP_TYPE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-39") {
		createTable(tableName: "RELATIONSHIP_ACTIVITY_LOG") {
			column(name: "MOD_REL_ACTIVITY_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "SERVICE_ITEM_SNAPSHOT_ID", type: "BIGINT")

			column(name: "ITEMS_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-40") {
		createTable(tableName: "RELATIONSHIP_SERVICE_ITEM") {
			column(name: "RELATIONSHIP_RELATED_ITEMS_ID", type: "BIGINT")

			column(name: "SERVICE_ITEM_ID", type: "BIGINT")

			column(name: "RELATED_ITEMS_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-41") {
		createTable(tableName: "SCORE_CARD") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_171")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "SCORE", type: "float") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-42") {
		createTable(tableName: "SCORE_CARD_ITEM") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_177")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(500)") {
				constraints(nullable: "false")
			}

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IMAGE", type: "VARCHAR(250)")

			column(name: "IS_STANDARD_QUESTION", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "QUESTION", type: "VARCHAR(250)") {
				constraints(nullable: "false")
			}

			column(name: "SHOW_ON_LISTING", type: "BOOLEAN")

			column(name: "WEIGHT", type: "float")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-43") {
		createTable(tableName: "SCORE_CARD_ITEM_RESPONSE") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_183")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IS_SATISFIED", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "SCORE_CARD_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "SCORE_CARD_ITEM_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-44") {
		createTable(tableName: "SERVICE_ITEM") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_193")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "AGENCY", type: "VARCHAR(255)")

			column(name: "AGENCY_ICON", type: "VARCHAR(255)")

			column(name: "APPROVAL_STATUS", type: "VARCHAR(11)") {
				constraints(nullable: "false")
			}

			column(name: "APPROVED_DATE", type: "DATETIME")

			column(name: "AUTHOR_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "AVG_RATE", type: "float") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DEPENDENCIES", type: "VARCHAR(1000)")

			column(name: "DESCRIPTION", type: "VARCHAR(4000)")

			column(name: "DOC_URL", type: "VARCHAR(2083)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IMAGE_LARGE_URL", type: "VARCHAR(2083)")

			column(name: "IMAGE_SMALL_URL", type: "VARCHAR(2083)")

			column(name: "INSTALL_URL", type: "VARCHAR(2083)")

			column(name: "IS_HIDDEN", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "IS_OUTSIDE", type: "BOOLEAN")

			column(name: "LAST_ACTIVITY_ID", type: "BIGINT")

			column(name: "LAUNCH_URL", type: "VARCHAR(2083)")

			column(name: "ORGANIZATION", type: "VARCHAR(256)")

			column(name: "OWF_PROPERTIES_ID", type: "BIGINT")

			column(name: "RELEASE_DATE", type: "DATETIME")

			column(name: "REQUIREMENTS", type: "VARCHAR(1000)")

			column(name: "SCORE_CARD_ID", type: "BIGINT")

			column(name: "SCREENSHOT1URL", type: "VARCHAR(2083)")

			column(name: "SCREENSHOT2URL", type: "VARCHAR(2083)")

			column(name: "STATE_ID", type: "BIGINT")

			column(name: "TECH_POC", type: "VARCHAR(256)")

			column(name: "TITLE", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "TOTAL_COMMENTS", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "TOTAL_RATE1", type: "INT")

			column(name: "TOTAL_RATE2", type: "INT")

			column(name: "TOTAL_RATE3", type: "INT")

			column(name: "TOTAL_RATE4", type: "INT")

			column(name: "TOTAL_RATE5", type: "INT")

			column(name: "TOTAL_VOTES", type: "INT") {
				constraints(nullable: "false")
			}

			column(name: "TYPES_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "VERSION_NAME", type: "VARCHAR(256)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-45") {
		createTable(tableName: "SERVICE_ITEM_ACTIVITY") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_205")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "ACTION", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "ACTIVITY_DATE", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "AUTHOR_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DETAILS", type: "VARCHAR(255)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "SERVICE_ITEM_ID", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "SERVICE_ITEM_VERSION", type: "BIGINT")

			column(name: "SERVICE_ITEM_ACTIVITIES_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-46") {
		createTable(tableName: "SERVICE_ITEM_CATEGORY") {
			column(name: "SERVICE_ITEM_CATEGORIES_ID", type: "BIGINT")

			column(name: "CATEGORY_ID", type: "BIGINT")

			column(name: "CATEGORIES_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-47") {
		createTable(tableName: "SERVICE_ITEM_CUSTOM_FIELD") {
			column(name: "SERVICE_ITEM_CUSTOM_FIELDS_ID", type: "BIGINT")

			column(name: "CUSTOM_FIELD_ID", type: "BIGINT")

			column(name: "CUSTOM_FIELDS_IDX", type: "INT")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-48") {
		createTable(tableName: "SERVICE_ITEM_SNAPSHOT") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_221")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "SERVICE_ITEM_ID", type: "BIGINT")

			column(name: "TITLE", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-49") {
		createTable(tableName: "SI_RECOMMENDED_LAYOUTS") {
			column(name: "SERVICE_ITEM_ID", type: "BIGINT")

			column(name: "RECOMMENDED_LAYOUT", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-50") {
		createTable(tableName: "STATE") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_228")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(250)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "IS_PUBLISHED", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "TITLE", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-51") {
		createTable(tableName: "TEXT") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_235")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "NAME", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "READ_ONLY", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "VALUE", type: "VARCHAR(250)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-52") {
		createTable(tableName: "TEXT_AREA_CF") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_242")
			}

			column(name: "VALUE", type: "VARCHAR(4000)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-53") {
		createTable(tableName: "TEXT_AREA_CFD") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_244")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-54") {
		createTable(tableName: "TEXT_CF") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_246")
			}

			column(name: "VALUE", type: "VARCHAR(256)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-55") {
		createTable(tableName: "TEXT_CFD") {
			column(name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_248")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-56") {
		createTable(tableName: "TYPES") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_250")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "DESCRIPTION", type: "VARCHAR(250)")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "HAS_ICONS", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "HAS_LAUNCH_URL", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "IMAGE_ID", type: "BIGINT")

			column(name: "IS_PERMANENT", type: "BOOLEAN")

			column(name: "OZONE_AWARE", type: "BOOLEAN") {
				constraints(nullable: "false")
			}

			column(name: "TITLE", type: "VARCHAR(50)") {
				constraints(nullable: "false")
			}

			column(name: "UUID", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-57") {
		createTable(tableName: "U_DOMAIN") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_49")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "USERNAME", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-58") {
		createTable(tableName: "U_DOMAIN_PREFERENCES") {
			column(name: "PREFERENCES", type: "BIGINT")

			column(name: "PREFERENCES_IDX", type: "VARCHAR(255)")

			column(name: "PREFERENCES_ELT", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-59") {
		createTable(tableName: "USER_ACCOUNT") {
			column(autoIncrement: "true", startWith: "1", name: "ID", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_259")
			}

			column(name: "VERSION", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "CREATED_BY_ID", type: "BIGINT")

			column(name: "CREATED_DATE", type: "DATETIME")

			column(name: "EDITED_BY_ID", type: "BIGINT")

			column(name: "EDITED_DATE", type: "DATETIME")

			column(name: "LAST_LOGIN", type: "DATETIME")

			column(name: "USERNAME", type: "VARCHAR(250)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-60") {
		createIndex(indexName: "SYS_IDX_SYS_CT_58_61", tableName: "CATEGORY", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-61") {
		createIndex(indexName: "SYS_IDX_SYS_CT_72_75", tableName: "CUSTOM_FIELD_DEFINITION", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-62") {
		createIndex(indexName: "SYS_IDX_SYS_CT_84_87", tableName: "EXT_PROFILE", unique: "true") {
			column(name: "SYSTEM_URI")

			column(name: "EXTERNAL_ID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-63") {
		createIndex(indexName: "SYS_IDX_SYS_CT_88_91", tableName: "EXT_SERVICE_ITEM", unique: "true") {
			column(name: "SYSTEM_URI")

			column(name: "EXTERNAL_ID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-64") {
		createIndex(indexName: "SYS_IDX_SYS_CT_100_104", tableName: "IMPORT_TASK", unique: "true") {
			column(name: "NAME")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-65") {
		createIndex(indexName: "SYS_IDX_SYS_CT_108_113", tableName: "INTENT_ACTION", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-66") {
		createIndex(indexName: "SYS_IDX_SYS_CT_112_116", tableName: "INTENT_DATA_TYPE", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-67") {
		createIndex(indexName: "SYS_IDX_SYS_CT_116_120", tableName: "INTENT_DIRECTION", unique: "true") {
			column(name: "TITLE")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-68") {
		createIndex(indexName: "SYS_IDX_SYS_CT_117_121", tableName: "INTENT_DIRECTION", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-69") {
		createIndex(indexName: "SYS_IDX_SYS_CT_131_137", tableName: "OWF_WIDGET_TYPES", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-70") {
		createIndex(indexName: "SYS_IDX_SYS_CT_135_140", tableName: "PROFILE", unique: "true") {
			column(name: "USERNAME")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-71") {
		createIndex(indexName: "SYS_IDX_SYS_CT_136_141", tableName: "PROFILE", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-72") {
		createIndex(indexName: "SYS_IDX_SYS_CT_166_229", tableName: "STATE", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-73") {
		createIndex(indexName: "SYS_IDX_SYS_CT_170_236", tableName: "TEXT", unique: "true") {
			column(name: "NAME")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-74") {
		createIndex(indexName: "SYS_IDX_SYS_CT_182_251", tableName: "TYPES", unique: "true") {
			column(name: "UUID")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-75") {
		createIndex(indexName: "SYS_IDX_SYS_CT_186_260", tableName: "USER_ACCOUNT", unique: "true") {
			column(name: "USERNAME")
		}
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-76") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "AFFILIATED_MARKETPLACE", baseTableSchemaName: "PUBLIC", constraintName: "FKA6EB2C37666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-77") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "AFFILIATED_MARKETPLACE", baseTableSchemaName: "PUBLIC", constraintName: "FKA6EB2C3E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-78") {
		addForeignKeyConstraint(baseColumnNames: "ICON_ID", baseTableName: "AFFILIATED_MARKETPLACE", baseTableSchemaName: "PUBLIC", constraintName: "FKA6EB2C3EA25263C", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "IMAGES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-79") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "AVATAR", baseTableSchemaName: "PUBLIC", constraintName: "FKAC32C1597666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-80") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "AVATAR", baseTableSchemaName: "PUBLIC", constraintName: "FKAC32C159E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-81") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "CATEGORY", baseTableSchemaName: "PUBLIC", constraintName: "FK302BCFE7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-82") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "CATEGORY", baseTableSchemaName: "PUBLIC", constraintName: "FK302BCFEE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-83") {
		addForeignKeyConstraint(baseColumnNames: "CHANGED_BY_ID", baseTableName: "CHANGE_LOG", baseTableSchemaName: "PUBLIC", constraintName: "FK80F28E35B624A19E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-84") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "CUSTOM_FIELD", baseTableSchemaName: "PUBLIC", constraintName: "FK2ACD76AC7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-85") {
		addForeignKeyConstraint(baseColumnNames: "CUSTOM_FIELD_DEFINITION_ID", baseTableName: "CUSTOM_FIELD", baseTableSchemaName: "PUBLIC", constraintName: "FK2ACD76AC6F62C9ED", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "CUSTOM_FIELD_DEFINITION", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-86") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "CUSTOM_FIELD", baseTableSchemaName: "PUBLIC", constraintName: "FK2ACD76ACE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-87") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "CUSTOM_FIELD_DEFINITION", baseTableSchemaName: "PUBLIC", constraintName: "FK150F70C67666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-88") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "CUSTOM_FIELD_DEFINITION", baseTableSchemaName: "PUBLIC", constraintName: "FK150F70C6E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-89") {
		addForeignKeyConstraint(baseColumnNames: "TYPES_ID", baseTableName: "CUSTOM_FIELD_DEFINITION_TYPES", baseTableSchemaName: "PUBLIC", constraintName: "FK1A84FFC06928D597", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "TYPES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-90") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "DEFAULT_IMAGES", baseTableSchemaName: "PUBLIC", constraintName: "FK6F064E367666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-91") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "DEFAULT_IMAGES", baseTableSchemaName: "PUBLIC", constraintName: "FK6F064E36E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-92") {
		addForeignKeyConstraint(baseColumnNames: "IMAGE_ID", baseTableName: "DEFAULT_IMAGES", baseTableSchemaName: "PUBLIC", constraintName: "FK6F064E36553AF61A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "IMAGES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-93") {
		addForeignKeyConstraint(baseColumnNames: "VALUE_ID", baseTableName: "DROP_DOWN_CF", baseTableSchemaName: "PUBLIC", constraintName: "FK13ADE7D0BC98CEE3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "FIELD_VALUE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-94") {
		addForeignKeyConstraint(baseColumnNames: "FIELD_VALUE_ID", baseTableName: "DROP_DOWN_CF_FIELD_VALUE", baseTableSchemaName: "PUBLIC", constraintName: "FK2627FFDDA5BD888", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "FIELD_VALUE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-95") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "FIELD_VALUE", baseTableSchemaName: "PUBLIC", constraintName: "FK29F571EC7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-96") {
		addForeignKeyConstraint(baseColumnNames: "CUSTOM_FIELD_DEFINITION_ID", baseTableName: "FIELD_VALUE", baseTableSchemaName: "PUBLIC", constraintName: "FK29F571ECF1F14D3C", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "DROP_DOWN_CFD", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-97") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "FIELD_VALUE", baseTableSchemaName: "PUBLIC", constraintName: "FK29F571ECE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-98") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "IMAGES", baseTableSchemaName: "PUBLIC", constraintName: "FKB95A82787666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-99") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "IMAGES", baseTableSchemaName: "PUBLIC", constraintName: "FKB95A8278E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-100") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "IMPORT_TASK", baseTableSchemaName: "PUBLIC", constraintName: "FK578EF9DF7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-101") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "IMPORT_TASK", baseTableSchemaName: "PUBLIC", constraintName: "FK578EF9DFE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-102") {
		addForeignKeyConstraint(baseColumnNames: "INTERFACE_CONFIG_ID", baseTableName: "IMPORT_TASK", baseTableSchemaName: "PUBLIC", constraintName: "FK578EF9DFA31F8712", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "INTERFACE_CONFIGURATION", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-103") {
		addForeignKeyConstraint(baseColumnNames: "LAST_RUN_RESULT_ID", baseTableName: "IMPORT_TASK", baseTableSchemaName: "PUBLIC", constraintName: "FK578EF9DF919216CA", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "IMPORT_TASK_RESULT", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-104") {
		addForeignKeyConstraint(baseColumnNames: "TASK_ID", baseTableName: "IMPORT_TASK_RESULT", baseTableSchemaName: "PUBLIC", constraintName: "FK983AC27D11D7F882", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "IMPORT_TASK", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-105") {
		addForeignKeyConstraint(baseColumnNames: "ACTION_ID", baseTableName: "INTENT", baseTableSchemaName: "PUBLIC", constraintName: "FKB971369CD8544299", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "INTENT_ACTION", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-106") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "INTENT", baseTableSchemaName: "PUBLIC", constraintName: "FKB971369C7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-107") {
		addForeignKeyConstraint(baseColumnNames: "DATA_TYPE_ID", baseTableName: "INTENT", baseTableSchemaName: "PUBLIC", constraintName: "FKB971369C283F938E", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "INTENT_DATA_TYPE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-108") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "INTENT", baseTableSchemaName: "PUBLIC", constraintName: "FKB971369CE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-109") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "INTENT_ACTION", baseTableSchemaName: "PUBLIC", constraintName: "FKEBCDD397666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-110") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "INTENT_ACTION", baseTableSchemaName: "PUBLIC", constraintName: "FKEBCDD39E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-111") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "INTENT_DATA_TYPE", baseTableSchemaName: "PUBLIC", constraintName: "FKEADB30CC7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-112") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "INTENT_DATA_TYPE", baseTableSchemaName: "PUBLIC", constraintName: "FKEADB30CCE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-113") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "INTENT_DIRECTION", baseTableSchemaName: "PUBLIC", constraintName: "FKC723A59C7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-114") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "INTENT_DIRECTION", baseTableSchemaName: "PUBLIC", constraintName: "FKC723A59CE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-115") {
		addForeignKeyConstraint(baseColumnNames: "AUTHOR_ID", baseTableName: "ITEM_COMMENT", baseTableSchemaName: "PUBLIC", constraintName: "FKE6D04D335A032135", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-116") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "ITEM_COMMENT", baseTableSchemaName: "PUBLIC", constraintName: "FKE6D04D337666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-117") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "ITEM_COMMENT", baseTableSchemaName: "PUBLIC", constraintName: "FKE6D04D33E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-118") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_ID", baseTableName: "ITEM_COMMENT", baseTableSchemaName: "PUBLIC", constraintName: "FKE6D04D33C7E5C662", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-119") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "OWF_PROPERTIES", baseTableSchemaName: "PUBLIC", constraintName: "FKE88638947666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-120") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "OWF_PROPERTIES", baseTableSchemaName: "PUBLIC", constraintName: "FKE8863894E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-121") {
		addForeignKeyConstraint(baseColumnNames: "INTENT_ID", baseTableName: "OWF_PROPERTIES_INTENT", baseTableSchemaName: "PUBLIC", constraintName: "FK3F99ECA7A651895D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "INTENT", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-122") {
		addForeignKeyConstraint(baseColumnNames: "OWF_PROPERTIES_INTENTS_ID", baseTableName: "OWF_PROPERTIES_INTENT", baseTableSchemaName: "PUBLIC", constraintName: "FK3F99ECA74704E25C", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "OWF_PROPERTIES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-123") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "OWF_WIDGET_TYPES", baseTableSchemaName: "PUBLIC", constraintName: "FK6AB6A9DF7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-124") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "OWF_WIDGET_TYPES", baseTableSchemaName: "PUBLIC", constraintName: "FK6AB6A9DFE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-125") {
		addForeignKeyConstraint(baseColumnNames: "AVATAR_ID", baseTableName: "PROFILE", baseTableSchemaName: "PUBLIC", constraintName: "FKED8E89A961C3343D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "AVATAR", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-126") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "PROFILE", baseTableSchemaName: "PUBLIC", constraintName: "FKED8E89A97666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-127") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "PROFILE", baseTableSchemaName: "PUBLIC", constraintName: "FKED8E89A9E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-128") {
		addForeignKeyConstraint(baseColumnNames: "REJECTION_LISTING_ID", baseTableName: "REJECTION_ACTIVITY", baseTableSchemaName: "PUBLIC", constraintName: "FKF35C128582548A4A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "REJECTION_LISTING", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-129") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "REJECTION_JUSTIFICATION", baseTableSchemaName: "PUBLIC", constraintName: "FK12B0A53C7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-130") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "REJECTION_JUSTIFICATION", baseTableSchemaName: "PUBLIC", constraintName: "FK12B0A53CE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-131") {
		addForeignKeyConstraint(baseColumnNames: "AUTHOR_ID", baseTableName: "REJECTION_LISTING", baseTableSchemaName: "PUBLIC", constraintName: "FK3F2BD44E5A032135", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-132") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "REJECTION_LISTING", baseTableSchemaName: "PUBLIC", constraintName: "FK3F2BD44E7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-133") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "REJECTION_LISTING", baseTableSchemaName: "PUBLIC", constraintName: "FK3F2BD44EE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-134") {
		addForeignKeyConstraint(baseColumnNames: "JUSTIFICATION_ID", baseTableName: "REJECTION_LISTING", baseTableSchemaName: "PUBLIC", constraintName: "FK3F2BD44E19CEB614", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "REJECTION_JUSTIFICATION", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-135") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_ID", baseTableName: "REJECTION_LISTING", baseTableSchemaName: "PUBLIC", constraintName: "FK3F2BD44EC7E5C662", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-136") {
		addForeignKeyConstraint(baseColumnNames: "OWNING_ENTITY_ID", baseTableName: "RELATIONSHIP", baseTableSchemaName: "PUBLIC", constraintName: "FKF06476389D70DD39", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-137") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_SNAPSHOT_ID", baseTableName: "RELATIONSHIP_ACTIVITY_LOG", baseTableSchemaName: "PUBLIC", constraintName: "FK594974BB25A20F9D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM_SNAPSHOT", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-138") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_ID", baseTableName: "RELATIONSHIP_SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FKDA02504C7E5C662", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-139") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "SCORE_CARD", baseTableSchemaName: "PUBLIC", constraintName: "FK5E60409D7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-140") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "SCORE_CARD", baseTableSchemaName: "PUBLIC", constraintName: "FK5E60409DE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-141") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "SCORE_CARD_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FKE51CCD757666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-142") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "SCORE_CARD_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FKE51CCD75E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-143") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "SCORE_CARD_ITEM_RESPONSE", baseTableSchemaName: "PUBLIC", constraintName: "FK80A6CBCB7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-144") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "SCORE_CARD_ITEM_RESPONSE", baseTableSchemaName: "PUBLIC", constraintName: "FK80A6CBCBE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-145") {
		addForeignKeyConstraint(baseColumnNames: "SCORE_CARD_ID", baseTableName: "SCORE_CARD_ITEM_RESPONSE", baseTableSchemaName: "PUBLIC", constraintName: "FK80A6CBCB190E00BC", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SCORE_CARD", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-146") {
		addForeignKeyConstraint(baseColumnNames: "SCORE_CARD_ITEM_ID", baseTableName: "SCORE_CARD_ITEM_RESPONSE", baseTableSchemaName: "PUBLIC", constraintName: "FK80A6CBCBEF469C97", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SCORE_CARD_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-147") {
		addForeignKeyConstraint(baseColumnNames: "AUTHOR_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565D5A032135", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-148") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565D7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-149") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565DE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-150") {
		addForeignKeyConstraint(baseColumnNames: "LAST_ACTIVITY_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565D2746B676", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM_ACTIVITY", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-151") {
		addForeignKeyConstraint(baseColumnNames: "OWF_PROPERTIES_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565D904D6974", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "OWF_PROPERTIES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-152") {
		addForeignKeyConstraint(baseColumnNames: "SCORE_CARD_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565D190E00BC", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SCORE_CARD", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-153") {
		addForeignKeyConstraint(baseColumnNames: "STATE_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565DDFEC3E97", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "STATE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-154") {
		addForeignKeyConstraint(baseColumnNames: "TYPES_ID", baseTableName: "SERVICE_ITEM", baseTableSchemaName: "PUBLIC", constraintName: "FK1571565D6928D597", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "TYPES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-155") {
		addForeignKeyConstraint(baseColumnNames: "AUTHOR_ID", baseTableName: "SERVICE_ITEM_ACTIVITY", baseTableSchemaName: "PUBLIC", constraintName: "FK870EA6B15A032135", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-156") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "SERVICE_ITEM_ACTIVITY", baseTableSchemaName: "PUBLIC", constraintName: "FK870EA6B17666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-157") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "SERVICE_ITEM_ACTIVITY", baseTableSchemaName: "PUBLIC", constraintName: "FK870EA6B1E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-158") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_ID", baseTableName: "SERVICE_ITEM_ACTIVITY", baseTableSchemaName: "PUBLIC", constraintName: "FK870EA6B1C7E5C662", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-159") {
		addForeignKeyConstraint(baseColumnNames: "CATEGORY_ID", baseTableName: "SERVICE_ITEM_CATEGORY", baseTableSchemaName: "PUBLIC", constraintName: "FKECC570A0DA41995D", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "CATEGORY", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-160") {
		addForeignKeyConstraint(baseColumnNames: "CUSTOM_FIELD_ID", baseTableName: "SERVICE_ITEM_CUSTOM_FIELD", baseTableSchemaName: "PUBLIC", constraintName: "FK46E9894E7B56E054", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "CUSTOM_FIELD", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-161") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_ID", baseTableName: "SERVICE_ITEM_SNAPSHOT", baseTableSchemaName: "PUBLIC", constraintName: "FKFABD8966C7E5C662", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-162") {
		addForeignKeyConstraint(baseColumnNames: "SERVICE_ITEM_ID", baseTableName: "SI_RECOMMENDED_LAYOUTS", baseTableSchemaName: "PUBLIC", constraintName: "FK863C793CC7E5C662", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SERVICE_ITEM", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-163") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "STATE", baseTableSchemaName: "PUBLIC", constraintName: "FK68AC4917666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-164") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "STATE", baseTableSchemaName: "PUBLIC", constraintName: "FK68AC491E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-165") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "TEXT", baseTableSchemaName: "PUBLIC", constraintName: "FK36452D7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-166") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "TEXT", baseTableSchemaName: "PUBLIC", constraintName: "FK36452DE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-167") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "TYPES", baseTableSchemaName: "PUBLIC", constraintName: "FK69B58797666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-168") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "TYPES", baseTableSchemaName: "PUBLIC", constraintName: "FK69B5879E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-169") {
		addForeignKeyConstraint(baseColumnNames: "IMAGE_ID", baseTableName: "TYPES", baseTableSchemaName: "PUBLIC", constraintName: "FK69B5879553AF61A", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "IMAGES", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-170") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "U_DOMAIN", baseTableSchemaName: "PUBLIC", constraintName: "FK97BAABEE7666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-171") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "U_DOMAIN", baseTableSchemaName: "PUBLIC", constraintName: "FK97BAABEEE31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-172") {
		addForeignKeyConstraint(baseColumnNames: "CREATED_BY_ID", baseTableName: "USER_ACCOUNT", baseTableSchemaName: "PUBLIC", constraintName: "FK14C321B97666C6D2", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}

	changeSet(author: "marketplace", context: "create", dbms: "hsqldb", id: "1375784599440-173") {
		addForeignKeyConstraint(baseColumnNames: "EDITED_BY_ID", baseTableName: "USER_ACCOUNT", baseTableSchemaName: "PUBLIC", constraintName: "FK14C321B9E31CB353", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "PROFILE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
	}
}
