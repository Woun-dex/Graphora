package dev.wound.Graphing.Service;

import dev.wound.Graphing.MappingEvent.SimulationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastSimulationResult(String workspaceId, SimulationResult result) {
        String destination = "/topic/workspace/" + workspaceId + "/simulation";
        log.info("Broadcasting simulation result to WebSocket destination: {}", destination);
        messagingTemplate.convertAndSend(destination, result);
    }
}
