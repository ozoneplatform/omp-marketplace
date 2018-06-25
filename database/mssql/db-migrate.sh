#!/usr/bin/env bash

pushd .. > /dev/null

liquibase \
 --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver \
 --classpath=./drivers/mssql-jdbc-6.4.0.jre8.jar \
 --changeLogFile=changelog-master.yaml \
 --url="jdbc:sqlserver://localhost:1433;databaseName=omp" \
 --username=sa \
 --password=PASSword123!@# \
 --defaultSchemaName=dbo \
 --contexts=create \
 migrate

popd > /dev/null
