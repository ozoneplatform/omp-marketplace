#!/usr/bin/env bash

DB_URL="jdbc:oracle:thin:@localhost:1521:oracle"
DB_USERNAME="omp"
DB_PASSWORD="omp"
DB_SCHEMA="omp"

DRIVER_CLASS="oracle.jdbc.driver.OracleDriver"
DRIVER_CLASSPATH="./drivers/ojdbc8.jar"


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
    sed -i '/^UPDATE DATABASECHANGELOGLOCK/d' ${2}
    sed -i '/^--\s*Lock Database/d' ${2}
    sed -i '/^--\s*Release Database Lock/d' ${2}

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

generateScriptForContext create ./scripts/oracle/omp-7.17.0-oracle-create.sql

runMigrationForContext create

generateScriptForContext upgrade ./scripts/oracle/omp-7.17.1.0-oracle-upgrade.sql

popd > /dev/null
