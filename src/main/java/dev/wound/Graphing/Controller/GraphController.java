package dev.wound.Graphing.Controller;

import dev.wound.Graphing.Entity.GraphNode;
import dev.wound.Graphing.Entity.NodeType;
import dev.wound.Graphing.Entity.RelationshipType;
import dev.wound.Graphing.Repository.Graph.ImpactProjection;
import dev.wound.Graphing.Service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GraphController {

    private final GraphService graphService;

    @QueryMapping
    public GraphNode getNodeById(@Argument UUID id) {
        return graphService.getNodeById(id).orElse(null);
    }

    @QueryMapping
    public GraphNode getNodeByName(@Argument String name) {
        return graphService.getNodeByName(name).orElse(null);
    }

    @QueryMapping
    public List<GraphNode> getNodesByGraph(@Argument UUID graphKey) {
        return graphService.getNodesByGraph(graphKey);
    }

    @QueryMapping
    public List<GraphNode> getDependents(@Argument UUID nodeId) {
        return graphService.getDependents(nodeId);
    }

    @QueryMapping
    public List<GraphNode> getDependencies(@Argument UUID nodeId) {
        return graphService.getDependencies(nodeId);
    }

    @QueryMapping
    public List<GraphNode> getNodesByGraphAndType(@Argument UUID graphKey, @Argument NodeType type) {
        return graphService.getNodesByGraphAndType(graphKey, type);
    }

    @QueryMapping
    public List<GraphNode> getOrphanNodes(@Argument UUID graphKey) {
        return graphService.getOrphanNodes(graphKey);
    }

    @QueryMapping
    public List<ImpactProjection> calculateImpact(@Argument String nodeName) {
        return graphService.calculateImpact(nodeName);
    }

    @MutationMapping
    public GraphNode createNode(@Argument String name, @Argument NodeType type, @Argument UUID graphKey) {
        return graphService.createNode(name, type, graphKey);
    }

    @MutationMapping
    public GraphNode updateNode(@Argument UUID id, @Argument String name, @Argument NodeType type) {
        return graphService.updateNode(id, name, type);
    }

    @MutationMapping
    public boolean deleteNode(@Argument UUID id) {
        graphService.deleteNode(id);
        return true;
    }

    @MutationMapping
    public GraphNode addEdge(
            @Argument UUID fromNodeId,
            @Argument UUID toNodeId,
            @Argument RelationshipType type,
            @Argument Float weight,
            @Argument Float riskFactor
    ) {
        return graphService.addEdge(fromNodeId, toNodeId, type, weight, riskFactor);
    }

    @MutationMapping
    public GraphNode removeEdge(@Argument UUID fromNodeId, @Argument UUID toNodeId) {
        return graphService.removeEdge(fromNodeId, toNodeId);
    }
}
