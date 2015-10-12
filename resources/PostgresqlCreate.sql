-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/21/15 7:39 AM
-- Against: postgres@jdbc:postgresql://localhost:5432/omp
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITH TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

INSERT INTO databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE databasechangelog (ID VARCHAR(63) NOT NULL, AUTHOR VARCHAR(63) NOT NULL, FILENAME VARCHAR(200) NOT NULL, DATEEXECUTED TIMESTAMP WITH TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONSTRAINT PK_DATABASECHANGELOG PRIMARY KEY (ID, AUTHOR, FILENAME));

-- Changeset changelog_2.3.1.groovy::2.3.1-1::marketplace::(Checksum: 3:6516205a9ad1a1b533980e2f6fec358b)
-- DDL statements for creating Marketplace 2.3.1 database structure
create table U_DOMAIN (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, username varchar(255) not null, created_by_id int8, edited_date timestamp, primary key (id));

create table U_DOMAIN_preferences (preferences int8, preferences_idx varchar(255), preferences_elt varchar(255) not null);

create table U_VIEWS (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, isadminview bool not null, created_by_id int8, name varchar(255) not null, isdefault bool not null, edited_date timestamp, primary key (id));

create table avatar (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, is_default bool not null, pic bytea, created_by_id int8, content_type varchar(255), edited_date timestamp, primary key (id));

create table category (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, title varchar(50) not null, description varchar(250), created_by_id int8, edited_date timestamp, primary key (id));

create table change_detail (id int8 not null, version int8 not null, new_value varchar(4000), object_version int8 not null, object_id int8 not null, field_name varchar(255) not null, old_value varchar(4000), object_class_name varchar(255) not null, primary key (id));

create table change_log (id int8 not null, version int8 not null, object_version int8 not null, changed_by_id int8 not null, object_id int8 not null, description varchar(255) not null, object_class_name varchar(255) not null, change_date timestamp not null, primary key (id));

create table custom_field (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, custom_field_definition_id int8 not null, created_by_id int8, edited_date timestamp, primary key (id));

create table custom_field_definition (id int8 not null, version int8 not null, created_date timestamp, tooltip varchar(50), created_by_id int8, edited_date timestamp, edited_by_id int8, label varchar(50) not null, description varchar(250), is_required bool not null, name varchar(50) not null, style_type varchar(255) not null, primary key (id));

create table custom_field_definition_types (cf_definition_types_id int8, types_id int8, types_idx int4);

create table default_images (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, image_id int8 not null, created_by_id int8, defined_default_type varchar(255) not null, edited_date timestamp, primary key (id));

create table drop_down_cf (id int8 not null, value_id int8, primary key (id));

create table drop_down_cfd (id int8 not null, primary key (id));

create table ext_profile (id int8 not null, external_view_url varchar(2083), system_uri varchar(255), external_id varchar(255), external_edit_url varchar(2083), primary key (id), unique (system_uri, external_id));

create table ext_service_item (id int8 not null, external_view_url varchar(2083), system_uri varchar(255), external_id varchar(255), external_edit_url varchar(2083), primary key (id), unique (system_uri, external_id));

create table field_value (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, custom_field_definition_id int8 not null, is_enabled int4 not null, display_text varchar(255) not null, created_by_id int8, edited_date timestamp, field_values_idx int4, primary key (id));

create table images (id int8 not null, version int8 not null, created_date timestamp, is_default bool not null, type varchar(255) not null, created_by_id int8, content_type varchar(255), bytes bytea not null, edited_date timestamp, edited_by_id int8, image_size float8, primary key (id));

create table item_comment (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, service_item_id int8 not null, text varchar(250) not null, created_by_id int8, author_id int8 not null, edited_date timestamp, primary key (id));

create table modify_relationship_activity (id int8 not null, primary key (id));

create table owf_properties (id int8 not null, version int8 not null, visible_in_launch bool not null, edited_by_id int8, created_date timestamp, singleton bool not null, created_by_id int8, edited_date timestamp, primary key (id));

create table profile (id int8 not null, version int8 not null, created_date timestamp not null, created_by_id int8, bio varchar(1000), edited_date timestamp, edited_by_id int8, username varchar(250) not null unique, email varchar(250), avatar_id int8, display_name varchar(250), primary key (id));

create table rating (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, service_item_id int8 not null, created_by_id int8, author_id int8 not null, rate float4 not null, edited_date timestamp, primary key (id));

create table rejection_activity (id int8 not null, rejection_listing_id int8, primary key (id));

create table rejection_justification (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, title varchar(50) not null, description varchar(250), created_by_id int8, edited_date timestamp, primary key (id));

create table rejection_listing (id int8 not null, version int8 not null, justification_id int8, edited_by_id int8, created_date timestamp, service_item_id int8 not null, description varchar(2000), created_by_id int8, author_id int8 not null, edited_date timestamp, primary key (id));

create table relationship (id int8 not null, version int8 not null, relationship_type varchar(255) not null, owning_entity_id int8 not null, primary key (id));

create table relationship_activity_log (mod_rel_activity_id int8 not null, service_item_snapshot_id int8, items_idx int4);

create table relationship_service_item (relationship_related_items_id int8, service_item_id int8, related_items_idx int4);

create table service_item (id int8 not null, version int8 not null, created_date timestamp, owf_properties_id int8, avg_rate float4 not null, approval_status varchar(11) not null, title varchar(50) not null, screenshot1url varchar(2083), image_small_url varchar(2083), image_large_url varchar(2083), total_votes int4 not null, launch_url varchar(2083), uuid varchar(255) not null, version_name varchar(50) not null, release_date timestamp not null, organization varchar(40), dependencies varchar(1000), types_id int8 not null, screenshot2url varchar(2083), created_by_id int8, requirements varchar(1000), doc_url varchar(2083), tech_poc varchar(255) not null, edited_date timestamp, edited_by_id int8, total_comments int4 not null, is_hidden int4 not null, description varchar(4000) not null, state_id int8 not null, install_url varchar(2083), last_activity_id int8, author_id int8 not null, primary key (id));

create table service_item_activity (id int8 not null, version int8 not null, created_date timestamp, action varchar(255) not null, service_item_id int8 not null, service_item_version int8, created_by_id int8, edited_date timestamp, edited_by_id int8, activity_date timestamp not null, author_id int8 not null, service_item_activities_idx int4, primary key (id));

create table service_item_category (service_item_categories_id int8 not null, category_id int8, categories_idx int4);

create table service_item_custom_field (service_item_custom_fields_id int8, custom_field_id int8, custom_fields_idx int4);

create table service_item_snapshot (id int8 not null, version int8 not null, service_item_id int8, title varchar(255) not null, primary key (id));

create table si_recommended_layouts (service_item_id int8, recommended_layout varchar(255));

create table state (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, is_published bool not null, title varchar(50) not null, description varchar(250), created_by_id int8, edited_date timestamp, primary key (id));

create table text (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, value varchar(250), created_by_id int8, read_only bool not null, name varchar(50) not null unique, edited_date timestamp, primary key (id));

create table text_cf (id int8 not null, value varchar(255), primary key (id));

create table text_cfd (id int8 not null, primary key (id));

create table types (id int8 not null, version int8 not null, created_date timestamp, title varchar(50) not null, created_by_id int8, role_access varchar(9) not null, edited_date timestamp, edited_by_id int8, has_launch_url bool not null, is_widget bool not null, description varchar(250), image_id int8, ozone_aware bool not null, has_icons bool not null, primary key (id));

create table user_account (id int8 not null, version int8 not null, edited_by_id int8, created_date timestamp, username varchar(250) not null unique, created_by_id int8, last_login timestamp, edited_date timestamp, primary key (id));

alter table U_DOMAIN add constraint FK97BAABEE7666C6D2 foreign key (created_by_id) references profile;

alter table U_DOMAIN add constraint FK97BAABEEE31CB353 foreign key (edited_by_id) references profile;

alter table U_VIEWS add constraint FK376C31E47666C6D2 foreign key (created_by_id) references profile;

alter table U_VIEWS add constraint FK376C31E4E31CB353 foreign key (edited_by_id) references profile;

alter table avatar add constraint FKAC32C1597666C6D2 foreign key (created_by_id) references profile;

alter table avatar add constraint FKAC32C159E31CB353 foreign key (edited_by_id) references profile;

alter table category add constraint FK302BCFE7666C6D2 foreign key (created_by_id) references profile;

alter table category add constraint FK302BCFEE31CB353 foreign key (edited_by_id) references profile;

alter table change_log add constraint FK80F28E35B624A19E foreign key (changed_by_id) references profile;

alter table custom_field add constraint FK2ACD76AC7666C6D2 foreign key (created_by_id) references profile;

alter table custom_field add constraint FK2ACD76ACE31CB353 foreign key (edited_by_id) references profile;

alter table custom_field add constraint FK2ACD76AC6F62C9ED foreign key (custom_field_definition_id) references custom_field_definition;

alter table custom_field_definition add constraint FK150F70C6E31CB353 foreign key (edited_by_id) references profile;

alter table custom_field_definition add constraint FK150F70C67666C6D2 foreign key (created_by_id) references profile;

alter table custom_field_definition_types add constraint FK1A84FFC06928D597 foreign key (types_id) references types;

alter table default_images add constraint FK6F064E367666C6D2 foreign key (created_by_id) references profile;

alter table default_images add constraint FK6F064E36E31CB353 foreign key (edited_by_id) references profile;

alter table default_images add constraint FK6F064E36553AF61A foreign key (image_id) references images;

