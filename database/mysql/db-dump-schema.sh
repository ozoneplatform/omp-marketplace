#!/usr/bin/env bash

CONTAINER="mysql_mysql_1"

docker exec ${CONTAINER} sh -c \
 'exec mysqldump --no-data --user=omp --password=omp omp_ref' \
 > ./mysql-old.sql

docker exec ${CONTAINER} sh -c \
 'exec mysqldump --no-data --user=omp --password=omp omp' \
 > ./mysql-new.sql
