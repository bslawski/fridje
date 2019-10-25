#!/bin/bash

source config.cfg

apt-get install sqlite3

sqlite3 "$DB_DIR/fridje.db" < init_db.sql
