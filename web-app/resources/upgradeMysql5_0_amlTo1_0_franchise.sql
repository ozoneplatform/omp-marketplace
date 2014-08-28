-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 12/10/12 10:05 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE `service_item` ADD `agency` VARCHAR(255);

ALTER TABLE `service_item` ADD `agency_icon` VARCHAR(255);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.1', '3:a1d04babbf1c20d5360e96e65afd2bae', 1);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:dbdf249ec73cc4f356a5d1fa56a22a8f)
ALTER TABLE `service_item` ADD `is_outside` bit;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.1', '3:dbdf249ec73cc4f356a5d1fa56a22a8f', 2);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-3::franchise-store::(Checksum: 3:0ee8acc59cc31553b0362e999aca8aa2)
DROP INDEX `change_detail_object_idx` ON `change_detail`;

DROP INDEX `change_log_object_idx` ON `change_log`;

DROP INDEX `cfd_types_cfd_idx` ON `custom_field_definition_types`;

DROP INDEX `rel_act_log_mod_rel_act_idx` ON `relationship_activity_log`;

DROP INDEX `rel_si_rel_items_id_idx` ON `relationship_service_item`;

DROP INDEX `si_act_si_ver_idx` ON `service_item_activity`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Drop Index (x6)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-3', '2.0.1', '3:0ee8acc59cc31553b0362e999aca8aa2', 3);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-4::franchise-store::(Checksum: 3:25ce821bff0d37532a8013ae30b25cf1)
CREATE TABLE `drop_down_cf_field_value` (`drop_down_cf_field_value_id` BIGINT, `field_value_id` BIGINT, `field_value_list_idx` INT) ENGINE=InnoDB;

ALTER TABLE `custom_field_definition` ADD `is_permanent` bit;

ALTER TABLE `drop_down_cfd` ADD `is_multi_select` bit;

CREATE INDEX `FK2627FFDDA5BD888` ON `drop_down_cf_field_value`(`field_value_id`);

ALTER TABLE `drop_down_cf_field_value` ADD CONSTRAINT `FK2627FFDDA5BD888` FOREIGN KEY (`field_value_id`) REFERENCES `field_value` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.1', '3:25ce821bff0d37532a8013ae30b25cf1', 4);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-5::franchise-store::(Checksum: 3:5c086ddb93bdb6421543df7c5030ada3)
ALTER TABLE `profile` MODIFY `username` VARCHAR(256) NOT NULL;

ALTER TABLE `service_item` MODIFY `tech_poc` VARCHAR(256) NOT NULL;

ALTER TABLE `service_item` MODIFY `title` VARCHAR(256) NOT NULL;

ALTER TABLE `service_item` MODIFY `version_name` VARCHAR(256) NOT NULL;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Add Not-Null Constraint (x4)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-5', '2.0.1', '3:5c086ddb93bdb6421543df7c5030ada3', 5);

-- Release Database Lock
