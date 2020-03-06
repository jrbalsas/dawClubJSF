dawClub using JSF
==========

Sample Maven Netbeans project with JSF CRUD Web App

Features:
-------------
- Simple DAOMap for testing purpose
- Sample DAOJPA implementation
- Uses CDI annotations with qualifiers for DAO selection in Controller
- Resource injection for JNDI connection pool
- Bean Validation with customized messages on ValidationMessages.properties
- Simple Entity CRUD views
- DataTable row editing view
- Sample view modification using AJAX
- Optional Authentication/Authorization rules and Logout button
- Persistence.xml configuration with JNDI datasource

## Requeriments

- Jakarta EE 8 Application Server, e.g. Payara

## Usage
- Compile and deploy in a JEE Application Server

### DB Access:
1. Configure JEE Datasource in web.xml
2. resources/META-INF/sql/schema.sql can be used to create tables not manated by JPA
3. resources/META-INF/sql/sampledata.sql used to insert sample data into database
4. Select Map or JPA DAOs implementation in controller
5. JPA DAO requires enable JNDI datasource in persistence.xml

### Realm Authentication/Authorization:
1. Create Realm in JEE Application Server, e.g. using AS GUI/asadmin tool
2. Uncomment/Create Security-constraints in web.xml
3. Select valid realm in web.xml