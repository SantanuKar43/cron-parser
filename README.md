### Description
Cron expression parser based on https://www.ibm.com/docs/en/db2/12.1?topic=task-unix-cron-format

Allows for slash operators like "1/2" to be processed as "1-{last valid value}/2"

### Dependencies:
- Java 21 - https://www.oracle.com/java/technologies/downloads/#java21
- Maven - https://maven.apache.org/download.cgi

### Build
From project root, run:

```mvn clean package``` 

### Run
From project root, run:

```java -jar target/cronparser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"```

