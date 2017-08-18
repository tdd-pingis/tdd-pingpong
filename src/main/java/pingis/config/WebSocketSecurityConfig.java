package pingis.config;

import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
  }

  @Bean
  public DefaultHandshakeHandler handshakeHandler() {
    WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
    policy.setInputBufferSize(8192);
    policy.setIdleTimeout(600000);

    return new DefaultHandshakeHandler(new JettyRequestUpgradeStrategy(policy));
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    StompWebSocketEndpointRegistration endpointRegistration = registry
        .addEndpoint("/websocket");

    // HACK: Can't use JettyRequestUpgradeStrategy when running cucumber tests, because
    // Jetty isn't running.
    if (applicationContext.getEnvironment().getProperty("RUNNING_CUCUMBER") == null) {
      endpointRegistration.setHandshakeHandler(handshakeHandler());
    }

    endpointRegistration.withSockJS();
  }
}
