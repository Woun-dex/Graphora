package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edge {

    private UUID id;
    private UUID fromNodeId;
    private UUID toNodeId;
    private RelationshipType relationshipType;
    private Float weight;
    private Float riskFactor;
}
