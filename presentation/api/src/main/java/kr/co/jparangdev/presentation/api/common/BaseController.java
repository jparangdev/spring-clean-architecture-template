package kr.co.jparangdev.presentation.api.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Base controller for REST endpoints
 */
@RestController
@RequestMapping("/api")
public class BaseController {

    /**
     * Health check endpoint
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}
