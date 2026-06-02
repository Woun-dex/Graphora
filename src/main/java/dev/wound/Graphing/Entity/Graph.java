package dev.wound.Graphing.Entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
public class Graph {

    private String id;
    private String title;
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    public void addNode(Node node) {
        Objects.requireNonNull(node, "node must not be null");
        if (containsNode(node.getId())) {
            throw new IllegalArgumentException("Node already exists in graph: " + node.getId());
        }
        nodes.add(node);
    }

    public boolean containsNode(UUID nodeId) {
        return nodes.stream().anyMatch(node -> node.getId().equals(nodeId));
    }

    public void addEdge(Edge edge) {
        Objects.requireNonNull(edge, "edge must not be null");
        if (!containsNode(edge.getFromNodeId()) || !containsNode(edge.getToNodeId())) {
            throw new IllegalStateException("Both endpoints must exist in the graph before adding an edge");
        }
        if (edges.stream().anyMatch(existing -> existing.getId().equals(edge.getId()))) {
            throw new IllegalArgumentException("Edge already exists in graph: " + edge.getId());
        }
        edges.add(edge);
    }

    public void removeNode(UUID nodeId) {
        nodes.removeIf(node -> node.getId().equals(nodeId));
        edges.removeIf(edge -> edge.getFromNodeId().equals(nodeId) || edge.getToNodeId().equals(nodeId));
    }
}
