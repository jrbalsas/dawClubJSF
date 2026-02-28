dawClub using Jakarta Faces [![Build Status](https://travis-ci.com/jrbalsas/dawClubJSF.svg?branch=master)](https://travis-ci.com/jrbalsas/dawClubJSF)
==========

Sample Maven project with Jakarta Faces CRUD Web App

Features:
-------------
- Views implemented with Jakarta Faces tags and HTML5-friendly markup tags 
- Simple DAOMap for testing purpose
- Sample DAO using Jakarta Data Repository definition
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
    - IdentityStores: InMemory, Database, Custom
    - HttpAuthenticationMethods: Basic, Form, CustomJSF Form/controller

## Requirements

- JDK 21 LTS (supported JDK 17)
- Jakarta EE 11 Application Server, e.g. Payara 7+
- WildFly 38+:
  - [Requires](https://docs.wildfly.org/38/WildFly_Elytron_Security.html#Elytron_and_Java_Authentication_SPI_for_Containers-JASPI) 
disabling integrated JASPI to support Jakarta EE Security API. There is a configuration script to apply these 
changes in https://github.com/wildfly/quickstart/tree/main/ee-security#configure-the-server
  - Add H2 Database runtime support to pom.xml  

## Usage
- Compile and deploy in a JEE Application Server

### DB Access:
1. Configure JEE Datasource in web.xml
2. Select the same Map or JData DAOs implementation in controller, AppConfig and ClubIdentityStore classes
3. Create sample data on AppConfig class
4. (optional) resources/META-INF/sql/schema.sql can be used to create tables not managed by JPA
5. (optional) resources/META-INF/sql/sampledata.sql used to insert sample data into JPA managed tables

### Access control:
1. Uncomment access-restriction section in access control rules (web.xml)
2. Uncomment required HttpAuthenticationMechanism in AppConfig class. Standard Form by default
3. Uncomment preferred IdentityStore/s in AppConfig class if required.
4. Custom ClubIdentityStore available by default. Select Map or JData DAOs implementation to access user credentials.
