# Notification Server
El notification server es una aplicación multi tenant desarrollada en JEE que expone un API REST para envío de notificaciones a dispositivos iOS vía APNS y Android vía GCM, liberada bajo Apache License 2.0.
![Funcionamiento Notification Server](https://github.com/jokoframework/notification-server/blob/master/doc/readme-notification-server.png?raw=true "Funcionamiento")
## Configuraciones
### 1. Configurar maven settings 
1.1 Descargar el archivo gohan-settings.xml en el directorio donde se encuentre la carpeta .m2

1.2 Ajustar los directorios que se encuentran en el archivo. 

1.3 Configurar en IDE para que apunte a este archivo

1.4 Instalar libreria APNS:
```
mvn install:install-file -Dfile=JavaPNS_2.3_Beta_3.jar -DgroupId=com.apple -DartifactId=javaPNS -Dversion=2.3 -Dpackaging=jar
```
Obs. Para MyEclipse, se debe configurar en Window->Preferences->User Settings

### 2. Configurar tomcat 
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
### 3. Actualización de Parámetros de Aplicación
```sql
update parametro set valor = 'RUTA_VALIDA_EN_EL_SERVIDOR' where nombre = 'PATH_CERTIFICADO';
update parametro set valor = 'NUMERO_EN_SEGUNDOS' where nombre = 'IOS_TIMER';
update parametro set valor = 'NUMERO_EN_SEGUNDOS' where nombre = 'ANDROID_TIMER';
```
## Cómo utilizar
### 1. Registrar una aplicación
Se registra una aplicación con un nombre, los apikeys(android) en ambientes de desarrollo y/o producción, y los certificados de desarrollo y/o producción  iOs codificados en base64.
```bash
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 1caa3f73-12a5-2b8a-bc1d-91d68ebee9c7" -d '{
    "nombre": "appName",
    "apiKeyDev": "asdf989230978tjtJXdQXi7iuhv6dVp7-MQk",
    "apiKeyProd": "asdf989230978jtJXdQXi7iuhv6dVp7-MQk",
    "certificadoProd": "MIIMdwIBAzCCDD4GCSqG",
    "certificadoDev": "MIIMdwIBAzCCDD4G",
    "keyFileProd": ".macMac.",
    "keyFileDev": ".macMac."
}
' "http://localhost:8080/notification-server/api/aplicacion"
```

### 2. Registrar un evento
| Field             | Descripción  |
|-------------------|------------------------------------------------------------|
| androidDeviceList | Lista de registrationIds de dispositivos android que van a recibir a notificación. |
| iosDeviceList     | Lista de registrationIds de dispositivos ios que van a recibir a notificación.     |
| sendToSync        | Si es o no una notificación en segundo plano.                                      |
| productionMode    | Define si se utilizaran los apiKey/certificado de producción o los de desarrollo.  |
| payload           | El mensaje en formato Json que será enviado.                                       |
| descripcion       | El título que tendrá la notificación.                                              |
| applicationName   | Nombre de la aplicación.                                                           |
```bash
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 500281fb-1815-d7ec-d6d1-12f74d639c2b" -d '{
    
    "androidDevicesList":["c_vJOwFwU7A:APA91bGKnG8Iw6CebA3k9RCyiwVJ60flFFI3nvHj3yA4_av2nEGX_03tJIg7xI1oJ3zi2yh99_Z8hUkeMx5mIjZTui2GqyFiMWzU35PonZqviLHasFn6-rYQq8pE6206LtAyJ_WkoYxF", "dAWcIi0q9kY:APA91bFMilYJFAEJ2_6as36mLTZE656jMNSORQN_yfh1dP6AO5wSn1-wWXC1i5DQ0p9kHRINc4go1zLq2U-SB4qf5kyAktaxgtijWutNcbibwfUm1wF286_42fZFCl7xyoxcduMzbFsV"],
    "sendToSync":false,
    "iosDevicesList":["b4c5273e1805c1141857cf8717fe5dbf814723785cc2201ed856f60f19c43158"],
    "productionMode":false,
    "payload":{"titulo":"prb",
        "detalle":"detalle",
        "idLlamado":"abc"
    },
    "descripcion":"Notificacion prueba",
    "applicationName":"appName"
}' "http://localhost:8080/notification-server/api/evento"
```