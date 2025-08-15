package minigameacle.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.user.dto.GamesRequest;
import minigameacle.user.dto.UserRequest;
import minigameacle.user.dto.UserResponse;
import minigameacle.user.model.User;
import minigameacle.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    final UserRepository userRepository;
    final ObjectMapper objectMapper;

    public UserResponse createUser(UserRequest userRequest){
        User savedUser = userRepository.save(mapToEntity(userRequest));
        log.info("Saved User: {} ", userRequest.getEmail());
        return mapToDTO(savedUser);
    }

    public UserResponse getUserByEmail(String email) {
        return mapToDTO(getUser(email));
    }

    public UserResponse saveGamesToUser(GamesRequest gamesRequest) {
        User user = getUser(gamesRequest.getEmail());
        Map<String, String> ownedGames = user.getGames() != null ? new HashMap<>(user.getGames()) : new HashMap<>();
        Map<String, String> requestedGames = gamesRequest.getGames() != null ? gamesRequest.getGames() : new HashMap<>();

        // Find common keys (games already owned)
        Set<String> commonGames = new HashSet<>(ownedGames.keySet());
        commonGames.retainAll(requestedGames.keySet());

        if (!commonGames.isEmpty()) {
            throw new RuntimeException("User with Email " + gamesRequest.getEmail() + " already owns " + commonGames);
        }
        // Add new games
        ownedGames.putAll(requestedGames);
        log.info("Saving games: {} ", ownedGames);
        // Set updated games and save
        user.setGames(ownedGames);
        return mapToDTO(userRepository.save(user));
    }

    private User getUser(String email){
        return userRepository.findById(email).orElseThrow(() ->
                new RuntimeException("User with Email "+ email +"not found."));
    }

    private User mapToEntity(UserRequest userDTO) {
        return objectMapper.convertValue(userDTO, User.class);
    }

    private UserResponse mapToDTO(User userEntity) {
        return objectMapper.convertValue(userEntity, UserResponse.class);
    }
}
