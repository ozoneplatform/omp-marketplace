#!/usr/bin/env bash

CONTAINER="mssql_mssql_1"

SQLCMD=""
SQLCMD="$SQLCMD CREATE DATABASE omp;"
SQLCMD="$SQLCMD CREATE DATABASE omp_ref;"

docker exec ${CONTAINER} sqlcmd -U sa -P PASSword123!@# -Q "$SQLCMD"
docker exec ${CONTAINER} sqlcmd -U sa -P PASSword123!@# -d omp_ref -i /schema/omp_7.17.0_mssql.sql
