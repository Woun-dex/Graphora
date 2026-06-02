package dev.wound.Graphing.Repository.Graph;

import dev.wound.Graphing.Entity.GraphNode;
import org.neo4j.bolt.connection.values.Path;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.UUID;

public interface GraphQueryRepository extends Neo4jRepository<GraphNode, UUID> {

    @Query("""
            MATCH path = (n:Entity {name:$node})-[:DEPENDS_ON*1..4]->(x)
            RETURN path
            """)
    List<Path> findImpactPaths(String node);

    @Query("""
            MATCH path = (n:Entity {name:$node})-[:DEPENDS_ON*1..4]->(x)
            WITH x,
                 reduce(score = 1.0, rel IN relationships(path) | score * rel.weight) AS impact
            RETURN x.name AS name, MAX(impact) AS impactScore
            ORDER BY impactScore DESC
            """)
    List<ImpactProjection> calculateImpact(String node);

    @Query("""
            CALL gds.betweenness.stream({
            nodeProjection: '*',
            relationshipProjection: {
                DEPENDS_ON: {type:'DEPENDS_ON', properties:'weight'}
            }
            })
            YIELD nodeId, score
            RETURN gds.util.asNode(nodeId).name, score
            ORDER BY score DESC
            """)
    List<CentralNode> findBottlenecks();

    @Query("""
            MATCH (node:Entity)
            WHERE node.graphKey = $graphKey
            OPTIONAL MATCH (node)-[r:DEPENDS_ON]-()
            WITH node, count(r) AS degree
            RETURN node.name AS name, toFloat(degree) AS score
            ORDER BY score DESC
            LIMIT 5
            """)
    List<CentralNode> findBottlenecksInGraph(UUID graphKey);

}
