package dev.wound.Graphing.Repository.Graph;

import dev.wound.Graphing.Entity.Edge;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;

public interface EdgesRepository extends Neo4jRepository<Edge, UUID> {
}
