-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 2/28/13 11:04 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.2_franchise.groovy::1.2_franchise-1::franchise-store::(Checksum: 3:93bccc9aa844709bb5669060a1147169)
CREATE TABLE [dbo].[application_configuration] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] SMALLDATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] SMALLDATETIME, [code] VARCHAR(250) NOT NULL, [value] VARCHAR(2000), [title] VARCHAR(250) NOT NULL, [description] VARCHAR(2000), [type] VARCHAR(250) NOT NULL, [group_name] VARCHAR(250) NOT NULL, [sub_group_name] VARCHAR(250), [mutable] BIT NOT NULL, [sub_group_order] BIGINT, CONSTRAINT [application_configurationPK] PRIMARY KEY ([id]), UNIQUE ([code]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-1', '2.0.1', '3:93bccc9aa844709bb5669060a1147169', 1)
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', 'Create index for application_configuration.group_name', GETDATE(), 'Create Index (x3), Add Foreign Key Constraint (x2)', 'EXECUTED', 'changelog_1.2_franchise.groovy', '1.2_franchise-2', '2.0.1', '3:8c76d18c40c82ecf837da46af444846c', 2)
GO

-- Release Database Lock
