-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/29/15 10:19 AM
-- Against: sa@jdbc:jtds:sqlserver://127.0.0.1:1433/omp
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Changeset default_data.groovy::defaultData-2::marketplace::(Checksum: 3:c20b878f50adbc51bcc950947b69af41)
UPDATE [dbo].[category] SET [description] = 'Example Category A' WHERE title = 'Category A'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category A', 'Example Category A', 0,
                        'bc476b16-b39d-4154-abcd-a32f0e77ef72', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category A'
                    )
GO

UPDATE [dbo].[category] SET [description] = 'Example Category B' WHERE title = 'Category B'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category B', 'Example Category B', 0,
                        '99052d44-feec-421d-96fb-b787489a0dd2', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category B'
                    )
GO

UPDATE [dbo].[category] SET [description] = 'Example Category C' WHERE title = 'Category C'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category C', 'Example Category C', 0,
                        '184b902e-9622-49b1-99bb-5eeed78090a5', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category C'
                    )
GO

UPDATE [dbo].[category] SET [description] = 'Analytics based on geographic data' WHERE title = 'Geospatial'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Geospatial', 'Analytics based on geographic data', 0,
                        'a4881581-1b6c-4013-9bf7-3aa7c6ffa695', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Geospatial'
                    )
GO

UPDATE [dbo].[category] SET [description] = 'Data set retrieval' WHERE title = 'Query'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Query', 'Data set retrieval', 0,
                        '494f798a-9199-4d54-96fb-26cc307db56d', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Query'
                    )
GO

UPDATE [dbo].[category] SET [description] = 'Data set summarization' WHERE title = 'Reporting'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Reporting', 'Data set summarization', 0,
                        '167a2251-6ed4-4f37-a849-ad8cfb891b23', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Reporting'
                    )
GO

UPDATE [dbo].[category] SET [description] = 'Amaltics based on temporal data' WHERE title = 'Temporal'
GO

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Temporal', 'Amaltics based on temporal data', 0,
                        '6c56b16d-e644-4cda-92f4-6a0e3264e9b9', GetDate(),
                        GetDate()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Temporal'
                    )
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-2', '2.0.5', '3:c20b878f50adbc51bcc950947b69af41', 169)
GO

-- Changeset default_data.groovy::defaultData-5::marketplace::(Checksum: 3:ff5a0b94e61583c8b47eb4ab08b6e551)
INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is Enterprise Management System (EMS) part of the support structure?', 'In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_EMS_lrg.png', 0,
                        GetDate(), GetDate(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is Enterprise Management System (EMS) part of the support structure?'
                    )
GO

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application hosted within the infrastructure of the cloud?', 'In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudHost_lrg.png', 0,
                        GetDate(), GetDate(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application hosted within the infrastructure of the cloud?'
                    )
GO

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does the application elastically scale?', 'In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Scale_lrg.png', 0,
                        GetDate(), GetDate(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does the application elastically scale?'
                    )
GO

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does this system operate without license constraints?', 'In order to satisfy this criterion, the system should operate without constraining the user to interact with it.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_LicenseFree_lrg.png', 0,
                        GetDate(), GetDate(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does this system operate without license constraints?'
                    )
GO

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application data utilizing cloud storage?', 'In order to satisfy this criterion, the application''s data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudStorage_lrg.png', 0,
                        GetDate(), GetDate(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application data utilizing cloud storage?'
                    )
GO

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application accessible through a web browser?', 'In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Browser_lrg.png', 0,
                        GetDate(), GetDate(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application accessible through a web browser?'
                    )
GO

INSERT INTO [dbo].[DATABASECHANGELOG] ([AUTHOR], [COMMENTS], [DATEEXECUTED], [DESCRIPTION], [EXECTYPE], [FILENAME], [ID], [LIQUIBASE], [MD5SUM], [ORDEREXECUTED]) VALUES ('marketplace', '', GETDATE(), 'Custom SQL (x6)', 'EXECUTED', 'default_data.groovy', 'defaultData-5', '2.0.5', '3:ff5a0b94e61583c8b47eb4ab08b6e551', 170)
GO

