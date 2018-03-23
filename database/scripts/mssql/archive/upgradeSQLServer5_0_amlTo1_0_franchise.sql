-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 12/10/12 10:07 AM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-1::franchise-store::(Checksum: 3:a1d04babbf1c20d5360e96e65afd2bae)
ALTER TABLE [dbo].[service_item] ADD [agency] VARCHAR(255)
GO

ALTER TABLE [dbo].[service_item] ADD [agency_icon] VARCHAR(255)
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Add Column (x2)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-1', '2.0.1', '3:a1d04babbf1c20d5360e96e65afd2bae', 1)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-2::franchise-store::(Checksum: 3:ec8f4a5a83dab731f6b4890930c62edd)
ALTER TABLE [dbo].[service_item] ADD [is_outside] TINYINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-2', '2.0.1', '3:ec8f4a5a83dab731f6b4890930c62edd', 2)
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Drop Index (x6)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-3', '2.0.1', '3:0ee8acc59cc31553b0362e999aca8aa2', 3)
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Create Table, Add Column (x2), Create Index, Add Foreign Key Constraint', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-4', '2.0.1', '3:3820fc70e2e0949952a5ffedf0928c29', 4)
GO

-- Changeset changelog_1.0_franchise.groovy::1.0_franchise-5::franchise-store::(Checksum: 3:f9079ad54404b13406806316cdbf7169)
ALTER TABLE [dbo].[service_item] ALTER COLUMN [tech_poc] NVARCHAR(256) NOT NULL
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [title] NVARCHAR(256) NOT NULL
GO

ALTER TABLE [dbo].[service_item] ALTER COLUMN [version_name] NVARCHAR(256) NOT NULL
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('franchise-store', '', GETDATE(), 'Add Not-Null Constraint (x3)', 'EXECUTED', 'changelog_1.0_franchise.groovy', '1.0_franchise-5', '2.0.1', '3:f9079ad54404b13406806316cdbf7169', 5)
GO

-- Release Database Lock
