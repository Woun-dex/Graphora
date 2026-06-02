package dev.wound.Graphing.Repository.Users;

import dev.wound.Graphing.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User , UUID> {

    Optional<User> findByName(String name);
}
