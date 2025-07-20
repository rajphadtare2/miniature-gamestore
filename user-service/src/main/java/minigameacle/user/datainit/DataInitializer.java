package minigameacle.user.datainit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.user.model.User;
import minigameacle.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        log.info("✔ Inserting user data if not already present.");

        List<User> users = List.of(
                User.builder().email("smiti@gmail.com").name("Smiti").build(),
                User.builder().email("raj@gmail.com").name("Raj").build(),
                User.builder().email("goopy@gmail.com").name("Goopi").build()
        );

        for (User user : users) {
            if (!userRepository.existsById(user.getEmail())) {
                userRepository.save(user);
            }
        }

        log.info("✔ User data inserted if not already present.");
    }
}
