package dev.wound.Graphing.MappingEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouseEvent {
    private String workspaceId;
    private UUID userId;
    private String username;
    private double x;
    private double y;
    private String timestamp;
}
