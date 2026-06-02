package dev.wound.Graphing.Service;

import dev.wound.Graphing.Entity.GraphEdge;
import dev.wound.Graphing.Entity.GraphNode;
import dev.wound.Graphing.Entity.NodeType;
import dev.wound.Graphing.Entity.RelationshipType;
import dev.wound.Graphing.Repository.Graph.GraphNodeRepository;
import dev.wound.Graphing.Repository.Graph.GraphQueryRepository;
import dev.wound.Graphing.Repository.Graph.ImpactProjection;
import lombok.RequiredArgsConstructor;
import org.neo4j.bolt.connection.values.Path;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final GraphNodeRepository nodeRepo;
    private final GraphQueryRepository queryRepo;

    // Node Operations
    public GraphNode createNode(GraphNode node) {
        if (node.getId() == null) {
            node.setId(UUID.randomUUID());
        }
        if (node.getDependencies() == null) {
            node.setDependencies(new ArrayList<>());
        }
        return nodeRepo.save(node);
    }

    public GraphNode createNode(String name, NodeType type, UUID graphKey) {
        GraphNode node = new GraphNode();
        node.setId(UUID.randomUUID());
        node.setName(name);
        node.setType(type);
        node.setGraphKey(graphKey);
        node.setDependencies(new ArrayList<>());
        return nodeRepo.save(node);
    }

    public Optional<GraphNode> getNodeById(UUID id) {
        return nodeRepo.findById(id);
    }

    public Optional<GraphNode> getNodeByName(String name) {
        return nodeRepo.findByName(name);
    }

    public List<GraphNode> getNodesByGraph(UUID graphKey) {
        return nodeRepo.findByGraphKey(graphKey);
    }

    public GraphNode updateNode(UUID id, String name, NodeType type) {
        GraphNode node = nodeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Node not found with ID: " + id));
        node.setName(name);
        node.setType(type);
        return nodeRepo.save(node);
    }

    public void deleteNode(UUID id) {
        nodeRepo.deleteById(id);
    }

    // Edge Operations
    public GraphNode addEdge(UUID fromNodeId, UUID toNodeId, RelationshipType type, Float weight, Float riskFactor) {
        GraphNode fromNode = nodeRepo.findById(fromNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Source node not found with ID: " + fromNodeId));
        GraphNode toNode = nodeRepo.findById(toNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Target node not found with ID: " + toNodeId));

        if (fromNode.getDependencies() == null) {
            fromNode.setDependencies(new ArrayList<>());
        }

        // Avoid adding duplicate edges to the same node
        boolean edgeExists = fromNode.getDependencies().stream()
                .anyMatch(edge -> edge.getTarget() != null && edge.getTarget().getId().equals(toNodeId));

        if (!edgeExists) {
            GraphEdge newEdge = new GraphEdge(
                    null,                  // Database generated ID
                    UUID.randomUUID(),     // Domain edgeId
                    fromNodeId,
                    toNodeId,
                    type,
                    weight,
                    riskFactor,
                    toNode
            );
            fromNode.getDependencies().add(newEdge);
        }

        return nodeRepo.save(fromNode);
    }

    public GraphNode removeEdge(UUID fromNodeId, UUID toNodeId) {
        GraphNode fromNode = nodeRepo.findById(fromNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Source node not found with ID: " + fromNodeId));

        if (fromNode.getDependencies() != null) {
            fromNode.getDependencies().removeIf(edge -> 
                    edge.getTarget() != null && edge.getTarget().getId().equals(toNodeId));
        }

        return nodeRepo.save(fromNode);
    }

    // Advanced Graph Queries
    public List<Path> findImpactPaths(String nodeName) {
        return queryRepo.findImpactPaths(nodeName);
    }

    public List<ImpactProjection> calculateImpact(String nodeName) {
        return queryRepo.calculateImpact(nodeName);
    }

    // Additional Custom Queries
    public List<GraphNode> getDependents(UUID nodeId) {
        return nodeRepo.findDependentsOfNode(nodeId);
    }

    public List<GraphNode> getDependencies(UUID nodeId) {
        return nodeRepo.findDependenciesOfNode(nodeId);
    }

    public List<GraphNode> getNodesByGraphAndType(UUID graphKey, NodeType type) {
        return nodeRepo.findByGraphKeyAndType(graphKey, type);
    }

    public List<GraphNode> getOrphanNodes(UUID graphKey) {
        return nodeRepo.findOrphanNodes(graphKey);
    }
}