alter table drop_down_cf add constraint FK13ADE7D0BC98CEE3 foreign key (value_id) references field_value;

alter table field_value add constraint FK29F571EC7666C6D2 foreign key (created_by_id) references profile;

alter table field_value add constraint FK29F571ECE31CB353 foreign key (edited_by_id) references profile;

alter table field_value add constraint FK29F571ECF1F14D3C foreign key (custom_field_definition_id) references drop_down_cfd;

alter table images add constraint FKB95A8278E31CB353 foreign key (edited_by_id) references profile;

alter table images add constraint FKB95A82787666C6D2 foreign key (created_by_id) references profile;

create index itm_cmnt_svc_item_id_idx on item_comment (service_item_id);

create index itm_cmnt_author_id_idx on item_comment (author_id);

alter table item_comment add constraint FKE6D04D337666C6D2 foreign key (created_by_id) references profile;

alter table item_comment add constraint FKE6D04D33E31CB353 foreign key (edited_by_id) references profile;

alter table item_comment add constraint FKE6D04D33C7E5C662 foreign key (service_item_id) references service_item;

alter table item_comment add constraint FKE6D04D335A032135 foreign key (author_id) references profile;

alter table owf_properties add constraint FKE88638947666C6D2 foreign key (created_by_id) references profile;

alter table owf_properties add constraint FKE8863894E31CB353 foreign key (edited_by_id) references profile;

alter table profile add constraint FKED8E89A9E31CB353 foreign key (edited_by_id) references profile;

alter table profile add constraint FKED8E89A97666C6D2 foreign key (created_by_id) references profile;

alter table profile add constraint FKED8E89A961C3343D foreign key (avatar_id) references avatar;

create index rating_svc_item_id_idx on rating (service_item_id);

alter table rating add constraint FKC815B19D7666C6D2 foreign key (created_by_id) references profile;

alter table rating add constraint FKC815B19DE31CB353 foreign key (edited_by_id) references profile;

alter table rating add constraint FKC815B19DC7E5C662 foreign key (service_item_id) references service_item;

alter table rating add constraint FKC815B19D5A032135 foreign key (author_id) references profile;

alter table rejection_activity add constraint FKF35C128582548A4A foreign key (rejection_listing_id) references rejection_listing;

alter table rejection_justification add constraint FK12B0A53C7666C6D2 foreign key (created_by_id) references profile;

alter table rejection_justification add constraint FK12B0A53CE31CB353 foreign key (edited_by_id) references profile;

create index rej_lst_svc_item_id_idx on rejection_listing (service_item_id);

alter table rejection_listing add constraint FK3F2BD44E7666C6D2 foreign key (created_by_id) references profile;

alter table rejection_listing add constraint FK3F2BD44EE31CB353 foreign key (edited_by_id) references profile;

alter table rejection_listing add constraint FK3F2BD44EC7E5C662 foreign key (service_item_id) references service_item;

alter table rejection_listing add constraint FK3F2BD44E5A032135 foreign key (author_id) references profile;

alter table rejection_listing add constraint FK3F2BD44E19CEB614 foreign key (justification_id) references rejection_justification;

alter table relationship add constraint FKF06476389D70DD39 foreign key (owning_entity_id) references service_item;

alter table relationship_activity_log add constraint FK594974BB25A20F9D foreign key (service_item_snapshot_id) references service_item_snapshot;

alter table relationship_service_item add constraint FKDA02504C7E5C662 foreign key (service_item_id) references service_item;

create index svc_item_author_id_idx on service_item (author_id);

alter table service_item add constraint FK1571565D2746B676 foreign key (last_activity_id) references service_item_activity;

alter table service_item add constraint FK1571565D904D6974 foreign key (owf_properties_id) references owf_properties;

alter table service_item add constraint FK1571565DE31CB353 foreign key (edited_by_id) references profile;

alter table service_item add constraint FK1571565D7666C6D2 foreign key (created_by_id) references profile;

alter table service_item add constraint FK1571565D6928D597 foreign key (types_id) references types;

alter table service_item add constraint FK1571565D5A032135 foreign key (author_id) references profile;

alter table service_item add constraint FK1571565DDFEC3E97 foreign key (state_id) references state;

create index svc_item_act_svc_item_id_idx on service_item_activity (service_item_id);

alter table service_item_activity add constraint FK870EA6B1E31CB353 foreign key (edited_by_id) references profile;

alter table service_item_activity add constraint FK870EA6B17666C6D2 foreign key (created_by_id) references profile;

alter table service_item_activity add constraint FK870EA6B1C7E5C662 foreign key (service_item_id) references service_item;

alter table service_item_activity add constraint FK870EA6B15A032135 foreign key (author_id) references profile;

create index svc_item_cat_id_idx on service_item_category (service_item_categories_id);

alter table service_item_category add constraint FKECC570A0DA41995D foreign key (category_id) references category;

create index svc_item_cst_fld_id_idx on service_item_custom_field (service_item_custom_fields_id);

alter table service_item_custom_field add constraint FK46E9894E7B56E054 foreign key (custom_field_id) references custom_field;

alter table service_item_snapshot add constraint FKFABD8966C7E5C662 foreign key (service_item_id) references service_item;

alter table si_recommended_layouts add constraint FK863C793CC7E5C662 foreign key (service_item_id) references service_item;

alter table state add constraint FK68AC4917666C6D2 foreign key (created_by_id) references profile;

alter table state add constraint FK68AC491E31CB353 foreign key (edited_by_id) references profile;

alter table text add constraint FK36452D7666C6D2 foreign key (created_by_id) references profile;

alter table text add constraint FK36452DE31CB353 foreign key (edited_by_id) references profile;

alter table types add constraint FK69B5879E31CB353 foreign key (edited_by_id) references profile;

alter table types add constraint FK69B58797666C6D2 foreign key (created_by_id) references profile;

alter table types add constraint FK69B5879553AF61A foreign key (image_id) references images;

alter table user_account add constraint FK14C321B97666C6D2 foreign key (created_by_id) references profile;

alter table user_account add constraint FK14C321B9E31CB353 foreign key (edited_by_id) references profile;

create sequence hibernate_sequence;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'DDL statements for creating Marketplace 2.3.1 database structure', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_2.3.1.groovy', '2.3.1-1', '2.0.5', '3:6516205a9ad1a1b533980e2f6fec358b', 1);

-- Changeset changelog_2.4.0.groovy::2.4.0-1::marketplace::(Checksum: 3:10daf4a2a9860b04359b57d9b7ca2a82)
-- Create affiliated_marketplace table
CREATE TABLE affiliated_marketplace (id int8 NOT NULL, "version" int8 NOT NULL, active INT NOT NULL, created_by_id int8, created_date TIMESTAMP WITHOUT TIME ZONE, edited_by_id int8, edited_date TIMESTAMP WITHOUT TIME ZONE, icon_id int8, name VARCHAR(50) NOT NULL, server_url VARCHAR(2083) NOT NULL, timeout int8, CONSTRAINT PK_AFFILIATED_MARKETPLACE PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create affiliated_marketplace table', NOW(), 'Create Table', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-1', '2.0.5', '3:10daf4a2a9860b04359b57d9b7ca2a82', 2);

-- Changeset changelog_2.4.0.groovy::2.4.0-2::marketplace::(Checksum: 3:67ad76339bec05c947305e1ce09393bd)
-- Create FK constraints for affiliated_marketplace table
ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C37666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C3E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C3EA25263C FOREIGN KEY (icon_id) REFERENCES images (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create FK constraints for affiliated_marketplace table', NOW(), 'Add Foreign Key Constraint (x3)', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-2', '2.0.5', '3:67ad76339bec05c947305e1ce09393bd', 3);

-- Changeset changelog_2.4.0.groovy::2.4.0-5::marketplace::(Checksum: 3:5c6ddc272ecb10462987351e5984b93d)
-- Add not-null constraints for ext_profile and ext_service_item.
ALTER TABLE ext_profile ALTER COLUMN  system_uri SET NOT NULL;

ALTER TABLE ext_service_item ALTER COLUMN  system_uri SET NOT NULL;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Add not-null constraints for ext_profile and ext_service_item.', NOW(), 'Add Not-Null Constraint (x2)', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-5', '2.0.5', '3:5c6ddc272ecb10462987351e5984b93d', 4);

-- Changeset changelog_2.4.0.groovy::2.4.0-3::marketplace::(Checksum: 3:7a686549fc702788c4411a4fa326c5ec)
-- Drop types.is_widget column
ALTER TABLE types DROP COLUMN is_widget;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop types.is_widget column', NOW(), 'Drop Column', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-3', '2.0.5', '3:7a686549fc702788c4411a4fa326c5ec', 5);

-- Changeset changelog_2.5.0.groovy::2.5.0-1::marketplace::(Checksum: 3:3794da93a0c032eea2ef743e6d5fa015)
ALTER TABLE category ADD uuid VARCHAR(255);

ALTER TABLE state ADD uuid VARCHAR(255);

ALTER TABLE types ADD uuid VARCHAR(255);

CREATE UNIQUE INDEX category_uuid_idx ON category(uuid);

CREATE UNIQUE INDEX state_uuid_idx ON state(uuid);

CREATE UNIQUE INDEX types_uuid_idx ON types(uuid);

ALTER TABLE profile ADD uuid VARCHAR(255);

ALTER TABLE custom_field_definition ADD uuid VARCHAR(255);

CREATE UNIQUE INDEX profile_uuid_idx ON profile(uuid);

