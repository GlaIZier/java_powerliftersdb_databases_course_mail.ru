# Java interface to powerliftersdb

Java interface to [this](https://github.com/GlaIZier/sql_powerliftersdb_databases_course_mail.ru) database.

## Dependencies
To make this app work u need [postgresql driver] (https://jdbc.postgresql.org/download.html) to be stored in ur local
maven repo:
```
mvn install:install-file -Dfile=./postgresql-9.4.1208.jar -DgroupId=org.postgresql -DartifactId=pjdbc -Dversion=9.4.1208 -Dpackaging=jar
```