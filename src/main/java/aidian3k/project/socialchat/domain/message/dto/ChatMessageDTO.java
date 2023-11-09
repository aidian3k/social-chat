package aidian3k.project.socialchat.domain.message.dto;

import aidian3k.project.socialchat.domain.message.enums.MessageType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatMessageDTO {
    String content;
    String sender;
    MessageType messageType;
}
