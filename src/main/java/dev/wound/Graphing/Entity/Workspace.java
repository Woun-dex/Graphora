package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    private String id;
    private String name;
    private Set<UUID> participants = new HashSet<>();

    public void addParticipant(UUID participantId) {
        Objects.requireNonNull(participantId, "participantId must not be null");
        participants.add(participantId);
    }
}
