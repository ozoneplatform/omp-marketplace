--  Changeset changelog-7.17.1.0.yaml::7.17.1.0-1::omp
--  update 'u_domain.preferences' mapping table
ALTER TABLE u_domain_preferences CHANGE preferences user_domain_instance_id BIGINT;

ALTER TABLE u_domain_preferences ADD preferences_object VARCHAR(255) NULL;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.1.0-1', 'omp', 'changelog-7.17.1.0.yaml', NOW(), 171, '8:8ed1d083f14dfa8973211dfd73178138', 'renameColumn newColumnName=user_domain_instance_id, oldColumnName=preferences, tableName=u_domain_preferences; addColumn tableName=u_domain_preferences', 'update ''u_domain.preferences'' mapping table', 'EXECUTED', 'upgrade', NULL, '3.6.1', '6497094586');

