#!/usr/bin/env bash

pushd .. > /dev/null

liquibase \
 --driver=org.postgresql.Driver \
 --classpath=./drivers/postgresql-42.1.4.jre7.jar \
 --changeLogFile=changelog-master.yaml \
 --url="jdbc:postgresql://localhost:5432/omp" \
 --username=omp \
 --password=omp \
 --defaultSchemaName=public \
 --contexts=create \
 migrate

popd > /dev/null
