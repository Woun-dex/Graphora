package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class GraphEdge {

    @RelationshipId
    private Long id;

    private UUID edgeId;
    private UUID fromNodeId;
    private UUID toNodeId;
    private RelationshipType relationshipType;
    private Float weight;
    private Float riskFactor;

    @TargetNode
    private GraphNode target;
}
