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
        EDGE_REMOVED
    }

    private String workspaceId;
    private Action action;
    private Object payload;
    private String timestamp;
}
