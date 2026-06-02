package dev.wound.Graphing.Repository.Graph;

import dev.wound.Graphing.Entity.Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;

public interface NodeRepository extends Neo4jRepository<Node, UUID> {
}
