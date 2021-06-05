1. install Redis on Mac through brew
2.Start redis server
`redis-server`
3.Start redis client
 `redis-cli`
4. show all the keys
`keys *`

5. get keys

`get order-by-id::1`

6. To check services in cloudfoundry
`cf s`
7. To delete app in cloudfoundry

`cf d -f <<appname>>`

8. to delete a service in cloudfoundry

`cf ds -f <<service name>>`

9. create a new service in cloudfoundry

syntax : `cf cs <<marketplace servicename>> <<plan name>> <<custom bind service name>>`
e.g:
`cf cs a9s-redis50 redis-single-nano st-redis`

10. package the application into jar

`./mvnw clean package`

11. push the application to cloudfoundry

`cf push -p target/redis-0.0.1-SNAPSHOT.jar`

12. to scale more instances

cf scale -i 2 st-redis
 