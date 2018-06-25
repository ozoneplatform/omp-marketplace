#!/usr/bin/env bash

CONTAINER="mssql_mssql_1"

# docker exec ${CONTAINER} sh -c \
#  "MSSQL_SCRIPTER_PASSWORD=\"PASSword123!@#\" exec mssql-scripter --exclude-use-database --exclude-headers -S localhost -d omp_ref -U sa" \
#  > ./mssql-old.sql

docker exec ${CONTAINER} sh -c \
 "MSSQL_SCRIPTER_PASSWORD=\"PASSword123!@#\" exec mssql-scripter --exclude-use-database --exclude-headers -S localhost -d omp -U sa" \
 > ./mssql-new.sql
