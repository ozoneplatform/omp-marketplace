-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 7/29/13 9:46 AM
-- Against: aml_mp_migra@10.10.16.59@jdbc:mysql://amldb01.goss.owfgoss.org:3306/aml_mp_migra
-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_7.2.groovy::7.2-1::marketplace::(Checksum: 3:dde59f923016d8ca19285ad540641e05)
CREATE TABLE `intent_action` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT, `created_date` DATE, `description` VARCHAR(256), `edited_by_id` BIGINT, `edited_date` DATE, `title` VARCHAR(256) NOT NULL, `uuid` VARCHAR(255), CONSTRAINT `intent_actionPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FKEBCDD397666C6D2` ON `intent_action`(`created_by_id`);

CREATE INDEX `FKEBCDD39E31CB353` ON `intent_action`(`edited_by_id`);

CREATE UNIQUE INDEX `uuid_unique_1366321689429` ON `intent_action`(`uuid`);

ALTER TABLE `intent_action` ADD CONSTRAINT `FKEBCDD397666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent_action` ADD CONSTRAINT `FKEBCDD39E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-1', '2.0.1', '3:dde59f923016d8ca19285ad540641e05', 48);

-- Changeset changelog_7.2.groovy::7.2-2::marketplace::(Checksum: 3:e258eba3bd98eab166a83139cc155884)
CREATE TABLE `intent_direction` (`created_by_id` BIGINT, `created_date` DATE, `description` VARCHAR(250), `edited_by_id` BIGINT, `edited_date` DATE, `id` BIGINT AUTO_INCREMENT  NOT NULL, `title` VARCHAR(7) NOT NULL, `uuid` VARCHAR(255), `version` BIGINT NOT NULL, CONSTRAINT `intent_directPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FKC723A59C7666C6D2` ON `intent_direction`(`created_by_id`);

CREATE INDEX `FKC723A59CE31CB353` ON `intent_direction`(`edited_by_id`);

CREATE UNIQUE INDEX `title_unique_1366386256451` ON `intent_direction`(`title`);

CREATE UNIQUE INDEX `uuid_unique_1366386256451` ON `intent_direction`(`uuid`);

ALTER TABLE `intent_direction` ADD CONSTRAINT `FKC723A59C7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent_direction` ADD CONSTRAINT `FKC723A59CE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x4), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-2', '2.0.1', '3:e258eba3bd98eab166a83139cc155884', 49);

-- Changeset changelog_7.2.groovy::7.2-3::marketplace::(Checksum: 3:bc12e4bff0bfb59fa7e73f8c7540711f)
CREATE TABLE `intent_data_type` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT, `created_date` DATE, `description` VARCHAR(256), `edited_by_id` BIGINT, `edited_date` DATE, `title` VARCHAR(256) NOT NULL, `uuid` VARCHAR(255), CONSTRAINT `intent_data_tPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FKEADB30CC7666C6D2` ON `intent_data_type`(`created_by_id`);

CREATE INDEX `FKEADB30CCE31CB353` ON `intent_data_type`(`edited_by_id`);

CREATE UNIQUE INDEX `uuid_unique_1366398847848` ON `intent_data_type`(`uuid`);

ALTER TABLE `intent_data_type` ADD CONSTRAINT `FKEADB30CC7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent_data_type` ADD CONSTRAINT `FKEADB30CCE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-3', '2.0.1', '3:bc12e4bff0bfb59fa7e73f8c7540711f', 50);

-- Changeset changelog_7.2.groovy::7.2-4::marketplace::(Checksum: 3:73e1796243f4e33c73d7b5b07f17ee46)
CREATE TABLE `owf_widget_types` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT, `created_date` DATE, `description` VARCHAR(255) NOT NULL, `edited_by_id` BIGINT, `edited_date` DATE, `title` VARCHAR(256) NOT NULL, `uuid` VARCHAR(255), CONSTRAINT `owf_widget_typePK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

ALTER TABLE `owf_properties` ADD `owf_widget_type` VARCHAR(255) DEFAULT 'standard' NOT NULL;

CREATE INDEX `FK6AB6A9DF7666C6D2` ON `owf_widget_types`(`created_by_id`);

CREATE INDEX `FK6AB6A9DFE31CB353` ON `owf_widget_types`(`edited_by_id`);

CREATE UNIQUE INDEX `uuid_unique_1366666109930` ON `owf_widget_types`(`uuid`);

ALTER TABLE `owf_widget_types` ADD CONSTRAINT `FK6AB6A9DF7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `owf_widget_types` ADD CONSTRAINT `FK6AB6A9DFE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Add Column, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-4', '2.0.1', '3:73e1796243f4e33c73d7b5b07f17ee46', 51);

-- Changeset changelog_7.2.groovy::7.2-5::marketplace::(Checksum: 3:21571b54501c40307f4bd432fba456f1)
ALTER TABLE `owf_properties` ADD `universal_name` VARCHAR(255);

ALTER TABLE `owf_properties` ADD `stack_context` VARCHAR(200);

ALTER TABLE `owf_properties` ADD `stack_descriptor` TEXT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column (x3)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-5', '2.0.1', '3:21571b54501c40307f4bd432fba456f1', 52);

-- Release Database Lock
