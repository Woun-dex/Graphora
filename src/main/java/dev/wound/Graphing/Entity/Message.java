package dev.wound.Graphing.Entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Message {

    private Long MessageID;
    private UUID SenderID;
    private Long WorkspaceID;
    private String Content;
    private LocalDateTime TimeStamp;
}
