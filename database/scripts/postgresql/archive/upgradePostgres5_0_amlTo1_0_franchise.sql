-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 12/10/12 10:06 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE service_item ADD agency VARCHAR(255);

ALTER TABLE service_item ADD agency_icon VARCHAR(255);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.1', '3:a1d04babbf1c20d5360e96e65afd2bae', 1);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:d80d797f3747f702b21f3e6a6ecb6039)
ALTER TABLE service_item ADD is_outside bool;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.1', '3:d80d797f3747f702b21f3e6a6ecb6039', 2);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-3::franchise-store::(Checksum: 3:0ee8acc59cc31553b0362e999aca8aa2)
DROP INDEX change_detail_object_idx;

DROP INDEX change_log_object_idx;

DROP INDEX cfd_types_cfd_idx;

DROP INDEX rel_act_log_mod_rel_act_idx;

DROP INDEX rel_si_rel_items_id_idx;

DROP INDEX si_act_si_ver_idx;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Drop Index (x6)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-3', '2.0.1', '3:0ee8acc59cc31553b0362e999aca8aa2', 3);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-4::franchise-store::(Checksum: 3:f5acf9debc261fa56e5b02edd179ae1a)
CREATE TABLE drop_down_cf_field_value (drop_down_cf_field_value_id int8, field_value_id int8, field_value_list_idx int4);

ALTER TABLE custom_field_definition ADD is_permanent bool;

ALTER TABLE drop_down_cfd ADD is_multi_select bool;

CREATE INDEX FK2627FFDDA5BD888 ON drop_down_cf_field_value(field_value_id);

ALTER TABLE drop_down_cf_field_value ADD CONSTRAINT FK2627FFDDA5BD888 FOREIGN KEY (field_value_id) REFERENCES field_value (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.1', '3:f5acf9debc261fa56e5b02edd179ae1a', 4);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-6::franchise-store::(Checksum: 3:467f95d21e9bd4003fc7ec4f3eb82f5d)
ALTER TABLE ext_service_item ALTER COLUMN  system_uri DROP NOT NULL;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Drop Not-Null Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-6', '2.0.1', '3:467f95d21e9bd4003fc7ec4f3eb82f5d', 5);

-- Release Database Lock
