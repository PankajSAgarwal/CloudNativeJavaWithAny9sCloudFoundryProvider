# 1. create fat jar
  `./mvnw clean verify`
# 2. create user provided service routing-eureka-service

`cf create-user-provided-service routing-eureka-service -r https://routing-eureka-service.de.a9sapp.eu`
  
# 3. push route-service to anynines cloudfoundry
    
    `cf push -f manifest.yml`
# 3 . to check the route name   

    `cf apps`
# 4. check the recent logs

    `cf logs --recent route-service`
            OR
     `cf logs route-service`
# 5. API Uri

https://<<routes>>/<<api>>

e.g `https://route-service-fearless-baboon-wr.de.a9sapp.eu`

