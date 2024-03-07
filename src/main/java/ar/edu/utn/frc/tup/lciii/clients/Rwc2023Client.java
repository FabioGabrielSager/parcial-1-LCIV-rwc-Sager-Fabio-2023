package ar.edu.utn.frc.tup.lciii.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Slf4j
public class Rwc2023Client {

    @Autowired
    RestTemplate restTemplate;
    private final String baseResourceUrl = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/";
    private static final String RESILIENCE4J_INSTANCE_NAME = "RwcAPICircuit";
    private static final String FALLBACK_METHOD = "fallback";

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<MatchDto[]> getMatchesByPool(char pool){
        return restTemplate.getForEntity(baseResourceUrl + "matches?pool=" + pool, MatchDto[].class);
    }

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<TeamDto[]> getTeamById(Long id){
        return restTemplate.getForEntity(baseResourceUrl + "teams?id=" + id, TeamDto[].class);
    }

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<TeamDto[]> getTeamsByPool(char pool){
        return restTemplate.getForEntity(baseResourceUrl + "teams?pool=" + pool, TeamDto[].class);
    }

    public ResponseEntity<Object> fallback(Throwable throwable){
        log.info("Execution - FallBack Weather API - Exception Message: " +
                throwable.getMessage());
        return ResponseEntity.status(503).body("Response from Circuit Breaker Fallback of Weather API");
    }
}
