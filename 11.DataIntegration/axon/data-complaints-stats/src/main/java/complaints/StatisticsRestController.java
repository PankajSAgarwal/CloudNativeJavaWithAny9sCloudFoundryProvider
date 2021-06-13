package complaints;


import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//<1>
@ProcessingGroup("statistics")
@RestController
public class StatisticsRestController {

    private final ConcurrentMap<String, AtomicLong> statistics = new ConcurrentHashMap<>();

    //<2>
    @EventHandler
    public void on(ComplaintFiledEvent event){
        statistics.computeIfAbsent(event.getCompany(),k -> new AtomicLong()).incrementAndGet();
    }

    //<3>
    @GetMapping
    public ConcurrentMap<String,AtomicLong> showStatistics(){
        return statistics;
    }



}
