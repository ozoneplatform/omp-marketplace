#!/usr/bin/env bash

DB_URL="jdbc:postgresql://localhost:5432/omp"
DB_USERNAME="omp"
DB_PASSWORD="omp"
DB_SCHEMA="public"

DRIVER_CLASS="org.postgresql.Driver"
DRIVER_CLASSPATH="./drivers/postgresql-42.1.4.jre7.jar"


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
