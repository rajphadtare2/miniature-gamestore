package minigameacle.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import minigameacle.user.dto.UserRequest;
import minigameacle.user.dto.UserResponse;
import minigameacle.user.model.User;
import minigameacle.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    final UserRepository userRepository;
    final ObjectMapper objectMapper;
    public UserResponse createUser(UserRequest userRequest){
        User savedUser = userRepository.save(mapToEntity(userRequest));
        return mapToDTO(savedUser);
    }

    private User mapToEntity(UserRequest userDTO) {
        return objectMapper.convertValue(userDTO, User.class);
    }

    private UserResponse mapToDTO(User userEntity) {
        return objectMapper.convertValue(userEntity, UserResponse.class);
    }
}
