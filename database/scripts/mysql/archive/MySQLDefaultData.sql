--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: changelog_master.groovy
--  Ran at: 10/21/15 7:34 AM
--  Against: root@localhost@jdbc:mysql://localhost:3306/omp
--  Liquibase version: 2.0.5
--  *********************************************************************

--  Changeset default_data.groovy::defaultData-2::marketplace::(Checksum: 3:58e68207e3f734862129e467a9ce6cdc)
UPDATE `category` SET `description` = 'Example Category A' WHERE title = 'Category A';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category A', 'Example Category A', 0,
                        '44de8683-ab75-495c-9da2-9633c5a6f7cc', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category A'
                    );

UPDATE `category` SET `description` = 'Example Category B' WHERE title = 'Category B';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category B', 'Example Category B', 0,
                        '63e3f2fc-f1e0-4408-ac17-00442fb78d8b', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category B'
                    );

UPDATE `category` SET `description` = 'Example Category C' WHERE title = 'Category C';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category C', 'Example Category C', 0,
                        'b8866ec1-c4b1-4240-ba9f-0c00585fa513', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category C'
                    );

UPDATE `category` SET `description` = 'Analytics based on geographic data' WHERE title = 'Geospatial';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Geospatial', 'Analytics based on geographic data', 0,
                        '29c91685-1ed6-403b-a93a-df2aa093fc58', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Geospatial'
                    );

UPDATE `category` SET `description` = 'Data set retrieval' WHERE title = 'Query';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Query', 'Data set retrieval', 0,
                        'e7f2ffd8-4a1f-4215-8eee-b4de1d810357', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Query'
                    );

UPDATE `category` SET `description` = 'Data set summarization' WHERE title = 'Reporting';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Reporting', 'Data set summarization', 0,
                        'f761521c-2936-460d-bcd1-7fcf59e227ca', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Reporting'
                    );

UPDATE `category` SET `description` = 'Amaltics based on temporal data' WHERE title = 'Temporal';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Temporal', 'Amaltics based on temporal data', 0,
                        '6eef56ff-0d19-49c4-b0f2-c22d98d149df', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Temporal'
                    );

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-2', '2.0.5', '3:58e68207e3f734862129e467a9ce6cdc', 177);

--  Changeset default_data.groovy::defaultData-5::marketplace::(Checksum: 3:ff91aa8b6ca20a3a67537c8d066acba3)
INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is Enterprise Management System (EMS) part of the support structure?', 'In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_EMS_lrg.png', 0,
                        NOW(), NOW(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is Enterprise Management System (EMS) part of the support structure?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application hosted within the infrastructure of the cloud?', 'In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudHost_lrg.png', 0,
                        NOW(), NOW(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application hosted within the infrastructure of the cloud?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does the application elastically scale?', 'In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Scale_lrg.png', 0,
                        NOW(), NOW(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does the application elastically scale?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does this system operate without license constraints?', 'In order to satisfy this criterion, the system should operate without constraining the user to interact with it.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_LicenseFree_lrg.png', 0,
                        NOW(), NOW(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does this system operate without license constraints?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application data utilizing cloud storage?', 'In order to satisfy this criterion, the application''s data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudStorage_lrg.png', 0,
                        NOW(), NOW(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application data utilizing cloud storage?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application accessible through a web browser?', 'In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Browser_lrg.png', 0,
                        NOW(), NOW(), 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application accessible through a web browser?'
                    );

INSERT INTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('marketplace', '', NOW(), 'Custom SQL (x6)', 'EXECUTED', 'default_data.groovy', 'defaultData-5', '2.0.5', '3:ff91aa8b6ca20a3a67537c8d066acba3', 178);

