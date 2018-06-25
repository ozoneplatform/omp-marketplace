#!/usr/bin/env bash

DB_URL="jdbc:sqlserver://localhost:1433;databaseName=omp"
DB_USERNAME="sa"
DB_PASSWORD="PASSword123!@#"
DB_SCHEMA="dbo"

DRIVER_CLASS="com.microsoft.sqlserver.jdbc.SQLServerDriver"
DRIVER_CLASSPATH="./drivers/mssql-jdbc-6.4.0.jre8.jar"


function generateScriptForContext() {
    liquibase \
      --driver="$DRIVER_CLASS" \
      --classpath="$DRIVER_CLASSPATH" \
      --changeLogFile=changelog-master.yaml \
      --url="$DB_URL" \
      --username="$DB_USERNAME" \
      --password="$DB_PASSWORD" \
      --outputDefaultCatalog=false \
      --outputDefaultSchema=false \
      --contexts="$1" \
      updateSQL > ${2}

    # Remove updates to DATABASECHANGELOGLOCK
    sed -i '/^UPDATE DATABASECHANGELOGLOCK/{ N; /.*/d }' ${2}
    sed -i '/^--\s*Lock Database/d' ${2}
    sed -i '/^--\s*Release Database Lock/d' ${2}

    # Remove 'USE' statement
    sed -i '/^USE omp;/{ N; /.*/d }' ${2}

    # Remove repeated blank lines
    sed -i -e '/./b' -e :n -e 'N;s/\n$//;tn' ${2}

    # Remove Liquibase header
    sed -i '/^Starting Liquibase/d' ${2}
    sed -i -e 1,9d ${2}
}

function runMigrationForContext() {
    liquibase \
      --driver="$DRIVER_CLASS" \
      --classpath="$DRIVER_CLASSPATH" \
      --changeLogFile=changelog-master.yaml \
      --url="$DB_URL" \
      --username="$DB_USERNAME" \
      --password="$DB_PASSWORD" \
      --defaultSchemaName="$DB_SCHEMA" \
      --contexts="$1" \
      migrate
}

pushd .. > /dev/null

generateScriptForContext create ./scripts/mssql/omp-7.17.0-mssql-create.sql

runMigrationForContext create

generateScriptForContext upgrade ./scripts/mssql/omp-7.17.1.0-mssql-upgrade.sql

popd > /dev/null
