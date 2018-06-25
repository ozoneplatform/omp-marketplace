#!/usr/bin/env bash

set -e

mysql \
 --user=root \
 --password=${MYSQL_ROOT_PASSWORD} \
<<-EOSQL
    CREATE DATABASE omp;
    GRANT ALL PRIVILEGES ON omp.* TO omp;

    CREATE DATABASE omp_ref;
    GRANT ALL PRIVILEGES ON omp_ref.* TO omp;
EOSQL


mysql \
 --user=root \
 --password=${MYSQL_ROOT_PASSWORD} \
 omp_ref < /schema/omp_7.17.0_mysql.sql
