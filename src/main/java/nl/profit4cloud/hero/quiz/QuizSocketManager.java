package nl.profit4cloud.hero.quiz;

import nl.profit4cloud.hero.RequestStartMessage;
import nl.profit4cloud.hero.UpdateNameMessage;
import nl.profit4cloud.hero.contestant.Contestant;
import nl.profit4cloud.hero.contestant.ContestantConnectedMessage;
import nl.profit4cloud.hero.contestant.ContestantStartMessage;
import nl.profit4cloud.hero.question.QuestionAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Optional;

@Controller
public class QuizSocketManager {

    private final QuizManager quizRoomManager;
    private final SimpMessagingTemplate webSocket;

    public QuizSocketManager(QuizManager quizRoomManager, SimpMessagingTemplate webSocket) {
        this.quizRoomManager = quizRoomManager;
        this.webSocket = webSocket;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizSocketManager.class);

    @EventListener
    public void onSessionConnectedEvent(SessionConnectEvent event) {
        LOGGER.info("Session connected: {}", event);
        var sha = StompHeaderAccessor.wrap(event.getMessage());
        var sessionId = sha.getSessionId();
        var uidList = sha.getNativeHeader("uid");
        if (uidList != null) {
            var uid = uidList.get(0);
            LOGGER.info("player connected, uid {}, sessionId {}", uid, sessionId);
            quizRoomManager.addPlayer(sessionId, uid);
        } else {
            LOGGER.info("management console connected {}", sessionId);
        }
        emitContestantUpdate();

    }

    @EventListener
    public void handleQuizEnd(QuizEndMessage event) {
        webSocket.convertAndSend("/topic/gameclientupdate", event);
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        var sha = StompHeaderAccessor.wrap(event.getMessage());
        var sessionId = sha.getSessionId();
        LOGGER.info("Session dis-connected: {}", sessionId);
        quizRoomManager.disconnectPlayer(sessionId);
        emitContestantUpdate();
    }

    @MessageMapping("/updateName")
    @SendTo("/topic/gameclientupdate")
    public ContestantConnectedMessage handleUpdateName(UpdateNameMessage message) {
        Optional.ofNullable(message.uid()).ifPresent(uid -> {
            quizRoomManager.updateContestantName(uid, message.name());
        });
        emitContestantUpdate();
        return new ContestantConnectedMessage(message.uid(), QuizState.CONNECTED, null);
    }

    @MessageMapping("/requestStart")
    @SendTo("/topic/gameclientupdate")
    public ContestantStartMessage requestStart(RequestStartMessage message) {
        emitContestantUpdate();
        return Optional.ofNullable(message.uid())
                .map(quizRoomManager::startGameFor)
                .stream()
                .peek(msg -> emitContestantUpdate())
                .peek(msg -> canPlay(msg))
                .findFirst()
                .orElseThrow();
    }


    @MessageMapping("/giveAnswer")
    @SendTo("/topic/gameManagerUpdate")
    public List<Contestant> handleAnswer(@Header("simpSessionId") String sessionId, QuestionAnswer answer) {
        quizRoomManager.handleAnswer(sessionId, answer);
        return quizRoomManager.getContestantsUpdate();
    }

    public void emitContestantUpdate() {
        var contestantsUpdate = quizRoomManager.getContestantsUpdate();



        webSocket.convertAndSend("/topic/gameManagerUpdate", contestantsUpdate);
    }


    public boolean canPlay(ContestantStartMessage contestant) {
        return switch (contestant.state()) {
            case CONNECTED, STARTED -> true;
            case UNDEFINED, BLOCKED, FINISHED -> false;
        };
    }
}
