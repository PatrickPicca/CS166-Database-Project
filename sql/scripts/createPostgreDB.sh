#! /bin/bash
export PGDATA=$folder/myDB/data
echo "creating db named ... "$USER"_DB"
createdb -h localhost -p 8192 $USER"_DB"
pg_ctl status
