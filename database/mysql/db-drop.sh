#!/usr/bin/env bash

DB_URL="jdbc:mysql://localhost:3306/omp?verifyServerCertificate=false&useSSL=false"
DB_USERNAME="omp"
DB_PASSWORD="omp"
DB_SCHEMA="omp"

DRIVER_CLASS="com.mysql.cj.jdbc.Driver"
DRIVER_CLASSPATH="./drivers/mysql-connector-java-8.0.11.jar"


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
