package br.social.impacthub.infrastructure.web;

import br.social.impacthub.infrastructure.web.docs.HealthControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController implements HealthControllerDocs {
    @GetMapping
    public ResponseEntity<Map<String, String>> check() {
        return ResponseEntity.ok(
                Map.of(
                        "status", "UP",
                        "timestamp", Instant.now().toString()
                )
        );
    }
}
