package aidian3k.project.socialchat.application;

import aidian3k.project.socialchat.domain.message.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class ChatController {

	@MessageMapping("/send-message")
	@SendTo("/topic/public")
	public ChatMessageDTO handleSendMessage(
		@Payload ChatMessageDTO chatMessageDTO
	) {
		return chatMessageDTO;
	}

	@MessageMapping("/add-user")
	@SendTo("/topic/public")
	public ChatMessageDTO handleUserEnter(
		@Payload ChatMessageDTO chatMessageDTO,
		SimpMessageHeaderAccessor simpMessageHeaderAccessor
	) {
		final String userNameWebSocketSession = "username";
		simpMessageHeaderAccessor
			.getSessionAttributes()
			.put(userNameWebSocketSession, chatMessageDTO.getSender());

        return chatMessageDTO;
	}
}
