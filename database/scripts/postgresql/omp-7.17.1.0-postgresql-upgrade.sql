-- Changeset changelog-7.17.1.0.yaml::7.17.1.0-1::omp
-- update 'u_domain.preferences' mapping table
ALTER TABLE u_domain_preferences RENAME COLUMN preferences TO user_domain_instance_id;

ALTER TABLE u_domain_preferences ADD preferences_object VARCHAR(255);

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.1.0-1', 'omp', 'changelog-7.17.1.0.yaml', NOW(), 156, '8:8ed1d083f14dfa8973211dfd73178138', 'renameColumn newColumnName=user_domain_instance_id, oldColumnName=preferences, tableName=u_domain_preferences; addColumn tableName=u_domain_preferences', 'update ''u_domain.preferences'' mapping table', 'EXECUTED', 'upgrade', NULL, '3.6.1', '6497060155');

-- Changeset changelog-7.17.1.0.yaml::7.17.1.0-2::omp
-- created_date and edited_date from date to timestamp
ALTER TABLE agency ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE agency ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE application_configuration ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE application_configuration ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE contact ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE contact ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE contact_type ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE contact_type ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE import_task ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE import_task ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent_action ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent_action ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent_data_type ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent_data_type ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent_direction ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE intent_direction ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE owf_widget_types ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE owf_widget_types ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE score_card_item ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE score_card_item ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE screenshot ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE screenshot ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE tag ALTER COLUMN created_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (created_date::TIMESTAMP WITHOUT TIME ZONE);

ALTER TABLE tag ALTER COLUMN edited_date TYPE TIMESTAMP WITHOUT TIME ZONE USING (edited_date::TIMESTAMP WITHOUT TIME ZONE);

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.1.0-2', 'omp', 'changelog-7.17.1.0.yaml', NOW(), 157, '8:c39980f093e554abe04ee1eb534330b8', 'modifyDataType columnName=created_date, tableName=agency; modifyDataType columnName=edited_date, tableName=agency; modifyDataType columnName=created_date, tableName=application_configuration; modifyDataType columnName=edited_date, tableName=applic...', 'created_date and edited_date from date to timestamp', 'EXECUTED', 'upgrade', NULL, '3.6.1', '6497060155');

