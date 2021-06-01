# 1. create fatjar
`./mvnw clean verify`

# 2. to run spring boot application with RestTemplateGreetingsClientApiGateway

`./mvnw spring-boot:run`
`http://192.168.0.6:8082/api/resttemplate/pankaj`

# 3. To run spring boot application with openfeign

`mvn spring-boot:run -Dspring-boot.run.profiles=feign`

`http://192.168.0.6:8082/api/feign/pankaj`

# 4. Zuul application.properties

`zuul.routes.hi.path=/lets/**`
`zuul.routes.hi.serviceId=greetings-service`

This configuration maps the request to, the greetings-service, everything from / and below , to /lets/* on the dege servie

url will now be `http://localhost:8082/lets/greeting-world` instead of `http://localhost:8082/greetings-service/greeting-world`
# 5. The zuul routes from routes endpoint

• enable all endpoints in application.properties

`management.endpoints.web.exposure.include=*`

• use below  url for routes

`http://localhost:8082/actuator/routes`

# 6. Run the greetings-client edge service with the profile "throttled" 
`http://localhost:8082/lets/greet/pankaj`

it will give a valid json response if request received after very 10s.
If there is more than one request within 10s , Zuul filter will throw an error of `Too Many Requests`



