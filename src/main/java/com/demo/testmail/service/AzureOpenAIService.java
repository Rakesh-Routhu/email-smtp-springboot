package com.demo.testmail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AzureOpenAIService {

    @Value("${azure.openai.api-key}")
    private String apiKey;

    @Value("${azure.openai.endpoint}")
    private String apiEndpoint;

    @Value("${azure.openai.deployment-name}")
    private String deploymentName;

    @Value("${azure.openai.api-version}")
    private String apiVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateText(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        // Construct the request body
        String requestBody = String.format(
                "{\"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"temperature\": 0}",
                prompt
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Build the URL with deployment name and API version
        String url = String.format("%s/openai/deployments/%s/chat/completions?api-version=%s",
                apiEndpoint, deploymentName, apiVersion);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            // Extract and return the generated text
            String generatedText = jsonResponse.path("choices").get(0).path("message").path("content").asText();

            return generatedText;
        } catch (HttpClientErrorException.Unauthorized e) {
            return "Error: Unauthorized. Please check your API key and permissions.";
        } catch (Exception e) {
            return "Error: Unable to generate text";
        }
    }
    
    
    public String translateText(String content, String targetLang) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        // Construct the request body for translation
        String prompt = String.format("Translate the following text to %s: %s", targetLang, content);
        String requestBody = String.format(
                "{\"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"temperature\": 0}",
                prompt
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Build the URL with deployment name and API version
        String url = String.format("%s/openai/deployments/%s/chat/completions?api-version=%s",
                apiEndpoint, deploymentName, apiVersion);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            // Extract and return the translated text
            String translatedText = jsonResponse.path("choices").get(0).path("message").path("content").asText();

            return translatedText;
        } catch (HttpClientErrorException.Unauthorized e) {
            return "Error: Unauthorized. Please check your API key and permissions.";
        } catch (Exception e) {
            return "Error: Unable to translate text";
        }
    }
}
