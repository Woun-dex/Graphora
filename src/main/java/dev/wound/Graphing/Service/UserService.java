package dev.wound.Graphing.Service;

import dev.wound.Graphing.Entity.User;
import dev.wound.Graphing.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public User createUser(String name) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        return userRepo.save(user);
    }

    public User createUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        return userRepo.save(user);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepo.findById(id);
    }

    public Optional<User> getUserByName(String name) {
        return userRepo.findByName(name);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User updateUser(UUID id, String name) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setName(name);
        return userRepo.save(user);
    }

    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }
}
