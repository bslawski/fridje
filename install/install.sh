#!/bin/bash

source config.cfg

sudo apt-get install sqlite3

sqlite3 "$DB_FILE" < init_db.sql
