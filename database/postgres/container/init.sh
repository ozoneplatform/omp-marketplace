#!/usr/bin/env bash

set -e

psql -v ON_ERROR_STOP=1 \
 --username="$POSTGRES_USER" \
 --quiet \
<<-EOSQL
    CREATE USER omp WITH SUPERUSER LOGIN PASSWORD 'omp';

    CREATE DATABASE omp;
    GRANT ALL PRIVILEGES ON DATABASE omp TO omp;

    CREATE DATABASE omp_ref;
    GRANT ALL PRIVILEGES ON DATABASE omp_ref TO omp;
EOSQL

psql -v ON_ERROR_STOP=1 \
 --username="$POSTGRES_USER" \
 --dbname="omp_ref" \
 --quiet \
 --file="/schema/omp_7.17.0_postgres.sql"
