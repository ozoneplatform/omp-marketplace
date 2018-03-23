-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/29/15 10:18 AM
-- Against: sa@jdbc:jtds:sqlserver://127.0.0.1:1433/omp
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE [dbo].[DATABASECHANGELOGLOCK] ([ID] INT NOT NULL, [LOCKED] BIT NOT NULL, [LOCKGRANTED] DATETIME, [LOCKEDBY] VARCHAR(255), CONSTRAINT [PK_DATABASECHANGELOGLOCK] PRIMARY KEY ([ID]))
GO

INSERT INTO [dbo].[DATABASECHANGELOGLOCK] ([ID], [LOCKED]) VALUES (1, 0)
GO

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE [dbo].[DATABASECHANGELOG] ([ID] VARCHAR(63) NOT NULL, [AUTHOR] VARCHAR(63) NOT NULL, [FILENAME] VARCHAR(200) NOT NULL, [DATEEXECUTED] DATETIME NOT NULL, [ORDEREXECUTED] INT NOT NULL, [EXECTYPE] VARCHAR(10) NOT NULL, [MD5SUM] VARCHAR(35), [DESCRIPTION] VARCHAR(255), [COMMENTS] VARCHAR(255), [TAG] VARCHAR(255), [LIQUIBASE] VARCHAR(20), CONSTRAINT [PK_DATABASECHANGELOG] PRIMARY KEY ([ID], [AUTHOR], [FILENAME]))
GO

-- Changeset changelog_2.3.1.groovy::2.3.1-1::marketplace::(Checksum: 3:b80148c4e25cef294a0e6c97ccccbb14)
-- DDL statements for creating Marketplace 2.3.1 database structure
create table U_DOMAIN (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, username nvarchar(255) not null, created_by_id numeric(19,0) null, edited_date datetime null, primary key (id))
GO

create table U_DOMAIN_preferences (preferences numeric(19,0) null, preferences_idx nvarchar(255) null, preferences_elt nvarchar(255) not null)
GO

create table U_VIEWS (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, isadminview tinyint not null, created_by_id numeric(19,0) null, name nvarchar(255) not null, isdefault tinyint not null, edited_date datetime null, primary key (id))
GO

create table avatar (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, is_default tinyint not null, pic image null, created_by_id numeric(19,0) null, content_type nvarchar(255) null, edited_date datetime null, primary key (id))
GO

create table category (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, title nvarchar(50) not null, description nvarchar(250) null, created_by_id numeric(19,0) null, edited_date datetime null, primary key (id))
GO

create table change_detail (id numeric(19,0) identity not null, version numeric(19,0) not null, new_value nvarchar(4000) null, object_version numeric(19,0) not null, object_id numeric(19,0) not null, field_name nvarchar(255) not null, old_value nvarchar(4000) null, object_class_name nvarchar(255) not null, primary key (id))
GO

create table change_log (id numeric(19,0) identity not null, version numeric(19,0) not null, object_version numeric(19,0) not null, changed_by_id numeric(19,0) not null, object_id numeric(19,0) not null, description nvarchar(255) not null, object_class_name nvarchar(255) not null, change_date datetime not null, primary key (id))
GO

create table custom_field (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, custom_field_definition_id numeric(19,0) not null, created_by_id numeric(19,0) null, edited_date datetime null, primary key (id))
GO

create table custom_field_definition (id numeric(19,0) identity not null, version numeric(19,0) not null, created_date datetime null, tooltip nvarchar(50) null, created_by_id numeric(19,0) null, edited_date datetime null, edited_by_id numeric(19,0) null, label nvarchar(50) not null, description nvarchar(250) null, is_required tinyint not null, name nvarchar(50) not null, style_type nvarchar(255) not null, primary key (id))
GO

create table custom_field_definition_types (cf_definition_types_id numeric(19,0) null, types_id numeric(19,0) null, types_idx int null)
GO

create table default_images (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, image_id numeric(19,0) not null, created_by_id numeric(19,0) null, defined_default_type nvarchar(255) not null, edited_date datetime null, primary key (id))
GO

create table drop_down_cf (id numeric(19,0) not null, value_id numeric(19,0) null, primary key (id))
GO

create table drop_down_cfd (id numeric(19,0) not null, primary key (id))
GO

create table ext_profile (id numeric(19,0) not null, external_view_url nvarchar(2083) null, system_uri nvarchar(255) null, external_id nvarchar(255) null, external_edit_url nvarchar(2083) null, primary key (id), unique (system_uri, external_id))
GO

create table ext_service_item (id numeric(19,0) not null, external_view_url nvarchar(2083) null, system_uri nvarchar(255) null, external_id nvarchar(255) null, external_edit_url nvarchar(2083) null, primary key (id), unique (system_uri, external_id))
GO

create table field_value (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, custom_field_definition_id numeric(19,0) not null, is_enabled int not null, display_text nvarchar(255) not null, created_by_id numeric(19,0) null, edited_date datetime null, field_values_idx int null, primary key (id))
GO

create table images (id numeric(19,0) identity not null, version numeric(19,0) not null, created_date datetime null, is_default tinyint not null, type nvarchar(255) not null, created_by_id numeric(19,0) null, content_type nvarchar(255) null, bytes image not null, edited_date datetime null, edited_by_id numeric(19,0) null, image_size double precision null, primary key (id))
GO

create table item_comment (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, service_item_id numeric(19,0) not null, text nvarchar(250) not null, created_by_id numeric(19,0) null, author_id numeric(19,0) not null, edited_date datetime null, primary key (id))
GO

create table modify_relationship_activity (id numeric(19,0) not null, primary key (id))
GO

create table owf_properties (id numeric(19,0) identity not null, version numeric(19,0) not null, visible_in_launch tinyint not null, edited_by_id numeric(19,0) null, created_date datetime null, singleton tinyint not null, created_by_id numeric(19,0) null, edited_date datetime null, primary key (id))
GO

create table profile (id numeric(19,0) identity not null, version numeric(19,0) not null, created_date datetime not null, created_by_id numeric(19,0) null, bio nvarchar(1000) null, edited_date datetime null, edited_by_id numeric(19,0) null, username nvarchar(250) not null unique, email nvarchar(250) null, avatar_id numeric(19,0) null, display_name nvarchar(250) null, primary key (id))
GO

create table rating (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, service_item_id numeric(19,0) not null, created_by_id numeric(19,0) null, author_id numeric(19,0) not null, rate float not null, edited_date datetime null, primary key (id))
GO

create table rejection_activity (id numeric(19,0) not null, rejection_listing_id numeric(19,0) null, primary key (id))
GO

create table rejection_justification (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, title nvarchar(50) not null, description nvarchar(250) null, created_by_id numeric(19,0) null, edited_date datetime null, primary key (id))
GO

create table rejection_listing (id numeric(19,0) identity not null, version numeric(19,0) not null, justification_id numeric(19,0) null, edited_by_id numeric(19,0) null, created_date datetime null, service_item_id numeric(19,0) not null, description nvarchar(2000) null, created_by_id numeric(19,0) null, author_id numeric(19,0) not null, edited_date datetime null, primary key (id))
GO

create table relationship (id numeric(19,0) identity not null, version numeric(19,0) not null, relationship_type nvarchar(255) not null, owning_entity_id numeric(19,0) not null, primary key (id))
GO

create table relationship_activity_log (mod_rel_activity_id numeric(19,0) not null, service_item_snapshot_id numeric(19,0) null, items_idx int null)
GO

create table relationship_service_item (relationship_related_items_id numeric(19,0) null, service_item_id numeric(19,0) null, related_items_idx int null)
GO

create table service_item (id numeric(19,0) identity not null, version numeric(19,0) not null, created_date datetime null, owf_properties_id numeric(19,0) null, avg_rate float not null, approval_status nvarchar(11) not null, title nvarchar(50) not null, screenshot1url nvarchar(2083) null, image_small_url nvarchar(2083) null, image_large_url nvarchar(2083) null, total_votes int not null, launch_url nvarchar(2083) null, uuid nvarchar(255) not null, version_name nvarchar(50) not null, release_date datetime not null, organization nvarchar(40) null, dependencies nvarchar(1000) null, types_id numeric(19,0) not null, screenshot2url nvarchar(2083) null, created_by_id numeric(19,0) null, requirements nvarchar(1000) null, doc_url nvarchar(2083) null, tech_poc nvarchar(255) not null, edited_date datetime null, edited_by_id numeric(19,0) null, total_comments int not null, is_hidden int not null, description nvarchar(4000) not null, state_id numeric(19,0) not null, install_url nvarchar(2083) null, last_activity_id numeric(19,0) null, author_id numeric(19,0) not null, primary key (id))
GO

create table service_item_activity (id numeric(19,0) identity not null, version numeric(19,0) not null, created_date datetime null, action nvarchar(255) not null, service_item_id numeric(19,0) not null, service_item_version numeric(19,0) null, created_by_id numeric(19,0) null, edited_date datetime null, edited_by_id numeric(19,0) null, activity_timestamp datetime not null, author_id numeric(19,0) not null, service_item_activities_idx int null, primary key (id))
GO

create table service_item_category (service_item_categories_id numeric(19,0) not null, category_id numeric(19,0) null, categories_idx int null)
GO

create table service_item_custom_field (service_item_custom_fields_id numeric(19,0) null, custom_field_id numeric(19,0) null, custom_fields_idx int null)
GO

create table service_item_snapshot (id numeric(19,0) identity not null, version numeric(19,0) not null, service_item_id numeric(19,0) null, title nvarchar(255) not null, primary key (id))
GO

create table si_recommended_layouts (service_item_id numeric(19,0) null, recommended_layout nvarchar(255) null)
GO

create table state (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, is_published tinyint not null, title nvarchar(50) not null, description nvarchar(250) null, created_by_id numeric(19,0) null, edited_date datetime null, primary key (id))
GO

create table text (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, value nvarchar(250) null, created_by_id numeric(19,0) null, read_only tinyint not null, name nvarchar(50) not null unique, edited_date datetime null, primary key (id))
GO

create table text_cf (id numeric(19,0) not null, value nvarchar(255) null, primary key (id))
GO

create table text_cfd (id numeric(19,0) not null, primary key (id))
GO

create table types (id numeric(19,0) identity not null, version numeric(19,0) not null, created_date datetime null, title nvarchar(50) not null, created_by_id numeric(19,0) null, role_access nvarchar(9) not null, edited_date datetime null, edited_by_id numeric(19,0) null, has_launch_url tinyint not null, is_widget tinyint not null, description nvarchar(250) null, image_id numeric(19,0) null, ozone_aware tinyint not null, has_icons tinyint not null, primary key (id))
GO

create table user_account (id numeric(19,0) identity not null, version numeric(19,0) not null, edited_by_id numeric(19,0) null, created_date datetime null, username nvarchar(250) not null unique, created_by_id numeric(19,0) null, last_login datetime null, edited_date datetime null, primary key (id))
GO

