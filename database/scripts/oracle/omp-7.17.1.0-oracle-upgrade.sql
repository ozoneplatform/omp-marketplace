SET DEFINE OFF;

-- Changeset changelog-7.17.1.0.yaml::7.17.1.0-1::omp
-- update 'u_domain.preferences' mapping table
ALTER TABLE u_domain_preferences RENAME COLUMN preferences TO user_domain_instance_id;

ALTER TABLE u_domain_preferences ADD preferences_object VARCHAR2(255 CHAR);

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.1.0-1', 'omp', 'changelog-7.17.1.0.yaml', SYSTIMESTAMP, 156, '8:95e41611d073da1bff3a67424cc08400', 'renameColumn newColumnName=user_domain_instance_id, oldColumnName=preferences, tableName=u_domain_preferences; addColumn tableName=u_domain_preferences', 'update ''u_domain.preferences'' mapping table', 'EXECUTED', 'upgrade', NULL, '3.6.1', '6497284520');

