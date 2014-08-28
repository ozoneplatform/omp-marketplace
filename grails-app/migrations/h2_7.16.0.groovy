databaseChangeLog = {

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-1") {
        createTable(tableName: "affiliated_marketplace") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "affiliated_maPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "active", type: "integer") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

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

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-2") {
        createTable(tableName: "agency") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "agencyPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "icon_url", type: "varchar(2000)") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-3") {
        createTable(tableName: "application_configuration") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "application_cPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "code", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(2000)")

            column(name: "group_name", type: "varchar(250)") {
                constraints(nullable: "false")
            }

            column(name: "help", type: "varchar(250)")

            column(name: "mutable", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "sub_group_name", type: "varchar(250)")

            column(name: "sub_group_order", type: "integer")

            column(name: "title", type: "varchar(250)") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "varchar(250)") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "varchar(2000)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-4") {
        createTable(tableName: "avatar") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "avatarPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "content_type", type: "varchar(255)")

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "is_default", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "pic", type: "binary(10485760)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-5") {
        createTable(tableName: "category") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "categoryPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(250)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-6") {
        createTable(tableName: "change_detail") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "change_detailPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "field_name", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "new_value", type: "varchar(4000)")

            column(name: "old_value", type: "varchar(4000)")

            column(name: "service_item_activity_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-7") {
        createTable(tableName: "check_box_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfPK")
            }

            column name: 'value', type: 'boolean'
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-8") {
        createTable(tableName: "check_box_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfdPK")
            }

            column(name: "selected_by_default", type: "boolean")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-9") {
        createTable(tableName: "contact") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "contactPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "varchar(100)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "varchar(100)") {
                constraints(nullable: "false")
            }

            column(name: "organization", type: "varchar(100)")

            column(name: "secure_phone", type: "varchar(50)")

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "type_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "unsecure_phone", type: "varchar(50)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-10") {
        createTable(tableName: "contact_type") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "contact_typePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "required", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(50)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-11") {
        createTable(tableName: "custom_field") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "custom_fieldPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "custom_field_definition_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-12") {
        createTable(tableName: "custom_field_definition") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "custom_field_PK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "all_types", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(250)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "is_permanent", type: "boolean")

            column(name: "is_required", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "label", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "section", type: "varchar(255)")

            column(name: "style_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "tooltip", type: "varchar(50)")

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-13") {
        createTable(tableName: "custom_field_definition_types") {
            column(name: "cf_definition_types_id", type: "bigint")

            column(name: "types_id", type: "bigint")

            column(name: "types_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-14") {
        createTable(tableName: "default_images") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "default_imagePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "defined_default_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-15") {
        createTable(tableName: "drop_down_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "drop_down_cfPK")
            }

            column(name: "value_id", type: "bigint")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-16") {
        createTable(tableName: "drop_down_cf_field_value") {
            column(name: "drop_down_cf_field_value_id", type: "bigint")

            column(name: "field_value_id", type: "bigint")

            column(name: "field_value_list_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-17") {
        createTable(tableName: "drop_down_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "drop_down_cfdPK")
            }

            column(name: "is_multi_select", type: "boolean")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-18") {
        createTable(tableName: "ext_profile") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "ext_profilePK")
            }

            column(name: "external_edit_url", type: "varchar(2083)")

            column(name: "external_id", type: "varchar(255)")

            column(name: "external_view_url", type: "varchar(2083)")

            column(name: "system_uri", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-19") {
        createTable(tableName: "ext_service_item") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "ext_service_iPK")
            }

            column(name: "external_edit_url", type: "varchar(2083)")

            column(name: "external_id", type: "varchar(256)")

            column(name: "external_view_url", type: "varchar(2083)")

            column(name: "system_uri", type: "varchar(256)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-20") {
        createTable(tableName: "field_value") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "field_valuePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "custom_field_definition_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "display_text", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "is_enabled", type: "integer") {
                constraints(nullable: "false")
            }

            column(name: "field_values_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-21") {
        createTable(tableName: "image_url_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfPK")
            }

            column(name: "value", type: "varchar(2083)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-22") {
        createTable(tableName: "image_url_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfdPK")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-23") {
        createTable(tableName: "images") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "imagesPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "bytes", type: "binary(10485760)") {
                constraints(nullable: "false")
            }

            column(name: "content_type", type: "varchar(255)")

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "image_size", type: "double")

            column(name: "is_default", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-24") {
        createTable(tableName: "import_task") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "import_taskPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "cron_exp", type: "varchar(255)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "enabled", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "exec_interval", type: "integer")

            column(name: "extra_url_params", type: "varchar(512)")

            column(name: "interface_config_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "keystore_pass", type: "varchar(2048)")

            column(name: "keystore_path", type: "varchar(2048)")

            column(name: "last_run_result_id", type: "bigint")

            column(name: "name", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "truststore_path", type: "varchar(2048)")

            column(name: "update_type", type: "varchar(7)") {
                constraints(nullable: "false")
            }

            column(name: "url", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-25") {
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

            column(name: "result", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "run_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "task_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-26") {
        createTable(tableName: "intent") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intentPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "action_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "data_type_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "receive", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "send", type: "boolean") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-27") {
        createTable(tableName: "intent_action") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intent_actionPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(256)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(256)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-28") {
        createTable(tableName: "intent_data_type") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intent_data_tPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(256)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(256)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-29") {
        createTable(tableName: "intent_direction") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intent_directPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(250)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(7)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-30") {
        createTable(tableName: "interface_configuration") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "interface_conPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "allow_truncate", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "auto_create_meta_data", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "default_large_icon_url", type: "varchar(2048)")

            column(name: "default_small_icon_url", type: "varchar(2048)")

            column(name: "delta_since_time_param", type: "varchar(64)")

            column(name: "delta_static_parameters", type: "varchar(2048)")

            column(name: "download_images", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "full_static_parameters", type: "varchar(2048)")

            column(name: "loose_match", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "varchar(256)") {
                constraints(nullable: "false")
            }

            column(name: "query_date_format", type: "varchar(32)")

            column(name: "response_date_format", type: "varchar(32)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-31") {
        createTable(tableName: "item_comment") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "item_commentPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "author_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "rate", type: "float")

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "text", type: "varchar(4000)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-32") {
        createTable(tableName: "modify_relationship_activity") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "modify_relatiPK")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-33") {
        createTable(tableName: "owf_properties") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "owf_propertiePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "background", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "mobile_ready", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "descriptor_url", type: "varchar(2083)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "height", type: "bigint")

            column(name: "owf_widget_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "singleton", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "stack_context", type: "varchar(200)")

            column(name: "stack_descriptor", type: "longvarchar")

            column(name: "universal_name", type: "varchar(255)")

            column(name: "visible_in_launch", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "width", type: "bigint")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-34") {
        createTable(tableName: "owf_properties_intent") {
            column(name: "owf_properties_intents_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "intent_id", type: "bigint")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-35") {
        createTable(tableName: "owf_widget_types") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "owf_widget_tyPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-36") {
        createTable(tableName: "profile") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "profilePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "avatar_id", type: "bigint")

            column(name: "bio", type: "varchar(1000)")

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "display_name", type: "varchar(256)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "varchar(256)")

            column(name: "user_roles", type: "varchar(255)")

            column(name: "username", type: "varchar(256)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-37") {
        createTable(tableName: "rejection_activity") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rejection_actPK")
            }

            column(name: "rejection_listing_id", type: "bigint")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-38") {
        createTable(tableName: "rejection_justification") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rejection_jusPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(250)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(50)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-39") {
        createTable(tableName: "rejection_listing") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rejection_lisPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "author_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(2000)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "justification_id", type: "bigint")

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-40") {
        createTable(tableName: "relationship") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "relationshipPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "owning_entity_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "relationship_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-41") {
        createTable(tableName: "relationship_activity_log") {
            column(name: "mod_rel_activity_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "service_item_snapshot_id", type: "bigint")

            column(name: "items_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-42") {
        createTable(tableName: "relationship_service_item") {
            column(name: "relationship_related_items_id", type: "bigint")

            column(name: "service_item_id", type: "bigint")

            column(name: "related_items_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-43") {
        createTable(tableName: "score_card_item") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "score_card_itPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(500)") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "image", type: "varchar(250)")

            column(name: "question", type: "varchar(250)") {
                constraints(nullable: "false")
            }

            column(name: "show_on_listing", type: "boolean")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-44") {
        createTable(tableName: "screenshot") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "screenshotPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "large_image_url", type: "varchar(2083)")

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "small_image_url", type: "varchar(2083)") {
                constraints(nullable: "false")
            }

            column(name: "ordinal", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-45") {
        createTable(tableName: "service_item") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "service_itemPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "agency_id", type: "bigint")

            column(name: "approval_status", type: "varchar(11)") {
                constraints(nullable: "false")
            }

            column(name: "approved_date", type: "timestamp")

            column(name: "avg_rate", type: "float") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "dependencies", type: "varchar(1000)")

            column(name: "description", type: "varchar(4000)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "image_large_url", type: "varchar(2083)")

            column(name: "image_medium_url", type: "varchar(2083)")

            column(name: "image_small_url", type: "varchar(2083)")

            column(name: "install_url", type: "varchar(2083)")

            column(name: "is_hidden", type: "integer") {
                constraints(nullable: "false")
            }

            column(name: "is_outside", type: "boolean")

            column(name: "last_activity_id", type: "bigint")

            column(name: "launch_url", type: "varchar(2083)")

            column(name: "opens_in_new_browser_tab", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "organization", type: "varchar(256)")

            column(name: "owf_properties_id", type: "bigint")

            column(name: "release_date", type: "timestamp")

            column(name: "requirements", type: "varchar(1000)")

            column(name: "state_id", type: "bigint")

            column(name: "title", type: "varchar(256)") {
                constraints(nullable: "false")
            }

            column(name: "total_comments", type: "integer") {
                constraints(nullable: "false")
            }

            column(name: "total_rate1", type: "integer")

            column(name: "total_rate2", type: "integer")

            column(name: "total_rate3", type: "integer")

            column(name: "total_rate4", type: "integer")

            column(name: "total_rate5", type: "integer")

            column(name: "total_votes", type: "integer") {
                constraints(nullable: "false")
            }

            column(name: "types_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "version_name", type: "varchar(256)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-46") {
        createTable(tableName: "service_item_activity") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "service_item_PK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "action", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "activity_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "author_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "service_item_activities_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-47") {
        createTable(tableName: "service_item_category") {
            column(name: "service_item_categories_id", type: "bigint")

            column(name: "category_id", type: "bigint")

            column(name: "categories_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-48") {
        createTable(tableName: "service_item_custom_field") {
            column(name: "service_item_custom_fields_id", type: "bigint")

            column(name: "custom_field_id", type: "bigint")

            column(name: "custom_fields_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-49") {
        createTable(tableName: "service_item_documentation_url") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "service_item_PK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "url", type: "varchar(2083)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-50") {
        createTable(tableName: "service_item_profile") {
            column(name: "service_item_owners_id", type: "bigint")

            column(name: "profile_id", type: "bigint")

            column(name: "owners_idx", type: "integer")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-51") {
        createTable(tableName: "service_item_score_card_item") {
            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "score_card_item_id", type: "bigint")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-52") {
        createTable(tableName: "service_item_snapshot") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "service_item_PK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: "bigint")

            column(name: "title", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-53") {
        createTable(tableName: "service_item_tag") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "service_item_PK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-54") {
        createTable(tableName: "service_item_tech_pocs") {
            column(name: "service_item_id", type: "bigint")

            column(name: "tech_poc", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-55") {
        createTable(tableName: "si_recommended_layouts") {
            column(name: "service_item_id", type: "bigint")

            column(name: "recommended_layout", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-56") {
        createTable(tableName: "state") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "statePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(250)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "is_published", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-57") {
        createTable(tableName: "tag") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "tagPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(16)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-58") {
        createTable(tableName: "text") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "textPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "read_only", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "varchar(250)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-59") {
        createTable(tableName: "text_area_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfPK")
            }

            column(name: "value", type: "varchar(4000)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-60") {
        createTable(tableName: "text_area_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfdPK")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-61") {
        createTable(tableName: "text_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_cfPK")
            }

            column(name: "value", type: "varchar(256)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-62") {
        createTable(tableName: "text_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_cfdPK")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-63") {
        createTable(tableName: "types") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "typesPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "varchar(250)")

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "has_icons", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "has_launch_url", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "bigint")

            column(name: "is_permanent", type: "boolean")

            column(name: "ozone_aware", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "title", type: "varchar(50)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "varchar(255)")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-64") {
        createTable(tableName: "U_DOMAIN") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "U_DOMAINPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "username", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-65") {
        createTable(tableName: "u_domain_preferences") {
            column(name: "preferences", type: "bigint")

            column(name: "preferences_idx", type: "varchar(255)")

            column(name: "preferences_elt", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-66") {
        createTable(tableName: "user_account") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "user_accountPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: "bigint")

            column(name: "created_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: "bigint")

            column(name: "edited_date", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_login", type: "timestamp")

            column(name: "username", type: "varchar(250)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-193") {
        createIndex(indexName: "app_config_group_name_idx", tableName: "application_configuration") {
            column(name: "group_name")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-194") {
        createIndex(indexName: "uuid_uniq_1402673669660", tableName: "category", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-195") {
        createIndex(indexName: "title_uniq_1402673669666", tableName: "contact_type", unique: "true") {
            column(name: "title")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-196") {
        createIndex(indexName: "uuid_uniq_1402673669669", tableName: "custom_field_definition", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-197") {
        addUniqueConstraint tableName: 'ext_profile', columnNames: 'system_uri, external_id'
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-198") {
        addUniqueConstraint tableName: 'ext_service_item', columnNames: 'system_uri, external_id'
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-199") {
        createIndex(indexName: "name_uniq_1402673669683", tableName: "import_task", unique: "true") {
            column(name: "name")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-200") {
        createIndex(indexName: "uuid_uniq_1402673669686", tableName: "intent_action", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-201") {
        createIndex(indexName: "uuid_uniq_1402673669687", tableName: "intent_data_type", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-202") {
        createIndex(indexName: "title_uniq_1402673669688", tableName: "intent_direction", unique: "true") {
            column(name: "title")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-203") {
        createIndex(indexName: "uuid_uniq_1402673669689", tableName: "intent_direction", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-204") {
        createIndex(indexName: "itm_cmnt_author_id_idx", tableName: "item_comment") {
            column(name: "author_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-205") {
        createIndex(indexName: "itm_cmnt_svc_item_id_idx", tableName: "item_comment") {
            column(name: "service_item_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-206") {
        createIndex(indexName: "uuid_uniq_1402673669695", tableName: "owf_widget_types", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-207") {
        createIndex(indexName: "username_uniq_1402673669697", tableName: "profile", unique: "true") {
            column(name: "username")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-208") {
        createIndex(indexName: "uuid_uniq_1402673669697", tableName: "profile", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-209") {
        createIndex(indexName: "rej_lst_author_id_idx", tableName: "rejection_listing") {
            column(name: "author_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-210") {
        createIndex(indexName: "rej_lst_svc_item_id_idx", tableName: "rejection_listing") {
            column(name: "service_item_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-211") {
        createIndex(indexName: "svc_item_act_svc_item_id_idx", tableName: "service_item_activity") {
            column(name: "service_item_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-212") {
        createIndex(indexName: "svc_item_cat_id_idx", tableName: "service_item_category") {
            column(name: "service_item_categories_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-213") {
        createIndex(indexName: "svc_item_cst_fld_id_idx", tableName: "service_item_custom_field") {
            column(name: "service_item_custom_fields_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-214") {
        createIndex(indexName: "service_item_tag_si_idx", tableName: "service_item_tag") {
            column(name: "service_item_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-215") {
        createIndex(indexName: "service_item_tag_tag_idx", tableName: "service_item_tag") {
            column(name: "tag_id")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-216") {
        createIndex(indexName: "uuid_uniq_1402673669716", tableName: "state", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-217") {
        createIndex(indexName: "tag_title_idx", tableName: "tag") {
            column(name: "title")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-218") {
        createIndex(indexName: "name_uniq_1402673669718", tableName: "text", unique: "true") {
            column(name: "name")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-219") {
        createIndex(indexName: "uuid_uniq_1402673669720", tableName: "types", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-220") {
        createIndex(indexName: "username_uniq_1402673669721", tableName: "user_account", unique: "true") {
            column(name: "username")
        }
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-67") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "affiliated_marketplace", constraintName: "FKA6EB2C37666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-68") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "affiliated_marketplace", constraintName: "FKA6EB2C3E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-69") {
        addForeignKeyConstraint(baseColumnNames: "icon_id", baseTableName: "affiliated_marketplace", constraintName: "FKA6EB2C3EA25263C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "images", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-70") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "agency", constraintName: "FKAB611C057666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-71") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "agency", constraintName: "FKAB611C05E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-72") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "avatar", constraintName: "FKAC32C1597666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-73") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "avatar", constraintName: "FKAC32C159E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-74") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "category", constraintName: "FK302BCFE7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-75") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "category", constraintName: "FK302BCFEE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-76") {
        addForeignKeyConstraint(baseColumnNames: "service_item_activity_id", baseTableName: "change_detail", constraintName: "FKB4467BC0855307BD", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item_activity", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-77") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "check_box_cf", constraintName: "FKE828FA6E7F5081E1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-78") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "check_box_cfd", constraintName: "FK1CF653B69F8CD3D4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field_definition", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-79") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "contact", constraintName: "FK38B724207666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-80") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "contact", constraintName: "FK38B72420E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-81") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "contact", constraintName: "FK38B72420C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-82") {
        addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "contact", constraintName: "FK38B72420BA3FC877", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "contact_type", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-83") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "contact_type", constraintName: "FK4C2BB7F97666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-84") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "contact_type", constraintName: "FK4C2BB7F9E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-85") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "custom_field", constraintName: "FK2ACD76AC7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-86") {
        addForeignKeyConstraint(baseColumnNames: "custom_field_definition_id", baseTableName: "custom_field", constraintName: "FK2ACD76AC6F62C9ED", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field_definition", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-87") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "custom_field", constraintName: "FK2ACD76ACE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-88") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "custom_field_definition", constraintName: "FK150F70C67666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-89") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "custom_field_definition", constraintName: "FK150F70C6E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-90") {
        addForeignKeyConstraint(baseColumnNames: "types_id", baseTableName: "custom_field_definition_types", constraintName: "FK1A84FFC06928D597", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "types", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-91") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "default_images", constraintName: "FK6F064E367666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-92") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "default_images", constraintName: "FK6F064E36E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-93") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "default_images", constraintName: "FK6F064E36553AF61A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "images", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-94") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "drop_down_cf", constraintName: "FK13ADE7D07F5081E1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-95") {
        addForeignKeyConstraint(baseColumnNames: "value_id", baseTableName: "drop_down_cf", constraintName: "FK13ADE7D0BC98CEE3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "field_value", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-96") {
        addForeignKeyConstraint(baseColumnNames: "field_value_id", baseTableName: "drop_down_cf_field_value", constraintName: "FK2627FFDDA5BD888", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "field_value", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-97") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "drop_down_cfd", constraintName: "FK620F12949F8CD3D4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field_definition", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-98") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "ext_profile", constraintName: "FKE9C8098B20F4E01", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-99") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "ext_service_item", constraintName: "FKDF673BB77B33140", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-100") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "field_value", constraintName: "FK29F571EC7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-101") {
        addForeignKeyConstraint(baseColumnNames: "custom_field_definition_id", baseTableName: "field_value", constraintName: "FK29F571ECF1F14D3C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "drop_down_cfd", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-102") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "field_value", constraintName: "FK29F571ECE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-103") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "image_url_cf", constraintName: "FK300028977F5081E1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-104") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "image_url_cfd", constraintName: "FKD004EAAD9F8CD3D4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field_definition", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-105") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "images", constraintName: "FKB95A82787666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-106") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "images", constraintName: "FKB95A8278E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-107") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "import_task", constraintName: "FK578EF9DF7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-108") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "import_task", constraintName: "FK578EF9DFE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-109") {
        addForeignKeyConstraint(baseColumnNames: "interface_config_id", baseTableName: "import_task", constraintName: "FK578EF9DFA31F8712", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "interface_configuration", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-110") {
        addForeignKeyConstraint(baseColumnNames: "last_run_result_id", baseTableName: "import_task", constraintName: "FK578EF9DF919216CA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "import_task_result", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-111") {
        addForeignKeyConstraint(baseColumnNames: "task_id", baseTableName: "import_task_result", constraintName: "FK983AC27D11D7F882", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "import_task", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-112") {
        addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "intent", constraintName: "FKB971369CD8544299", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "intent_action", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-113") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent", constraintName: "FKB971369C7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-114") {
        addForeignKeyConstraint(baseColumnNames: "data_type_id", baseTableName: "intent", constraintName: "FKB971369C283F938E", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "intent_data_type", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-115") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent", constraintName: "FKB971369CE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-116") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent_action", constraintName: "FKEBCDD397666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-117") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent_action", constraintName: "FKEBCDD39E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-118") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent_data_type", constraintName: "FKEADB30CC7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-119") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent_data_type", constraintName: "FKEADB30CCE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-120") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent_direction", constraintName: "FKC723A59C7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-121") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent_direction", constraintName: "FKC723A59CE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-122") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "item_comment", constraintName: "FKE6D04D335A032135", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-123") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "item_comment", constraintName: "FKE6D04D337666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-124") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "item_comment", constraintName: "FKE6D04D33E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-125") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "item_comment", constraintName: "FKE6D04D33C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-126") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "modify_relationship_activity", constraintName: "FKE68D3F71C359936F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item_activity", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-127") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "owf_properties", constraintName: "FKE88638947666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-128") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "owf_properties", constraintName: "FKE8863894E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-129") {
        addForeignKeyConstraint(baseColumnNames: "intent_id", baseTableName: "owf_properties_intent", constraintName: "FK3F99ECA7A651895D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "intent", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-130") {
        addForeignKeyConstraint(baseColumnNames: "owf_properties_intents_id", baseTableName: "owf_properties_intent", constraintName: "FK3F99ECA74704E25C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "owf_properties", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-131") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "owf_widget_types", constraintName: "FK6AB6A9DF7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-132") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "owf_widget_types", constraintName: "FK6AB6A9DFE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-133") {
        addForeignKeyConstraint(baseColumnNames: "avatar_id", baseTableName: "profile", constraintName: "FKED8E89A961C3343D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "avatar", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-134") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "profile", constraintName: "FKED8E89A97666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-135") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "profile", constraintName: "FKED8E89A9E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-136") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "rejection_activity", constraintName: "FKF35C1285C359936F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item_activity", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-137") {
        addForeignKeyConstraint(baseColumnNames: "rejection_listing_id", baseTableName: "rejection_activity", constraintName: "FKF35C128582548A4A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "rejection_listing", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-138") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "rejection_justification", constraintName: "FK12B0A53C7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-139") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "rejection_justification", constraintName: "FK12B0A53CE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-140") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "rejection_listing", constraintName: "FK3F2BD44E5A032135", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-141") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "rejection_listing", constraintName: "FK3F2BD44E7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-142") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "rejection_listing", constraintName: "FK3F2BD44EE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-143") {
        addForeignKeyConstraint(baseColumnNames: "justification_id", baseTableName: "rejection_listing", constraintName: "FK3F2BD44E19CEB614", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "rejection_justification", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-144") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "rejection_listing", constraintName: "FK3F2BD44EC7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-145") {
        addForeignKeyConstraint(baseColumnNames: "owning_entity_id", baseTableName: "relationship", constraintName: "FKF06476389D70DD39", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-146") {
        addForeignKeyConstraint(baseColumnNames: "service_item_snapshot_id", baseTableName: "relationship_activity_log", constraintName: "FK594974BB25A20F9D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item_snapshot", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-147") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "relationship_service_item", constraintName: "FKDA02504C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-148") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "score_card_item", constraintName: "FKE51CCD757666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-149") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "score_card_item", constraintName: "FKE51CCD75E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-150") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "screenshot", constraintName: "FKE72D85667666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-151") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "screenshot", constraintName: "FKE72D8566E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-152") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "screenshot", constraintName: "FKE72D8566C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-153") {
        addForeignKeyConstraint(baseColumnNames: "agency_id", baseTableName: "service_item", constraintName: "FK1571565D143B24BD", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "agency", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-154") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "service_item", constraintName: "FK1571565D7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-155") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "service_item", constraintName: "FK1571565DE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-156") {
        addForeignKeyConstraint(baseColumnNames: "last_activity_id", baseTableName: "service_item", constraintName: "FK1571565D2746B676", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item_activity", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-157") {
        addForeignKeyConstraint(baseColumnNames: "owf_properties_id", baseTableName: "service_item", constraintName: "FK1571565D904D6974", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "owf_properties", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-158") {
        addForeignKeyConstraint(baseColumnNames: "state_id", baseTableName: "service_item", constraintName: "FK1571565DDFEC3E97", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-159") {
        addForeignKeyConstraint(baseColumnNames: "types_id", baseTableName: "service_item", constraintName: "FK1571565D6928D597", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "types", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-160") {
        addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "service_item_activity", constraintName: "FK870EA6B15A032135", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-161") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "service_item_activity", constraintName: "FK870EA6B17666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-162") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "service_item_activity", constraintName: "FK870EA6B1E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-163") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_activity", constraintName: "FK870EA6B1C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-164") {
        addForeignKeyConstraint(baseColumnNames: "category_id", baseTableName: "service_item_category", constraintName: "FKECC570A0DA41995D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "category", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-165") {
        addForeignKeyConstraint(baseColumnNames: "custom_field_id", baseTableName: "service_item_custom_field", constraintName: "FK46E9894E7B56E054", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-166") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_documentation_url", constraintName: "FK24572D08C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-167") {
        addForeignKeyConstraint(baseColumnNames: "profile_id", baseTableName: "service_item_profile", constraintName: "FK68B5D9C7C0565C57", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-168") {
        addForeignKeyConstraint(baseColumnNames: "score_card_item_id", baseTableName: "service_item_score_card_item", constraintName: "FKBF91F93EF469C97", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "score_card_item", referencesUniqueColumn: "false", onDelete: 'CASCADE')
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-169") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_score_card_item", constraintName: "FKBF91F93C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-170") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_snapshot", constraintName: "FKFABD8966C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-171") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "service_item_tag", constraintName: "FKB621CEB87666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-172") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_tag", constraintName: "FKB621CEB8C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-173") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "service_item_tag", constraintName: "FKB621CEB8EACAF137", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-174") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_tech_pocs", constraintName: "FKA55CFB56C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-175") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "si_recommended_layouts", constraintName: "FK863C793CC7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-176") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "state", constraintName: "FK68AC4917666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-177") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "state", constraintName: "FK68AC491E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-178") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "tag", constraintName: "FK1BF9A7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-179") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "tag", constraintName: "FK1BF9AE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-180") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "text", constraintName: "FK36452D7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-181") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "text", constraintName: "FK36452DE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-182") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "text_area_cf", constraintName: "FK4C3A28437F5081E1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-183") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "text_area_cfd", constraintName: "FK3B0AE0819F8CD3D4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field_definition", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-184") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "text_cf", constraintName: "FKAB7D80B57F5081E1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-185") {
        addForeignKeyConstraint(baseColumnNames: "id", baseTableName: "text_cfd", constraintName: "FKC432964F9F8CD3D4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_field_definition", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-186") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "types", constraintName: "FK69B58797666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-187") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "types", constraintName: "FK69B5879E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-188") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "types", constraintName: "FK69B5879553AF61A", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "images", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-189") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "U_DOMAIN", constraintName: "FK97BAABEE7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-190") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "U_DOMAIN", constraintName: "FK97BAABEEE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-191") {
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "user_account", constraintName: "FK14C321B97666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }

    changeSet(author: 'marketplace', context: 'create', dbms: 'h2', id: "1402673669851-192") {
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "user_account", constraintName: "FK14C321B9E31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
    }
}
