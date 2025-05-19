package com.musinsa.test.controller.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "API Server Health Check")
public class HealthCheckController {
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "ok")
    )
    @GetMapping("/health_check")
    public String healthCheck() {
        return "Service is up and running!";
    }
}
