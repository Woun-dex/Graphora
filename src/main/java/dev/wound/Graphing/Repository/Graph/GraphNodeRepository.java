package dev.wound.Graphing.Repository.Graph;

import dev.wound.Graphing.Entity.GraphNode;
import dev.wound.Graphing.Entity.NodeType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GraphNodeRepository extends Neo4jRepository<GraphNode, UUID> {

    Optional<GraphNode> findByName(String name);

    List<GraphNode> findByType(NodeType type);

    List<GraphNode> findByGraphKey(UUID graphKey);

    @Query("MATCH (dependent:Entity)-[:DEPENDS_ON]->(node:Entity) WHERE node.id = $nodeId RETURN dependent")
    List<GraphNode> findDependentsOfNode(UUID nodeId);

    @Query("MATCH (node:Entity)-[:DEPENDS_ON]->(dependency:Entity) WHERE node.id = $nodeId RETURN dependency")
    List<GraphNode> findDependenciesOfNode(UUID nodeId);

    @Query("MATCH (node:Entity) WHERE node.graphKey = $graphKey AND node.type = $type RETURN node")
    List<GraphNode> findByGraphKeyAndType(UUID graphKey, NodeType type);

    @Query("MATCH (node:Entity) WHERE node.graphKey = $graphKey AND NOT (node)-[:DEPENDS_ON]-() RETURN node")
    List<GraphNode> findOrphanNodes(UUID graphKey);

}
