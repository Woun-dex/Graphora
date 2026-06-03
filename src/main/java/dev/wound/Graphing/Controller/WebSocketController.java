package dev.wound.Graphing.Controller;

import dev.wound.Graphing.MappingEvent.MessageEvent;
import dev.wound.Graphing.MappingEvent.MouseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

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
}
