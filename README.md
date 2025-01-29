### Description
Cron expression parser based on https://www.ibm.com/docs/en/db2/12.1?topic=task-unix-cron-format

Allows for slash operators like "1/2" to be processed as "1-{last valid value}/2"

### Features
- Parses standard UNIX-like cron expressions (minute, hour, day of month, month, day of week)
- Supports special characters `("*", "-", ",", "/")`
- Outputs all valid values for each of the fields as per given the expression

### Dependencies:
- Java 21 - https://www.oracle.com/java/technologies/downloads/#java21
- Maven - https://maven.apache.org/download.cgi

### Build
From project root, run:

```mvn clean package``` 
### Test

```mvn test```

### Run
Download jar from release https://github.com/SantanuKar43/cron-parser/releases/tag/v1.0 and run:

```java -jar cronparser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"```

If building source code, go to project root and run:

```java -jar target/cronparser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"```

### Output
```
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find

```

