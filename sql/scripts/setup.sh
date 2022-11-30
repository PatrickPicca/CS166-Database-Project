#! /bin/bash
source ./startPostgreSQL.sh
sleep 2
source ./createPostgreDB.sh
source ./create_db.sh
sleep 2
psql -h localhost -p $PGPORT $USER"_DB"