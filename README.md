Instalar libreria APNS:
mvn install:install-file -Dfile=JavaPNS_2.3_Beta_3.jar -DgroupId=com.apple -DartifactId=javaPNS -Dversion=2.3 -Dpackaging=jar


Agregar al server.xml / Tomcat
<GlobalNamingResources>
<Resource name="jdbc/notification-server"
           global="jdbc/notification-server"
           auth="Container"
           type="javax.sql.DataSource"
           driverClassName="org.postgresql.Driver"
           url="jdbc:postgresql://localhost:5432/notification-server"
           username="postgres"
           password="postgres"

           maxActive="100"
           maxIdle="20"
           minIdle="5"
           maxWait="10000"/>
 </GlobalNamingResources>
