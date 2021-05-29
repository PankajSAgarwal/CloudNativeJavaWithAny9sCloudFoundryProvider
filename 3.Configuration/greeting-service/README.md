1. The Controller class GreetingController annotated with RefreshScope
2. To send a RefreshScopedEvent we send a POST method to  refresh endpoint exposed by actuator
`http://localhost:8080/actuator/refresh`

Not GET on browser to /actutor/refresh will not refesh .

3. To update a property

a. Update the property in application.yml of config-server
b. Commit the changes in git.
c. Send a Post request with an empty body to `http://localhost:8080/actuator/refresh`
`curl -d{} http://localhost:8080/actuator/refresh`
d. Test using `http://localhost:8080/greeting` 
