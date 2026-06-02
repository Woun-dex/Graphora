package dev.wound.Graphing.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private Long MessageID;
    private UUID SenderID;
    private Long WorkspaceID;
    private String Content;
    private LocalDateTime TimeStamp;
}
