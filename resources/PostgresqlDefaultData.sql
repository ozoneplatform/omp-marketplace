-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/21/15 7:40 AM
-- Against: postgres@jdbc:postgresql://localhost:5432/omp
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Changeset default_data.groovy::defaultData-1::marketplace::(Checksum: 3:8a72896a119302a682eebcb0ab45c9f5)
-- Ensure that the category table has auto-incrementing ids
ALTER TABLE category ALTER COLUMN id SET DEFAULT nextval('hibernate_sequence');

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Ensure that the category table has auto-incrementing ids', NOW(), 'Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-1', '2.0.5', '3:8a72896a119302a682eebcb0ab45c9f5', 170);

-- Changeset default_data.groovy::defaultData-2::marketplace::(Checksum: 3:1ecfbd2147a02d93cf462ad7ef305499)
UPDATE category SET description = 'Example Category A' WHERE title = 'Category A';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category A', 'Example Category A', 0,
                        '3f2c046e-8128-457f-8da9-df81730c150a', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category A'
                    );

UPDATE category SET description = 'Example Category B' WHERE title = 'Category B';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category B', 'Example Category B', 0,
                        'cb0c4d15-1646-4a3f-a22e-5227a114acfa', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category B'
                    );

UPDATE category SET description = 'Example Category C' WHERE title = 'Category C';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category C', 'Example Category C', 0,
                        '7f36aa76-6a55-485b-b22b-53088eed37c1', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category C'
                    );

UPDATE category SET description = 'Analytics based on geographic data' WHERE title = 'Geospatial';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Geospatial', 'Analytics based on geographic data', 0,
                        'a3503cd1-5e96-414c-9540-168dceba5aaf', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Geospatial'
                    );

UPDATE category SET description = 'Data set retrieval' WHERE title = 'Query';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Query', 'Data set retrieval', 0,
                        'df2dbf01-dd02-425f-8123-fd8a8aa0a661', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Query'
                    );

UPDATE category SET description = 'Data set summarization' WHERE title = 'Reporting';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Reporting', 'Data set summarization', 0,
                        '4d06d8b0-d052-4c58-8e89-72fcefd9b5f4', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Reporting'
                    );

UPDATE category SET description = 'Amaltics based on temporal data' WHERE title = 'Temporal';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Temporal', 'Amaltics based on temporal data', 0,
                        '35620f14-5e19-477f-b927-61a66b3ab05c', NOW(),
                        NOW()
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Temporal'
                    );

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-2', '2.0.5', '3:1ecfbd2147a02d93cf462ad7ef305499', 171);

-- Changeset default_data.groovy::defaultData-5::marketplace::(Checksum: 3:22e926834c3186c78e3e41d88453452b)
INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is Enterprise Management System (EMS) part of the support structure?', 'In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_EMS_lrg.png', 0,
                        NOW(), NOW(), true
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is Enterprise Management System (EMS) part of the support structure?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application hosted within the infrastructure of the cloud?', 'In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudHost_lrg.png', 0,
                        NOW(), NOW(), true
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application hosted within the infrastructure of the cloud?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does the application elastically scale?', 'In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Scale_lrg.png', 0,
                        NOW(), NOW(), true
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does the application elastically scale?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does this system operate without license constraints?', 'In order to satisfy this criterion, the system should operate without constraining the user to interact with it.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_LicenseFree_lrg.png', 0,
                        NOW(), NOW(), true
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does this system operate without license constraints?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application data utilizing cloud storage?', 'In order to satisfy this criterion, the application''s data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudStorage_lrg.png', 0,
                        NOW(), NOW(), true
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application data utilizing cloud storage?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application accessible through a web browser?', 'In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Browser_lrg.png', 0,
                        NOW(), NOW(), true
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application accessible through a web browser?'
                    );

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', NOW(), 'Custom SQL (x6)', 'EXECUTED', 'default_data.groovy', 'defaultData-5', '2.0.5', '3:22e926834c3186c78e3e41d88453452b', 172);

