package dev.wound.Graphing.MappingEvent;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEvents {

    private WebsocketEvent type;
    private UUID userId;
    private String userName;

}
