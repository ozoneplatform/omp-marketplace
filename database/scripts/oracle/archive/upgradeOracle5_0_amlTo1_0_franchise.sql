-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 12/10/12 10:08 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE service_item ADD agency VARCHAR2(255);

ALTER TABLE service_item ADD agency_icon VARCHAR2(255);

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', SYSTIMESTAMP, 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.1', '3:a1d04babbf1c20d5360e96e65afd2bae', 1);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:9975dd71c2446968bd67361dd7a86475)
ALTER TABLE service_item ADD is_outside NUMBER(1,0);

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', SYSTIMESTAMP, 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.1', '3:9975dd71c2446968bd67361dd7a86475', 2);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-3::franchise-store::(Checksum: 3:0ee8acc59cc31553b0362e999aca8aa2)
DROP INDEX change_detail_object_idx;

DROP INDEX change_log_object_idx;

DROP INDEX cfd_types_cfd_idx;

DROP INDEX rel_act_log_mod_rel_act_idx;

DROP INDEX rel_si_rel_items_id_idx;

DROP INDEX si_act_si_ver_idx;

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', SYSTIMESTAMP, 'Drop Index (x6)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-3', '2.0.1', '3:0ee8acc59cc31553b0362e999aca8aa2', 3);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-4::franchise-store::(Checksum: 3:86a8f49e748297aa431770230370cfdd)
CREATE TABLE drop_down_cf_field_value (drop_down_cf_field_value_id NUMBER(19,0), field_value_id NUMBER(19,0), field_value_list_idx NUMBER(10,0));

ALTER TABLE custom_field_definition ADD is_permanent NUMBER(1,0);

ALTER TABLE drop_down_cfd ADD is_multi_select NUMBER(1,0);

CREATE INDEX FK2627FFDDA5BD888 ON drop_down_cf_field_value(field_value_id);

ALTER TABLE drop_down_cf_field_value ADD CONSTRAINT FK2627FFDDA5BD888 FOREIGN KEY (field_value_id) REFERENCES field_value (id);

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', SYSTIMESTAMP, 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.1', '3:86a8f49e748297aa431770230370cfdd', 4);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-6::franchise-store::(Checksum: 3:467f95d21e9bd4003fc7ec4f3eb82f5d)
ALTER TABLE ext_service_item MODIFY system_uri NULL;

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', SYSTIMESTAMP, 'Drop Not-Null Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-6', '2.0.1', '3:467f95d21e9bd4003fc7ec4f3eb82f5d', 5);

-- Release Database Lock
