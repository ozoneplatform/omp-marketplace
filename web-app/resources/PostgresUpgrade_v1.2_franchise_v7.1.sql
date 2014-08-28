-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 6/10/13 3:13 PM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_7.0.groovy::7.0-1::marketplace::(Checksum: 3:70d3313cedb44a2430d183b912b1cfe8)
ALTER TABLE owf_properties ADD height BIGINT;

ALTER TABLE owf_properties ADD width BIGINT;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_7.0.groovy', '7.0-1', '2.0.1', '3:70d3313cedb44a2430d183b912b1cfe8', 43);

-- Changeset changelog_7.0.groovy::7.0-2::marketplace::(Checksum: 3:5e5d0f3fee742d246977b284fdd76d73)
ALTER TABLE application_configuration ADD help VARCHAR(2000);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.0.groovy', '7.0-2', '2.0.1', '3:5e5d0f3fee742d246977b284fdd76d73', 44);

-- Changeset changelog_7.0.groovy::7.0-3::marketplace::(Checksum: 3:d6e16b9122df8fa0ef05bf2a96af2b24)
-- Transfer intelligence custom field to domain custom field
UPDATE custom_field_definition
            SET name='domain'
            WHERE name='intelligence';

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Transfer intelligence custom field to domain custom field', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.0.groovy', '7.0-3', '2.0.1', '3:d6e16b9122df8fa0ef05bf2a96af2b24', 45);

-- Release Database Lock
