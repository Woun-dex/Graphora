package dev.wound.Graphing.Service;

import dev.wound.Graphing.Entity.Workspace;
import dev.wound.Graphing.Repository.Users.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepo;

    public Workspace createWorkspace(String name) {
        Workspace workspace = new Workspace();
        workspace.setId(UUID.randomUUID().toString());
        workspace.setName(name);
        workspace.setParticipants(new HashSet<>());
        workspace.setGraphId(UUID.randomUUID());
        return workspaceRepo.save(workspace);
    }

    public Workspace createWorkspace(Workspace workspace) {
        if (workspace.getId() == null) {
            workspace.setId(UUID.randomUUID().toString());
        }
        if (workspace.getParticipants() == null) {
            workspace.setParticipants(new HashSet<>());
        }
        if (workspace.getGraphId() == null) {
            workspace.setGraphId(UUID.randomUUID());
        }
        return workspaceRepo.save(workspace);
    }

    public Optional<Workspace> getWorkspaceById(String id) {
        return workspaceRepo.findById(id);
    }

    public List<Workspace> getAllWorkspaces() {
        return workspaceRepo.findAll();
    }

    public Workspace addParticipant(String workspaceId, UUID participantId) {
        Workspace workspace = workspaceRepo.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with ID: " + workspaceId));
        workspace.addParticipant(participantId);
        return workspaceRepo.save(workspace);
    }

    public Workspace removeParticipant(String workspaceId, UUID participantId) {
        Workspace workspace = workspaceRepo.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with ID: " + workspaceId));
        if (workspace.getParticipants() != null) {
            workspace.getParticipants().remove(participantId);
        }
        return workspaceRepo.save(workspace);
    }

    public List<Workspace> getWorkspacesForUser(UUID userId) {
        return workspaceRepo.findByParticipantsContains(userId);
    }

    public Workspace updateWorkspaceName(String id, String name) {
        Workspace workspace = workspaceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with ID: " + id));
        workspace.setName(name);
        return workspaceRepo.save(workspace);
    }

    public void deleteWorkspace(String id) {
        workspaceRepo.deleteById(id);
    }
}
