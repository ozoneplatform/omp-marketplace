--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: changelog_master.groovy
--  Ran at: 10/21/15 7:34 AM
--  Against: root@localhost@jdbc:mysql://localhost:3306/omp
--  Liquibase version: 2.0.5
--  *********************************************************************

--  Create Database Lock Table
CREATE TABLE `DATABASECHANGELOGLOCK` (`ID` INT NOT NULL, `LOCKED` TINYINT(1) NOT NULL, `LOCKGRANTED` DATETIME NULL, `LOCKEDBY` VARCHAR(255) NULL, CONSTRAINT `PK_DATABASECHANGELOGLOCK` PRIMARY KEY (`ID`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOGLOCK` (`ID`, `LOCKED`) VALUES (1, 0);

--  Lock Database
--  Create Database Change Log Table
CREATE TABLE `DATABASECHANGELOG` (`ID` VARCHAR(63) NOT NULL, `AUTHOR` VARCHAR(63) NOT NULL, `FILENAME` VARCHAR(200) NOT NULL, `DATEEXECUTED` DATETIME NOT NULL, `ORDEREXECUTED` INT NOT NULL, `EXECTYPE` VARCHAR(10) NOT NULL, `MD5SUM` VARCHAR(35) NULL, `DESCRIPTION` VARCHAR(255) NULL, `COMMENTS` VARCHAR(255) NULL, `TAG` VARCHAR(255) NULL, `LIQUIBASE` VARCHAR(20) NULL, CONSTRAINT `PK_DATABASECHANGELOG` PRIMARY KEY (`ID`, `AUTHOR`, `FILENAME`)) ENGINE=InnoDB;

--  Changeset changelog_2.3.1.groovy::2.3.1-1::marketplace::(Checksum: 3:bddb7980ca47e011c0807f8144e6cb79)
--  DDL statements for creating Marketplace 2.3.1 database structure
create table U_DOMAIN (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, username varchar(255) not null, created_by_id bigint, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table U_DOMAIN_preferences (preferences bigint, preferences_idx varchar(255), preferences_elt varchar(255) not null) ENGINE=InnoDB;

create table U_VIEWS (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, isadminview bit not null, created_by_id bigint, name varchar(255) not null, isdefault bit not null, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table avatar (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, is_default bit not null, pic mediumblob, created_by_id bigint, content_type varchar(255), edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table category (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, title varchar(50) not null, description varchar(250), created_by_id bigint, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table change_detail (id bigint not null auto_increment, version bigint not null, new_value varchar(4000), object_version bigint not null, object_id bigint not null, field_name varchar(255) not null, old_value varchar(4000), object_class_name varchar(255) not null, primary key (id)) ENGINE=InnoDB;

create table change_log (id bigint not null auto_increment, version bigint not null, object_version bigint not null, changed_by_id bigint not null, object_id bigint not null, description varchar(255) not null, object_class_name varchar(255) not null, change_date datetime not null, primary key (id)) ENGINE=InnoDB;

create table custom_field (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, custom_field_definition_id bigint not null, created_by_id bigint, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table custom_field_definition (id bigint not null auto_increment, version bigint not null, created_date datetime, tooltip varchar(50), created_by_id bigint, edited_date datetime, edited_by_id bigint, label varchar(50) not null, description varchar(250), is_required bit not null, name varchar(50) not null, style_type varchar(255) not null, primary key (id)) ENGINE=InnoDB;

create table custom_field_definition_types (cf_definition_types_id bigint, types_id bigint, types_idx integer) ENGINE=InnoDB;

create table default_images (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, image_id bigint not null, created_by_id bigint, defined_default_type varchar(255) not null, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table drop_down_cf (id bigint not null, value_id bigint, primary key (id)) ENGINE=InnoDB;

create table drop_down_cfd (id bigint not null, primary key (id)) ENGINE=InnoDB;

create table ext_profile (id bigint not null, external_view_url varchar(2083), system_uri varchar(255), external_id varchar(255), external_edit_url varchar(2083), primary key (id), unique (system_uri, external_id)) ENGINE=InnoDB;

create table ext_service_item (id bigint not null, external_view_url varchar(2083), system_uri varchar(255), external_id varchar(255), external_edit_url varchar(2083), primary key (id), unique (system_uri, external_id)) ENGINE=InnoDB;

create table field_value (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, custom_field_definition_id bigint not null, is_enabled integer not null, display_text varchar(255) not null, created_by_id bigint, edited_date datetime, field_values_idx integer, primary key (id)) ENGINE=InnoDB;

create table images (id bigint not null auto_increment, version bigint not null, created_date datetime, is_default bit not null, type varchar(255) not null, created_by_id bigint, content_type varchar(255), bytes mediumblob not null, edited_date datetime, edited_by_id bigint, image_size double precision, primary key (id)) ENGINE=InnoDB;

create table item_comment (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, service_item_id bigint not null, text varchar(250) not null, created_by_id bigint, author_id bigint not null, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table modify_relationship_activity (id bigint not null, primary key (id)) ENGINE=InnoDB;

create table owf_properties (id bigint not null auto_increment, version bigint not null, visible_in_launch bit not null, edited_by_id bigint, created_date datetime, singleton bit not null, created_by_id bigint, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table profile (id bigint not null auto_increment, version bigint not null, created_date datetime not null, created_by_id bigint, bio varchar(1000), edited_date datetime, edited_by_id bigint, username varchar(250) not null unique, email varchar(250), avatar_id bigint, display_name varchar(250), primary key (id)) ENGINE=InnoDB;

create table rating (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, service_item_id bigint not null, created_by_id bigint, author_id bigint not null, rate float not null, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table rejection_activity (id bigint not null, rejection_listing_id bigint, primary key (id)) ENGINE=InnoDB;

create table rejection_justification (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, title varchar(50) not null, description varchar(250), created_by_id bigint, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table rejection_listing (id bigint not null auto_increment, version bigint not null, justification_id bigint, edited_by_id bigint, created_date datetime, service_item_id bigint not null, description varchar(2000), created_by_id bigint, author_id bigint not null, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table relationship (id bigint not null auto_increment, version bigint not null, relationship_type varchar(255) not null, owning_entity_id bigint not null, primary key (id)) ENGINE=InnoDB;

create table relationship_activity_log (mod_rel_activity_id bigint not null, service_item_snapshot_id bigint, items_idx integer) ENGINE=InnoDB;

create table relationship_service_item (relationship_related_items_id bigint, service_item_id bigint, related_items_idx integer) ENGINE=InnoDB;

create table service_item (id bigint not null auto_increment, version bigint not null, created_date datetime, owf_properties_id bigint, avg_rate float not null, approval_status varchar(11) not null, title varchar(50) not null, screenshot1url varchar(2083), image_small_url varchar(2083), image_large_url varchar(2083), total_votes integer not null, launch_url varchar(2083), uuid varchar(255) not null, version_name varchar(50) not null, release_date datetime not null, organization varchar(40), dependencies varchar(1000), types_id bigint not null, screenshot2url varchar(2083), created_by_id bigint, requirements varchar(1000), doc_url varchar(2083), tech_poc varchar(255) not null, edited_date datetime, edited_by_id bigint, total_comments integer not null, is_hidden integer not null, description varchar(4000) not null, state_id bigint not null, install_url varchar(2083), last_activity_id bigint, author_id bigint not null, primary key (id)) ENGINE=InnoDB;

create table service_item_activity (id bigint not null auto_increment, version bigint not null, created_date datetime, action varchar(255) not null, service_item_id bigint not null, service_item_version bigint, created_by_id bigint, edited_date datetime, edited_by_id bigint, activity_timestamp datetime not null, author_id bigint not null, service_item_activities_idx integer, primary key (id)) ENGINE=InnoDB;

create table service_item_category (service_item_categories_id bigint not null, category_id bigint, categories_idx integer) ENGINE=InnoDB;

create table service_item_custom_field (service_item_custom_fields_id bigint, custom_field_id bigint, custom_fields_idx integer) ENGINE=InnoDB;

create table service_item_snapshot (id bigint not null auto_increment, version bigint not null, service_item_id bigint, title varchar(255) not null, primary key (id)) ENGINE=InnoDB;

create table si_recommended_layouts (service_item_id bigint, recommended_layout varchar(255)) ENGINE=InnoDB;

create table state (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, is_published bit not null, title varchar(50) not null, description varchar(250), created_by_id bigint, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table text (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, value varchar(250), created_by_id bigint, read_only bit not null, name varchar(50) not null unique, edited_date datetime, primary key (id)) ENGINE=InnoDB;

create table text_cf (id bigint not null, value varchar(255), primary key (id)) ENGINE=InnoDB;

create table text_cfd (id bigint not null, primary key (id)) ENGINE=InnoDB;

create table types (id bigint not null auto_increment, version bigint not null, created_date datetime, title varchar(50) not null, created_by_id bigint, role_access varchar(9) not null, edited_date datetime, edited_by_id bigint, has_launch_url bit not null, is_widget bit not null, description varchar(250), image_id bigint, ozone_aware bit not null, has_icons bit not null, primary key (id)) ENGINE=InnoDB;

create table user_account (id bigint not null auto_increment, version bigint not null, edited_by_id bigint, created_date datetime, username varchar(250) not null unique, created_by_id bigint, last_login datetime, edited_date datetime, primary key (id)) ENGINE=InnoDB;

alter table U_DOMAIN add index FK97BAABEE7666C6D2 (created_by_id), add constraint FK97BAABEE7666C6D2 foreign key (created_by_id) references profile (id);

alter table U_DOMAIN add index FK97BAABEEE31CB353 (edited_by_id), add constraint FK97BAABEEE31CB353 foreign key (edited_by_id) references profile (id);

alter table U_VIEWS add index FK376C31E47666C6D2 (created_by_id), add constraint FK376C31E47666C6D2 foreign key (created_by_id) references profile (id);

alter table U_VIEWS add index FK376C31E4E31CB353 (edited_by_id), add constraint FK376C31E4E31CB353 foreign key (edited_by_id) references profile (id);

alter table avatar add index FKAC32C1597666C6D2 (created_by_id), add constraint FKAC32C1597666C6D2 foreign key (created_by_id) references profile (id);

alter table avatar add index FKAC32C159E31CB353 (edited_by_id), add constraint FKAC32C159E31CB353 foreign key (edited_by_id) references profile (id);

alter table category add index FK302BCFE7666C6D2 (created_by_id), add constraint FK302BCFE7666C6D2 foreign key (created_by_id) references profile (id);

alter table category add index FK302BCFEE31CB353 (edited_by_id), add constraint FK302BCFEE31CB353 foreign key (edited_by_id) references profile (id);

alter table change_log add index FK80F28E35B624A19E (changed_by_id), add constraint FK80F28E35B624A19E foreign key (changed_by_id) references profile (id);

alter table custom_field add index FK2ACD76AC7666C6D2 (created_by_id), add constraint FK2ACD76AC7666C6D2 foreign key (created_by_id) references profile (id);

alter table custom_field add index FK2ACD76ACE31CB353 (edited_by_id), add constraint FK2ACD76ACE31CB353 foreign key (edited_by_id) references profile (id);

alter table custom_field add index FK2ACD76AC6F62C9ED (custom_field_definition_id), add constraint FK2ACD76AC6F62C9ED foreign key (custom_field_definition_id) references custom_field_definition (id);

alter table custom_field_definition add index FK150F70C6E31CB353 (edited_by_id), add constraint FK150F70C6E31CB353 foreign key (edited_by_id) references profile (id);

alter table custom_field_definition add index FK150F70C67666C6D2 (created_by_id), add constraint FK150F70C67666C6D2 foreign key (created_by_id) references profile (id);

alter table custom_field_definition_types add index FK1A84FFC06928D597 (types_id), add constraint FK1A84FFC06928D597 foreign key (types_id) references types (id);

alter table default_images add index FK6F064E367666C6D2 (created_by_id), add constraint FK6F064E367666C6D2 foreign key (created_by_id) references profile (id);

alter table default_images add index FK6F064E36E31CB353 (edited_by_id), add constraint FK6F064E36E31CB353 foreign key (edited_by_id) references profile (id);

alter table default_images add index FK6F064E36553AF61A (image_id), add constraint FK6F064E36553AF61A foreign key (image_id) references images (id);

alter table drop_down_cf add index FK13ADE7D0BC98CEE3 (value_id), add constraint FK13ADE7D0BC98CEE3 foreign key (value_id) references field_value (id);

alter table field_value add index FK29F571EC7666C6D2 (created_by_id), add constraint FK29F571EC7666C6D2 foreign key (created_by_id) references profile (id);

alter table field_value add index FK29F571ECE31CB353 (edited_by_id), add constraint FK29F571ECE31CB353 foreign key (edited_by_id) references profile (id);

alter table field_value add index FK29F571ECF1F14D3C (custom_field_definition_id), add constraint FK29F571ECF1F14D3C foreign key (custom_field_definition_id) references drop_down_cfd (id);

alter table images add index FKB95A8278E31CB353 (edited_by_id), add constraint FKB95A8278E31CB353 foreign key (edited_by_id) references profile (id);

alter table images add index FKB95A82787666C6D2 (created_by_id), add constraint FKB95A82787666C6D2 foreign key (created_by_id) references profile (id);

create index itm_cmnt_svc_item_id_idx on item_comment (service_item_id);

create index itm_cmnt_author_id_idx on item_comment (author_id);

alter table item_comment add index FKE6D04D337666C6D2 (created_by_id), add constraint FKE6D04D337666C6D2 foreign key (created_by_id) references profile (id);

alter table item_comment add index FKE6D04D33E31CB353 (edited_by_id), add constraint FKE6D04D33E31CB353 foreign key (edited_by_id) references profile (id);

alter table item_comment add index FKE6D04D33C7E5C662 (service_item_id), add constraint FKE6D04D33C7E5C662 foreign key (service_item_id) references service_item (id);

alter table item_comment add index FKE6D04D335A032135 (author_id), add constraint FKE6D04D335A032135 foreign key (author_id) references profile (id);

alter table owf_properties add index FKE88638947666C6D2 (created_by_id), add constraint FKE88638947666C6D2 foreign key (created_by_id) references profile (id);

alter table owf_properties add index FKE8863894E31CB353 (edited_by_id), add constraint FKE8863894E31CB353 foreign key (edited_by_id) references profile (id);

alter table profile add index FKED8E89A9E31CB353 (edited_by_id), add constraint FKED8E89A9E31CB353 foreign key (edited_by_id) references profile (id);

alter table profile add index FKED8E89A97666C6D2 (created_by_id), add constraint FKED8E89A97666C6D2 foreign key (created_by_id) references profile (id);

alter table profile add index FKED8E89A961C3343D (avatar_id), add constraint FKED8E89A961C3343D foreign key (avatar_id) references avatar (id);

create index rating_svc_item_id_idx on rating (service_item_id);

alter table rating add index FKC815B19D7666C6D2 (created_by_id), add constraint FKC815B19D7666C6D2 foreign key (created_by_id) references profile (id);

alter table rating add index FKC815B19DE31CB353 (edited_by_id), add constraint FKC815B19DE31CB353 foreign key (edited_by_id) references profile (id);

alter table rating add index FKC815B19DC7E5C662 (service_item_id), add constraint FKC815B19DC7E5C662 foreign key (service_item_id) references service_item (id);

alter table rating add index FKC815B19D5A032135 (author_id), add constraint FKC815B19D5A032135 foreign key (author_id) references profile (id);

alter table rejection_activity add index FKF35C128582548A4A (rejection_listing_id), add constraint FKF35C128582548A4A foreign key (rejection_listing_id) references rejection_listing (id);

alter table rejection_justification add index FK12B0A53C7666C6D2 (created_by_id), add constraint FK12B0A53C7666C6D2 foreign key (created_by_id) references profile (id);

alter table rejection_justification add index FK12B0A53CE31CB353 (edited_by_id), add constraint FK12B0A53CE31CB353 foreign key (edited_by_id) references profile (id);

create index rej_lst_svc_item_id_idx on rejection_listing (service_item_id);

alter table rejection_listing add index FK3F2BD44E7666C6D2 (created_by_id), add constraint FK3F2BD44E7666C6D2 foreign key (created_by_id) references profile (id);

alter table rejection_listing add index FK3F2BD44EE31CB353 (edited_by_id), add constraint FK3F2BD44EE31CB353 foreign key (edited_by_id) references profile (id);

alter table rejection_listing add index FK3F2BD44EC7E5C662 (service_item_id), add constraint FK3F2BD44EC7E5C662 foreign key (service_item_id) references service_item (id);

alter table rejection_listing add index FK3F2BD44E5A032135 (author_id), add constraint FK3F2BD44E5A032135 foreign key (author_id) references profile (id);

alter table rejection_listing add index FK3F2BD44E19CEB614 (justification_id), add constraint FK3F2BD44E19CEB614 foreign key (justification_id) references rejection_justification (id);

alter table relationship add index FKF06476389D70DD39 (owning_entity_id), add constraint FKF06476389D70DD39 foreign key (owning_entity_id) references service_item (id);

alter table relationship_activity_log add index FK594974BB25A20F9D (service_item_snapshot_id), add constraint FK594974BB25A20F9D foreign key (service_item_snapshot_id) references service_item_snapshot (id);

alter table relationship_service_item add index FKDA02504C7E5C662 (service_item_id), add constraint FKDA02504C7E5C662 foreign key (service_item_id) references service_item (id);

create index svc_item_author_id_idx on service_item (author_id);

alter table service_item add index FK1571565D2746B676 (last_activity_id), add constraint FK1571565D2746B676 foreign key (last_activity_id) references service_item_activity (id);

alter table service_item add index FK1571565D904D6974 (owf_properties_id), add constraint FK1571565D904D6974 foreign key (owf_properties_id) references owf_properties (id);

alter table service_item add index FK1571565DE31CB353 (edited_by_id), add constraint FK1571565DE31CB353 foreign key (edited_by_id) references profile (id);

alter table service_item add index FK1571565D7666C6D2 (created_by_id), add constraint FK1571565D7666C6D2 foreign key (created_by_id) references profile (id);

alter table service_item add index FK1571565D6928D597 (types_id), add constraint FK1571565D6928D597 foreign key (types_id) references types (id);

alter table service_item add index FK1571565D5A032135 (author_id), add constraint FK1571565D5A032135 foreign key (author_id) references profile (id);

alter table service_item add index FK1571565DDFEC3E97 (state_id), add constraint FK1571565DDFEC3E97 foreign key (state_id) references state (id);

create index svc_item_act_svc_item_id_idx on service_item_activity (service_item_id);

alter table service_item_activity add index FK870EA6B1E31CB353 (edited_by_id), add constraint FK870EA6B1E31CB353 foreign key (edited_by_id) references profile (id);

alter table service_item_activity add index FK870EA6B17666C6D2 (created_by_id), add constraint FK870EA6B17666C6D2 foreign key (created_by_id) references profile (id);

alter table service_item_activity add index FK870EA6B1C7E5C662 (service_item_id), add constraint FK870EA6B1C7E5C662 foreign key (service_item_id) references service_item (id);

alter table service_item_activity add index FK870EA6B15A032135 (author_id), add constraint FK870EA6B15A032135 foreign key (author_id) references profile (id);

create index svc_item_cat_id_idx on service_item_category (service_item_categories_id);

alter table service_item_category add index FKECC570A0DA41995D (category_id), add constraint FKECC570A0DA41995D foreign key (category_id) references category (id);

create index svc_item_cst_fld_id_idx on service_item_custom_field (service_item_custom_fields_id);

alter table service_item_custom_field add index FK46E9894E7B56E054 (custom_field_id), add constraint FK46E9894E7B56E054 foreign key (custom_field_id) references custom_field (id);

alter table service_item_snapshot add index FKFABD8966C7E5C662 (service_item_id), add constraint FKFABD8966C7E5C662 foreign key (service_item_id) references service_item (id);

alter table si_recommended_layouts add index FK863C793CC7E5C662 (service_item_id), add constraint FK863C793CC7E5C662 foreign key (service_item_id) references service_item (id);

alter table state add index FK68AC4917666C6D2 (created_by_id), add constraint FK68AC4917666C6D2 foreign key (created_by_id) references profile (id);

alter table state add index FK68AC491E31CB353 (edited_by_id), add constraint FK68AC491E31CB353 foreign key (edited_by_id) references profile (id);

alter table text add index FK36452D7666C6D2 (created_by_id), add constraint FK36452D7666C6D2 foreign key (created_by_id) references profile (id);

alter table text add index FK36452DE31CB353 (edited_by_id), add constraint FK36452DE31CB353 foreign key (edited_by_id) references profile (id);

alter table types add index FK69B5879E31CB353 (edited_by_id), add constraint FK69B5879E31CB353 foreign key (edited_by_id) references profile (id);

alter table types add index FK69B58797666C6D2 (created_by_id), add constraint FK69B58797666C6D2 foreign key (created_by_id) references profile (id);

alter table types add index FK69B5879553AF61A (image_id), add constraint FK69B5879553AF61A foreign key (image_id) references images (id);

alter table user_account add index FK14C321B97666C6D2 (created_by_id), add constraint FK14C321B97666C6D2 foreign key (created_by_id) references profile (id);

alter table user_account add index FK14C321B9E31CB353 (edited_by_id), add constraint FK14C321B9E31CB353 foreign key (edited_by_id) references profile (id);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'DDL statements for creating Marketplace 2.3.1 database structure', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_2.3.1.groovy', '2.3.1-1', '2.0.5', '3:bddb7980ca47e011c0807f8144e6cb79', 1);

--  Changeset changelog_2.4.0.groovy::2.4.0-1::marketplace::(Checksum: 3:6649bfff3f3cebdaad804653d1ac0edf)
--  Create affiliated_marketplace table
CREATE TABLE `affiliated_marketplace` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `active` INT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATETIME NULL, `edited_by_id` BIGINT NULL, `edited_date` DATETIME NULL, `icon_id` BIGINT NULL, `name` VARCHAR(50) NOT NULL, `server_url` VARCHAR(2083) NOT NULL, `timeout` BIGINT NULL, CONSTRAINT `PK_AFFILIATED_MARKETPLACE` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create affiliated_marketplace table', NOW(), 'Create Table', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-1', '2.0.5', '3:6649bfff3f3cebdaad804653d1ac0edf', 2);

--  Changeset changelog_2.4.0.groovy::2.4.0-2.1::marketplace::(Checksum: 3:479540701d442f1b9621438f2929afc1)
--  Disable foreign key check for mysql when modifying FK constraints, Enable after done modifying.
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Disable foreign key check for mysql when modifying FK constraints, Enable after done modifying.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-2.1', '2.0.5', '3:479540701d442f1b9621438f2929afc1', 3);

--  Changeset changelog_2.4.0.groovy::2.4.0-2::marketplace::(Checksum: 3:67ad76339bec05c947305e1ce09393bd)
--  Create FK constraints for affiliated_marketplace table
ALTER TABLE `affiliated_marketplace` ADD CONSTRAINT `FKA6EB2C37666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `affiliated_marketplace` ADD CONSTRAINT `FKA6EB2C3E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `affiliated_marketplace` ADD CONSTRAINT `FKA6EB2C3EA25263C` FOREIGN KEY (`icon_id`) REFERENCES `images` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create FK constraints for affiliated_marketplace table', NOW(), 'Add Foreign Key Constraint (x3)', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-2', '2.0.5', '3:67ad76339bec05c947305e1ce09393bd', 4);

--  Changeset changelog_2.4.0.groovy::2.4.0-5::marketplace::(Checksum: 3:d7e77d4b934ff6a18ab341422e50c752)
--  Add not-null constraints for ext_profile and ext_service_item. Update profile.bio column type.
ALTER TABLE `ext_profile` MODIFY `system_uri` VARCHAR(255) NOT NULL;

ALTER TABLE `ext_service_item` MODIFY `system_uri` VARCHAR(255) NOT NULL;

ALTER TABLE `profile` MODIFY `bio` VARCHAR(1000);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Add not-null constraints for ext_profile and ext_service_item. Update profile.bio column type.', NOW(), 'Add Not-Null Constraint (x2), Modify data type', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-5', '2.0.5', '3:d7e77d4b934ff6a18ab341422e50c752', 5);

--  Changeset changelog_2.4.0.groovy::2.4.0-2.2::marketplace::(Checksum: 3:749647065b1f0a294fcbed81e9ca6e06)
--  Enable foreign key check for mysql when DONE modifying FK constraints.
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Enable foreign key check for mysql when DONE modifying FK constraints.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-2.2', '2.0.5', '3:749647065b1f0a294fcbed81e9ca6e06', 6);

--  Changeset changelog_2.4.0.groovy::2.4.0-3::marketplace::(Checksum: 3:7a686549fc702788c4411a4fa326c5ec)
--  Drop types.is_widget column
ALTER TABLE `types` DROP COLUMN `is_widget`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Drop types.is_widget column', NOW(), 'Drop Column', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-3', '2.0.5', '3:7a686549fc702788c4411a4fa326c5ec', 7);

--  Changeset changelog_2.5.0.groovy::2.5.0-1::marketplace::(Checksum: 3:20f8da34e147dafc7c0107cee22ca4ba)
ALTER TABLE `category` ADD `uuid` VARCHAR(255);

ALTER TABLE `state` ADD `uuid` VARCHAR(255);

ALTER TABLE `types` ADD `uuid` VARCHAR(255);

CREATE UNIQUE INDEX `category_uuid_idx` ON `category`(`uuid`);

CREATE UNIQUE INDEX `state_uuid_idx` ON `state`(`uuid`);

CREATE UNIQUE INDEX `types_uuid_idx` ON `types`(`uuid`);

ALTER TABLE `profile` ADD `uuid` VARCHAR(255);

ALTER TABLE `custom_field_definition` ADD `uuid` VARCHAR(255);

CREATE UNIQUE INDEX `profile_uuid_idx` ON `profile`(`uuid`);

CREATE UNIQUE INDEX `cfd_uuid_idx` ON `custom_field_definition`(`uuid`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column (x3), Create Index (x3), Add Column (x2), Create Index (x2)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-1', '2.0.5', '3:20f8da34e147dafc7c0107cee22ca4ba', 8);

--  Changeset changelog_2.5.0.groovy::2.5.0-3::marketplace::(Checksum: 3:7ad852b9ea5b3e51df8b1898837d458e)
ALTER TABLE `owf_properties` ADD `background` TINYINT(1) NOT NULL DEFAULT 0;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-3', '2.0.5', '3:7ad852b9ea5b3e51df8b1898837d458e', 9);

--  Changeset changelog_2.5.0.groovy::2.5.0-4::marketplace::(Checksum: 3:d45e76a74dcc6a048378341726cb3794)
CREATE TABLE `import_task` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATETIME NULL, `cron_exp` VARCHAR(255) NULL, `download_images` bit NOT NULL, `edited_by_id` BIGINT NULL, `edited_date` DATETIME NULL, `enabled` bit NOT NULL, `exec_interval` INT NULL, `extra_url_params` VARCHAR(512) NULL, `interface_config_id` BIGINT NOT NULL, `last_run_result_id` BIGINT NULL, `name` VARCHAR(50) NOT NULL, `update_type` VARCHAR(7) NOT NULL, `url` VARCHAR(255) NULL, `keystore_pass` VARCHAR(2048) NULL, `keystore_path` VARCHAR(2048) NULL, `truststore_path` VARCHAR(2048) NULL, CONSTRAINT `import_taskPK` PRIMARY KEY (`id`), UNIQUE (`name`)) ENGINE=InnoDB;

CREATE TABLE `import_task_result` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `message` VARCHAR(4000) NOT NULL, `result` bit NOT NULL, `run_date` DATETIME NOT NULL, `task_id` BIGINT NOT NULL, CONSTRAINT `import_task_rPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `interface_configuration` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `allow_truncate` bit NOT NULL, `auto_create_meta_data` bit NOT NULL, `default_large_icon_url` VARCHAR(2048) NULL, `default_small_icon_url` VARCHAR(2048) NULL, `delta_since_time_param` VARCHAR(64) NULL, `delta_static_parameters` VARCHAR(2048) NULL, `full_static_parameters` VARCHAR(2048) NULL, `loose_match` bit NOT NULL, `name` VARCHAR(256) NOT NULL, `query_date_format` VARCHAR(32) NULL, `response_date_format` VARCHAR(32) NULL, CONSTRAINT `interface_conPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table (x3)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-4', '2.0.5', '3:d45e76a74dcc6a048378341726cb3794', 10);

--  Changeset changelog_2.5.0.groovy::2.5.0-5::marketplace::(Checksum: 3:f6cb3e205f6f2414aeb88e9f687f1791)
ALTER TABLE `import_task` ADD CONSTRAINT `FK578EF9DF7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `import_task` ADD CONSTRAINT `FK578EF9DFE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `import_task` ADD CONSTRAINT `FK578EF9DFA31F8712` FOREIGN KEY (`interface_config_id`) REFERENCES `interface_configuration` (`id`);

ALTER TABLE `import_task` ADD CONSTRAINT `FK578EF9DF919216CA` FOREIGN KEY (`last_run_result_id`) REFERENCES `import_task_result` (`id`);

ALTER TABLE `import_task_result` ADD CONSTRAINT `FK983AC27D11D7F882` FOREIGN KEY (`task_id`) REFERENCES `import_task` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Foreign Key Constraint (x5)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-5', '2.0.5', '3:f6cb3e205f6f2414aeb88e9f687f1791', 11);

--  Changeset changelog_2.5.0.groovy::2.5.0-6::marketplace::(Checksum: 3:0fbf0612d8e3cf537bc44335f28d1654)
ALTER TABLE `service_item` MODIFY `title` VARCHAR(256);

ALTER TABLE `service_item` MODIFY `version_name` VARCHAR(256);

ALTER TABLE `service_item` MODIFY `organization` VARCHAR(256);

ALTER TABLE `service_item` MODIFY `tech_poc` VARCHAR(256);

ALTER TABLE `profile` MODIFY `username` VARCHAR(256);

ALTER TABLE `profile` MODIFY `display_name` VARCHAR(256);

ALTER TABLE `profile` MODIFY `email` VARCHAR(256);

ALTER TABLE `text_cf` MODIFY `value` VARCHAR(256);

ALTER TABLE `ext_service_item` MODIFY `system_uri` VARCHAR(256);

ALTER TABLE `ext_service_item` MODIFY `external_id` VARCHAR(256);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Modify data type (x10)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-6', '2.0.5', '3:0fbf0612d8e3cf537bc44335f28d1654', 12);

--  Changeset changelog_5.0.groovy::5.0-1::marketplace::(Checksum: 3:fc5a25b1ec26ad35157cbb13b848adc4)
--  Drop types.role_access and u_views table
ALTER TABLE `types` DROP COLUMN `role_access`;

DROP TABLE `U_VIEWS`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Drop types.role_access and u_views table', NOW(), 'Drop Column, Drop Table', 'EXECUTED', 'changelog_5.0.groovy', '5.0-1', '2.0.5', '3:fc5a25b1ec26ad35157cbb13b848adc4', 13);

--  Changeset changelog_5.0.groovy::5.0-2::marketplace::(Checksum: 3:e0ec212634fc9ad519fd02d3240774e2)
--  Add columns for counting votes to service_item
ALTER TABLE `service_item` ADD `total_rate1` INT;

ALTER TABLE `service_item` ADD `total_rate2` INT;

ALTER TABLE `service_item` ADD `total_rate3` INT;

ALTER TABLE `service_item` ADD `total_rate4` INT;

ALTER TABLE `service_item` ADD `total_rate5` INT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Add columns for counting votes to service_item', NOW(), 'Add Column (x5)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-2', '2.0.5', '3:e0ec212634fc9ad519fd02d3240774e2', 14);

--  Changeset changelog_5.0.groovy::5.0-3::marketplace::(Checksum: 3:5f6ba95466cbe695beb449f18dadf636)
--  Replace import_task.download_images with interface_configuration.download_images
ALTER TABLE `interface_configuration` ADD `download_images` TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE `import_task` DROP COLUMN `download_images`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Replace import_task.download_images with interface_configuration.download_images', NOW(), 'Add Column, Drop Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-3', '2.0.5', '3:5f6ba95466cbe695beb449f18dadf636', 15);

--  Changeset changelog_5.0.groovy::5.0-4::marketplace::(Checksum: 3:b424e240f657bf64281648dc04d5d236)
--  Drop not-null constraint for and expand 'text' field in item_comment table
ALTER TABLE `item_comment` MODIFY `text` VARCHAR(250) NULL;

ALTER TABLE `item_comment` MODIFY `text` VARCHAR(4000);

ALTER TABLE `item_comment` ADD `rate` FLOAT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Drop not-null constraint for and expand ''text'' field in item_comment table', NOW(), 'Drop Not-Null Constraint, Modify data type, Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-4', '2.0.5', '3:b424e240f657bf64281648dc04d5d236', 16);

--  Changeset changelog_5.0.groovy::5.0-7::marketplace::(Checksum: 3:2d396f22f301193c7279b5fa21d33d99)
--  Drop RATING table
DROP TABLE `rating`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Drop RATING table', NOW(), 'Drop Table', 'EXECUTED', 'changelog_5.0.groovy', '5.0-7', '2.0.5', '3:2d396f22f301193c7279b5fa21d33d99', 17);

--  Changeset changelog_5.0.groovy::5.0-8::marketplace::(Checksum: 3:b01c491aa1315d6fbacd9b59dbd9faf7)
--  Add tables supporting text area and image URL custom fields
CREATE TABLE `image_url_cf` (`id` BIGINT NOT NULL, `value` VARCHAR(2083) NULL, CONSTRAINT `image_url_cfPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `image_url_cfd` (`id` BIGINT NOT NULL, CONSTRAINT `image_url_cfdPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `text_area_cf` (`id` BIGINT NOT NULL, `value` VARCHAR(4000) NULL, CONSTRAINT `text_area_cfPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `text_area_cfd` (`id` BIGINT NOT NULL, CONSTRAINT `text_area_cfdPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Add tables supporting text area and image URL custom fields', NOW(), 'Create Table (x4)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-8', '2.0.5', '3:b01c491aa1315d6fbacd9b59dbd9faf7', 18);

--  Changeset changelog_5.0.groovy::5.0-9::marketplace::(Checksum: 3:5a5d74d439b8a633c30541221e8ec124)
--  Add tables supporting check box custom fields
CREATE TABLE `check_box_cf` (`id` BIGINT NOT NULL, `value` bit NULL, CONSTRAINT `check_box_cfPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `check_box_cfd` (`id` BIGINT NOT NULL, `selected_by_default` bit NULL, CONSTRAINT `check_box_cfdPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Add tables supporting check box custom fields', NOW(), 'Create Table (x2)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-9', '2.0.5', '3:5a5d74d439b8a633c30541221e8ec124', 19);

--  Changeset changelog_5.0.groovy::5.0-10::marketplace::(Checksum: 3:3161bd12012792d0da7ab51979cb4f9b)
--  Add column to custom filed defintion table to store the section where to display that field
ALTER TABLE `custom_field_definition` ADD `section` VARCHAR(255);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Add column to custom filed defintion table to store the section where to display that field', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-10', '2.0.5', '3:3161bd12012792d0da7ab51979cb4f9b', 20);

--  Changeset changelog_5.0.groovy::5.0-11::marketplace::(Checksum: 3:701f5fdf006444dbf8e28f0b791c999a)
ALTER TABLE `custom_field_definition` ADD `all_types` TINYINT(1) NOT NULL DEFAULT 0;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-11', '2.0.5', '3:701f5fdf006444dbf8e28f0b791c999a', 21);

--  Changeset changelog_5.0.groovy::5.0-12::marketplace::(Checksum: 3:d25a6166facdcb45342bda12c7e3d7fc)
--  Add column to service item table to store the approved date
ALTER TABLE `service_item` ADD `approval_date` DATETIME;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Add column to service item table to store the approved date', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-12', '2.0.5', '3:d25a6166facdcb45342bda12c7e3d7fc', 22);

--  Changeset changelog_5.0.groovy::5.0-14::marketplace::(Checksum: 3:3091b73b99472559cae278d8c2c3858b)
--  Create indexes for change_detail and change_log tables to speed up lookups
CREATE INDEX `change_detail_object_idx` ON `change_detail`(`object_class_name`, `object_id`, `object_version`);

CREATE INDEX `change_log_object_idx` ON `change_log`(`object_class_name`, `object_id`, `object_version`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create indexes for change_detail and change_log tables to speed up lookups', NOW(), 'Create Index (x2)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-14', '2.0.5', '3:3091b73b99472559cae278d8c2c3858b', 23);

--  Changeset changelog_5.0.groovy::5.0-15::marketplace::(Checksum: 3:07d03d98d92fb50e61d9c52df992d6cd)
--  Create indexes for custom_field_definition_types and field_value tables to speed up lookups
CREATE INDEX `cfd_types_cfd_idx` ON `custom_field_definition_types`(`cf_definition_types_id`);

CREATE INDEX `cfd_types_types_idx` ON `custom_field_definition_types`(`types_id`);

CREATE INDEX `field_value_cfd_idx` ON `field_value`(`custom_field_definition_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create indexes for custom_field_definition_types and field_value tables to speed up lookups', NOW(), 'Create Index (x3)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-15', '2.0.5', '3:07d03d98d92fb50e61d9c52df992d6cd', 24);

--  Changeset changelog_5.0.groovy::5.0-16::marketplace::(Checksum: 3:4cfe9e1b266fa8f81917b9c2f8255e6d)
--  Create indexes for tables to speed up lookups
CREATE INDEX `si_snapshot_id_idx` ON `service_item_snapshot`(`service_item_id`);

CREATE INDEX `cf_cfd_idx` ON `custom_field`(`custom_field_definition_id`);

CREATE INDEX `si_rec_layouts_idx` ON `si_recommended_layouts`(`service_item_id`);

CREATE INDEX `rejection_act_listing_id_idx` ON `rejection_activity`(`rejection_listing_id`);

CREATE INDEX `rejection_listing_just_id_idx` ON `rejection_listing`(`justification_id`);

CREATE INDEX `relationship_owing_id_idx` ON `relationship`(`owning_entity_id`);

CREATE INDEX `rel_act_log_mod_rel_act_idx` ON `relationship_activity_log`(`mod_rel_activity_id`);

CREATE INDEX `rel_act_log_mod_si_snpsht_idx` ON `relationship_activity_log`(`service_item_snapshot_id`);

CREATE INDEX `rel_si_rel_items_id_idx` ON `relationship_service_item`(`relationship_related_items_id`);

CREATE INDEX `si_act_si_ver_idx` ON `service_item_activity`(`service_item_version`);

CREATE INDEX `si_cat_cat_id_idx` ON `service_item_category`(`category_id`);

CREATE INDEX `si_cf_cf_id_idx` ON `service_item_custom_field`(`custom_field_id`);

CREATE INDEX `si_owf_props_id_idx` ON `service_item`(`owf_properties_id`);

CREATE INDEX `si_types_id_idx` ON `service_item`(`types_id`);

CREATE INDEX `si_state_id_idx` ON `service_item`(`state_id`);

CREATE INDEX `si_last_activity_idx` ON `service_item`(`last_activity_id`);

CREATE INDEX `si_created_by_id_idx` ON `service_item`(`created_by_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create indexes for tables to speed up lookups', NOW(), 'Create Index (x17)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-16', '2.0.5', '3:4cfe9e1b266fa8f81917b9c2f8255e6d', 25);

--  Changeset changelog_5.0.groovy::5.0-18::marketplace::(Checksum: 3:43678308dc3a992c28f53b4efeb30268)
--  Create indexes for rejection_listing table to speed up lookups
CREATE INDEX `rej_lst_author_id_idx` ON `rejection_listing`(`author_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create indexes for rejection_listing table to speed up lookups', NOW(), 'Create Index', 'EXECUTED', 'changelog_5.0.groovy', '5.0-18', '2.0.5', '3:43678308dc3a992c28f53b4efeb30268', 26);

--  Changeset changelog_5.0_aml.groovy::aml_5.0-1::aml-marketplace::(Checksum: 3:2d1368449dd017f06e542e16fd8b4b1c)
CREATE TABLE `score_card` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATETIME NULL, `edited_by_id` BIGINT NULL, `edited_date` DATETIME NULL, `score` FLOAT(19) NOT NULL, CONSTRAINT `score_cardPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-1', '2.0.5', '3:2d1368449dd017f06e542e16fd8b4b1c', 27);

--  Changeset changelog_5.0_aml.groovy::aml_5.0-2::aml-marketplace::(Checksum: 3:ffb57b9f6c0d3d879f9aa049cc73e5c2)
CREATE TABLE `score_card_item` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATETIME NULL, `description` VARCHAR(500) NOT NULL, `edited_by_id` BIGINT NULL, `edited_date` DATETIME NULL, `is_standard_question` TINYINT(1) NOT NULL, `question` VARCHAR(250) NOT NULL, `weight` FLOAT(19) NULL, CONSTRAINT `sc_itemPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-2', '2.0.5', '3:ffb57b9f6c0d3d879f9aa049cc73e5c2', 28);

--  Changeset changelog_5.0_aml.groovy::aml_5.0-3::aml-marketplace::(Checksum: 3:a2362da0fc0c5c2adcf631b0d5bfb729)
CREATE TABLE `score_card_item_response` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATETIME NULL, `edited_by_id` BIGINT NULL, `edited_date` DATETIME NULL, `is_satisfied` TINYINT(1) NOT NULL, `score_card_id` BIGINT NOT NULL, `score_card_item_id` BIGINT NOT NULL, CONSTRAINT `sc_responsePK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-3', '2.0.5', '3:a2362da0fc0c5c2adcf631b0d5bfb729', 29);

--  Changeset changelog_5.0_aml.groovy::aml_5.0-4::aml-marketplace::(Checksum: 3:a10c921a8ef6370cc64cc06016c7c55c)
ALTER TABLE `service_item` ADD `score_card_id` BIGINT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-4', '2.0.5', '3:a10c921a8ef6370cc64cc06016c7c55c', 30);

--  Changeset changelog_5.0_aml.groovy::aml_5.0-5::aml-marketplace::(Checksum: 3:63bdefa2034c53e720100a0e83826dab)
CREATE INDEX `FK5E60409D7666C6D2` ON `score_card`(`created_by_id`);

CREATE INDEX `FK5E60409DE31CB353` ON `score_card`(`edited_by_id`);

CREATE INDEX `FKE51CCD757666C6D2` ON `score_card_item`(`created_by_id`);

CREATE INDEX `FKE51CCD75E31CB353` ON `score_card_item`(`edited_by_id`);

CREATE INDEX `FK80A6CBCB190E00BC` ON `score_card_item_response`(`score_card_id`);

CREATE INDEX `FK80A6CBCB7666C6D2` ON `score_card_item_response`(`created_by_id`);

CREATE INDEX `FK80A6CBCBE31CB353` ON `score_card_item_response`(`edited_by_id`);

CREATE INDEX `FK80A6CBCBEF469C97` ON `score_card_item_response`(`score_card_item_id`);

CREATE INDEX `FK1571565D190E00BC` ON `service_item`(`score_card_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Create Index (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-5', '2.0.5', '3:63bdefa2034c53e720100a0e83826dab', 31);

--  Changeset changelog_5.0_aml.groovy::aml_5.0-6::aml-marketplace::(Checksum: 3:b4c50686ed2e827d6a5d5de248d5154c)
ALTER TABLE `score_card` ADD CONSTRAINT `FK5E60409D7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card` ADD CONSTRAINT `FK5E60409DE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item` ADD CONSTRAINT `FKE51CCD757666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item` ADD CONSTRAINT `FKE51CCD75E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCB7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCBE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCB190E00BC` FOREIGN KEY (`score_card_id`) REFERENCES `score_card` (`id`);

ALTER TABLE `score_card_item_response` ADD CONSTRAINT `FK80A6CBCBEF469C97` FOREIGN KEY (`score_card_item_id`) REFERENCES `score_card_item` (`id`);

ALTER TABLE `service_item` ADD CONSTRAINT `FK1571565D190E00BC` FOREIGN KEY (`score_card_id`) REFERENCES `score_card` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('aml-marketplace', '', NOW(), 'Add Foreign Key Constraint (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-6', '2.0.5', '3:b4c50686ed2e827d6a5d5de248d5154c', 32);

--  Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE `service_item` ADD `agency` VARCHAR(255);

ALTER TABLE `service_item` ADD `agency_icon` VARCHAR(255);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.5', '3:a1d04babbf1c20d5360e96e65afd2bae', 33);

--  Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:dbdf249ec73cc4f356a5d1fa56a22a8f)
ALTER TABLE `service_item` ADD `is_outside` bit;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.5', '3:dbdf249ec73cc4f356a5d1fa56a22a8f', 34);

--  Changeset changelog_1.0_franchise.groovy::1.0_franchise-3::franchise-store::(Checksum: 3:0ee8acc59cc31553b0362e999aca8aa2)
DROP INDEX `change_detail_object_idx` ON `change_detail`;

DROP INDEX `change_log_object_idx` ON `change_log`;

DROP INDEX `cfd_types_cfd_idx` ON `custom_field_definition_types`;

DROP INDEX `rel_act_log_mod_rel_act_idx` ON `relationship_activity_log`;

DROP INDEX `rel_si_rel_items_id_idx` ON `relationship_service_item`;

DROP INDEX `si_act_si_ver_idx` ON `service_item_activity`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Drop Index (x6)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-3', '2.0.5', '3:0ee8acc59cc31553b0362e999aca8aa2', 35);

--  Changeset changelog_1.0_franchise.groovy::1.0_franchise-4::franchise-store::(Checksum: 3:25ce821bff0d37532a8013ae30b25cf1)
CREATE TABLE `drop_down_cf_field_value` (`drop_down_cf_field_value_id` BIGINT NULL, `field_value_id` BIGINT NULL, `field_value_list_idx` INT NULL) ENGINE=InnoDB;

ALTER TABLE `custom_field_definition` ADD `is_permanent` bit;

ALTER TABLE `drop_down_cfd` ADD `is_multi_select` bit;

CREATE INDEX `FK2627FFDDA5BD888` ON `drop_down_cf_field_value`(`field_value_id`);

ALTER TABLE `drop_down_cf_field_value` ADD CONSTRAINT `FK2627FFDDA5BD888` FOREIGN KEY (`field_value_id`) REFERENCES `field_value` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.5', '3:25ce821bff0d37532a8013ae30b25cf1', 36);

--  Changeset changelog_1.0_franchise.groovy::1.0_franchise-5::franchise-store::(Checksum: 3:5c086ddb93bdb6421543df7c5030ada3)
ALTER TABLE `profile` MODIFY `username` VARCHAR(256) NOT NULL;

ALTER TABLE `service_item` MODIFY `tech_poc` VARCHAR(256) NOT NULL;

ALTER TABLE `service_item` MODIFY `title` VARCHAR(256) NOT NULL;

ALTER TABLE `service_item` MODIFY `version_name` VARCHAR(256) NOT NULL;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Add Not-Null Constraint (x4)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-5', '2.0.5', '3:5c086ddb93bdb6421543df7c5030ada3', 37);

--  Changeset changelog_1.2_franchise.groovy::1.2_franchise-1::franchise-store::(Checksum: 3:a3a3eaab836585ac44bc0d4b7828c381)
CREATE TABLE `application_configuration` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `code` VARCHAR(250) NOT NULL, `value` VARCHAR(2000) NULL, `title` VARCHAR(250) NOT NULL, `description` VARCHAR(2000) NULL, `type` VARCHAR(250) NOT NULL, `group_name` VARCHAR(250) NOT NULL, `sub_group_name` VARCHAR(250) NULL, `mutable` TINYINT(1) NOT NULL, `sub_group_order` BIGINT NULL, CONSTRAINT `application_configurationPK` PRIMARY KEY (`id`), UNIQUE (`code`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-1', '2.0.5', '3:a3a3eaab836585ac44bc0d4b7828c381', 38);

--  Changeset changelog_1.2_franchise.groovy::1.2_franchise-2::franchise-store::(Checksum: 3:8c76d18c40c82ecf837da46af444846c)
--  Create index for application_configuration.group_name
CREATE INDEX `FKFC9C0477666C6D2` ON `application_configuration`(`created_by_id`);

CREATE INDEX `FKFC9C047E31CB353` ON `application_configuration`(`edited_by_id`);

CREATE INDEX `app_config_group_name_idx` ON `application_configuration`(`group_name`);

ALTER TABLE `application_configuration` ADD CONSTRAINT `FKFC9C0477666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `application_configuration` ADD CONSTRAINT `FKFC9C047E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('franchise-store', 'Create index for application_configuration.group_name', NOW(), 'Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-2', '2.0.5', '3:8c76d18c40c82ecf837da46af444846c', 39);

--  Changeset changelog_7.1.groovy::7.1-1::marketplace::(Checksum: 3:70d3313cedb44a2430d183b912b1cfe8)
ALTER TABLE `owf_properties` ADD `height` BIGINT;

ALTER TABLE `owf_properties` ADD `width` BIGINT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_7.1.groovy', '7.1-1', '2.0.5', '3:70d3313cedb44a2430d183b912b1cfe8', 40);

--  Changeset changelog_7.1.groovy::7.1-2::marketplace::(Checksum: 3:5e5d0f3fee742d246977b284fdd76d73)
ALTER TABLE `application_configuration` ADD `help` VARCHAR(2000);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.1.groovy', '7.1-2', '2.0.5', '3:5e5d0f3fee742d246977b284fdd76d73', 41);

--  Changeset changelog_7.2.groovy::7.2-1::marketplace::(Checksum: 3:dde59f923016d8ca19285ad540641e05)
CREATE TABLE `intent_action` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `description` VARCHAR(256) NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `title` VARCHAR(256) NOT NULL, `uuid` VARCHAR(255) NULL, CONSTRAINT `intent_actionPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FKEBCDD397666C6D2` ON `intent_action`(`created_by_id`);

CREATE INDEX `FKEBCDD39E31CB353` ON `intent_action`(`edited_by_id`);

CREATE UNIQUE INDEX `uuid_unique_1366321689429` ON `intent_action`(`uuid`);

ALTER TABLE `intent_action` ADD CONSTRAINT `FKEBCDD397666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent_action` ADD CONSTRAINT `FKEBCDD39E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-1', '2.0.5', '3:dde59f923016d8ca19285ad540641e05', 42);

--  Changeset changelog_7.2.groovy::7.2-2::marketplace::(Checksum: 3:e258eba3bd98eab166a83139cc155884)
CREATE TABLE `intent_direction` (`created_by_id` BIGINT NULL, `created_date` DATE NULL, `description` VARCHAR(250) NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `id` BIGINT AUTO_INCREMENT NOT NULL, `title` VARCHAR(7) NOT NULL, `uuid` VARCHAR(255) NULL, `version` BIGINT NOT NULL, CONSTRAINT `intent_directPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FKC723A59C7666C6D2` ON `intent_direction`(`created_by_id`);

CREATE INDEX `FKC723A59CE31CB353` ON `intent_direction`(`edited_by_id`);

CREATE UNIQUE INDEX `title_unique_1366386256451` ON `intent_direction`(`title`);

CREATE UNIQUE INDEX `uuid_unique_1366386256451` ON `intent_direction`(`uuid`);

ALTER TABLE `intent_direction` ADD CONSTRAINT `FKC723A59C7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent_direction` ADD CONSTRAINT `FKC723A59CE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x4), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-2', '2.0.5', '3:e258eba3bd98eab166a83139cc155884', 43);

--  Changeset changelog_7.2.groovy::7.2-3::marketplace::(Checksum: 3:bc12e4bff0bfb59fa7e73f8c7540711f)
CREATE TABLE `intent_data_type` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `description` VARCHAR(256) NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `title` VARCHAR(256) NOT NULL, `uuid` VARCHAR(255) NULL, CONSTRAINT `intent_data_tPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FKEADB30CC7666C6D2` ON `intent_data_type`(`created_by_id`);

CREATE INDEX `FKEADB30CCE31CB353` ON `intent_data_type`(`edited_by_id`);

CREATE UNIQUE INDEX `uuid_unique_1366398847848` ON `intent_data_type`(`uuid`);

ALTER TABLE `intent_data_type` ADD CONSTRAINT `FKEADB30CC7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent_data_type` ADD CONSTRAINT `FKEADB30CCE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-3', '2.0.5', '3:bc12e4bff0bfb59fa7e73f8c7540711f', 44);

--  Changeset changelog_7.2.groovy::7.2-4::marketplace::(Checksum: 3:09a1dc754584d1cd1efe6b077afe39ae)
CREATE TABLE `owf_widget_types` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `description` VARCHAR(255) NOT NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `title` VARCHAR(256) NOT NULL, `uuid` VARCHAR(255) NULL, CONSTRAINT `owf_widget_typePK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

ALTER TABLE `owf_properties` ADD `owf_widget_type` VARCHAR(255) NOT NULL DEFAULT 'standard';

CREATE INDEX `FK6AB6A9DF7666C6D2` ON `owf_widget_types`(`created_by_id`);

CREATE INDEX `FK6AB6A9DFE31CB353` ON `owf_widget_types`(`edited_by_id`);

CREATE UNIQUE INDEX `uuid_unique_1366666109930` ON `owf_widget_types`(`uuid`);

ALTER TABLE `owf_widget_types` ADD CONSTRAINT `FK6AB6A9DF7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `owf_widget_types` ADD CONSTRAINT `FK6AB6A9DFE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Add Column, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-4', '2.0.5', '3:09a1dc754584d1cd1efe6b077afe39ae', 45);

--  Changeset changelog_7.2.groovy::7.2-5::marketplace::(Checksum: 3:21571b54501c40307f4bd432fba456f1)
ALTER TABLE `owf_properties` ADD `universal_name` VARCHAR(255);

ALTER TABLE `owf_properties` ADD `stack_context` VARCHAR(200);

ALTER TABLE `owf_properties` ADD `stack_descriptor` LONGTEXT;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column (x3)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-5', '2.0.5', '3:21571b54501c40307f4bd432fba456f1', 46);

--  Changeset changelog_7.3.0.groovy::7.3.0-1::marketplace::(Checksum: 3:ef88eb1178b48b3590a7291563871a30)
ALTER TABLE `score_card_item` ADD `image` VARCHAR(255);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-1', '2.0.5', '3:ef88eb1178b48b3590a7291563871a30', 47);

--  Changeset changelog_7.3.0.groovy::7.3.0-2::marketplace::(Checksum: 3:cb41b1179b6a373e0861c7b29400d814)
ALTER TABLE `service_item_activity` ADD `details` VARCHAR(255);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-2', '2.0.5', '3:cb41b1179b6a373e0861c7b29400d814', 48);

--  Changeset changelog_7.3.0.groovy::7.3.0-3::marketplace::(Checksum: 3:ab1c2a78ad360b01b91b1a2232ae3c87)
ALTER TABLE `owf_properties` ADD `descriptor_url` VARCHAR(2083);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-3', '2.0.5', '3:ab1c2a78ad360b01b91b1a2232ae3c87', 49);

--  Changeset changelog_7.3.0.groovy::7.3.0-4::marketplace::(Checksum: 3:8dad893690319284db723406dfc00350)
ALTER TABLE `service_item` MODIFY `description` VARCHAR(4000) NULL;

ALTER TABLE `service_item` MODIFY `release_date` DATETIME NULL;

ALTER TABLE `service_item` MODIFY `state_id` BIGINT NULL;

ALTER TABLE `service_item` MODIFY `tech_poc` VARCHAR(256) NULL;

ALTER TABLE `service_item` MODIFY `version_name` VARCHAR(256) NULL;

ALTER TABLE `service_item_category` MODIFY `service_item_categories_id` BIGINT NULL;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Drop Not-Null Constraint (x6)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-4', '2.0.5', '3:8dad893690319284db723406dfc00350', 50);

--  Changeset changelog_7.3.0.groovy::7.3.0-5::marketplace::(Checksum: 3:28e58147bcb07fab3ae955b45d9352e3)
CREATE TABLE `intent` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `action_id` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `data_type_id` BIGINT NOT NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `receive` TINYINT(1) NOT NULL, `send` TINYINT(1) NOT NULL, CONSTRAINT `intentPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `owf_properties_intent` (`owf_properties_intents_id` BIGINT NULL, `intent_id` BIGINT NULL) ENGINE=InnoDB;

CREATE INDEX `FKB971369CD8544299` ON `intent`(`action_id`);

CREATE INDEX `FKB971369C7666C6D2` ON `intent`(`created_by_id`);

CREATE INDEX `FKB971369C283F938E` ON `intent`(`data_type_id`);

CREATE INDEX `FKB971369CE31CB353` ON `intent`(`edited_by_id`);

CREATE INDEX `FK3F99ECA7A651895D` ON `owf_properties_intent`(`intent_id`);

CREATE INDEX `owfProps_intent_id_idx` ON `owf_properties_intent`(`owf_properties_intents_id`);

ALTER TABLE `intent` ADD CONSTRAINT `FKB971369CD8544299` FOREIGN KEY (`action_id`) REFERENCES `intent_action` (`id`);

ALTER TABLE `intent` ADD CONSTRAINT `FKB971369C7666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `intent` ADD CONSTRAINT `FKB971369C283F938E` FOREIGN KEY (`data_type_id`) REFERENCES `intent_data_type` (`id`);

ALTER TABLE `intent` ADD CONSTRAINT `FKB971369CE31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `owf_properties_intent` ADD CONSTRAINT `FK3F99ECA7A651895D` FOREIGN KEY (`intent_id`) REFERENCES `intent` (`id`);

ALTER TABLE `owf_properties_intent` ADD CONSTRAINT `FK3F99ECA74704E25C` FOREIGN KEY (`owf_properties_intents_id`) REFERENCES `owf_properties` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table (x2), Create Index (x6), Add Foreign Key Constraint (x6)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-5', '2.0.5', '3:28e58147bcb07fab3ae955b45d9352e3', 51);

--  Changeset changelog_7.3.0.groovy::7.3.0-6::marketplace::(Checksum: 3:188326c86a75bf88826fe0addaaed0bb)
ALTER TABLE `types` ADD `is_permanent` TINYINT(1);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-6', '2.0.5', '3:188326c86a75bf88826fe0addaaed0bb', 52);

--  Changeset changelog_7.3.0.groovy::7.3.0-8::marketplace::(Checksum: 3:0e4b85a436faf5855fe99ffe9238c780)
ALTER TABLE `score_card_item` ADD `show_on_listing` TINYINT(1);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-8', '2.0.5', '3:0e4b85a436faf5855fe99ffe9238c780', 53);

--  Changeset changelog_7.3.0.groovy::7.3.0-12::marketplace::(Checksum: 3:81da42021cf40d5046b0d45387730d23)
--  Create new mapping table for service item's 'owners' field replacing 'author' property.
CREATE TABLE `service_item_profile` (`service_item_owners_id` BIGINT NULL, `profile_id` BIGINT NULL, `owners_idx` INT NULL) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create new mapping table for service item''s ''owners'' field replacing ''author'' property.', NOW(), 'Create Table', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-12', '2.0.5', '3:81da42021cf40d5046b0d45387730d23', 54);

--  Changeset changelog_7.3.0.groovy::7.3.0-14::marketplace::(Checksum: 3:22e4413e8d2db347eca366766100decf)
--  Drop 'author_id' column corresponding to the 'author' property of service item being removed.
DROP INDEX `svc_item_author_id_idx` ON `service_item`;

ALTER TABLE `service_item` DROP FOREIGN KEY `FK1571565D5A032135`;

ALTER TABLE `service_item` DROP COLUMN `author_id`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Drop ''author_id'' column corresponding to the ''author'' property of service item being removed.', NOW(), 'Drop Index, Drop Foreign Key Constraint, Drop Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-14', '2.0.5', '3:22e4413e8d2db347eca366766100decf', 55);

--  Changeset changelog_7.3.0.groovy::7.3.0-15::marketplace::(Checksum: 3:8a4b6a9ffea16feb446d6b2fb4429825)
CREATE TABLE `service_item_documentation_url` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT DEFAULT 0 NOT NULL, `name` VARCHAR(256) NOT NULL, `service_item_id` BIGINT NOT NULL, `url` VARCHAR(2083) NOT NULL, CONSTRAINT `service_item_PK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE TABLE `service_item_tech_pocs` (`service_item_id` BIGINT NULL, `tech_poc` VARCHAR(256) NULL) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-15', '2.0.5', '3:8a4b6a9ffea16feb446d6b2fb4429825', 56);

--  Changeset changelog_7.3.0.groovy::7.3.0-16::marketplace::(Checksum: 3:29b11454e2ee263c40d977090718a9ca)
ALTER TABLE `service_item_documentation_url` ADD CONSTRAINT `FK24572D08C7E5C662` FOREIGN KEY (`service_item_id`) REFERENCES `service_item` (`id`);

ALTER TABLE `service_item_tech_pocs` ADD CONSTRAINT `FKA55CFB56C7E5C662` FOREIGN KEY (`service_item_id`) REFERENCES `service_item` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-16', '2.0.5', '3:29b11454e2ee263c40d977090718a9ca', 57);

--  Changeset changelog_7.3.0.groovy::7.3.0-19::marketplace::(Checksum: 3:b6082ec33d0f9cd5358039b2cb06e19f)
ALTER TABLE `service_item` DROP COLUMN `doc_url`;

ALTER TABLE `service_item` DROP COLUMN `tech_poc`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Drop Column (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-19', '2.0.5', '3:b6082ec33d0f9cd5358039b2cb06e19f', 58);

--  Changeset changelog_7.5.0.groovy::7.5.0-1::marketplace::(Checksum: 3:e609efe728480f5fa22e324cb8fa364c)
CREATE TABLE `agency` (`id` BIGINT AUTO_INCREMENT NOT NULL, `title` VARCHAR(255) NULL, `icon_url` VARCHAR(2083) NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `version` BIGINT DEFAULT 0 NOT NULL, CONSTRAINT `agencyPK` PRIMARY KEY (`id`), UNIQUE (`title`)) ENGINE=InnoDB;

ALTER TABLE `service_item` ADD `agency_id` BIGINT;

ALTER TABLE `service_item` ADD CONSTRAINT `SERVICE_ITEM_AGENCY_FK` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE SET NULL;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Add Column, Add Foreign Key Constraint', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-1', '2.0.5', '3:e609efe728480f5fa22e324cb8fa364c', 59);

--  Changeset changelog_7.5.0.groovy::7.5.0-3::marketplace::(Checksum: 3:978dc473dd30d3beb49553a281692079)
ALTER TABLE `service_item` DROP COLUMN `agency`;

ALTER TABLE `service_item` DROP COLUMN `agency_icon`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Drop Column (x2)', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-3', '2.0.5', '3:978dc473dd30d3beb49553a281692079', 60);

--  Changeset changelog_7.6.0.groovy::7.6.0-1::marketplace::(Checksum: 3:8652867eb3c624d0e73892ba40f72549)
CREATE TABLE `screenshot` (`id` BIGINT AUTO_INCREMENT NOT NULL, `small_image_url` VARCHAR(2083) NOT NULL, `large_image_url` VARCHAR(2083) NULL, `ordinal` INT NULL, `service_item_id` BIGINT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `version` BIGINT DEFAULT 0 NOT NULL, CONSTRAINT `screenshotPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

ALTER TABLE `screenshot` ADD CONSTRAINT `SCREENSHOT_SERVICE_ITEM_FK` FOREIGN KEY (`service_item_id`) REFERENCES `service_item` (`id`) ON DELETE CASCADE;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Add Foreign Key Constraint', 'EXECUTED', 'changelog_7.6.0.groovy', '7.6.0-1', '2.0.5', '3:8652867eb3c624d0e73892ba40f72549', 61);

--  Changeset changelog_7.6.0.groovy::7.6.0-2::marketplace::(Checksum: 3:ed5b1d1912b7769b4449c3533a5e4a86)
INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot1url, 0, id
                FROM service_item
                WHERE screenshot1url IS NOT NULL AND screenshot1url <> '';

INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot2url, 1, id
                FROM service_item
                WHERE screenshot2url IS NOT NULL AND screenshot2url <> '';

ALTER TABLE `service_item` DROP COLUMN `screenshot1url`;

ALTER TABLE `service_item` DROP COLUMN `screenshot2url`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Custom SQL (x2), Drop Column (x2)', 'EXECUTED', 'changelog_7.6.0.groovy', '7.6.0-2', '2.0.5', '3:ed5b1d1912b7769b4449c3533a5e4a86', 62);

--  Changeset changelog_7.9.0.groovy::7.9.0-2::marketplace::(Checksum: 3:47c89f94ae81a109928fbef3287e0e05)
CREATE TABLE `contact_type` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `required` TINYINT(1) NOT NULL, `title` VARCHAR(50) NOT NULL, CONSTRAINT `contact_typePK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-2', '2.0.5', '3:47c89f94ae81a109928fbef3287e0e05', 63);

--  Changeset changelog_7.9.0.groovy::7.9.0-3::marketplace::(Checksum: 3:84a1cac2f49e8a997bb5cadbe98281d1)
CREATE TABLE `contact` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `email` VARCHAR(100) NOT NULL, `name` VARCHAR(100) NOT NULL, `organization` VARCHAR(100) NULL, `secure_phone` VARCHAR(50) NULL, `type_id` BIGINT NOT NULL, `service_item_id` BIGINT NOT NULL, `unsecure_phone` VARCHAR(50) NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, CONSTRAINT `contactPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `FK4C2BB7F97666C6D2` ON `contact_type`(`created_by_id`);

CREATE INDEX `FK4C2BB7F9E31CB353` ON `contact_type`(`edited_by_id`);

CREATE INDEX `FK38B72420C7E5C662` ON `contact`(`service_item_id`);

CREATE INDEX `FK38B72420BA3FC877` ON `contact`(`type_id`);

CREATE INDEX `FK38B72420E31CB353` ON `contact`(`edited_by_id`);

CREATE INDEX `FK38B724207666C6D2` ON `contact`(`created_by_id`);

ALTER TABLE `contact_type` ADD CONSTRAINT `FK4C2BB7F97666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `contact_type` ADD CONSTRAINT `FK4C2BB7F9E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `contact` ADD CONSTRAINT `FK38B724207666C6D2` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `contact` ADD CONSTRAINT `FK38B72420E31CB353` FOREIGN KEY (`edited_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `contact` ADD CONSTRAINT `FK38B72420BA3FC877` FOREIGN KEY (`type_id`) REFERENCES `contact_type` (`id`);

ALTER TABLE `contact` ADD CONSTRAINT `FK38B72420C7E5C662` FOREIGN KEY (`service_item_id`) REFERENCES `service_item` (`id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x6), Add Foreign Key Constraint (x6)', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-3', '2.0.5', '3:84a1cac2f49e8a997bb5cadbe98281d1', 64);

--  Changeset changelog_7.9.0.groovy::7.9.0-4::marketplace::(Checksum: 3:d76e0aa887053b1f64792f3bfd91cbe2)
CREATE UNIQUE INDEX `title_unique_1389723125532` ON `contact_type`(`title`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Index', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-4', '2.0.5', '3:d76e0aa887053b1f64792f3bfd91cbe2', 65);

--  Changeset changelog_7.10.0.groovy::7.10.0-1::marketplace::(Checksum: 3:c36101b773b019ee4625b75411cb8eb6)
ALTER TABLE `service_item` ADD `opens_in_new_browser_tab` TINYINT(1);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-1', '2.0.5', '3:c36101b773b019ee4625b75411cb8eb6', 66);

--  Changeset changelog_7.10.0.groovy::7.10.0-2::marketplace::(Checksum: 3:beafab57293fa84b39d3e25be9531b43)
ALTER TABLE `profile` ADD `user_roles` VARCHAR(255);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-2', '2.0.5', '3:beafab57293fa84b39d3e25be9531b43', 67);

--  Changeset changelog_7.11.0.groovy::7.11.0-2::marketplace::(Checksum: 3:46b29bbb4574aec82193638d9c1a08d4)
ALTER TABLE `change_detail` ADD `service_item_activity_id` BIGINT NOT NULL;

CREATE INDEX `FKB4467BC0855307BD` ON `change_detail`(`service_item_activity_id`);

ALTER TABLE `change_detail` ADD CONSTRAINT `FKB4467BC0855307BD` FOREIGN KEY (`service_item_activity_id`) REFERENCES `service_item_activity` (`id`);

ALTER TABLE `service_item_activity` DROP COLUMN `service_item_version`;

ALTER TABLE `service_item_activity` DROP COLUMN `details`;

ALTER TABLE `change_detail` DROP COLUMN `object_class_name`;

ALTER TABLE `change_detail` DROP COLUMN `object_id`;

ALTER TABLE `change_detail` DROP COLUMN `object_version`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column, Create Index, Add Foreign Key Constraint, Drop Column (x5)', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-2', '2.0.5', '3:46b29bbb4574aec82193638d9c1a08d4', 68);

--  Changeset changelog_7.12.0.groovy::7.12.0-1::marketplace::(Checksum: 3:932edbdb31fd51477e9580ee37bc8fba)
DROP TABLE `change_log`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Drop Table', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-1', '2.0.5', '3:932edbdb31fd51477e9580ee37bc8fba', 69);

--  Changeset changelog_7.12.0.groovy::7.12.0-2::marketplace::(Checksum: 3:1c807d392fd79380b2e030a451e6ea0b)
CREATE TABLE `tag` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `created_date` DATE NULL, `edited_by_id` BIGINT NULL, `edited_date` DATE NULL, `title` VARCHAR(16) NOT NULL, CONSTRAINT `tag_PK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `tag_title_idx` ON `tag`(`title`);

CREATE TABLE `service_item_tag` (`id` BIGINT AUTO_INCREMENT NOT NULL, `service_item_id` BIGINT NOT NULL, `tag_id` BIGINT NOT NULL, `created_by_id` BIGINT NULL, `version` BIGINT NOT NULL, CONSTRAINT `service_item_tag_PK` PRIMARY KEY (`id`)) ENGINE=InnoDB;

CREATE INDEX `service_item_tag_si_idx` ON `service_item_tag`(`service_item_id`);

CREATE INDEX `service_item_tag_tag_idx` ON `service_item_tag`(`tag_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index, Create Table, Create Index (x2)', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-2', '2.0.5', '3:1c807d392fd79380b2e030a451e6ea0b', 70);

--  Changeset changelog_7.12.0.groovy::7.12.0-3::marketplace::(Checksum: 3:81c00e342acc2d70a14bee90e6b5de57)
ALTER TABLE `service_item_tag` ADD CONSTRAINT `service_item_tag_FK_si` FOREIGN KEY (`service_item_id`) REFERENCES `service_item` (`id`);

ALTER TABLE `service_item_tag` ADD CONSTRAINT `service_item_tag_FK_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`);

ALTER TABLE `service_item_tag` ADD CONSTRAINT `service_item_tag_FK_cb` FOREIGN KEY (`created_by_id`) REFERENCES `profile` (`id`);

ALTER TABLE `service_item_tag` ADD CONSTRAINT `service_item_tag_unique_idx` UNIQUE (`service_item_id`, `tag_id`);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Foreign Key Constraint (x3), Add Unique Constraint', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-3', '2.0.5', '3:81c00e342acc2d70a14bee90e6b5de57', 71);

--  Changeset changelog_7.16.0.groovy::7.16.0-1::marketplace::(Checksum: 3:2818d572466e125521de5380a02cf7e4)
--  switch to case-sensitive collation for the tags table
ALTER TABLE tag CONVERT TO CHARSET latin1 COLLATE latin1_general_cs;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'switch to case-sensitive collation for the tags table', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-1', '2.0.5', '3:2818d572466e125521de5380a02cf7e4', 72);

--  Changeset changelog_7.16.0.groovy::7.16.0-2::marketplace::(Checksum: 3:9c7fbae6fdea582b51eeed650647e080)
ALTER TABLE `service_item` ADD `image_medium_url` VARCHAR(2083);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-2', '2.0.5', '3:9c7fbae6fdea582b51eeed650647e080', 73);

--  Changeset changelog_7.16.0.groovy::7.16.0-5::marketplace::(Checksum: 3:254414ea4972c81264cc952d9c24e279)
CREATE TABLE `service_item_score_card_item` (`service_item_id` BIGINT NULL, `score_card_item_id` BIGINT NULL) ENGINE=InnoDB;

ALTER TABLE `service_item_score_card_item` ADD CONSTRAINT `FKBF91F93EF469C97` FOREIGN KEY (`score_card_item_id`) REFERENCES `score_card_item` (`id`) ON DELETE CASCADE;

ALTER TABLE `service_item_score_card_item` ADD CONSTRAINT `FKBF91F939C51FA9F` FOREIGN KEY (`service_item_id`) REFERENCES `service_item` (`id`);

ALTER TABLE `score_card` DROP FOREIGN KEY `FK5E60409D7666C6D2`;

ALTER TABLE `score_card` DROP FOREIGN KEY `FK5E60409DE31CB353`;

ALTER TABLE `score_card_item_response` DROP FOREIGN KEY `FK80A6CBCB7666C6D2`;

ALTER TABLE `score_card_item_response` DROP FOREIGN KEY `FK80A6CBCBE31CB353`;

ALTER TABLE `score_card_item_response` DROP FOREIGN KEY `FK80A6CBCB190E00BC`;

ALTER TABLE `score_card_item_response` DROP FOREIGN KEY `FK80A6CBCBEF469C97`;

ALTER TABLE `service_item` DROP FOREIGN KEY `FK1571565D190E00BC`;

ALTER TABLE `score_card_item` DROP COLUMN `is_standard_question`;

ALTER TABLE `score_card_item` DROP COLUMN `weight`;

ALTER TABLE `service_item` DROP COLUMN `score_card_id`;

DROP TABLE `score_card`;

DROP TABLE `score_card_item_response`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Create Table, Add Foreign Key Constraint (x2), Drop Foreign Key Constraint (x7), Drop Column (x3), Drop Table (x2)', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-5', '2.0.5', '3:254414ea4972c81264cc952d9c24e279', 74);

--  Changeset app_config.groovy::app_config-2::marketplace::(Checksum: 3:f3084b1a17c66754a2444e37659a69a4)
INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.name', 'BRANDING', 1, NULL, 1, ' ', 'String', '', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.logo', 'BRANDING', 1, NULL, 2, ' ', 'Image', '/static/themes/gold.theme/images/Mp_logo.png', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.icon', 'BRANDING', 1, NULL, 3, ' ', 'Image', '/static/themes/common/images/agency/agencyDefault.png', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('free.warning.content', 'BRANDING', 1, NULL, 4, ' ', 'String', '', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('url.public', 'BRANDING', 1, NULL, 5, ' ', 'string', '', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('about.box.content', 'BRANDING', 1, 'About Information', 1, ' ', 'String', 'The Store allows visitors to discover and explore business and convenience applications and enables user-configurable visualizations of available content.', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('about.box.image', 'BRANDING', 1, 'About Information', 2, ' ', 'Image', '/static/themes/gold.theme/images/Mp_logo_128x128.png', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('access.alert.enable', 'BRANDING', 1, 'Access Alert Information', 1, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('access.alert.content', 'BRANDING', 1, 'Access Alert Information', 2, ' ', 'String', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla interdum eleifend sapien dignissim malesuada. Sed imperdiet augue vitae justo feugiat eget porta est blandit. Proin ipsum ipsum, rutrum ac gravida in, ullamcorper a augue. Sed at scelerisque augue. Morbi scelerisque gravida sapien ut feugiat. Donec dictum, nisl commodo dapibus pellentesque, enim quam consectetur quam, at dictum dui augue at risus. Ut id nunc in justo molestie semper. Curabitur magna velit, varius eu porttitor et, tempor pulvinar nulla. Nam at tellus nec felis tincidunt fringilla. Nunc nisi sem, egestas ut consequat eget, luctus et nisi. Nulla et lorem odio, vitae pretium ipsum. Integer tellus libero, molestie a feugiat a, imperdiet sit amet metus. Aenean auctor fringilla eros, sit amet suscipit felis eleifend a.', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.footer.featured.title', 'BRANDING', 1, 'Footer Information', 1, ' ', 'String', 'Store', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.footer.featured.content', 'BRANDING', 1, 'Footer Information', 2, ' ', 'String', 'The Store allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content.', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.open.search.title.message', 'BRANDING', 1, 'Open Search', 1, ' ', 'String', 'Marketplace Search', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.open.search.description.message', 'BRANDING', 1, 'Open Search', 2, ' ', 'String', 'Marketplace Search Description', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.open.search.fav.icon', 'BRANDING', 1, 'Open Search', 3, ' ', 'Image', '/static/themes/gold.theme/images/favicon.ico', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.open.search.site.icon', 'BRANDING', 1, 'Open Search', 4, ' ', 'Image', '/static/themes/common/images/themes/default/market_64x64.png', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.custom.header.url', 'BRANDING', 1, 'Custom Header and Footer', 1, ' ', 'String', NULL, 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.custom.header.height', 'BRANDING', 1, 'Custom Header and Footer', 2, ' ', 'Integer', '0', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.custom.footer.url', 'BRANDING', 1, 'Custom Header and Footer', 3, ' ', 'String', NULL, 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.custom.footer.height', 'BRANDING', 1, 'Custom Header and Footer', 4, ' ', 'Integer', '0', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.custom.css', 'BRANDING', 1, 'Custom Header and Footer', 5, ' ', 'String', NULL, 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.custom.js', 'BRANDING', 1, 'Custom Header and Footer', 6, ' ', 'String', NULL, 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.insideOutside.behavior', 'ADDITIONAL_CONFIGURATION', 1, NULL, 1, ' ', 'String', 'ADMIN_SELECTED', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.enable.ext.serviceitem', 'ADDITIONAL_CONFIGURATION', 1, NULL, 2, ' ', 'Boolean', 'false', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.allow.owner.to.edit.approved.listing', 'ADDITIONAL_CONFIGURATION', 1, NULL, 3, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.amp.search.result.size', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Search', 1, ' ', 'Integer', '30', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.amp.search.default.timeout', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Search', 2, ' ', 'Integer', '30000', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.image.allow.upload', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Image Configurations', 1, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.type.image.max.size', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Image Configurations', 2, ' ', 'Integer', '1048576', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.amp.image.max.size', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Image Configurations', 3, ' ', 'Integer', '1048576', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.owf.sync.urls', 'ADDITIONAL_CONFIGURATION', 1, 'OWF Sync', 1, ' ', 'List', NULL, 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.contact.email', 'ADDITIONAL_CONFIGURATION', 1, 'Store Contact Information', 1, ' ', 'String', NULL, 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.quick.view.detail.fields', 'ADDITIONAL_CONFIGURATION', 1, 'Quick View', 1, ' ', 'String', 'types, categories, state, releasedDate, lastActivity, owners, organization, Alternate POC Info, Technical POC Info, Support POC Info, launchUrl', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.enable.scoreCard', 'SCORECARD', 1, NULL, 1, ' ', 'Boolean', 'false', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.is.franchise', 'HIDDEN', 0, NULL, 1, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.default.theme', 'HIDDEN', 1, NULL, 2, ' ', 'String', 'gold', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.job.disable.accounts.interval', 'HIDDEN', 1, NULL, 3, ' ', 'Integer', '1440', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.job.disable.accounts.start.time', 'HIDDEN', 1, NULL, 4, ' ', 'String', '23:59:59', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.session.control.enabled', 'USER_ACCOUNT_SETTINGS', 1, 'Session Control', 1, ' ', 'Boolean', 'false', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.session.control.max.concurrent', 'USER_ACCOUNT_SETTINGS', 1, 'Session Control', 2, ' ', 'Integer', '1', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.disable.inactive.accounts', 'USER_ACCOUNT_SETTINGS', 1, 'Inactive Accounts', 1, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.inactivity.threshold', 'USER_ACCOUNT_SETTINGS', 1, 'Inactive Accounts', 2, ' ', 'Integer', '90', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.enable.cef.logging', 'AUDITING', 1, NULL, 1, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.enable.cef.object.access.logging', 'AUDITING', 1, NULL, 2, ' ', 'Boolean', 'false', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.enable.cef.log.sweep', 'AUDITING', 1, NULL, 3, ' ', 'Boolean', 'true', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.cef.log.location', 'AUDITING', 1, NULL, 4, ' ', 'String', '/usr/share/tomcat6', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.cef.sweep.log.location', 'AUDITING', 1, NULL, 5, ' ', 'String', '/var/log/cef', 0);

INSERT INTO `application_configuration` (`code`, `group_name`, `mutable`, `sub_group_name`, `sub_group_order`, `title`, `type`, `value`, `version`) VALUES ('store.security.level', 'AUDITING', 1, NULL, 6, ' ', 'String', NULL, 0);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Insert Row (x47)', 'EXECUTED', 'app_config.groovy', 'app_config-2', '2.0.5', '3:f3084b1a17c66754a2444e37659a69a4', 75);

--  Changeset changelog_7.16.0.groovy::7.16.0-6::marketplace::(Checksum: 3:d2f31c636d691a47e3d2e8b73cbd66b1)
DELETE FROM `application_configuration`  WHERE code = 'store.domains';

UPDATE `application_configuration` SET `sub_group_name` = 'Partner Store Image Configurations' WHERE sub_group_name = 'Image Configurations';

UPDATE `application_configuration` SET `sub_group_name` = 'Partner Store Search' WHERE sub_group_name = 'Affiliated Search';

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Delete Data, Update Data (x2)', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-6', '2.0.5', '3:d2f31c636d691a47e3d2e8b73cbd66b1', 76);

--  Changeset changelog_7.16.0.groovy::7.16.0-7::marketplace::(Checksum: 3:027bd76dec2ca88af0145b25dfc00c93)
ALTER TABLE `U_DOMAIN_preferences` RENAME `u_domain_preferences`;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Rename Table', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-7', '2.0.5', '3:027bd76dec2ca88af0145b25dfc00c93', 77);

--  Changeset changelog_7.16.0.groovy::7.16.0-8::marketplace::(Checksum: 3:54b5f1e34983065c4273b232dece1777)
ALTER TABLE `owf_properties` ADD `mobile_ready` TINYINT(1) NOT NULL DEFAULT 0;

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-8', '2.0.5', '3:54b5f1e34983065c4273b232dece1777', 78);

--  Changeset changelog_7.16.0.groovy::7.16.0-9::marketplace::(Checksum: 3:2337d16fb8f4bbd2ea73b9c3a830c68f)
UPDATE application_configuration SET type = 'String' WHERE type = 'string';

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-9', '2.0.5', '3:2337d16fb8f4bbd2ea73b9c3a830c68f', 79);

--  *********************************************************************
--  SQL to add all changesets to database history table
--  *********************************************************************
--  Change Log: changelog_master.groovy
--  Ran at: 10/21/15 7:35 AM
--  Against: root@localhost@jdbc:mysql://localhost:3306/omp
--  Liquibase version: 2.0.5
--  *********************************************************************

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Transfer rating information into the item_comment table', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-5', '2.0.5', '3:8b35402c8c016b3dd7a9b83f8f23348a', 276);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Compute number of votes for each rating for the service items', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-6', '2.0.5', '3:9ed670c788094838125b3045c42f6fce', 277);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Transfer approved date information into the service_item table', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-13', '2.0.5', '3:6efbf9a22068bcab382bc6a466bf40ae', 278);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Set the section for existing cfds to typeProperties', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-17', '2.0.5', '3:ce25f8b65d80c3e960d0883264d5206f', 279);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Update Data (x3)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-7', '2.0.5', '3:b337838d37b361db713c9fcfad1af557', 280);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Update Data (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-9', '2.0.5', '3:8386d1edefea8f79361809de2eea6608', 281);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create a record in service_item_profile table for each service item linking it with its owner.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-13', '2.0.5', '3:3658ca8cb6ad5d02fc45644d4a37a38b', 282);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create a record in service_item_tech_pocs table for each service item linking it with its technical POC.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-17', '2.0.5', '3:ff31dc39fa460d736a824986b06ed8e2', 283);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'Create a record in service_item_documentation_url table for each service item linking it with its technical POC.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-18', '2.0.5', '3:7daee389a18729b712930a71f89bb6f9', 284);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Custom SQL (x2)', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-2', '2.0.5', '3:a45d3e114365ea29cc7cfc4fec431a94', 285);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Update Data', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-4', '2.0.5', '3:8554bf7cdf0efcf8fe8105bb50d3dd30', 286);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', 'create temporary table to hold change detail information while the tables are modified', NOW(), 'Create Table, Custom SQL (x3)', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-1', '2.0.5', '3:1edfcd293610d3a2f803bae919b9826d', 287);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-3', '2.0.5', '3:400733a9dc22034bcb0b8b8e411e55b2', 288);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Update Data', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-3', '2.0.5', '3:f1f70bdbd696fdb464ebe75521269059', 289);

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Update Data', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-4', '2.0.5', '3:e0aa28a5c3bb94e98ae4195dfa54d632', 290);

