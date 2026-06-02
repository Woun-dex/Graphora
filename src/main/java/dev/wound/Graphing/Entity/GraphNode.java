package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("Entity")
public class GraphNode {

    @Id
    private UUID id;
    private String name;
    private NodeType type;
    private UUID graphKey;

    @Relationship(type = "DEPENDS_ON")
    private List<GraphEdge> dependencies;
}
