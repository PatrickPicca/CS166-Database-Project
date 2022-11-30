#! /bin/bash
cp ./*.dat /tmp/$USER/myDB/data
psql -h localhost -p 8192 "$USER"_DB < ./Lab_8/create_tables.sql