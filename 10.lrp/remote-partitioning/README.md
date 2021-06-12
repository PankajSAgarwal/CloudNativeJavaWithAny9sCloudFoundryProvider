1. Launch a few instances of worker nodes using worker profiles
2. Launch the leader node by not specifying a specific profile.
3. Launch an instance of job . The job doesnt run on application launch.

You can trigger a run through a REST endpoint 

`curl -d{} http://localhost:8080/migrate`

To check the status of count in table people and new_people

`curl -d{} http://localhost:8080/status`
 