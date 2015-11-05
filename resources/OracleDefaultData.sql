-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog_master.groovy
-- Ran at: 10/21/15 7:54 AM
-- Against: OMP@jdbc:oracle:thin:@192.168.99.100:49161:XE
-- Liquibase version: 2.0.5
-- *********************************************************************

-- Changeset default_data.groovy::defaultData-1::marketplace::(Checksum: 3:455f2dcb71e4a479ebaffe7ad0cbbf66)
-- Create a trigger for category insert
create or replace trigger category_insert before insert on category
            for each row
            when (new.id is null)
            begin
            select hibernate_sequence.nextval into :new.id from dual;
            end;
            /

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create a trigger for category insert', SYSTIMESTAMP, 'Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-1', '2.0.5', '3:455f2dcb71e4a479ebaffe7ad0cbbf66', 173);

-- Changeset default_data.groovy::defaultData-2::marketplace::(Checksum: 3:ace42ef3f1972c499aa07602a2920332)
UPDATE category SET description = 'Example Category A' WHERE title = 'Category A';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category A', 'Example Category A', 0,
                        '8174756a-59e1-4598-9646-4ea9b56662c2', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category A'
                    );

UPDATE category SET description = 'Example Category B' WHERE title = 'Category B';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category B', 'Example Category B', 0,
                        '4ab1d698-e698-4138-bbe9-db2c30a565be', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category B'
                    );

UPDATE category SET description = 'Example Category C' WHERE title = 'Category C';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Category C', 'Example Category C', 0,
                        '2cd93dd8-17f8-4fbe-ace4-c5041786d6f7', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Category C'
                    );

UPDATE category SET description = 'Analytics based on geographic data' WHERE title = 'Geospatial';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Geospatial', 'Analytics based on geographic data', 0,
                        '52022205-7baf-429c-ae80-94b9acec3f11', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Geospatial'
                    );

UPDATE category SET description = 'Data set retrieval' WHERE title = 'Query';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Query', 'Data set retrieval', 0,
                        '247e66ed-6af7-4cf6-9477-9000e1b96d0f', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Query'
                    );

UPDATE category SET description = 'Data set summarization' WHERE title = 'Reporting';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Reporting', 'Data set summarization', 0,
                        'fe783870-a6a1-4461-bbad-b659e3c88933', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Reporting'
                    );

UPDATE category SET description = 'Amaltics based on temporal data' WHERE title = 'Temporal';

INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT 'Temporal', 'Amaltics based on temporal data', 0,
                        '927865ba-1e8b-4a3b-8260-634a3c8cd701', SYSTIMESTAMP,
                        SYSTIMESTAMP
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = 'Temporal'
                    );

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', SYSTIMESTAMP, 'Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL, Update Data, Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-2', '2.0.5', '3:ace42ef3f1972c499aa07602a2920332', 174);

-- Changeset default_data.groovy::defaultData-3::marketplace::(Checksum: 3:4760322aa0e02211aa9d44fa9fa32fa3)
-- Drop the trigger
drop trigger category_insert;

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop the trigger', SYSTIMESTAMP, 'Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-3', '2.0.5', '3:4760322aa0e02211aa9d44fa9fa32fa3', 175);

-- Changeset default_data.groovy::defaultData-4::marketplace::(Checksum: 3:d8eda4bd4984c2efac967d6541698a01)
-- Create a trigger for score_card_item insert
create or replace trigger score_card_item_insert before insert on score_card_item
            for each row
            when (new.id is null)
            begin
            select hibernate_sequence.nextval into :new.id from dual;
            end;
            /

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Create a trigger for score_card_item insert', SYSTIMESTAMP, 'Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-4', '2.0.5', '3:d8eda4bd4984c2efac967d6541698a01', 176);

-- Changeset default_data.groovy::defaultData-5::marketplace::(Checksum: 3:28624aeb09cb19669d2dcccc46bfec2d)
INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is Enterprise Management System (EMS) part of the support structure?', 'In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_EMS_lrg.png', 0,
                        SYSTIMESTAMP, SYSTIMESTAMP, 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is Enterprise Management System (EMS) part of the support structure?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application hosted within the infrastructure of the cloud?', 'In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudHost_lrg.png', 0,
                        SYSTIMESTAMP, SYSTIMESTAMP, 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application hosted within the infrastructure of the cloud?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does the application elastically scale?', 'In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Scale_lrg.png', 0,
                        SYSTIMESTAMP, SYSTIMESTAMP, 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does the application elastically scale?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Does this system operate without license constraints?', 'In order to satisfy this criterion, the system should operate without constraining the user to interact with it.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_LicenseFree_lrg.png', 0,
                        SYSTIMESTAMP, SYSTIMESTAMP, 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Does this system operate without license constraints?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application data utilizing cloud storage?', 'In order to satisfy this criterion, the application''s data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudStorage_lrg.png', 0,
                        SYSTIMESTAMP, SYSTIMESTAMP, 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application data utilizing cloud storage?'
                    );

INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT 'Is the application accessible through a web browser?', 'In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.', '/marketplace/themes/common/images/scorecard/ScorecardIcons_Browser_lrg.png', 0,
                        SYSTIMESTAMP, SYSTIMESTAMP, 1
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = 'Is the application accessible through a web browser?'
                    );

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', '', SYSTIMESTAMP, 'Custom SQL (x6)', 'EXECUTED', 'default_data.groovy', 'defaultData-5', '2.0.5', '3:28624aeb09cb19669d2dcccc46bfec2d', 177);

-- Changeset default_data.groovy::defaultData-6::marketplace::(Checksum: 3:d57007d471090a883d67500565ca383b)
-- Drop the trigger
drop trigger score_card_item_insert;

INSERT INTO DATABASECHANGELOG (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('marketplace', 'Drop the trigger', SYSTIMESTAMP, 'Custom SQL', 'EXECUTED', 'default_data.groovy', 'defaultData-6', '2.0.5', '3:d57007d471090a883d67500565ca383b', 178);

