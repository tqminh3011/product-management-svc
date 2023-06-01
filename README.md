# SMG Backend Challenge: Product Management Service

## Overview
Following technology stacks are applied to build this product-management-svc:
* Language: **Java 17**
* Servlet container: **Netty**
* **Spring Boot 3** with Reactive support (**Webflux**, **R2DBC**)
* Database: **MySQL 8**
* Message broker: **Kafka**
* Unit Testing: **JUnit 5, Mockito**
* Integration Testing: **Spring Boot Test, TestContainers**
* Dependency management: **Gradle**

## Project structure
TBU

## How to run
### Prerequisites
Make sure you have the following dependencies installed on your machine:
* JDK 17 (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* Docker (https://docs.docker.com/engine/install/)
* Gradle (https://gradle.org/install/)
</br></br>
### Step 1: Pre-check environment
-- **IMPORTANT** -- Before proceeding to next steps:
* Start Docker engine
* If you have multiple Java versions installed, make sure to switch to Java17.
</br> </br>
### Step 2: Build the service

Open terminal in root folder of *product-management-svc* project, execute following command: ```./gradlew clean build``` </br>
</br>It'll start compiling the application into .jar file (target to ```build/libs``` folder) and execute all written tests (incl. Unit and Integration Tests).
</br> </br>

### Step 3: Run the service
1. Keep staying at the root folder, execute following command ```docker-compose up``` for first time running
   </br>(For next time, you can use command ```docker-compose start```)
2. Execute command ```java -jar build/libs/product-management-svc-1.0.0.jar``` . This will start the Spring Boot service, which should take about <10s depending on your machine.
</br> </br>

### Step 4: Test the service
There are 2 ways for testing the service, via Curl commands or Swagger UI. I'll recommend the Swagger approach as it's simple to use and bring better visualization with UI.

#### Using Swagger UI for testing
After the service has been started, open any web browser and go to url: http://localhost:9611/swagger-ui.html. You can see the available endpoint and start testing from there.
</br>Example JSON payload:
```
{
  "name": "Denim Jacket - XL",
  "productOwner": "owner1",
  "type": "FASHION",
  "price": 10.99
}
```

#### Using Curl for testing
Sample Curl:
```
curl -X 'POST' \
  'http://localhost:9611/products' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Denim Jacket - XL",
  "productOwner": "owner1",
  "type": "FASHION",
  "price": 10.99
}'
```