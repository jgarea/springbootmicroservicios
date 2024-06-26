package daw.gateway.gateway.filter;


import daw.gateway.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
//                try {
////                    //REST call to AUTH service
////                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
//                    template.getForObject("http://localhost/auth/validateToken?token=" + authHeader, String.class);
//                    System.out.println(authHeader);
//                    System.out.println(template);
//                    //jwtUtil.validateToken(authHeader);
//
//                } catch (Exception e) {
//                    System.out.println("invalid access...!");
//                    System.out.println(e);
//                    throw new RuntimeException("un authorized access to application");
//                }
                try {
                    // REST call to AUTH service
                    ResponseEntity<String> response = template.getForEntity("http://localhost:8085/auth/validateToken?token=" + authHeader, String.class);
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        //throw new RuntimeException("Unauthorized access to application");
                        return handleUnauthorized(exchange);
                    }
                } catch (HttpClientErrorException e) {
                    System.out.println("Invalid access: " + e.getStatusCode());
                    //throw new RuntimeException("Unauthorized access to application");
                    return handleUnauthorized(exchange);
                } catch (Exception e) {
                    System.out.println("Invalid access: " + e.getMessage());
                    //throw new RuntimeException("Unauthorized access to application");
                    return handleUnauthorized(exchange);
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    public static class Config {

    }
}