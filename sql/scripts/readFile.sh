#! /bin/bash
export PGPORT=8192
psql -h localhost -p $PGPORT "$USER"_DB < $1