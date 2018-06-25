-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/10/12 1:30 PM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_5.0_aml.groovy::aml_5.0-1::aml-marketplace::(Checksum: 3:2d1368449dd017f06e542e16fd8b4b1c)
CREATE TABLE `score_card` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT, `created_date` DATETIME, `edited_by_id` BIGINT, `edited_date` DATETIME, `score` FLOAT(19) NOT NULL, CONSTRAINT `score_cardPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-1', '2.0.1', '3:2d1368449dd017f06e542e16fd8b4b1c', 1);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-2::aml-marketplace::(Checksum: 3:ffb57b9f6c0d3d879f9aa049cc73e5c2)
CREATE TABLE `score_card_item` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT, `created_date` DATETIME, `description` VARCHAR(500) NOT NULL, `edited_by_id` BIGINT, `edited_date` DATETIME, `is_standard_question` TINYINT(1) NOT NULL, `question` VARCHAR(250) NOT NULL, `weight` FLOAT(19), CONSTRAINT `sc_itemPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-2', '2.0.1', '3:ffb57b9f6c0d3d879f9aa049cc73e5c2', 2);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-3::aml-marketplace::(Checksum: 3:a2362da0fc0c5c2adcf631b0d5bfb729)
CREATE TABLE `score_card_item_response` (`id` BIGINT AUTO_INCREMENT  NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT, `created_date` DATETIME, `edited_by_id` BIGINT, `edited_date` DATETIME, `is_satisfied` TINYINT(1) NOT NULL, `score_card_id` BIGINT NOT NULL, `score_card_item_id` BIGINT NOT NULL, CONSTRAINT `sc_responsePK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-3', '2.0.1', '3:a2362da0fc0c5c2adcf631b0d5bfb729', 3);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-4::aml-marketplace::(Checksum: 3:a10c921a8ef6370cc64cc06016c7c55c)
ALTER TABLE `service_item` ADD `score_card_id` BIGINT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-4', '2.0.1', '3:a10c921a8ef6370cc64cc06016c7c55c', 4);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-5::aml-marketplace::(Checksum: 3:63bdefa2034c53e720100a0e83826dab)
CREATE INDEX `FK5E60409D7666C6D2` ON `score_card`(`created_by_id`);

CREATE INDEX `FK5E60409DE31CB353` ON `score_card`(`edited_by_id`);

CREATE INDEX `FKE51CCD757666C6D2` ON `score_card_item`(`created_by_id`);

CREATE INDEX `FKE51CCD75E31CB353` ON `score_card_item`(`edited_by_id`);

CREATE INDEX `FK80A6CBCB190E00BC` ON `score_card_item_response`(`score_card_id`);

CREATE INDEX `FK80A6CBCB7666C6D2` ON `score_card_item_response`(`created_by_id`);

CREATE INDEX `FK80A6CBCBE31CB353` ON `score_card_item_response`(`edited_by_id`);

CREATE INDEX `FK80A6CBCBEF469C97` ON `score_card_item_response`(`score_card_item_id`);

CREATE INDEX `FK1571565D190E00BC` ON `service_item`(`score_card_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Index (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-5', '2.0.1', '3:63bdefa2034c53e720100a0e83826dab', 5);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-6::aml-marketplace::(Checksum: 3:b4c50686ed2e827d6a5d5de248d5154c)
ALTER TABLE `score_card` ADD CONSTRAINT `FK5E60409D7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card` ADD CONSTRAINT `FK5E60409DE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item` ADD CONSTRAINT `FKE51CCD757666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item` ADD CONSTRAINT `FKE51CCD75E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCB7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCBE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCB190E00BC` FOREIGN KEY (`score_card_id`) REFERENCES `score_card` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCBEF469C97` FOREIGN KEY (`score_card_item_id`) REFERENCES `score_card_item` (`id`);

ALTER TABLE `service_item` ADD CONSTRAINT `FK1571565D190E00BC` FOREIGN KEY (`score_card_id`) REFERENCES `score_card` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Add Foreign Key Constraint (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-6', '2.0.1', '3:b4c50686ed2e827d6a5d5de248d5154c', 6);

-- Release Database Lock
