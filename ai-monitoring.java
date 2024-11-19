// AIMonitoringConfig.java
package com.IdiomasBR.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.aop.TimedAspect;

@Configuration
public class AIMonitoringConfig {
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}

// AIMetrics.java
package com.IdiomasBR.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIMetrics {
    private final MeterRegistry registry;
    private final Counter aiRequestsTotal;
    private final Counter aiErrorsTotal;
    
    public AIMetrics(MeterRegistry registry) {
        this.registry = registry;
        this.aiRequestsTotal = Counter.builder("ai_requests_total")
            .description("Total AI API requests")
            .register(registry);
        this.aiErrorsTotal = Counter.builder("ai_errors_total")
            .description("Total AI API errors")
            .register(registry);
    }
    
    public void incrementRequests() {
        aiRequestsTotal.increment();
    }
    
    public void incrementErrors() {
        aiErrorsTotal.increment();
    }
}

// Updated AITeachingService.java
package com.IdiomasBR.service;

import com.IdiomasBR.monitoring.AIMetrics;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AITeachingService {
    private final AIMetrics metrics;
    
    @Timed(value = "ai.request.time", description = "Time taken for AI request")
    private String callOpenAI(String prompt) {
        log.info("Making AI request with prompt length: {}", prompt.length());
        metrics.incrementRequests();
        
        try {
            String response = // previous OpenAI call implementation
            log.info("AI request successful, response length: {}", response.length());
            return response;
        } catch (Exception e) {
            metrics.incrementErrors();
            log.error("AI request failed: {}", e.getMessage(), e);
            throw new AIException("Error calling OpenAI API", e);
        }
    }
}

// application.properties additions
management.endpoints.web.exposure.include=health,metrics,prometheus
management.endpoint.health.show-details=always
