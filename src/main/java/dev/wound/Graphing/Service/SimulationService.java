package dev.wound.Graphing.Service;

import dev.wound.Graphing.Entity.GraphNode;
import dev.wound.Graphing.Entity.NodeType;
import dev.wound.Graphing.Entity.SimulationEventType;
import dev.wound.Graphing.MappingEvent.SimulationInput;
import dev.wound.Graphing.MappingEvent.SimulationResult;
import dev.wound.Graphing.Repository.Graph.CentralNode;
import dev.wound.Graphing.Repository.Graph.GraphNodeRepository;
import dev.wound.Graphing.Repository.Graph.GraphQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final GraphNodeRepository nodeRepo;
    private final GraphQueryRepository queryRepo;

    public SimulationResult runSimulation(SimulationInput input) {
        // Throw exception if the database is empty
        long nodeCount = nodeRepo.count();
        if (nodeCount == 0) {
            throw new IllegalArgumentException("No nodes found in the database. Simulation cannot be performed.");
        }

        // Dynamically find trigger node
        GraphNode triggerNode = null;
        if (input.getTriggerNode() != null) {
            triggerNode = nodeRepo.findById(input.getTriggerNode()).orElse(null);
        }

        // If trigger ID is not found or not provided, pick the first available node dynamically
        if (triggerNode == null) {
            List<GraphNode> allNodes = nodeRepo.findAll();
            if (!allNodes.isEmpty()) {
                triggerNode = allNodes.get(0);
            } else {
                throw new IllegalArgumentException("No nodes found in the database. Simulation cannot be performed.");
            }
        }

        String triggerName = triggerNode.getName();

        // 1. Calculate event parameters based on event type
        float baseMultiplier = getBaseMultiplier(input.getEventType());
        float decayFactor = getDecayFactor(input.getEventType());

        // BFS traversal downstream along dependents (in reverse direction of DEPENDS_ON)
        List<SimulationResult.AffectedNode> affectedList = new ArrayList<>();
        List<SimulationResult.CriticalPath> criticalPaths = new ArrayList<>();
        Set<String> affectedNames = new HashSet<>();

        // Traversal state helper
        class TraversalState {
            final GraphNode node;
            final List<SimulationResult.ExposureEdge> path;
            final float strength;
            final int depth;

            TraversalState(GraphNode node, List<SimulationResult.ExposureEdge> path, float strength, int depth) {
                this.node = node;
                this.path = path;
                this.strength = strength;
                this.depth = depth;
            }
        }

        Queue<TraversalState> queue = new LinkedList<>();
        queue.add(new TraversalState(triggerNode, new ArrayList<>(), 1.0f, 0));
        affectedNames.add(triggerName);

        float totalStrength = 0;
        int maxDepth = 0;

        while (!queue.isEmpty()) {
            TraversalState current = queue.poll();
            maxDepth = Math.max(maxDepth, current.depth);

            // Fetch incoming dependants (nodes that point to current node)
            List<GraphNode> dependents = nodeRepo.findDependentsOfNode(current.node.getId());

            for (GraphNode dep : dependents) {
                if (affectedNames.contains(dep.getName()) || current.depth >= 4) {
                    continue; // Prevent cycles & limit depth to 4
                }

                // Retrieve edge weight dynamically from the actual relationship
                float weight = 0.8f;
                if (dep.getDependencies() != null) {
                    weight = (float) dep.getDependencies().stream()
                            .filter(edge -> edge.getTarget() != null && edge.getTarget().getId().equals(current.node.getId()))
                            .mapToDouble(edge -> edge.getWeight() != null ? edge.getWeight() : 0.8)
                            .findFirst()
                            .orElse(0.8);
                }

                // Calculate downstream path strength dynamically
                float nextStrength = current.strength * weight * decayFactor;
                float impactScore = input.getSeverity() * nextStrength * baseMultiplier;

                // Cap impact score at 1.0
                impactScore = Math.min(impactScore, 1.0f);

                // Build exposure path dynamically from actual node names
                List<SimulationResult.ExposureEdge> newPath = new ArrayList<>(current.path);
                newPath.add(new SimulationResult.ExposureEdge(current.node.getName(), dep.getName(), weight));

                affectedNames.add(dep.getName());
                totalStrength += nextStrength;

                // Determine Risk Level dynamically based on impact score
                String riskLevel = classifyRiskLevel(impactScore);

                // Count alternative paths from actual dependencies
                int alternativePaths = (dep.getDependencies() != null) ? dep.getDependencies().size() : 0;

                // Calculate Mitigation Score dynamically
                float mitigationBase = getMitigationBase(input.getEventType());
                float mitigationScore = Math.min(mitigationBase + (alternativePaths * 0.12f), 0.95f);

                affectedList.add(new SimulationResult.AffectedNode(
                        dep.getName(),
                        mapNodeTypeToLabel(dep.getType()),
                        Math.round(impactScore * 100f) / 100f,
                        riskLevel,
                        newPath,
                        alternativePaths,
                        Math.round(mitigationScore * 100f) / 100f
                ));

                // Build critical paths dynamically from real traversal
                if (impactScore > 0.5f) {
                    List<String> pathNodes = new ArrayList<>();
                    pathNodes.add(triggerName);
                    for (SimulationResult.ExposureEdge edge : newPath) {
                        pathNodes.add(edge.getTo());
                    }
                    criticalPaths.add(new SimulationResult.CriticalPath(
                            "cp_" + (criticalPaths.size() + 1),
                            pathNodes,
                            Math.round(impactScore * 100f) / 100f,
                            current.depth == 0 ? "DIRECT_DEPENDENCY" : "INDIRECT_DEPENDENCY"
                    ));
                }

                queue.add(new TraversalState(dep, newPath, nextStrength, current.depth + 1));
            }
        }

        // 2. Build Impact Analysis dynamically
        int totalAffected = affectedList.size();
        float avgStrength = totalAffected > 0 ? (totalStrength / totalAffected) : 0f;
        SimulationResult.ImpactAnalysis impactAnalysis = new SimulationResult.ImpactAnalysis(
                totalAffected,
                maxDepth,
                Math.round(avgStrength * 100f) / 100f
        );

        // 3. System Insights dynamically from actual graph topology
        List<SimulationResult.BottleneckNode> bottlenecks = buildBottlenecks(triggerName);

        List<String> riskHotspots = new ArrayList<>();
        for (SimulationResult.AffectedNode an : affectedList) {
            if ("CRITICAL".equals(an.getRiskLevel()) || "HIGH".equals(an.getRiskLevel())) {
                riskHotspots.add(an.getNode() + " (" + an.getType() + ")");
            }
            if (riskHotspots.size() >= 5) break;
        }
        if (riskHotspots.isEmpty()) {
            riskHotspots.add(triggerName + " Dependencies");
        }

        float avgMitigation = 0;
        if (totalAffected > 0) {
            for (SimulationResult.AffectedNode an : affectedList) {
                avgMitigation += an.getMitigationScore();
            }
            avgMitigation = avgMitigation / totalAffected;
        } else {
            avgMitigation = 0.5f;
        }
        float resilience = Math.max(0.1f, avgMitigation - (input.getSeverity() * 0.2f));

        SimulationResult.SystemInsights insights = new SimulationResult.SystemInsights(
                bottlenecks,
                riskHotspots,
                Math.round(resilience * 100f) / 100f
        );

        // 4. Build Simulation Event dynamically
        SimulationResult.SimulationEvent simEvent = new SimulationResult.SimulationEvent(
                "evt_" + (1000 + new Random().nextInt(9000)),
                input.getEventType(),
                triggerName,
                input.getSeverity(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );

        return new SimulationResult(simEvent, impactAnalysis, affectedList, criticalPaths, insights);
    }

    // --- Helper methods ---

    private float getBaseMultiplier(SimulationEventType eventType) {
        switch (eventType) {
            case SUPPLY_DISRUPTION:
            case RESOURCE_SHORTAGE:
                return 1.0f;
            case FACTORY_SHUTDOWN:
                return 1.2f;
            case PRICE_INCREASE:
            case PRICE_DECREASE:
                return 0.7f;
            case LOGISTICS_DELAY:
            case PORT_CLOSURE:
            case TRANSPORT_FAILURE:
                return 0.8f;
            default:
                return 0.8f;
        }
    }

    private float getDecayFactor(SimulationEventType eventType) {
        switch (eventType) {
            case SUPPLY_DISRUPTION:
            case RESOURCE_SHORTAGE:
                return 0.90f;
            case FACTORY_SHUTDOWN:
                return 0.40f;
            case PRICE_INCREASE:
            case PRICE_DECREASE:
                return 0.80f;
            case LOGISTICS_DELAY:
            case PORT_CLOSURE:
            case TRANSPORT_FAILURE:
                return 0.85f;
            default:
                return 0.80f;
        }
    }

    private float getMitigationBase(SimulationEventType eventType) {
        switch (eventType) {
            case PRICE_INCREASE:
            case PRICE_DECREASE:
                return 0.5f;
            case SUPPLY_DISRUPTION:
            case RESOURCE_SHORTAGE:
                return 0.15f;
            case FACTORY_SHUTDOWN:
                return 0.1f;
            default:
                return 0.2f;
        }
    }

    private String classifyRiskLevel(float impactScore) {
        if (impactScore > 0.85f) return "CRITICAL";
        if (impactScore > 0.65f) return "HIGH";
        if (impactScore > 0.35f) return "MEDIUM";
        return "LOW";
    }

    private List<SimulationResult.BottleneckNode> buildBottlenecks(String triggerName) {
        List<SimulationResult.BottleneckNode> bottlenecks = new ArrayList<>();
        try {
            List<CentralNode> centralNodes = queryRepo.findBottlenecks();
            if (centralNodes != null) {
                for (CentralNode cn : centralNodes) {
                    if (bottlenecks.size() >= 3) break;
                    bottlenecks.add(new SimulationResult.BottleneckNode(
                            cn.getName(),
                            cn.getScore() != null ? cn.getScore().floatValue() : 0.5f,
                            0.75f
                    ));
                }
            }
        } catch (Exception e) {
            // Safe fallback if GDS plugin is not installed — use the trigger node itself
            bottlenecks.add(new SimulationResult.BottleneckNode(triggerName, 0.90f, 0.85f));
        }
        if (bottlenecks.isEmpty()) {
            bottlenecks.add(new SimulationResult.BottleneckNode(triggerName, 0.90f, 0.85f));
        }
        return bottlenecks;
    }

    private String mapNodeTypeToLabel(NodeType type) {
        if (type == null) return "Unknown";
        switch (type) {
            case COMPANY: return "Company";
            case SUPPLIER: return "Supplier";
            case RESOURCE: return "Resource";
            case PRODUCT: return "Product";
            case COUNTRY: return "Country";
            case MARKET: return "Market";
            default: return "Entity";
        }
    }
}
