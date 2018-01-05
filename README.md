# Teacher Assistant

### Description

This application is an electronic journal that helps university teachers with their work.

### Requirements

 - Oracle JDK V1.8
 - Apache Maven V3
 - Apache Tomcat V8.x

## Development
Database contains 18 tables.

## Build

> `mvn clean install`

## Create release

> `mvn release:clean release:prepare -Darguments='-Dmaven.test.skip=true'`
