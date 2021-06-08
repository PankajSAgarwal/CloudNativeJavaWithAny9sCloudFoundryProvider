1. To have launchd start rabbitmq now and restart at login:
  `brew services start rabbitmq`
    
    Or, 

if you don't want/need a background service you can just run:
  `rabbitmq-server`

2. To have launchd start kafka now and restart at login:
        `brew services start kafka`
        
      Or, if you don't want/need a background service you can just run:
    
`zookeeper-server-start -daemon /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties`