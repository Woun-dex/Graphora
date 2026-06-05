package dev.wound.Graphing.MappingEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawEvent {
    private String workspaceId;
    private String senderId;
    private String type;
    private double x;
    private double y;
    private String color;
    private int size;
    private String timestamp;
}
