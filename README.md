dawClub using JSF
==========

Sample Netbeans project with JSF CRUD Web App

Features:
-------------
- Simple DAOList for testing purpose
- Sample DAOJDBC implementation
- Sample Glassfish Connection Pool configuration file
- Uses CDI annotations with qualifiers for DAO selection
- Resource injection for JNDI connetion pool
- Bean Validation with customized messages on ValidationMessages.properties
- Simple Entity CRUD views
- DataTable row editing view
- Sample view modification using AJAX

Requeriments:
--------------
- Glassfish +4 required (for CDI annotations)
- For DAOJdbc testing a JDBC database connection required, i.e. DerbyDB (edit glassfish-resources.xml to adapt connection pool for your database)
- WEB-INF/db.sql can be used to create required table into database

