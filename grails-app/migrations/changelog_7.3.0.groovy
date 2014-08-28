/**
 * This changelog include the changesets for the 7.3 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.3.0-1*
 *  context: create|upgrade-only, 7.3.0
 */

databaseChangeLog = {
    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-1", context: "create, 7.3.0") {
        addColumn(tableName: "score_card_item") {
            column(name: "image", type: "java.sql.Types.VARCHAR(255)")
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-2", context: "create, 7.3.0") {
        addColumn(tableName: "service_item_activity") {
            column(name: "details", type: "java.sql.Types.VARCHAR(255)")
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-3", context: "create, 7.3.0") {
        addColumn(tableName: "owf_properties") {
            column(name: "descriptor_url", type: "java.sql.Types.VARCHAR(2083)")
        }
    }

    changeSet(author: "marketplace", dbms: "mysql, postgresql, oracle", id: "7.3.0-4", context: "create, 7.3.0") {
        dropNotNullConstraint(columnDataType: "java.sql.Types.VARCHAR(4000)",
            columnName: "description", tableName: "service_item")
        dropNotNullConstraint(columnDataType: "DATETIME", columnName: "release_date",
            tableName: "service_item")
        dropNotNullConstraint(columnDataType: "BIGINT", columnName: "state_id",
            tableName: "service_item")
        dropNotNullConstraint(columnDataType: "java.sql.Types.VARCHAR(256)",
            columnName: "tech_poc", tableName: "service_item")
        dropNotNullConstraint(columnDataType: "java.sql.Types.VARCHAR(256)",
            columnName: "version_name", tableName: "service_item")
        dropNotNullConstraint(columnDataType: "BIGINT", columnName: "service_item_categories_id",
            tableName: "service_item_category")
    }

    changeSet(author: "marketplace", dbms: "mssql", id: "7.3.0-4", context: "create, 7.3.0") {
        dropNotNullConstraint(columnDataType: "java.sql.Types.VARCHAR(4000)",
            columnName: "description", tableName: "service_item")
        dropNotNullConstraint(columnDataType: "DATETIME", columnName: "release_date",
            tableName: "service_item")

        // mssql prevents you from altering columns with active objects linked to them.
        // To get around this for the create script, we're simply dropping them before the alter and
        // recreating them afterwards.
        dropIndex(indexName: "si_state_id_idx", tableName: "service_item")
        dropForeignKeyConstraint(baseTableName: "service_item", constraintName: "FK1571565DDFEC3E97")
        dropNotNullConstraint(columnDataType: '${marketplace.profileId}', columnName: "state_id",
            tableName: "service_item")
        createIndex(tableName: "service_item", indexName: "si_state_id_idx") {
            column(name: "state_id")
        }
        sql("""
            alter table service_item add constraint FK1571565DDFEC3E97 foreign key (state_id) references state;
            """)

        dropNotNullConstraint(columnDataType: "java.sql.Types.VARCHAR(256)",
            columnName: "tech_poc", tableName: "service_item")
        dropNotNullConstraint(columnDataType: "java.sql.Types.VARCHAR(256)",
            columnName: "version_name", tableName: "service_item")

        // mssql prevents you from altering columns with active objects linked to them.
        // To get around this for the create script, we're simply dropping them before the alter and
        // recreating them afterwards.
        dropIndex(indexName: "svc_item_cat_id_idx", tableName: "service_item_category")
        dropNotNullConstraint(columnDataType: "BIGINT", columnName: "service_item_categories_id",
            tableName: "service_item_category")
        createIndex(tableName: "service_item_category", indexName: "svc_item_cat_id_idx") {
            column(name: "service_item_categories_id")
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-5", context: "create, 7.3.0") {
        createTable(tableName: "intent") {
            column(autoIncrement: "true", name: "id", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "intentPK")
            }

            column(name: "version", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "action_id", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "created_by_id", type: '${marketplace.profileId}')

            column(name: "created_date", type: "java.sql.Types.DATE")

            column(name: "data_type_id", type: "java.sql.Types.BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "edited_by_id", type: '${marketplace.profileId}')

            column(name: "edited_date", type: "java.sql.Types.DATE")

            column(name: "receive", type: "java.sql.Types.BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "send", type: "java.sql.Types.BOOLEAN") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "owf_properties_intent") {
            column(name: "owf_properties_intents_id", type: '${marketplace.profileId}')

            column(name: "intent_id", type: "java.sql.Types.BIGINT")
        }

        createIndex(indexName: "FKB971369CD8544299", tableName: "intent") {
            column(name: "action_id")
        }

        createIndex(indexName: "FKB971369C7666C6D2", tableName: "intent") {
            column(name: "created_by_id")
        }

        createIndex(indexName: "FKB971369C283F938E", tableName: "intent") {
            column(name: "data_type_id")
        }

        createIndex(indexName: "FKB971369CE31CB353", tableName: "intent") {
            column(name: "edited_by_id")
        }

        createIndex(indexName: "FK3F99ECA7A651895D", tableName: "owf_properties_intent") {
            column(name: "intent_id")
        }

        createIndex(indexName: "owfProps_intent_id_idx", tableName: "owf_properties_intent") {
            column(name: "owf_properties_intents_id")
        }

        addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "intent", constraintName: "FKB971369CD8544299", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "intent_action", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "created_by_id", baseTableName: "intent", constraintName: "FKB971369C7666C6D2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "data_type_id", baseTableName: "intent", constraintName: "FKB971369C283F938E", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "intent_data_type", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "edited_by_id", baseTableName: "intent", constraintName: "FKB971369CE31CB353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "intent_id", baseTableName: "owf_properties_intent", constraintName: "FK3F99ECA7A651895D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "intent", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "owf_properties_intents_id", baseTableName: "owf_properties_intent", constraintName: "FK3F99ECA74704E25C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "owf_properties", referencesUniqueColumn: "false")
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-6", context: "create, 7.3.0") {
        addColumn(tableName: "types") {
            column(name: "is_permanent", type: "java.sql.Types.BOOLEAN")
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-7", context: "upgrade-only, 7.3.0") {
        update(tableName: "types") {
            column(name: "title", value: "OZONE App")
            column(name: "description", value: "OZONE app")
            column(name: "has_launch_url", valueBoolean: "false")
            column(name: "has_icons", valueBoolean: "false")
            where "title='OZONE Stack'"
        }
        update(tableName: "types") {
            column(name: "title", value: "App Component")
            column(name: "description", value: "app component")
            where "title='Ozone Apps'"
        }
        update(tableName: "types") {
            column(name: "title", value: "Web App")
            column(name: "description", value: "web app")
            where "title='Web Apps'"
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-8", context: "create, 7.3.0") {
        addColumn(tableName: "score_card_item") {
            column(name: "show_on_listing", type: "java.sql.Types.BOOLEAN")
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql, oracle", id: "7.3.0-9", context: "upgrade-only, 7.3.0") {
        update(tableName: "types") {
            column(name: "is_permanent", valueBoolean: "true")
            where "title='OZONE App'"
        }
        update(tableName: "types") {
            column(name: "is_permanent", valueBoolean: "true")
            where "title='App Component'"
        }
    }

    //Adding a schema for hsql - previously did not have this in marketplace. This brings hsql up to date with all preceding changesets.
    include file: "hsql_7.3.0.groovy"

    changeSet(author: "marketplace", id: "7.3.0-12", dbms: "mssql, mysql, oracle, postgresql, hsqldb", context: "create, 7.3.0") {
        comment("Create new mapping table for service item's 'owners' field replacing 'author' property.")
        createTable(tableName: "service_item_profile") {
            column(name: "service_item_owners_id", type: "bigint")

            column(name: "profile_id", type: "bigint")

            column(name: "owners_idx", type: "integer")
        }
    }

    changeSet(author: "marketplace", id: "7.3.0-13", dbms: "mssql, mysql, oracle, postgresql, hsqldb", context: "upgrade-only, 7.3.0") {
        comment("Create a record in service_item_profile table for each service item linking it with its owner.")
        sql ( """
            INSERT INTO service_item_profile (service_item_owners_id, profile_id, owners_idx) SELECT id, author_id, 0 FROM service_item;
            """)
    }


    changeSet(author: "marketplace", id: "7.3.0-14", dbms: "hsqldb, postgresql", context: "create, 7.3.0") {
        comment("Drop 'author_id' column corresponding to the 'author' property of service item being removed.")
        dropForeignKeyConstraint(baseTableName: "service_item", constraintName: "FK1571565D5A032135")
        dropColumn(columnName: "author_id", tableName: "service_item")
    }

    changeSet(author: "marketplace", id: "7.3.0-14", dbms: "mssql, mysql, oracle", context: "create, 7.3.0") {
        comment("Drop 'author_id' column corresponding to the 'author' property of service item being removed.")
        dropIndex(indexName: "svc_item_author_id_idx", tableName: "service_item")
        dropForeignKeyConstraint(baseTableName: "service_item", constraintName: "FK1571565D5A032135")
        dropColumn(columnName: "author_id", tableName: "service_item")
    }

    changeSet(author: "marketplace", id: "7.3.0-15", dbms: "mysql, oracle, postgresql, hsqldb, mssql", context: "create, 7.3.0") {
        createTable(tableName: "service_item_documentation_url") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "service_item_PK")
            }

            column(name: "version", type: "BIGINT", defaultValueNumeric: "0") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "java.sql.Types.VARCHAR(256)") {
                constraints(nullable: "false")
            }

            column(name: "service_item_id", type: '${marketplace.profileId}') {
                constraints(nullable: "false")
            }

            column(name: "url", type: "java.sql.Types.VARCHAR(2083)") {
                constraints(nullable: "false") }
        }

        createTable(tableName: "service_item_tech_pocs") {
            column(name: "service_item_id", type: '${marketplace.profileId}')

            column(name: "tech_poc", type: "java.sql.Types.VARCHAR(256)")
        }
    }

    changeSet(author: "marketplace", id: "7.3.0-16", dbms: "mssql, mysql, oracle, postgresql, hsqldb", context: "create, 7.3.0") {
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_documentation_url", constraintName: "FK24572D08C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
        addForeignKeyConstraint(baseColumnNames: "service_item_id", baseTableName: "service_item_tech_pocs", constraintName: "FKA55CFB56C7E5C662", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
    }

    changeSet(author: "marketplace", id: "7.3.0-17", dbms: "mssql, mysql, oracle, postgresql, hsqldb", context: "upgrade-only, 7.3.0") {
        comment("Create a record in service_item_tech_pocs table for each service item linking it with its technical POC.")
        sql ( """
            INSERT INTO service_item_tech_pocs (service_item_id, tech_poc) SELECT id, tech_poc FROM service_item;
            """)
    }

    changeSet(author: "marketplace", id: "7.3.0-18", dbms: "mssql, mysql, postgresql, hsqldb", context: "upgrade-only, 7.3.0") {
        comment("Create a record in service_item_documentation_url table for each service item linking it with its technical POC.")
        sql ( """
            INSERT INTO service_item_documentation_url (service_item_id, name, url) SELECT id, doc_url, doc_url FROM service_item WHERE doc_url IS NOT NULL;
            """)
    }

    changeSet(author: "marketplace", id: "7.3.0-18", dbms: "oracle", context: "upgrade-only, 7.3.0") {
        comment("Create a record in service_item_documentation_url table for each service item linking it with its technical POC.")
        sql ( """
            INSERT INTO service_item_documentation_url (id, version, service_item_id, name, url)
                SELECT HIBERNATE_SEQUENCE.NEXTVAL, 0, id, doc_url, doc_url FROM service_item WHERE doc_url IS NOT NULL;
            """)
    }

    changeSet(author: "marketplace", id: "7.3.0-19", dbms: "mssql, mysql, oracle, postgresql, hsqldb", context: "create, 7.3.0") {
        dropColumn(columnName: "doc_url", tableName: "service_item")
        dropColumn(columnName: "tech_poc", tableName: "service_item")
    }

    changeSet(author: "franchise-store", id: "7.3.0-21", dbms: "postgresql", context: "create, 7.3.0") {
        comment("Drop no-longer needed indices. Postgress-specific to compensate for the Liquibase bug")
        sql ( """
            DROP INDEX IF EXISTS  change_detail_object_idx;

            DROP INDEX IF EXISTS  change_log_object_idx;

            DROP INDEX IF EXISTS  cfd_types_cfd_idx;

            DROP INDEX IF EXISTS  rel_act_log_mod_rel_act_idx;

            DROP INDEX IF EXISTS  rel_si_rel_items_id_idx;

            DROP INDEX IF EXISTS  si_act_si_ver_idx;

            DROP INDEX IF EXISTS svc_item_author_id_idx;
        """)
    }

}
