package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${AI_API_URL:https://api.cerebras.ai/v1/chat/completions}")
    private String apiUrl;

    @Value("${AI_API_KEY:}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AIService() {
        this.restTemplate = new RestTemplate();
    }

    public String getAIResponse(String userMessage) {
        if (apiKey == null || apiKey.isBlank()) {
            return "AI_API_KEY is not configured";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-4-scout-17b-16e-instruct");
        body.put("stream", false);
        body.put("temperature", 0);
        body.put("max_tokens", -1);
        body.put("seed", 0);
        body.put("top_p", 1);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);
        body.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
                return contentNode.asText();
            } else {
                return "Error: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
}
