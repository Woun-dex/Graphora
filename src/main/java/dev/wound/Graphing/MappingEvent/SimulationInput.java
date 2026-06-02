package dev.wound.Graphing.MappingEvent;

import java.util.UUID;

import dev.wound.Graphing.Entity.SimulationEventType;
import lombok.Data;

@Data
public class SimulationInput {

    private UUID triggerNode;
    private SimulationEventType eventType;
    private float severity;

}
