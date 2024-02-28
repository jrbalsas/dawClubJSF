dawClub using Jakarta Faces [![Build Status](https://travis-ci.com/jrbalsas/dawClubJSF.svg?branch=master)](https://travis-ci.com/jrbalsas/dawClubJSF)
==========

Sample Maven project with Jakarta Faces CRUD Web App

Features:
-------------
- Simple DAOMap for testing purpose
- Sample DAOJPA implementation
- Uses CDI annotations with qualifiers for DAO selection in Controller
- Bean Validation with customized messages on ValidationMessages.properties
- Custom BeanValidation Implementation: ``@Dni``
- Simple Entity CRUD views
- DataTable row editing view
- Sample view modification using AJAX
- H2 database connection
- Standard JNDI connection pool definition in web.xml
- Persistence.xml configuration with JNDI datasource
- Optional access control rules
- JEE Security API options :
    - IdentityStores: Embedded, Database, Custom
    - HttpAuthenticationMethods: Basic, Form, CustomJSF Form/controller
- Upload entity images on external folder
- Specialized Servlet for downloading entity external images ``ClienteImageServlet``
- Read config parameters from ``main/resources/application.properties`` file

## Requirements

- JDK 17+
- Jakarta EE 10 Application Server, e.g. Payara 6+
- WildFly 20+:
  - [Requires](https://docs.wildfly.org/27/WildFly_Elytron_Security.html#Elytron_and_Java_Authentication_SPI_for_Containers-JASPI) 
disabling integrated JASPI to support Jakarta EE Security API. There is a configuration script to apply these 
changes in https://github.com/wildfly/quickstart/tree/main/ee-security#configure-the-server
  - Add H2 Database runtime support to pom.xml  

## Usage
- Compile and deploy in a JEE Application Server
- By default, uploaded user images are placed on ``<<User_home_dir>>/webapp-data/customerimg`` folder. Change it on ``application.properties`` file 

### DB Access:
1. Configure JEE Datasource in web.xml
2. resources/META-INF/sql/schema.sql can be used to create tables not manated by JPA
3. resources/META-INF/sql/sampledata.sql used to insert sample data into database
4. Select Map or JPA DAOs implementation in controller

### Access control:
1. Uncomment access-restriction section in access control rules (web.xml)
2. Uncomment required IdentityStore/s in AppConfig class. Custom ClubIdentityStore available by default
3. Uncomment required HttpAuthenticationMechanism in AppConfig class. Standard Form by default
