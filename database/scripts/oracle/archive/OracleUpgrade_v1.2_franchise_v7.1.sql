-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 6/10/13 3:17 PM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_7.0.groovy::7.0-1::marketplace::(Checksum: 3:70d3313cedb44a2430d183b912b1cfe8)
ALTER TABLE owf_properties ADD height NUMBER(38,0);

ALTER TABLE owf_properties ADD width NUMBER(38,0);

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', SYSTIMESTAMP, 'Add Column (x2)', 'EXECUTED', 'changelog_7.0.groovy', '7.0-1', '2.0.1', '3:70d3313cedb44a2430d183b912b1cfe8', 43);

-- Changeset changelog_7.0.groovy::7.0-2::marketplace::(Checksum: 3:5e5d0f3fee742d246977b284fdd76d73)
ALTER TABLE application_configuration ADD help VARCHAR2(2000);

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', SYSTIMESTAMP, 'Add Column', 'EXECUTED', 'changelog_7.0.groovy', '7.0-2', '2.0.1', '3:5e5d0f3fee742d246977b284fdd76d73', 44);

-- Changeset changelog_7.0.groovy::7.0-3::marketplace::(Checksum: 3:ce0c3906b9e948f839ba08d5c4572ae3)
-- Transfer intelligence custom field to domain custom field
UPDATE custom_field_definition
            SET custom_field_definition.name='domain'
            WHERE custom_field_definition.name='intelligence';

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Transfer intelligence custom field to domain custom field', SYSTIMESTAMP, 'Custom SQL', 'EXECUTED', 'changelog_7.0.groovy', '7.0-3', '2.0.1', '3:ce0c3906b9e948f839ba08d5c4572ae3', 45);

-- Release Database Lock
