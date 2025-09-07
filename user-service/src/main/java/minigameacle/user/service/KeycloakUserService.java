package minigameacle.user.service;

import lombok.extern.slf4j.Slf4j;
import minigameacle.user.dto.LoginDTO;
import minigameacle.user.dto.UserDTO;
import minigameacle.user.dto.UserRequest;
import minigameacle.user.dto.UserResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class KeycloakUserService {

    private final String serverUrl;
    private final Keycloak keycloak;
    private final String realm;
    private final UserService userService;

    public KeycloakUserService(
            @Value("${keycloak.server.url}") String serverUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.admin.username}") String adminUsername,
            @Value("${keycloak.admin.password}") String adminPassword,
            UserService userService) {

        this.userService = userService;
        this.realm = realm;
        this.serverUrl = serverUrl;
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .username(adminUsername)
                .password(adminPassword)
                .clientId("admin-cli")
                .build();
    }

    public UserResponse createUser(UserDTO userDto) {
        log.info("registering user");
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getName());
        user.setEnabled(true);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(userDto.getPassword());

        user.setCredentials(Collections.singletonList(cred));

        try (Response response = keycloak.realm(realm)
                .users()
                .create(user)) {

            if (response.getStatus() == 201) {
                UserRequest userRequest = new UserRequest();
                userRequest.setEmail(userDto.getEmail());
                userRequest.setName(userDto.getName());
                return userService.createUser(userRequest);
            } else {
                throw new RuntimeException("Error creating User");
            }
        }

    }

    public String login(LoginDTO loginDTO) throws IOException {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        String urlParameters =
                "grant_type=password" +
                        "&client_id=" + URLEncoder.encode(loginDTO.getClientId(), StandardCharsets.UTF_8) +
                        "&username=" + URLEncoder.encode(loginDTO.getEmail(), StandardCharsets.UTF_8) +
                        "&password=" + URLEncoder.encode(loginDTO.getPassword(), StandardCharsets.UTF_8);

        log.info("Hitting Keycloak with  URL: {} and Parameters: {}", tokenUrl, urlParameters);

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) new URL(tokenUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.getOutputStream().write(postData);

        int responseCode = conn.getResponseCode();
        InputStream stream;
        if (responseCode >= 200 && responseCode < 300) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}

