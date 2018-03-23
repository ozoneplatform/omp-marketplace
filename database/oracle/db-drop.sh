#!/usr/bin/env bash

DB_URL="jdbc:oracle:thin:@localhost:1521:oracle"
DB_USERNAME="omp"
DB_PASSWORD="omp"
DB_SCHEMA="omp"

DRIVER_CLASS="oracle.jdbc.driver.OracleDriver"
DRIVER_CLASSPATH="./drivers/ojdbc8.jar"


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
