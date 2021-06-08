Run one instance of one of the producer nodes (whichever one) 
and run three instances of the consumer. 

Visit `http://localhost:8080/hi/World` and observe in the consumers’ logs that all three have the message sent to the broadcast channel; one (although there’s no telling which, so check all of the consoles) will have the message sent to the direct channel. Raise the stakes by killing all of the consumer nodes, then visiting `http://localhost:8080/hi/Again` . 

All the consumers are down, but we specified that the point-to-point connection is durable, so as soon as you restart one of the consumers you’ll see the direct message arrive and be logged to the console. 
                                                                                                                                                                                    
                                                                                                                                                                                