alter table U_DOMAIN add constraint FK97BAABEE7666C6D2 foreign key (created_by_id) references profile
GO

alter table U_DOMAIN add constraint FK97BAABEEE31CB353 foreign key (edited_by_id) references profile
GO

alter table U_VIEWS add constraint FK376C31E47666C6D2 foreign key (created_by_id) references profile
GO

alter table U_VIEWS add constraint FK376C31E4E31CB353 foreign key (edited_by_id) references profile
GO

alter table avatar add constraint FKAC32C1597666C6D2 foreign key (created_by_id) references profile
GO

alter table avatar add constraint FKAC32C159E31CB353 foreign key (edited_by_id) references profile
GO

alter table category add constraint FK302BCFE7666C6D2 foreign key (created_by_id) references profile
GO

alter table category add constraint FK302BCFEE31CB353 foreign key (edited_by_id) references profile
GO

alter table change_log add constraint FK80F28E35B624A19E foreign key (changed_by_id) references profile
GO

alter table custom_field add constraint FK2ACD76AC7666C6D2 foreign key (created_by_id) references profile
GO

alter table custom_field add constraint FK2ACD76ACE31CB353 foreign key (edited_by_id) references profile
GO

alter table custom_field add constraint FK2ACD76AC6F62C9ED foreign key (custom_field_definition_id) references custom_field_definition
GO

alter table custom_field_definition add constraint FK150F70C6E31CB353 foreign key (edited_by_id) references profile
GO

alter table custom_field_definition add constraint FK150F70C67666C6D2 foreign key (created_by_id) references profile
GO

alter table custom_field_definition_types add constraint FK1A84FFC06928D597 foreign key (types_id) references types
GO

alter table default_images add constraint FK6F064E367666C6D2 foreign key (created_by_id) references profile
GO

alter table default_images add constraint FK6F064E36E31CB353 foreign key (edited_by_id) references profile
GO

alter table default_images add constraint FK6F064E36553AF61A foreign key (image_id) references images
GO

alter table drop_down_cf add constraint FK13ADE7D0BC98CEE3 foreign key (value_id) references field_value
GO

alter table field_value add constraint FK29F571EC7666C6D2 foreign key (created_by_id) references profile
GO

alter table field_value add constraint FK29F571ECE31CB353 foreign key (edited_by_id) references profile
GO

alter table field_value add constraint FK29F571ECF1F14D3C foreign key (custom_field_definition_id) references drop_down_cfd
GO

alter table images add constraint FKB95A8278E31CB353 foreign key (edited_by_id) references profile
GO

alter table images add constraint FKB95A82787666C6D2 foreign key (created_by_id) references profile
GO

create index itm_cmnt_svc_item_id_idx on item_comment (service_item_id)
GO

create index itm_cmnt_author_id_idx on item_comment (author_id)
GO

alter table item_comment add constraint FKE6D04D337666C6D2 foreign key (created_by_id) references profile
GO

alter table item_comment add constraint FKE6D04D33E31CB353 foreign key (edited_by_id) references profile
GO

alter table item_comment add constraint FKE6D04D33C7E5C662 foreign key (service_item_id) references service_item
GO

alter table item_comment add constraint FKE6D04D335A032135 foreign key (author_id) references profile
GO

alter table owf_properties add constraint FKE88638947666C6D2 foreign key (created_by_id) references profile
GO

alter table owf_properties add constraint FKE8863894E31CB353 foreign key (edited_by_id) references profile
GO

alter table profile add constraint FKED8E89A9E31CB353 foreign key (edited_by_id) references profile
GO

alter table profile add constraint FKED8E89A97666C6D2 foreign key (created_by_id) references profile
GO

alter table profile add constraint FKED8E89A961C3343D foreign key (avatar_id) references avatar
GO

create index rating_svc_item_id_idx on rating (service_item_id)
GO

alter table rating add constraint FKC815B19D7666C6D2 foreign key (created_by_id) references profile
GO

alter table rating add constraint FKC815B19DE31CB353 foreign key (edited_by_id) references profile
GO

alter table rating add constraint FKC815B19DC7E5C662 foreign key (service_item_id) references service_item
GO

alter table rating add constraint FKC815B19D5A032135 foreign key (author_id) references profile
GO

alter table rejection_activity add constraint FKF35C128582548A4A foreign key (rejection_listing_id) references rejection_listing
GO

alter table rejection_justification add constraint FK12B0A53C7666C6D2 foreign key (created_by_id) references profile
GO

alter table rejection_justification add constraint FK12B0A53CE31CB353 foreign key (edited_by_id) references profile
GO

create index rej_lst_svc_item_id_idx on rejection_listing (service_item_id)
GO

alter table rejection_listing add constraint FK3F2BD44E7666C6D2 foreign key (created_by_id) references profile
GO

alter table rejection_listing add constraint FK3F2BD44EE31CB353 foreign key (edited_by_id) references profile
GO

alter table rejection_listing add constraint FK3F2BD44EC7E5C662 foreign key (service_item_id) references service_item
GO

alter table rejection_listing add constraint FK3F2BD44E5A032135 foreign key (author_id) references profile
GO

alter table rejection_listing add constraint FK3F2BD44E19CEB614 foreign key (justification_id) references rejection_justification
GO

alter table relationship add constraint FKF06476389D70DD39 foreign key (owning_entity_id) references service_item
GO

alter table relationship_activity_log add constraint FK594974BB25A20F9D foreign key (service_item_snapshot_id) references service_item_snapshot
GO

alter table relationship_service_item add constraint FKDA02504C7E5C662 foreign key (service_item_id) references service_item
GO

create index svc_item_author_id_idx on service_item (author_id)
GO

alter table service_item add constraint FK1571565D2746B676 foreign key (last_activity_id) references service_item_activity
GO

alter table service_item add constraint FK1571565D904D6974 foreign key (owf_properties_id) references owf_properties
GO

alter table service_item add constraint FK1571565DE31CB353 foreign key (edited_by_id) references profile
GO

alter table service_item add constraint FK1571565D7666C6D2 foreign key (created_by_id) references profile
GO

alter table service_item add constraint FK1571565D6928D597 foreign key (types_id) references types
GO

alter table service_item add constraint FK1571565D5A032135 foreign key (author_id) references profile
GO

alter table service_item add constraint FK1571565DDFEC3E97 foreign key (state_id) references state
GO

create index svc_item_act_svc_item_id_idx on service_item_activity (service_item_id)
GO

alter table service_item_activity add constraint FK870EA6B1E31CB353 foreign key (edited_by_id) references profile
GO

alter table service_item_activity add constraint FK870EA6B17666C6D2 foreign key (created_by_id) references profile
GO

alter table service_item_activity add constraint FK870EA6B1C7E5C662 foreign key (service_item_id) references service_item
GO

alter table service_item_activity add constraint FK870EA6B15A032135 foreign key (author_id) references profile
GO

create index svc_item_cat_id_idx on service_item_category (service_item_categories_id)
GO

alter table service_item_category add constraint FKECC570A0DA41995D foreign key (category_id) references category
GO

create index svc_item_cst_fld_id_idx on service_item_custom_field (service_item_custom_fields_id)
GO

alter table service_item_custom_field add constraint FK46E9894E7B56E054 foreign key (custom_field_id) references custom_field
GO

alter table service_item_snapshot add constraint FKFABD8966C7E5C662 foreign key (service_item_id) references service_item
GO

alter table si_recommended_layouts add constraint FK863C793CC7E5C662 foreign key (service_item_id) references service_item
GO

alter table state add constraint FK68AC4917666C6D2 foreign key (created_by_id) references profile
GO

alter table state add constraint FK68AC491E31CB353 foreign key (edited_by_id) references profile
GO

alter table text add constraint FK36452D7666C6D2 foreign key (created_by_id) references profile
GO

alter table text add constraint FK36452DE31CB353 foreign key (edited_by_id) references profile
GO

alter table types add constraint FK69B5879E31CB353 foreign key (edited_by_id) references profile
GO

alter table types add constraint FK69B58797666C6D2 foreign key (created_by_id) references profile
GO

alter table types add constraint FK69B5879553AF61A foreign key (image_id) references images
GO

alter table user_account add constraint FK14C321B97666C6D2 foreign key (created_by_id) references profile
GO

alter table user_account add constraint FK14C321B9E31CB353 foreign key (edited_by_id) references profile
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'DDL statements for creating Marketplace 2.3.1 database structure', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_2.3.1.groovy', '2.3.1-1', '2.0.5', '3:b80148c4e25cef294a0e6c97ccccbb14', 1)
GO

-- Changeset changelog_2.4.0.groovy::2.4.0-1::marketplace::(Checksum: 3:191a9d3527b92537101be697cbee2e46)
-- Create affiliated_marketplace table
CREATE TABLE [dbo].[affiliated_marketplace] ([id] NUMERIC(19,0) IDENTITY NOT NULL, [version] NUMERIC(19,0) NOT NULL, [active] INT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [icon_id] NUMERIC(19,0), [name] NVARCHAR(50) NOT NULL, [server_url] NVARCHAR(2083) NOT NULL, [timeout] NUMERIC(19,0), CONSTRAINT [PK_AFFILIATED_MARKETPLACE] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create affiliated_marketplace table', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-1', '2.0.5', '3:191a9d3527b92537101be697cbee2e46', 2)
GO

-- Changeset changelog_2.4.0.groovy::2.4.0-2::marketplace::(Checksum: 3:67ad76339bec05c947305e1ce09393bd)
-- Create FK constraints for affiliated_marketplace table
ALTER TABLE [dbo].[affiliated_marketplace] ADD CONSTRAINT [FKA6EB2C37666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[affiliated_marketplace] ADD CONSTRAINT [FKA6EB2C3E31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[affiliated_marketplace] ADD CONSTRAINT [FKA6EB2C3EA25263C] FOREIGN KEY ([icon_id]) REFERENCES [dbo].[images] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create FK constraints for affiliated_marketplace table', GETDATE(), 'Add Foreign Key Constraint (x3)', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-2', '2.0.5', '3:67ad76339bec05c947305e1ce09393bd', 3)
GO

-- Changeset changelog_2.4.0.groovy::2.4.0-3::marketplace::(Checksum: 3:7a686549fc702788c4411a4fa326c5ec)
-- Drop types.is_widget column
ALTER TABLE [dbo].[types] DROP COLUMN [is_widget]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Drop types.is_widget column', GETDATE(), 'Drop Column', 'EXECUTED', 'changelog_2.4.0.groovy', '2.4.0-3', '2.0.5', '3:7a686549fc702788c4411a4fa326c5ec', 4)
GO

-- Changeset changelog_2.5.0.groovy::2.5.0-1::marketplace::(Checksum: 3:fa54d9c033cbf44fb8095b1c237349b7)
ALTER TABLE [dbo].[category] ADD [uuid] NVARCHAR(255)
GO

UPDATE [dbo].[category] SET [uuid] = NEWID() WHERE [uuid] is NULL
GO

ALTER TABLE [dbo].[state] ADD [uuid] NVARCHAR(255)
GO

UPDATE [dbo].[state] SET [uuid] = NEWID() WHERE [uuid] is NULL
GO

ALTER TABLE [dbo].[types] ADD [uuid] NVARCHAR(255)
GO

UPDATE [dbo].[types] SET [uuid] = NEWID() WHERE [uuid] is NULL
GO

CREATE UNIQUE INDEX [category_uuid_idx] ON [dbo].[category]([uuid])
GO

CREATE UNIQUE INDEX [state_uuid_idx] ON [dbo].[state]([uuid])
GO

CREATE UNIQUE INDEX [types_uuid_idx] ON [dbo].[types]([uuid])
GO

ALTER TABLE [dbo].[profile] ADD [uuid] NVARCHAR(255)
GO

UPDATE [dbo].[profile] SET [uuid] = NEWID() WHERE [uuid] is NULL
GO

ALTER TABLE [dbo].[custom_field_definition] ADD [uuid] NVARCHAR(255)
GO

UPDATE [dbo].[custom_field_definition] SET [uuid] = NEWID() WHERE [uuid] is NULL
GO

CREATE UNIQUE INDEX [profile_uuid_idx] ON [dbo].[profile]([uuid])
GO

CREATE UNIQUE INDEX [cfd_uuid_idx] ON [dbo].[custom_field_definition]([uuid])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column, Custom SQL, Add Column, Custom SQL, Add Column, Custom SQL, Create Index (x3), Add Column, Custom SQL, Add Column, Custom SQL, Create Index (x2)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-1', '2.0.5', '3:fa54d9c033cbf44fb8095b1c237349b7', 5)
GO

-- Changeset changelog_2.5.0.groovy::2.5.0-3::marketplace::(Checksum: 3:7ad852b9ea5b3e51df8b1898837d458e)
ALTER TABLE [dbo].[owf_properties] ADD [background] BIT NOT NULL CONSTRAINT DF_owf_properties_background DEFAULT 0
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-3', '2.0.5', '3:7ad852b9ea5b3e51df8b1898837d458e', 6)
GO

-- Changeset changelog_2.5.0.groovy::2.5.0-4::marketplace::(Checksum: 3:b0a5061dbda5633e6510a3b3f6460090)
CREATE TABLE [dbo].[import_task] ([id] NUMERIC(19,0) IDENTITY NOT NULL, [version] NUMERIC(19,0) NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [cron_exp] NVARCHAR(255), [download_images] TINYINT NOT NULL, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [enabled] TINYINT NOT NULL, [exec_interval] INT, [extra_url_params] NVARCHAR(512), [interface_config_id] NUMERIC(19,0) NOT NULL, [last_run_result_id] NUMERIC(19,0), [name] NVARCHAR(50) NOT NULL, [update_type] NVARCHAR(7) NOT NULL, [url] NVARCHAR(255), [keystore_pass] NVARCHAR(2048), [keystore_path] NVARCHAR(2048), [truststore_path] NVARCHAR(2048), CONSTRAINT [import_taskPK] PRIMARY KEY ([id]), UNIQUE ([name]))
GO

CREATE TABLE [dbo].[import_task_result] ([id] NUMERIC(19,0) IDENTITY NOT NULL, [version] NUMERIC(19,0) NOT NULL, [message] NVARCHAR(4000) NOT NULL, [result] TINYINT NOT NULL, [run_date] DATETIME NOT NULL, [task_id] NUMERIC(19,0) NOT NULL, CONSTRAINT [import_task_rPK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[interface_configuration] ([id] NUMERIC(19,0) IDENTITY NOT NULL, [version] NUMERIC(19,0) NOT NULL, [allow_truncate] TINYINT NOT NULL, [auto_create_meta_data] TINYINT NOT NULL, [default_large_icon_url] NVARCHAR(2048), [default_small_icon_url] NVARCHAR(2048), [delta_since_time_param] NVARCHAR(64), [delta_static_parameters] NVARCHAR(2048), [full_static_parameters] NVARCHAR(2048), [loose_match] TINYINT NOT NULL, [name] NVARCHAR(256) NOT NULL, [query_date_format] NVARCHAR(32), [response_date_format] NVARCHAR(32), CONSTRAINT [interface_conPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table (x3)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-4', '2.0.5', '3:b0a5061dbda5633e6510a3b3f6460090', 7)
GO

-- Changeset changelog_2.5.0.groovy::2.5.0-5::marketplace::(Checksum: 3:f6cb3e205f6f2414aeb88e9f687f1791)
ALTER TABLE [dbo].[import_task] ADD CONSTRAINT [FK578EF9DF7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[import_task] ADD CONSTRAINT [FK578EF9DFE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[import_task] ADD CONSTRAINT [FK578EF9DFA31F8712] FOREIGN KEY ([interface_config_id]) REFERENCES [dbo].[interface_configuration] ([id])
GO

ALTER TABLE [dbo].[import_task] ADD CONSTRAINT [FK578EF9DF919216CA] FOREIGN KEY ([last_run_result_id]) REFERENCES [dbo].[import_task_result] ([id])
GO

ALTER TABLE [dbo].[import_task_result] ADD CONSTRAINT [FK983AC27D11D7F882] FOREIGN KEY ([task_id]) REFERENCES [dbo].[import_task] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Foreign Key Constraint (x5)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-5', '2.0.5', '3:f6cb3e205f6f2414aeb88e9f687f1791', 8)
GO

-- Changeset changelog_2.5.0.groovy::2.5.0-6::marketplace::(Checksum: 3:e17decde06415350cdcd9bb76628afa0)
ALTER TABLE [dbo].[service_item] ALTER COLUMN [title] NVARCHAR(256)
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [version_name] NVARCHAR(256)
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [organization] NVARCHAR(256)
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [tech_poc] NVARCHAR(256)
GO

ALTER TABLE [dbo].[profile] ALTER COLUMN [username] NVARCHAR(256)
GO

ALTER TABLE [dbo].[profile] ALTER COLUMN [display_name] NVARCHAR(256)
GO

ALTER TABLE [dbo].[profile] ALTER COLUMN [email] NVARCHAR(256)
GO

ALTER TABLE [dbo].[text_cf] ALTER COLUMN [value] NVARCHAR(256)
GO

ALTER TABLE [dbo].[ext_service_item] ALTER COLUMN [system_uri] NVARCHAR(256)
GO

ALTER TABLE [dbo].[ext_service_item] ALTER COLUMN [external_id] NVARCHAR(256)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Modify data type (x10)', 'EXECUTED', 'changelog_2.5.0.groovy', '2.5.0-6', '2.0.5', '3:e17decde06415350cdcd9bb76628afa0', 9)
GO

-- Changeset changelog_5.0.groovy::5.0-1::marketplace::(Checksum: 3:fc5a25b1ec26ad35157cbb13b848adc4)
-- Drop types.role_access and u_views table
ALTER TABLE [dbo].[types] DROP COLUMN [role_access]
GO

DROP TABLE [dbo].[U_VIEWS]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Drop types.role_access and u_views table', GETDATE(), 'Drop Column, Drop Table', 'EXECUTED', 'changelog_5.0.groovy', '5.0-1', '2.0.5', '3:fc5a25b1ec26ad35157cbb13b848adc4', 10)
GO

-- Changeset changelog_5.0.groovy::5.0-2::marketplace::(Checksum: 3:ab731912fbde155e95e16daa86d37ea1)
-- Add columns for counting votes to service_item
ALTER TABLE [dbo].[service_item] ADD [total_rate1] INT
GO

ALTER TABLE [dbo].[service_item] ADD [total_rate2] INT
GO

ALTER TABLE [dbo].[service_item] ADD [total_rate3] INT
GO

ALTER TABLE [dbo].[service_item] ADD [total_rate4] INT
GO

ALTER TABLE [dbo].[service_item] ADD [total_rate5] INT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Add columns for counting votes to service_item', GETDATE(), 'Add Column (x5)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-2', '2.0.5', '3:ab731912fbde155e95e16daa86d37ea1', 11)
GO

-- Changeset changelog_5.0.groovy::5.0-3::marketplace::(Checksum: 3:5f6ba95466cbe695beb449f18dadf636)
-- Replace import_task.download_images with interface_configuration.download_images
ALTER TABLE [dbo].[interface_configuration] ADD [download_images] BIT NOT NULL CONSTRAINT DF_interface_configuration_download_images DEFAULT 0
GO

ALTER TABLE [dbo].[import_task] DROP COLUMN [download_images]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Replace import_task.download_images with interface_configuration.download_images', GETDATE(), 'Add Column, Drop Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-3', '2.0.5', '3:5f6ba95466cbe695beb449f18dadf636', 12)
GO

-- Changeset changelog_5.0.groovy::5.0-4::marketplace::(Checksum: 3:bc1f678fe3a1ff7662c5d56f3f24dfa1)
-- Drop not-null constraint for and expand 'text' field in item_comment table
ALTER TABLE [dbo].[item_comment] ALTER COLUMN [text] NVARCHAR(250) NULL
GO

ALTER TABLE [dbo].[item_comment] ALTER COLUMN [text] NVARCHAR(4000)
GO

ALTER TABLE [dbo].[item_comment] ADD [rate] FLOAT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Drop not-null constraint for and expand ''text'' field in item_comment table', GETDATE(), 'Drop Not-Null Constraint, Modify data type, Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-4', '2.0.5', '3:bc1f678fe3a1ff7662c5d56f3f24dfa1', 13)
GO

-- Changeset changelog_5.0.groovy::5.0-7::marketplace::(Checksum: 3:2d396f22f301193c7279b5fa21d33d99)
-- Drop RATING table
DROP TABLE [dbo].[rating]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Drop RATING table', GETDATE(), 'Drop Table', 'EXECUTED', 'changelog_5.0.groovy', '5.0-7', '2.0.5', '3:2d396f22f301193c7279b5fa21d33d99', 14)
GO

-- Changeset changelog_5.0.groovy::5.0-8::marketplace::(Checksum: 3:c8b9b34bfb6d169e19a3e2e714ea359a)
-- Add tables supporting text area and image URL custom fields
CREATE TABLE [dbo].[image_url_cf] ([id] NUMERIC(19,0) NOT NULL, [value] NVARCHAR(2083), CONSTRAINT [image_url_cfPK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[image_url_cfd] ([id] NUMERIC(19,0) NOT NULL, CONSTRAINT [image_url_cfdPK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[text_area_cf] ([id] NUMERIC(19,0) NOT NULL, [value] NVARCHAR(4000), CONSTRAINT [text_area_cfPK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[text_area_cfd] ([id] NUMERIC(19,0) NOT NULL, CONSTRAINT [text_area_cfdPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Add tables supporting text area and image URL custom fields', GETDATE(), 'Create Table (x4)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-8', '2.0.5', '3:c8b9b34bfb6d169e19a3e2e714ea359a', 15)
GO

-- Changeset changelog_5.0.groovy::5.0-9::marketplace::(Checksum: 3:f74ea577fc7ca0deb42e33ed729f5e5e)
-- Add tables supporting check box custom fields
CREATE TABLE [dbo].[check_box_cf] ([id] NUMERIC(19,0) NOT NULL, [value] TINYINT, CONSTRAINT [check_box_cfPK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[check_box_cfd] ([id] NUMERIC(19,0) NOT NULL, [selected_by_default] TINYINT, CONSTRAINT [check_box_cfdPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Add tables supporting check box custom fields', GETDATE(), 'Create Table (x2)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-9', '2.0.5', '3:f74ea577fc7ca0deb42e33ed729f5e5e', 16)
GO

-- Changeset changelog_5.0.groovy::5.0-10::marketplace::(Checksum: 3:3161bd12012792d0da7ab51979cb4f9b)
-- Add column to custom filed defintion table to store the section where to display that field
ALTER TABLE [dbo].[custom_field_definition] ADD [section] VARCHAR(255)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Add column to custom filed defintion table to store the section where to display that field', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-10', '2.0.5', '3:3161bd12012792d0da7ab51979cb4f9b', 17)
GO

-- Changeset changelog_5.0.groovy::5.0-11::marketplace::(Checksum: 3:701f5fdf006444dbf8e28f0b791c999a)
ALTER TABLE [dbo].[custom_field_definition] ADD [all_types] BIT NOT NULL CONSTRAINT DF_custom_field_definition_all_types DEFAULT 0
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-11', '2.0.5', '3:701f5fdf006444dbf8e28f0b791c999a', 18)
GO

-- Changeset changelog_5.0.groovy::5.0-12::marketplace::(Checksum: 3:d25a6166facdcb45342bda12c7e3d7fc)
-- Add column to service item table to store the approved date
ALTER TABLE [dbo].[service_item] ADD [approval_date] DATETIME
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Add column to service item table to store the approved date', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_5.0.groovy', '5.0-12', '2.0.5', '3:d25a6166facdcb45342bda12c7e3d7fc', 19)
GO

-- Changeset changelog_5.0.groovy::5.0-14::marketplace::(Checksum: 3:3091b73b99472559cae278d8c2c3858b)
-- Create indexes for change_detail and change_log tables to speed up lookups
CREATE INDEX [change_detail_object_idx] ON [dbo].[change_detail]([object_class_name], [object_id], [object_version])
GO

CREATE INDEX [change_log_object_idx] ON [dbo].[change_log]([object_class_name], [object_id], [object_version])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create indexes for change_detail and change_log tables to speed up lookups', GETDATE(), 'Create Index (x2)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-14', '2.0.5', '3:3091b73b99472559cae278d8c2c3858b', 20)
GO

-- Changeset changelog_5.0.groovy::5.0-15::marketplace::(Checksum: 3:07d03d98d92fb50e61d9c52df992d6cd)
-- Create indexes for custom_field_definition_types and field_value tables to speed up lookups
CREATE INDEX [cfd_types_cfd_idx] ON [dbo].[custom_field_definition_types]([cf_definition_types_id])
GO

CREATE INDEX [cfd_types_types_idx] ON [dbo].[custom_field_definition_types]([types_id])
GO

CREATE INDEX [field_value_cfd_idx] ON [dbo].[field_value]([custom_field_definition_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create indexes for custom_field_definition_types and field_value tables to speed up lookups', GETDATE(), 'Create Index (x3)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-15', '2.0.5', '3:07d03d98d92fb50e61d9c52df992d6cd', 21)
GO

-- Changeset changelog_5.0.groovy::5.0-16::marketplace::(Checksum: 3:4cfe9e1b266fa8f81917b9c2f8255e6d)
-- Create indexes for tables to speed up lookups
CREATE INDEX [si_snapshot_id_idx] ON [dbo].[service_item_snapshot]([service_item_id])
GO

CREATE INDEX [cf_cfd_idx] ON [dbo].[custom_field]([custom_field_definition_id])
GO

CREATE INDEX [si_rec_layouts_idx] ON [dbo].[si_recommended_layouts]([service_item_id])
GO

CREATE INDEX [rejection_act_listing_id_idx] ON [dbo].[rejection_activity]([rejection_listing_id])
GO

CREATE INDEX [rejection_listing_just_id_idx] ON [dbo].[rejection_listing]([justification_id])
GO

CREATE INDEX [relationship_owing_id_idx] ON [dbo].[relationship]([owning_entity_id])
GO

CREATE INDEX [rel_act_log_mod_rel_act_idx] ON [dbo].[relationship_activity_log]([mod_rel_activity_id])
GO

CREATE INDEX [rel_act_log_mod_si_snpsht_idx] ON [dbo].[relationship_activity_log]([service_item_snapshot_id])
GO

CREATE INDEX [rel_si_rel_items_id_idx] ON [dbo].[relationship_service_item]([relationship_related_items_id])
GO

CREATE INDEX [si_act_si_ver_idx] ON [dbo].[service_item_activity]([service_item_version])
GO

CREATE INDEX [si_cat_cat_id_idx] ON [dbo].[service_item_category]([category_id])
GO

CREATE INDEX [si_cf_cf_id_idx] ON [dbo].[service_item_custom_field]([custom_field_id])
GO

CREATE INDEX [si_owf_props_id_idx] ON [dbo].[service_item]([owf_properties_id])
GO

CREATE INDEX [si_types_id_idx] ON [dbo].[service_item]([types_id])
GO

CREATE INDEX [si_state_id_idx] ON [dbo].[service_item]([state_id])
GO

CREATE INDEX [si_last_activity_idx] ON [dbo].[service_item]([last_activity_id])
GO

CREATE INDEX [si_created_by_id_idx] ON [dbo].[service_item]([created_by_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create indexes for tables to speed up lookups', GETDATE(), 'Create Index (x17)', 'EXECUTED', 'changelog_5.0.groovy', '5.0-16', '2.0.5', '3:4cfe9e1b266fa8f81917b9c2f8255e6d', 22)
GO

-- Changeset changelog_5.0.groovy::5.0-18::marketplace::(Checksum: 3:43678308dc3a992c28f53b4efeb30268)
-- Create indexes for rejection_listing table to speed up lookups
CREATE INDEX [rej_lst_author_id_idx] ON [dbo].[rejection_listing]([author_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create indexes for rejection_listing table to speed up lookups', GETDATE(), 'Create Index', 'EXECUTED', 'changelog_5.0.groovy', '5.0-18', '2.0.5', '3:43678308dc3a992c28f53b4efeb30268', 23)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-1::aml-marketplace::(Checksum: 3:ecd912dc8867e6cb8284084390923ab2)
CREATE TABLE [dbo].[score_card] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [score] FLOAT(19) NOT NULL, CONSTRAINT [score_cardPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-1', '2.0.5', '3:ecd912dc8867e6cb8284084390923ab2', 24)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-2::aml-marketplace::(Checksum: 3:64762952f51d0fe7b8c45fe091eba93a)
CREATE TABLE [dbo].[score_card_item] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [description] VARCHAR(500) NOT NULL, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [is_standard_question] BIT NOT NULL, [question] VARCHAR(250) NOT NULL, [weight] FLOAT(19), CONSTRAINT [sc_itemPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-2', '2.0.5', '3:64762952f51d0fe7b8c45fe091eba93a', 25)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-3::aml-marketplace::(Checksum: 3:b51bd092c9dcac8bd991f969117efa09)
CREATE TABLE [dbo].[score_card_item_response] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [is_satisfied] BIT NOT NULL, [score_card_id] BIGINT NOT NULL, [score_card_item_id] BIGINT NOT NULL, CONSTRAINT [sc_responsePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-3', '2.0.5', '3:b51bd092c9dcac8bd991f969117efa09', 26)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-4::aml-marketplace::(Checksum: 3:a10c921a8ef6370cc64cc06016c7c55c)
ALTER TABLE [dbo].[service_item] ADD [score_card_id] BIGINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-4', '2.0.5', '3:a10c921a8ef6370cc64cc06016c7c55c', 27)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-5::aml-marketplace::(Checksum: 3:63bdefa2034c53e720100a0e83826dab)
CREATE INDEX [FK5E60409D7666C6D2] ON [dbo].[score_card]([created_by_id])
GO

CREATE INDEX [FK5E60409DE31CB353] ON [dbo].[score_card]([edited_by_id])
GO

CREATE INDEX [FKE51CCD757666C6D2] ON [dbo].[score_card_item]([created_by_id])
GO

CREATE INDEX [FKE51CCD75E31CB353] ON [dbo].[score_card_item]([edited_by_id])
GO

CREATE INDEX [FK80A6CBCB190E00BC] ON [dbo].[score_card_item_response]([score_card_id])
GO

CREATE INDEX [FK80A6CBCB7666C6D2] ON [dbo].[score_card_item_response]([created_by_id])
GO

CREATE INDEX [FK80A6CBCBE31CB353] ON [dbo].[score_card_item_response]([edited_by_id])
GO

CREATE INDEX [FK80A6CBCBEF469C97] ON [dbo].[score_card_item_response]([score_card_item_id])
GO

CREATE INDEX [FK1571565D190E00BC] ON [dbo].[service_item]([score_card_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Index (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-5', '2.0.5', '3:63bdefa2034c53e720100a0e83826dab', 28)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-6::aml-marketplace::(Checksum: 3:b4c50686ed2e827d6a5d5de248d5154c)
ALTER TABLE [dbo].[score_card] ADD CONSTRAINT [FK5E60409D7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[score_card] ADD CONSTRAINT [FK5E60409DE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[score_card_item] ADD CONSTRAINT [FKE51CCD757666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[score_card_item] ADD CONSTRAINT [FKE51CCD75E31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[score_card_item_response] ADD CONSTRAINT [FK80A6CBCB7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[score_card_item_response] ADD CONSTRAINT [FK80A6CBCBE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[score_card_item_response] ADD CONSTRAINT [FK80A6CBCB190E00BC] FOREIGN KEY ([score_card_id]) REFERENCES [dbo].[score_card] ([id])
GO

ALTER TABLE [dbo].[score_card_item_response] ADD CONSTRAINT [FK80A6CBCBEF469C97] FOREIGN KEY ([score_card_item_id]) REFERENCES [dbo].[score_card_item] ([id])
GO

ALTER TABLE [dbo].[service_item] ADD CONSTRAINT [FK1571565D190E00BC] FOREIGN KEY ([score_card_id]) REFERENCES [dbo].[score_card] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Add Foreign Key Constraint (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-6', '2.0.5', '3:b4c50686ed2e827d6a5d5de248d5154c', 29)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE [dbo].[service_item] ADD [agency] VARCHAR(255)
GO

ALTER TABLE [dbo].[service_item] ADD [agency_icon] VARCHAR(255)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.5', '3:a1d04babbf1c20d5360e96e65afd2bae', 30)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:ec8f4a5a83dab731f6b4890930c62edd)
ALTER TABLE [dbo].[service_item] ADD [is_outside] TINYINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.5', '3:ec8f4a5a83dab731f6b4890930c62edd', 31)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-3::franchise-store::(Checksum: 3:0ee8acc59cc31553b0362e999aca8aa2)
DROP INDEX [dbo].[change_detail].[change_detail_object_idx]
GO

DROP INDEX [dbo].[change_log].[change_log_object_idx]
GO

DROP INDEX [dbo].[custom_field_definition_types].[cfd_types_cfd_idx]
GO

DROP INDEX [dbo].[relationship_activity_log].[rel_act_log_mod_rel_act_idx]
GO

DROP INDEX [dbo].[relationship_service_item].[rel_si_rel_items_id_idx]
GO

DROP INDEX [dbo].[service_item_activity].[si_act_si_ver_idx]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Drop Index (x6)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-3', '2.0.5', '3:0ee8acc59cc31553b0362e999aca8aa2', 32)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-4::franchise-store::(Checksum: 3:3820fc70e2e0949952a5ffedf0928c29)
CREATE TABLE [dbo].[drop_down_cf_field_value] ([drop_down_cf_field_value_id] NUMERIC(19,0), [field_value_id] NUMERIC(19,0), [field_value_list_idx] INT)
GO

ALTER TABLE [dbo].[custom_field_definition] ADD [is_permanent] BIT
GO

ALTER TABLE [dbo].[drop_down_cfd] ADD [is_multi_select] BIT
GO

CREATE INDEX [FK2627FFDDA5BD888] ON [dbo].[drop_down_cf_field_value]([field_value_id])
GO

ALTER TABLE [dbo].[drop_down_cf_field_value] ADD CONSTRAINT [FK2627FFDDA5BD888] FOREIGN KEY ([field_value_id]) REFERENCES [dbo].[field_value] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.5', '3:3820fc70e2e0949952a5ffedf0928c29', 33)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-5::franchise-store::(Checksum: 3:f9079ad54404b13406806316cdbf7169)
ALTER TABLE [dbo].[service_item] ALTER COLUMN [tech_poc] NVARCHAR(256) NOT NULL
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [title] NVARCHAR(256) NOT NULL
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [version_name] NVARCHAR(256) NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Add Not-Null Constraint (x3)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-5', '2.0.5', '3:f9079ad54404b13406806316cdbf7169', 34)
GO

-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-1::franchise-store::(Checksum: 3:93bccc9aa844709bb5669060a1147169)
CREATE TABLE [dbo].[application_configuration] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [code] VARCHAR(250) NOT NULL, [value] VARCHAR(2000), [title] VARCHAR(250) NOT NULL, [description] VARCHAR(2000), [type] VARCHAR(250) NOT NULL, [group_name] VARCHAR(250) NOT NULL, [sub_group_name] VARCHAR(250), [mutable] BIT NOT NULL, [sub_group_order] BIGINT, CONSTRAINT [application_configurationPK] PRIMARY KEY ([id]), UNIQUE ([code]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-1', '2.0.5', '3:93bccc9aa844709bb5669060a1147169', 35)
GO

-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-2::franchise-store::(Checksum: 3:8c76d18c40c82ecf837da46af444846c)
-- Create index for application_configuration.group_name
CREATE INDEX [FKFC9C0477666C6D2] ON [dbo].[application_configuration]([created_by_id])
GO

CREATE INDEX [FKFC9C047E31CB353] ON [dbo].[application_configuration]([edited_by_id])
GO

CREATE INDEX [app_config_group_name_idx] ON [dbo].[application_configuration]([group_name])
GO

ALTER TABLE [dbo].[application_configuration] ADD CONSTRAINT [FKFC9C0477666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[application_configuration] ADD CONSTRAINT [FKFC9C047E31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', 'Create index for application_configuration.group_name', GETDATE(), 'Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-2', '2.0.5', '3:8c76d18c40c82ecf837da46af444846c', 36)
GO

-- Changeset changelog_7.1.groovy::7.1-1::marketplace::(Checksum: 3:70d3313cedb44a2430d183b912b1cfe8)
ALTER TABLE [dbo].[owf_properties] ADD [height] BIGINT
GO

ALTER TABLE [dbo].[owf_properties] ADD [width] BIGINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column (x2)', 'EXECUTED', 'changelog_7.1.groovy', '7.1-1', '2.0.5', '3:70d3313cedb44a2430d183b912b1cfe8', 37)
GO

-- Changeset changelog_7.1.groovy::7.1-2::marketplace::(Checksum: 3:5e5d0f3fee742d246977b284fdd76d73)
ALTER TABLE [dbo].[application_configuration] ADD [help] VARCHAR(2000)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.1.groovy', '7.1-2', '2.0.5', '3:5e5d0f3fee742d246977b284fdd76d73', 38)
GO

-- Changeset changelog_7.2.groovy::7.2-1::marketplace::(Checksum: 3:37e31178188d6d69550c6abefcfafe5f)
CREATE TABLE [dbo].[intent_action] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(256), [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(256) NOT NULL, [uuid] VARCHAR(255), CONSTRAINT [intent_actionPK] PRIMARY KEY ([id]))
GO

CREATE INDEX [FKEBCDD397666C6D2] ON [dbo].[intent_action]([created_by_id])
GO

CREATE INDEX [FKEBCDD39E31CB353] ON [dbo].[intent_action]([edited_by_id])
GO

CREATE UNIQUE INDEX [uuid_unique_1366321689429] ON [dbo].[intent_action]([uuid])
GO

ALTER TABLE [dbo].[intent_action] ADD CONSTRAINT [FKEBCDD397666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[intent_action] ADD CONSTRAINT [FKEBCDD39E31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-1', '2.0.5', '3:37e31178188d6d69550c6abefcfafe5f', 39)
GO

-- Changeset changelog_7.2.groovy::7.2-2::marketplace::(Checksum: 3:8f37748aa663ac4b2f4f4ae5625d1ddb)
CREATE TABLE [dbo].[intent_direction] ([created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(250), [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [id] BIGINT IDENTITY NOT NULL, [title] VARCHAR(7) NOT NULL, [uuid] VARCHAR(255), [version] BIGINT NOT NULL, CONSTRAINT [intent_directPK] PRIMARY KEY ([id]))
GO

CREATE INDEX [FKC723A59C7666C6D2] ON [dbo].[intent_direction]([created_by_id])
GO

CREATE INDEX [FKC723A59CE31CB353] ON [dbo].[intent_direction]([edited_by_id])
GO

CREATE UNIQUE INDEX [title_unique_1366386256451] ON [dbo].[intent_direction]([title])
GO

CREATE UNIQUE INDEX [uuid_unique_1366386256451] ON [dbo].[intent_direction]([uuid])
GO

ALTER TABLE [dbo].[intent_direction] ADD CONSTRAINT [FKC723A59C7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[intent_direction] ADD CONSTRAINT [FKC723A59CE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x4), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-2', '2.0.5', '3:8f37748aa663ac4b2f4f4ae5625d1ddb', 40)
GO

-- Changeset changelog_7.2.groovy::7.2-3::marketplace::(Checksum: 3:ae20f44a6ea989ee57e6bd457e6a4f49)
CREATE TABLE [dbo].[intent_data_type] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(256), [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(256) NOT NULL, [uuid] VARCHAR(255), CONSTRAINT [intent_data_tPK] PRIMARY KEY ([id]))
GO

CREATE INDEX [FKEADB30CC7666C6D2] ON [dbo].[intent_data_type]([created_by_id])
GO

CREATE INDEX [FKEADB30CCE31CB353] ON [dbo].[intent_data_type]([edited_by_id])
GO

CREATE UNIQUE INDEX [uuid_unique_1366398847848] ON [dbo].[intent_data_type]([uuid])
GO

ALTER TABLE [dbo].[intent_data_type] ADD CONSTRAINT [FKEADB30CC7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[intent_data_type] ADD CONSTRAINT [FKEADB30CCE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-3', '2.0.5', '3:ae20f44a6ea989ee57e6bd457e6a4f49', 41)
GO

-- Changeset changelog_7.2.groovy::7.2-4::marketplace::(Checksum: 3:41323d3f3bfe6139c03ad329142ecf9a)
CREATE TABLE [dbo].[owf_widget_types] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(255) NOT NULL, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(256) NOT NULL, [uuid] VARCHAR(255), CONSTRAINT [owf_widget_typePK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[owf_properties] ADD [owf_widget_type] VARCHAR(255) NOT NULL CONSTRAINT DF_owf_properties_owf_widget_type DEFAULT 'standard'
GO

CREATE INDEX [FK6AB6A9DF7666C6D2] ON [dbo].[owf_widget_types]([created_by_id])
GO

CREATE INDEX [FK6AB6A9DFE31CB353] ON [dbo].[owf_widget_types]([edited_by_id])
GO

CREATE UNIQUE INDEX [uuid_unique_1366666109930] ON [dbo].[owf_widget_types]([uuid])
GO

ALTER TABLE [dbo].[owf_widget_types] ADD CONSTRAINT [FK6AB6A9DF7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[owf_widget_types] ADD CONSTRAINT [FK6AB6A9DFE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Add Column, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-4', '2.0.5', '3:41323d3f3bfe6139c03ad329142ecf9a', 42)
GO

-- Changeset changelog_7.2.groovy::7.2-5::marketplace::(Checksum: 3:21571b54501c40307f4bd432fba456f1)
ALTER TABLE [dbo].[owf_properties] ADD [universal_name] VARCHAR(255)
GO

ALTER TABLE [dbo].[owf_properties] ADD [stack_context] VARCHAR(200)
GO

ALTER TABLE [dbo].[owf_properties] ADD [stack_descriptor] NVARCHAR(MAX)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column (x3)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-5', '2.0.5', '3:21571b54501c40307f4bd432fba456f1', 43)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-1::marketplace::(Checksum: 3:ef88eb1178b48b3590a7291563871a30)
ALTER TABLE [dbo].[score_card_item] ADD [image] VARCHAR(255)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-1', '2.0.5', '3:ef88eb1178b48b3590a7291563871a30', 44)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-2::marketplace::(Checksum: 3:cb41b1179b6a373e0861c7b29400d814)
ALTER TABLE [dbo].[service_item_activity] ADD [details] VARCHAR(255)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-2', '2.0.5', '3:cb41b1179b6a373e0861c7b29400d814', 45)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-3::marketplace::(Checksum: 3:ab1c2a78ad360b01b91b1a2232ae3c87)
ALTER TABLE [dbo].[owf_properties] ADD [descriptor_url] VARCHAR(2083)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-3', '2.0.5', '3:ab1c2a78ad360b01b91b1a2232ae3c87', 46)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-4::marketplace::(Checksum: 3:1bdeb96b81e0666aa3a1a95ed5f889dc)
ALTER TABLE [dbo].[service_item] ALTER COLUMN [description] VARCHAR(4000) NULL
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [release_date] DATETIME NULL
GO

DROP INDEX [dbo].[service_item].[si_state_id_idx]
GO

ALTER TABLE [dbo].[service_item] DROP CONSTRAINT [FK1571565DDFEC3E97]
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [state_id] NUMERIC(19,0) NULL
GO

CREATE INDEX [si_state_id_idx] ON [dbo].[service_item]([state_id])
GO

alter table service_item add constraint FK1571565DDFEC3E97 foreign key (state_id) references state
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [tech_poc] VARCHAR(256) NULL
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [version_name] VARCHAR(256) NULL
GO

DROP INDEX [dbo].[service_item_category].[svc_item_cat_id_idx]
GO

ALTER TABLE [dbo].[service_item_category] ALTER COLUMN [service_item_categories_id] BIGINT NULL
GO

CREATE INDEX [svc_item_cat_id_idx] ON [dbo].[service_item_category]([service_item_categories_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Drop Not-Null Constraint (x2), Drop Index, Drop Foreign Key Constraint, Drop Not-Null Constraint, Create Index, Custom SQL, Drop Not-Null Constraint (x2), Drop Index, Drop Not-Null Constraint, Create Index', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-4', '2.0.5', '3:1bdeb96b81e0666aa3a1a95ed5f889dc', 47)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-5::marketplace::(Checksum: 3:8ac7e4781a6ac2c3fea38a3ad678cce5)
CREATE TABLE [dbo].[intent] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [action_id] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [data_type_id] BIGINT NOT NULL, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [receive] BIT NOT NULL, [send] BIT NOT NULL, CONSTRAINT [intentPK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[owf_properties_intent] ([owf_properties_intents_id] NUMERIC(19,0), [intent_id] BIGINT)
GO

CREATE INDEX [FKB971369CD8544299] ON [dbo].[intent]([action_id])
GO

CREATE INDEX [FKB971369C7666C6D2] ON [dbo].[intent]([created_by_id])
GO

CREATE INDEX [FKB971369C283F938E] ON [dbo].[intent]([data_type_id])
GO

CREATE INDEX [FKB971369CE31CB353] ON [dbo].[intent]([edited_by_id])
GO

CREATE INDEX [FK3F99ECA7A651895D] ON [dbo].[owf_properties_intent]([intent_id])
GO

CREATE INDEX [owfProps_intent_id_idx] ON [dbo].[owf_properties_intent]([owf_properties_intents_id])
GO

ALTER TABLE [dbo].[intent] ADD CONSTRAINT [FKB971369CD8544299] FOREIGN KEY ([action_id]) REFERENCES [dbo].[intent_action] ([id])
GO

ALTER TABLE [dbo].[intent] ADD CONSTRAINT [FKB971369C7666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[intent] ADD CONSTRAINT [FKB971369C283F938E] FOREIGN KEY ([data_type_id]) REFERENCES [dbo].[intent_data_type] ([id])
GO

ALTER TABLE [dbo].[intent] ADD CONSTRAINT [FKB971369CE31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[owf_properties_intent] ADD CONSTRAINT [FK3F99ECA7A651895D] FOREIGN KEY ([intent_id]) REFERENCES [dbo].[intent] ([id])
GO

ALTER TABLE [dbo].[owf_properties_intent] ADD CONSTRAINT [FK3F99ECA74704E25C] FOREIGN KEY ([owf_properties_intents_id]) REFERENCES [dbo].[owf_properties] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table (x2), Create Index (x6), Add Foreign Key Constraint (x6)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-5', '2.0.5', '3:8ac7e4781a6ac2c3fea38a3ad678cce5', 48)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-6::marketplace::(Checksum: 3:188326c86a75bf88826fe0addaaed0bb)
ALTER TABLE [dbo].[types] ADD [is_permanent] BIT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-6', '2.0.5', '3:188326c86a75bf88826fe0addaaed0bb', 49)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-8::marketplace::(Checksum: 3:0e4b85a436faf5855fe99ffe9238c780)
ALTER TABLE [dbo].[score_card_item] ADD [show_on_listing] BIT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-8', '2.0.5', '3:0e4b85a436faf5855fe99ffe9238c780', 50)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-12::marketplace::(Checksum: 3:81da42021cf40d5046b0d45387730d23)
-- Create new mapping table for service item's 'owners' field replacing 'author' property.
CREATE TABLE [dbo].[service_item_profile] ([service_item_owners_id] BIGINT, [profile_id] BIGINT, [owners_idx] INT)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create new mapping table for service item''s ''owners'' field replacing ''author'' property.', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-12', '2.0.5', '3:81da42021cf40d5046b0d45387730d23', 51)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-14::marketplace::(Checksum: 3:22e4413e8d2db347eca366766100decf)
-- Drop 'author_id' column corresponding to the 'author' property of service item being removed.
DROP INDEX [dbo].[service_item].[svc_item_author_id_idx]
GO

ALTER TABLE [dbo].[service_item] DROP CONSTRAINT [FK1571565D5A032135]
GO

ALTER TABLE [dbo].[service_item] DROP COLUMN [author_id]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Drop ''author_id'' column corresponding to the ''author'' property of service item being removed.', GETDATE(), 'Drop Index, Drop Foreign Key Constraint, Drop Column', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-14', '2.0.5', '3:22e4413e8d2db347eca366766100decf', 52)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-15::marketplace::(Checksum: 3:dc82c39c1b23306e58690c3e90950e59)
CREATE TABLE [dbo].[service_item_documentation_url] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT CONSTRAINT DF_service_item_documentation_url_version DEFAULT 0 NOT NULL, [name] VARCHAR(256) NOT NULL, [service_item_id] NUMERIC(19,0) NOT NULL, [url] VARCHAR(2083) NOT NULL, CONSTRAINT [service_item_PK] PRIMARY KEY ([id]))
GO

CREATE TABLE [dbo].[service_item_tech_pocs] ([service_item_id] NUMERIC(19,0), [tech_poc] VARCHAR(256))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-15', '2.0.5', '3:dc82c39c1b23306e58690c3e90950e59', 53)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-16::marketplace::(Checksum: 3:29b11454e2ee263c40d977090718a9ca)
ALTER TABLE [dbo].[service_item_documentation_url] ADD CONSTRAINT [FK24572D08C7E5C662] FOREIGN KEY ([service_item_id]) REFERENCES [dbo].[service_item] ([id])
GO

ALTER TABLE [dbo].[service_item_tech_pocs] ADD CONSTRAINT [FKA55CFB56C7E5C662] FOREIGN KEY ([service_item_id]) REFERENCES [dbo].[service_item] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-16', '2.0.5', '3:29b11454e2ee263c40d977090718a9ca', 54)
GO

-- Changeset changelog_7.3.0.groovy::7.3.0-19::marketplace::(Checksum: 3:b6082ec33d0f9cd5358039b2cb06e19f)
ALTER TABLE [dbo].[service_item] DROP COLUMN [doc_url]
GO

ALTER TABLE [dbo].[service_item] DROP COLUMN [tech_poc]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Drop Column (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-19', '2.0.5', '3:b6082ec33d0f9cd5358039b2cb06e19f', 55)
GO

-- Changeset changelog_7.5.0.groovy::7.5.0-1::marketplace::(Checksum: 3:66c904064f544b5f583c3ad37d68795f)
CREATE TABLE [dbo].[agency] ([id] BIGINT IDENTITY NOT NULL, [title] VARCHAR(255), [icon_url] VARCHAR(2083) NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [version] BIGINT CONSTRAINT DF_agency_version DEFAULT 0 NOT NULL, CONSTRAINT [agencyPK] PRIMARY KEY ([id]), UNIQUE ([title]))
GO

ALTER TABLE [dbo].[service_item] ADD [agency_id] BIGINT
GO

ALTER TABLE [dbo].[service_item] ADD CONSTRAINT [SERVICE_ITEM_AGENCY_FK] FOREIGN KEY ([agency_id]) REFERENCES [dbo].[agency] ([id]) ON DELETE SET NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Add Column, Add Foreign Key Constraint', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-1', '2.0.5', '3:66c904064f544b5f583c3ad37d68795f', 56)
GO

-- Changeset changelog_7.5.0.groovy::7.5.0-3::marketplace::(Checksum: 3:978dc473dd30d3beb49553a281692079)
ALTER TABLE [dbo].[service_item] DROP COLUMN [agency]
GO

ALTER TABLE [dbo].[service_item] DROP COLUMN [agency_icon]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Drop Column (x2)', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-3', '2.0.5', '3:978dc473dd30d3beb49553a281692079', 57)
GO

-- Changeset changelog_7.6.0.groovy::7.6.0-1::marketplace::(Checksum: 3:b2f7151bab712c4b6d8df8d3caa0a9c4)
CREATE TABLE [dbo].[screenshot] ([id] NUMERIC(19,0) IDENTITY NOT NULL, [small_image_url] VARCHAR(2083) NOT NULL, [large_image_url] VARCHAR(2083), [ordinal] INT, [service_item_id] NUMERIC(19,0), [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [version] BIGINT CONSTRAINT DF_screenshot_version DEFAULT 0 NOT NULL, CONSTRAINT [PK_SCREENSHOT] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[screenshot] ADD CONSTRAINT [SCREENSHOT_SERVICE_ITEM_FK] FOREIGN KEY ([service_item_id]) REFERENCES [dbo].[service_item] ([id]) ON DELETE CASCADE
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Add Foreign Key Constraint', 'EXECUTED', 'changelog_7.6.0.groovy', '7.6.0-1', '2.0.5', '3:b2f7151bab712c4b6d8df8d3caa0a9c4', 58)
GO

-- Changeset changelog_7.6.0.groovy::7.6.0-2::marketplace::(Checksum: 3:ed5b1d1912b7769b4449c3533a5e4a86)
INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot1url, 0, id
                FROM service_item
                WHERE screenshot1url IS NOT NULL AND screenshot1url <> ''
GO

INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot2url, 1, id
                FROM service_item
                WHERE screenshot2url IS NOT NULL AND screenshot2url <> ''
GO

ALTER TABLE [dbo].[service_item] DROP COLUMN [screenshot1url]
GO

ALTER TABLE [dbo].[service_item] DROP COLUMN [screenshot2url]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Custom SQL (x2), Drop Column (x2)', 'EXECUTED', 'changelog_7.6.0.groovy', '7.6.0-2', '2.0.5', '3:ed5b1d1912b7769b4449c3533a5e4a86', 59)
GO

-- Changeset changelog_7.9.0.groovy::7.9.0-2::marketplace::(Checksum: 3:e350ccacf7bdbef62697e47991c7b3c7)
CREATE TABLE [dbo].[contact_type] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [required] BIT NOT NULL, [title] VARCHAR(50) NOT NULL, CONSTRAINT [contact_typePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-2', '2.0.5', '3:e350ccacf7bdbef62697e47991c7b3c7', 60)
GO

-- Changeset changelog_7.9.0.groovy::7.9.0-3::marketplace::(Checksum: 3:d635e9f30d58b48f2dbe732b48b1e161)
CREATE TABLE [dbo].[contact] ([id] NUMERIC(19,0) IDENTITY NOT NULL, [version] BIGINT NOT NULL, [email] VARCHAR(100) NOT NULL, [name] VARCHAR(100) NOT NULL, [organization] VARCHAR(100), [secure_phone] VARCHAR(50), [type_id] BIGINT NOT NULL, [service_item_id] NUMERIC(19,0) NOT NULL, [unsecure_phone] VARCHAR(50), [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, CONSTRAINT [contactPK] PRIMARY KEY ([id]))
GO

CREATE INDEX [FK4C2BB7F97666C6D2] ON [dbo].[contact_type]([created_by_id])
GO

CREATE INDEX [FK4C2BB7F9E31CB353] ON [dbo].[contact_type]([edited_by_id])
GO

CREATE INDEX [FK38B72420C7E5C662] ON [dbo].[contact]([service_item_id])
GO

CREATE INDEX [FK38B72420BA3FC877] ON [dbo].[contact]([type_id])
GO

CREATE INDEX [FK38B72420E31CB353] ON [dbo].[contact]([edited_by_id])
GO

CREATE INDEX [FK38B724207666C6D2] ON [dbo].[contact]([created_by_id])
GO

ALTER TABLE [dbo].[contact_type] ADD CONSTRAINT [FK4C2BB7F97666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[contact_type] ADD CONSTRAINT [FK4C2BB7F9E31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[contact] ADD CONSTRAINT [FK38B724207666C6D2] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[contact] ADD CONSTRAINT [FK38B72420E31CB353] FOREIGN KEY ([edited_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[contact] ADD CONSTRAINT [FK38B72420BA3FC877] FOREIGN KEY ([type_id]) REFERENCES [dbo].[contact_type] ([id])
GO

ALTER TABLE [dbo].[contact] ADD CONSTRAINT [FK38B72420C7E5C662] FOREIGN KEY ([service_item_id]) REFERENCES [dbo].[service_item] ([id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x6), Add Foreign Key Constraint (x6)', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-3', '2.0.5', '3:d635e9f30d58b48f2dbe732b48b1e161', 61)
GO

-- Changeset changelog_7.9.0.groovy::7.9.0-4::marketplace::(Checksum: 3:d76e0aa887053b1f64792f3bfd91cbe2)
CREATE UNIQUE INDEX [title_unique_1389723125532] ON [dbo].[contact_type]([title])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Index', 'EXECUTED', 'changelog_7.9.0.groovy', '7.9.0-4', '2.0.5', '3:d76e0aa887053b1f64792f3bfd91cbe2', 62)
GO

-- Changeset changelog_7.10.0.groovy::7.10.0-1::marketplace::(Checksum: 3:c36101b773b019ee4625b75411cb8eb6)
ALTER TABLE [dbo].[service_item] ADD [opens_in_new_browser_tab] BIT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-1', '2.0.5', '3:c36101b773b019ee4625b75411cb8eb6', 63)
GO

-- Changeset changelog_7.10.0.groovy::7.10.0-2::marketplace::(Checksum: 3:beafab57293fa84b39d3e25be9531b43)
ALTER TABLE [dbo].[profile] ADD [user_roles] VARCHAR(255)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-2', '2.0.5', '3:beafab57293fa84b39d3e25be9531b43', 64)
GO

-- Changeset changelog_7.11.0.groovy::7.11.0-2::marketplace::(Checksum: 3:4829b18572a019543127454c992c46a8)
ALTER TABLE [dbo].[change_detail] ADD [service_item_activity_id] NUMERIC(19,0) NOT NULL
GO

CREATE INDEX [FKB4467BC0855307BD] ON [dbo].[change_detail]([service_item_activity_id])
GO

ALTER TABLE [dbo].[change_detail] ADD CONSTRAINT [FKB4467BC0855307BD] FOREIGN KEY ([service_item_activity_id]) REFERENCES [dbo].[service_item_activity] ([id])
GO

ALTER TABLE [dbo].[service_item_activity] DROP COLUMN [service_item_version]
GO

ALTER TABLE [dbo].[service_item_activity] DROP COLUMN [details]
GO

ALTER TABLE [dbo].[change_detail] DROP COLUMN [object_class_name]
GO

ALTER TABLE [dbo].[change_detail] DROP COLUMN [object_id]
GO

ALTER TABLE [dbo].[change_detail] DROP COLUMN [object_version]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column, Create Index, Add Foreign Key Constraint, Drop Column (x5)', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-2', '2.0.5', '3:4829b18572a019543127454c992c46a8', 65)
GO

-- Changeset changelog_7.12.0.groovy::7.12.0-1::marketplace::(Checksum: 3:932edbdb31fd51477e9580ee37bc8fba)
DROP TABLE [dbo].[change_log]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Drop Table', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-1', '2.0.5', '3:932edbdb31fd51477e9580ee37bc8fba', 66)
GO

-- Changeset changelog_7.12.0.groovy::7.12.0-2::marketplace::(Checksum: 3:8d556e5d8cba5cb21c029658e7014d29)
CREATE TABLE [dbo].[tag] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(16) NOT NULL, CONSTRAINT [tag_PK] PRIMARY KEY ([id]))
GO

CREATE INDEX [tag_title_idx] ON [dbo].[tag]([title])
GO

CREATE TABLE [dbo].[service_item_tag] ([id] BIGINT IDENTITY NOT NULL, [service_item_id] NUMERIC(19,0) NOT NULL, [tag_id] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [version] BIGINT NOT NULL, CONSTRAINT [service_item_tag_PK] PRIMARY KEY ([id]))
GO

CREATE INDEX [service_item_tag_si_idx] ON [dbo].[service_item_tag]([service_item_id])
GO

CREATE INDEX [service_item_tag_tag_idx] ON [dbo].[service_item_tag]([tag_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index, Create Table, Create Index (x2)', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-2', '2.0.5', '3:8d556e5d8cba5cb21c029658e7014d29', 67)
GO

-- Changeset changelog_7.12.0.groovy::7.12.0-3::marketplace::(Checksum: 3:81c00e342acc2d70a14bee90e6b5de57)
ALTER TABLE [dbo].[service_item_tag] ADD CONSTRAINT [service_item_tag_FK_si] FOREIGN KEY ([service_item_id]) REFERENCES [dbo].[service_item] ([id])
GO

ALTER TABLE [dbo].[service_item_tag] ADD CONSTRAINT [service_item_tag_FK_tag] FOREIGN KEY ([tag_id]) REFERENCES [dbo].[tag] ([id])
GO

ALTER TABLE [dbo].[service_item_tag] ADD CONSTRAINT [service_item_tag_FK_cb] FOREIGN KEY ([created_by_id]) REFERENCES [dbo].[profile] ([id])
GO

ALTER TABLE [dbo].[service_item_tag] ADD CONSTRAINT [service_item_tag_unique_idx] UNIQUE ([service_item_id], [tag_id])
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Foreign Key Constraint (x3), Add Unique Constraint', 'EXECUTED', 'changelog_7.12.0.groovy', '7.12.0-3', '2.0.5', '3:81c00e342acc2d70a14bee90e6b5de57', 68)
GO

-- Changeset changelog_7.16.0.groovy::7.16.0-2::marketplace::(Checksum: 3:9c7fbae6fdea582b51eeed650647e080)
ALTER TABLE [dbo].[service_item] ADD [image_medium_url] VARCHAR(2083)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-2', '2.0.5', '3:9c7fbae6fdea582b51eeed650647e080', 69)
GO

-- Changeset changelog_7.16.0.groovy::7.16.0-5::marketplace::(Checksum: 3:f0977751d972a0f445038888bc9bd973)
CREATE TABLE [dbo].[service_item_score_card_item] ([service_item_id] NUMERIC(19,0), [score_card_item_id] BIGINT)
GO

ALTER TABLE [dbo].[service_item_score_card_item] ADD CONSTRAINT [FKBF91F93EF469C97] FOREIGN KEY ([score_card_item_id]) REFERENCES [dbo].[score_card_item] ([id]) ON DELETE CASCADE
GO

ALTER TABLE [dbo].[service_item_score_card_item] ADD CONSTRAINT [FKBF91F939C51FA9F] FOREIGN KEY ([service_item_id]) REFERENCES [dbo].[service_item] ([id])
GO

ALTER TABLE [dbo].[score_card] DROP CONSTRAINT [FK5E60409D7666C6D2]
GO

ALTER TABLE [dbo].[score_card] DROP CONSTRAINT [FK5E60409DE31CB353]
GO

ALTER TABLE [dbo].[score_card_item_response] DROP CONSTRAINT [FK80A6CBCB7666C6D2]
GO

ALTER TABLE [dbo].[score_card_item_response] DROP CONSTRAINT [FK80A6CBCBE31CB353]
GO

ALTER TABLE [dbo].[score_card_item_response] DROP CONSTRAINT [FK80A6CBCB190E00BC]
GO

ALTER TABLE [dbo].[score_card_item_response] DROP CONSTRAINT [FK80A6CBCBEF469C97]
GO

ALTER TABLE [dbo].[service_item] DROP CONSTRAINT [FK1571565D190E00BC]
GO

DROP INDEX [dbo].[service_item].[FK1571565D190E00BC]
GO

ALTER TABLE [dbo].[score_card_item] DROP COLUMN [is_standard_question]
GO

ALTER TABLE [dbo].[score_card_item] DROP COLUMN [weight]
GO

ALTER TABLE [dbo].[service_item] DROP COLUMN [score_card_id]
GO

DROP TABLE [dbo].[score_card]
GO

DROP TABLE [dbo].[score_card_item_response]
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Add Foreign Key Constraint (x2), Drop Foreign Key Constraint (x7), Drop Index, Drop Column (x3), Drop Table (x2)', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-5', '2.0.5', '3:f0977751d972a0f445038888bc9bd973', 70)
GO

-- Changeset app_config.groovy::app_config-2::marketplace::(Checksum: 3:f3084b1a17c66754a2444e37659a69a4)
INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.name', 'BRANDING', 1, NULL, 1, ' ', 'String', '', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.logo', 'BRANDING', 1, NULL, 2, ' ', 'Image', '/static/themes/gold.theme/images/Mp_logo.png', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.icon', 'BRANDING', 1, NULL, 3, ' ', 'Image', '/static/themes/common/images/agency/agencyDefault.png', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('free.warning.content', 'BRANDING', 1, NULL, 4, ' ', 'String', '', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('url.public', 'BRANDING', 1, NULL, 5, ' ', 'string', '', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('about.box.content', 'BRANDING', 1, 'About Information', 1, ' ', 'String', 'The Store allows visitors to discover and explore business and convenience applications and enables user-configurable visualizations of available content.', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('about.box.image', 'BRANDING', 1, 'About Information', 2, ' ', 'Image', '/static/themes/gold.theme/images/Mp_logo_128x128.png', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('access.alert.enable', 'BRANDING', 1, 'Access Alert Information', 1, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('access.alert.content', 'BRANDING', 1, 'Access Alert Information', 2, ' ', 'String', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla interdum eleifend sapien dignissim malesuada. Sed imperdiet augue vitae justo feugiat eget porta est blandit. Proin ipsum ipsum, rutrum ac gravida in, ullamcorper a augue. Sed at scelerisque augue. Morbi scelerisque gravida sapien ut feugiat. Donec dictum, nisl commodo dapibus pellentesque, enim quam consectetur quam, at dictum dui augue at risus. Ut id nunc in justo molestie semper. Curabitur magna velit, varius eu porttitor et, tempor pulvinar nulla. Nam at tellus nec felis tincidunt fringilla. Nunc nisi sem, egestas ut consequat eget, luctus et nisi. Nulla et lorem odio, vitae pretium ipsum. Integer tellus libero, molestie a feugiat a, imperdiet sit amet metus. Aenean auctor fringilla eros, sit amet suscipit felis eleifend a.', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.footer.featured.title', 'BRANDING', 1, 'Footer Information', 1, ' ', 'String', 'Store', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.footer.featured.content', 'BRANDING', 1, 'Footer Information', 2, ' ', 'String', 'The Store allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content.', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.open.search.title.message', 'BRANDING', 1, 'Open Search', 1, ' ', 'String', 'Marketplace Search', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.open.search.description.message', 'BRANDING', 1, 'Open Search', 2, ' ', 'String', 'Marketplace Search Description', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.open.search.fav.icon', 'BRANDING', 1, 'Open Search', 3, ' ', 'Image', '/static/themes/gold.theme/images/favicon.ico', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.open.search.site.icon', 'BRANDING', 1, 'Open Search', 4, ' ', 'Image', '/static/themes/common/images/themes/default/market_64x64.png', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.custom.header.url', 'BRANDING', 1, 'Custom Header and Footer', 1, ' ', 'String', NULL, 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.custom.header.height', 'BRANDING', 1, 'Custom Header and Footer', 2, ' ', 'Integer', '0', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.custom.footer.url', 'BRANDING', 1, 'Custom Header and Footer', 3, ' ', 'String', NULL, 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.custom.footer.height', 'BRANDING', 1, 'Custom Header and Footer', 4, ' ', 'Integer', '0', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.custom.css', 'BRANDING', 1, 'Custom Header and Footer', 5, ' ', 'String', NULL, 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.custom.js', 'BRANDING', 1, 'Custom Header and Footer', 6, ' ', 'String', NULL, 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.insideOutside.behavior', 'ADDITIONAL_CONFIGURATION', 1, NULL, 1, ' ', 'String', 'ADMIN_SELECTED', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.enable.ext.serviceitem', 'ADDITIONAL_CONFIGURATION', 1, NULL, 2, ' ', 'Boolean', 'false', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.allow.owner.to.edit.approved.listing', 'ADDITIONAL_CONFIGURATION', 1, NULL, 3, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.amp.search.result.size', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Search', 1, ' ', 'Integer', '30', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.amp.search.default.timeout', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Search', 2, ' ', 'Integer', '30000', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.image.allow.upload', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Image Configurations', 1, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.type.image.max.size', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Image Configurations', 2, ' ', 'Integer', '1048576', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.amp.image.max.size', 'ADDITIONAL_CONFIGURATION', 1, 'Partner Store Image Configurations', 3, ' ', 'Integer', '1048576', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.owf.sync.urls', 'ADDITIONAL_CONFIGURATION', 1, 'OWF Sync', 1, ' ', 'List', NULL, 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.contact.email', 'ADDITIONAL_CONFIGURATION', 1, 'Store Contact Information', 1, ' ', 'String', NULL, 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.quick.view.detail.fields', 'ADDITIONAL_CONFIGURATION', 1, 'Quick View', 1, ' ', 'String', 'types, categories, state, releasedDate, lastActivity, owners, organization, Alternate POC Info, Technical POC Info, Support POC Info, launchUrl', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.enable.scoreCard', 'SCORECARD', 1, NULL, 1, ' ', 'Boolean', 'false', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.is.franchise', 'HIDDEN', 0, NULL, 1, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.default.theme', 'HIDDEN', 1, NULL, 2, ' ', 'String', 'gold', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.job.disable.accounts.interval', 'HIDDEN', 1, NULL, 3, ' ', 'Integer', '1440', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.job.disable.accounts.start.time', 'HIDDEN', 1, NULL, 4, ' ', 'String', '23:59:59', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.session.control.enabled', 'USER_ACCOUNT_SETTINGS', 1, 'Session Control', 1, ' ', 'Boolean', 'false', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.session.control.max.concurrent', 'USER_ACCOUNT_SETTINGS', 1, 'Session Control', 2, ' ', 'Integer', '1', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.disable.inactive.accounts', 'USER_ACCOUNT_SETTINGS', 1, 'Inactive Accounts', 1, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.inactivity.threshold', 'USER_ACCOUNT_SETTINGS', 1, 'Inactive Accounts', 2, ' ', 'Integer', '90', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.enable.cef.logging', 'AUDITING', 1, NULL, 1, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.enable.cef.object.access.logging', 'AUDITING', 1, NULL, 2, ' ', 'Boolean', 'false', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.enable.cef.log.sweep', 'AUDITING', 1, NULL, 3, ' ', 'Boolean', 'true', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.cef.log.location', 'AUDITING', 1, NULL, 4, ' ', 'String', '/usr/share/tomcat6', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.cef.sweep.log.location', 'AUDITING', 1, NULL, 5, ' ', 'String', '/var/log/cef', 0)
GO

INSERT INTO [dbo].[application_configuration] ([code], [group_name], [mutable], [sub_group_name], [sub_group_order], [title], [type], [value], [version]) VALUES ('store.security.level', 'AUDITING', 1, NULL, 6, ' ', 'String', NULL, 0)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Insert Row (x47)', 'EXECUTED', 'app_config.groovy', 'app_config-2', '2.0.5', '3:f3084b1a17c66754a2444e37659a69a4', 71)
GO

-- Changeset changelog_7.16.0.groovy::7.16.0-6::marketplace::(Checksum: 3:d2f31c636d691a47e3d2e8b73cbd66b1)
DELETE FROM [dbo].[application_configuration]  WHERE code = 'store.domains'
GO

UPDATE [dbo].[application_configuration] SET [sub_group_name] = 'Partner Store Image Configurations' WHERE sub_group_name = 'Image Configurations'
GO

UPDATE [dbo].[application_configuration] SET [sub_group_name] = 'Partner Store Search' WHERE sub_group_name = 'Affiliated Search'
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Delete Data, Update Data (x2)', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-6', '2.0.5', '3:d2f31c636d691a47e3d2e8b73cbd66b1', 72)
GO

-- Changeset changelog_7.16.0.groovy::7.16.0-7::marketplace::(Checksum: 3:027bd76dec2ca88af0145b25dfc00c93)
exec sp_rename '[dbo].[U_DOMAIN_preferences]', 'u_domain_preferences'
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Rename Table', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-7', '2.0.5', '3:027bd76dec2ca88af0145b25dfc00c93', 73)
GO

-- Changeset changelog_7.16.0.groovy::7.16.0-8::marketplace::(Checksum: 3:54b5f1e34983065c4273b232dece1777)
ALTER TABLE [dbo].[owf_properties] ADD [mobile_ready] BIT NOT NULL CONSTRAINT DF_owf_properties_mobile_ready DEFAULT 0
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-8', '2.0.5', '3:54b5f1e34983065c4273b232dece1777', 74)
GO

-- Changeset changelog_7.16.0.groovy::7.16.0-9::marketplace::(Checksum: 3:2337d16fb8f4bbd2ea73b9c3a830c68f)
UPDATE application_configuration SET type = 'String' WHERE type = 'string'
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-9', '2.0.5', '3:2337d16fb8f4bbd2ea73b9c3a830c68f', 75)
GO

-- *********************************************************************
-- SQL to add all changesets to database history table
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/29/15 10:19 AM
-- Against: sa@jdbc:jtds:sqlserver://127.0.0.1:1433/omp
-- Liquibase version: 2.0.5
-- *********************************************************************

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Transfer rating information into the item_comment table', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-5', '2.0.5', '3:8b35402c8c016b3dd7a9b83f8f23348a', 264)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Compute number of votes for each rating for the service items', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-6', '2.0.5', '3:9ed670c788094838125b3045c42f6fce', 265)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Transfer approved date information into the service_item table', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-13', '2.0.5', '3:6efbf9a22068bcab382bc6a466bf40ae', 266)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Set the section for existing cfds to typeProperties', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_5.0.groovy', '5.0-17', '2.0.5', '3:ce25f8b65d80c3e960d0883264d5206f', 267)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Update Data (x3)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-7', '2.0.5', '3:b337838d37b361db713c9fcfad1af557', 268)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Update Data (x2)', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-9', '2.0.5', '3:8386d1edefea8f79361809de2eea6608', 269)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create a record in service_item_profile table for each service item linking it with its owner.', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-13', '2.0.5', '3:3658ca8cb6ad5d02fc45644d4a37a38b', 270)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create a record in service_item_tech_pocs table for each service item linking it with its technical POC.', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-17', '2.0.5', '3:ff31dc39fa460d736a824986b06ed8e2', 271)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'Create a record in service_item_documentation_url table for each service item linking it with its technical POC.', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_7.3.0.groovy', '7.3.0-18', '2.0.5', '3:7daee389a18729b712930a71f89bb6f9', 272)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Custom SQL (x2)', 'EXECUTED', 'changelog_7.5.0.groovy', '7.5.0-2', '2.0.5', '3:a45d3e114365ea29cc7cfc4fec431a94', 273)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Update Data', 'EXECUTED', 'changelog_7.10.0.groovy', '7.10.0-4', '2.0.5', '3:8554bf7cdf0efcf8fe8105bb50d3dd30', 274)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', 'create temporary table to hold change detail information while the tables are modified', GETDATE(), 'Create Table, Custom SQL (x3)', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-1', '2.0.5', '3:1edfcd293610d3a2f803bae919b9826d', 275)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Custom SQL', 'EXECUTED', 'changelog_7.11.0.groovy', '7.11.0-3', '2.0.5', '3:400733a9dc22034bcb0b8b8e411e55b2', 276)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Update Data', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-3', '2.0.5', '3:f1f70bdbd696fdb464ebe75521269059', 277)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Update Data', 'EXECUTED', 'changelog_7.16.0.groovy', '7.16.0-4', '2.0.5', '3:e0aa28a5c3bb94e98ae4195dfa54d632', 278)
GO

