### Description
Cron expression parser loosely based on the format specified here: https://www.ibm.com/docs/en/db2/12.1?topic=task-unix-cron-format

Allows for slash operators like "1/2" to be processed as "1-{last valid value}/2"

### Features
- Parses standard UNIX-like cron expressions (minute, hour, day of month, month, day of week).
- Supports special characters `("*", "-", ",", "/")`.
- Outputs all valid values for each of the fields as per the given expression.
- All field names and their allowed values:

  | Field name   | Allowed values | Allowed special characters |
  |--------------|----------------|----------------------------|
  | minute       | 0-59           | `*` `,` `/` `-`            |
  | hour         | 0-23           | `*` `,` `/` `-`            |
  | day of month | 1-31           | `*` `,` `/` `-`            |
  | month        | 1-12           | `*` `,` `/` `-`            | 
  | day of week  | 0-6            | `*` `,` `/` `-`            |

### Dependencies:
- Java 21 or above - https://www.oracle.com/java/technologies/downloads/#java21
- Maven - https://maven.apache.org/download.cgi

### Build
From project root, run:

```
$ mvn clean package
``` 
### Test
From project root, run:
```
$ mvn verify
```
Current code coverage report:

| Package                           | Missed Instructions | Coverage | Missed Branches | Branch Coverage | Complexity Missed | Complexity Covered | Missed Lines | Covered Lines | Missed Methods | Covered Methods | Missed Classes |
|------------------------------------|---------------------|----------|-----------------|----------------|------------------|------------------|--------------|--------------|---------------|---------------|---------------|
| org.cronparser.model              | 111                 | 49%      | 10              | 50%            | 20               | 30               | 11           | 40           | 10            | 20            | 4             |
| org.cronparser                    | 15                  | 0%       | N/A             | N/A            | 2                | 2                | 4            | 4            | 2             | 2             | 1             |
| org.cronparser.parser.field       | 0                   | 100%     | 1               | 97%            | 1                | 31               | 0            | 70           | 0             | 12            | 1             |
| org.cronparser.parser             | 0                   | 100%     | 0               | 100%           | 0                | 7                | 0            | 20           | 0             | 3             | 1             |

**Total Coverage Summary**
- **Instructions:** 82%
- **Branches:** 83%


### Run
- Download jar from releases here - https://github.com/SantanuKar43/cron-parser/releases/tag/v1.0 and run:

```
$ java -jar cronparser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
```

- If building from the source code, navigate to the project root directory and run:

```
$ mvn clean package
$ java -jar target/cronparser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
```

- If using Docker, execute the run.sh script from the project root:

```
$ sh run.sh [expression]
```

### Sample input-output
input:
```
$ sh run.sh "*/15 0 1,15 * 1-5 /usr/bin/find"
```
output:
```
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find

```

