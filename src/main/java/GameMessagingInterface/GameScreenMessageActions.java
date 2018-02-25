package GameMessagingInterface;

import GameLogic.Player;
import GameLogic.Role;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreenMessageActions {

    private static final String STATUS_UPDATE = "STATUS_UPDATE";
    private static final String GAME_EVENT = "GAME_EVENT";
    private static final Integer MESSAGE_SEND_ATTEMPTS = 5;

    private GameMessageService gameMessageService;

    public GameScreenMessageActions(GameMessageService gameMessageService) {
        this.gameMessageService = gameMessageService;
    }

    public void sendStatusUpdate(List<Session> gameListeners, JSONObject update) {
        JSONObject message = new JSONObject();
        message.put("type", STATUS_UPDATE);
        message.put("data", update);

        gameListeners.forEach(listener -> {
            this.gameMessageService.sendGameScreenMessage(listener, message, MESSAGE_SEND_ATTEMPTS);
        });
    }

    public void sendGameEvent(List<Session> gameListeners, String eventType, String header, String subheader) {
        JSONObject message = new JSONObject();
        message.put("type", GAME_EVENT);
        message.put("header", header);
        message.put("subheader", subheader);
        message.put("eventType", eventType);

        gameListeners.forEach(listener -> {
            this.gameMessageService.sendGameScreenMessage(listener, message, MESSAGE_SEND_ATTEMPTS);
        });
    }
}
