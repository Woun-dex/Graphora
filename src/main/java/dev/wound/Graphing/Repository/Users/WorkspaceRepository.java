package dev.wound.Graphing.Repository.Users;

import dev.wound.Graphing.Entity.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {

    List<Workspace> findByParticipantsContains(UUID participantId);

}
