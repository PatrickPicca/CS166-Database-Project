#! /bin/bash
source ./startPostgreSQL.sh
sleep 2
source ./createPostgreDB.sh
dir=$(git rev-parse --show-toplevel)
sed -i "s+/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3+$dir+g" ../src/load_data.sql > ../src/load_data.sql.tmp
source ./create_db.sh
sleep 2
rm ../src/load_data.sql.tmp
psql -h localhost -p $PGPORT $USER"_DB"