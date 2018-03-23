#!/usr/bin/env bash

DB_URL="jdbc:sqlserver://localhost:1433;databaseName=omp"
DB_USERNAME="sa"
DB_PASSWORD="PASSword123!@#"
DB_SCHEMA="dbo"

DRIVER_CLASS="com.microsoft.sqlserver.jdbc.SQLServerDriver"
DRIVER_CLASSPATH="./drivers/mssql-jdbc-6.4.0.jre8.jar"


pushd .. > /dev/null

liquibase \
  --driver="$DRIVER_CLASS" \
  --classpath="$DRIVER_CLASSPATH" \
  --changeLogFile=changelog-master.yaml \
  --url="$DB_URL" \
  --username="$DB_USERNAME" \
  --password="$DB_PASSWORD" \
  --defaultSchemaName="$DB_SCHEMA" \
  dropAll > /dev/null

popd > /dev/null
