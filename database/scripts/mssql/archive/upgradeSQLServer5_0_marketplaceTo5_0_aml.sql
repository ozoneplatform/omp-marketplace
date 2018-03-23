-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/10/12 1:32 PM

-- Liquibase version: 2.0.1
-- *********************************************************************

-- Lock Database
-- Changeset changelog_5.0_aml.groovy::aml_5.0-1::aml-marketplace::(Checksum: 3:ecd912dc8867e6cb8284084390923ab2)
CREATE TABLE [dbo].[score_card] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [score] FLOAT(19) NOT NULL, CONSTRAINT [score_cardPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-1', '2.0.1', '3:ecd912dc8867e6cb8284084390923ab2', 1)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-2::aml-marketplace::(Checksum: 3:64762952f51d0fe7b8c45fe091eba93a)
CREATE TABLE [dbo].[score_card_item] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [description] VARCHAR(500) NOT NULL, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [is_standard_question] BIT NOT NULL, [question] VARCHAR(250) NOT NULL, [weight] FLOAT(19), CONSTRAINT [sc_itemPK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-2', '2.0.1', '3:64762952f51d0fe7b8c45fe091eba93a', 2)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-3::aml-marketplace::(Checksum: 3:b51bd092c9dcac8bd991f969117efa09)
CREATE TABLE [dbo].[score_card_item_response] ([id] BIGINT IDENTITY  NOT NULL, [version] BIGINT NOT NULL, [created_by_id] NUMERIC(19,0), [created_date] DATETIME, [edited_by_id] NUMERIC(19,0), [edited_date] DATETIME, [is_satisfied] BIT NOT NULL, [score_card_id] BIGINT NOT NULL, [score_card_item_id] BIGINT NOT NULL, CONSTRAINT [sc_responsePK] PRIMARY KEY ([id]))
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Table', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-3', '2.0.1', '3:b51bd092c9dcac8bd991f969117efa09', 3)
GO

-- Changeset changelog_5.0_aml.groovy::aml_5.0-4::aml-marketplace::(Checksum: 3:a10c921a8ef6370cc64cc06016c7c55c)
ALTER TABLE [dbo].[service_item] ADD [score_card_id] BIGINT
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Add Column', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-4', '2.0.1', '3:a10c921a8ef6370cc64cc06016c7c55c', 4)
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Create Index (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-5', '2.0.1', '3:63bdefa2034c53e720100a0e83826dab', 5)
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

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('aml-marketplace', '', GETDATE(), 'Add Foreign Key Constraint (x9)', 'EXECUTED', 'changelog_5.0_aml.groovy', 'aml_5.0-6', '2.0.1', '3:b4c50686ed2e827d6a5d5de248d5154c', 6)
GO

-- Release Database Lock
