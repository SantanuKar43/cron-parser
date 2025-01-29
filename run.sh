#!/bin/bash
# Use docker to run
docker build -t cronparser-image .
docker run --rm cronparser-image java -jar target/cronparser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
