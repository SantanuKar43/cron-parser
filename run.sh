#!/bin/bash
# Use docker to run
if [ $# -eq 0 ]; then
  echo "Usage: $0 <cron_expression>"
  exit 1
fi

cron_expression="$1"

docker build -t cronparser-image .

docker run --rm cronparser-image java -jar target/cronparser-1.0-SNAPSHOT.jar "$cron_expression"
