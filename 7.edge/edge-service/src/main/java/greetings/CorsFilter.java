package greetings;

import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Profile("cors")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class CorsFilter implements Filter {
    private final Log log = LogFactory.getLog(getClass());
    private final Map<String, List<ServiceInstance>> catalog
            = new ConcurrentHashMap<>();

    private final DiscoveryClient discoveryClient;
    //1
    @Autowired
    public CorsFilter(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.refreshCatalog();
    }


    private void refreshCatalog() {
        discoveryClient.getServices().forEach(
                svc->this.catalog
                        .put(svc,
                                this.discoveryClient.getInstances(svc)));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    //2
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String originHeaderValue = originFor(request);
        boolean clientAllowed = isClientAllowed(originHeaderValue);


    }
    //3
    private boolean isClientAllowed(String origin) {
        if(StringUtils.hasText(origin)){
            URI originUri = URI.create(origin);
            int port = originUri.getPort();
            String match = originUri.getHost() + ":" + (port < 0 ? 80 : port);
            this.catalog.forEach((k,v)->{
                String collect = v.stream()
                        .map(si -> si.getHost() + ":" + si.getPort()+ "(" + si.getServiceId() + ")")
                        .collect(Collectors.joining());
            });

            boolean svcMatch = this.catalog.keySet().stream().anyMatch(
                    serviceId->this.catalog.get(serviceId).stream().map(si->si.getHost() + ":" + si.getPort())
                    .anyMatch(hp->hp.equalsIgnoreCase(match)));
            return svcMatch;

        }
        return false;
    }
    //4
    @EventListener(HeartbeatEvent.class)
    public void onHeartbeatEvent(HeartbeatEvent e){
        this.refreshCatalog();
    }

    private String originFor(HttpServletRequest request) {
        return StringUtils.hasText(
                request.getHeader(HttpHeaders.ORIGIN))
                ? request.getHeader(HttpHeaders.ORIGIN)
                :request.getHeader(HttpHeaders.REFERER);
    }
}
