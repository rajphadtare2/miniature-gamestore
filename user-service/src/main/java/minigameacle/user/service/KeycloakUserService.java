package minigameacle.user.service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class KeycloakUserService {

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
}

