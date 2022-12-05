#!/bin/bash
export PGPORT=8192
export PGDATA=$folder/myDB/data

#DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p $PGPORT $USER"_DB" < ../src/create_tables.sql
psql -h localhost -p $PGPORT $USER"_DB" < ../src/create_indexes.sql
psql -h localhost -p $PGPORT $USER"_DB" < ../src/load_data.sql
psql -h localhost -p $PGPORT $USER"_DB" < ../src/triggers.sql

