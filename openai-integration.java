// OpenAIConfig.java
package com.IdiomasBR.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class OpenAIConfig {
    @Bean
    public RestTemplate openAIRestTemplate(RestTemplateBuilder builder) {
        return builder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}

// OpenAIRequest.java
package com.IdiomasBR.dto;

import lombok.Data;
import java.util.List;

@Data
public class OpenAIRequest {
    private String model = "gpt-4";
    private List<Message> messages;
    private double temperature = 0.7;
    
    @Data
    public static class Message {
        private String role;
        private String content;
    }
}

// OpenAIResponse.java
package com.IdiomasBR.dto;

import lombok.Data;
import java.util.List;

@Data
public class OpenAIResponse {
    private List<Choice> choices;
    
    @Data
    public static class Choice {
        private Message message;
    }
    
    @Data
    public static class Message {
        private String content;
    }
}

// AIException.java
package com.IdiomasBR.exception;

public class AIException extends RuntimeException {
    public AIException(String message) {
        super(message);
    }
    
    public AIException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Updated AITeachingService.java
package com.IdiomasBR.service;

import com.IdiomasBR.dto.OpenAIRequest;
import com.IdiomasBR.dto.OpenAIResponse;
import com.IdiomasBR.exception.AIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import java.util.List;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AITeachingService {
    private final RestTemplate restTemplate;
    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    
    @Value("${openai.api.key}")
    private String apiKey;

    @Retryable(value = {AIException.class}, maxAttempts = 3)
    private String callOpenAI(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            OpenAIRequest request = new OpenAIRequest();
            OpenAIRequest.Message message = new OpenAIRequest.Message();
            message.setRole("user");
            message.setContent(prompt);
            request.setMessages(Collections.singletonList(message));

            HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                OpenAIResponse.class
            );

            if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            }
            
            throw new AIException("No response from AI service");
            
        } catch (Exception e) {
            throw new AIException("Error calling OpenAI API", e);
        }
    }
}
