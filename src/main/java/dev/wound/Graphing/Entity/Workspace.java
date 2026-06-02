package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Document(collection = "workspaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    private String id;
    private String name;
    private Set<UUID> participants = new HashSet<>();
    private UUID graphId ;

    public void addParticipant(UUID participantId) {
        Objects.requireNonNull(participantId, "participantId must not be null");
        participants.add(participantId);
    }
}
