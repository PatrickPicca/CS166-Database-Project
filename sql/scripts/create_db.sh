#!/bin/bash
export PGPORT=8192
export PGDATA=$folder/myDB/data

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p $PGPORT $USER"_DB" < /home/csmajs/tlian020/VirtualBox\ VMs/CS166-Database-Project-Phase-3/sql/src/create_tables.sql
psql -h localhost -p $PGPORT $USER"_DB" < /home/csmajs/tlian020/VirtualBox\ VMs/CS166-Database-Project-Phase-3/sql/src/create_indexes.sql
psql -h localhost -p $PGPORT $USER"_DB" < /home/csmajs/tlian020/VirtualBox\ VMs/CS166-Database-Project-Phase-3/sql/src/load_data.sql

