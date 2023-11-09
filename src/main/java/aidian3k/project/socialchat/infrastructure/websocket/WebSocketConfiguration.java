package aidian3k.project.socialchat.infrastructure.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration
	implements WebSocketMessageBrokerConfigurer {

	private static final String STOMP_DEFAULT_ENDPOINT = "/ws";
	private static final String DESTINATION_PREFIXES = "/app";
	private static final String BROKER_NAME = "/topic";

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint(STOMP_DEFAULT_ENDPOINT)
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes(DESTINATION_PREFIXES);
		registry.enableSimpleBroker(BROKER_NAME);
	}
}
