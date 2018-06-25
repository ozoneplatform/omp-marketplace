#!/usr/bin/env bash

DB_URL="jdbc:postgresql://localhost:5432/omp"
DB_USERNAME="omp"
DB_PASSWORD="omp"
DB_SCHEMA="public"

DRIVER_CLASS="org.postgresql.Driver"
DRIVER_CLASSPATH="./drivers/postgresql-42.1.4.jre7.jar"


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
    sed -i '/^UPDATE databasechangeloglock/d' ${2}
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

generateScriptForContext create ./scripts/postgresql/omp-7.17.0-postgresql-create.sql

runMigrationForContext create

generateScriptForContext upgrade ./scripts/postgresql/omp-7.17.0-postgresql-upgrade.sql

popd > /dev/null
