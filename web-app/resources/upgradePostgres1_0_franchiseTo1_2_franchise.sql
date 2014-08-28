-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 2/28/13 11:02 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-1::franchise-store::(Checksum: 3:a3a3eaab836585ac44bc0d4b7828c381)
CREATE TABLE application_configuration (id bigserial NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, code VARCHAR(250) NOT NULL, value VARCHAR(2000), title VARCHAR(250) NOT NULL, description VARCHAR(2000), type VARCHAR(250) NOT NULL, group_name VARCHAR(250) NOT NULL, sub_group_name VARCHAR(250), mutable BOOLEAN NOT NULL, sub_group_order BIGINT, CONSTRAINT "application_configurationPK" PRIMARY KEY (id), UNIQUE (code));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-1', '2.0.1', '3:a3a3eaab836585ac44bc0d4b7828c381', 1);

-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-2::franchise-store::(Checksum: 3:8c76d18c40c82ecf837da46af444846c)
-- Create index for application_configuration.group_name
CREATE INDEX FKFC9C0477666C6D2 ON application_configuration(created_by_id);

CREATE INDEX FKFC9C047E31CB353 ON application_configuration(edited_by_id);

CREATE INDEX app_config_group_name_idx ON application_configuration(group_name);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C0477666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C047E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', 'Create index for application_configuration.group_name', NOW(), 'Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-2', '2.0.1', '3:8c76d18c40c82ecf837da46af444846c', 2);

-- Release Database Lock
