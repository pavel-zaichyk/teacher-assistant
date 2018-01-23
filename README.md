# Teacher Assistant

### Description

This application is an electronic journal that helps university teachers with their work.

### Requirements

 - Oracle JDK V1.8
 - Apache Maven V3
 - Apache Tomcat V8.x

## Development

1. Copy **app_files** folder (/src/main/resources/tomcat/app_files) into **TOMCAT_HOME** folder.

    This folder should contain next:
    * folder **config** and file **config.properties**
    * folder **csv**
    * folder **database** and file **db.s3db**
    * folder **logs**

2. Create folders **/photo/students** in **TOMCAT_HOME/webapps**

3. Build web-archive and move it into **TOMCAT_HOME/webapps**

## Build

> `mvn clean install`

## Create release

> `mvn release:clean release:prepare -Darguments='-Dmaven.test.skip=true'`