CREATE UNIQUE INDEX cfd_uuid_idx ON custom_field_definition(uuid);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column (x3), Create Index (x3), Add Column (x2), Create Index (x2)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-1', '2.0.5', '3:3794da93a0c032eea2ef743e6d5fa015', 6);

-- Changeset changelog_2.5.0.groovy::2.5.0-3::marketplace::(Checksum: 3:7ad852b9ea5b3e51df8b1898837d458e)
ALTER TABLE owf_properties ADD background BOOLEAN NOT NULL DEFAULT FALSE;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-3', '2.0.5', '3:7ad852b9ea5b3e51df8b1898837d458e', 7);

-- Changeset changelog_2.5.0.groovy::2.5.0-4::marketplace::(Checksum: 3:68ec66a50dfd1bc03b163268cbab8556)
CREATE TABLE import_task (id int8 NOT NULL, "version" int8 NOT NULL, created_by_id int8, created_date TIMESTAMP WITH TIME ZONE, cron_exp VARCHAR(255), download_images bool NOT NULL, edited_by_id int8, edited_date TIMESTAMP WITH TIME ZONE, enabled bool NOT NULL, exec_interval int4, extra_url_params VARCHAR(512), interface_config_id int8 NOT NULL, last_run_result_id int8, name VARCHAR(50) NOT NULL, update_type VARCHAR(7) NOT NULL, url VARCHAR(255), keystore_pass VARCHAR(2048), keystore_path VARCHAR(2048), truststore_path VARCHAR(2048), CONSTRAINT "import_taskPK" PRIMARY KEY (id), UNIQUE (name));

CREATE TABLE import_task_result (id int8 NOT NULL, "version" int8 NOT NULL, message VARCHAR(4000) NOT NULL, result bool NOT NULL, run_date TIMESTAMP WITH TIME ZONE NOT NULL, task_id int8 NOT NULL, CONSTRAINT "import_task_rPK" PRIMARY KEY (id));

