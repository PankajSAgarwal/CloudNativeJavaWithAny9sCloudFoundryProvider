To connect an application(downstream-service) to a route-service for applications deployed in pivotal web services

#1. Create user provided route service, pointing it to the URL for our deployed route-service
`cf create-user-provided-service route-service -r https://my-route-service.cfapps.io`

for anynines cloudfoundry

`cf create-user-provided-service route-service -r https://route-service.de.a9sapp.eu`

#2. Bind that route service to any application whose route is http://my-downstream-service.cfapps.io

`cf bind-route-service cfapps.io route-service -hostanme my-downstream-service`

for anynines cloudfoundry

`cf bind-route-service de.a9sapp.eu route-service -hostanme downstream-service`

#3 `cf logs --recent route-service`
