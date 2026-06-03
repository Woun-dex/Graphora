package dev.wound.Graphing.MappingEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketEvent<T> {
    private String type;
    private T payload;
    private String timestamp;
}
