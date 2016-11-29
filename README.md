## Java interface to powerliftersdb

Java example interface to [this](https://github.com/GlaIZier/sql_powerliftersdb_databases_course_mail.ru) database.
Have been written with usage of:
* Java 8
* Servlets
* Jdbc
* Postgres connection pool
* Tomcat connection pool
* Hibernate 
* JPA
* Freemarker
* Maven 
* Maven Wrapper
* Tomcat
* Postgres
* Docker

### Dependencies
All project dependencies will be downloaded by Maven from central repo.
To run this application locally you need:
 1. [PostgreSql 9.6](https://www.postgresql.org/download/) installed locally
 2. Installed [Tomcat 7](http://tomcat.apache.org/download-70.cgi) with java 8 support
You probably can use newest versions of Tomcat and Postgres but I didn't test it.

To run this application in container you need to install [Docker](https://www.docker.com/)

### Profiles 
There are two profiles in resources/profiles dir and described in pom file to package application for local usage and inside docker accordingly.
If you need to deploy this application to remote tomcat or with remote postgres support you should add another profile and edit pom file.

### Build
#### Build from sources 
To build locally you need installed Maven locally:
```
mvn clean package -P local
```
or you can use Maven Wrapper:
```
./mvnw clean package -P local
```
 
#### Build docker image
If you don't have postgres installed or tomcat you can use docker. 
Go to docker folder and run
```
make
```
This will create tomcat 7 container with application inside. 
But you still need initialized postgres service as external dependency.

### Run
#### Run locally
1. Make sure you installed tomcat 7 and run it on 8080 port
2. Make sure you installed Postgresql 9.6 and run it on 5432 port
3. Initialize Postgres with data:
    ```
    psql -U postgres -d postgres -a -f docker/init/init.sql
    ```
4. Deploy application on Tomcat:
    ```
    mvn clean tomcat7:redeploy -P local
    ```
    or
    ```
    ./mvnw clean tomcat7:redeploy -P local
    ```
    or 
    just copy war file from docker/app to <tomcat-dir>/webapps folder 
5. Use browser to run the application
    ```
    http://localhost:8080/powerliftersdb
    ```

#### Run inside docker
To run inside docker container use command:
```
make run
```
and go to 
```
http://localhost:8080/powerliftersdb
```

### LICENSE
GNU GPLv3