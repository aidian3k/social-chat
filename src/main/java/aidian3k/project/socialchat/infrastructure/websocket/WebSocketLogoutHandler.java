package aidian3k.project.socialchat.infrastructure.websocket;

import aidian3k.project.socialchat.domain.message.dto.ChatMessageDTO;
import aidian3k.project.socialchat.domain.message.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketLogoutHandler {

	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@EventListener
	public void handleUserLeftChat(SessionDisconnectEvent event) {
		final StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(
			event.getMessage()
		);
		final String username = (String) stompHeaderAccessor
			.getSessionAttributes()
			.get("username");
		logAndNotifyUsersAboutExitingChat(username);
	}

	private void logAndNotifyUsersAboutExitingChat(String username) {
		if (username != null) {
			log.info("User with username=[{}] has left the chat", username);
			ChatMessageDTO leftMessage = ChatMessageDTO
				.builder()
				.messageType(MessageType.LEAVE)
				.sender(username)
				.build();
			simpMessageSendingOperations.convertAndSend("/topic/public", leftMessage);
		}
	}
}
