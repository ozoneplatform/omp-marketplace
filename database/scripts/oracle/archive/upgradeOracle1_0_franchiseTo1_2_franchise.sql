-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 2/28/13 11:06 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-1::franchise-store::(Checksum: 3:a3a3eaab836585ac44bc0d4b7828c381)
CREATE TABLE application_configuration (id NUMBER(38,0) NOT NULL, version NUMBER(38,0) NOT NULL, created_by_id NUMBER(38,0), created_date DATE, edited_by_id NUMBER(38,0), edited_date DATE, code VARCHAR2(250) NOT NULL, value VARCHAR2(2000), title VARCHAR2(250) NOT NULL, description VARCHAR2(2000), type VARCHAR2(250) NOT NULL, group_name VARCHAR2(250) NOT NULL, sub_group_name VARCHAR2(250), mutable NUMBER(1) NOT NULL, sub_group_order NUMBER(38,0), CONSTRAINT application_configurationPK PRIMARY KEY (id), UNIQUE (code));

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', SYSTIMESTAMP, 'Create Table', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-1', '2.0.1', '3:a3a3eaab836585ac44bc0d4b7828c381', 1);

-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-2::franchise-store::(Checksum: 3:8c76d18c40c82ecf837da46af444846c)
-- Create index for application_configuration.group_name
CREATE INDEX FKFC9C0477666C6D2 ON application_configuration(created_by_id);

CREATE INDEX FKFC9C047E31CB353 ON application_configuration(edited_by_id);

CREATE INDEX app_config_group_name_idx ON application_configuration(group_name);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C0477666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C047E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', 'Create index for application_configuration.group_name', SYSTIMESTAMP, 'Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-2', '2.0.1', '3:8c76d18c40c82ecf837da46af444846c', 2);

-- Release Database Lock
