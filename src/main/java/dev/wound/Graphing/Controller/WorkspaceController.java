package dev.wound.Graphing.Controller;

import dev.wound.Graphing.Entity.Workspace;
import dev.wound.Graphing.Service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @QueryMapping
    public Workspace getWorkspaceById(@Argument String id) {
        return workspaceService.getWorkspaceById(id).orElse(null);
    }

    @QueryMapping
    public List<Workspace> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @QueryMapping
    public List<Workspace> getWorkspacesForUser(@Argument UUID userId) {
        return workspaceService.getWorkspacesForUser(userId);
    }

    @MutationMapping
    public Workspace createWorkspace(@Argument String name) {
        return workspaceService.createWorkspace(name);
    }

    @MutationMapping
    public Workspace updateWorkspaceName(@Argument String id, @Argument String name) {
        return workspaceService.updateWorkspaceName(id, name);
    }

    @MutationMapping
    public boolean deleteWorkspace(@Argument String id) {
        workspaceService.deleteWorkspace(id);
        return true;
    }

    @MutationMapping
    public Workspace addParticipant(@Argument String workspaceId, @Argument UUID participantId) {
        return workspaceService.addParticipant(workspaceId, participantId);
    }

    @MutationMapping
    public Workspace removeParticipant(@Argument String workspaceId, @Argument UUID participantId) {
        return workspaceService.removeParticipant(workspaceId, participantId);
    }
}
