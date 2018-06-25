#!/usr/bin/env bash

CONTAINER="postgres_postgres_1"

docker exec ${CONTAINER} sh -c \
 "pg_dump --dbname=omp_ref --username=omp --schema-only --no-owner" \
 > ./postgres-old.sql

docker exec ${CONTAINER} sh -c \
 "pg_dump --dbname=omp --username=omp --schema-only --no-owner" \
 > ./postgres-new.sql
