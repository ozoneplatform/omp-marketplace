-- Create Database Lock Table
CREATE TABLE DATABASECHANGELOGLOCK (ID int NOT NULL, LOCKED bit NOT NULL, LOCKGRANTED datetime2(3), LOCKEDBY nvarchar(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID))
GO

-- Initialize Database Lock Table
DELETE FROM DATABASECHANGELOGLOCK
GO

INSERT INTO DATABASECHANGELOGLOCK (ID, LOCKED) VALUES (1, 0)
GO

-- Create Database Change Log Table
CREATE TABLE DATABASECHANGELOG (ID nvarchar(255) NOT NULL, AUTHOR nvarchar(255) NOT NULL, FILENAME nvarchar(255) NOT NULL, DATEEXECUTED datetime2(3) NOT NULL, ORDEREXECUTED int NOT NULL, EXECTYPE nvarchar(10) NOT NULL, MD5SUM nvarchar(35), DESCRIPTION nvarchar(255), COMMENTS nvarchar(255), TAG nvarchar(255), LIQUIBASE nvarchar(20), CONTEXTS nvarchar(255), LABELS nvarchar(255), DEPLOYMENT_ID nvarchar(10))
GO

-- Changeset create/changelog-7.17.0-agency.yaml::7.17.0-agency::omp
-- create 'agency' table
CREATE TABLE agency (id bigint IDENTITY (1, 1) NOT NULL, title varchar(255), icon_url varchar(2083) NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, edited_by_id numeric(19, 0), edited_date smalldatetime, version bigint CONSTRAINT DF_agency_version DEFAULT 0 NOT NULL, CONSTRAINT agencyPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-agency', 'omp', 'create/changelog-7.17.0-agency.yaml', GETDATE(), 1, '8:c867d1b49a249375e14c8e370e5f1c0e', 'createTable tableName=agency', 'create ''agency'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-agency.yaml::7.17.0-agency-constraints::omp
-- create 'agency' constraints
ALTER TABLE agency ADD UNIQUE (title)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-agency-constraints', 'omp', 'create/changelog-7.17.0-agency.yaml', GETDATE(), 2, '8:280ec00d14e9fa3d4625618ec09af8d3', 'addUniqueConstraint tableName=agency', 'create ''agency'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-avatar.yaml::7.17.0-avatar::omp
-- create 'avatar' table
CREATE TABLE avatar (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, is_default tinyint NOT NULL, pic image, created_by_id numeric(19, 0), content_type nvarchar(255), edited_date datetime)
GO

ALTER TABLE avatar ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-avatar', 'omp', 'create/changelog-7.17.0-avatar.yaml', GETDATE(), 3, '8:1e86cf73c05e2367c4ac5987028ae511', 'createTable tableName=avatar; addPrimaryKey tableName=avatar', 'create ''avatar'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-check_box_cf.yaml::7.17.0-check_box_cf::omp
-- create 'check_box_cf' table
CREATE TABLE check_box_cf (id numeric(19, 0) NOT NULL, value tinyint, CONSTRAINT check_box_cfPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-check_box_cf', 'omp', 'create/changelog-7.17.0-check_box_cf.yaml', GETDATE(), 4, '8:0de74b0ff0454f5ce4c5ca39eaa1e4f5', 'createTable tableName=check_box_cf', 'create ''check_box_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-check_box_cfd.yaml::7.17.0-check_box_cfd::omp
-- create 'check_box_cfd' table
CREATE TABLE check_box_cfd (id numeric(19, 0) NOT NULL, selected_by_default tinyint, CONSTRAINT check_box_cfdPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-check_box_cfd', 'omp', 'create/changelog-7.17.0-check_box_cfd.yaml', GETDATE(), 5, '8:108883674123a51e55ffdf8c5ee98333', 'createTable tableName=check_box_cfd', 'create ''check_box_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-drop_down_cfd.yaml::7.17.0-drop_down_cfd::omp
-- create 'drop_down_cfd' table
CREATE TABLE drop_down_cfd (id numeric(19, 0) NOT NULL, is_multi_select bit)
GO

ALTER TABLE drop_down_cfd ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cfd', 'omp', 'create/changelog-7.17.0-drop_down_cfd.yaml', GETDATE(), 6, '8:683255fde6bb3569ab78618beeb30598', 'createTable tableName=drop_down_cfd; addPrimaryKey tableName=drop_down_cfd', 'create ''drop_down_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-ext_profile.yaml::7.17.0-ext_profile::omp
-- create 'ext_profile' table
CREATE TABLE ext_profile (id numeric(19, 0) NOT NULL, external_view_url nvarchar(2083), system_uri nvarchar(255), external_id nvarchar(255), external_edit_url nvarchar(2083))
GO

ALTER TABLE ext_profile ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_profile', 'omp', 'create/changelog-7.17.0-ext_profile.yaml', GETDATE(), 7, '8:91dbfe3be5e36159e8edc65bc42e3d81', 'createTable tableName=ext_profile; addPrimaryKey tableName=ext_profile', 'create ''ext_profile'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-ext_profile.yaml::7.17.0-ext_profile-constraints::omp
-- create 'ext_profile' constraints
ALTER TABLE ext_profile ADD UNIQUE (system_uri, external_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_profile-constraints', 'omp', 'create/changelog-7.17.0-ext_profile.yaml', GETDATE(), 8, '8:c48766db440f5790f5f1e0be0f0ead7d', 'addUniqueConstraint tableName=ext_profile', 'create ''ext_profile'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-ext_service_item.yaml::7.17.0-ext_service_item::omp
-- create 'ext_service_item' table
CREATE TABLE ext_service_item (id numeric(19, 0) NOT NULL, external_view_url nvarchar(2083), system_uri nvarchar(256), external_id nvarchar(256), external_edit_url nvarchar(2083))
GO

ALTER TABLE ext_service_item ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_service_item', 'omp', 'create/changelog-7.17.0-ext_service_item.yaml', GETDATE(), 9, '8:37b233076878e6ac1e3673849deabb27', 'createTable tableName=ext_service_item; addPrimaryKey tableName=ext_service_item', 'create ''ext_service_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-ext_service_item.yaml::7.17.0-ext_service_item-constraints::omp
-- create 'ext_service_item' constraints
ALTER TABLE ext_service_item ADD UNIQUE (system_uri, external_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_service_item-constraints', 'omp', 'create/changelog-7.17.0-ext_service_item.yaml', GETDATE(), 10, '8:c53fbe90b5e4bbd2abbf12a513d38f8a', 'addUniqueConstraint tableName=ext_service_item', 'create ''ext_service_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-image_url_cf.yaml::7.17.0-image_url_cf::omp
-- create 'image_url_cf' table
CREATE TABLE image_url_cf (id numeric(19, 0) NOT NULL, value nvarchar(2083), CONSTRAINT image_url_cfPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-image_url_cf', 'omp', 'create/changelog-7.17.0-image_url_cf.yaml', GETDATE(), 11, '8:306eef4efcd5cb63fb426aa3204c29da', 'createTable tableName=image_url_cf', 'create ''image_url_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-image_url_cfd.yaml::7.17.0-image_url_cfd::omp
-- create 'image_url_cfd' table
CREATE TABLE image_url_cfd (id numeric(19, 0) NOT NULL, CONSTRAINT image_url_cfdPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-image_url_cfd', 'omp', 'create/changelog-7.17.0-image_url_cfd.yaml', GETDATE(), 12, '8:07246182dae9a49367e2a3cc84a2e011', 'createTable tableName=image_url_cfd', 'create ''image_url_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-interface_configuration.yaml::7.17.0-interface_configuration::omp
-- create 'interface_configuration' table
CREATE TABLE interface_configuration (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, allow_truncate tinyint NOT NULL, auto_create_meta_data tinyint NOT NULL, default_large_icon_url nvarchar(2048), default_small_icon_url nvarchar(2048), delta_since_time_param nvarchar(64), delta_static_parameters nvarchar(2048), full_static_parameters nvarchar(2048), loose_match tinyint NOT NULL, name nvarchar(256) NOT NULL, query_date_format nvarchar(32), response_date_format nvarchar(32), download_images bit CONSTRAINT DF_interface_configuration_download_images DEFAULT 0 NOT NULL, CONSTRAINT interface_conPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-interface_configuration', 'omp', 'create/changelog-7.17.0-interface_configuration.yaml', GETDATE(), 13, '8:60c0b32d266f2b744d7c5f3bb5a82404', 'createTable tableName=interface_configuration', 'create ''interface_configuration'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-modify_relationship_activity.yaml::7.17.0-modify_relationship_activity::omp
-- create 'modify_relationship_activity' table
CREATE TABLE modify_relationship_activity (id numeric(19, 0) NOT NULL)
GO

ALTER TABLE modify_relationship_activity ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-modify_relationship_activity', 'omp', 'create/changelog-7.17.0-modify_relationship_activity.yaml', GETDATE(), 14, '8:83786ed600ccf380ebfe6de2fff558f6', 'createTable tableName=modify_relationship_activity; addPrimaryKey tableName=modify_relationship_activity', 'create ''modify_relationship_activity'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_profile.yaml::7.17.0-service_item_profile::omp
-- create 'service_item_profile' table
CREATE TABLE service_item_profile (service_item_owners_id bigint, profile_id bigint, owners_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_profile', 'omp', 'create/changelog-7.17.0-service_item_profile.yaml', GETDATE(), 15, '8:386462be000d94d5150843c4ce8090eb', 'createTable tableName=service_item_profile', 'create ''service_item_profile'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-tag.yaml::7.17.0-tag::omp
-- create 'tag' table
CREATE TABLE tag (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, edited_by_id numeric(19, 0), edited_date smalldatetime, title varchar(16) NOT NULL, CONSTRAINT tag_PK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-tag', 'omp', 'create/changelog-7.17.0-tag.yaml', GETDATE(), 16, '8:a38d52921faeab2e9fb679cfd4b14615', 'createTable tableName=tag', 'create ''tag'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-tag.yaml::7.17.0-tag-indices::omp
-- create 'tag' indices
CREATE NONCLUSTERED INDEX tag_title_idx ON tag(title)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-tag-indices', 'omp', 'create/changelog-7.17.0-tag.yaml', GETDATE(), 17, '8:4edd892e0e4056cddd3e7452a2424ed3', 'createIndex indexName=tag_title_idx, tableName=tag', 'create ''tag'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-text_area_cf.yaml::7.17.0-text_area_cf::omp
-- create 'text_area_cf' table
CREATE TABLE text_area_cf (id numeric(19, 0) NOT NULL, value nvarchar(4000), CONSTRAINT text_area_cfPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_area_cf', 'omp', 'create/changelog-7.17.0-text_area_cf.yaml', GETDATE(), 18, '8:d0fd1c986e9e12b695cfaf60b920e6a3', 'createTable tableName=text_area_cf', 'create ''text_area_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-text_area_cfd.yaml::7.17.0-text_area_cfd::omp
-- create 'text_area_cfd' table
CREATE TABLE text_area_cfd (id numeric(19, 0) NOT NULL, CONSTRAINT text_area_cfdPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_area_cfd', 'omp', 'create/changelog-7.17.0-text_area_cfd.yaml', GETDATE(), 19, '8:31d1cb3310af00adfe2529d9af6fbe44', 'createTable tableName=text_area_cfd', 'create ''text_area_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-text_cf.yaml::7.17.0-text_cf::omp
-- create 'text_cf' table
CREATE TABLE text_cf (id numeric(19, 0) NOT NULL, value nvarchar(256))
GO

ALTER TABLE text_cf ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_cf', 'omp', 'create/changelog-7.17.0-text_cf.yaml', GETDATE(), 20, '8:89da28292db5f1d540544e88c0e82075', 'createTable tableName=text_cf; addPrimaryKey tableName=text_cf', 'create ''text_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-text_cfd.yaml::7.17.0-text_cfd::omp
-- create 'text_cfd' table
CREATE TABLE text_cfd (id numeric(19, 0) NOT NULL)
GO

ALTER TABLE text_cfd ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_cfd', 'omp', 'create/changelog-7.17.0-text_cfd.yaml', GETDATE(), 21, '8:02e39e3e26fe2c278e5d69aad2037e03', 'createTable tableName=text_cfd; addPrimaryKey tableName=text_cfd', 'create ''text_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-u_domain_preferences.yaml::7.17.0-u_domain_preferences::omp
-- create 'u_domain_preferences' table
CREATE TABLE u_domain_preferences (preferences numeric(19, 0), preferences_idx nvarchar(255), preferences_elt nvarchar(255) NOT NULL)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-u_domain_preferences', 'omp', 'create/changelog-7.17.0-u_domain_preferences.yaml', GETDATE(), 22, '8:e3c148fd1750cc37ebf768f1230e1013', 'createTable tableName=u_domain_preferences', 'create ''u_domain_preferences'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-profile.yaml::7.17.0-profile::omp
-- create 'profile' table
CREATE TABLE profile (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_date datetime NOT NULL, created_by_id numeric(19, 0), bio nvarchar(1000), edited_date datetime, edited_by_id numeric(19, 0), username nvarchar(256), email nvarchar(256), avatar_id numeric(19, 0), display_name nvarchar(256), uuid nvarchar(255), user_roles varchar(255))
GO

ALTER TABLE profile ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-profile', 'omp', 'create/changelog-7.17.0-profile.yaml', GETDATE(), 23, '8:08ccc1e7cda438a272817773bf57dca9', 'createTable tableName=profile; addPrimaryKey tableName=profile', 'create ''profile'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-profile.yaml::7.17.0-profile-constraints::omp
-- create 'profile' constraints
ALTER TABLE profile ADD UNIQUE (username)
GO

ALTER TABLE profile ADD CONSTRAINT FKED8E89A9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE profile ADD CONSTRAINT FKED8E89A97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE profile ADD CONSTRAINT FKED8E89A961C3343D FOREIGN KEY (avatar_id) REFERENCES avatar (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-profile-constraints', 'omp', 'create/changelog-7.17.0-profile.yaml', GETDATE(), 24, '8:243d71826c7080ccb2be52d9b10e5c1e', 'addUniqueConstraint tableName=profile; addForeignKeyConstraint baseTableName=profile, constraintName=FKED8E89A9E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=profile, constraintName=FKED8E89A97666C6D2, referencedTable...', 'create ''profile'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-profile.yaml::7.17.0-profile-indices::omp
-- create 'profile' indices
CREATE UNIQUE NONCLUSTERED INDEX profile_uuid_idx ON profile(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-profile-indices', 'omp', 'create/changelog-7.17.0-profile.yaml', GETDATE(), 25, '8:d058a81fb5aa05cab59dddfde7bf15d0', 'createIndex indexName=profile_uuid_idx, tableName=profile', 'create ''profile'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-avatar-fk.yaml::7.17.0-avatar-constraints::omp
-- create 'avatar' constraints
ALTER TABLE avatar ADD CONSTRAINT FKAC32C1597666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE avatar ADD CONSTRAINT FKAC32C159E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-avatar-constraints', 'omp', 'create/changelog-7.17.0-avatar-fk.yaml', GETDATE(), 26, '8:29580d80bb7bb7eb898e43fcfcfcb476', 'addForeignKeyConstraint baseTableName=avatar, constraintName=FKAC32C1597666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=avatar, constraintName=FKAC32C159E31CB353, referencedTableName=profile', 'create ''avatar'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-application_configuration.yaml::7.17.0-application_configuration::omp
-- create 'application_configuration' table
CREATE TABLE application_configuration (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, edited_by_id numeric(19, 0), edited_date smalldatetime, code varchar(250) NOT NULL, value varchar(2000), title varchar(250) NOT NULL, description varchar(2000), type varchar(250) NOT NULL, group_name varchar(250) NOT NULL, sub_group_name varchar(250), mutable bit NOT NULL, sub_group_order bigint, help varchar(2000), CONSTRAINT application_configurationPK PRIMARY KEY (id), UNIQUE (code))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-application_configuration', 'omp', 'create/changelog-7.17.0-application_configuration.yaml', GETDATE(), 27, '8:52ee18738cda4ff189f5f24d57999a0a', 'createTable tableName=application_configuration', 'create ''application_configuration'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-application_configuration.yaml::7.17.0-application_configuration-constraints::omp
-- create 'application_configuration' constraints
ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C0477666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C047E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-application_configuration-constraints', 'omp', 'create/changelog-7.17.0-application_configuration.yaml', GETDATE(), 28, '8:35f730727e99599020aa22c3fdf842eb', 'addForeignKeyConstraint baseTableName=application_configuration, constraintName=FKFC9C0477666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=application_configuration, constraintName=FKFC9C047E31CB353, referencedTableName=...', 'create ''application_configuration'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-application_configuration.yaml::7.17.0-application_configuration-indices::omp
-- create 'application_configuration' indices
CREATE NONCLUSTERED INDEX FKFC9C0477666C6D2 ON application_configuration(created_by_id)
GO

CREATE NONCLUSTERED INDEX FKFC9C047E31CB353 ON application_configuration(edited_by_id)
GO

CREATE NONCLUSTERED INDEX app_config_group_name_idx ON application_configuration(group_name)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-application_configuration-indices', 'omp', 'create/changelog-7.17.0-application_configuration.yaml', GETDATE(), 29, '8:2fc975f2cc5ff4c0ea4eb2f7b4bb8cf2', 'createIndex indexName=FKFC9C0477666C6D2, tableName=application_configuration; createIndex indexName=FKFC9C047E31CB353, tableName=application_configuration; createIndex indexName=app_config_group_name_idx, tableName=application_configuration', 'create ''application_configuration'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-category.yaml::7.17.0-category::omp
-- create 'category' table
CREATE TABLE category (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, title nvarchar(50) NOT NULL, description nvarchar(250), created_by_id numeric(19, 0), edited_date datetime, uuid nvarchar(255))
GO

ALTER TABLE category ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-category', 'omp', 'create/changelog-7.17.0-category.yaml', GETDATE(), 30, '8:ff9fa3127979b522537de080a464522d', 'createTable tableName=category; addPrimaryKey tableName=category', 'create ''category'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-category.yaml::7.17.0-category-constraints::omp
-- create 'category' constraints
ALTER TABLE category ADD CONSTRAINT FK302BCFE7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE category ADD CONSTRAINT FK302BCFEE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-category-constraints', 'omp', 'create/changelog-7.17.0-category.yaml', GETDATE(), 31, '8:af20b5d4f27385f8db710863d375b279', 'addForeignKeyConstraint baseTableName=category, constraintName=FK302BCFE7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=category, constraintName=FK302BCFEE31CB353, referencedTableName=profile', 'create ''category'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-category.yaml::7.17.0-category-indices::omp
-- create 'category' indices
CREATE UNIQUE NONCLUSTERED INDEX category_uuid_idx ON category(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-category-indices', 'omp', 'create/changelog-7.17.0-category.yaml', GETDATE(), 32, '8:cc127d21beaa0e77e7775aacc4d4d841', 'createIndex indexName=category_uuid_idx, tableName=category', 'create ''category'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-contact_type.yaml::7.17.0-contact_type::omp
-- create 'contact_type' table
CREATE TABLE contact_type (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, edited_by_id numeric(19, 0), edited_date smalldatetime, required bit NOT NULL, title varchar(50) NOT NULL, CONSTRAINT contact_typePK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact_type', 'omp', 'create/changelog-7.17.0-contact_type.yaml', GETDATE(), 33, '8:4fa8e5d6047eef33e08503244ce4af10', 'createTable tableName=contact_type', 'create ''contact_type'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-contact_type.yaml::7.17.0-contact_type-constraints::omp
-- create 'contact_type' constraints
ALTER TABLE contact_type ADD CONSTRAINT FK4C2BB7F97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE contact_type ADD CONSTRAINT FK4C2BB7F9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact_type-constraints', 'omp', 'create/changelog-7.17.0-contact_type.yaml', GETDATE(), 34, '8:2a4c5e940aa9d03cdb3955f8bc83330a', 'addForeignKeyConstraint baseTableName=contact_type, constraintName=FK4C2BB7F97666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=contact_type, constraintName=FK4C2BB7F9E31CB353, referencedTableName=profile', 'create ''contact_type'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-contact_type.yaml::7.17.0-contact_type-indices::omp
-- create 'contact_type' indices
CREATE NONCLUSTERED INDEX FK4C2BB7F97666C6D2 ON contact_type(created_by_id)
GO

CREATE NONCLUSTERED INDEX FK4C2BB7F9E31CB353 ON contact_type(edited_by_id)
GO

CREATE UNIQUE NONCLUSTERED INDEX title_unique_1389723125532 ON contact_type(title)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact_type-indices', 'omp', 'create/changelog-7.17.0-contact_type.yaml', GETDATE(), 35, '8:27f1149bc3c5b3223093b61f6dcd6f7a', 'createIndex indexName=FK4C2BB7F97666C6D2, tableName=contact_type; createIndex indexName=FK4C2BB7F9E31CB353, tableName=contact_type; createIndex indexName=title_unique_1389723125532, tableName=contact_type', 'create ''contact_type'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field_definition.yaml::7.17.0-custom_field_definition::omp
-- create 'custom_field_definition' table
CREATE TABLE custom_field_definition (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_date datetime, tooltip nvarchar(50), created_by_id numeric(19, 0), edited_date datetime, edited_by_id numeric(19, 0), label nvarchar(50) NOT NULL, description nvarchar(250), is_required tinyint NOT NULL, name nvarchar(50) NOT NULL, style_type nvarchar(255) NOT NULL, uuid nvarchar(255), section varchar(255), all_types bit CONSTRAINT DF_custom_field_definition_all_types DEFAULT 0 NOT NULL, is_permanent bit)
GO

ALTER TABLE custom_field_definition ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition', 'omp', 'create/changelog-7.17.0-custom_field_definition.yaml', GETDATE(), 36, '8:34547e18ea6a0411ad414d61eb7559fe', 'createTable tableName=custom_field_definition; addPrimaryKey tableName=custom_field_definition', 'create ''custom_field_definition'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field_definition.yaml::7.17.0-custom_field_definition-constraints::omp
-- create 'custom_field_definition' constraints
ALTER TABLE custom_field_definition ADD CONSTRAINT FK150F70C6E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE custom_field_definition ADD CONSTRAINT FK150F70C67666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition-constraints', 'omp', 'create/changelog-7.17.0-custom_field_definition.yaml', GETDATE(), 37, '8:839294c66627c7010940bb3c169d2bd7', 'addForeignKeyConstraint baseTableName=custom_field_definition, constraintName=FK150F70C6E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=custom_field_definition, constraintName=FK150F70C67666C6D2, referencedTableName=pr...', 'create ''custom_field_definition'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field_definition.yaml::7.17.0-custom_field_definition-indices::omp
-- create 'custom_field_definition' indices
CREATE UNIQUE NONCLUSTERED INDEX cfd_uuid_idx ON custom_field_definition(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition-indices', 'omp', 'create/changelog-7.17.0-custom_field_definition.yaml', GETDATE(), 38, '8:ecf918813b35d797af8b4becc32f7759', 'createIndex indexName=cfd_uuid_idx, tableName=custom_field_definition', 'create ''custom_field_definition'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-images.yaml::7.17.0-images::omp
-- create 'images' table
CREATE TABLE images (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_date datetime, is_default tinyint NOT NULL, type nvarchar(255) NOT NULL, created_by_id numeric(19, 0), content_type nvarchar(255), bytes image NOT NULL, edited_date datetime, edited_by_id numeric(19, 0), image_size float(53))
GO

ALTER TABLE images ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-images', 'omp', 'create/changelog-7.17.0-images.yaml', GETDATE(), 39, '8:097e4b6d2677dcd91a936f3669d8702b', 'createTable tableName=images; addPrimaryKey tableName=images', 'create ''images'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-images.yaml::7.17.0-images-constraints::omp
-- create 'images' constraints
ALTER TABLE images ADD CONSTRAINT FKB95A8278E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE images ADD CONSTRAINT FKB95A82787666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-images-constraints', 'omp', 'create/changelog-7.17.0-images.yaml', GETDATE(), 40, '8:69ed3a2a2dc214beae6c59daa82db08f', 'addForeignKeyConstraint baseTableName=images, constraintName=FKB95A8278E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=images, constraintName=FKB95A82787666C6D2, referencedTableName=profile', 'create ''images'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_action.yaml::7.17.0-intent_action::omp
-- create 'intent_action' table
CREATE TABLE intent_action (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, description varchar(256), edited_by_id numeric(19, 0), edited_date smalldatetime, title varchar(256) NOT NULL, uuid varchar(255), CONSTRAINT intent_actionPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_action', 'omp', 'create/changelog-7.17.0-intent_action.yaml', GETDATE(), 41, '8:6711e6d8173db281131e38ac9b46f7ec', 'createTable tableName=intent_action', 'create ''intent_action'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_action.yaml::7.17.0-intent_action-constraints::omp
-- create 'intent_action' constraints
ALTER TABLE intent_action ADD CONSTRAINT FKEBCDD397666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE intent_action ADD CONSTRAINT FKEBCDD39E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_action-constraints', 'omp', 'create/changelog-7.17.0-intent_action.yaml', GETDATE(), 42, '8:4312ce60d27dcb8ce8e03ad25afaf23e', 'addForeignKeyConstraint baseTableName=intent_action, constraintName=FKEBCDD397666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=intent_action, constraintName=FKEBCDD39E31CB353, referencedTableName=profile', 'create ''intent_action'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_action.yaml::7.17.0-intent_action-indices::omp
-- create 'intent_action' indices
CREATE NONCLUSTERED INDEX FKEBCDD397666C6D2 ON intent_action(created_by_id)
GO

CREATE NONCLUSTERED INDEX FKEBCDD39E31CB353 ON intent_action(edited_by_id)
GO

CREATE UNIQUE NONCLUSTERED INDEX uuid_unique_1366321689429 ON intent_action(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_action-indices', 'omp', 'create/changelog-7.17.0-intent_action.yaml', GETDATE(), 43, '8:60f68376d431f715c54cd4726804b5a8', 'createIndex indexName=FKEBCDD397666C6D2, tableName=intent_action; createIndex indexName=FKEBCDD39E31CB353, tableName=intent_action; createIndex indexName=uuid_unique_1366321689429, tableName=intent_action', 'create ''intent_action'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_data_type.yaml::7.17.0-intent_data_type::omp
-- create 'intent_data_type' table
CREATE TABLE intent_data_type (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, description varchar(256), edited_by_id numeric(19, 0), edited_date smalldatetime, title varchar(256) NOT NULL, uuid varchar(255), CONSTRAINT intent_data_tPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_data_type', 'omp', 'create/changelog-7.17.0-intent_data_type.yaml', GETDATE(), 44, '8:afbb0fdab68bced7cce0d8fa31d34488', 'createTable tableName=intent_data_type', 'create ''intent_data_type'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_data_type.yaml::7.17.0-intent_data_type-constraints::omp
-- create 'intent_data_type' constraints
ALTER TABLE intent_data_type ADD CONSTRAINT FKEADB30CC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE intent_data_type ADD CONSTRAINT FKEADB30CCE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_data_type-constraints', 'omp', 'create/changelog-7.17.0-intent_data_type.yaml', GETDATE(), 45, '8:a92ba5702785927628be2f1a56826f6a', 'addForeignKeyConstraint baseTableName=intent_data_type, constraintName=FKEADB30CC7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=intent_data_type, constraintName=FKEADB30CCE31CB353, referencedTableName=profile', 'create ''intent_data_type'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_data_type.yaml::7.17.0-intent_data_type-indices::omp
-- create 'intent_data_type' indices
CREATE NONCLUSTERED INDEX FKEADB30CC7666C6D2 ON intent_data_type(created_by_id)
GO

CREATE NONCLUSTERED INDEX FKEADB30CCE31CB353 ON intent_data_type(edited_by_id)
GO

CREATE UNIQUE NONCLUSTERED INDEX uuid_unique_1366398847848 ON intent_data_type(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_data_type-indices', 'omp', 'create/changelog-7.17.0-intent_data_type.yaml', GETDATE(), 46, '8:f6ee8d4fcbfc6dca5eb344b738d7c124', 'createIndex indexName=FKEADB30CC7666C6D2, tableName=intent_data_type; createIndex indexName=FKEADB30CCE31CB353, tableName=intent_data_type; createIndex indexName=uuid_unique_1366398847848, tableName=intent_data_type', 'create ''intent_data_type'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_direction.yaml::7.17.0-intent_direction::omp
-- create 'intent_direction' table
CREATE TABLE intent_direction (created_by_id numeric(19, 0), created_date smalldatetime, description varchar(250), edited_by_id numeric(19, 0), edited_date smalldatetime, id bigint IDENTITY (1, 1) NOT NULL, title varchar(7) NOT NULL, uuid varchar(255), version bigint NOT NULL, CONSTRAINT intent_directPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_direction', 'omp', 'create/changelog-7.17.0-intent_direction.yaml', GETDATE(), 47, '8:f733d5142e5ec8ff072cdcaea5c89537', 'createTable tableName=intent_direction', 'create ''intent_direction'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_direction.yaml::7.17.0-intent_direction-constraints::omp
-- create 'intent_direction' constraints
ALTER TABLE intent_direction ADD CONSTRAINT FKC723A59C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE intent_direction ADD CONSTRAINT FKC723A59CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_direction-constraints', 'omp', 'create/changelog-7.17.0-intent_direction.yaml', GETDATE(), 48, '8:467b4a8f7c9277237a9bfe2f82e51a90', 'addForeignKeyConstraint baseTableName=intent_direction, constraintName=FKC723A59C7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=intent_direction, constraintName=FKC723A59CE31CB353, referencedTableName=profile', 'create ''intent_direction'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent_direction.yaml::7.17.0-intent_direction-indices::omp
-- create 'intent_direction' indices
CREATE NONCLUSTERED INDEX FKC723A59C7666C6D2 ON intent_direction(created_by_id)
GO

CREATE NONCLUSTERED INDEX FKC723A59CE31CB353 ON intent_direction(edited_by_id)
GO

CREATE UNIQUE NONCLUSTERED INDEX title_unique_1366386256451 ON intent_direction(title)
GO

CREATE UNIQUE NONCLUSTERED INDEX uuid_unique_1366386256451 ON intent_direction(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_direction-indices', 'omp', 'create/changelog-7.17.0-intent_direction.yaml', GETDATE(), 49, '8:a620ed929b1b47f5b3f275f8cb4aa9e3', 'createIndex indexName=FKC723A59C7666C6D2, tableName=intent_direction; createIndex indexName=FKC723A59CE31CB353, tableName=intent_direction; createIndex indexName=title_unique_1366386256451, tableName=intent_direction; createIndex indexName=uuid_un...', 'create ''intent_direction'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_properties.yaml::7.17.0-owf_properties::omp
-- create 'owf_properties' table
CREATE TABLE owf_properties (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, visible_in_launch tinyint NOT NULL, edited_by_id numeric(19, 0), created_date datetime, singleton tinyint NOT NULL, created_by_id numeric(19, 0), edited_date datetime, background bit CONSTRAINT DF_owf_properties_background DEFAULT 0 NOT NULL, height bigint, width bigint, owf_widget_type varchar(255) CONSTRAINT DF_owf_properties_owf_widget_type DEFAULT 'standard' NOT NULL, universal_name varchar(255), stack_context varchar(200), stack_descriptor nvarchar(MAX), descriptor_url varchar(2083), mobile_ready bit CONSTRAINT DF_owf_properties_mobile_ready DEFAULT 0 NOT NULL)
GO

ALTER TABLE owf_properties ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties', 'omp', 'create/changelog-7.17.0-owf_properties.yaml', GETDATE(), 50, '8:d5aaaa70c6a408e2d5d763ee5b4f6a81', 'createTable tableName=owf_properties; addPrimaryKey tableName=owf_properties', 'create ''owf_properties'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_properties.yaml::7.17.0-owf_properties-constraints::omp
-- create 'owf_properties' constraints
ALTER TABLE owf_properties ADD CONSTRAINT FKE88638947666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE owf_properties ADD CONSTRAINT FKE8863894E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties-constraints', 'omp', 'create/changelog-7.17.0-owf_properties.yaml', GETDATE(), 51, '8:778512247bbde04d30acaccc347f1625', 'addForeignKeyConstraint baseTableName=owf_properties, constraintName=FKE88638947666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=owf_properties, constraintName=FKE8863894E31CB353, referencedTableName=profile', 'create ''owf_properties'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_widget_types.yaml::7.17.0-owf_widget_types::omp
-- create 'owf_widget_types' table
CREATE TABLE owf_widget_types (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, description varchar(255) NOT NULL, edited_by_id numeric(19, 0), edited_date smalldatetime, title varchar(256) NOT NULL, uuid varchar(255), CONSTRAINT owf_widget_typePK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_widget_types', 'omp', 'create/changelog-7.17.0-owf_widget_types.yaml', GETDATE(), 52, '8:5c6f02f97f945b0a83a3e0127abc794e', 'createTable tableName=owf_widget_types', 'create ''owf_widget_types'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_widget_types.yaml::7.17.0-owf_widget_types-constraints::omp
-- create 'owf_widget_types' constraints
ALTER TABLE owf_widget_types ADD CONSTRAINT FK6AB6A9DF7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE owf_widget_types ADD CONSTRAINT FK6AB6A9DFE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_widget_types-constraints', 'omp', 'create/changelog-7.17.0-owf_widget_types.yaml', GETDATE(), 53, '8:71ddfb566810644b566deb0c89bb73aa', 'addForeignKeyConstraint baseTableName=owf_widget_types, constraintName=FK6AB6A9DF7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=owf_widget_types, constraintName=FK6AB6A9DFE31CB353, referencedTableName=profile', 'create ''owf_widget_types'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_widget_types.yaml::7.17.0-owf_widget_types-indices::omp
-- create 'owf_widget_types' indices
CREATE UNIQUE NONCLUSTERED INDEX uuid_unique_1366666109930 ON owf_widget_types(uuid)
GO

CREATE NONCLUSTERED INDEX FK6AB6A9DF7666C6D2 ON owf_widget_types(created_by_id)
GO

CREATE NONCLUSTERED INDEX FK6AB6A9DFE31CB353 ON owf_widget_types(edited_by_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_widget_types-indices', 'omp', 'create/changelog-7.17.0-owf_widget_types.yaml', GETDATE(), 54, '8:348c48ae2e1ff0d3a987958b8cde0402', 'createIndex indexName=uuid_unique_1366666109930, tableName=owf_widget_types; createIndex indexName=FK6AB6A9DF7666C6D2, tableName=owf_widget_types; createIndex indexName=FK6AB6A9DFE31CB353, tableName=owf_widget_types', 'create ''owf_widget_types'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_justification.yaml::7.17.0-rejection_justification::omp
-- create 'rejection_justification' table
CREATE TABLE rejection_justification (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, title nvarchar(50) NOT NULL, description nvarchar(250), created_by_id numeric(19, 0), edited_date datetime)
GO

ALTER TABLE rejection_justification ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_justification', 'omp', 'create/changelog-7.17.0-rejection_justification.yaml', GETDATE(), 55, '8:17d1e364e6cac2428ca1c21ac67afa21', 'createTable tableName=rejection_justification; addPrimaryKey tableName=rejection_justification', 'create ''rejection_justification'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_justification.yaml::7.17.0-rejection_justification-constraints::omp
-- create 'rejection_justification' constraints
ALTER TABLE rejection_justification ADD CONSTRAINT FK12B0A53C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE rejection_justification ADD CONSTRAINT FK12B0A53CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_justification-constraints', 'omp', 'create/changelog-7.17.0-rejection_justification.yaml', GETDATE(), 56, '8:965782a0b6fecac7ee36cd7b07210990', 'addForeignKeyConstraint baseTableName=rejection_justification, constraintName=FK12B0A53C7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=rejection_justification, constraintName=FK12B0A53CE31CB353, referencedTableName=pr...', 'create ''rejection_justification'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-score_card_item.yaml::7.17.0-score_card_item::omp
-- create 'score_card_item' table
CREATE TABLE score_card_item (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, created_by_id numeric(19, 0), created_date datetime, description varchar(500) NOT NULL, edited_by_id numeric(19, 0), edited_date datetime, question varchar(250) NOT NULL, image varchar(255), show_on_listing bit, CONSTRAINT sc_itemPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-score_card_item', 'omp', 'create/changelog-7.17.0-score_card_item.yaml', GETDATE(), 57, '8:aaac4fe8ca33b6d193b8b87386ff1652', 'createTable tableName=score_card_item', 'create ''score_card_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-score_card_item.yaml::7.17.0-score_card_item-constraints::omp
-- create 'score_card_item' constraints
ALTER TABLE score_card_item ADD CONSTRAINT FKE51CCD757666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE score_card_item ADD CONSTRAINT FKE51CCD75E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-score_card_item-constraints', 'omp', 'create/changelog-7.17.0-score_card_item.yaml', GETDATE(), 58, '8:e5db69ed465cc0b99125168ba8d6cc39', 'addForeignKeyConstraint baseTableName=score_card_item, constraintName=FKE51CCD757666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=score_card_item, constraintName=FKE51CCD75E31CB353, referencedTableName=profile', 'create ''score_card_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-score_card_item.yaml::7.17.0-score_card_item-indices::omp
-- create 'score_card_item' indices
CREATE NONCLUSTERED INDEX FKE51CCD757666C6D2 ON score_card_item(created_by_id)
GO

CREATE NONCLUSTERED INDEX FKE51CCD75E31CB353 ON score_card_item(edited_by_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-score_card_item-indices', 'omp', 'create/changelog-7.17.0-score_card_item.yaml', GETDATE(), 59, '8:b0d9feae89e32e975cad2d7984cb7bdb', 'createIndex indexName=FKE51CCD757666C6D2, tableName=score_card_item; createIndex indexName=FKE51CCD75E31CB353, tableName=score_card_item', 'create ''score_card_item'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-state.yaml::7.17.0-state::omp
-- create 'state' table
CREATE TABLE state (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, is_published tinyint NOT NULL, title nvarchar(50) NOT NULL, description nvarchar(250), created_by_id numeric(19, 0), edited_date datetime, uuid nvarchar(255))
GO

ALTER TABLE state ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-state', 'omp', 'create/changelog-7.17.0-state.yaml', GETDATE(), 60, '8:82ce572669b0098a1afe70926283590b', 'createTable tableName=state; addPrimaryKey tableName=state', 'create ''state'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-state.yaml::7.17.0-state-constraints::omp
-- create 'state' constraints
ALTER TABLE state ADD CONSTRAINT FK68AC4917666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE state ADD CONSTRAINT FK68AC491E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-state-constraints', 'omp', 'create/changelog-7.17.0-state.yaml', GETDATE(), 61, '8:68ff9ca5066bd6f60c0f13f00dd20f8d', 'addForeignKeyConstraint baseTableName=state, constraintName=FK68AC4917666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=state, constraintName=FK68AC491E31CB353, referencedTableName=profile', 'create ''state'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-state.yaml::7.17.0-state-indices::omp
-- create 'state' indices
CREATE UNIQUE NONCLUSTERED INDEX state_uuid_idx ON state(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-state-indices', 'omp', 'create/changelog-7.17.0-state.yaml', GETDATE(), 62, '8:c14b14f3d9ba4c78874eea9d57d0348a', 'createIndex indexName=state_uuid_idx, tableName=state', 'create ''state'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-text.yaml::7.17.0-text::omp
-- create 'text' table
CREATE TABLE text (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, value nvarchar(250), created_by_id numeric(19, 0), read_only tinyint NOT NULL, name nvarchar(50) NOT NULL, edited_date datetime)
GO

ALTER TABLE text ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text', 'omp', 'create/changelog-7.17.0-text.yaml', GETDATE(), 63, '8:da843f141a382ce3588ba5430ee78875', 'createTable tableName=text; addPrimaryKey tableName=text', 'create ''text'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-text.yaml::7.17.0-text-constraints::omp
-- create 'text' constraints
ALTER TABLE text ADD UNIQUE (name)
GO

ALTER TABLE text ADD CONSTRAINT FK36452D7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE text ADD CONSTRAINT FK36452DE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text-constraints', 'omp', 'create/changelog-7.17.0-text.yaml', GETDATE(), 64, '8:62c7b2582039f543616183cfae2cf076', 'addUniqueConstraint tableName=text; addForeignKeyConstraint baseTableName=text, constraintName=FK36452D7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=text, constraintName=FK36452DE31CB353, referencedTableName=profile', 'create ''text'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-user_account.yaml::7.17.0-user_account::omp
-- create 'user_account' table
CREATE TABLE user_account (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, username nvarchar(250) NOT NULL, created_by_id numeric(19, 0), last_login datetime, edited_date datetime)
GO

ALTER TABLE user_account ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-user_account', 'omp', 'create/changelog-7.17.0-user_account.yaml', GETDATE(), 65, '8:b82313852939950d8ee251705e2da433', 'createTable tableName=user_account; addPrimaryKey tableName=user_account', 'create ''user_account'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-user_account.yaml::7.17.0-user_account-constraints::omp
-- create 'user_account' constraints
ALTER TABLE user_account ADD UNIQUE (username)
GO

ALTER TABLE user_account ADD CONSTRAINT FK14C321B97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE user_account ADD CONSTRAINT FK14C321B9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-user_account-constraints', 'omp', 'create/changelog-7.17.0-user_account.yaml', GETDATE(), 66, '8:32eed90e116f5ff7751d0acce94195c8', 'addUniqueConstraint tableName=user_account; addForeignKeyConstraint baseTableName=user_account, constraintName=FK14C321B97666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=user_account, constraintName=FK14C321B9E31CB353, ...', 'create ''user_account'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-u_domain.yaml::7.17.0-u_domain::omp
-- create 'u_domain' table
CREATE TABLE U_DOMAIN (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, username nvarchar(255) NOT NULL, created_by_id numeric(19, 0), edited_date datetime)
GO

ALTER TABLE u_domain ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-u_domain', 'omp', 'create/changelog-7.17.0-u_domain.yaml', GETDATE(), 67, '8:648511a001c31a1f5129aae360c37344', 'createTable tableName=U_DOMAIN; addPrimaryKey tableName=u_domain', 'create ''u_domain'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-u_domain.yaml::7.17.0-u_domain-constraints::omp
-- create 'u_domain' constraints
ALTER TABLE U_DOMAIN ADD CONSTRAINT FK97BAABEE7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE U_DOMAIN ADD CONSTRAINT FK97BAABEEE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-u_domain-constraints', 'omp', 'create/changelog-7.17.0-u_domain.yaml', GETDATE(), 68, '8:0df4a8a52d52420f0f96161c30aa4771', 'addForeignKeyConstraint baseTableName=U_DOMAIN, constraintName=FK97BAABEE7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=U_DOMAIN, constraintName=FK97BAABEEE31CB353, referencedTableName=profile', 'create ''u_domain'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field::omp
-- create 'custom_field' table
CREATE TABLE custom_field (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, custom_field_definition_id numeric(19, 0) NOT NULL, created_by_id numeric(19, 0), edited_date datetime)
GO

ALTER TABLE custom_field ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field', 'omp', 'create/changelog-7.17.0-custom_field.yaml', GETDATE(), 69, '8:c0cc3e88b5ec686eaba5305bf65464e5', 'createTable tableName=custom_field; addPrimaryKey tableName=custom_field', 'create ''custom_field'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field-constraints::omp
-- create 'custom_field' constraints
ALTER TABLE custom_field ADD CONSTRAINT FK2ACD76AC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE custom_field ADD CONSTRAINT FK2ACD76ACE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE custom_field ADD CONSTRAINT FK2ACD76AC6F62C9ED FOREIGN KEY (custom_field_definition_id) REFERENCES custom_field_definition (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field-constraints', 'omp', 'create/changelog-7.17.0-custom_field.yaml', GETDATE(), 70, '8:7b6a15576049afc2a0784f1dd7dc6a24', 'addForeignKeyConstraint baseTableName=custom_field, constraintName=FK2ACD76AC7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=custom_field, constraintName=FK2ACD76ACE31CB353, referencedTableName=profile; addForeignKeyCo...', 'create ''custom_field'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field-indices::omp
-- create 'custom_field' indices
CREATE NONCLUSTERED INDEX cf_cfd_idx ON custom_field(custom_field_definition_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field-indices', 'omp', 'create/changelog-7.17.0-custom_field.yaml', GETDATE(), 71, '8:00c5d997fcca7b32ad1ea8ebcf9a49d9', 'createIndex indexName=cf_cfd_idx, tableName=custom_field', 'create ''custom_field'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field::omp
-- create 'service_item_custom_field' table
CREATE TABLE service_item_custom_field (service_item_custom_fields_id numeric(19, 0), custom_field_id numeric(19, 0), custom_fields_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', GETDATE(), 72, '8:ed8d9b99adca3a32f871a0d256d5555a', 'createTable tableName=service_item_custom_field', 'create ''service_item_custom_field'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-constraints::omp
-- create 'service_item_custom_field' constraints
ALTER TABLE service_item_custom_field ADD CONSTRAINT FK46E9894E7B56E054 FOREIGN KEY (custom_field_id) REFERENCES custom_field (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-constraints', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', GETDATE(), 73, '8:b8a17dcf815b2e145efd514d5fdc84d7', 'addForeignKeyConstraint baseTableName=service_item_custom_field, constraintName=FK46E9894E7B56E054, referencedTableName=custom_field', 'create ''service_item_custom_field'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-indices_1::omp
-- create 'service_item_custom_field' indices
CREATE NONCLUSTERED INDEX svc_item_cst_fld_id_idx ON service_item_custom_field(service_item_custom_fields_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-indices_1', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', GETDATE(), 74, '8:aeae086b1fbf38df9180593a672d74b5', 'createIndex indexName=svc_item_cst_fld_id_idx, tableName=service_item_custom_field', 'create ''service_item_custom_field'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-indices_2::omp
-- create 'service_item_custom_field' indices
CREATE NONCLUSTERED INDEX si_cf_cf_id_idx ON service_item_custom_field(custom_field_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-indices_2', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', GETDATE(), 75, '8:67b83331e3f9a419cd92ed60a413f68c', 'createIndex indexName=si_cf_cf_id_idx, tableName=service_item_custom_field', 'create ''service_item_custom_field'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value::omp
-- create 'field_value' table
CREATE TABLE field_value (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, custom_field_definition_id numeric(19, 0) NOT NULL, is_enabled int NOT NULL, display_text nvarchar(255) NOT NULL, created_by_id numeric(19, 0), edited_date datetime, field_values_idx int)
GO

ALTER TABLE field_value ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value', 'omp', 'create/changelog-7.17.0-field_value.yaml', GETDATE(), 76, '8:cae5feebbe4094f53bc13f8f22f14428', 'createTable tableName=field_value; addPrimaryKey tableName=field_value', 'create ''field_value'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value-constraints::omp
-- create 'field_value' constraints
ALTER TABLE field_value ADD CONSTRAINT FK29F571EC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE field_value ADD CONSTRAINT FK29F571ECE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE field_value ADD CONSTRAINT FK29F571ECF1F14D3C FOREIGN KEY (custom_field_definition_id) REFERENCES drop_down_cfd (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value-constraints', 'omp', 'create/changelog-7.17.0-field_value.yaml', GETDATE(), 77, '8:acc6a3f7e4775645057775a1d12ebcea', 'addForeignKeyConstraint baseTableName=field_value, constraintName=FK29F571EC7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=field_value, constraintName=FK29F571ECE31CB353, referencedTableName=profile; addForeignKeyCons...', 'create ''field_value'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value-indices::omp
-- create 'field_value' indices
CREATE NONCLUSTERED INDEX field_value_cfd_idx ON field_value(custom_field_definition_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value-indices', 'omp', 'create/changelog-7.17.0-field_value.yaml', GETDATE(), 78, '8:5015c67c693d7df754d37bbce024fc74', 'createIndex indexName=field_value_cfd_idx, tableName=field_value', 'create ''field_value'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-drop_down_cf.yaml::7.17.0-drop_down_cf::omp
-- create 'drop_down_cf' table
CREATE TABLE drop_down_cf (id numeric(19, 0) NOT NULL, value_id numeric(19, 0))
GO

ALTER TABLE drop_down_cf ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf', 'omp', 'create/changelog-7.17.0-drop_down_cf.yaml', GETDATE(), 79, '8:cf8ea3c48b9fd841fc9aa372b1d15739', 'createTable tableName=drop_down_cf; addPrimaryKey tableName=drop_down_cf', 'create ''drop_down_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-drop_down_cf.yaml::7.17.0-drop_down_cf-constraints::omp
-- create 'drop_down_cf' constraints
ALTER TABLE drop_down_cf ADD CONSTRAINT FK13ADE7D0BC98CEE3 FOREIGN KEY (value_id) REFERENCES field_value (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf-constraints', 'omp', 'create/changelog-7.17.0-drop_down_cf.yaml', GETDATE(), 80, '8:e7fccd29cc0c8123e56a4f71ec3df69a', 'addForeignKeyConstraint baseTableName=drop_down_cf, constraintName=FK13ADE7D0BC98CEE3, referencedTableName=field_value', 'create ''drop_down_cf'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-drop_down_cf_field_value.yaml::7.17.0-drop_down_cf_field_value::omp
-- create 'drop_down_cf_field_value' table
CREATE TABLE drop_down_cf_field_value (drop_down_cf_field_value_id numeric(19, 0), field_value_id numeric(19, 0), field_value_list_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf_field_value', 'omp', 'create/changelog-7.17.0-drop_down_cf_field_value.yaml', GETDATE(), 81, '8:3dd9d09b50d3fefb871b7b4b0381879e', 'createTable tableName=drop_down_cf_field_value', 'create ''drop_down_cf_field_value'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-drop_down_cf_field_value.yaml::7.17.0-drop_down_cf_field_value-constraints::omp
-- create 'drop_down_cf_field_value' constraints
ALTER TABLE drop_down_cf_field_value ADD CONSTRAINT FK2627FFDDA5BD888 FOREIGN KEY (field_value_id) REFERENCES field_value (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf_field_value-constraints', 'omp', 'create/changelog-7.17.0-drop_down_cf_field_value.yaml', GETDATE(), 82, '8:dcb262802d76f5dde8e2d5f35e558104', 'addForeignKeyConstraint baseTableName=drop_down_cf_field_value, constraintName=FK2627FFDDA5BD888, referencedTableName=field_value', 'create ''drop_down_cf_field_value'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-drop_down_cf_field_value.yaml::7.17.0-drop_down_cf_field_value-indices::omp
-- create 'drop_down_cf_field_value' indices
CREATE NONCLUSTERED INDEX FK2627FFDDA5BD888 ON drop_down_cf_field_value(field_value_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf_field_value-indices', 'omp', 'create/changelog-7.17.0-drop_down_cf_field_value.yaml', GETDATE(), 83, '8:959b4322632a98c4021748671cda3e7d', 'createIndex indexName=FK2627FFDDA5BD888, tableName=drop_down_cf_field_value', 'create ''drop_down_cf_field_value'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category::omp
-- create 'service_item_category' table
CREATE TABLE service_item_category (service_item_categories_id bigint, category_id numeric(19, 0), categories_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', GETDATE(), 84, '8:c9976b061921371a1f59705eec934aa2', 'createTable tableName=service_item_category', 'create ''service_item_category'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-constraints::omp
-- create 'service_item_category' constraints
ALTER TABLE service_item_category ADD CONSTRAINT FKECC570A0DA41995D FOREIGN KEY (category_id) REFERENCES category (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-constraints', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', GETDATE(), 85, '8:1f76ff702bbce596cab57c462afbb869', 'addForeignKeyConstraint baseTableName=service_item_category, constraintName=FKECC570A0DA41995D, referencedTableName=category', 'create ''service_item_category'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-indices_1::omp
-- create 'service_item_category' indices
CREATE NONCLUSTERED INDEX svc_item_cat_id_idx ON service_item_category(service_item_categories_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-indices_1', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', GETDATE(), 86, '8:472fb375b06d06f5ef4617630a73cb6e', 'createIndex indexName=svc_item_cat_id_idx, tableName=service_item_category', 'create ''service_item_category'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-indices_2::omp
-- create 'service_item_category' indices
CREATE NONCLUSTERED INDEX si_cat_cat_id_idx ON service_item_category(category_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-indices_2', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', GETDATE(), 87, '8:ac315a8a967a207abdfe2c4bd4188125', 'createIndex indexName=si_cat_cat_id_idx, tableName=service_item_category', 'create ''service_item_category'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-import_task.yaml::7.17.0-import_task::omp
-- create 'import_task' table
CREATE TABLE import_task (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_by_id numeric(19, 0), created_date datetime, cron_exp nvarchar(255), edited_by_id numeric(19, 0), edited_date datetime, enabled tinyint NOT NULL, exec_interval int, extra_url_params nvarchar(512), interface_config_id numeric(19, 0) NOT NULL, last_run_result_id numeric(19, 0), name nvarchar(50) NOT NULL, update_type nvarchar(7) NOT NULL, url nvarchar(255), keystore_pass nvarchar(2048), keystore_path nvarchar(2048), truststore_path nvarchar(2048), CONSTRAINT import_taskPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task', 'omp', 'create/changelog-7.17.0-import_task.yaml', GETDATE(), 88, '8:2c3533ac25224c0d53a3ae0c2d08c82f', 'createTable tableName=import_task', 'create ''import_task'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-import_task_result.yaml::7.17.0-import_task_result::omp
-- create 'import_task_result' table
CREATE TABLE import_task_result (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, message nvarchar(4000) NOT NULL, result tinyint NOT NULL, run_date datetime NOT NULL, task_id numeric(19, 0) NOT NULL, CONSTRAINT import_task_rPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task_result', 'omp', 'create/changelog-7.17.0-import_task_result.yaml', GETDATE(), 89, '8:b225010492e19423c29f069eb786b37e', 'createTable tableName=import_task_result', 'create ''import_task_result'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-import_task_result.yaml::7.17.0-import_task_result-constraints::omp
-- create 'import_task_result' constraints
ALTER TABLE import_task_result ADD CONSTRAINT FK983AC27D11D7F882 FOREIGN KEY (task_id) REFERENCES import_task (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task_result-constraints', 'omp', 'create/changelog-7.17.0-import_task_result.yaml', GETDATE(), 90, '8:ad7643713bb471b687a43e7cbc1e6cc3', 'addForeignKeyConstraint baseTableName=import_task_result, constraintName=FK983AC27D11D7F882, referencedTableName=import_task', 'create ''import_task_result'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-import_task-fk.yaml::7.17.0-import_task-constraints::omp
-- create 'import_task' constraints
ALTER TABLE import_task ADD UNIQUE (name)
GO

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DF7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DFE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DFA31F8712 FOREIGN KEY (interface_config_id) REFERENCES interface_configuration (id)
GO

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DF919216CA FOREIGN KEY (last_run_result_id) REFERENCES import_task_result (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task-constraints', 'omp', 'create/changelog-7.17.0-import_task-fk.yaml', GETDATE(), 91, '8:78e2f50b13206f83aca41db8909a74a8', 'addUniqueConstraint tableName=import_task; addForeignKeyConstraint baseTableName=import_task, constraintName=FK578EF9DF7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=import_task, constraintName=FK578EF9DFE31CB353, ref...', 'create ''import_task'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent.yaml::7.17.0-intent::omp
-- create 'intent' table
CREATE TABLE intent (id bigint IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, action_id bigint NOT NULL, created_by_id numeric(19, 0), created_date smalldatetime, data_type_id bigint NOT NULL, edited_by_id numeric(19, 0), edited_date smalldatetime, receive bit NOT NULL, send bit NOT NULL, CONSTRAINT intentPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent', 'omp', 'create/changelog-7.17.0-intent.yaml', GETDATE(), 92, '8:982ba9f9e22a8836283e1d265b00223a', 'createTable tableName=intent', 'create ''intent'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent.yaml::7.17.0-intent-constraints::omp
-- create 'intent' constraints
ALTER TABLE intent ADD CONSTRAINT FKB971369CD8544299 FOREIGN KEY (action_id) REFERENCES intent_action (id)
GO

ALTER TABLE intent ADD CONSTRAINT FKB971369C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE intent ADD CONSTRAINT FKB971369C283F938E FOREIGN KEY (data_type_id) REFERENCES intent_data_type (id)
GO

ALTER TABLE intent ADD CONSTRAINT FKB971369CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent-constraints', 'omp', 'create/changelog-7.17.0-intent.yaml', GETDATE(), 93, '8:3a931477e946bc4c3a4db4618c26f67f', 'addForeignKeyConstraint baseTableName=intent, constraintName=FKB971369CD8544299, referencedTableName=intent_action; addForeignKeyConstraint baseTableName=intent, constraintName=FKB971369C7666C6D2, referencedTableName=profile; addForeignKeyConstrai...', 'create ''intent'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-intent.yaml::7.17.0-intent-indices::omp
-- create 'intent' indices
CREATE NONCLUSTERED INDEX FKB971369CD8544299 ON intent(action_id)
GO

CREATE NONCLUSTERED INDEX FKB971369C7666C6D2 ON intent(created_by_id)
GO

CREATE NONCLUSTERED INDEX FKB971369C283F938E ON intent(data_type_id)
GO

CREATE NONCLUSTERED INDEX FKB971369CE31CB353 ON intent(edited_by_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent-indices', 'omp', 'create/changelog-7.17.0-intent.yaml', GETDATE(), 94, '8:7f472e42d2ae4ae9e81dd5b5edfd1792', 'createIndex indexName=FKB971369CD8544299, tableName=intent; createIndex indexName=FKB971369C7666C6D2, tableName=intent; createIndex indexName=FKB971369C283F938E, tableName=intent; createIndex indexName=FKB971369CE31CB353, tableName=intent', 'create ''intent'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_properties_intent.yaml::7.17.0-owf_properties_intent::omp
-- create 'owf_properties_intent 'table
CREATE TABLE owf_properties_intent (owf_properties_intents_id numeric(19, 0), intent_id bigint)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties_intent', 'omp', 'create/changelog-7.17.0-owf_properties_intent.yaml', GETDATE(), 95, '8:2b6bfcb1ec5a9c45ede9784d35e9a2bf', 'createTable tableName=owf_properties_intent', 'create ''owf_properties_intent ''table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_properties_intent.yaml::7.17.0-owf_properties_intent-constraints::omp
-- create 'owf_properties_intent' constraints
ALTER TABLE owf_properties_intent ADD CONSTRAINT FK3F99ECA7A651895D FOREIGN KEY (intent_id) REFERENCES intent (id)
GO

ALTER TABLE owf_properties_intent ADD CONSTRAINT FK3F99ECA74704E25C FOREIGN KEY (owf_properties_intents_id) REFERENCES owf_properties (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties_intent-constraints', 'omp', 'create/changelog-7.17.0-owf_properties_intent.yaml', GETDATE(), 96, '8:ed3de2ab4ee2f373217f22f4fcb2b5c4', 'addForeignKeyConstraint baseTableName=owf_properties_intent, constraintName=FK3F99ECA7A651895D, referencedTableName=intent; addForeignKeyConstraint baseTableName=owf_properties_intent, constraintName=FK3F99ECA74704E25C, referencedTableName=owf_pro...', 'create ''owf_properties_intent'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-owf_properties_intent.yaml::7.17.0-owf_properties_intent-indices::omp
-- create 'owf_properties_intent' indices
CREATE NONCLUSTERED INDEX FK3F99ECA7A651895D ON owf_properties_intent(intent_id)
GO

CREATE NONCLUSTERED INDEX owfProps_intent_id_idx ON owf_properties_intent(owf_properties_intents_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties_intent-indices', 'omp', 'create/changelog-7.17.0-owf_properties_intent.yaml', GETDATE(), 97, '8:0d8fd74e6eed892c5706c7a6ba3d4e60', 'createIndex indexName=FK3F99ECA7A651895D, tableName=owf_properties_intent; createIndex indexName=owfProps_intent_id_idx, tableName=owf_properties_intent', 'create ''owf_properties_intent'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-default_images.yaml::7.17.0-default_images::omp
-- create 'default_images' table
CREATE TABLE default_images (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, image_id numeric(19, 0) NOT NULL, created_by_id numeric(19, 0), defined_default_type nvarchar(255) NOT NULL, edited_date datetime)
GO

ALTER TABLE default_images ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-default_images', 'omp', 'create/changelog-7.17.0-default_images.yaml', GETDATE(), 98, '8:b8e9effd7f4ea8733d58f714b85c768f', 'createTable tableName=default_images; addPrimaryKey tableName=default_images', 'create ''default_images'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-default_images.yaml::7.17.0-default_images-constraints::omp
-- create 'default_images' constraints
ALTER TABLE default_images ADD CONSTRAINT FK6F064E367666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE default_images ADD CONSTRAINT FK6F064E36E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE default_images ADD CONSTRAINT FK6F064E36553AF61A FOREIGN KEY (image_id) REFERENCES images (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-default_images-constraints', 'omp', 'create/changelog-7.17.0-default_images.yaml', GETDATE(), 99, '8:ba39c2c040d6e9d3eebc40a272e65cd6', 'addForeignKeyConstraint baseTableName=default_images, constraintName=FK6F064E367666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=default_images, constraintName=FK6F064E36E31CB353, referencedTableName=profile; addForeignK...', 'create ''default_images'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-types.yaml::7.17.0-types::omp
-- create 'types' table
CREATE TABLE types (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_date datetime, title nvarchar(50) NOT NULL, created_by_id numeric(19, 0), edited_date datetime, edited_by_id numeric(19, 0), has_launch_url tinyint NOT NULL, description nvarchar(250), image_id numeric(19, 0), ozone_aware tinyint NOT NULL, has_icons tinyint NOT NULL, uuid nvarchar(255), is_permanent bit)
GO

ALTER TABLE types ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-types', 'omp', 'create/changelog-7.17.0-types.yaml', GETDATE(), 100, '8:abc8db556048898a2438b8efbbe13afb', 'createTable tableName=types; addPrimaryKey tableName=types', 'create ''types'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-types.yaml::7.17.0-types-constraints::omp
-- create 'types' constraints
ALTER TABLE types ADD CONSTRAINT FK69B5879E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE types ADD CONSTRAINT FK69B58797666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE types ADD CONSTRAINT FK69B5879553AF61A FOREIGN KEY (image_id) REFERENCES images (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-types-constraints', 'omp', 'create/changelog-7.17.0-types.yaml', GETDATE(), 101, '8:0f5fdcc24cbe24240338fd32dbcea041', 'addForeignKeyConstraint baseTableName=types, constraintName=FK69B5879E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=types, constraintName=FK69B58797666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTab...', 'create ''types'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-types.yaml::7.17.0-types-indices::omp
-- create 'types' indices
CREATE UNIQUE NONCLUSTERED INDEX types_uuid_idx ON types(uuid)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-types-indices', 'omp', 'create/changelog-7.17.0-types.yaml', GETDATE(), 102, '8:807f6b4487507ee33157c3fbd79c6fe3', 'createIndex indexName=types_uuid_idx, tableName=types', 'create ''types'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item.yaml::7.17.0-service_item::omp
-- create 'service_item' table
CREATE TABLE service_item (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_date datetime, owf_properties_id numeric(19, 0), avg_rate float(53) NOT NULL, approval_status nvarchar(11) NOT NULL, title nvarchar(256) NOT NULL, image_small_url nvarchar(2083), image_large_url nvarchar(2083), total_votes int NOT NULL, launch_url nvarchar(2083), uuid nvarchar(255) NOT NULL, version_name varchar(256), release_date datetime, organization nvarchar(256), dependencies nvarchar(1000), types_id numeric(19, 0) NOT NULL, created_by_id numeric(19, 0), requirements nvarchar(1000), edited_date datetime, edited_by_id numeric(19, 0), total_comments int NOT NULL, is_hidden int NOT NULL, description varchar(4000), state_id numeric(19, 0), install_url nvarchar(2083), last_activity_id numeric(19, 0), total_rate1 int, total_rate2 int, total_rate3 int, total_rate4 int, total_rate5 int, approval_date datetime, is_outside tinyint, agency_id bigint, opens_in_new_browser_tab bit, image_medium_url varchar(2083))
GO

ALTER TABLE service_item ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item', 'omp', 'create/changelog-7.17.0-service_item.yaml', GETDATE(), 103, '8:fe5bf3c86b8f357d01ae2f5bcebac42a', 'createTable tableName=service_item; addPrimaryKey tableName=service_item', 'create ''service_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item.yaml::7.17.0-service_item-indices::omp
-- create 'service_item' indices
CREATE NONCLUSTERED INDEX si_owf_props_id_idx ON service_item(owf_properties_id)
GO

CREATE NONCLUSTERED INDEX si_types_id_idx ON service_item(types_id)
GO

CREATE NONCLUSTERED INDEX si_state_id_idx ON service_item(state_id)
GO

CREATE NONCLUSTERED INDEX si_last_activity_idx ON service_item(last_activity_id)
GO

CREATE NONCLUSTERED INDEX si_created_by_id_idx ON service_item(created_by_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item-indices', 'omp', 'create/changelog-7.17.0-service_item.yaml', GETDATE(), 104, '8:5485e1642748dc5f4926b38606c2429e', 'createIndex indexName=si_owf_props_id_idx, tableName=service_item; createIndex indexName=si_types_id_idx, tableName=service_item; createIndex indexName=si_state_id_idx, tableName=service_item; createIndex indexName=si_last_activity_idx, tableName=...', 'create ''service_item'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag::omp
-- create 'service_item_tag' table
CREATE TABLE service_item_tag (id bigint IDENTITY (1, 1) NOT NULL, service_item_id numeric(19, 0) NOT NULL, tag_id bigint NOT NULL, created_by_id numeric(19, 0), version bigint NOT NULL, CONSTRAINT service_item_tag_PK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', GETDATE(), 105, '8:60a466185fed1fbe750e43010d387cfd', 'createTable tableName=service_item_tag', 'create ''service_item_tag'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag-constraints::omp
-- create 'service_item_tag' constraints
ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_unique_idx UNIQUE (service_item_id, tag_id)
GO

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_FK_si FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_FK_tag FOREIGN KEY (tag_id) REFERENCES tag (id)
GO

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_FK_cb FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag-constraints', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', GETDATE(), 106, '8:ab179435b4370660b5e67843f41eed38', 'addUniqueConstraint constraintName=service_item_tag_unique_idx, tableName=service_item_tag; addForeignKeyConstraint baseTableName=service_item_tag, constraintName=service_item_tag_FK_si, referencedTableName=service_item; addForeignKeyConstraint ba...', 'create ''service_item_tag'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag-indices::omp
-- create 'service_item_tag' indices
CREATE NONCLUSTERED INDEX service_item_tag_si_idx ON service_item_tag(service_item_id)
GO

CREATE NONCLUSTERED INDEX service_item_tag_tag_idx ON service_item_tag(tag_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag-indices', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', GETDATE(), 107, '8:4ecbc89b508c686bc14581475c3c52ec', 'createIndex indexName=service_item_tag_si_idx, tableName=service_item_tag; createIndex indexName=service_item_tag_tag_idx, tableName=service_item_tag', 'create ''service_item_tag'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_score_card_item.yaml::7.17.0-service_item_score_card_item::omp
-- create 'service_item_score_card_item' table
CREATE TABLE service_item_score_card_item (service_item_id numeric(19, 0), score_card_item_id bigint)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_score_card_item', 'omp', 'create/changelog-7.17.0-service_item_score_card_item.yaml', GETDATE(), 108, '8:31a3e8a3bda0f78ab7e9015ee9cc65e0', 'createTable tableName=service_item_score_card_item', 'create ''service_item_score_card_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_score_card_item.yaml::7.17.0-service_item_score_card_item-constraints::omp
-- create 'service_item_score_card_item' constraints
ALTER TABLE service_item_score_card_item ADD CONSTRAINT FKBF91F93EF469C97 FOREIGN KEY (score_card_item_id) REFERENCES score_card_item (id) ON DELETE CASCADE
GO

ALTER TABLE service_item_score_card_item ADD CONSTRAINT FKBF91F939C51FA9F FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_score_card_item-constraints', 'omp', 'create/changelog-7.17.0-service_item_score_card_item.yaml', GETDATE(), 109, '8:90efc3501fc3bee39d920e23b7d397e7', 'addForeignKeyConstraint baseTableName=service_item_score_card_item, constraintName=FKBF91F93EF469C97, referencedTableName=score_card_item; addForeignKeyConstraint baseTableName=service_item_score_card_item, constraintName=FKBF91F939C51FA9F, refere...', 'create ''service_item_score_card_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity::omp
-- create 'service_item_activity' table
CREATE TABLE service_item_activity (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, created_date datetime, action nvarchar(255) NOT NULL, service_item_id numeric(19, 0) NOT NULL, created_by_id numeric(19, 0), edited_date datetime, edited_by_id numeric(19, 0), activity_timestamp datetime NOT NULL, author_id numeric(19, 0) NOT NULL, service_item_activities_idx int)
GO

ALTER TABLE service_item_activity ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', GETDATE(), 110, '8:927b28173aa53fcebfe133e6464d01cf', 'createTable tableName=service_item_activity; addPrimaryKey tableName=service_item_activity', 'create ''service_item_activity'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity-indices::omp
-- create 'service_item_activity' indices
CREATE NONCLUSTERED INDEX svc_item_act_svc_item_id_idx ON service_item_activity(service_item_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity-indices', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', GETDATE(), 111, '8:76b4bc37012b0e17630772110b7d0f92', 'createIndex indexName=svc_item_act_svc_item_id_idx, tableName=service_item_activity', 'create ''service_item_activity'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity-constraints::omp
-- create 'service_item_activity' constraints
ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B1E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B17666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B1C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B15A032135 FOREIGN KEY (author_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity-constraints', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', GETDATE(), 112, '8:42e095fdff9e0b9a4aa9192b70212fa2', 'addForeignKeyConstraint baseTableName=service_item_activity, constraintName=FK870EA6B1E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=service_item_activity, constraintName=FK870EA6B17666C6D2, referencedTableName=profil...', 'create ''service_item_activity'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item-fk.yaml::7.17.0-service_item-fk::omp
-- create 'service_item' foreign keys
ALTER TABLE service_item ADD CONSTRAINT FK1571565D2746B676 FOREIGN KEY (last_activity_id) REFERENCES service_item_activity (id)
GO

ALTER TABLE service_item ADD CONSTRAINT FK1571565D904D6974 FOREIGN KEY (owf_properties_id) REFERENCES owf_properties (id)
GO

ALTER TABLE service_item ADD CONSTRAINT FK1571565DE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE service_item ADD CONSTRAINT FK1571565D7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE service_item ADD CONSTRAINT FK1571565D6928D597 FOREIGN KEY (types_id) REFERENCES types (id)
GO

ALTER TABLE service_item ADD CONSTRAINT FK1571565DDFEC3E97 FOREIGN KEY (state_id) REFERENCES state (id)
GO

ALTER TABLE service_item ADD CONSTRAINT SERVICE_ITEM_AGENCY_FK FOREIGN KEY (agency_id) REFERENCES agency (id) ON DELETE SET NULL
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item-fk', 'omp', 'create/changelog-7.17.0-service_item-fk.yaml', GETDATE(), 113, '8:f9e6f6b0668a2ee9531af78b3c78e90a', 'addForeignKeyConstraint baseTableName=service_item, constraintName=FK1571565D2746B676, referencedTableName=service_item_activity; addForeignKeyConstraint baseTableName=service_item, constraintName=FK1571565D904D6974, referencedTableName=owf_proper...', 'create ''service_item'' foreign keys', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing::omp
-- create 'rejection_listing' table
CREATE TABLE rejection_listing (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, justification_id numeric(19, 0), edited_by_id numeric(19, 0), created_date datetime, service_item_id numeric(19, 0) NOT NULL, description nvarchar(2000), created_by_id numeric(19, 0), author_id numeric(19, 0) NOT NULL, edited_date datetime)
GO

ALTER TABLE rejection_listing ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', GETDATE(), 114, '8:53944c17c380539664e7d9f23851dc34', 'createTable tableName=rejection_listing; addPrimaryKey tableName=rejection_listing', 'create ''rejection_listing'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-constraints::omp
-- create 'rejection_listing' constraints
ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44E7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44EE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44EC7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44E5A032135 FOREIGN KEY (author_id) REFERENCES profile (id)
GO

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44E19CEB614 FOREIGN KEY (justification_id) REFERENCES rejection_justification (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-constraints', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', GETDATE(), 115, '8:2b446f0e3d58e98e9f94459cbc3b1c42', 'addForeignKeyConstraint baseTableName=rejection_listing, constraintName=FK3F2BD44E7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=rejection_listing, constraintName=FK3F2BD44EE31CB353, referencedTableName=profile; addFo...', 'create ''rejection_listing'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-indices_1::omp
-- create 'rejection_listing' indices
CREATE NONCLUSTERED INDEX rej_lst_svc_item_id_idx ON rejection_listing(service_item_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-indices_1', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', GETDATE(), 116, '8:b75225e340da5b345ff65829e212ba76', 'createIndex indexName=rej_lst_svc_item_id_idx, tableName=rejection_listing', 'create ''rejection_listing'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-indices_2::omp
-- create 'rejection_listing' indices
CREATE NONCLUSTERED INDEX rejection_listing_just_id_idx ON rejection_listing(justification_id)
GO

CREATE NONCLUSTERED INDEX rej_lst_author_id_idx ON rejection_listing(author_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-indices_2', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', GETDATE(), 117, '8:ec66d01640d1733afcc34d8438ab5d31', 'createIndex indexName=rejection_listing_just_id_idx, tableName=rejection_listing; createIndex indexName=rej_lst_author_id_idx, tableName=rejection_listing', 'create ''rejection_listing'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity::omp
-- create 'rejection_activity' table
CREATE TABLE rejection_activity (id numeric(19, 0) NOT NULL, rejection_listing_id numeric(19, 0))
GO

ALTER TABLE rejection_activity ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', GETDATE(), 118, '8:c84143725f8949ec053f3f0f30d5ed79', 'createTable tableName=rejection_activity; addPrimaryKey tableName=rejection_activity', 'create ''rejection_activity'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity-constraints::omp
-- create 'rejection_activity' constraints
ALTER TABLE rejection_activity ADD CONSTRAINT FKF35C128582548A4A FOREIGN KEY (rejection_listing_id) REFERENCES rejection_listing (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity-constraints', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', GETDATE(), 119, '8:751abc1a4a1fd3817f0c980bd84e02a9', 'addForeignKeyConstraint baseTableName=rejection_activity, constraintName=FKF35C128582548A4A, referencedTableName=rejection_listing', 'create ''rejection_activity'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity-indices::omp
-- create 'rejection_activity' indices
CREATE NONCLUSTERED INDEX rejection_act_listing_id_idx ON rejection_activity(rejection_listing_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity-indices', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', GETDATE(), 120, '8:b4064cd56dc2323fda302c4824fbb6ee', 'createIndex indexName=rejection_act_listing_id_idx, tableName=rejection_activity', 'create ''rejection_activity'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot::omp
-- create 'service_item_snapshot' table
CREATE TABLE service_item_snapshot (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, service_item_id numeric(19, 0), title nvarchar(255) NOT NULL)
GO

ALTER TABLE service_item_snapshot ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', GETDATE(), 121, '8:7361453c4becd36863882d60a8192388', 'createTable tableName=service_item_snapshot; addPrimaryKey tableName=service_item_snapshot', 'create ''service_item_snapshot'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot-constraints::omp
-- create 'service_item_snapshot' constraints
ALTER TABLE service_item_snapshot ADD CONSTRAINT FKFABD8966C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot-constraints', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', GETDATE(), 122, '8:acf172475a047a04bc33a0f124410dc1', 'addForeignKeyConstraint baseTableName=service_item_snapshot, constraintName=FKFABD8966C7E5C662, referencedTableName=service_item', 'create ''service_item_snapshot'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot-indices::omp
-- create 'service_item_snapshot' indices
CREATE NONCLUSTERED INDEX si_snapshot_id_idx ON service_item_snapshot(service_item_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot-indices', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', GETDATE(), 123, '8:4c97371bb3743dbec88632840a5c3a6a', 'createIndex indexName=si_snapshot_id_idx, tableName=service_item_snapshot', 'create ''service_item_snapshot'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log::omp
-- create 'relationship_activity_log' table
CREATE TABLE relationship_activity_log (mod_rel_activity_id numeric(19, 0) NOT NULL, service_item_snapshot_id numeric(19, 0), items_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', GETDATE(), 124, '8:efd37558d02fc5a5cb8a38acb7945f7f', 'createTable tableName=relationship_activity_log', 'create ''relationship_activity_log'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log-constraints::omp
-- create 'relationship_activity_log' constraints
ALTER TABLE relationship_activity_log ADD CONSTRAINT FK594974BB25A20F9D FOREIGN KEY (service_item_snapshot_id) REFERENCES service_item_snapshot (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log-constraints', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', GETDATE(), 125, '8:c34da16ff81ac34b973b19b0e119e48c', 'addForeignKeyConstraint baseTableName=relationship_activity_log, constraintName=FK594974BB25A20F9D, referencedTableName=service_item_snapshot', 'create ''relationship_activity_log'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log-indices::omp
-- create 'relationship_activity_log' indices
CREATE NONCLUSTERED INDEX rel_act_log_mod_si_snpsht_idx ON relationship_activity_log(service_item_snapshot_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log-indices', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', GETDATE(), 126, '8:968336fc067e8927c80f551c896dbc84', 'createIndex indexName=rel_act_log_mod_si_snpsht_idx, tableName=relationship_activity_log', 'create ''relationship_activity_log'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_documentation_url.yaml::7.17.0-service_item_documentation_url::omp
-- create 'service_item_documentation_url' table
CREATE TABLE service_item_documentation_url (id bigint IDENTITY (1, 1) NOT NULL, version bigint CONSTRAINT DF_service_item_documentation_url_version DEFAULT 0 NOT NULL, name varchar(256) NOT NULL, service_item_id numeric(19, 0) NOT NULL, url varchar(2083) NOT NULL, CONSTRAINT service_item_PK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_documentation_url', 'omp', 'create/changelog-7.17.0-service_item_documentation_url.yaml', GETDATE(), 127, '8:c67561008a9409e748fa4ea37d4e713b', 'createTable tableName=service_item_documentation_url', 'create ''service_item_documentation_url'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_documentation_url.yaml::7.17.0-service_item_documentation_url-constraints::omp
-- create 'service_item_documentation_url' constraints
ALTER TABLE service_item_documentation_url ADD CONSTRAINT FK24572D08C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_documentation_url-constraints', 'omp', 'create/changelog-7.17.0-service_item_documentation_url.yaml', GETDATE(), 128, '8:0fc0b159fa39d7ea27e858d3560f871a', 'addForeignKeyConstraint baseTableName=service_item_documentation_url, constraintName=FK24572D08C7E5C662, referencedTableName=service_item', 'create ''service_item_documentation_url'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_tech_pocs.yaml::7.17.0-service_item_tech_pocs::omp
-- create 'service_item_tech_pocs' table
CREATE TABLE service_item_tech_pocs (service_item_id numeric(19, 0), tech_poc varchar(256))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tech_pocs', 'omp', 'create/changelog-7.17.0-service_item_tech_pocs.yaml', GETDATE(), 129, '8:f3c60e7d94424a48ced23061001efa3a', 'createTable tableName=service_item_tech_pocs', 'create ''service_item_tech_pocs'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-service_item_tech_pocs.yaml::7.17.0-service_item_tech_pocs-constraints::omp
-- create 'service_item_tech_pocs' constraints
ALTER TABLE service_item_tech_pocs ADD CONSTRAINT FKA55CFB56C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tech_pocs-constraints', 'omp', 'create/changelog-7.17.0-service_item_tech_pocs.yaml', GETDATE(), 130, '8:23b732b4702270712a83727101647c14', 'addForeignKeyConstraint baseTableName=service_item_tech_pocs, constraintName=FKA55CFB56C7E5C662, referencedTableName=service_item', 'create ''service_item_tech_pocs'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommended_layouts::omp
-- create 'si_recommended_layouts' table
CREATE TABLE si_recommended_layouts (service_item_id numeric(19, 0), recommended_layout nvarchar(255))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommended_layouts', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', GETDATE(), 131, '8:f0b54e6f05b524e8fee9caf020775d2d', 'createTable tableName=si_recommended_layouts', 'create ''si_recommended_layouts'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommend_layouts-constraints::omp
-- create 'si_recommend_layouts' constraints
ALTER TABLE si_recommended_layouts ADD CONSTRAINT FK863C793CC7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommend_layouts-constraints', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', GETDATE(), 132, '8:a16e859e1aa5220b86a98370a5c3419a', 'addForeignKeyConstraint baseTableName=si_recommended_layouts, constraintName=FK863C793CC7E5C662, referencedTableName=service_item', 'create ''si_recommend_layouts'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommend_layouts-indices::omp
-- create 'si_recommend_layouts' indices
CREATE NONCLUSTERED INDEX si_rec_layouts_idx ON si_recommended_layouts(service_item_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommend_layouts-indices', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', GETDATE(), 133, '8:ba9523e13a60da23b70c5d9086a54216', 'createIndex indexName=si_rec_layouts_idx, tableName=si_recommended_layouts', 'create ''si_recommend_layouts'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-screenshot.yaml::7.17.0-screenshot::omp
-- create 'screenshot' table
CREATE TABLE screenshot (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, small_image_url varchar(2083) NOT NULL, large_image_url varchar(2083), ordinal int, service_item_id numeric(19, 0), created_by_id numeric(19, 0), created_date smalldatetime, edited_by_id numeric(19, 0), edited_date smalldatetime, version bigint CONSTRAINT DF_screenshot_version DEFAULT 0 NOT NULL, CONSTRAINT PK_SCREENSHOT PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-screenshot', 'omp', 'create/changelog-7.17.0-screenshot.yaml', GETDATE(), 134, '8:3264b147e4f9de5a528699cf8caa6a4d', 'createTable tableName=screenshot', 'create ''screenshot'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-screenshot.yaml::7.17.0-screenshot-constraints::omp
-- create 'screenshot' constraints
ALTER TABLE screenshot ADD CONSTRAINT SCREENSHOT_SERVICE_ITEM_FK FOREIGN KEY (service_item_id) REFERENCES service_item (id) ON DELETE CASCADE
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-screenshot-constraints', 'omp', 'create/changelog-7.17.0-screenshot.yaml', GETDATE(), 135, '8:664acb9ab6e1eda0c2b45817f86b8c87', 'addForeignKeyConstraint baseTableName=screenshot, constraintName=SCREENSHOT_SERVICE_ITEM_FK, referencedTableName=service_item', 'create ''screenshot'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship::omp
-- create 'relationship' table
CREATE TABLE relationship (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, relationship_type nvarchar(255) NOT NULL, owning_entity_id numeric(19, 0) NOT NULL)
GO

ALTER TABLE relationship ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship', 'omp', 'create/changelog-7.17.0-relationship.yaml', GETDATE(), 136, '8:8107005f45b3ec3d1c565ebe24b35176', 'createTable tableName=relationship; addPrimaryKey tableName=relationship', 'create ''relationship'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship-constraints::omp
-- create 'relationship' constraints
ALTER TABLE relationship ADD CONSTRAINT FKF06476389D70DD39 FOREIGN KEY (owning_entity_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship-constraints', 'omp', 'create/changelog-7.17.0-relationship.yaml', GETDATE(), 137, '8:c829c3cf6ffc97c3f4ee9a6f01d433eb', 'addForeignKeyConstraint baseTableName=relationship, constraintName=FKF06476389D70DD39, referencedTableName=service_item', 'create ''relationship'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship-indices::omp
-- create 'relationship' indices
CREATE NONCLUSTERED INDEX relationship_owing_id_idx ON relationship(owning_entity_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship-indices', 'omp', 'create/changelog-7.17.0-relationship.yaml', GETDATE(), 138, '8:4bf26b137c2a432d8d6be5aef0677049', 'createIndex indexName=relationship_owing_id_idx, tableName=relationship', 'create ''relationship'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship_service_item.yaml::7.17.0-relationship_service_item::omp
-- create 'relationship_service_item' table
CREATE TABLE relationship_service_item (relationship_related_items_id numeric(19, 0), service_item_id numeric(19, 0), related_items_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_service_item', 'omp', 'create/changelog-7.17.0-relationship_service_item.yaml', GETDATE(), 139, '8:f18830757d3848327a21f97f440b3989', 'createTable tableName=relationship_service_item', 'create ''relationship_service_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-relationship_service_item.yaml::7.17.0-relationship_service_item-constraints::omp
-- create 'relationship_service_item' constraints
ALTER TABLE relationship_service_item ADD CONSTRAINT FKDA02504C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_service_item-constraints', 'omp', 'create/changelog-7.17.0-relationship_service_item.yaml', GETDATE(), 140, '8:9adc63402c979147e92e527ef25218ef', 'addForeignKeyConstraint baseTableName=relationship_service_item, constraintName=FKDA02504C7E5C662, referencedTableName=service_item', 'create ''relationship_service_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment::omp
-- create 'item_comment' table
CREATE TABLE item_comment (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, edited_by_id numeric(19, 0), created_date datetime, service_item_id numeric(19, 0) NOT NULL, text nvarchar(4000), created_by_id numeric(19, 0), author_id numeric(19, 0) NOT NULL, edited_date datetime, rate float(53))
GO

ALTER TABLE item_comment ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment', 'omp', 'create/changelog-7.17.0-item_comment.yaml', GETDATE(), 141, '8:e4006af242391132928072d27fbd2d62', 'createTable tableName=item_comment; addPrimaryKey tableName=item_comment', 'create ''item_comment'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment-constraints::omp
-- create 'item_comment' constraints
ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D337666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D33E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D33C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D335A032135 FOREIGN KEY (author_id) REFERENCES profile (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment-constraints', 'omp', 'create/changelog-7.17.0-item_comment.yaml', GETDATE(), 142, '8:5336ec2c90d00ac795f1ad642fbd1675', 'addForeignKeyConstraint baseTableName=item_comment, constraintName=FKE6D04D337666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=item_comment, constraintName=FKE6D04D33E31CB353, referencedTableName=profile; addForeignKeyCo...', 'create ''item_comment'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment-indices::omp
-- create 'item_comment' indices
CREATE NONCLUSTERED INDEX itm_cmnt_svc_item_id_idx ON item_comment(service_item_id)
GO

CREATE NONCLUSTERED INDEX itm_cmnt_author_id_idx ON item_comment(author_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment-indices', 'omp', 'create/changelog-7.17.0-item_comment.yaml', GETDATE(), 143, '8:1de386b3024efc24aea1b0e0245817e0', 'createIndex indexName=itm_cmnt_svc_item_id_idx, tableName=item_comment; createIndex indexName=itm_cmnt_author_id_idx, tableName=item_comment', 'create ''item_comment'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-change_detail.yaml::7.17.0-change_detail::omp
-- create 'change_detail' table
CREATE TABLE change_detail (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, new_value nvarchar(4000), field_name nvarchar(255) NOT NULL, old_value nvarchar(4000), service_item_activity_id numeric(19, 0) NOT NULL)
GO

ALTER TABLE change_detail ADD PRIMARY KEY (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-change_detail', 'omp', 'create/changelog-7.17.0-change_detail.yaml', GETDATE(), 144, '8:229020e2a9b602ea9d52097ee02618c0', 'createTable tableName=change_detail; addPrimaryKey tableName=change_detail', 'create ''change_detail'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-change_detail.yaml::7.17.0-change_detail-constraints::omp
-- create 'change_detail' indices
ALTER TABLE change_detail ADD CONSTRAINT FKB4467BC0855307BD FOREIGN KEY (service_item_activity_id) REFERENCES service_item_activity (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-change_detail-constraints', 'omp', 'create/changelog-7.17.0-change_detail.yaml', GETDATE(), 145, '8:7209140bd8c4ae18ced90ec021b0cd55', 'addForeignKeyConstraint baseTableName=change_detail, constraintName=FKB4467BC0855307BD, referencedTableName=service_item_activity', 'create ''change_detail'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-change_detail.yaml::7.17.0-change_detail-indices::omp
-- create 'change_detail' indices
CREATE NONCLUSTERED INDEX FKB4467BC0855307BD ON change_detail(service_item_activity_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-change_detail-indices', 'omp', 'create/changelog-7.17.0-change_detail.yaml', GETDATE(), 146, '8:449e6837eb72f9f5a87a64dacce63954', 'createIndex indexName=FKB4467BC0855307BD, tableName=change_detail', 'create ''change_detail'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-contact.yaml::7.17.0-contact::omp
-- create 'contact' table
CREATE TABLE contact (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version bigint NOT NULL, email varchar(100) NOT NULL, name varchar(100) NOT NULL, organization varchar(100), secure_phone varchar(50), type_id bigint NOT NULL, service_item_id numeric(19, 0) NOT NULL, unsecure_phone varchar(50), created_by_id numeric(19, 0), created_date smalldatetime, edited_by_id numeric(19, 0), edited_date smalldatetime, CONSTRAINT contactPK PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact', 'omp', 'create/changelog-7.17.0-contact.yaml', GETDATE(), 147, '8:f59d0193a20e1edbf70a8265092ad861', 'createTable tableName=contact', 'create ''contact'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-contact.yaml::7.17.0-contact-constraints::omp
-- create 'contact' constraints
ALTER TABLE contact ADD CONSTRAINT FK38B724207666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE contact ADD CONSTRAINT FK38B72420E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE contact ADD CONSTRAINT FK38B72420BA3FC877 FOREIGN KEY (type_id) REFERENCES contact_type (id)
GO

ALTER TABLE contact ADD CONSTRAINT FK38B72420C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact-constraints', 'omp', 'create/changelog-7.17.0-contact.yaml', GETDATE(), 148, '8:29e8cf4c13898ebb4b0041421fa697b8', 'addForeignKeyConstraint baseTableName=contact, constraintName=FK38B724207666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=contact, constraintName=FK38B72420E31CB353, referencedTableName=profile; addForeignKeyConstraint b...', 'create ''contact'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-contact.yaml::7.17.0-contact-indices::omp
-- create 'contact' indices
CREATE NONCLUSTERED INDEX FK38B72420C7E5C662 ON contact(service_item_id)
GO

CREATE NONCLUSTERED INDEX FK38B72420BA3FC877 ON contact(type_id)
GO

CREATE NONCLUSTERED INDEX FK38B72420E31CB353 ON contact(edited_by_id)
GO

CREATE NONCLUSTERED INDEX FK38B724207666C6D2 ON contact(created_by_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact-indices', 'omp', 'create/changelog-7.17.0-contact.yaml', GETDATE(), 149, '8:313e0234cd63b81997f526763b01e73e', 'createIndex indexName=FK38B72420C7E5C662, tableName=contact; createIndex indexName=FK38B72420BA3FC877, tableName=contact; createIndex indexName=FK38B72420E31CB353, tableName=contact; createIndex indexName=FK38B724207666C6D2, tableName=contact', 'create ''contact'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types::omp
-- create 'custom_field_definition_types' table
CREATE TABLE custom_field_definition_types (cf_definition_types_id numeric(19, 0), types_id numeric(19, 0), types_idx int)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', GETDATE(), 150, '8:6b0871641c5337bcfcbec5f26b95a8d7', 'createTable tableName=custom_field_definition_types', 'create ''custom_field_definition_types'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types-constraints::omp
-- create 'custom_field_definition_types' constraints
ALTER TABLE custom_field_definition_types ADD CONSTRAINT FK1A84FFC06928D597 FOREIGN KEY (types_id) REFERENCES types (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types-constraints', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', GETDATE(), 151, '8:004fa32145698c28d3063ff80f07e182', 'addForeignKeyConstraint baseTableName=custom_field_definition_types, constraintName=FK1A84FFC06928D597, referencedTableName=types', 'create ''custom_field_definition_types'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types-indices::omp
-- create 'custom_field_definition_types' indices
CREATE NONCLUSTERED INDEX cfd_types_types_idx ON custom_field_definition_types(types_id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types-indices', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', GETDATE(), 152, '8:9b039e020d2575090ac92ca4fd5e4d2e', 'createIndex indexName=cfd_types_types_idx, tableName=custom_field_definition_types', 'create ''custom_field_definition_types'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-affiliated_marketplace.yaml::7.17.0-affiliated_marketplace::omp
-- create 'affiliated_marketplace' table
CREATE TABLE affiliated_marketplace (id numeric(19, 0) IDENTITY (1, 1) NOT NULL, version numeric(19, 0) NOT NULL, active int NOT NULL, created_by_id numeric(19, 0), created_date datetime, edited_by_id numeric(19, 0), edited_date datetime, icon_id numeric(19, 0), name nvarchar(50) NOT NULL, server_url nvarchar(2083) NOT NULL, timeout numeric(19, 0), CONSTRAINT PK_AFFILIATED_MARKETPLACE PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-affiliated_marketplace', 'omp', 'create/changelog-7.17.0-affiliated_marketplace.yaml', GETDATE(), 153, '8:a8a7da229754df37c13f427aa348286c', 'createTable tableName=affiliated_marketplace', 'create ''affiliated_marketplace'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

-- Changeset create/changelog-7.17.0-affiliated_marketplace.yaml::7.17.0-affiliated_marketplace-constraints::omp
-- create 'affiliated_marketplace' constraints
ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C37666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id)
GO

ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C3E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id)
GO

ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C3EA25263C FOREIGN KEY (icon_id) REFERENCES images (id)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-affiliated_marketplace-constraints', 'omp', 'create/changelog-7.17.0-affiliated_marketplace.yaml', GETDATE(), 154, '8:129a0323608fe68be271de563fd7db99', 'addForeignKeyConstraint baseTableName=affiliated_marketplace, constraintName=FKA6EB2C37666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=affiliated_marketplace, constraintName=FKA6EB2C3E31CB353, referencedTableName=profil...', 'create ''affiliated_marketplace'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497048765')
GO

