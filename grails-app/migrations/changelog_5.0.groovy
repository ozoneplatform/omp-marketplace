databaseChangeLog = {

// use 'create' context for changesets that should be part of creation script
// use version number context (e.g., 5.0) to denote the changesets which should go into upgrade scripts for that version 
// favor making database agnostic changeSets - however if needed the dbms attribute can be set to make a db specific changeset


    changeSet(author: "marketplace", id: "5.0-1", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
        comment("Drop types.role_access and u_views table")
        dropColumn(columnName: "role_access", tableName: "types")

        dropTable(tableName: "U_VIEWS")
    }

    // TODO: Michael, the ids below should be the same for the four databases?
    //			column(name: "total_rate1", type: "int(11)") {
    changeSet(author: "marketplace", dbms: "mysql", id: "5.0-2", context: "create, 5.0") {
        comment("Add columns for counting votes to service_item")
        addColumn(tableName: "service_item") {
            column(name: "total_rate1", type: "integer") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate2", type: "integer") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate3", type: "integer") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate4", type: "integer") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate5", type: "integer") {
                constraints(nullable: "true")
            }
        }
    } // end of mysql

    changeSet(author: "marketplace", dbms: "oracle", id: "5.0-2", context: "create, 5.0") {
        comment("Add columns for counting votes to service_item")
        addColumn(tableName: "service_item") {
            column(name: "total_rate1", type: "number(10,0)") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate2", type: "number(10,0)") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate3", type: "number(10,0)") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate4", type: "number(10,0)") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate5", type: "number(10,0)") {
                constraints(nullable: "true")
            }
        }
    } // end of oracle

    changeSet(author: "marketplace", dbms: "postgresql", id: "5.0-2", context: "create, 5.0") {
        comment("Add columns for counting votes to service_item")
        addColumn(tableName: "service_item") {
            column(name: "total_rate1", type: "int4") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate2", type: "int4") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate3", type: "int4") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate4", type: "int4") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate5", type: "int4") {
                constraints(nullable: "true")
            }
        }
    } // end of postgres

    changeSet(author: "marketplace", dbms: "mssql", id: "5.0-2", context: "create, 5.0") {
        comment("Add columns for counting votes to service_item")
        addColumn(tableName: "service_item") {
            column(name: "total_rate1", type: "int") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate2", type: "int") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate3", type: "int") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate4", type: "int") {
                constraints(nullable: "true")
            }
        }
        addColumn(tableName: "service_item") {
            column(name: "total_rate5", type: "int") {
                constraints(nullable: "true")
            }
        }
    } // end of SQLServer

    // Changes for
    // MARKETPLACE-4240 - Move the 'downloadImages' flag from ImportTask into InterfaceConfiguration
    changeSet(author: "marketplace", id: "5.0-3", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
        comment("Replace import_task.download_images with interface_configuration.download_images")
        addColumn(tableName: "interface_configuration") {
            column(name: "download_images", type: "boolean", defaultValueBoolean: "false") {
                constraints(nullable: "false")
            }
        }

        dropColumn(columnName: "download_images", tableName: "import_task")
    }

    changeSet(author: "marketplace", id: "5.0-4", dbms: "mysql", context: "create, 5.0") {
        comment("Drop not-null constraint for and expand 'text' field in item_comment table")
        dropNotNullConstraint(tableName: "item_comment", columnName: "text", columnDataType: "varchar(250)")
        modifyDataType(tableName: "item_comment", columnName: "text", newDataType: "varchar(4000)")
        addColumn(tableName: "item_comment") {
            column(name: "rate", type: "float") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-4", dbms: "oracle", context: "create, 5.0") {
        comment("Drop not-null constraint for and expand 'text' field in item_comment table")
        dropNotNullConstraint(tableName: "item_comment", columnName: "text")
        modifyDataType(tableName: "item_comment", columnName: "text", newDataType: "varchar2(4000 char)")
        addColumn(tableName: "item_comment") {
            column(name: "rate", type: "float") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-4", dbms: "postgresql", context: "create, 5.0") {
        comment("Drop not-null constraint for and expand 'text' field in item_comment table")
        dropNotNullConstraint(tableName: "item_comment", columnName: "text", columnDataType: "varchar(250)")
        modifyDataType(tableName: "item_comment", columnName: "text", newDataType: "varchar(4000)")
        addColumn(tableName: "item_comment") {
            column(name: "rate", type: "float4") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-4", dbms: "mssql", context: "create, 5.0") {
        comment("Drop not-null constraint for and expand 'text' field in item_comment table")
        dropNotNullConstraint(tableName: "item_comment", columnName: "text", columnDataType: "nvarchar(250)")
        modifyDataType(tableName: "item_comment", columnName: "text", newDataType: "nvarchar(4000)")
        addColumn(tableName: "item_comment") {
            column(name: "rate", type: "float") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-5", dbms: "mysql", context: "upgrade-only, 5.0") {
        comment("Transfer rating information into the item_comment table")
        sql("""
            update item_comment
            set item_comment.rate =
            (
                select rating.rate
                    from rating
                    where item_comment.service_item_id = rating.service_item_id
                    and item_comment.author_id = rating.author_id
            );

            insert item_comment (version, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate)
            select 0, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate
            from rating r where not exists
            (
                select ic.id from item_comment ic where r.author_id = ic.author_id and r.service_item_id = ic.service_item_id
            );
        """)
    }

    changeSet(author: "marketplace", id: "5.0-5", dbms: "oracle", context: "upgrade-only, 5.0") {
        comment("Transfer rating information into the item_comment table")
        sql("""
            update item_comment
            set item_comment.rate =
            (
                select rating.rate
                    from rating
                    where item_comment.service_item_id = rating.service_item_id
                    and item_comment.author_id = rating.author_id
            );

            insert into item_comment (id, version, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate)
            select HIBERNATE_SEQUENCE.NEXTVAL, 0, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate
            from rating r where not exists
            (
            select ic.id from item_comment ic where r.author_id = ic.author_id and r.service_item_id = ic.service_item_id
            );
        """)
    }

    changeSet(author: "marketplace", id: "5.0-5", dbms: "postgresql", context: "upgrade-only, 5.0") {
        comment("Transfer rating information into the item_comment table")
        sql("""
            update item_comment
            set rate =
            (
                select rating.rate
                    from rating
                    where item_comment.service_item_id = rating.service_item_id
                        and item_comment.author_id = rating.author_id
            );

            insert into item_comment (id, version, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate)
            select NEXTVAL('hibernate_sequence'), 0, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate
            from rating r where not exists
            (
                select ic.id from item_comment ic where r.author_id = ic.author_id and r.service_item_id = ic.service_item_id
            );
        """)
    }

    changeSet(author: "marketplace", id: "5.0-5", dbms: "mssql", context: "upgrade-only, 5.0") {
        comment("Transfer rating information into the item_comment table")
        sql("""
            update item_comment
            set item_comment.rate =
            (
                select rating.rate
                    from rating
                    where item_comment.service_item_id = rating.service_item_id
                    and item_comment.author_id = rating.author_id
            );

            insert item_comment (version, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate)
            select 0, edited_by_id, created_date, service_item_id, created_by_id, author_id, edited_date, rate
            from rating r where not exists
            (
                select ic.id from item_comment ic where r.author_id = ic.author_id and r.service_item_id = ic.service_item_id
            );
        """)
    }

    changeSet(author: "marketplace", id: "5.0-6", dbms: "mssql, mysql, oracle, postgresql", context: "upgrade-only, 5.0") {
        comment("Compute number of votes for each rating for the service items")
        sql("""
            update service_item
            set total_rate1 =
                (
                select COUNT(*) from rating
                    where service_item.id = rating.service_item_id
                    and rate = 1
            ),
            total_rate2 =
                (
                select COUNT(*) from rating
                        where service_item.id = rating.service_item_id
                        and rate = 2
            ),
            total_rate3 =
                (
                select COUNT(*) from rating
                        where service_item.id = rating.service_item_id
                        and rate = 3
            ),
            total_rate4 =
                (
                select COUNT(*) from rating
                        where service_item.id = rating.service_item_id
                        and rate = 4
            ),
            total_rate5 =
                (
                select COUNT(*) from rating
                        where service_item.id = rating.service_item_id
                        and rate = 5
            )
            where total_votes > 0
        """)
    }

    changeSet(author: "marketplace", id: "5.0-7", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
        comment("Drop RATING table")
        dropTable(tableName: "rating")
    }


    changeSet(author: "marketplace", id: "5.0-8", dbms: "mysql", context: "create, 5.0") {
        comment("Add tables supporting text area and image URL custom fields")
        createTable(tableName: "image_url_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfPK")
            }

            column(name: "value", type: "varchar(2083)")
        }

        createTable(tableName: "image_url_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfdPK")
            }
        }

        createTable(tableName: "text_area_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfPK")
            }

            column(name: "value", type: "varchar(4000)")
        }

        createTable(tableName: "text_area_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfdPK")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-8", dbms: "oracle", context: "create, 5.0") {
        comment("Add tables supporting text area and image URL custom fields")
        createTable(tableName: "image_url_cf") {
            column(name: "id", type: "number(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfPK")
            }

            column(name: "value", type: "varchar2(2083 char)")
        }
        createTable(tableName: "image_url_cfd") {
            column(name: "id", type: "number(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfdPK")
            }
        }
        createTable(tableName: "text_area_cf") {
            column(name: "id", type: "number(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfPK")
            }

            column(name: "value", type: "varchar2(4000 char)")
        }
        createTable(tableName: "text_area_cfd") {
            column(name: "id", type: "number(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfdPK")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-8", dbms: "postgresql", context: "create, 5.0") {
        comment("Add tables supporting text area and image URL custom fields")
        createTable(tableName: "image_url_cf") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfPK")
            }

            column(name: "value", type: "varchar(2083)")
        }

        createTable(tableName: "image_url_cfd") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfdPK")
            }
        }

        createTable(tableName: "text_area_cf") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfPK")
            }

            column(name: "value", type: "varchar(4000)")
        }

        createTable(tableName: "text_area_cfd") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfdPK")
            }
        }
    }

    changeSet(author: "marketplace", id: "5.0-8", dbms: "mssql", context: "create, 5.0") {
        comment("Add tables supporting text area and image URL custom fields")
        createTable(tableName: "image_url_cf") {
            column(name: "id", type: "numeric(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfPK")
            }

            column(name: "value", type: "nvarchar(2083)")
        }

        createTable(tableName: "image_url_cfd") {
            column(name: "id", type: "numeric(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "image_url_cfdPK")
            }
        }

        createTable(tableName: "text_area_cf") {
            column(name: "id", type: "numeric(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfPK")
            }

            column(name: "value", type: "nvarchar(4000)")
        }

        createTable(tableName: "text_area_cfd") {
            column(name: "id", type: "numeric(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "text_area_cfdPK")
            }
        }
    }

    // CheckBox custom fields
    changeSet(author: "marketplace", id: "5.0-9", dbms: "mysql", context: "create, 5.0") {
        comment("Add tables supporting check box custom fields")
        createTable(tableName: "check_box_cf") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfPK")
            }
            column(name: "value", type: "bit")
        }
        createTable(tableName: "check_box_cfd") {
            column(name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfdPK")
            }
            column(name: "selected_by_default", type: "bit")
        }
    }

    changeSet(author: "marketplace", id: "5.0-9", dbms: "oracle", context: "create, 5.0") {
        comment("Add tables supporting check box custom fields")
        createTable(tableName: "check_box_cf") {
            column(name: "id", type: "number(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfPK")
            }
            column(name: "value", type: "number(1,0)")
        }
        createTable(tableName: "check_box_cfd") {
            column(name: "id", type: "number(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfdPK")
            }
             column(name: "selected_by_default", type: "number(1,0)")
        }
    }

    changeSet(author: "marketplace", id: "5.0-9", dbms: "postgresql", context: "create, 5.0") {
        comment("Add tables supporting check box custom fields")
        createTable(tableName: "check_box_cf") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfPK")
            }
            column(name: "value", type: "bool")
        }
        createTable(tableName: "check_box_cfd") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfdPK")
            }
            column(name: "selected_by_default", type: "bool")
        }
    }

    changeSet(author: "marketplace", id: "5.0-9", dbms: "mssql", context: "create, 5.0") {
        comment("Add tables supporting check box custom fields")
        createTable(tableName: "check_box_cf") {
            column(name: "id", type: "numeric(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfPK")
            }
            column(name: "value", type: "tinyint")
        }
        createTable(tableName: "check_box_cfd") {
            column(name: "id", type: "numeric(19,0)") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "check_box_cfdPK")
            }
            column(name: "selected_by_default", type: "tinyint")
        }
    }

    changeSet(author: "marketplace", id: "5.0-10", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
        comment("Add column to custom filed defintion table to store the section where to display that field")
        addColumn(tableName: "custom_field_definition") {
            column(name: "section", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }
    
	changeSet(author: "marketplace", id: "5.0-11", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
			addColumn(tableName: "custom_field_definition") {
				column(name: "all_types", type: "boolean", defaultValueBoolean: "false") {
					constraints(nullable: "false")
				}
			}
	}
	
	// CREATE APPROVED DATE COLUMN FIELD
	changeSet(author: "marketplace", id: "5.0-12", dbms: "mysql", context: "create, 5.0") {
		comment("Add column to service item table to store the approved date")
        addColumn(tableName: "service_item") {
			column(name: "approval_date", type: "datetime")
		}
	}
	
	changeSet(author: "marketplace", id: "5.0-12", dbms: "oracle", context: "create, 5.0") {
		comment("Add column to service item table to store the approved date")
		addColumn(tableName: "service_item") {
			column(name: "approval_date", type: "TIMESTAMP (6)")
		}
	}
	
	changeSet(author: "marketplace", id: "5.0-12", dbms: "postgresql", context: "create, 5.0") {
		comment("Add column to service item table to store the approved date")
		addColumn(tableName: "service_item") {
			column(name: "approval_date", type: "timestamp")
		}
	}
	
	changeSet(author: "marketplace", id: "5.0-12", dbms: "mssql", context: "create, 5.0") {
		comment("Add column to service item table to store the approved date")
		addColumn(tableName: "service_item") {
			column(name: "approval_date", type: "datetime")
		}
	}
	
	//UPGRADE: POPULATE APPROVED DATE COLUMN FIELD
	changeSet(author: "marketplace", id: "5.0-13", dbms: "mssql, mysql, oracle, postgresql", context: "upgrade-only, 5.0") {
		comment("Transfer approved date information into the service_item table")
		sql("""
			update service_item
			set approval_date =
			(
				select service_item_activity.activity_timestamp
					from service_item_activity
					where service_item.id = service_item_activity.service_item_id
					and service_item_activity.action = 'APPROVED'
			);
		""")
	}

    changeSet(author: "marketplace", id: "5.0-14", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
        comment("Create indexes for change_detail and change_log tables to speed up lookups")

        createIndex(tableName: "change_detail", indexName: "change_detail_object_idx") {
            column(name: "object_class_name")
            column(name: "object_id")
            column(name: "object_version")
        }

        createIndex(tableName: "change_log", indexName: "change_log_object_idx") {
            column(name: "object_class_name")
            column(name: "object_id")
            column(name: "object_version")
        }
    }
	
	changeSet(author: "marketplace", id: "5.0-15", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
		comment("Create indexes for custom_field_definition_types and field_value tables to speed up lookups")

		createIndex(tableName: "custom_field_definition_types", indexName: "cfd_types_cfd_idx") {
			column(name: "cf_definition_types_id")
		}
		
		createIndex(tableName: "custom_field_definition_types", indexName: "cfd_types_types_idx") {
			column(name: "types_id")
		}
		
		createIndex(tableName: "field_value", indexName: "field_value_cfd_idx") {
			column(name: "custom_field_definition_id")
		}
	}
	
	changeSet(author: "marketplace", id: "5.0-16", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
		comment("Create indexes for tables to speed up lookups")

		createIndex(tableName: "service_item_snapshot", indexName: "si_snapshot_id_idx") {
			column(name: "service_item_id")
		}
		
		createIndex(tableName: "custom_field", indexName: "cf_cfd_idx") {
			column(name: "custom_field_definition_id")
		}
		
		createIndex(tableName: "si_recommended_layouts", indexName: "si_rec_layouts_idx") {
			column(name: "service_item_id")
		}
		
		createIndex(tableName: "rejection_activity", indexName: "rejection_act_listing_id_idx") {
			column(name: "rejection_listing_id")
		}
		
		createIndex(tableName: "rejection_listing", indexName: "rejection_listing_just_id_idx") {
			column(name: "justification_id")
		}
		
		createIndex(tableName: "relationship", indexName: "relationship_owing_id_idx") {
			column(name: "owning_entity_id")
		}
		
		createIndex(tableName: "relationship_activity_log", indexName: "rel_act_log_mod_rel_act_idx") {
			column(name: "mod_rel_activity_id")
		}
		
		createIndex(tableName: "relationship_activity_log", indexName: "rel_act_log_mod_si_snpsht_idx") {
			column(name: "service_item_snapshot_id")
		}
		
		createIndex(tableName: "relationship_service_item", indexName: "rel_si_rel_items_id_idx") {
			column(name: "relationship_related_items_id")
		}
		
		createIndex(tableName: "service_item_activity", indexName: "si_act_si_ver_idx") {
			column(name: "service_item_version")
		}
		
		createIndex(tableName: "service_item_category", indexName: "si_cat_cat_id_idx") {
			column(name: "category_id")
		}
		
		createIndex(tableName: "service_item_custom_field", indexName: "si_cf_cf_id_idx") {
			column(name: "custom_field_id")
		}
		
		createIndex(tableName: "service_item", indexName: "si_owf_props_id_idx") {
			column(name: "owf_properties_id")
		}
		
		createIndex(tableName: "service_item", indexName: "si_types_id_idx") {
			column(name: "types_id")
		}
		
		createIndex(tableName: "service_item", indexName: "si_state_id_idx") {
			column(name: "state_id")
		}
		
		createIndex(tableName: "service_item", indexName: "si_last_activity_idx") {
			column(name: "last_activity_id")
		}
		
		createIndex(tableName: "service_item", indexName: "si_created_by_id_idx") {
			column(name: "created_by_id")
		}
	}

	changeSet(author: "marketplace", id: "5.0-17", dbms: "mssql, mysql, oracle, postgresql", context: "upgrade-only, 5.0") {
		comment("Set the section for existing cfds to typeProperties")
		sql("""
			update custom_field_definition set section = 'typeProperties'
			where section is null;
        """)
	}
	
	changeSet(author: "marketplace", id: "5.0-18", dbms: "mssql, mysql, oracle, postgresql", context: "create, 5.0") {
		comment("Create indexes for rejection_listing table to speed up lookups")

		createIndex(tableName: "rejection_listing", indexName: "rej_lst_author_id_idx") {
			column(name: "author_id")
		}
	}
}
