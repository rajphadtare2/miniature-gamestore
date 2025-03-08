package minigameacle.order.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // GET Request
    public JsonNode sendGetRequest(String url, Map<String, String> queryParams) throws Exception {
        StringBuilder queryParamString = new StringBuilder();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (!queryParamString.isEmpty()) {
                queryParamString.append("&");
            }
            var entryValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            queryParamString.append(entry.getKey()).append("=").append(entryValue);
        }

        String finalUrl = url + "?" + queryParamString;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(finalUrl))
                .GET()
                .build();
        log.info("GET Final URL: {}", finalUrl);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("GET Request : {}", response.body());
        if (response.statusCode() == 200) {
            return objectMapper.readTree(response.body());
        } else {
            throw new RuntimeException("GET Request Failed. HTTP Status Code: " + response.statusCode() + " Message: " + response.body());
        }
    }

    // POST Request with JSON Body
    public JsonNode sendPostRequest(String url, Map<String, Object> requestBody) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("POST Request: {}", response.body());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readTree(response.body());
        } else {
            throw new RuntimeException("POST Request Failed. HTTP Status Code: " + response.statusCode() + " Message: " + response.body());
        }
    }

    // PUT Request with JSON Body
    public void sendPutRequest(String url, Map<String, Object> requestBody) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("PUT Request: {}", response.body());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            log.info("PUT Request SUCCESS: {}", response.body());
        } else {
            throw new RuntimeException("PUT Request Failed. HTTP Status Code: " + response.statusCode() + " Message: " + response.body());
        }
    }
}
