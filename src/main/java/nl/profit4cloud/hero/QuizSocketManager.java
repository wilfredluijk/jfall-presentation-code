package nl.profit4cloud.hero;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class QuizSocketManager {
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("Received a new web socket connection");
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("User Disconnected");
    }
}
