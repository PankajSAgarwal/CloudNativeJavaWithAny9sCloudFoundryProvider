# 1. create fat jar
  `./mvnw clean verify`
  
# 2. push route-service to anynines cloudfoundry
    
    `cf push -f manifest.yml`
# 3 . to check the route name   

    `cf apps`
# 4. check the recent logs

    `cf logs --recent route-service`
            OR
     `cf logs route-service`
# 5. API Uri

https://<<routes>>/

e.g https://route-service-fearless-baboon-wr.de.a9sapp.eu

