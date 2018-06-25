--  Create Database Lock Table
CREATE TABLE DATABASECHANGELOGLOCK (ID INT NOT NULL, LOCKED BIT(1) NOT NULL, LOCKGRANTED datetime NULL, LOCKEDBY VARCHAR(255) NULL, CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

--  Initialize Database Lock Table
DELETE FROM DATABASECHANGELOGLOCK;

INSERT INTO DATABASECHANGELOGLOCK (ID, LOCKED) VALUES (1, 0);

--  Create Database Change Log Table
CREATE TABLE DATABASECHANGELOG (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED datetime NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35) NULL, DESCRIPTION VARCHAR(255) NULL, COMMENTS VARCHAR(255) NULL, TAG VARCHAR(255) NULL, LIQUIBASE VARCHAR(20) NULL, CONTEXTS VARCHAR(255) NULL, LABELS VARCHAR(255) NULL, DEPLOYMENT_ID VARCHAR(10) NULL);

--  Changeset create/changelog-7.17.0-sequences.yaml::7.17.0-sequences::omp
--  create hibernate sequence
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-sequences', 'omp', 'create/changelog-7.17.0-sequences.yaml', NOW(), 1, '8:66d339bfb0b07ca6736080b2bc5a37fd', 'createSequence sequenceName=hibernate_sequence', 'create hibernate sequence', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-agency.yaml::7.17.0-agency::omp
--  create 'agency' table
CREATE TABLE agency (id BIGINT AUTO_INCREMENT NOT NULL, title VARCHAR(255) NULL, icon_url VARCHAR(2083) NOT NULL, created_by_id BIGINT NULL, created_date date NULL, edited_by_id BIGINT NULL, edited_date date NULL, version BIGINT DEFAULT 0 NOT NULL, CONSTRAINT PK_AGENCY PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-agency', 'omp', 'create/changelog-7.17.0-agency.yaml', NOW(), 2, '8:70f90bb68cb93f47ce9322630cf46dbd', 'createTable tableName=agency', 'create ''agency'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-agency.yaml::7.17.0-agency-constraints::omp
--  create 'agency' constraints
ALTER TABLE agency ADD UNIQUE (title);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-agency-constraints', 'omp', 'create/changelog-7.17.0-agency.yaml', NOW(), 3, '8:280ec00d14e9fa3d4625618ec09af8d3', 'addUniqueConstraint tableName=agency', 'create ''agency'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-avatar.yaml::7.17.0-avatar::omp
--  create 'avatar' table
CREATE TABLE avatar (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, is_default BIT(1) NOT NULL, pic MEDIUMBLOB NULL, created_by_id BIGINT NULL, content_type VARCHAR(255) NULL, edited_date datetime NULL, CONSTRAINT PK_AVATAR PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-avatar', 'omp', 'create/changelog-7.17.0-avatar.yaml', NOW(), 4, '8:909dd0cf2d2f74a7c222ca628095f007', 'createTable tableName=avatar', 'create ''avatar'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-check_box_cf.yaml::7.17.0-check_box_cf::omp
--  create 'check_box_cf' table
CREATE TABLE check_box_cf (id BIGINT NOT NULL, value BIT(1) NULL, CONSTRAINT PK_CHECK_BOX_CF PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-check_box_cf', 'omp', 'create/changelog-7.17.0-check_box_cf.yaml', NOW(), 5, '8:6d044a8e68647e71d12fe68e92febbe4', 'createTable tableName=check_box_cf', 'create ''check_box_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-check_box_cfd.yaml::7.17.0-check_box_cfd::omp
--  create 'check_box_cfd' table
CREATE TABLE check_box_cfd (id BIGINT NOT NULL, selected_by_default BIT(1) NULL, CONSTRAINT PK_CHECK_BOX_CFD PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-check_box_cfd', 'omp', 'create/changelog-7.17.0-check_box_cfd.yaml', NOW(), 6, '8:c43258e9d096d31ce7dd3cdc081a0c11', 'createTable tableName=check_box_cfd', 'create ''check_box_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-drop_down_cfd.yaml::7.17.0-drop_down_cfd::omp
--  create 'drop_down_cfd' table
CREATE TABLE drop_down_cfd (id BIGINT NOT NULL, is_multi_select BIT NULL, CONSTRAINT PK_DROP_DOWN_CFD PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cfd', 'omp', 'create/changelog-7.17.0-drop_down_cfd.yaml', NOW(), 7, '8:6db4568908db267023f3e92038c23290', 'createTable tableName=drop_down_cfd', 'create ''drop_down_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-ext_profile.yaml::7.17.0-ext_profile::omp
--  create 'ext_profile' table
CREATE TABLE ext_profile (id BIGINT NOT NULL, external_view_url VARCHAR(2083) NULL, system_uri VARCHAR(255) NOT NULL, external_id VARCHAR(255) NULL, external_edit_url VARCHAR(2083) NULL, CONSTRAINT PK_EXT_PROFILE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_profile', 'omp', 'create/changelog-7.17.0-ext_profile.yaml', NOW(), 8, '8:a3d51e6a13860b7fe9384a73ef11ab07', 'createTable tableName=ext_profile', 'create ''ext_profile'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-ext_profile.yaml::7.17.0-ext_profile-constraints::omp
--  create 'ext_profile' constraints
ALTER TABLE ext_profile ADD UNIQUE (system_uri, external_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_profile-constraints', 'omp', 'create/changelog-7.17.0-ext_profile.yaml', NOW(), 9, '8:c48766db440f5790f5f1e0be0f0ead7d', 'addUniqueConstraint tableName=ext_profile', 'create ''ext_profile'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-ext_service_item.yaml::7.17.0-ext_service_item::omp
--  create 'ext_service_item' table
CREATE TABLE ext_service_item (id BIGINT NOT NULL, external_view_url VARCHAR(2083) NULL, system_uri VARCHAR(256) NULL, external_id VARCHAR(256) NULL, external_edit_url VARCHAR(2083) NULL, CONSTRAINT PK_EXT_SERVICE_ITEM PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_service_item', 'omp', 'create/changelog-7.17.0-ext_service_item.yaml', NOW(), 10, '8:72d2c1367a7e0a27463608936d2d92d1', 'createTable tableName=ext_service_item', 'create ''ext_service_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-ext_service_item.yaml::7.17.0-ext_service_item-constraints::omp
--  create 'ext_service_item' constraints
ALTER TABLE ext_service_item ADD UNIQUE (system_uri, external_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-ext_service_item-constraints', 'omp', 'create/changelog-7.17.0-ext_service_item.yaml', NOW(), 11, '8:c53fbe90b5e4bbd2abbf12a513d38f8a', 'addUniqueConstraint tableName=ext_service_item', 'create ''ext_service_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-image_url_cf.yaml::7.17.0-image_url_cf::omp
--  create 'image_url_cf' table
CREATE TABLE image_url_cf (id BIGINT NOT NULL, value VARCHAR(2083) NULL, CONSTRAINT image_url_cfPK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-image_url_cf', 'omp', 'create/changelog-7.17.0-image_url_cf.yaml', NOW(), 12, '8:4ad7d2858c7be74dc806e277a4465648', 'createTable tableName=image_url_cf', 'create ''image_url_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-image_url_cfd.yaml::7.17.0-image_url_cfd::omp
--  create 'image_url_cfd' table
CREATE TABLE image_url_cfd (id BIGINT NOT NULL, CONSTRAINT image_url_cfdPK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-image_url_cfd', 'omp', 'create/changelog-7.17.0-image_url_cfd.yaml', NOW(), 13, '8:eece26771cfef53912f76f3d654bb8dd', 'createTable tableName=image_url_cfd', 'create ''image_url_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-interface_configuration.yaml::7.17.0-interface_configuration::omp
--  create 'interface_configuration' table
CREATE TABLE interface_configuration (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, allow_truncate BIT(1) NOT NULL, auto_create_meta_data BIT(1) NOT NULL, default_large_icon_url VARCHAR(2048) NULL, default_small_icon_url VARCHAR(2048) NULL, delta_since_time_param VARCHAR(64) NULL, delta_static_parameters VARCHAR(2048) NULL, full_static_parameters VARCHAR(2048) NULL, loose_match BIT(1) NOT NULL, name VARCHAR(256) NOT NULL, query_date_format VARCHAR(32) NULL, response_date_format VARCHAR(32) NULL, download_images TINYINT(1) DEFAULT false NOT NULL, CONSTRAINT PK_INTERFACE_CONFIGURATION PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-interface_configuration', 'omp', 'create/changelog-7.17.0-interface_configuration.yaml', NOW(), 14, '8:a67303cb0248fd4b03ec670eade20e3c', 'createTable tableName=interface_configuration', 'create ''interface_configuration'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-modify_relationship_activity.yaml::7.17.0-modify_relationship_activity::omp
--  create 'modify_relationship_activity' table
CREATE TABLE modify_relationship_activity (id BIGINT NOT NULL, CONSTRAINT PK_MODIFY_RELATIONSHIP_ACTIVITY PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-modify_relationship_activity', 'omp', 'create/changelog-7.17.0-modify_relationship_activity.yaml', NOW(), 15, '8:c52afb45b1d39ba1f1be8869c9907d9d', 'createTable tableName=modify_relationship_activity', 'create ''modify_relationship_activity'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_profile.yaml::7.17.0-service_item_profile::omp
--  create 'service_item_profile' table
CREATE TABLE service_item_profile (service_item_owners_id BIGINT NULL, profile_id BIGINT NULL, owners_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_profile', 'omp', 'create/changelog-7.17.0-service_item_profile.yaml', NOW(), 16, '8:4d84e72c04907269999cb7e852d8dfe8', 'createTable tableName=service_item_profile', 'create ''service_item_profile'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-tag.yaml::7.17.0-tag::omp
--  create 'tag' table
CREATE TABLE tag (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, edited_by_id BIGINT NULL, edited_date date NULL, title VARCHAR(16) NOT NULL, CONSTRAINT PK_TAG PRIMARY KEY (id));

ALTER TABLE tag COLLATE latin1_general_cs;

ALTER TABLE tag MODIFY title varchar(16) COLLATE latin1_general_cs NOT NULL;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-tag', 'omp', 'create/changelog-7.17.0-tag.yaml', NOW(), 17, '8:c8cf552dd9f7117617854bceb2f973e6', 'createTable tableName=tag; sql', 'create ''tag'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-tag.yaml::7.17.0-tag-indices::omp
--  create 'tag' indices
CREATE INDEX tag_title_idx ON tag(title);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-tag-indices', 'omp', 'create/changelog-7.17.0-tag.yaml', NOW(), 18, '8:4edd892e0e4056cddd3e7452a2424ed3', 'createIndex indexName=tag_title_idx, tableName=tag', 'create ''tag'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-text_area_cf.yaml::7.17.0-text_area_cf::omp
--  create 'text_area_cf' table
CREATE TABLE text_area_cf (id BIGINT NOT NULL, value VARCHAR(4000) NULL, CONSTRAINT text_area_cfPK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_area_cf', 'omp', 'create/changelog-7.17.0-text_area_cf.yaml', NOW(), 19, '8:b1ae587f06cde5695b2a0c7ae8d5a9b0', 'createTable tableName=text_area_cf', 'create ''text_area_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-text_area_cfd.yaml::7.17.0-text_area_cfd::omp
--  create 'text_area_cfd' table
CREATE TABLE text_area_cfd (id BIGINT NOT NULL, CONSTRAINT text_area_cfdPK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_area_cfd', 'omp', 'create/changelog-7.17.0-text_area_cfd.yaml', NOW(), 20, '8:99784a68f85eeb93fb591d3644c11750', 'createTable tableName=text_area_cfd', 'create ''text_area_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-text_cf.yaml::7.17.0-text_cf::omp
--  create 'text_cf' table
CREATE TABLE text_cf (id BIGINT NOT NULL, value VARCHAR(256) NULL, CONSTRAINT PK_TEXT_CF PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_cf', 'omp', 'create/changelog-7.17.0-text_cf.yaml', NOW(), 21, '8:ef38ac5ee6e079b7d8387985cc6e6cc7', 'createTable tableName=text_cf', 'create ''text_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-text_cfd.yaml::7.17.0-text_cfd::omp
--  create 'text_cfd' table
CREATE TABLE text_cfd (id BIGINT NOT NULL, CONSTRAINT PK_TEXT_CFD PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text_cfd', 'omp', 'create/changelog-7.17.0-text_cfd.yaml', NOW(), 22, '8:4e798c24b40193aa1c41427dac724045', 'createTable tableName=text_cfd', 'create ''text_cfd'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-u_domain_preferences.yaml::7.17.0-u_domain_preferences::omp
--  create 'u_domain_preferences' table
CREATE TABLE u_domain_preferences (preferences BIGINT NULL, preferences_idx VARCHAR(255) NULL, preferences_elt VARCHAR(255) NOT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-u_domain_preferences', 'omp', 'create/changelog-7.17.0-u_domain_preferences.yaml', NOW(), 23, '8:e6bf859a2d054acd11d1365e25786f3c', 'createTable tableName=u_domain_preferences', 'create ''u_domain_preferences'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-profile.yaml::7.17.0-profile::omp
--  create 'profile' table
CREATE TABLE profile (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_date datetime NOT NULL, created_by_id BIGINT NULL, bio VARCHAR(1000) NULL, edited_date datetime NULL, edited_by_id BIGINT NULL, username VARCHAR(256) NOT NULL, email VARCHAR(256) NULL, avatar_id BIGINT NULL, display_name VARCHAR(256) NULL, uuid VARCHAR(255) NULL, user_roles VARCHAR(255) NULL, CONSTRAINT PK_PROFILE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-profile', 'omp', 'create/changelog-7.17.0-profile.yaml', NOW(), 24, '8:7a7269af33692468f01ad03f6902abd8', 'createTable tableName=profile', 'create ''profile'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-profile.yaml::7.17.0-profile-constraints::omp
--  create 'profile' constraints
ALTER TABLE profile ADD UNIQUE (username);

ALTER TABLE profile ADD CONSTRAINT FKED8E89A9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE profile ADD CONSTRAINT FKED8E89A97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE profile ADD CONSTRAINT FKED8E89A961C3343D FOREIGN KEY (avatar_id) REFERENCES avatar (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-profile-constraints', 'omp', 'create/changelog-7.17.0-profile.yaml', NOW(), 25, '8:243d71826c7080ccb2be52d9b10e5c1e', 'addUniqueConstraint tableName=profile; addForeignKeyConstraint baseTableName=profile, constraintName=FKED8E89A9E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=profile, constraintName=FKED8E89A97666C6D2, referencedTable...', 'create ''profile'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-profile.yaml::7.17.0-profile-indices::omp
--  create 'profile' indices
CREATE UNIQUE INDEX profile_uuid_idx ON profile(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-profile-indices', 'omp', 'create/changelog-7.17.0-profile.yaml', NOW(), 26, '8:d058a81fb5aa05cab59dddfde7bf15d0', 'createIndex indexName=profile_uuid_idx, tableName=profile', 'create ''profile'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-avatar-fk.yaml::7.17.0-avatar-constraints::omp
--  create 'avatar' constraints
ALTER TABLE avatar ADD CONSTRAINT FKAC32C1597666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE avatar ADD CONSTRAINT FKAC32C159E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-avatar-constraints', 'omp', 'create/changelog-7.17.0-avatar-fk.yaml', NOW(), 27, '8:29580d80bb7bb7eb898e43fcfcfcb476', 'addForeignKeyConstraint baseTableName=avatar, constraintName=FKAC32C1597666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=avatar, constraintName=FKAC32C159E31CB353, referencedTableName=profile', 'create ''avatar'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-application_configuration.yaml::7.17.0-application_configuration::omp
--  create 'application_configuration' table
CREATE TABLE application_configuration (id BIGINT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, edited_by_id BIGINT NULL, edited_date date NULL, code VARCHAR(250) NOT NULL, value VARCHAR(2000) NULL, title VARCHAR(250) NOT NULL, description VARCHAR(2000) NULL, type VARCHAR(250) NOT NULL, group_name VARCHAR(250) NOT NULL, sub_group_name VARCHAR(250) NULL, mutable TINYINT(1) NOT NULL, sub_group_order BIGINT NULL, help VARCHAR(2000) NULL, CONSTRAINT PK_APPLICATION_CONFIGURATION PRIMARY KEY (id), UNIQUE (code));

ALTER TABLE application_configuration MODIFY id BIGINT AUTO_INCREMENT;

ALTER TABLE application_configuration AUTO_INCREMENT=48;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-application_configuration', 'omp', 'create/changelog-7.17.0-application_configuration.yaml', NOW(), 28, '8:55acf19d0b3c4d43a8b4be44b00f97a8', 'createTable tableName=application_configuration; addAutoIncrement columnName=id, tableName=application_configuration', 'create ''application_configuration'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-application_configuration.yaml::7.17.0-application_configuration-constraints::omp
--  create 'application_configuration' constraints
ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C0477666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE application_configuration ADD CONSTRAINT FKFC9C047E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-application_configuration-constraints', 'omp', 'create/changelog-7.17.0-application_configuration.yaml', NOW(), 29, '8:35f730727e99599020aa22c3fdf842eb', 'addForeignKeyConstraint baseTableName=application_configuration, constraintName=FKFC9C0477666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=application_configuration, constraintName=FKFC9C047E31CB353, referencedTableName=...', 'create ''application_configuration'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-application_configuration.yaml::7.17.0-application_configuration-indices::omp
--  create 'application_configuration' indices
CREATE INDEX FKFC9C0477666C6D2 ON application_configuration(created_by_id);

CREATE INDEX FKFC9C047E31CB353 ON application_configuration(edited_by_id);

CREATE INDEX app_config_group_name_idx ON application_configuration(group_name);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-application_configuration-indices', 'omp', 'create/changelog-7.17.0-application_configuration.yaml', NOW(), 30, '8:2fc975f2cc5ff4c0ea4eb2f7b4bb8cf2', 'createIndex indexName=FKFC9C0477666C6D2, tableName=application_configuration; createIndex indexName=FKFC9C047E31CB353, tableName=application_configuration; createIndex indexName=app_config_group_name_idx, tableName=application_configuration', 'create ''application_configuration'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-category.yaml::7.17.0-category::omp
--  create 'category' table
CREATE TABLE category (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, title VARCHAR(50) NOT NULL, description VARCHAR(250) NULL, created_by_id BIGINT NULL, edited_date datetime NULL, uuid VARCHAR(255) NULL, CONSTRAINT PK_CATEGORY PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-category', 'omp', 'create/changelog-7.17.0-category.yaml', NOW(), 31, '8:4856c741c74da3051338cf04394a6080', 'createTable tableName=category', 'create ''category'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-category.yaml::7.17.0-category-constraints::omp
--  create 'category' constraints
ALTER TABLE category ADD CONSTRAINT FK302BCFE7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE category ADD CONSTRAINT FK302BCFEE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-category-constraints', 'omp', 'create/changelog-7.17.0-category.yaml', NOW(), 32, '8:af20b5d4f27385f8db710863d375b279', 'addForeignKeyConstraint baseTableName=category, constraintName=FK302BCFE7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=category, constraintName=FK302BCFEE31CB353, referencedTableName=profile', 'create ''category'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-category.yaml::7.17.0-category-indices::omp
--  create 'category' indices
CREATE UNIQUE INDEX category_uuid_idx ON category(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-category-indices', 'omp', 'create/changelog-7.17.0-category.yaml', NOW(), 33, '8:cc127d21beaa0e77e7775aacc4d4d841', 'createIndex indexName=category_uuid_idx, tableName=category', 'create ''category'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-contact_type.yaml::7.17.0-contact_type::omp
--  create 'contact_type' table
CREATE TABLE contact_type (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, edited_by_id BIGINT NULL, edited_date date NULL, required TINYINT(1) NOT NULL, title VARCHAR(50) NOT NULL, CONSTRAINT PK_CONTACT_TYPE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact_type', 'omp', 'create/changelog-7.17.0-contact_type.yaml', NOW(), 34, '8:f0025a90314449c9bfe2351b41ac9138', 'createTable tableName=contact_type', 'create ''contact_type'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-contact_type.yaml::7.17.0-contact_type-constraints::omp
--  create 'contact_type' constraints
ALTER TABLE contact_type ADD CONSTRAINT FK4C2BB7F97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE contact_type ADD CONSTRAINT FK4C2BB7F9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact_type-constraints', 'omp', 'create/changelog-7.17.0-contact_type.yaml', NOW(), 35, '8:2a4c5e940aa9d03cdb3955f8bc83330a', 'addForeignKeyConstraint baseTableName=contact_type, constraintName=FK4C2BB7F97666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=contact_type, constraintName=FK4C2BB7F9E31CB353, referencedTableName=profile', 'create ''contact_type'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-contact_type.yaml::7.17.0-contact_type-indices::omp
--  create 'contact_type' indices
CREATE INDEX FK4C2BB7F97666C6D2 ON contact_type(created_by_id);

CREATE INDEX FK4C2BB7F9E31CB353 ON contact_type(edited_by_id);

CREATE UNIQUE INDEX title_unique_1389723125532 ON contact_type(title);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact_type-indices', 'omp', 'create/changelog-7.17.0-contact_type.yaml', NOW(), 36, '8:27f1149bc3c5b3223093b61f6dcd6f7a', 'createIndex indexName=FK4C2BB7F97666C6D2, tableName=contact_type; createIndex indexName=FK4C2BB7F9E31CB353, tableName=contact_type; createIndex indexName=title_unique_1389723125532, tableName=contact_type', 'create ''contact_type'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition.yaml::7.17.0-custom_field_definition::omp
--  create 'custom_field_definition' table
CREATE TABLE custom_field_definition (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_date datetime NULL, tooltip VARCHAR(50) NULL, created_by_id BIGINT NULL, edited_date datetime NULL, edited_by_id BIGINT NULL, label VARCHAR(50) NOT NULL, description VARCHAR(250) NULL, is_required BIT(1) NOT NULL, name VARCHAR(50) NOT NULL, style_type VARCHAR(255) NOT NULL, uuid VARCHAR(255) NULL, section VARCHAR(255) NULL, all_types TINYINT(1) DEFAULT false NOT NULL, is_permanent BIT(1) NULL, CONSTRAINT PK_CUSTOM_FIELD_DEFINITION PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition', 'omp', 'create/changelog-7.17.0-custom_field_definition.yaml', NOW(), 37, '8:01cefe6eebcd18dd61f4712ddae67902', 'createTable tableName=custom_field_definition', 'create ''custom_field_definition'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition.yaml::7.17.0-custom_field_definition-constraints::omp
--  create 'custom_field_definition' constraints
ALTER TABLE custom_field_definition ADD CONSTRAINT FK150F70C6E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE custom_field_definition ADD CONSTRAINT FK150F70C67666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition-constraints', 'omp', 'create/changelog-7.17.0-custom_field_definition.yaml', NOW(), 38, '8:839294c66627c7010940bb3c169d2bd7', 'addForeignKeyConstraint baseTableName=custom_field_definition, constraintName=FK150F70C6E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=custom_field_definition, constraintName=FK150F70C67666C6D2, referencedTableName=pr...', 'create ''custom_field_definition'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition.yaml::7.17.0-custom_field_definition-indices::omp
--  create 'custom_field_definition' indices
CREATE UNIQUE INDEX cfd_uuid_idx ON custom_field_definition(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition-indices', 'omp', 'create/changelog-7.17.0-custom_field_definition.yaml', NOW(), 39, '8:ecf918813b35d797af8b4becc32f7759', 'createIndex indexName=cfd_uuid_idx, tableName=custom_field_definition', 'create ''custom_field_definition'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-images.yaml::7.17.0-images::omp
--  create 'images' table
CREATE TABLE images (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_date datetime NULL, is_default BIT(1) NOT NULL, type VARCHAR(255) NOT NULL, created_by_id BIGINT NULL, content_type VARCHAR(255) NULL, bytes MEDIUMBLOB NOT NULL, edited_date datetime NULL, edited_by_id BIGINT NULL, image_size DOUBLE NULL, CONSTRAINT PK_IMAGES PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-images', 'omp', 'create/changelog-7.17.0-images.yaml', NOW(), 40, '8:5ebf6a80e81a808b51b73e15e3168238', 'createTable tableName=images', 'create ''images'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-images.yaml::7.17.0-images-constraints::omp
--  create 'images' constraints
ALTER TABLE images ADD CONSTRAINT FKB95A8278E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE images ADD CONSTRAINT FKB95A82787666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-images-constraints', 'omp', 'create/changelog-7.17.0-images.yaml', NOW(), 41, '8:69ed3a2a2dc214beae6c59daa82db08f', 'addForeignKeyConstraint baseTableName=images, constraintName=FKB95A8278E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=images, constraintName=FKB95A82787666C6D2, referencedTableName=profile', 'create ''images'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_action.yaml::7.17.0-intent_action::omp
--  create 'intent_action' table
CREATE TABLE intent_action (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, description VARCHAR(256) NULL, edited_by_id BIGINT NULL, edited_date date NULL, title VARCHAR(256) NOT NULL, uuid VARCHAR(255) NULL, CONSTRAINT PK_INTENT_ACTION PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_action', 'omp', 'create/changelog-7.17.0-intent_action.yaml', NOW(), 42, '8:06f171d68337cb8e7a6ae245545d6ec7', 'createTable tableName=intent_action', 'create ''intent_action'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_action.yaml::7.17.0-intent_action-constraints::omp
--  create 'intent_action' constraints
ALTER TABLE intent_action ADD CONSTRAINT FKEBCDD397666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent_action ADD CONSTRAINT FKEBCDD39E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_action-constraints', 'omp', 'create/changelog-7.17.0-intent_action.yaml', NOW(), 43, '8:4312ce60d27dcb8ce8e03ad25afaf23e', 'addForeignKeyConstraint baseTableName=intent_action, constraintName=FKEBCDD397666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=intent_action, constraintName=FKEBCDD39E31CB353, referencedTableName=profile', 'create ''intent_action'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_action.yaml::7.17.0-intent_action-indices::omp
--  create 'intent_action' indices
CREATE INDEX FKEBCDD397666C6D2 ON intent_action(created_by_id);

CREATE INDEX FKEBCDD39E31CB353 ON intent_action(edited_by_id);

CREATE UNIQUE INDEX uuid_unique_1366321689429 ON intent_action(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_action-indices', 'omp', 'create/changelog-7.17.0-intent_action.yaml', NOW(), 44, '8:60f68376d431f715c54cd4726804b5a8', 'createIndex indexName=FKEBCDD397666C6D2, tableName=intent_action; createIndex indexName=FKEBCDD39E31CB353, tableName=intent_action; createIndex indexName=uuid_unique_1366321689429, tableName=intent_action', 'create ''intent_action'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_data_type.yaml::7.17.0-intent_data_type::omp
--  create 'intent_data_type' table
CREATE TABLE intent_data_type (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, description VARCHAR(256) NULL, edited_by_id BIGINT NULL, edited_date date NULL, title VARCHAR(256) NOT NULL, uuid VARCHAR(255) NULL, CONSTRAINT PK_INTENT_DATA_TYPE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_data_type', 'omp', 'create/changelog-7.17.0-intent_data_type.yaml', NOW(), 45, '8:96aca3b318b126ed1cd7fe72f3a2f065', 'createTable tableName=intent_data_type', 'create ''intent_data_type'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_data_type.yaml::7.17.0-intent_data_type-constraints::omp
--  create 'intent_data_type' constraints
ALTER TABLE intent_data_type ADD CONSTRAINT FKEADB30CC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent_data_type ADD CONSTRAINT FKEADB30CCE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_data_type-constraints', 'omp', 'create/changelog-7.17.0-intent_data_type.yaml', NOW(), 46, '8:a92ba5702785927628be2f1a56826f6a', 'addForeignKeyConstraint baseTableName=intent_data_type, constraintName=FKEADB30CC7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=intent_data_type, constraintName=FKEADB30CCE31CB353, referencedTableName=profile', 'create ''intent_data_type'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_data_type.yaml::7.17.0-intent_data_type-indices::omp
--  create 'intent_data_type' indices
CREATE INDEX FKEADB30CC7666C6D2 ON intent_data_type(created_by_id);

CREATE INDEX FKEADB30CCE31CB353 ON intent_data_type(edited_by_id);

CREATE UNIQUE INDEX uuid_unique_1366398847848 ON intent_data_type(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_data_type-indices', 'omp', 'create/changelog-7.17.0-intent_data_type.yaml', NOW(), 47, '8:f6ee8d4fcbfc6dca5eb344b738d7c124', 'createIndex indexName=FKEADB30CC7666C6D2, tableName=intent_data_type; createIndex indexName=FKEADB30CCE31CB353, tableName=intent_data_type; createIndex indexName=uuid_unique_1366398847848, tableName=intent_data_type', 'create ''intent_data_type'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_direction.yaml::7.17.0-intent_direction::omp
--  create 'intent_direction' table
CREATE TABLE intent_direction (created_by_id BIGINT NULL, created_date date NULL, description VARCHAR(250) NULL, edited_by_id BIGINT NULL, edited_date date NULL, id BIGINT AUTO_INCREMENT NOT NULL, title VARCHAR(7) NOT NULL, uuid VARCHAR(255) NULL, version BIGINT NOT NULL, CONSTRAINT PK_INTENT_DIRECTION PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_direction', 'omp', 'create/changelog-7.17.0-intent_direction.yaml', NOW(), 48, '8:22c02821088d568d39d83531331b1fc0', 'createTable tableName=intent_direction', 'create ''intent_direction'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_direction.yaml::7.17.0-intent_direction-constraints::omp
--  create 'intent_direction' constraints
ALTER TABLE intent_direction ADD CONSTRAINT FKC723A59C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent_direction ADD CONSTRAINT FKC723A59CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_direction-constraints', 'omp', 'create/changelog-7.17.0-intent_direction.yaml', NOW(), 49, '8:467b4a8f7c9277237a9bfe2f82e51a90', 'addForeignKeyConstraint baseTableName=intent_direction, constraintName=FKC723A59C7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=intent_direction, constraintName=FKC723A59CE31CB353, referencedTableName=profile', 'create ''intent_direction'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent_direction.yaml::7.17.0-intent_direction-indices::omp
--  create 'intent_direction' indices
CREATE INDEX FKC723A59C7666C6D2 ON intent_direction(created_by_id);

CREATE INDEX FKC723A59CE31CB353 ON intent_direction(edited_by_id);

CREATE UNIQUE INDEX title_unique_1366386256451 ON intent_direction(title);

CREATE UNIQUE INDEX uuid_unique_1366386256451 ON intent_direction(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent_direction-indices', 'omp', 'create/changelog-7.17.0-intent_direction.yaml', NOW(), 50, '8:a620ed929b1b47f5b3f275f8cb4aa9e3', 'createIndex indexName=FKC723A59C7666C6D2, tableName=intent_direction; createIndex indexName=FKC723A59CE31CB353, tableName=intent_direction; createIndex indexName=title_unique_1366386256451, tableName=intent_direction; createIndex indexName=uuid_un...', 'create ''intent_direction'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_properties.yaml::7.17.0-owf_properties::omp
--  create 'owf_properties' table
CREATE TABLE owf_properties (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, visible_in_launch BIT(1) NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, singleton BIT(1) NOT NULL, created_by_id BIGINT NULL, edited_date datetime NULL, background TINYINT(1) DEFAULT false NOT NULL, height BIGINT NULL, width BIGINT NULL, owf_widget_type VARCHAR(255) DEFAULT 'standard' NOT NULL, universal_name VARCHAR(255) NULL, stack_context VARCHAR(200) NULL, stack_descriptor LONGTEXT NULL, descriptor_url VARCHAR(2083) NULL, mobile_ready TINYINT(1) DEFAULT false NOT NULL, CONSTRAINT PK_OWF_PROPERTIES PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties', 'omp', 'create/changelog-7.17.0-owf_properties.yaml', NOW(), 51, '8:5c72934f4f5e3895fddd2836f6f3a866', 'createTable tableName=owf_properties', 'create ''owf_properties'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_properties.yaml::7.17.0-owf_properties-constraints::omp
--  create 'owf_properties' constraints
ALTER TABLE owf_properties ADD CONSTRAINT FKE88638947666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE owf_properties ADD CONSTRAINT FKE8863894E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties-constraints', 'omp', 'create/changelog-7.17.0-owf_properties.yaml', NOW(), 52, '8:778512247bbde04d30acaccc347f1625', 'addForeignKeyConstraint baseTableName=owf_properties, constraintName=FKE88638947666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=owf_properties, constraintName=FKE8863894E31CB353, referencedTableName=profile', 'create ''owf_properties'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_widget_types.yaml::7.17.0-owf_widget_types::omp
--  create 'owf_widget_types' table
CREATE TABLE owf_widget_types (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, description VARCHAR(255) NOT NULL, edited_by_id BIGINT NULL, edited_date date NULL, title VARCHAR(256) NOT NULL, uuid VARCHAR(255) NULL, CONSTRAINT PK_OWF_WIDGET_TYPES PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_widget_types', 'omp', 'create/changelog-7.17.0-owf_widget_types.yaml', NOW(), 53, '8:367e08c58812f4b3dad7ff036a129ee9', 'createTable tableName=owf_widget_types', 'create ''owf_widget_types'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_widget_types.yaml::7.17.0-owf_widget_types-constraints::omp
--  create 'owf_widget_types' constraints
ALTER TABLE owf_widget_types ADD CONSTRAINT FK6AB6A9DF7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE owf_widget_types ADD CONSTRAINT FK6AB6A9DFE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_widget_types-constraints', 'omp', 'create/changelog-7.17.0-owf_widget_types.yaml', NOW(), 54, '8:71ddfb566810644b566deb0c89bb73aa', 'addForeignKeyConstraint baseTableName=owf_widget_types, constraintName=FK6AB6A9DF7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=owf_widget_types, constraintName=FK6AB6A9DFE31CB353, referencedTableName=profile', 'create ''owf_widget_types'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_widget_types.yaml::7.17.0-owf_widget_types-indices::omp
--  create 'owf_widget_types' indices
CREATE UNIQUE INDEX uuid_unique_1366666109930 ON owf_widget_types(uuid);

CREATE INDEX FK6AB6A9DF7666C6D2 ON owf_widget_types(created_by_id);

CREATE INDEX FK6AB6A9DFE31CB353 ON owf_widget_types(edited_by_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_widget_types-indices', 'omp', 'create/changelog-7.17.0-owf_widget_types.yaml', NOW(), 55, '8:348c48ae2e1ff0d3a987958b8cde0402', 'createIndex indexName=uuid_unique_1366666109930, tableName=owf_widget_types; createIndex indexName=FK6AB6A9DF7666C6D2, tableName=owf_widget_types; createIndex indexName=FK6AB6A9DFE31CB353, tableName=owf_widget_types', 'create ''owf_widget_types'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_justification.yaml::7.17.0-rejection_justification::omp
--  create 'rejection_justification' table
CREATE TABLE rejection_justification (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, title VARCHAR(50) NOT NULL, description VARCHAR(250) NULL, created_by_id BIGINT NULL, edited_date datetime NULL, CONSTRAINT PK_REJECTION_JUSTIFICATION PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_justification', 'omp', 'create/changelog-7.17.0-rejection_justification.yaml', NOW(), 56, '8:2f5e5629b3c54a29ce8f43b83dfd04fd', 'createTable tableName=rejection_justification', 'create ''rejection_justification'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_justification.yaml::7.17.0-rejection_justification-constraints::omp
--  create 'rejection_justification' constraints
ALTER TABLE rejection_justification ADD CONSTRAINT FK12B0A53C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE rejection_justification ADD CONSTRAINT FK12B0A53CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_justification-constraints', 'omp', 'create/changelog-7.17.0-rejection_justification.yaml', NOW(), 57, '8:965782a0b6fecac7ee36cd7b07210990', 'addForeignKeyConstraint baseTableName=rejection_justification, constraintName=FK12B0A53C7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=rejection_justification, constraintName=FK12B0A53CE31CB353, referencedTableName=pr...', 'create ''rejection_justification'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-score_card_item.yaml::7.17.0-score_card_item::omp
--  create 'score_card_item' table
CREATE TABLE score_card_item (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date datetime NULL, description VARCHAR(500) NOT NULL, edited_by_id BIGINT NULL, edited_date datetime NULL, question VARCHAR(250) NOT NULL, image VARCHAR(255) NULL, show_on_listing TINYINT(1) NULL, CONSTRAINT PK_SCORE_CARD_ITEM PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-score_card_item', 'omp', 'create/changelog-7.17.0-score_card_item.yaml', NOW(), 58, '8:e1625d1ba9b52eb4b06da2d048ea3ade', 'createTable tableName=score_card_item', 'create ''score_card_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-score_card_item.yaml::7.17.0-score_card_item-constraints::omp
--  create 'score_card_item' constraints
ALTER TABLE score_card_item ADD CONSTRAINT FKE51CCD757666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE score_card_item ADD CONSTRAINT FKE51CCD75E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-score_card_item-constraints', 'omp', 'create/changelog-7.17.0-score_card_item.yaml', NOW(), 59, '8:e5db69ed465cc0b99125168ba8d6cc39', 'addForeignKeyConstraint baseTableName=score_card_item, constraintName=FKE51CCD757666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=score_card_item, constraintName=FKE51CCD75E31CB353, referencedTableName=profile', 'create ''score_card_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-score_card_item.yaml::7.17.0-score_card_item-indices::omp
--  create 'score_card_item' indices
CREATE INDEX FKE51CCD757666C6D2 ON score_card_item(created_by_id);

CREATE INDEX FKE51CCD75E31CB353 ON score_card_item(edited_by_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-score_card_item-indices', 'omp', 'create/changelog-7.17.0-score_card_item.yaml', NOW(), 60, '8:b0d9feae89e32e975cad2d7984cb7bdb', 'createIndex indexName=FKE51CCD757666C6D2, tableName=score_card_item; createIndex indexName=FKE51CCD75E31CB353, tableName=score_card_item', 'create ''score_card_item'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-state.yaml::7.17.0-state::omp
--  create 'state' table
CREATE TABLE state (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, is_published BIT(1) NOT NULL, title VARCHAR(50) NOT NULL, description VARCHAR(250) NULL, created_by_id BIGINT NULL, edited_date datetime NULL, uuid VARCHAR(255) NULL, CONSTRAINT PK_STATE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-state', 'omp', 'create/changelog-7.17.0-state.yaml', NOW(), 61, '8:2e038fca181b0ca0507a648d96709413', 'createTable tableName=state', 'create ''state'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-state.yaml::7.17.0-state-constraints::omp
--  create 'state' constraints
ALTER TABLE state ADD CONSTRAINT FK68AC4917666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE state ADD CONSTRAINT FK68AC491E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-state-constraints', 'omp', 'create/changelog-7.17.0-state.yaml', NOW(), 62, '8:68ff9ca5066bd6f60c0f13f00dd20f8d', 'addForeignKeyConstraint baseTableName=state, constraintName=FK68AC4917666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=state, constraintName=FK68AC491E31CB353, referencedTableName=profile', 'create ''state'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-state.yaml::7.17.0-state-indices::omp
--  create 'state' indices
CREATE UNIQUE INDEX state_uuid_idx ON state(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-state-indices', 'omp', 'create/changelog-7.17.0-state.yaml', NOW(), 63, '8:c14b14f3d9ba4c78874eea9d57d0348a', 'createIndex indexName=state_uuid_idx, tableName=state', 'create ''state'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-text.yaml::7.17.0-text::omp
--  create 'text' table
CREATE TABLE text (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, value VARCHAR(250) NULL, created_by_id BIGINT NULL, read_only BIT(1) NOT NULL, name VARCHAR(50) NOT NULL, edited_date datetime NULL, CONSTRAINT PK_TEXT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text', 'omp', 'create/changelog-7.17.0-text.yaml', NOW(), 64, '8:6ab20a5217ef6d3a8b5b53496727c7af', 'createTable tableName=text', 'create ''text'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-text.yaml::7.17.0-text-constraints::omp
--  create 'text' constraints
ALTER TABLE text ADD UNIQUE (name);

ALTER TABLE text ADD CONSTRAINT FK36452D7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE text ADD CONSTRAINT FK36452DE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-text-constraints', 'omp', 'create/changelog-7.17.0-text.yaml', NOW(), 65, '8:62c7b2582039f543616183cfae2cf076', 'addUniqueConstraint tableName=text; addForeignKeyConstraint baseTableName=text, constraintName=FK36452D7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=text, constraintName=FK36452DE31CB353, referencedTableName=profile', 'create ''text'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-user_account.yaml::7.17.0-user_account::omp
--  create 'user_account' table
CREATE TABLE user_account (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, username VARCHAR(250) NOT NULL, created_by_id BIGINT NULL, last_login datetime NULL, edited_date datetime NULL, CONSTRAINT PK_USER_ACCOUNT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-user_account', 'omp', 'create/changelog-7.17.0-user_account.yaml', NOW(), 66, '8:5e28892daed3dd358c3cefccedb7ccc4', 'createTable tableName=user_account', 'create ''user_account'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-user_account.yaml::7.17.0-user_account-constraints::omp
--  create 'user_account' constraints
ALTER TABLE user_account ADD UNIQUE (username);

ALTER TABLE user_account ADD CONSTRAINT FK14C321B97666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE user_account ADD CONSTRAINT FK14C321B9E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-user_account-constraints', 'omp', 'create/changelog-7.17.0-user_account.yaml', NOW(), 67, '8:32eed90e116f5ff7751d0acce94195c8', 'addUniqueConstraint tableName=user_account; addForeignKeyConstraint baseTableName=user_account, constraintName=FK14C321B97666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=user_account, constraintName=FK14C321B9E31CB353, ...', 'create ''user_account'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-u_domain.yaml::7.17.0-u_domain::omp
--  create 'u_domain' table
CREATE TABLE U_DOMAIN (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, username VARCHAR(255) NOT NULL, created_by_id BIGINT NULL, edited_date datetime NULL, CONSTRAINT PK_U_DOMAIN PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-u_domain', 'omp', 'create/changelog-7.17.0-u_domain.yaml', NOW(), 68, '8:679c6bcf8d6be129b321816c42d6c9e3', 'createTable tableName=U_DOMAIN', 'create ''u_domain'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-u_domain.yaml::7.17.0-u_domain-constraints::omp
--  create 'u_domain' constraints
ALTER TABLE U_DOMAIN ADD CONSTRAINT FK97BAABEE7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE U_DOMAIN ADD CONSTRAINT FK97BAABEEE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-u_domain-constraints', 'omp', 'create/changelog-7.17.0-u_domain.yaml', NOW(), 69, '8:0df4a8a52d52420f0f96161c30aa4771', 'addForeignKeyConstraint baseTableName=U_DOMAIN, constraintName=FK97BAABEE7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=U_DOMAIN, constraintName=FK97BAABEEE31CB353, referencedTableName=profile', 'create ''u_domain'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field::omp
--  create 'custom_field' table
CREATE TABLE custom_field (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, custom_field_definition_id BIGINT NOT NULL, created_by_id BIGINT NULL, edited_date datetime NULL, CONSTRAINT PK_CUSTOM_FIELD PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field', 'omp', 'create/changelog-7.17.0-custom_field.yaml', NOW(), 70, '8:e470298e18c6be2123e0233cebc9c9fd', 'createTable tableName=custom_field', 'create ''custom_field'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field-constraints::omp
--  create 'custom_field' constraints
ALTER TABLE custom_field ADD CONSTRAINT FK2ACD76AC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE custom_field ADD CONSTRAINT FK2ACD76ACE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE custom_field ADD CONSTRAINT FK2ACD76AC6F62C9ED FOREIGN KEY (custom_field_definition_id) REFERENCES custom_field_definition (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field-constraints', 'omp', 'create/changelog-7.17.0-custom_field.yaml', NOW(), 71, '8:7b6a15576049afc2a0784f1dd7dc6a24', 'addForeignKeyConstraint baseTableName=custom_field, constraintName=FK2ACD76AC7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=custom_field, constraintName=FK2ACD76ACE31CB353, referencedTableName=profile; addForeignKeyCo...', 'create ''custom_field'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field-indices_mysql::omp
--  create 'custom_field' indices for mysql
CREATE INDEX FK2ACD76AC6F62C9ED ON custom_field(custom_field_definition_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field-indices_mysql', 'omp', 'create/changelog-7.17.0-custom_field.yaml', NOW(), 72, '8:4a6aef6cfa8bf33a73071e51edce92f7', 'createIndex indexName=FK2ACD76AC6F62C9ED, tableName=custom_field', 'create ''custom_field'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field.yaml::7.17.0-custom_field-indices::omp
--  create 'custom_field' indices
CREATE INDEX cf_cfd_idx ON custom_field(custom_field_definition_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field-indices', 'omp', 'create/changelog-7.17.0-custom_field.yaml', NOW(), 73, '8:00c5d997fcca7b32ad1ea8ebcf9a49d9', 'createIndex indexName=cf_cfd_idx, tableName=custom_field', 'create ''custom_field'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field::omp
--  create 'service_item_custom_field' table
CREATE TABLE service_item_custom_field (service_item_custom_fields_id BIGINT NULL, custom_field_id BIGINT NULL, custom_fields_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', NOW(), 74, '8:0cb41f69b2f228cf7a823971c73724c5', 'createTable tableName=service_item_custom_field', 'create ''service_item_custom_field'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-constraints::omp
--  create 'service_item_custom_field' constraints
ALTER TABLE service_item_custom_field ADD CONSTRAINT FK46E9894E7B56E054 FOREIGN KEY (custom_field_id) REFERENCES custom_field (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-constraints', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', NOW(), 75, '8:b8a17dcf815b2e145efd514d5fdc84d7', 'addForeignKeyConstraint baseTableName=service_item_custom_field, constraintName=FK46E9894E7B56E054, referencedTableName=custom_field', 'create ''service_item_custom_field'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-indices_1::omp
--  create 'service_item_custom_field' indices
CREATE INDEX svc_item_cst_fld_id_idx ON service_item_custom_field(service_item_custom_fields_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-indices_1', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', NOW(), 76, '8:aeae086b1fbf38df9180593a672d74b5', 'createIndex indexName=svc_item_cst_fld_id_idx, tableName=service_item_custom_field', 'create ''service_item_custom_field'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-indices_mysql::omp
--  create 'service_item_custom_field' indices for mysql
CREATE INDEX FK46E9894E7B56E054 ON service_item_custom_field(custom_field_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-indices_mysql', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', NOW(), 77, '8:cd4754015b59317d8c2fdd8168d2342d', 'createIndex indexName=FK46E9894E7B56E054, tableName=service_item_custom_field', 'create ''service_item_custom_field'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_custom_field.yaml::7.17.0-service_item_custom_field-indices_2::omp
--  create 'service_item_custom_field' indices
CREATE INDEX si_cf_cf_id_idx ON service_item_custom_field(custom_field_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_custom_field-indices_2', 'omp', 'create/changelog-7.17.0-service_item_custom_field.yaml', NOW(), 78, '8:67b83331e3f9a419cd92ed60a413f68c', 'createIndex indexName=si_cf_cf_id_idx, tableName=service_item_custom_field', 'create ''service_item_custom_field'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value::omp
--  create 'field_value' table
CREATE TABLE field_value (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, custom_field_definition_id BIGINT NOT NULL, is_enabled INT NOT NULL, display_text VARCHAR(255) NOT NULL, created_by_id BIGINT NULL, edited_date datetime NULL, field_values_idx INT NULL, CONSTRAINT PK_FIELD_VALUE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value', 'omp', 'create/changelog-7.17.0-field_value.yaml', NOW(), 79, '8:54c4d1ac4f4ad371058d6f8d05fe2a2e', 'createTable tableName=field_value', 'create ''field_value'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value-constraints::omp
--  create 'field_value' constraints
ALTER TABLE field_value ADD CONSTRAINT FK29F571EC7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE field_value ADD CONSTRAINT FK29F571ECE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE field_value ADD CONSTRAINT FK29F571ECF1F14D3C FOREIGN KEY (custom_field_definition_id) REFERENCES drop_down_cfd (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value-constraints', 'omp', 'create/changelog-7.17.0-field_value.yaml', NOW(), 80, '8:acc6a3f7e4775645057775a1d12ebcea', 'addForeignKeyConstraint baseTableName=field_value, constraintName=FK29F571EC7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=field_value, constraintName=FK29F571ECE31CB353, referencedTableName=profile; addForeignKeyCons...', 'create ''field_value'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value-indices_mysql::omp
--  create 'field_value' indices for mysql
CREATE INDEX FK29F571ECF1F14D3C ON field_value(custom_field_definition_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value-indices_mysql', 'omp', 'create/changelog-7.17.0-field_value.yaml', NOW(), 81, '8:0f520f4756bb5a814d18ca0b089e66aa', 'createIndex indexName=FK29F571ECF1F14D3C, tableName=field_value', 'create ''field_value'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-field_value.yaml::7.17.0-field_value-indices::omp
--  create 'field_value' indices
CREATE INDEX field_value_cfd_idx ON field_value(custom_field_definition_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-field_value-indices', 'omp', 'create/changelog-7.17.0-field_value.yaml', NOW(), 82, '8:5015c67c693d7df754d37bbce024fc74', 'createIndex indexName=field_value_cfd_idx, tableName=field_value', 'create ''field_value'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-drop_down_cf.yaml::7.17.0-drop_down_cf::omp
--  create 'drop_down_cf' table
CREATE TABLE drop_down_cf (id BIGINT NOT NULL, value_id BIGINT NULL, CONSTRAINT PK_DROP_DOWN_CF PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf', 'omp', 'create/changelog-7.17.0-drop_down_cf.yaml', NOW(), 83, '8:840763ef6984033bef988d09921ba2e3', 'createTable tableName=drop_down_cf', 'create ''drop_down_cf'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-drop_down_cf.yaml::7.17.0-drop_down_cf-constraints::omp
--  create 'drop_down_cf' constraints
ALTER TABLE drop_down_cf ADD CONSTRAINT FK13ADE7D0BC98CEE3 FOREIGN KEY (value_id) REFERENCES field_value (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf-constraints', 'omp', 'create/changelog-7.17.0-drop_down_cf.yaml', NOW(), 84, '8:e7fccd29cc0c8123e56a4f71ec3df69a', 'addForeignKeyConstraint baseTableName=drop_down_cf, constraintName=FK13ADE7D0BC98CEE3, referencedTableName=field_value', 'create ''drop_down_cf'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-drop_down_cf_field_value.yaml::7.17.0-drop_down_cf_field_value::omp
--  create 'drop_down_cf_field_value' table
CREATE TABLE drop_down_cf_field_value (drop_down_cf_field_value_id BIGINT NULL, field_value_id BIGINT NULL, field_value_list_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf_field_value', 'omp', 'create/changelog-7.17.0-drop_down_cf_field_value.yaml', NOW(), 85, '8:b1c2815795fb74564be3097caf7802af', 'createTable tableName=drop_down_cf_field_value', 'create ''drop_down_cf_field_value'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-drop_down_cf_field_value.yaml::7.17.0-drop_down_cf_field_value-constraints::omp
--  create 'drop_down_cf_field_value' constraints
ALTER TABLE drop_down_cf_field_value ADD CONSTRAINT FK2627FFDDA5BD888 FOREIGN KEY (field_value_id) REFERENCES field_value (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf_field_value-constraints', 'omp', 'create/changelog-7.17.0-drop_down_cf_field_value.yaml', NOW(), 86, '8:dcb262802d76f5dde8e2d5f35e558104', 'addForeignKeyConstraint baseTableName=drop_down_cf_field_value, constraintName=FK2627FFDDA5BD888, referencedTableName=field_value', 'create ''drop_down_cf_field_value'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-drop_down_cf_field_value.yaml::7.17.0-drop_down_cf_field_value-indices::omp
--  create 'drop_down_cf_field_value' indices
CREATE INDEX FK2627FFDDA5BD888 ON drop_down_cf_field_value(field_value_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-drop_down_cf_field_value-indices', 'omp', 'create/changelog-7.17.0-drop_down_cf_field_value.yaml', NOW(), 87, '8:959b4322632a98c4021748671cda3e7d', 'createIndex indexName=FK2627FFDDA5BD888, tableName=drop_down_cf_field_value', 'create ''drop_down_cf_field_value'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category::omp
--  create 'service_item_category' table
CREATE TABLE service_item_category (service_item_categories_id BIGINT NULL, category_id BIGINT NULL, categories_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', NOW(), 88, '8:f1172f0816aaea39519a6a33db558851', 'createTable tableName=service_item_category', 'create ''service_item_category'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-constraints::omp
--  create 'service_item_category' constraints
ALTER TABLE service_item_category ADD CONSTRAINT FKECC570A0DA41995D FOREIGN KEY (category_id) REFERENCES category (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-constraints', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', NOW(), 89, '8:1f76ff702bbce596cab57c462afbb869', 'addForeignKeyConstraint baseTableName=service_item_category, constraintName=FKECC570A0DA41995D, referencedTableName=category', 'create ''service_item_category'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-indices_1::omp
--  create 'service_item_category' indices
CREATE INDEX svc_item_cat_id_idx ON service_item_category(service_item_categories_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-indices_1', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', NOW(), 90, '8:472fb375b06d06f5ef4617630a73cb6e', 'createIndex indexName=svc_item_cat_id_idx, tableName=service_item_category', 'create ''service_item_category'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-indices_mysql::omp
--  create 'service_item_category' indices for mysql
CREATE INDEX FKECC570A0DA41995D ON service_item_category(category_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-indices_mysql', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', NOW(), 91, '8:8c09564e90bed86a5f0b82f2d1c76833', 'createIndex indexName=FKECC570A0DA41995D, tableName=service_item_category', 'create ''service_item_category'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_category.yaml::7.17.0-service_item_category-indices_2::omp
--  create 'service_item_category' indices
CREATE INDEX si_cat_cat_id_idx ON service_item_category(category_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_category-indices_2', 'omp', 'create/changelog-7.17.0-service_item_category.yaml', NOW(), 92, '8:ac315a8a967a207abdfe2c4bd4188125', 'createIndex indexName=si_cat_cat_id_idx, tableName=service_item_category', 'create ''service_item_category'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-import_task.yaml::7.17.0-import_task::omp
--  create 'import_task' table
CREATE TABLE import_task (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_by_id BIGINT NULL, created_date datetime NULL, cron_exp VARCHAR(255) NULL, edited_by_id BIGINT NULL, edited_date datetime NULL, enabled BIT(1) NOT NULL, exec_interval INT NULL, extra_url_params VARCHAR(512) NULL, interface_config_id BIGINT NOT NULL, last_run_result_id BIGINT NULL, name VARCHAR(50) NOT NULL, update_type VARCHAR(7) NOT NULL, url VARCHAR(255) NULL, keystore_pass VARCHAR(2048) NULL, keystore_path VARCHAR(2048) NULL, truststore_path VARCHAR(2048) NULL, CONSTRAINT PK_IMPORT_TASK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task', 'omp', 'create/changelog-7.17.0-import_task.yaml', NOW(), 93, '8:20ea54d0649d24015eab9100a3d64093', 'createTable tableName=import_task', 'create ''import_task'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-import_task_result.yaml::7.17.0-import_task_result::omp
--  create 'import_task_result' table
CREATE TABLE import_task_result (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, message VARCHAR(4000) NOT NULL, result BIT(1) NOT NULL, run_date datetime NOT NULL, task_id BIGINT NOT NULL, CONSTRAINT PK_IMPORT_TASK_RESULT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task_result', 'omp', 'create/changelog-7.17.0-import_task_result.yaml', NOW(), 94, '8:af3efdd2a109c1b9ee4f189eabaafbee', 'createTable tableName=import_task_result', 'create ''import_task_result'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-import_task_result.yaml::7.17.0-import_task_result-constraints::omp
--  create 'import_task_result' constraints
ALTER TABLE import_task_result ADD CONSTRAINT FK983AC27D11D7F882 FOREIGN KEY (task_id) REFERENCES import_task (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task_result-constraints', 'omp', 'create/changelog-7.17.0-import_task_result.yaml', NOW(), 95, '8:ad7643713bb471b687a43e7cbc1e6cc3', 'addForeignKeyConstraint baseTableName=import_task_result, constraintName=FK983AC27D11D7F882, referencedTableName=import_task', 'create ''import_task_result'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-import_task-fk.yaml::7.17.0-import_task-constraints::omp
--  create 'import_task' constraints
ALTER TABLE import_task ADD UNIQUE (name);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DF7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DFE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DFA31F8712 FOREIGN KEY (interface_config_id) REFERENCES interface_configuration (id);

ALTER TABLE import_task ADD CONSTRAINT FK578EF9DF919216CA FOREIGN KEY (last_run_result_id) REFERENCES import_task_result (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-import_task-constraints', 'omp', 'create/changelog-7.17.0-import_task-fk.yaml', NOW(), 96, '8:78e2f50b13206f83aca41db8909a74a8', 'addUniqueConstraint tableName=import_task; addForeignKeyConstraint baseTableName=import_task, constraintName=FK578EF9DF7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=import_task, constraintName=FK578EF9DFE31CB353, ref...', 'create ''import_task'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent.yaml::7.17.0-intent::omp
--  create 'intent' table
CREATE TABLE intent (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, action_id BIGINT NOT NULL, created_by_id BIGINT NULL, created_date date NULL, data_type_id BIGINT NOT NULL, edited_by_id BIGINT NULL, edited_date date NULL, receive TINYINT(1) NOT NULL, send TINYINT(1) NOT NULL, CONSTRAINT PK_INTENT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent', 'omp', 'create/changelog-7.17.0-intent.yaml', NOW(), 97, '8:3103a16038fa1649a1e0f56062d7bfa8', 'createTable tableName=intent', 'create ''intent'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent.yaml::7.17.0-intent-constraints::omp
--  create 'intent' constraints
ALTER TABLE intent ADD CONSTRAINT FKB971369CD8544299 FOREIGN KEY (action_id) REFERENCES intent_action (id);

ALTER TABLE intent ADD CONSTRAINT FKB971369C7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE intent ADD CONSTRAINT FKB971369C283F938E FOREIGN KEY (data_type_id) REFERENCES intent_data_type (id);

ALTER TABLE intent ADD CONSTRAINT FKB971369CE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent-constraints', 'omp', 'create/changelog-7.17.0-intent.yaml', NOW(), 98, '8:3a931477e946bc4c3a4db4618c26f67f', 'addForeignKeyConstraint baseTableName=intent, constraintName=FKB971369CD8544299, referencedTableName=intent_action; addForeignKeyConstraint baseTableName=intent, constraintName=FKB971369C7666C6D2, referencedTableName=profile; addForeignKeyConstrai...', 'create ''intent'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-intent.yaml::7.17.0-intent-indices::omp
--  create 'intent' indices
CREATE INDEX FKB971369CD8544299 ON intent(action_id);

CREATE INDEX FKB971369C7666C6D2 ON intent(created_by_id);

CREATE INDEX FKB971369C283F938E ON intent(data_type_id);

CREATE INDEX FKB971369CE31CB353 ON intent(edited_by_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-intent-indices', 'omp', 'create/changelog-7.17.0-intent.yaml', NOW(), 99, '8:7f472e42d2ae4ae9e81dd5b5edfd1792', 'createIndex indexName=FKB971369CD8544299, tableName=intent; createIndex indexName=FKB971369C7666C6D2, tableName=intent; createIndex indexName=FKB971369C283F938E, tableName=intent; createIndex indexName=FKB971369CE31CB353, tableName=intent', 'create ''intent'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_properties_intent.yaml::7.17.0-owf_properties_intent::omp
--  create 'owf_properties_intent 'table
CREATE TABLE owf_properties_intent (owf_properties_intents_id BIGINT NULL, intent_id BIGINT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties_intent', 'omp', 'create/changelog-7.17.0-owf_properties_intent.yaml', NOW(), 100, '8:24ed1960c6306350c7ceab5afc72db04', 'createTable tableName=owf_properties_intent', 'create ''owf_properties_intent ''table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_properties_intent.yaml::7.17.0-owf_properties_intent-constraints::omp
--  create 'owf_properties_intent' constraints
ALTER TABLE owf_properties_intent ADD CONSTRAINT FK3F99ECA7A651895D FOREIGN KEY (intent_id) REFERENCES intent (id);

ALTER TABLE owf_properties_intent ADD CONSTRAINT FK3F99ECA74704E25C FOREIGN KEY (owf_properties_intents_id) REFERENCES owf_properties (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties_intent-constraints', 'omp', 'create/changelog-7.17.0-owf_properties_intent.yaml', NOW(), 101, '8:ed3de2ab4ee2f373217f22f4fcb2b5c4', 'addForeignKeyConstraint baseTableName=owf_properties_intent, constraintName=FK3F99ECA7A651895D, referencedTableName=intent; addForeignKeyConstraint baseTableName=owf_properties_intent, constraintName=FK3F99ECA74704E25C, referencedTableName=owf_pro...', 'create ''owf_properties_intent'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-owf_properties_intent.yaml::7.17.0-owf_properties_intent-indices::omp
--  create 'owf_properties_intent' indices
CREATE INDEX FK3F99ECA7A651895D ON owf_properties_intent(intent_id);

CREATE INDEX owfProps_intent_id_idx ON owf_properties_intent(owf_properties_intents_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-owf_properties_intent-indices', 'omp', 'create/changelog-7.17.0-owf_properties_intent.yaml', NOW(), 102, '8:0d8fd74e6eed892c5706c7a6ba3d4e60', 'createIndex indexName=FK3F99ECA7A651895D, tableName=owf_properties_intent; createIndex indexName=owfProps_intent_id_idx, tableName=owf_properties_intent', 'create ''owf_properties_intent'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-default_images.yaml::7.17.0-default_images::omp
--  create 'default_images' table
CREATE TABLE default_images (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, image_id BIGINT NOT NULL, created_by_id BIGINT NULL, defined_default_type VARCHAR(255) NOT NULL, edited_date datetime NULL, CONSTRAINT PK_DEFAULT_IMAGES PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-default_images', 'omp', 'create/changelog-7.17.0-default_images.yaml', NOW(), 103, '8:36294217b7d9363665bb3f09b72576bd', 'createTable tableName=default_images', 'create ''default_images'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-default_images.yaml::7.17.0-default_images-constraints::omp
--  create 'default_images' constraints
ALTER TABLE default_images ADD CONSTRAINT FK6F064E367666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE default_images ADD CONSTRAINT FK6F064E36E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE default_images ADD CONSTRAINT FK6F064E36553AF61A FOREIGN KEY (image_id) REFERENCES images (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-default_images-constraints', 'omp', 'create/changelog-7.17.0-default_images.yaml', NOW(), 104, '8:ba39c2c040d6e9d3eebc40a272e65cd6', 'addForeignKeyConstraint baseTableName=default_images, constraintName=FK6F064E367666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=default_images, constraintName=FK6F064E36E31CB353, referencedTableName=profile; addForeignK...', 'create ''default_images'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-types.yaml::7.17.0-types::omp
--  create 'types' table
CREATE TABLE types (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_date datetime NULL, title VARCHAR(50) NOT NULL, created_by_id BIGINT NULL, edited_date datetime NULL, edited_by_id BIGINT NULL, has_launch_url BIT(1) NOT NULL, description VARCHAR(250) NULL, image_id BIGINT NULL, ozone_aware BIT(1) NOT NULL, has_icons BIT(1) NOT NULL, uuid VARCHAR(255) NULL, is_permanent TINYINT(1) NULL, CONSTRAINT PK_TYPES PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-types', 'omp', 'create/changelog-7.17.0-types.yaml', NOW(), 105, '8:f5bf0480d28bf63450e5c28e7030e84a', 'createTable tableName=types', 'create ''types'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-types.yaml::7.17.0-types-constraints::omp
--  create 'types' constraints
ALTER TABLE types ADD CONSTRAINT FK69B5879E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE types ADD CONSTRAINT FK69B58797666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE types ADD CONSTRAINT FK69B5879553AF61A FOREIGN KEY (image_id) REFERENCES images (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-types-constraints', 'omp', 'create/changelog-7.17.0-types.yaml', NOW(), 106, '8:0f5fdcc24cbe24240338fd32dbcea041', 'addForeignKeyConstraint baseTableName=types, constraintName=FK69B5879E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=types, constraintName=FK69B58797666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTab...', 'create ''types'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-types.yaml::7.17.0-types-indices::omp
--  create 'types' indices
CREATE UNIQUE INDEX types_uuid_idx ON types(uuid);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-types-indices', 'omp', 'create/changelog-7.17.0-types.yaml', NOW(), 107, '8:807f6b4487507ee33157c3fbd79c6fe3', 'createIndex indexName=types_uuid_idx, tableName=types', 'create ''types'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item.yaml::7.17.0-service_item::omp
--  create 'service_item' table
CREATE TABLE service_item (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_date datetime NULL, owf_properties_id BIGINT NULL, avg_rate FLOAT NOT NULL, approval_status VARCHAR(11) NOT NULL, title VARCHAR(256) NOT NULL, image_small_url VARCHAR(2083) NULL, image_large_url VARCHAR(2083) NULL, total_votes INT NOT NULL, launch_url VARCHAR(2083) NULL, uuid VARCHAR(255) NOT NULL, version_name VARCHAR(256) NULL, release_date datetime NULL, organization VARCHAR(256) NULL, dependencies VARCHAR(1000) NULL, types_id BIGINT NOT NULL, created_by_id BIGINT NULL, requirements VARCHAR(1000) NULL, edited_date datetime NULL, edited_by_id BIGINT NULL, total_comments INT NOT NULL, is_hidden INT NOT NULL, description VARCHAR(4000) NULL, state_id BIGINT NULL, install_url VARCHAR(2083) NULL, last_activity_id BIGINT NULL, total_rate1 INT NULL, total_rate2 INT NULL, total_rate3 INT NULL, total_rate4 INT NULL, total_rate5 INT NULL, approval_date datetime NULL, is_outside BIT(1) NULL, agency_id BIGINT NULL, opens_in_new_browser_tab TINYINT(1) NULL, image_medium_url VARCHAR(2083) NULL, CONSTRAINT PK_SERVICE_ITEM PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item', 'omp', 'create/changelog-7.17.0-service_item.yaml', NOW(), 108, '8:5fd7d55391a9537e5035d4283439b62d', 'createTable tableName=service_item', 'create ''service_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item.yaml::7.17.0-service_item-indices_mysql::omp
--  create 'service_item' indices for mysql
CREATE INDEX FK1571565D2746B676 ON service_item(last_activity_id);

CREATE INDEX FK1571565D904D6974 ON service_item(owf_properties_id);

CREATE INDEX FK1571565DE31CB353 ON service_item(edited_by_id);

CREATE INDEX FK1571565D7666C6D2 ON service_item(created_by_id);

CREATE INDEX FK1571565D6928D597 ON service_item(types_id);

CREATE INDEX FK1571565DDFEC3E97 ON service_item(state_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item-indices_mysql', 'omp', 'create/changelog-7.17.0-service_item.yaml', NOW(), 109, '8:990b3869997af545788212ed526ca695', 'createIndex indexName=FK1571565D2746B676, tableName=service_item; createIndex indexName=FK1571565D904D6974, tableName=service_item; createIndex indexName=FK1571565DE31CB353, tableName=service_item; createIndex indexName=FK1571565D7666C6D2, tableNa...', 'create ''service_item'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item.yaml::7.17.0-service_item-indices::omp
--  create 'service_item' indices
CREATE INDEX si_owf_props_id_idx ON service_item(owf_properties_id);

CREATE INDEX si_types_id_idx ON service_item(types_id);

CREATE INDEX si_state_id_idx ON service_item(state_id);

CREATE INDEX si_last_activity_idx ON service_item(last_activity_id);

CREATE INDEX si_created_by_id_idx ON service_item(created_by_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item-indices', 'omp', 'create/changelog-7.17.0-service_item.yaml', NOW(), 110, '8:5485e1642748dc5f4926b38606c2429e', 'createIndex indexName=si_owf_props_id_idx, tableName=service_item; createIndex indexName=si_types_id_idx, tableName=service_item; createIndex indexName=si_state_id_idx, tableName=service_item; createIndex indexName=si_last_activity_idx, tableName=...', 'create ''service_item'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag::omp
--  create 'service_item_tag' table
CREATE TABLE service_item_tag (id BIGINT AUTO_INCREMENT NOT NULL, service_item_id BIGINT NOT NULL, tag_id BIGINT NOT NULL, created_by_id BIGINT NULL, version BIGINT NOT NULL, CONSTRAINT service_item_tag_PK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', NOW(), 111, '8:778294bff5a45b34b9d2012434725ca3', 'createTable tableName=service_item_tag', 'create ''service_item_tag'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag-constraints::omp
--  create 'service_item_tag' constraints
ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_unique_idx UNIQUE (service_item_id, tag_id);

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_FK_si FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_FK_tag FOREIGN KEY (tag_id) REFERENCES tag (id);

ALTER TABLE service_item_tag ADD CONSTRAINT service_item_tag_FK_cb FOREIGN KEY (created_by_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag-constraints', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', NOW(), 112, '8:ab179435b4370660b5e67843f41eed38', 'addUniqueConstraint constraintName=service_item_tag_unique_idx, tableName=service_item_tag; addForeignKeyConstraint baseTableName=service_item_tag, constraintName=service_item_tag_FK_si, referencedTableName=service_item; addForeignKeyConstraint ba...', 'create ''service_item_tag'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag-indices::omp
--  create 'service_item_tag' indices
CREATE INDEX service_item_tag_si_idx ON service_item_tag(service_item_id);

CREATE INDEX service_item_tag_tag_idx ON service_item_tag(tag_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag-indices', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', NOW(), 113, '8:4ecbc89b508c686bc14581475c3c52ec', 'createIndex indexName=service_item_tag_si_idx, tableName=service_item_tag; createIndex indexName=service_item_tag_tag_idx, tableName=service_item_tag', 'create ''service_item_tag'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_tag.yaml::7.17.0-service_item_tag-indices_mysql::omp
--  create 'service_item_tag' indices for mysql
CREATE INDEX service_item_tag_FK_cb ON service_item_tag(created_by_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tag-indices_mysql', 'omp', 'create/changelog-7.17.0-service_item_tag.yaml', NOW(), 114, '8:5ee1d033dccf999a82d448e4132b79f7', 'createIndex indexName=service_item_tag_FK_cb, tableName=service_item_tag', 'create ''service_item_tag'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_score_card_item.yaml::7.17.0-service_item_score_card_item::omp
--  create 'service_item_score_card_item' table
CREATE TABLE service_item_score_card_item (service_item_id BIGINT NULL, score_card_item_id BIGINT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_score_card_item', 'omp', 'create/changelog-7.17.0-service_item_score_card_item.yaml', NOW(), 115, '8:ca78a90fdfc0d9dfb5fef41a7c657767', 'createTable tableName=service_item_score_card_item', 'create ''service_item_score_card_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_score_card_item.yaml::7.17.0-service_item_score_card_item-constraints::omp
--  create 'service_item_score_card_item' constraints
ALTER TABLE service_item_score_card_item ADD CONSTRAINT FKBF91F93EF469C97 FOREIGN KEY (score_card_item_id) REFERENCES score_card_item (id) ON DELETE CASCADE;

ALTER TABLE service_item_score_card_item ADD CONSTRAINT FKBF91F939C51FA9F FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_score_card_item-constraints', 'omp', 'create/changelog-7.17.0-service_item_score_card_item.yaml', NOW(), 116, '8:90efc3501fc3bee39d920e23b7d397e7', 'addForeignKeyConstraint baseTableName=service_item_score_card_item, constraintName=FKBF91F93EF469C97, referencedTableName=score_card_item; addForeignKeyConstraint baseTableName=service_item_score_card_item, constraintName=FKBF91F939C51FA9F, refere...', 'create ''service_item_score_card_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity::omp
--  create 'service_item_activity' table
CREATE TABLE service_item_activity (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, created_date datetime NULL, action VARCHAR(255) NOT NULL, service_item_id BIGINT NOT NULL, created_by_id BIGINT NULL, edited_date datetime NULL, edited_by_id BIGINT NULL, activity_timestamp datetime NOT NULL, author_id BIGINT NOT NULL, service_item_activities_idx INT NULL, CONSTRAINT PK_SERVICE_ITEM_ACTIVITY PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', NOW(), 117, '8:d16730a1f20c7fed4b1fc7e1aed8cf65', 'createTable tableName=service_item_activity', 'create ''service_item_activity'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity-indices::omp
--  create 'service_item_activity' indices
CREATE INDEX svc_item_act_svc_item_id_idx ON service_item_activity(service_item_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity-indices', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', NOW(), 118, '8:76b4bc37012b0e17630772110b7d0f92', 'createIndex indexName=svc_item_act_svc_item_id_idx, tableName=service_item_activity', 'create ''service_item_activity'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity-indices_mysql::omp
--  create 'service_item_activity' indices for mysql
CREATE INDEX FK870EA6B1E31CB353 ON service_item_activity(edited_by_id);

CREATE INDEX FK870EA6B17666C6D2 ON service_item_activity(created_by_id);

CREATE INDEX FK870EA6B1C7E5C662 ON service_item_activity(service_item_id);

CREATE INDEX FK870EA6B15A032135 ON service_item_activity(author_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity-indices_mysql', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', NOW(), 119, '8:0f8c3c82518314fccf2ebb2c9897d2fe', 'createIndex indexName=FK870EA6B1E31CB353, tableName=service_item_activity; createIndex indexName=FK870EA6B17666C6D2, tableName=service_item_activity; createIndex indexName=FK870EA6B1C7E5C662, tableName=service_item_activity; createIndex indexName=...', 'create ''service_item_activity'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_activity.yaml::7.17.0-service_item_activity-constraints::omp
--  create 'service_item_activity' constraints
ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B1E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B17666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B1C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE service_item_activity ADD CONSTRAINT FK870EA6B15A032135 FOREIGN KEY (author_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_activity-constraints', 'omp', 'create/changelog-7.17.0-service_item_activity.yaml', NOW(), 120, '8:42e095fdff9e0b9a4aa9192b70212fa2', 'addForeignKeyConstraint baseTableName=service_item_activity, constraintName=FK870EA6B1E31CB353, referencedTableName=profile; addForeignKeyConstraint baseTableName=service_item_activity, constraintName=FK870EA6B17666C6D2, referencedTableName=profil...', 'create ''service_item_activity'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item-fk.yaml::7.17.0-service_item-fk::omp
--  create 'service_item' foreign keys
ALTER TABLE service_item ADD CONSTRAINT FK1571565D2746B676 FOREIGN KEY (last_activity_id) REFERENCES service_item_activity (id);

ALTER TABLE service_item ADD CONSTRAINT FK1571565D904D6974 FOREIGN KEY (owf_properties_id) REFERENCES owf_properties (id);

ALTER TABLE service_item ADD CONSTRAINT FK1571565DE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE service_item ADD CONSTRAINT FK1571565D7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE service_item ADD CONSTRAINT FK1571565D6928D597 FOREIGN KEY (types_id) REFERENCES types (id);

ALTER TABLE service_item ADD CONSTRAINT FK1571565DDFEC3E97 FOREIGN KEY (state_id) REFERENCES state (id);

ALTER TABLE service_item ADD CONSTRAINT SERVICE_ITEM_AGENCY_FK FOREIGN KEY (agency_id) REFERENCES agency (id) ON DELETE SET NULL;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item-fk', 'omp', 'create/changelog-7.17.0-service_item-fk.yaml', NOW(), 121, '8:f9e6f6b0668a2ee9531af78b3c78e90a', 'addForeignKeyConstraint baseTableName=service_item, constraintName=FK1571565D2746B676, referencedTableName=service_item_activity; addForeignKeyConstraint baseTableName=service_item, constraintName=FK1571565D904D6974, referencedTableName=owf_proper...', 'create ''service_item'' foreign keys', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing::omp
--  create 'rejection_listing' table
CREATE TABLE rejection_listing (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, justification_id BIGINT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, service_item_id BIGINT NOT NULL, description VARCHAR(2000) NULL, created_by_id BIGINT NULL, author_id BIGINT NOT NULL, edited_date datetime NULL, CONSTRAINT PK_REJECTION_LISTING PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', NOW(), 122, '8:6a090e1fc61ab6f787f45cae165fb947', 'createTable tableName=rejection_listing', 'create ''rejection_listing'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-constraints::omp
--  create 'rejection_listing' constraints
ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44E7666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44EE31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44EC7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44E5A032135 FOREIGN KEY (author_id) REFERENCES profile (id);

ALTER TABLE rejection_listing ADD CONSTRAINT FK3F2BD44E19CEB614 FOREIGN KEY (justification_id) REFERENCES rejection_justification (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-constraints', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', NOW(), 123, '8:2b446f0e3d58e98e9f94459cbc3b1c42', 'addForeignKeyConstraint baseTableName=rejection_listing, constraintName=FK3F2BD44E7666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=rejection_listing, constraintName=FK3F2BD44EE31CB353, referencedTableName=profile; addFo...', 'create ''rejection_listing'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-indices_1::omp
--  create 'rejection_listing' indices
CREATE INDEX rej_lst_svc_item_id_idx ON rejection_listing(service_item_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-indices_1', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', NOW(), 124, '8:b75225e340da5b345ff65829e212ba76', 'createIndex indexName=rej_lst_svc_item_id_idx, tableName=rejection_listing', 'create ''rejection_listing'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-indices_mysql::omp
--  create 'rejection_listing' indices for mysql
CREATE INDEX FK3F2BD44E7666C6D2 ON rejection_listing(created_by_id);

CREATE INDEX FK3F2BD44EE31CB353 ON rejection_listing(edited_by_id);

CREATE INDEX FK3F2BD44EC7E5C662 ON rejection_listing(service_item_id);

CREATE INDEX FK3F2BD44E5A032135 ON rejection_listing(author_id);

CREATE INDEX FK3F2BD44E19CEB614 ON rejection_listing(justification_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-indices_mysql', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', NOW(), 125, '8:8a635c56ca9b778ee8768ea012578639', 'createIndex indexName=FK3F2BD44E7666C6D2, tableName=rejection_listing; createIndex indexName=FK3F2BD44EE31CB353, tableName=rejection_listing; createIndex indexName=FK3F2BD44EC7E5C662, tableName=rejection_listing; createIndex indexName=FK3F2BD44E5A...', 'create ''rejection_listing'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_listing.yaml::7.17.0-rejection_listing-indices_2::omp
--  create 'rejection_listing' indices
CREATE INDEX rejection_listing_just_id_idx ON rejection_listing(justification_id);

CREATE INDEX rej_lst_author_id_idx ON rejection_listing(author_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_listing-indices_2', 'omp', 'create/changelog-7.17.0-rejection_listing.yaml', NOW(), 126, '8:ec66d01640d1733afcc34d8438ab5d31', 'createIndex indexName=rejection_listing_just_id_idx, tableName=rejection_listing; createIndex indexName=rej_lst_author_id_idx, tableName=rejection_listing', 'create ''rejection_listing'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity::omp
--  create 'rejection_activity' table
CREATE TABLE rejection_activity (id BIGINT NOT NULL, rejection_listing_id BIGINT NULL, CONSTRAINT PK_REJECTION_ACTIVITY PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', NOW(), 127, '8:fa76ced7c3c5edbffba3cfc3d3670e03', 'createTable tableName=rejection_activity', 'create ''rejection_activity'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity-constraints::omp
--  create 'rejection_activity' constraints
ALTER TABLE rejection_activity ADD CONSTRAINT FKF35C128582548A4A FOREIGN KEY (rejection_listing_id) REFERENCES rejection_listing (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity-constraints', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', NOW(), 128, '8:751abc1a4a1fd3817f0c980bd84e02a9', 'addForeignKeyConstraint baseTableName=rejection_activity, constraintName=FKF35C128582548A4A, referencedTableName=rejection_listing', 'create ''rejection_activity'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity-indices_mysql::omp
--  create 'rejection_activity' indices for mysql
CREATE INDEX FKF35C128582548A4A ON rejection_activity(rejection_listing_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity-indices_mysql', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', NOW(), 129, '8:70af1e7677605314415f9e92e1b34266', 'createIndex indexName=FKF35C128582548A4A, tableName=rejection_activity', 'create ''rejection_activity'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-rejection_activity.yaml::7.17.0-rejection_activity-indices::omp
--  create 'rejection_activity' indices
CREATE INDEX rejection_act_listing_id_idx ON rejection_activity(rejection_listing_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-rejection_activity-indices', 'omp', 'create/changelog-7.17.0-rejection_activity.yaml', NOW(), 130, '8:b4064cd56dc2323fda302c4824fbb6ee', 'createIndex indexName=rejection_act_listing_id_idx, tableName=rejection_activity', 'create ''rejection_activity'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot::omp
--  create 'service_item_snapshot' table
CREATE TABLE service_item_snapshot (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, service_item_id BIGINT NULL, title VARCHAR(255) NOT NULL, CONSTRAINT PK_SERVICE_ITEM_SNAPSHOT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', NOW(), 131, '8:33c282de0e948782155166638486bfc7', 'createTable tableName=service_item_snapshot', 'create ''service_item_snapshot'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot-constraints::omp
--  create 'service_item_snapshot' constraints
ALTER TABLE service_item_snapshot ADD CONSTRAINT FKFABD8966C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot-constraints', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', NOW(), 132, '8:acf172475a047a04bc33a0f124410dc1', 'addForeignKeyConstraint baseTableName=service_item_snapshot, constraintName=FKFABD8966C7E5C662, referencedTableName=service_item', 'create ''service_item_snapshot'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot-indices_mysql::omp
--  create 'service_item_snapshot' indices for mysql
CREATE INDEX FKFABD8966C7E5C662 ON service_item_snapshot(service_item_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot-indices_mysql', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', NOW(), 133, '8:d97811acf69d66c3998c0f969547aee0', 'createIndex indexName=FKFABD8966C7E5C662, tableName=service_item_snapshot', 'create ''service_item_snapshot'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_snapshot.yaml::7.17.0-service_item_snapshot-indices::omp
--  create 'service_item_snapshot' indices
CREATE INDEX si_snapshot_id_idx ON service_item_snapshot(service_item_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_snapshot-indices', 'omp', 'create/changelog-7.17.0-service_item_snapshot.yaml', NOW(), 134, '8:4c97371bb3743dbec88632840a5c3a6a', 'createIndex indexName=si_snapshot_id_idx, tableName=service_item_snapshot', 'create ''service_item_snapshot'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log::omp
--  create 'relationship_activity_log' table
CREATE TABLE relationship_activity_log (mod_rel_activity_id BIGINT NOT NULL, service_item_snapshot_id BIGINT NULL, items_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', NOW(), 135, '8:b1d2c98915a7a0f04a2124bf7352ea5b', 'createTable tableName=relationship_activity_log', 'create ''relationship_activity_log'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log-constraints::omp
--  create 'relationship_activity_log' constraints
ALTER TABLE relationship_activity_log ADD CONSTRAINT FK594974BB25A20F9D FOREIGN KEY (service_item_snapshot_id) REFERENCES service_item_snapshot (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log-constraints', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', NOW(), 136, '8:c34da16ff81ac34b973b19b0e119e48c', 'addForeignKeyConstraint baseTableName=relationship_activity_log, constraintName=FK594974BB25A20F9D, referencedTableName=service_item_snapshot', 'create ''relationship_activity_log'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log-indices_mysql::omp
--  create 'relationship_activity_log' indices for mysql
CREATE INDEX FK594974BB25A20F9D ON relationship_activity_log(service_item_snapshot_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log-indices_mysql', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', NOW(), 137, '8:35dcd8ad49c149b408f54a7025e6eb00', 'createIndex indexName=FK594974BB25A20F9D, tableName=relationship_activity_log', 'create ''relationship_activity_log'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship_activity_log.yaml::7.17.0-relationship_activity_log-indices::omp
--  create 'relationship_activity_log' indices
CREATE INDEX rel_act_log_mod_si_snpsht_idx ON relationship_activity_log(service_item_snapshot_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_activity_log-indices', 'omp', 'create/changelog-7.17.0-relationship_activity_log.yaml', NOW(), 138, '8:968336fc067e8927c80f551c896dbc84', 'createIndex indexName=rel_act_log_mod_si_snpsht_idx, tableName=relationship_activity_log', 'create ''relationship_activity_log'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_documentation_url.yaml::7.17.0-service_item_documentation_url::omp
--  create 'service_item_documentation_url' table
CREATE TABLE service_item_documentation_url (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT DEFAULT 0 NOT NULL, name VARCHAR(256) NOT NULL, service_item_id BIGINT NOT NULL, url VARCHAR(2083) NOT NULL, CONSTRAINT service_item_PK PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_documentation_url', 'omp', 'create/changelog-7.17.0-service_item_documentation_url.yaml', NOW(), 139, '8:18ab287d364a5b2190e65fb514b84510', 'createTable tableName=service_item_documentation_url', 'create ''service_item_documentation_url'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_documentation_url.yaml::7.17.0-service_item_documentation_url-constraints::omp
--  create 'service_item_documentation_url' constraints
ALTER TABLE service_item_documentation_url ADD CONSTRAINT FK24572D08C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_documentation_url-constraints', 'omp', 'create/changelog-7.17.0-service_item_documentation_url.yaml', NOW(), 140, '8:0fc0b159fa39d7ea27e858d3560f871a', 'addForeignKeyConstraint baseTableName=service_item_documentation_url, constraintName=FK24572D08C7E5C662, referencedTableName=service_item', 'create ''service_item_documentation_url'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_tech_pocs.yaml::7.17.0-service_item_tech_pocs::omp
--  create 'service_item_tech_pocs' table
CREATE TABLE service_item_tech_pocs (service_item_id BIGINT NULL, tech_poc VARCHAR(256) NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tech_pocs', 'omp', 'create/changelog-7.17.0-service_item_tech_pocs.yaml', NOW(), 141, '8:bab2e22535dc76372373649720baec4e', 'createTable tableName=service_item_tech_pocs', 'create ''service_item_tech_pocs'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-service_item_tech_pocs.yaml::7.17.0-service_item_tech_pocs-constraints::omp
--  create 'service_item_tech_pocs' constraints
ALTER TABLE service_item_tech_pocs ADD CONSTRAINT FKA55CFB56C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-service_item_tech_pocs-constraints', 'omp', 'create/changelog-7.17.0-service_item_tech_pocs.yaml', NOW(), 142, '8:23b732b4702270712a83727101647c14', 'addForeignKeyConstraint baseTableName=service_item_tech_pocs, constraintName=FKA55CFB56C7E5C662, referencedTableName=service_item', 'create ''service_item_tech_pocs'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommended_layouts::omp
--  create 'si_recommended_layouts' table
CREATE TABLE si_recommended_layouts (service_item_id BIGINT NULL, recommended_layout VARCHAR(255) NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommended_layouts', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', NOW(), 143, '8:6adb6a1cfaba30e084c9cabfaa30cdc8', 'createTable tableName=si_recommended_layouts', 'create ''si_recommended_layouts'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommend_layouts-constraints::omp
--  create 'si_recommend_layouts' constraints
ALTER TABLE si_recommended_layouts ADD CONSTRAINT FK863C793CC7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommend_layouts-constraints', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', NOW(), 144, '8:a16e859e1aa5220b86a98370a5c3419a', 'addForeignKeyConstraint baseTableName=si_recommended_layouts, constraintName=FK863C793CC7E5C662, referencedTableName=service_item', 'create ''si_recommend_layouts'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommend_layouts-indices_mysql::omp
--  create 'si_recommend_layouts' indices for mysql
CREATE INDEX FK863C793CC7E5C662 ON si_recommended_layouts(service_item_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommend_layouts-indices_mysql', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', NOW(), 145, '8:c680fa480f180036d4cf5b82fdef8a15', 'createIndex indexName=FK863C793CC7E5C662, tableName=si_recommended_layouts', 'create ''si_recommend_layouts'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-si_recommended_layouts.yaml::7.17.0-si_recommend_layouts-indices::omp
--  create 'si_recommend_layouts' indices
CREATE INDEX si_rec_layouts_idx ON si_recommended_layouts(service_item_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-si_recommend_layouts-indices', 'omp', 'create/changelog-7.17.0-si_recommended_layouts.yaml', NOW(), 146, '8:ba9523e13a60da23b70c5d9086a54216', 'createIndex indexName=si_rec_layouts_idx, tableName=si_recommended_layouts', 'create ''si_recommend_layouts'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-screenshot.yaml::7.17.0-screenshot::omp
--  create 'screenshot' table
CREATE TABLE screenshot (id BIGINT AUTO_INCREMENT NOT NULL, small_image_url VARCHAR(2083) NOT NULL, large_image_url VARCHAR(2083) NULL, ordinal INT NULL, service_item_id BIGINT NULL, created_by_id BIGINT NULL, created_date date NULL, edited_by_id BIGINT NULL, edited_date date NULL, version BIGINT DEFAULT 0 NOT NULL, CONSTRAINT PK_SCREENSHOT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-screenshot', 'omp', 'create/changelog-7.17.0-screenshot.yaml', NOW(), 147, '8:843ab99e8a613b582d0411ed378918b5', 'createTable tableName=screenshot', 'create ''screenshot'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-screenshot.yaml::7.17.0-screenshot-constraints::omp
--  create 'screenshot' constraints
ALTER TABLE screenshot ADD CONSTRAINT SCREENSHOT_SERVICE_ITEM_FK FOREIGN KEY (service_item_id) REFERENCES service_item (id) ON DELETE CASCADE;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-screenshot-constraints', 'omp', 'create/changelog-7.17.0-screenshot.yaml', NOW(), 148, '8:664acb9ab6e1eda0c2b45817f86b8c87', 'addForeignKeyConstraint baseTableName=screenshot, constraintName=SCREENSHOT_SERVICE_ITEM_FK, referencedTableName=service_item', 'create ''screenshot'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship::omp
--  create 'relationship' table
CREATE TABLE relationship (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, relationship_type VARCHAR(255) NOT NULL, owning_entity_id BIGINT NOT NULL, CONSTRAINT PK_RELATIONSHIP PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship', 'omp', 'create/changelog-7.17.0-relationship.yaml', NOW(), 149, '8:0f9a661322b64425140b776c3849e775', 'createTable tableName=relationship', 'create ''relationship'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship-constraints::omp
--  create 'relationship' constraints
ALTER TABLE relationship ADD CONSTRAINT FKF06476389D70DD39 FOREIGN KEY (owning_entity_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship-constraints', 'omp', 'create/changelog-7.17.0-relationship.yaml', NOW(), 150, '8:c829c3cf6ffc97c3f4ee9a6f01d433eb', 'addForeignKeyConstraint baseTableName=relationship, constraintName=FKF06476389D70DD39, referencedTableName=service_item', 'create ''relationship'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship-indices_mysql::omp
--  create 'relationship' indices for mysql
CREATE INDEX FKF06476389D70DD39 ON relationship(owning_entity_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship-indices_mysql', 'omp', 'create/changelog-7.17.0-relationship.yaml', NOW(), 151, '8:8dc88646a51d8dc3cb9efa010996b668', 'createIndex indexName=FKF06476389D70DD39, tableName=relationship', 'create ''relationship'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship.yaml::7.17.0-relationship-indices::omp
--  create 'relationship' indices
CREATE INDEX relationship_owing_id_idx ON relationship(owning_entity_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship-indices', 'omp', 'create/changelog-7.17.0-relationship.yaml', NOW(), 152, '8:4bf26b137c2a432d8d6be5aef0677049', 'createIndex indexName=relationship_owing_id_idx, tableName=relationship', 'create ''relationship'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship_service_item.yaml::7.17.0-relationship_service_item::omp
--  create 'relationship_service_item' table
CREATE TABLE relationship_service_item (relationship_related_items_id BIGINT NULL, service_item_id BIGINT NULL, related_items_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_service_item', 'omp', 'create/changelog-7.17.0-relationship_service_item.yaml', NOW(), 153, '8:a25d76fdb8a2e75c8fdb47d26ccc04a9', 'createTable tableName=relationship_service_item', 'create ''relationship_service_item'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-relationship_service_item.yaml::7.17.0-relationship_service_item-constraints::omp
--  create 'relationship_service_item' constraints
ALTER TABLE relationship_service_item ADD CONSTRAINT FKDA02504C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-relationship_service_item-constraints', 'omp', 'create/changelog-7.17.0-relationship_service_item.yaml', NOW(), 154, '8:9adc63402c979147e92e527ef25218ef', 'addForeignKeyConstraint baseTableName=relationship_service_item, constraintName=FKDA02504C7E5C662, referencedTableName=service_item', 'create ''relationship_service_item'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment::omp
--  create 'item_comment' table
CREATE TABLE item_comment (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, edited_by_id BIGINT NULL, created_date datetime NULL, service_item_id BIGINT NOT NULL, text VARCHAR(4000) NULL, created_by_id BIGINT NULL, author_id BIGINT NOT NULL, edited_date datetime NULL, rate FLOAT NULL, CONSTRAINT PK_ITEM_COMMENT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment', 'omp', 'create/changelog-7.17.0-item_comment.yaml', NOW(), 155, '8:79e4713fc9b15b6f031a3812a2a6a196', 'createTable tableName=item_comment', 'create ''item_comment'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment-constraints::omp
--  create 'item_comment' constraints
ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D337666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D33E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D33C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

ALTER TABLE item_comment ADD CONSTRAINT FKE6D04D335A032135 FOREIGN KEY (author_id) REFERENCES profile (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment-constraints', 'omp', 'create/changelog-7.17.0-item_comment.yaml', NOW(), 156, '8:5336ec2c90d00ac795f1ad642fbd1675', 'addForeignKeyConstraint baseTableName=item_comment, constraintName=FKE6D04D337666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=item_comment, constraintName=FKE6D04D33E31CB353, referencedTableName=profile; addForeignKeyCo...', 'create ''item_comment'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment-indices::omp
--  create 'item_comment' indices
CREATE INDEX itm_cmnt_svc_item_id_idx ON item_comment(service_item_id);

CREATE INDEX itm_cmnt_author_id_idx ON item_comment(author_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment-indices', 'omp', 'create/changelog-7.17.0-item_comment.yaml', NOW(), 157, '8:1de386b3024efc24aea1b0e0245817e0', 'createIndex indexName=itm_cmnt_svc_item_id_idx, tableName=item_comment; createIndex indexName=itm_cmnt_author_id_idx, tableName=item_comment', 'create ''item_comment'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-item_comment.yaml::7.17.0-item_comment-indices_mysql::omp
--  create 'item_comment' indices for mysql
CREATE INDEX FKE6D04D337666C6D2 ON item_comment(created_by_id);

CREATE INDEX FKE6D04D33E31CB353 ON item_comment(edited_by_id);

CREATE INDEX FKE6D04D33C7E5C662 ON item_comment(service_item_id);

CREATE INDEX FKE6D04D335A032135 ON item_comment(author_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-item_comment-indices_mysql', 'omp', 'create/changelog-7.17.0-item_comment.yaml', NOW(), 158, '8:cc507d8f2c9a40776d50d91025ef5cd5', 'createIndex indexName=FKE6D04D337666C6D2, tableName=item_comment; createIndex indexName=FKE6D04D33E31CB353, tableName=item_comment; createIndex indexName=FKE6D04D33C7E5C662, tableName=item_comment; createIndex indexName=FKE6D04D335A032135, tableNa...', 'create ''item_comment'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-change_detail.yaml::7.17.0-change_detail::omp
--  create 'change_detail' table
CREATE TABLE change_detail (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, new_value VARCHAR(4000) NULL, field_name VARCHAR(255) NOT NULL, old_value VARCHAR(4000) NULL, service_item_activity_id BIGINT NOT NULL, CONSTRAINT PK_CHANGE_DETAIL PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-change_detail', 'omp', 'create/changelog-7.17.0-change_detail.yaml', NOW(), 159, '8:bc9552fe83e1e673a0062dc80057f117', 'createTable tableName=change_detail', 'create ''change_detail'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-change_detail.yaml::7.17.0-change_detail-constraints::omp
--  create 'change_detail' indices
ALTER TABLE change_detail ADD CONSTRAINT FKB4467BC0855307BD FOREIGN KEY (service_item_activity_id) REFERENCES service_item_activity (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-change_detail-constraints', 'omp', 'create/changelog-7.17.0-change_detail.yaml', NOW(), 160, '8:7209140bd8c4ae18ced90ec021b0cd55', 'addForeignKeyConstraint baseTableName=change_detail, constraintName=FKB4467BC0855307BD, referencedTableName=service_item_activity', 'create ''change_detail'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-change_detail.yaml::7.17.0-change_detail-indices::omp
--  create 'change_detail' indices
CREATE INDEX FKB4467BC0855307BD ON change_detail(service_item_activity_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-change_detail-indices', 'omp', 'create/changelog-7.17.0-change_detail.yaml', NOW(), 161, '8:449e6837eb72f9f5a87a64dacce63954', 'createIndex indexName=FKB4467BC0855307BD, tableName=change_detail', 'create ''change_detail'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-contact.yaml::7.17.0-contact::omp
--  create 'contact' table
CREATE TABLE contact (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, email VARCHAR(100) NOT NULL, name VARCHAR(100) NOT NULL, organization VARCHAR(100) NULL, secure_phone VARCHAR(50) NULL, type_id BIGINT NOT NULL, service_item_id BIGINT NOT NULL, unsecure_phone VARCHAR(50) NULL, created_by_id BIGINT NULL, created_date date NULL, edited_by_id BIGINT NULL, edited_date date NULL, CONSTRAINT PK_CONTACT PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact', 'omp', 'create/changelog-7.17.0-contact.yaml', NOW(), 162, '8:3b66525d9c7280a80ecbaaf288b753f1', 'createTable tableName=contact', 'create ''contact'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-contact.yaml::7.17.0-contact-constraints::omp
--  create 'contact' constraints
ALTER TABLE contact ADD CONSTRAINT FK38B724207666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE contact ADD CONSTRAINT FK38B72420E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE contact ADD CONSTRAINT FK38B72420BA3FC877 FOREIGN KEY (type_id) REFERENCES contact_type (id);

ALTER TABLE contact ADD CONSTRAINT FK38B72420C7E5C662 FOREIGN KEY (service_item_id) REFERENCES service_item (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact-constraints', 'omp', 'create/changelog-7.17.0-contact.yaml', NOW(), 163, '8:29e8cf4c13898ebb4b0041421fa697b8', 'addForeignKeyConstraint baseTableName=contact, constraintName=FK38B724207666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=contact, constraintName=FK38B72420E31CB353, referencedTableName=profile; addForeignKeyConstraint b...', 'create ''contact'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-contact.yaml::7.17.0-contact-indices::omp
--  create 'contact' indices
CREATE INDEX FK38B72420C7E5C662 ON contact(service_item_id);

CREATE INDEX FK38B72420BA3FC877 ON contact(type_id);

CREATE INDEX FK38B72420E31CB353 ON contact(edited_by_id);

CREATE INDEX FK38B724207666C6D2 ON contact(created_by_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-contact-indices', 'omp', 'create/changelog-7.17.0-contact.yaml', NOW(), 164, '8:313e0234cd63b81997f526763b01e73e', 'createIndex indexName=FK38B72420C7E5C662, tableName=contact; createIndex indexName=FK38B72420BA3FC877, tableName=contact; createIndex indexName=FK38B72420E31CB353, tableName=contact; createIndex indexName=FK38B724207666C6D2, tableName=contact', 'create ''contact'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types::omp
--  create 'custom_field_definition_types' table
CREATE TABLE custom_field_definition_types (cf_definition_types_id BIGINT NULL, types_id BIGINT NULL, types_idx INT NULL);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', NOW(), 165, '8:f2740631c461ce1962a035a68d258e0d', 'createTable tableName=custom_field_definition_types', 'create ''custom_field_definition_types'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types-constraints::omp
--  create 'custom_field_definition_types' constraints
ALTER TABLE custom_field_definition_types ADD CONSTRAINT FK1A84FFC06928D597 FOREIGN KEY (types_id) REFERENCES types (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types-constraints', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', NOW(), 166, '8:004fa32145698c28d3063ff80f07e182', 'addForeignKeyConstraint baseTableName=custom_field_definition_types, constraintName=FK1A84FFC06928D597, referencedTableName=types', 'create ''custom_field_definition_types'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types-mysql::omp
--  create 'custom_field_definition_types' indices for mysql
CREATE INDEX FK1A84FFC06928D597 ON custom_field_definition_types(types_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types-mysql', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', NOW(), 167, '8:9f22f7e5a780a5de7c235f7f4e646467', 'createIndex indexName=FK1A84FFC06928D597, tableName=custom_field_definition_types', 'create ''custom_field_definition_types'' indices for mysql', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-custom_field_definition_types.yaml::7.17.0-custom_field_definition_types-indices::omp
--  create 'custom_field_definition_types' indices
CREATE INDEX cfd_types_types_idx ON custom_field_definition_types(types_id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-custom_field_definition_types-indices', 'omp', 'create/changelog-7.17.0-custom_field_definition_types.yaml', NOW(), 168, '8:9b039e020d2575090ac92ca4fd5e4d2e', 'createIndex indexName=cfd_types_types_idx, tableName=custom_field_definition_types', 'create ''custom_field_definition_types'' indices', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-affiliated_marketplace.yaml::7.17.0-affiliated_marketplace::omp
--  create 'affiliated_marketplace' table
CREATE TABLE affiliated_marketplace (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, active INT NOT NULL, created_by_id BIGINT NULL, created_date datetime NULL, edited_by_id BIGINT NULL, edited_date datetime NULL, icon_id BIGINT NULL, name VARCHAR(50) NOT NULL, server_url VARCHAR(2083) NOT NULL, timeout BIGINT NULL, CONSTRAINT PK_AFFILIATED_MARKETPLACE PRIMARY KEY (id));

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-affiliated_marketplace', 'omp', 'create/changelog-7.17.0-affiliated_marketplace.yaml', NOW(), 169, '8:230fe69074df2e339ca0f68ea0de6303', 'createTable tableName=affiliated_marketplace', 'create ''affiliated_marketplace'' table', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

--  Changeset create/changelog-7.17.0-affiliated_marketplace.yaml::7.17.0-affiliated_marketplace-constraints::omp
--  create 'affiliated_marketplace' constraints
ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C37666C6D2 FOREIGN KEY (created_by_id) REFERENCES profile (id);

ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C3E31CB353 FOREIGN KEY (edited_by_id) REFERENCES profile (id);

ALTER TABLE affiliated_marketplace ADD CONSTRAINT FKA6EB2C3EA25263C FOREIGN KEY (icon_id) REFERENCES images (id);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.0-affiliated_marketplace-constraints', 'omp', 'create/changelog-7.17.0-affiliated_marketplace.yaml', NOW(), 170, '8:129a0323608fe68be271de563fd7db99', 'addForeignKeyConstraint baseTableName=affiliated_marketplace, constraintName=FKA6EB2C37666C6D2, referencedTableName=profile; addForeignKeyConstraint baseTableName=affiliated_marketplace, constraintName=FKA6EB2C3E31CB353, referencedTableName=profil...', 'create ''affiliated_marketplace'' constraints', 'EXECUTED', 'create', NULL, '3.6.1', '6497045456');