CREATE TABLE interface_configuration (id int8 NOT NULL, "version" int8 NOT NULL, allow_truncate bool NOT NULL, auto_create_meta_data bool NOT NULL, default_large_icon_url VARCHAR(2048), default_small_icon_url VARCHAR(2048), delta_since_time_param VARCHAR(64), delta_static_parameters VARCHAR(2048), full_static_parameters VARCHAR(2048), loose_match bool NOT NULL, name VARCHAR(256) NOT NULL, query_date_format VARCHAR(32), response_date_format VARCHAR(32), CONSTRAINT "interface_conPK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table (x3)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-4', '2.0.5', '3:68ec66a50dfd1bc03b163268cbab8556', 8);

-- Changeset changelog_2.5.0.groovy::2.5.0-5::marketplace::(Checksum: 3:f6cb3e205f6f2414aeb88e9f687f1791)
ALTER TABLE import_task ADD CONSTRAINT FK578EF9DF7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DFE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DFA31F8712 FOREIGN KEY (interface_config_id) REFERENCES interface_configuration (id);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DF919216CA FOREIGN KEY (last_run_result_id) REFERENCES import_task_result (id);

ALTER TABLE import_task_result ADD CONSTRAINT FK983AC27D11D7F882 FOREIGN KEY (task_id) REFERENCES import_task (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Foreign Key Constraint (x5)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-5', '2.0.5', '3:f6cb3e205f6f2414aeb88e9f687f1791', 9);

-- Changeset changelog_2.5.0.groovy::2.5.0-6::marketplace::(Checksum: 3:0fbf0612d8e3cf537bc44335f28d1654)
ALTER TABLE service_item ALTER COLUMN title TYPE VARCHAR(256);

ALTER TABLE service_item ALTER COLUMN version_name TYPE VARCHAR(256);

ALTER TABLE service_item ALTER COLUMN organization TYPE VARCHAR(256);

ALTER TABLE service_item ALTER COLUMN tech_poc TYPE VARCHAR(256);

ALTER TABLE profile ALTER COLUMN username TYPE VARCHAR(256);

ALTER TABLE profile ALTER COLUMN display_name TYPE VARCHAR(256);

ALTER TABLE profile ALTER COLUMN email TYPE VARCHAR(256);

ALTER TABLE text_cf ALTER COLUMN value TYPE VARCHAR(256);

ALTER TABLE ext_service_item ALTER COLUMN system_uri TYPE VARCHAR(256);

ALTER TABLE ext_service_item ALTER COLUMN external_id TYPE VARCHAR(256);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Modify data type (x10)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-6', '2.0.5', '3:0fbf0612d8e3cf537bc44335f28d1654', 10);

-- Changeset changelog_5.0.groovy::5.0-1::marketplace::(Checksum: 3:fc5a25b1ec26ad35157cbb13b848adc4)
-- Drop types.role_access and u_views table
ALTER TABLE types DROP COLUMN role_access;

DROP TABLE U_VIEWS;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop types.role_access and u_views table', NOW(), 'Drop Column, Drop Table', 'EXECUTED', 'changelog_5.0.groovy', '5.0-1', '2.0.5', '3:fc5a25b1ec26ad35157cbb13b848adc4', 11);

-- Changeset changelog_5.0.groovy::5.0-2::marketplace::(Checksum: 3:2fb80c3d0f617ff79970aa0eee91f374)
-- Add columns for counting votes to service_item
ALTER TABLE service_item ADD total_rate1 int4;

ALTER TABLE service_item ADD total_rate2 int4;

ALTER TABLE service_item ADD total_rate3 int4;

ALTER TABLE service_item ADD total_rate4 int4;

ALTER TABLE service_item ADD total_rate5 int4;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Add columns for counting votes to service_item', NOW(), 'Add Column (x5)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-2', '2.0.5', '3:2fb80c3d0f617ff79970aa0eee91f374', 12);

-- Changeset changelog_5.0.groovy::5.0-3::marketplace::(Checksum: 3:5f6ba95466cbe695beb449f18dadf636)
-- Replace import_task.download_images with interface_configuration.download_images
ALTER TABLE interface_configuration ADD download_images BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE import_task DROP COLUMN download_images;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Replace import_task.download_images with interface_configuration.download_images', NOW(), 'Add Column, Drop Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-3', '2.0.5', '3:5f6ba95466cbe695beb449f18dadf636', 13);

-- Changeset changelog_5.0.groovy::5.0-4::marketplace::(Checksum: 3:92d4c763cba32762da3e19c17b8b0010)
-- Drop not-null constraint for and expand 'text' field in item_comment table
ALTER TABLE item_comment ALTER COLUMN  text DROP NOT NULL;

ALTER TABLE item_comment ALTER COLUMN text TYPE VARCHAR(4000);

ALTER TABLE item_comment ADD rate FLOAT4;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop not-null constraint for and expand ''text'' field in item_comment table', NOW(), 'Drop Not-Null Constraint, Modify data type, Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-4', '2.0.5', '3:92d4c763cba32762da3e19c17b8b0010', 14);

-- Changeset changelog_5.0.groovy::5.0-7::marketplace::(Checksum: 3:2d396f22f301193c7279b5fa21d33d99)
-- Drop RATING table
DROP TABLE rating;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop RATING table', NOW(), 'Drop Table', 'EXECUTED', 'changelog_5.0.groovy', '5.0-7', '2.0.5', '3:2d396f22f301193c7279b5fa21d33d99', 15);

-- Changeset changelog_5.0.groovy::5.0-8::marketplace::(Checksum: 3:0c0b210789450eae93a45f102e7098d8)
-- Add tables supporting text area and image URL custom fields
CREATE TABLE image_url_cf (id int8 NOT NULL, value VARCHAR(2083), CONSTRAINT "image_url_cfPK" PRIMARY KEY (id));

CREATE TABLE image_url_cfd (id int8 NOT NULL, CONSTRAINT "image_url_cfdPK" PRIMARY KEY (id));

CREATE TABLE text_area_cf (id int8 NOT NULL, value VARCHAR(4000), CONSTRAINT "text_area_cfPK" PRIMARY KEY (id));

CREATE TABLE text_area_cfd (id int8 NOT NULL, CONSTRAINT "text_area_cfdPK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Add tables supporting text area and image URL custom fields', NOW(), 'Create Table (x4)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-8', '2.0.5', '3:0c0b210789450eae93a45f102e7098d8', 16);

-- Changeset changelog_5.0.groovy::5.0-9::marketplace::(Checksum: 3:00acd9da1886cf4eecd780c55e81b386)
-- Add tables supporting check box custom fields
CREATE TABLE check_box_cf (id int8 NOT NULL, value bool, CONSTRAINT "check_box_cfPK" PRIMARY KEY (id));

CREATE TABLE check_box_cfd (id int8 NOT NULL, selected_by_default bool, CONSTRAINT "check_box_cfdPK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Add tables supporting check box custom fields', NOW(), 'Create Table (x2)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-9', '2.0.5', '3:00acd9da1886cf4eecd780c55e81b386', 17);

-- Changeset changelog_5.0.groovy::5.0-10::marketplace::(Checksum: 3:3161bd12012792d0da7ab51979cb4f9b)
-- Add column to custom filed defintion table to store the section where to display that field
ALTER TABLE custom_field_definition ADD section VARCHAR(255);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Add column to custom filed defintion table to store the section where to display that field', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-10', '2.0.5', '3:3161bd12012792d0da7ab51979cb4f9b', 18);

-- Changeset changelog_5.0.groovy::5.0-11::marketplace::(Checksum: 3:701f5fdf006444dbf8e28f0b791c999a)
ALTER TABLE custom_field_definition ADD all_types BOOLEAN NOT NULL DEFAULT FALSE;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-11', '2.0.5', '3:701f5fdf006444dbf8e28f0b791c999a', 19);

-- Changeset changelog_5.0.groovy::5.0-12::marketplace::(Checksum: 3:26dda763dbaaceae684086d0a61d0815)
-- Add column to service item table to store the approved date
ALTER TABLE service_item ADD approved_date TIMESTAMP WITH TIME ZONE;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Add column to service item table to store the approved date', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-12', '2.0.5', '3:26dda763dbaaceae684086d0a61d0815', 20);

-- Changeset changelog_5.0.groovy::5.0-14::marketplace::(Checksum: 3:3091b73b99472559cae278d8c2c3858b)
-- Create indexes for change_detail and change_log tables to speed up lookups
CREATE INDEX change_detail_object_idx ON change_detail(object_class_name, object_id, object_version);

CREATE INDEX change_log_object_idx ON change_log(object_class_name, object_id, object_version);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create indexes for change_detail and change_log tables to speed up lookups', NOW(), 'Create Index (x2)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-14', '2.0.5', '3:3091b73b99472559cae278d8c2c3858b', 21);

-- Changeset changelog_5.0.groovy::5.0-15::marketplace::(Checksum: 3:07d03d98d92fb50e61d9c52df992d6cd)
-- Create indexes for custom_field_definition_types and field_value tables to speed up lookups
CREATE INDEX cfd_types_cfd_idx ON custom_field_definition_types(cf_definition_types_id);

CREATE INDEX cfd_types_types_idx ON custom_field_definition_types(types_id);

CREATE INDEX field_value_cfd_idx ON field_value(custom_field_definition_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create indexes for custom_field_definition_types and field_value tables to speed up lookups', NOW(), 'Create Index (x3)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-15', '2.0.5', '3:07d03d98d92fb50e61d9c52df992d6cd', 22);

-- Changeset changelog_5.0.groovy::5.0-16::marketplace::(Checksum: 3:4cfe9e1b266fa8f81917b9c2f8255e6d)
-- Create indexes for tables to speed up lookups
CREATE INDEX si_snapshot_id_idx ON service_item_snapshot(service_item_id);

CREATE INDEX cf_cfd_idx ON custom_field(custom_field_definition_id);

CREATE INDEX si_rec_layouts_idx ON si_recommended_layouts(service_item_id);

CREATE INDEX rejection_act_listing_id_idx ON rejection_activity(rejection_listing_id);

CREATE INDEX rejection_listing_just_id_idx ON rejection_listing(justification_id);

CREATE INDEX relationship_owing_id_idx ON relationship(owning_entity_id);

CREATE INDEX rel_act_log_mod_rel_act_idx ON relationship_activity_log(mod_rel_activity_id);

CREATE INDEX rel_act_log_mod_si_snpsht_idx ON relationship_activity_log(service_item_snapshot_id);

CREATE INDEX rel_si_rel_items_id_idx ON relationship_service_item(relationship_related_items_id);

CREATE INDEX si_act_si_ver_idx ON service_item_activity(service_item_version);

CREATE INDEX si_cat_cat_id_idx ON service_item_category(category_id);

CREATE INDEX si_cf_cf_id_idx ON service_item_custom_field(custom_field_id);

CREATE INDEX si_owf_props_id_idx ON service_item(owf_properties_id);

CREATE INDEX si_types_id_idx ON service_item(types_id);

CREATE INDEX si_state_id_idx ON service_item(state_id);

CREATE INDEX si_last_activity_idx ON service_item(last_activity_id);

CREATE INDEX si_created_by_id_idx ON service_item(created_by_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create indexes for tables to speed up lookups', NOW(), 'Create Index (x17)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-16', '2.0.5', '3:4cfe9e1b266fa8f81917b9c2f8255e6d', 23);

-- Changeset changelog_5.0.groovy::5.0-18::marketplace::(Checksum: 3:43678308dc3a992c28f53b4efeb30268)
-- Create indexes for rejection_listing table to speed up lookups
CREATE INDEX rej_lst_author_id_idx ON rejection_listing(author_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create indexes for rejection_listing table to speed up lookups', NOW(), 'Create Index', 'EXECUTED', 'changelog_5.0.groovy', '5.0-18', '2.0.5', '3:43678308dc3a992c28f53b4efeb30268', 24);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-1::aml-marketplace::(Checksum: 3:2d1368449dd017f06e542e16fd8b4b1c)
CREATE TABLE score_card (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date TIMESTAMP WITH TIME ZONE, edited_by_id BIGINT, edited_date TIMESTAMP WITH TIME ZONE, score FLOAT(19) NOT NULL, CONSTRAINT "score_cardPK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-1', '2.0.5', '3:2d1368449dd017f06e542e16fd8b4b1c', 25);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-2::aml-marketplace::(Checksum: 3:ffb57b9f6c0d3d879f9aa049cc73e5c2)
CREATE TABLE score_card_item (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date TIMESTAMP WITH TIME ZONE, description VARCHAR(500) NOT NULL, edited_by_id BIGINT, edited_date TIMESTAMP WITH TIME ZONE, is_standard_question BOOLEAN NOT NULL, question VARCHAR(250) NOT NULL, weight FLOAT(19), CONSTRAINT "sc_itemPK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-2', '2.0.5', '3:ffb57b9f6c0d3d879f9aa049cc73e5c2', 26);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-3::aml-marketplace::(Checksum: 3:a2362da0fc0c5c2adcf631b0d5bfb729)
CREATE TABLE score_card_item_response (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date TIMESTAMP WITH TIME ZONE, edited_by_id BIGINT, edited_date TIMESTAMP WITH TIME ZONE, is_satisfied BOOLEAN NOT NULL, score_card_id BIGINT NOT NULL, score_card_item_id BIGINT NOT NULL, CONSTRAINT "sc_responsePK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aml-marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-3', '2.0.5', '3:a2362da0fc0c5c2adcf631b0d5bfb729', 27);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-4::aml-marketplace::(Checksum: 3:a10c921a8ef6370cc64cc06016c7c55c)
ALTER TABLE service_item ADD score_card_id BIGINT;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aml-marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-4', '2.0.5', '3:a10c921a8ef6370cc64cc06016c7c55c', 28);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-5::aml-marketplace::(Checksum: 3:63bdefa2034c53e720100a0e83826dab)
CREATE INDEX FK5E60409D7666C6D2 ON score_card(created_by_id);

CREATE INDEX FK5E60409DE31CB353 ON score_card(edited_by_id);

CREATE INDEX FKE51CCD757666C6D2 ON score_card_item(created_by_id);

CREATE INDEX FKE51CCD75E31CB353 ON score_card_item(edited_by_id);

CREATE INDEX FK80A6CBCB190E00BC ON score_card_item_response(score_card_id);

CREATE INDEX FK80A6CBCB7666C6D2 ON score_card_item_response(created_by_id);

CREATE INDEX FK80A6CBCBE31CB353 ON score_card_item_response(edited_by_id);

CREATE INDEX FK80A6CBCBEF469C97 ON score_card_item_response(score_card_item_id);

CREATE INDEX FK1571565D190E00BC ON service_item(score_card_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aml-marketplace', '', NOW(), 'Create Index (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-5', '2.0.5', '3:63bdefa2034c53e720100a0e83826dab', 29);

-- Changeset changelog_5.0_aml.groovy::aml_5.0-6::aml-marketplace::(Checksum: 3:b4c50686ed2e827d6a5d5de248d5154c)
ALTER TABLE score_card ADD CONSTRAINT FK5E60409D7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE score_card ADD CONSTRAINT FK5E60409DE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE score_card_item ADD CONSTRAINT FKE51CCD757666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE score_card_item ADD CONSTRAINT FKE51CCD75E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE score_card_item_response ADD CONSTRAINT FK80A6CBCB7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE score_card_item_response ADD CONSTRAINT FK80A6CBCBE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE score_card_item_response ADD CONSTRAINT FK80A6CBCB190E00BC FOREIGN KEY (score_card_id) REFERENCES score_card (id);

ALTER TABLE score_card_item_response ADD CONSTRAINT FK80A6CBCBEF469C97 FOREIGN KEY (score_card_item_id) REFERENCES score_card_item (id);

ALTER TABLE service_item ADD CONSTRAINT FK1571565D190E00BC FOREIGN KEY (score_card_id) REFERENCES score_card (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('aml-marketplace', '', NOW(), 'Add Foreign Key Constraint (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-6', '2.0.5', '3:b4c50686ed2e827d6a5d5de248d5154c', 30);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE service_item ADD agency VARCHAR(255);

ALTER TABLE service_item ADD agency_icon VARCHAR(255);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.5', '3:a1d04babbf1c20d5360e96e65afd2bae', 31);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:d80d797f3747f702b21f3e6a6ecb6039)
ALTER TABLE service_item ADD is_outside bool;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.5', '3:d80d797f3747f702b21f3e6a6ecb6039', 32);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-4::franchise-store::(Checksum: 3:f5acf9debc261fa56e5b02edd179ae1a)
CREATE TABLE drop_down_cf_field_value (drop_down_cf_field_value_id int8, field_value_id int8, field_value_list_idx int4);

ALTER TABLE custom_field_definition ADD is_permanent bool;

ALTER TABLE drop_down_cfd ADD is_multi_select bool;

CREATE INDEX FK2627FFDDA5BD888 ON drop_down_cf_field_value(field_value_id);

ALTER TABLE drop_down_cf_field_value ADD CONSTRAINT FK2627FFDDA5BD888 FOREIGN KEY (field_value_id) REFERENCES field_value (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.5', '3:f5acf9debc261fa56e5b02edd179ae1a', 33);

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-6::franchise-store::(Checksum: 3:467f95d21e9bd4003fc7ec4f3eb82f5d)
ALTER TABLE ext_service_item ALTER COLUMN  system_uri DROP NOT NULL;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Drop Not-Null Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-6', '2.0.5', '3:467f95d21e9bd4003fc7ec4f3eb82f5d', 34);

-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-1::franchise-store::(Checksum: 3:a3a3eaab836585ac44bc0d4b7828c381)
CREATE TABLE application_configuration (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, code VARCHAR(250) NOT NULL, value VARCHAR(2000), title VARCHAR(250) NOT NULL, description VARCHAR(2000), type VARCHAR(250) NOT NULL, group_name VARCHAR(250) NOT NULL, sub_group_name VARCHAR(250), mutable BOOLEAN NOT NULL, sub_group_order BIGINT, CONSTRAINT "application_configurationPK" PRIMARY KEY (id), UNIQUE (code));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-1', '2.0.5', '3:a3a3eaab836585ac44bc0d4b7828c381', 35);

-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-2::franchise-store::(Checksum: 3:8c76d18c40c82ecf837da46af444846c)
-- Create index for application_configuration.group_name
CREATE INDEX FKFC9C0477666C6D2 ON application_configuration(created_by_id);

CREATE INDEX FKFC9C047E31CB353 ON application_configuration(edited_by_id);

CREATE INDEX app_config_group_name_idx ON application_configuration(group_name);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C0477666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C047E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', 'Create index for application_configuration.group_name', NOW(), 'Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-2', '2.0.5', '3:8c76d18c40c82ecf837da46af444846c', 36);

-- Changeset changelog_7.1.groovy::7.1-1::marketplace::(Checksum: 3:70d3313cedb44a2430d183b912b1cfe8)
ALTER TABLE owf_properties ADD height BIGINT;

ALTER TABLE owf_properties ADD width BIGINT;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column (x2)', 'EXECUTED', 'changelog_7.1.groovy', '7.1-1', '2.0.5', '3:70d3313cedb44a2430d183b912b1cfe8', 37);

-- Changeset changelog_7.1.groovy::7.1-2::marketplace::(Checksum: 3:5e5d0f3fee742d246977b284fdd76d73)
ALTER TABLE application_configuration ADD help VARCHAR(2000);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.1.groovy', '7.1-2', '2.0.5', '3:5e5d0f3fee742d246977b284fdd76d73', 38);

-- Changeset changelog_7.2.groovy::7.2-1::marketplace::(Checksum: 3:dde59f923016d8ca19285ad540641e05)
CREATE TABLE intent_action (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, description VARCHAR(256), edited_by_id BIGINT, edited_date DATE, title VARCHAR(256) NOT NULL, uuid VARCHAR(255), CONSTRAINT "intent_actionPK" PRIMARY KEY (id));

CREATE INDEX FKEBCDD397666C6D2 ON intent_action(created_by_id);

CREATE INDEX FKEBCDD39E31CB353 ON intent_action(edited_by_id);

CREATE UNIQUE INDEX uuid_unique_1366321689429 ON intent_action(uuid);

ALTER TABLE intent_action ADD CONSTRAINT FKEBCDD397666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent_action ADD CONSTRAINT FKEBCDD39E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-1', '2.0.5', '3:dde59f923016d8ca19285ad540641e05', 39);

-- Changeset changelog_7.2.groovy::7.2-2::marketplace::(Checksum: 3:e258eba3bd98eab166a83139cc155884)
CREATE TABLE intent_direction (created_by_id BIGINT, created_date DATE, description VARCHAR(250), edited_by_id BIGINT, edited_date DATE, id bigserial NOT NULL, title VARCHAR(7) NOT NULL, uuid VARCHAR(255), "version" BIGINT NOT NULL, CONSTRAINT "intent_directPK" PRIMARY KEY (id));

CREATE INDEX FKC723A59C7666C6D2 ON intent_direction(created_by_id);

CREATE INDEX FKC723A59CE31CB353 ON intent_direction(edited_by_id);

CREATE UNIQUE INDEX title_unique_1366386256451 ON intent_direction(title);

CREATE UNIQUE INDEX uuid_unique_1366386256451 ON intent_direction(uuid);

ALTER TABLE intent_direction ADD CONSTRAINT FKC723A59C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent_direction ADD CONSTRAINT FKC723A59CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x4), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-2', '2.0.5', '3:e258eba3bd98eab166a83139cc155884', 40);

-- Changeset changelog_7.2.groovy::7.2-3::marketplace::(Checksum: 3:bc12e4bff0bfb59fa7e73f8c7540711f)
CREATE TABLE intent_data_type (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, description VARCHAR(256), edited_by_id BIGINT, edited_date DATE, title VARCHAR(256) NOT NULL, uuid VARCHAR(255), CONSTRAINT "intent_data_tPK" PRIMARY KEY (id));

CREATE INDEX FKEADB30CC7666C6D2 ON intent_data_type(created_by_id);

CREATE INDEX FKEADB30CCE31CB353 ON intent_data_type(edited_by_id);

CREATE UNIQUE INDEX uuid_unique_1366398847848 ON intent_data_type(uuid);

ALTER TABLE intent_data_type ADD CONSTRAINT FKEADB30CC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent_data_type ADD CONSTRAINT FKEADB30CCE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-3', '2.0.5', '3:bc12e4bff0bfb59fa7e73f8c7540711f', 41);

-- Changeset changelog_7.2.groovy::7.2-4::marketplace::(Checksum: 3:09a1dc754584d1cd1efe6b077afe39ae)
CREATE TABLE owf_widget_types (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, description VARCHAR(255) NOT NULL, edited_by_id BIGINT, edited_date DATE, title VARCHAR(256) NOT NULL, uuid VARCHAR(255), CONSTRAINT "owf_widget_typePK" PRIMARY KEY (id));

ALTER TABLE owf_properties ADD owf_widget_type VARCHAR(255) NOT NULL DEFAULT 'standard';

CREATE INDEX FK6AB6A9DF7666C6D2 ON owf_widget_types(created_by_id);

CREATE INDEX FK6AB6A9DFE31CB353 ON owf_widget_types(edited_by_id);

CREATE UNIQUE INDEX uuid_unique_1366666109930 ON owf_widget_types(uuid);

ALTER TABLE owf_widget_types ADD CONSTRAINT FK6AB6A9DF7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE owf_widget_types ADD CONSTRAINT FK6AB6A9DFE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Add Column, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-4', '2.0.5', '3:09a1dc754584d1cd1efe6b077afe39ae', 42);

-- Changeset changelog_7.2.groovy::7.2-5::marketplace::(Checksum: 3:21571b54501c40307f4bd432fba456f1)
ALTER TABLE owf_properties ADD universal_name VARCHAR(255);

ALTER TABLE owf_properties ADD stack_context VARCHAR(200);

ALTER TABLE owf_properties ADD stack_descriptor TEXT;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column (x3)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-5', '2.0.5', '3:21571b54501c40307f4bd432fba456f1', 43);

-- Changeset changelog_7.3.0.groovy::7.3.0-1::marketplace::(Checksum: 3:ef88eb1178b48b3590a7291563871a30)
ALTER TABLE score_card_item ADD image VARCHAR(255);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-1', '2.0.5', '3:ef88eb1178b48b3590a7291563871a30', 44);

-- Changeset changelog_7.3.0.groovy::7.3.0-2::marketplace::(Checksum: 3:cb41b1179b6a373e0861c7b29400d814)
ALTER TABLE service_item_activity ADD details VARCHAR(255);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-2', '2.0.5', '3:cb41b1179b6a373e0861c7b29400d814', 45);

-- Changeset changelog_7.3.0.groovy::7.3.0-3::marketplace::(Checksum: 3:ab1c2a78ad360b01b91b1a2232ae3c87)
ALTER TABLE owf_properties ADD descriptor_url VARCHAR(2083);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-3', '2.0.5', '3:ab1c2a78ad360b01b91b1a2232ae3c87', 46);

-- Changeset changelog_7.3.0.groovy::7.3.0-4::marketplace::(Checksum: 3:8dad893690319284db723406dfc00350)
ALTER TABLE service_item ALTER COLUMN  description DROP NOT NULL;

ALTER TABLE service_item ALTER COLUMN  release_date DROP NOT NULL;

ALTER TABLE service_item ALTER COLUMN  state_id DROP NOT NULL;

ALTER TABLE service_item ALTER COLUMN  tech_poc DROP NOT NULL;

ALTER TABLE service_item ALTER COLUMN  version_name DROP NOT NULL;

ALTER TABLE service_item_category ALTER COLUMN  service_item_categories_id DROP NOT NULL;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Drop Not-Null Constraint (x6)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-4', '2.0.5', '3:8dad893690319284db723406dfc00350', 47);

-- Changeset changelog_7.3.0.groovy::7.3.0-5::marketplace::(Checksum: 3:28e58147bcb07fab3ae955b45d9352e3)
CREATE TABLE intent (id bigserial NOT NULL, "version" BIGINT NOT NULL, action_id BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, data_type_id BIGINT NOT NULL, edited_by_id BIGINT, edited_date DATE, receive BOOLEAN NOT NULL, send BOOLEAN NOT NULL, CONSTRAINT "intentPK" PRIMARY KEY (id));

CREATE TABLE owf_properties_intent (owf_properties_intents_id BIGINT, intent_id BIGINT);

CREATE INDEX FKB971369CD8544299 ON intent(action_id);

CREATE INDEX FKB971369C7666C6D2 ON intent(created_by_id);

CREATE INDEX FKB971369C283F938E ON intent(data_type_id);

CREATE INDEX FKB971369CE31CB353 ON intent(edited_by_id);

CREATE INDEX FK3F99ECA7A651895D ON owf_properties_intent(intent_id);

CREATE INDEX "owfProps_intent_id_idx" ON owf_properties_intent(owf_properties_intents_id);

ALTER TABLE intent ADD CONSTRAINT FKB971369CD8544299 FOREIGN KEY (action_id) REFERENCES intent_action (id);

ALTER TABLE intent ADD CONSTRAINT FKB971369C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent ADD CONSTRAINT FKB971369C283F938E FOREIGN KEY (data_type_id) REFERENCES intent_data_type (id);

ALTER TABLE intent ADD CONSTRAINT FKB971369CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE owf_properties_intent ADD CONSTRAINT FK3F99ECA7A651895D FOREIGN KEY (intent_id) REFERENCES intent (id);

ALTER TABLE owf_properties_intent ADD CONSTRAINT FK3F99ECA74704E25C FOREIGN KEY (owf_properties_intents_id) REFERENCES owf_properties (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table (x2), Create Index (x6), Add Foreign Key Constraint (x6)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-5', '2.0.5', '3:28e58147bcb07fab3ae955b45d9352e3', 48);

-- Changeset changelog_7.3.0.groovy::7.3.0-6::marketplace::(Checksum: 3:188326c86a75bf88826fe0addaaed0bb)
ALTER TABLE types ADD is_permanent BOOLEAN;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-6', '2.0.5', '3:188326c86a75bf88826fe0addaaed0bb', 49);

-- Changeset changelog_7.3.0.groovy::7.3.0-8::marketplace::(Checksum: 3:0e4b85a436faf5855fe99ffe9238c780)
ALTER TABLE score_card_item ADD show_on_listing BOOLEAN;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-8', '2.0.5', '3:0e4b85a436faf5855fe99ffe9238c780', 50);

-- Changeset changelog_7.3.0.groovy::7.3.0-12::marketplace::(Checksum: 3:81da42021cf40d5046b0d45387730d23)
-- Create new mapping table for service item's 'owners' field replacing 'author' property.
CREATE TABLE service_item_profile (service_item_owners_id BIGINT, profile_id BIGINT, owners_idx INT);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create new mapping table for service item''s ''owners'' field replacing ''author'' property.', NOW(), 'Create Table', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-12', '2.0.5', '3:81da42021cf40d5046b0d45387730d23', 51);

-- Changeset changelog_7.3.0.groovy::7.3.0-14::marketplace::(Checksum: 3:1c60475cd9c9c32e19e220bf19ada60f)
-- Drop 'author_id' column corresponding to the 'author' property of service item being removed.
ALTER TABLE service_item DROP CONSTRAINT FK1571565D5A032135;

ALTER TABLE service_item DROP COLUMN author_id;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop ''author_id'' column corresponding to the ''author'' property of service item being removed.', NOW(), 'Drop Foreign Key Constraint, Drop Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-14', '2.0.5', '3:1c60475cd9c9c32e19e220bf19ada60f', 52);

-- Changeset changelog_7.3.0.groovy::7.3.0-15::marketplace::(Checksum: 3:8a4b6a9ffea16feb446d6b2fb4429825)
CREATE TABLE service_item_documentation_url (id bigserial NOT NULL, "version" BIGINT DEFAULT 0 NOT NULL, name VARCHAR(256) NOT NULL, service_item_id BIGINT NOT NULL, url VARCHAR(2083) NOT NULL, CONSTRAINT "service_item_PK" PRIMARY KEY (id));

CREATE TABLE service_item_tech_pocs (service_item_id BIGINT, tech_poc VARCHAR(256));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-15', '2.0.5', '3:8a4b6a9ffea16feb446d6b2fb4429825', 53);

-- Changeset changelog_7.3.0.groovy::7.3.0-16::marketplace::(Checksum: 3:29b11454e2ee263c40d977090718a9ca)
ALTER TABLE service_item_documentation_url ADD CONSTRAINT FK24572D08C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE service_item_tech_pocs ADD CONSTRAINT FKA55CFB56C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-16', '2.0.5', '3:29b11454e2ee263c40d977090718a9ca', 54);

-- Changeset changelog_7.3.0.groovy::7.3.0-19::marketplace::(Checksum: 3:b6082ec33d0f9cd5358039b2cb06e19f)
ALTER TABLE service_item DROP COLUMN doc_url;

ALTER TABLE service_item DROP COLUMN tech_poc;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Drop Column (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-19', '2.0.5', '3:b6082ec33d0f9cd5358039b2cb06e19f', 55);

-- Changeset changelog_7.3.0.groovy::7.3.0-21::franchise-store::(Checksum: 3:8783021a7f5d1842434f5a0958fafb06)
-- Drop no-longer needed indices. Postgress-specific to compensate for the Liquibase bug
DROP INDEX IF EXISTS  change_detail_object_idx;

DROP INDEX IF EXISTS  change_log_object_idx;

DROP INDEX IF EXISTS  cfd_types_cfd_idx;

DROP INDEX IF EXISTS  rel_act_log_mod_rel_act_idx;

DROP INDEX IF EXISTS  rel_si_rel_items_id_idx;

DROP INDEX IF EXISTS  si_act_si_ver_idx;

DROP INDEX IF EXISTS svc_item_author_id_idx;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('franchise-store', 'Drop no-longer needed indices. Postgress-specific to compensate for the Liquibase bug', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-21', '2.0.5', '3:8783021a7f5d1842434f5a0958fafb06', 56);

-- Changeset changelog_7.5.0.groovy::7.5.0-1::marketplace::(Checksum: 3:e609efe728480f5fa22e324cb8fa364c)
CREATE TABLE agency (id bigserial NOT NULL, title VARCHAR(255), icon_url VARCHAR(2083) NOT NULL, created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, "version" BIGINT DEFAULT 0 NOT NULL, CONSTRAINT "agencyPK" PRIMARY KEY (id), UNIQUE (title));

ALTER TABLE service_item ADD agency_id BIGINT;

ALTER TABLE service_item ADD CONSTRAINT SERVICE_ITEM_AGENCY_FK FOREIGN KEY (agency_id) REFERENCES agency (id) ON DELETE SET NULL;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Add Column, Add Foreign Key Constraint', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-1', '2.0.5', '3:e609efe728480f5fa22e324cb8fa364c', 57);

-- Changeset changelog_7.5.0.groovy::7.5.0-3::marketplace::(Checksum: 3:978dc473dd30d3beb49553a281692079)
ALTER TABLE service_item DROP COLUMN agency;

ALTER TABLE service_item DROP COLUMN agency_icon;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Drop Column (x2)', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-3', '2.0.5', '3:978dc473dd30d3beb49553a281692079', 58);

-- Changeset changelog_7.6.0.groovy::7.6.0-1::marketplace::(Checksum: 3:8652867eb3c624d0e73892ba40f72549)
CREATE TABLE screenshot (id bigserial NOT NULL, small_image_url VARCHAR(2083) NOT NULL, large_image_url VARCHAR(2083), ordinal INT, service_item_id BIGINT, created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, "version" BIGINT DEFAULT 0 NOT NULL, CONSTRAINT "screenshotPK" PRIMARY KEY (id));

ALTER TABLE screenshot ADD CONSTRAINT SCREENSHOT_SERVICE_ITEM_FK FOREIGN KEY (service_item_id) REFERENCES service_item (id) ON DELETE CASCADE;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Add Foreign Key Constraint', 'EXECUTED', 'changelog_7.6.0.groovy', '7.6.0-1', '2.0.5', '3:8652867eb3c624d0e73892ba40f72549', 59);

-- Changeset changelog_7.6.0.groovy::7.6.0-2::marketplace::(Checksum: 3:ed5b1d1912b7769b4449c3533a5e4a86)
INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot1url, 0, id
                FROM service_item
                WHERE screenshot1url IS NOT NULL AND screenshot1url <> '';

INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot2url, 1, id
                FROM service_item
                WHERE screenshot2url IS NOT NULL AND screenshot2url <> '';

ALTER TABLE service_item DROP COLUMN screenshot1url;

ALTER TABLE service_item DROP COLUMN screenshot2url;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Custom SQL (x2), Drop Column (x2)', 'EXECUTED', 'changelog_7.6.0.groovy', '7.6.0-2', '2.0.5', '3:ed5b1d1912b7769b4449c3533a5e4a86', 60);

-- Changeset changelog_7.9.0.groovy::7.9.0-2::marketplace::(Checksum: 3:47c89f94ae81a109928fbef3287e0e05)
CREATE TABLE contact_type (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, required BOOLEAN NOT NULL, title VARCHAR(50) NOT NULL, CONSTRAINT "contact_typePK" PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-2', '2.0.5', '3:47c89f94ae81a109928fbef3287e0e05', 61);

-- Changeset changelog_7.9.0.groovy::7.9.0-3::marketplace::(Checksum: 3:84a1cac2f49e8a997bb5cadbe98281d1)
CREATE TABLE contact (id bigserial NOT NULL, "version" BIGINT NOT NULL, email VARCHAR(100) NOT NULL, name VARCHAR(100) NOT NULL, organization VARCHAR(100), secure_phone VARCHAR(50), type_id BIGINT NOT NULL, service_item_id BIGINT NOT NULL, unsecure_phone VARCHAR(50), created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, CONSTRAINT "contactPK" PRIMARY KEY (id));

CREATE INDEX FK4C2BB7F97666C6D2 ON contact_type(created_by_id);

CREATE INDEX FK4C2BB7F9E31CB353 ON contact_type(edited_by_id);

CREATE INDEX FK38B72420C7E5C662 ON contact(service_item_id);

CREATE INDEX FK38B72420BA3FC877 ON contact(type_id);

CREATE INDEX FK38B72420E31CB353 ON contact(edited_by_id);

CREATE INDEX FK38B724207666C6D2 ON contact(created_by_id);

ALTER TABLE contact_type ADD CONSTRAINT FK4C2BB7F97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE contact_type ADD CONSTRAINT FK4C2BB7F9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE contact ADD CONSTRAINT FK38B724207666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE contact ADD CONSTRAINT FK38B72420E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE contact ADD CONSTRAINT FK38B72420BA3FC877 FOREIGN KEY (type_id) REFERENCES contact_type (id);

ALTER TABLE contact ADD CONSTRAINT FK38B72420C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index (x6), Add Foreign Key Constraint (x6)', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-3', '2.0.5', '3:84a1cac2f49e8a997bb5cadbe98281d1', 62);

-- Changeset changelog_7.9.0.groovy::7.9.0-4::marketplace::(Checksum: 3:d76e0aa887053b1f64792f3bfd91cbe2)
CREATE UNIQUE INDEX title_unique_1389723125532 ON contact_type(title);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Index', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-4', '2.0.5', '3:d76e0aa887053b1f64792f3bfd91cbe2', 63);

-- Changeset changelog_7.10.0.groovy::7.10.0-1::marketplace::(Checksum: 3:c36101b773b019ee4625b75411cb8eb6)
ALTER TABLE service_item ADD opens_in_new_browser_tab BOOLEAN;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-1', '2.0.5', '3:c36101b773b019ee4625b75411cb8eb6', 64);

-- Changeset changelog_7.10.0.groovy::7.10.0-2::marketplace::(Checksum: 3:beafab57293fa84b39d3e25be9531b43)
ALTER TABLE profile ADD user_roles VARCHAR(255);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-2', '2.0.5', '3:beafab57293fa84b39d3e25be9531b43', 65);

-- Changeset changelog_7.11.0.groovy::7.11.0-2::marketplace::(Checksum: 3:46b29bbb4574aec82193638d9c1a08d4)
ALTER TABLE change_detail ADD service_item_activity_id BIGINT NOT NULL;

CREATE INDEX FKB4467BC0855307BD ON change_detail(service_item_activity_id);

ALTER TABLE change_detail ADD CONSTRAINT FKB4467BC0855307BD FOREIGN KEY (service_item_activity_id) REFERENCES service_item_activity (id);

ALTER TABLE service_item_activity DROP COLUMN service_item_version;

ALTER TABLE service_item_activity DROP COLUMN details;

ALTER TABLE change_detail DROP COLUMN object_class_name;

ALTER TABLE change_detail DROP COLUMN object_id;

ALTER TABLE change_detail DROP COLUMN object_version;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column, Create Index, Add Foreign Key Constraint, Drop Column (x5)', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-2', '2.0.5', '3:46b29bbb4574aec82193638d9c1a08d4', 66);

-- Changeset changelog_7.12.0.groovy::7.12.0-1::marketplace::(Checksum: 3:932edbdb31fd51477e9580ee37bc8fba)
DROP TABLE change_log;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Drop Table', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-1', '2.0.5', '3:932edbdb31fd51477e9580ee37bc8fba', 67);

-- Changeset changelog_7.12.0.groovy::7.12.0-2::marketplace::(Checksum: 3:1c807d392fd79380b2e030a451e6ea0b)
CREATE TABLE tag (id bigserial NOT NULL, "version" BIGINT NOT NULL, created_by_id BIGINT, created_date DATE, edited_by_id BIGINT, edited_date DATE, title VARCHAR(16) NOT NULL, CONSTRAINT "tag_PK" PRIMARY KEY (id));

CREATE INDEX tag_title_idx ON tag(title);

CREATE TABLE service_item_tag (id bigserial NOT NULL, service_item_id BIGINT NOT NULL, tag_id BIGINT NOT NULL, created_by_id BIGINT, "version" BIGINT NOT NULL, CONSTRAINT "service_item_tag_PK" PRIMARY KEY (id));

CREATE INDEX service_item_tag_si_idx ON service_item_tag(service_item_id);

CREATE INDEX service_item_tag_tag_idx ON service_item_tag(tag_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Create Index, Create Table, Create Index (x2)', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-2', '2.0.5', '3:1c807d392fd79380b2e030a451e6ea0b', 68);

-- Changeset changelog_7.12.0.groovy::7.12.0-3::marketplace::(Checksum: 3:81c00e342acc2d70a14bee90e6b5de57)
ALTER TABLE service_item_tag ADD CONSTRAINT "service_item_tag_FK_si" FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE service_item_tag ADD CONSTRAINT "service_item_tag_FK_tag" FOREIGN KEY (tag_id) REFERENCES tag (id);

ALTER TABLE service_item_tag ADD CONSTRAINT "service_item_tag_FK_cb" FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_unique_idx UNIQUE (service_item_id, tag_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Foreign Key Constraint (x3), Add Unique Constraint', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-3', '2.0.5', '3:81c00e342acc2d70a14bee90e6b5de57', 69);

-- Changeset changelog_7.16.0.groovy::7.16.0-2::marketplace::(Checksum: 3:9c7fbae6fdea582b51eeed650647e080)
ALTER TABLE service_item ADD image_medium_url VARCHAR(2083);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-2', '2.0.5', '3:9c7fbae6fdea582b51eeed650647e080', 70);

-- Changeset changelog_7.16.0.groovy::7.16.0-5::marketplace::(Checksum: 3:254414ea4972c81264cc952d9c24e279)
CREATE TABLE service_item_score_card_item (service_item_id BIGINT, score_card_item_id BIGINT);

ALTER TABLE service_item_score_card_item ADD CONSTRAINT FKBF91F93EF469C97 FOREIGN KEY (score_card_item_id) REFERENCES score_card_item (id) ON DELETE CASCADE;

ALTER TABLE service_item_score_card_item ADD CONSTRAINT FKBF91F939C51FA9F FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE score_card DROP CONSTRAINT FK5E60409D7666C6D2;

ALTER TABLE score_card DROP CONSTRAINT FK5E60409DE31CB353;

ALTER TABLE score_card_item_response DROP CONSTRAINT FK80A6CBCB7666C6D2;

ALTER TABLE score_card_item_response DROP CONSTRAINT FK80A6CBCBE31CB353;

ALTER TABLE score_card_item_response DROP CONSTRAINT FK80A6CBCB190E00BC;

ALTER TABLE score_card_item_response DROP CONSTRAINT FK80A6CBCBEF469C97;

ALTER TABLE service_item DROP CONSTRAINT FK1571565D190E00BC;

ALTER TABLE score_card_item DROP COLUMN is_standard_question;

ALTER TABLE score_card_item DROP COLUMN weight;

ALTER TABLE service_item DROP COLUMN score_card_id;

DROP TABLE score_card;

DROP TABLE score_card_item_response;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Create Table, Add Foreign Key Constraint (x2), Drop Foreign Key Constraint (x7), Drop Column (x3), Drop Table (x2)', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-5', '2.0.5', '3:254414ea4972c81264cc952d9c24e279', 71);

-- Changeset app_config.groovy::app_config-2::marketplace::(Checksum: 3:f3084b1a17c66754a2444e37659a69a4)
INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.name', 'BRANDING', TRUE, NULL, 1, ' ', 'String', '', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.logo', 'BRANDING', TRUE, NULL, 2, ' ', 'Image', '/marketplace/themes/gold.theme/images/Mp_logo.png', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.icon', 'BRANDING', TRUE, NULL, 3, ' ', 'Image', '/marketplace/themes/common/images/agency/agencyDefault.png', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('free.warning.content', 'BRANDING', TRUE, NULL, 4, ' ', 'String', '', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('url.public', 'BRANDING', TRUE, NULL, 5, ' ', 'string', '', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('about.box.content', 'BRANDING', TRUE, 'About Information', 1, ' ', 'String', 'The Store allows visitors to discover and explore business and convenience applications and enables user-configurable visualizations of available content.', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('about.box.image', 'BRANDING', TRUE, 'About Information', 2, ' ', 'Image', '/marketplace/themes/gold.theme/images/Mp_logo_128x128.png', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('access.alert.enable', 'BRANDING', TRUE, 'Access Alert Information', 1, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('access.alert.content', 'BRANDING', TRUE, 'Access Alert Information', 2, ' ', 'String', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla interdum eleifend sapien dignissim malesuada. Sed imperdiet augue vitae justo feugiat eget porta est blandit. Proin ipsum ipsum, rutrum ac gravida in, ullamcorper a augue. Sed at scelerisque augue. Morbi scelerisque gravida sapien ut feugiat. Donec dictum, nisl commodo dapibus pellentesque, enim quam consectetur quam, at dictum dui augue at risus. Ut id nunc in justo molestie semper. Curabitur magna velit, varius eu porttitor et, tempor pulvinar nulla. Nam at tellus nec felis tincidunt fringilla. Nunc nisi sem, egestas ut consequat eget, luctus et nisi. Nulla et lorem odio, vitae pretium ipsum. Integer tellus libero, molestie a feugiat a, imperdiet sit amet metus. Aenean auctor fringilla eros, sit amet suscipit felis eleifend a.', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.footer.featured.title', 'BRANDING', TRUE, 'Footer Information', 1, ' ', 'String', 'Store', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.footer.featured.content', 'BRANDING', TRUE, 'Footer Information', 2, ' ', 'String', 'The Store allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content.', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.open.search.title.message', 'BRANDING', TRUE, 'Open Search', 1, ' ', 'String', 'Marketplace Search', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.open.search.description.message', 'BRANDING', TRUE, 'Open Search', 2, ' ', 'String', 'Marketplace Search Description', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.open.search.fav.icon', 'BRANDING', TRUE, 'Open Search', 3, ' ', 'Image', '/marketplace/themes/gold.theme/images/favicon.ico', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.open.search.site.icon', 'BRANDING', TRUE, 'Open Search', 4, ' ', 'Image', '../images/themes/default/market_64x64.png', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.custom.header.url', 'BRANDING', TRUE, 'Custom Header and Footer', 1, ' ', 'String', NULL, 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.custom.header.height', 'BRANDING', TRUE, 'Custom Header and Footer', 2, ' ', 'Integer', '0', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.custom.footer.url', 'BRANDING', TRUE, 'Custom Header and Footer', 3, ' ', 'String', NULL, 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.custom.footer.height', 'BRANDING', TRUE, 'Custom Header and Footer', 4, ' ', 'Integer', '0', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.custom.css', 'BRANDING', TRUE, 'Custom Header and Footer', 5, ' ', 'String', NULL, 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.custom.js', 'BRANDING', TRUE, 'Custom Header and Footer', 6, ' ', 'String', NULL, 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.insideOutside.behavior', 'ADDITIONAL_CONFIGURATION', TRUE, NULL, 1, ' ', 'String', 'ADMIN_SELECTED', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.enable.ext.serviceitem', 'ADDITIONAL_CONFIGURATION', TRUE, NULL, 2, ' ', 'Boolean', 'false', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.allow.owner.to.edit.approved.listing', 'ADDITIONAL_CONFIGURATION', TRUE, NULL, 3, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.amp.search.result.size', 'ADDITIONAL_CONFIGURATION', TRUE, 'Partner Store Search', 1, ' ', 'Integer', '30', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.amp.search.default.timeout', 'ADDITIONAL_CONFIGURATION', TRUE, 'Partner Store Search', 2, ' ', 'Integer', '30000', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.image.allow.upload', 'ADDITIONAL_CONFIGURATION', TRUE, 'Partner Store Image Configurations', 1, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.type.image.max.size', 'ADDITIONAL_CONFIGURATION', TRUE, 'Partner Store Image Configurations', 2, ' ', 'Integer', '1048576', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.amp.image.max.size', 'ADDITIONAL_CONFIGURATION', TRUE, 'Partner Store Image Configurations', 3, ' ', 'Integer', '1048576', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.owf.sync.urls', 'ADDITIONAL_CONFIGURATION', TRUE, 'OWF Sync', 1, ' ', 'List', NULL, 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.contact.email', 'ADDITIONAL_CONFIGURATION', TRUE, 'Store Contact Information', 1, ' ', 'String', NULL, 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.quick.view.detail.fields', 'ADDITIONAL_CONFIGURATION', TRUE, 'Quick View', 1, ' ', 'String', 'types, categories, state, releaseDate, lastActivity, owners, organization, Alternate POC Info, Technical POC Info, Support POC Info, launchUrl', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.enable.scoreCard', 'SCORECARD', TRUE, NULL, 1, ' ', 'Boolean', 'false', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.is.franchise', 'HIDDEN', FALSE, NULL, 1, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.default.theme', 'HIDDEN', TRUE, NULL, 2, ' ', 'String', 'gold', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.job.disable.accounts.interval', 'HIDDEN', TRUE, NULL, 3, ' ', 'Integer', '1440', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.job.disable.accounts.start.time', 'HIDDEN', TRUE, NULL, 4, ' ', 'String', '23:59:59', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.session.control.enabled', 'USER_ACCOUNT_SETTINGS', TRUE, 'Session Control', 1, ' ', 'Boolean', 'false', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.session.control.max.concurrent', 'USER_ACCOUNT_SETTINGS', TRUE, 'Session Control', 2, ' ', 'Integer', '1', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.disable.inactive.accounts', 'USER_ACCOUNT_SETTINGS', TRUE, 'Inactive Accounts', 1, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.inactivity.threshold', 'USER_ACCOUNT_SETTINGS', TRUE, 'Inactive Accounts', 2, ' ', 'Integer', '90', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.enable.cef.logging', 'AUDITING', TRUE, NULL, 1, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.enable.cef.object.access.logging', 'AUDITING', TRUE, NULL, 2, ' ', 'Boolean', 'false', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.enable.cef.log.sweep', 'AUDITING', TRUE, NULL, 3, ' ', 'Boolean', 'true', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.cef.log.location', 'AUDITING', TRUE, NULL, 4, ' ', 'String', '/usr/share/tomcat6', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.cef.sweep.log.location', 'AUDITING', TRUE, NULL, 5, ' ', 'String', '/var/log/cef', 0);

INSERT INTO application_configuration (code, group_name, mutable, sub_group_name, sub_group_order, title, type, value, "version") VALUES ('store.security.level', 'AUDITING', TRUE, NULL, 6, ' ', 'String', NULL, 0);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Insert Row (x47)', 'EXECUTED', 'app_config.groovy', 'app_config-2', '2.0.5', '3:f3084b1a17c66754a2444e37659a69a4', 72);

-- Changeset changelog_7.16.0.groovy::7.16.0-6::marketplace::(Checksum: 3:d2f31c636d691a47e3d2e8b73cbd66b1)
DELETE FROM application_configuration  WHERE code = 'store.domains';

UPDATE application_configuration SET sub_group_name = 'Partner Store Image Configurations' WHERE sub_group_name = 'Image Configurations';

UPDATE application_configuration SET sub_group_name = 'Partner Store Search' WHERE sub_group_name = 'Affiliated Search';

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Delete Data, Update Data (x2)', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-6', '2.0.5', '3:d2f31c636d691a47e3d2e8b73cbd66b1', 73);

-- Changeset changelog_7.16.0.groovy::7.16.0-8::marketplace::(Checksum: 3:54b5f1e34983065c4273b232dece1777)
ALTER TABLE owf_properties ADD mobile_ready BOOLEAN NOT NULL DEFAULT FALSE;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Add Column', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-8', '2.0.5', '3:54b5f1e34983065c4273b232dece1777', 74);

-- Changeset changelog_7.16.0.groovy::7.16.0-9::marketplace::(Checksum: 3:2337d16fb8f4bbd2ea73b9c3a830c68f)
UPDATE application_configuration SET type = 'String' WHERE type = 'string';

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-9', '2.0.5', '3:2337d16fb8f4bbd2ea73b9c3a830c68f', 75);

-- *********************************************************************
-- SQL to add all changesets to database history table
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/21/15 7:40 AM
-- Against: postgres@jdbc:postgresql://localhost:5432/omp
-- Liquibase version: 2.0.5
-- *********************************************************************

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Transfer rating information into the item_comment table', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-5', '2.0.5', '3:3688cbd797cb6cde3827130aa74d1903', 267);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Compute number of votes for each rating for the service items', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-6', '2.0.5', '3:9ed670c788094838125b3045c42f6fce', 268);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Transfer approved date information into the service_item table', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-13', '2.0.5', '3:6efbf9a22068bcab382bc6a466bf40ae', 269);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Set the section for existing cfds to typeProperties', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-17', '2.0.5', '3:ce25f8b65d80c3e960d0883264d5206f', 270);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Update Data (x3)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-7', '2.0.5', '3:b337838d37b361db713c9fcfad1af557', 271);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Update Data (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-9', '2.0.5', '3:8386d1edefea8f79361809de2eea6608', 272);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create a record in service_item_profile table for each service item linking it with its owner.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-13', '2.0.5', '3:3658ca8cb6ad5d02fc45644d4a37a38b', 273);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create a record in service_item_tech_pocs table for each service item linking it with its technical POC.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-17', '2.0.5', '3:ff31dc39fa460d736a824986b06ed8e2', 274);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create a record in service_item_documentation_url table for each service item linking it with its technical POC.', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-18', '2.0.5', '3:7daee389a18729b712930a71f89bb6f9', 275);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Custom SQL (x2)', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-2', '2.0.5', '3:20ee7d13d6531fc2e7883160b0e3a8d2', 276);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Update Data', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-4', '2.0.5', '3:8554bf7cdf0efcf8fe8105bb50d3dd30', 277);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'create temporary table to hold change detail information while the tables are modified', NOW(), 'Create Table, Custom SQL (x3)', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-1', '2.0.5', '3:1edfcd293610d3a2f803bae919b9826d', 278);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Custom SQL', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-3', '2.0.5', '3:400733a9dc22034bcb0b8b8e411e55b2', 279);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Update Data', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-3', '2.0.5', '3:f1f70bdbd696fdb464ebe75521269059', 280);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Update Data', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-4', '2.0.5', '3:e0aa28a5c3bb94e98ae4195dfa54d632', 281);

