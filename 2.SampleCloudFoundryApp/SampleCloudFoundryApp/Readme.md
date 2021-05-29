## CloudFoundry command
1. `cf login`
2.`cf create-service p-mysql 100mb bootcamp-customers-mysql`
3. to bind to mysql service in anynine cf provider
General Syntax: `cf create-service <SERVICE> <PLAN> <DB Name>`

To see the offerings of a service in market place
`cf marketplace -e <servicename>`
e.g `cf marketplace -e a9s-mysql101`
e.g `cf create-service a9s-mysql101 mysql-single-small bootcamp-customers-mysql`
4. `cf push -p target/<<jarname>> <<appname>> --random-route --no-start`
5. `cf bind-service <<app-name>> <<service-name>>`
6. `cf start <<app-name>>`
7. `cf push -f manifest.yml`
8. Get Guid of the org

`cf org <<org name>> --guid`

9. Getting summary of organinzation
`cf curl /v2/organizations/<org id from step 8>/summary`
`cf curl /v2/organizations/f71a89ca-4b7f-4bce-94fd-fbc77c4dd23c/summary`
10. to get details of env variables in VCAP_SERVICES
`cf env <appname>`

11. to connect to my-sql database in cloudfoundry from local system
a. Push you app

`cf push bootcamp-customers`
b. Enable SSH for your app.
`cf enable-ssh bootcamp-customers`
c. Create Your Service Key
cf create-service-key <<DBname>> EXTERNAL-ACCESS-KEY
e.g `cf create-service-key bootcamp-customers-mysql EXTERNAL-ACCESS-KEY`
d. Retrieve your new service key using the cf-service-key
`cf service-key bootcamp-customers-mysql EXTERNAL-ACCESS-KEY`
e. Configure Your SSH Tunnel
`cf ssh -L 63306:us-cdbr-iron-east-01.mysql.net:3306 YOUR-HOST-APP

• Use any available local port for port forwarding. For example, 63306.
• Replace us-cdbr-iron-east-01.mysql.net with the address provided under hostname in the service key retrieved above.
• Replace 3306 with the port provided under port above.
• Replace YOUR-HOST-APP with the name of your host app.

f. Access Your Service Instance

• Open another tab of commandline

`mysql -u b5136e448be920 -h 0 -p -D ad_b2fca6t49704585d -P 63306`

• Replace b5136e448be920 with the username provided under username in your service key.
• -h 0 instructs mysql to connect to your local machine (use -h 127.0.0.1 for Windows).
• -p instructs mysql to prompt for a password. When prompted, use the password provided under password in your service key.
• Replace ad_b2fca6t49704585d with the database name provided under name in your service key.
• -P 63306 instructs mysql to connect on port 63306.

12. Access the data saved in mysql 
`https://<<route for the app>>/customers`

`


