package dev.wound.Graphing.Controller;

import dev.wound.Graphing.MappingEvent.SimulationInput;
import dev.wound.Graphing.MappingEvent.SimulationResult;
import dev.wound.Graphing.Service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @MutationMapping
    public SimulationResult runSimulation(@Argument SimulationInput input) {
        return simulationService.runSimulation(input);
    }
}
