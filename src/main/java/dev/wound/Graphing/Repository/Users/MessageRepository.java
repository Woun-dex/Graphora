package dev.wound.Graphing.Repository.Users;

import dev.wound.Graphing.Entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message,Long> {
}
