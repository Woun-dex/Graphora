package dev.wound.Graphing.Repository.Graph;

import dev.wound.Graphing.Entity.Graph;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GraphRepository extends MongoRepository<Graph, Long> {
}
