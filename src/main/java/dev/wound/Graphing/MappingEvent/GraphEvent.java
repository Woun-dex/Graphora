package dev.wound.Graphing.MappingEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphEvent {
    public enum Action {
        NODE_CREATED,
        NODE_UPDATED,
        NODE_DELETED,
        EDGE_ADDED,
        EDGE_REMOVED,
        SIMULATION_CLEARED
    }

    private String workspaceId;
    private String senderId;
    private Action action;
    private Object payload;
    private String timestamp;
}
