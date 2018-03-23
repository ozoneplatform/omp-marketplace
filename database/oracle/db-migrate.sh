#!/usr/bin/env bash

pushd .. > /dev/null

liquibase \
 --driver=oracle.jdbc.driver.OracleDriver \
 --classpath=./drivers/ojdbc8.jar \
 --changeLogFile=changelog-master.yaml \
 --url="jdbc:oracle:thin:@localhost:1521:oracle" \
 --username=omp \
 --password=omp \
 --defaultSchemaName=omp \
 --contexts=create \
 migrate

popd > /dev/null
