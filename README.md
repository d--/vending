# vending
My solution to the Pillar Technology Vending Machine kata.

|                  |                           |
| ---------------- | -------------------------:|
| Code:            |                    Java 7 |
| Build:           |        Gradle 2.4 Wrapper |
| Static analysis: | Findbugs, PMD, Checkstyle |
| Test coverage:   |                    Jacoco |

To build:

    $ ./gradlew build
  
Jacoco will generate a test coverage report at:

    ./build/reports/jacoco/test/html/index.html

To generate IntelliJ IDEA project files:

    $ ./gradlew idea
