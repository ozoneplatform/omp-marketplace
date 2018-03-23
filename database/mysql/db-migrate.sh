#!/usr/bin/env bash

pushd .. > /dev/null

liquibase \
 --driver=com.mysql.cj.jdbc.Driver \
 --classpath=./drivers/mysql-connector-java-8.0.11.jar \
 --changeLogFile=changelog-master.yaml \
 --url="jdbc:mysql://localhost:3306/omp?verifyServerCertificate=false&useSSL=false" \
 --username=omp \
 --password=omp \
 --defaultSchemaName=omp \
 --contexts=create \
 migrate

popd > /dev/null
