package com.joshiai.emailwriter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshiai.emailwriter.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {


    private final WebClient webClient;

    @Value("${gemini.url}")
    private String geminiUrl;

    @Value("${gemini.apiKey}")
    private String geminiApiKey;


    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateEmail(EmailRequest emailRequest) {
        // Prompt
        String prompt = buildPrompt(emailRequest);
        // request part
        Map<String, Object> reqBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );
        // Req and Res

        String response = webClient.post()
                .uri(geminiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // extract and return response
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts").get(0).path("text").asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a email reply based on the following information and please don't generate a subject line:\n");

        if (emailRequest.getTone() != null) {
            prompt.append("Use a Tone : ").append(emailRequest.getTone()).append("\n");
        }
        prompt.append("Original Email : ").append(emailRequest.getEmailContent()).append("\n");
        return prompt.toString();
    }
}
