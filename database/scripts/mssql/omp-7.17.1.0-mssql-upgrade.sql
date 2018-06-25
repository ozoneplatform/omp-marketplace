-- Changeset changelog-7.17.1.0.yaml::7.17.1.0-1::omp
-- update 'u_domain.preferences' mapping table
exec sp_rename 'u_domain_preferences.preferences', 'user_domain_instance_id'
GO

ALTER TABLE u_domain_preferences ADD preferences_object nvarchar(255)
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7.17.1.0-1', 'omp', 'changelog-7.17.1.0.yaml', GETDATE(), 155, '8:0919967061f55ee8289ba3fbcedf4707', 'renameColumn newColumnName=user_domain_instance_id, oldColumnName=preferences, tableName=u_domain_preferences; addColumn tableName=u_domain_preferences', 'update ''u_domain.preferences'' mapping table', 'EXECUTED', 'upgrade', NULL, '3.6.1', '6497070037')
GO

