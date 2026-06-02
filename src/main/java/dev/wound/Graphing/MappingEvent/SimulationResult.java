package dev.wound.Graphing.MappingEvent;

import dev.wound.Graphing.Entity.SimulationEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {

    private SimulationEvent event;
    private ImpactAnalysis impactAnalysis;
    private List<AffectedNode> affectedNodes;
    private List<CriticalPath> criticalPaths;
    private SystemInsights systemInsights;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimulationEvent {
        private String id;
        private SimulationEventType type;
        private String triggerNode;
        private float severity;
        private String timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImpactAnalysis {
        private int totalAffectedNodes;
        private int maxDepth;
        private float avgDependencyStrength;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AffectedNode {
        private String node;
        private String type;
        private float impactScore;
        private String riskLevel; // e.g. CRITICAL, HIGH, MEDIUM, LOW
        private List<ExposureEdge> exposurePath;
        private int alternativePaths;
        private float mitigationScore;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExposureEdge {
        private String from;
        private String to;
        private float weight;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriticalPath {
        private String pathId;
        private List<String> nodes;
        private float strength;
        private String type; // e.g. DIRECT_DEPENDENCY, INDIRECT_DEPENDENCY
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemInsights {
        private List<BottleneckNode> bottleneckNodes;
        private List<String> riskHotspots;
        private float resilienceScore;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BottleneckNode {
        private String name;
        private float centralityScore;
        private float failureImpactRadius;
    }
}
