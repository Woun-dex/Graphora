package dev.wound.Graphing.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.wound.Graphing.Entity.GraphEdge;
import dev.wound.Graphing.Entity.GraphNode;
import dev.wound.Graphing.MappingEvent.DrawEvent;
import dev.wound.Graphing.MappingEvent.GraphEvent;
import dev.wound.Graphing.MappingEvent.MessageEvent;
import dev.wound.Graphing.MappingEvent.MouseEvent;
import dev.wound.Graphing.Service.GraphService;
import dev.wound.Graphing.Service.WorkspaceService;
import dev.wound.Graphing.Service.SimulationService;
import dev.wound.Graphing.MappingEvent.SimulationInput;
import dev.wound.Graphing.MappingEvent.SimulationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final GraphService graphService;
    private final WorkspaceService workspaceService;
    private final SimulationService simulationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/workspace/{workspaceId}/cursor")
    @SendTo("/topic/workspace/{workspaceId}/cursor")
    public MouseEvent handleCursorMove(@DestinationVariable String workspaceId, @Payload MouseEvent event) {
        log.debug("Received cursor move event for workspace {}: x={}, y={}", workspaceId, event.getX(), event.getY());
        return event;
    }

    @MessageMapping("/workspace/{workspaceId}/chat")
    @SendTo("/topic/workspace/{workspaceId}/chat")
    public MessageEvent handleChatMessage(@DestinationVariable String workspaceId, @Payload MessageEvent event) {
        log.debug("Received chat message event for workspace {}: {}", workspaceId, event.getContent());
        return event;
    }

    @MessageMapping("/workspace/{workspaceId}/graph")
    @SendTo("/topic/workspace/{workspaceId}/graph")
    public GraphEvent handleGraphChange(@DestinationVariable String workspaceId, @Payload GraphEvent event) {
        log.debug("Received graph change event for workspace {}: action={}", workspaceId, event.getAction());
        try {
            switch (event.getAction()) {
                case NODE_CREATED:
                    GraphNode nodeToCreate = objectMapper.convertValue(event.getPayload(), GraphNode.class);
                    if (nodeToCreate.getGraphKey() == null) {
                        workspaceService.getWorkspaceById(workspaceId)
                                .ifPresent(ws -> nodeToCreate.setGraphKey(ws.getGraphId()));
                    }
                    GraphNode createdNode = graphService.createNode(nodeToCreate);
                    event.setPayload(createdNode);
                    break;
                case NODE_UPDATED:
                    GraphNode nodeToUpdate = objectMapper.convertValue(event.getPayload(), GraphNode.class);
                    GraphNode updatedNode = graphService.updateNode(nodeToUpdate.getId(), nodeToUpdate.getName(), nodeToUpdate.getType());
                    event.setPayload(updatedNode);
                    break;
                case NODE_DELETED:
                    UUID nodeIdToDelete = null;
                    if (event.getPayload() instanceof String) {
                        nodeIdToDelete = UUID.fromString((String) event.getPayload());
                    } else {
                        try {
                            GraphNode node = objectMapper.convertValue(event.getPayload(), GraphNode.class);
                            nodeIdToDelete = node.getId();
                        } catch (Exception e) {
                            Map<?, ?> map = objectMapper.convertValue(event.getPayload(), Map.class);
                            nodeIdToDelete = UUID.fromString(map.get("id").toString());
                        }
                    }
                    if (nodeIdToDelete != null) {
                        graphService.deleteNode(nodeIdToDelete);
                    }
                    break;
                case EDGE_ADDED:
                    GraphEdge edgeToAdd = objectMapper.convertValue(event.getPayload(), GraphEdge.class);
                    GraphNode nodeWithAddedEdge = graphService.addEdge(
                            edgeToAdd.getFromNodeId(),
                            edgeToAdd.getToNodeId(),
                            edgeToAdd.getRelationshipType(),
                            edgeToAdd.getWeight(),
                            edgeToAdd.getRiskFactor()
                    );
                    event.setPayload(nodeWithAddedEdge);
                    break;
                case EDGE_REMOVED:
                    GraphEdge edgeToRemove = objectMapper.convertValue(event.getPayload(), GraphEdge.class);
                    GraphNode nodeWithRemovedEdge = graphService.removeEdge(
                            edgeToRemove.getFromNodeId(),
                            edgeToRemove.getToNodeId()
                    );
                    event.setPayload(nodeWithRemovedEdge);
                    break;
                case SIMULATION_CLEARED:
                    break;
                default:
                    log.warn("Unknown graph action: {}", event.getAction());
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to process graph change event for workspace {}: {}", workspaceId, e.getMessage(), e);
            throw e;
        }
        return event;
    }

    @MessageMapping("/workspace/{workspaceId}/draw")
    @SendTo("/topic/workspace/{workspaceId}/draw")
    public DrawEvent handleDraw(@DestinationVariable String workspaceId, @Payload DrawEvent event) {
        log.debug("Received draw event for workspace {}: type={}", workspaceId, event.getType());
        return event;
    }

    @MessageMapping("/workspace/{workspaceId}/simulation")
    @SendTo("/topic/workspace/{workspaceId}/simulation")
    public SimulationResult handleSimulation(@DestinationVariable String workspaceId, @Payload SimulationInput event) {
        log.info("Received simulation request via WebSocket for workspace {}: triggerNode={}, eventType={}, severity={}", 
            workspaceId, event.getTriggerNode(), event.getEventType(), event.getSeverity());
        event.setWorkspaceId(workspaceId);
        return simulationService.runSimulation(event);
    }
}
