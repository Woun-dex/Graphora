package dev.wound.Graphing.Controller;

import dev.wound.Graphing.Entity.User;
import dev.wound.Graphing.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @QueryMapping
    public User getUserById(@Argument UUID id) {
        return userService.getUserById(id).orElse(null);
    }

    @QueryMapping
    public User getUserByName(@Argument String name) {
        return userService.getUserByName(name).orElse(null);
    }

    @QueryMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @MutationMapping
    public User createUser(@Argument String name) {
        return userService.createUser(name);
    }

    @MutationMapping
    public User updateUser(@Argument UUID id, @Argument String name) {
        return userService.updateUser(id, name);
    }

    @MutationMapping
    public boolean deleteUser(@Argument UUID id) {
        userService.deleteUser(id);
        return true;
    }
}
