dawClub using JSF
==========

Sample Maven Netbeans project with JSF CRUD Web App

Features:
-------------
- Simple DAOList for testing purpose
- Sample DAOJDBC implementation
- Sample DAOJPA implementation
- Uses CDI annotations with qualifiers for DAO selection in Controller
- Resource injection for JNDI connetion pool
- EntityManagerFactory CDI Producer method for EntityManager Injection in Servlet containers, i.e. Tomcat...
- Bean Validation with customized messages on ValidationMessages.properties
- Simple Entity CRUD views
- DataTable row editing view
- Sample view modification using AJAX
- Optional Authentication/Authorization rules and Logout button
- Persistence.xml configuration with JNDI datasource

## Requeriments

- JEE Application Server, i.e. Payara, Glassfish

## Usage
- Compile and deploy in a JEE Application Server

### DB Access:
1. Configure JEE Datasource in web.xml
2. WEB-INF/DBInitScript.sql can be used to create required table and insert sample data into database
3. Select JDBC or JPA DAOs implementation in controller
4. JPA DAO requires enable JNDI datasource in persistence.xml

### Realm Authentication/Authorization:
1. Create Realm in JEE Application Server, e.g. using AS GUI/asadmin tool
2. Uncomment/Create Security-constraints in web.xml
3. Select valid realm in web.xml