package dev.wound.Graphing.Repository.Users;

import dev.wound.Graphing.Entity.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
}
