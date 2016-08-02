Configuraciones

# H1 1. Configurar maven settings 

1.1 Descargar el archivo gohan-settings.xml en el directorio donde se encuentre la carpeta .m2
1.2 Ajustar los directorios que se encuentran en el archivo. 
1.3 Configurar en IDE para que apunte a este archivo
1.4 Instalar libreria APNS:
	mvn install:install-file -Dfile=JavaPNS_2.3_Beta_3.jar -DgroupId=com.apple -DartifactId=javaPNS -Dversion=2.3 -Dpackaging=jar

Obs. Para MyEclipse, se debe configurar en Window->Preferences->User Settings

# H1 2. Configurar tomcat 
2.1 Descargar el Tomcat 7 en http://tomcat.apache.org/download-70.cgi
2.2 chmod +x tomcat/bin/*.sh
2.3 Antes de levantar el tomcat hay que poner el driver de postgres en el directorio "lib" dentro de tomcat. 
El driver se puede en contrar dentro del repositorio maven local en org/postgresql/postgresql/9.3-1101-jdbc4/postgresql-9.3-1101-jdbc4.jar
2.4 Agregar la siguiente configuración al server.xml del tomcat en la sección GlobalResources.
```xml
	<Resource name="jdbc/notification-server" 
	global="jdbc/notification-server" 
	auth="Container" 
	type="javax.sql.DataSource" 
	driverClassName="org.postgresql.Driver" 
	url="jdbc:postgresql://**localhost:5432**/**notification-server**" 
	**username="postgres"**
	**password="postgres"**
	maxActive="100"
	maxIdle="20"
	minIdle="5"
	maxWait="10000"/>
```
# H1 3. Actualización de Parámetros de Aplicación
update parametro set valor = **'RUTA_VALIDA_EN_EL_SERVIDOR'** where nombre = 'PATH_CERTIFICADO';
update parametro set valor = **'NUMERO_EN_SEGUNDOS'** where nombre = 'IOS_TIMER';
update parametro set valor = **'NUMERO_EN_SEGUNDOS'** where nombre = 'ANDROID_TIMER';