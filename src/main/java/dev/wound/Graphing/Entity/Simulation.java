package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Simulation {

    private String id;
    private SimulationEventType eventType;
    private UUID triggerNodeId;
    private float severity;

    public void validateSeverity() {
        if (severity < 0 || severity > 1) {
            throw new IllegalArgumentException("Severity must be between 0 and 1");
        }
    }
}
