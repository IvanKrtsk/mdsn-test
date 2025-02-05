# **Modsen test task**
This project is an implementation of a test task, the purpose of which is to develop a microservice application for a book library

### Project structure:
1. Book-storage-service
2. Book-tracker-service
3. Discovery-server
4. API-Gateway

### Technologies used
1. Spring Boot
2. ORM: Spring Data Jpa
3. RDBMS: PostgreSQL
4. MapStruct
5. Authentication via bearer token(JWT, Spring Security, Keycloak)
6. Apache Kafka
7. Swagger
8. Migrations: Liquibase
9. Testing: JUnit, Mockito, Testcontainers

### Getting Started
#### Preparation:
##### You must first install: 
* Java 21(or higher)
* PostgreSQL
* Apache Kafka(installation instruction: https://kafka.apache.org/quickstart)
* Keycloak 26.1.0(or higher)

##### Then, to work successfully, you will need:
For database:
* Create a database with the name "mdsn_db" on the default PostgreSQL port(5432) with the default login and password "postgres"

For Apache Kafka:
* I added a "data" folder to the project. After you follow the installation tutorial, download and unzip the folder with kafka, you need to move the folder called "data" (from the directory "kafka-files") and place it in the directory "kafka_{version}" **!or add next folders: data/kafka-logs, data/zookeeper manually!**. After that, replace the existing files "zookeeper.properties" and "server.properties" of the folder "kafka_{version}/config" with the ones I provided in the directory "kafka-files"

For Keycloak:
* Create a new realm with name "mdsn"
* Create a new client(for example with name "springsecurity") and add a root url: http://localhost:8083, valid redirect URIs: http://localhost:8083/*
* Create 2 realm roles with names: "ROLE_MANAGER", "ROLE_USER"

### Before next steps make sure that you started Apache Kafka and Keycloak

#### The next step is to install the application itself
First of all do this to clone the repository: 
`git clone https://github.com/IvanKrtsk/mdsn-test.git`

Then you should do `mvn clean install` in every service

The next step is to launch the application, !please follow the given sequence!

`java -jar target/discovery-server-0.0.1-SNAPSHOT.jar`

`java -jar target/api-gateway-0.0.1-SNAPSHOT.jar`

Before the next steps, run docker (required step for testcontainers)

`java -jar target/book-storage-service-0.0.1-SNAPSHOT.jar`

`java -jar target/book-tracker-service-0.0.1-SNAPSHOT.jar`

Gateway will start on: http://localhost:8083

### Swagger
#### You can view all documentation via swagger on api-gateway:
http://localhost:8083/swagger-ui.html

![img.png](pictures/img.png)

Choose service here:

![img.png](pictures/img1.png)

_!But in order to make requests, you need to get an authorization token!_

### Authorization
So, if you have configured the Keycloak as I indicated, all you have to do is create a test user and assign him a role.
After that you should to perform next http request(make sure keycloak is running, !and don't forget to copy client secret!):

`

    POST http://localhost:8080/realms/mdsn/protocol/openid-connect/token

    Content-Type: application/x-www-form-urlencoded

    client_id=springsecurity&client_secret=vs164aNG7suT3685FqqHHsWLcw6HnL8O&username=tester&password=tester&grant_type=password

`

After that you will receive something like that:

`{

    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsNnV3WEFZUVhfWEpFUmhaQjl0a1Y3WU9veEhENEpHc2NMbzdDdTFGaHdVIn0.eyJleHAiOjE3Mzg1ODIyMDksImlhdCI6MTczODU4MTYwOSwianRpIjoiZTE0N2NmNmEtZmQ5MC00N2E0LTllNTktNGVlYTBkMTRlMjc4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tZHNuIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImI2OWM0MjQzLTJlMDEtNDg3OC1hNTQxLTU3OWM4YmNlZGYxYiIsInR5cCI6IkJlYXJlciIsImF6cCI6InNwcmluZ3NlY3VyaXR5Iiwic2lkIjoiOGVmNzI2ODctM2MzMS00ZWNmLTg4NGEtNTY1ZTBiOTA4YmIzIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODMiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJPTEVfVVNFUiIsImRlZmF1bHQtcm9sZXMtbWRzbiIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6InRlc3RlciB0ZXN0ZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0ZXIiLCJnaXZlbl9uYW1lIjoidGVzdGVyIiwiZmFtaWx5X25hbWUiOiJ0ZXN0ZXIiLCJlbWFpbCI6InRlc3RlckBnbWFpbC5jb20ifQ.Ddp7yFCrbjp7Y8bzfKtD6h_gMFJGPcg0WLxCmP2S8kbGNdr-RYDUzTeEqP51GedHzCy_HWykZAQJCd8ZhtNvSqX52ctd7uVg2OiFxQabiytjkjmU0-fv7SMb9trlbfy3qT6H_Ol3ryrhAstTQjiN6D1Qy7nOtfXKnOMIeSTyu76R0pXbIW6dtnL-xyCUc9vkF-ajXX2ZOFBgmalNAlol2tcwahjssY84eADaB9urhqYjAShPvdITnN0jttNM3LkTICLNXUkNF_-EPX_pvNBOZ-N_OdNLtUCRje16UwiAFe-SLjWXn3gEGJqquVgpamMOQ9xZIyKllOE0unUCplPpSQ",

    "expires_in": 600,

    "refresh_expires_in": 1800,

    "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzZmM3MzI1OC00ZTc5LTQ3YjMtOGNkNi0xYjI0Yjk0ODEyNmMifQ.eyJleHAiOjE3Mzg1ODM0MDksImlhdCI6MTczODU4MTYwOSwianRpIjoiOWE0MTRhYjctOTlmZi00NzBkLTkwZTMtOGUyYmIwOWE3ZDU2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tZHNuIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tZHNuIiwic3ViIjoiYjY5YzQyNDMtMmUwMS00ODc4LWE1NDEtNTc5YzhiY2VkZjFiIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InNwcmluZ3NlY3VyaXR5Iiwic2lkIjoiOGVmNzI2ODctM2MzMS00ZWNmLTg4NGEtNTY1ZTBiOTA4YmIzIiwic2NvcGUiOiJiYXNpYyBwcm9maWxlIGVtYWlsIHdlYi1vcmlnaW5zIGFjciByb2xlcyJ9.W7fkfAEZLQK-mxYN5MWlCTYEtY-RZsPFPtx4kuIHDtKwUw-AZBWeZfQNnwyOy6drXxGm97gtiUQtQpps9jI-hw",

    "token_type": "Bearer",

    "not-before-policy": 0,

    "session_state": "8ef72687-3c31-4ecf-884a-565e0b908bb3",

    "scope": "profile email"
}`

And after that all you need to do is to copy access_token and paste it here:

![img.png](pictures/img2.png)

#### Roles:
- ROLE_MANAGER: you will get access to any endpoint in book-storage-service and book-tracker-service
- ROLE_USER: you will get access to all endpoints in book-tracker-service, but you can't access /book-storage/add, /book-storage/delete, /book-storage/update in book-storage-service