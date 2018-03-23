-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 7/29/13 9:50 AM
-- Against: owf_test@jdbc:jtds:sqlserver://owfdb02.goss.owfgoss.org:1443/aml_migration
-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_7.2.groovy::7.2-1::marketplace::(Checksum: 3:37e31178188d6d69550c6abefcfafe5f)
CREATE TABLE [dbo].[intent_action] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(256), [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(256) NOT NULL, [uuid] VARCHAR(255), CONSTRAINT [intent_actionPK] PRIMARY KEY ([id]))
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-1', '2.0.1', '3:37e31178188d6d69550c6abefcfafe5f', 45)
GO

-- Changeset changelog_7.2.groovy::7.2-2::marketplace::(Checksum: 3:8f37748aa663ac4b2f4f4ae5625d1ddb)
CREATE TABLE [dbo].[intent_direction] ([created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(250), [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [id] BIGINT IDENTITY  NOT NULL, [title] VARCHAR(7) NOT NULL, [uuid] VARCHAR(255), [version] BIGINT NOT NULL, CONSTRAINT [intent_directPK] PRIMARY KEY ([id]))
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x4), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-2', '2.0.1', '3:8f37748aa663ac4b2f4f4ae5625d1ddb', 46)
GO

-- Changeset changelog_7.2.groovy::7.2-3::marketplace::(Checksum: 3:ae20f44a6ea989ee57e6bd457e6a4f49)
CREATE TABLE [dbo].[intent_data_type] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(256), [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(256) NOT NULL, [uuid] VARCHAR(255), CONSTRAINT [intent_data_tPK] PRIMARY KEY ([id]))
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-3', '2.0.1', '3:ae20f44a6ea989ee57e6bd457e6a4f49', 47)
GO

-- Changeset changelog_7.2.groovy::7.2-4::marketplace::(Checksum: 3:13bfc87f098ddf3d04d10c5bc629d4df)
CREATE TABLE [dbo].[owf_widget_types] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [description] VARCHAR(255) NOT NULL, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [title] VARCHAR(256) NOT NULL, [uuid] VARCHAR(255), CONSTRAINT [owf_widget_typePK] PRIMARY KEY ([id]))
GO

ALTER TABLE [dbo].[owf_properties] ADD [owf_widget_type] VARCHAR(255) NOT NULL DEFAULT 'standard'
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Create Table, Add Column, Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-4', '2.0.1', '3:13bfc87f098ddf3d04d10c5bc629d4df', 48)
GO

-- Changeset changelog_7.2.groovy::7.2-5::marketplace::(Checksum: 3:21571b54501c40307f4bd432fba456f1)
ALTER TABLE [dbo].[owf_properties] ADD [universal_name] VARCHAR(255)
GO

ALTER TABLE [dbo].[owf_properties] ADD [stack_context] VARCHAR(200)
GO

ALTER TABLE [dbo].[owf_properties] ADD [stack_descriptor] NVARCHAR(MAX)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Add Column (x3)', 'EXECUTED', 'changelog_7.2.groovy', '7.2-5', '2.0.1', '3:21571b54501c40307f4bd432fba456f1', 49)
GO

-- Release Database Lock
