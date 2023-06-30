#!/bin/bash

psql -c 'CREATE DATABASE file_api_db'
psql file_api_db -f /docker-entrypoint-initdb.d/file_api_init.txt
psql -c 'CREATE DATABASE song_api_db'
psql song_api_db -f /docker-entrypoint-initdb.d/song_api_init.txt