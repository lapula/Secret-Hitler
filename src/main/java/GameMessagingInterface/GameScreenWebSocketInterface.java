
package GameMessagingInterface;

import GameLogic.Game;
import SithImperative.Main;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@WebSocket
public class GameScreenWebSocketInterface {

    // Day in ms
    private static final Long TIMEOUT_MS = 1000l * 60l * 60l;
    private static final String PING = "PING";
    private static final String CREATE_GAME = "CREATE_GAME";
    
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        user.setIdleTimeout(TIMEOUT_MS);
        System.out.println("Game connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closed game listener");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        JSONObject obj = new JSONObject(message);
        Map<String, String> messageMap = obj.toMap().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().toString()
        ));

        Game game = Main.games.get(messageMap.get("gameName"));

        if (messageMap.get("type").equals(CREATE_GAME)) {
            createGame(user, messageMap);
        } else if (messageMap.get("type").equals(PING)) {
            //sendStatusUpdate(Main.games.get(gameName).getGameListeners(), game.toJSON());
        } else {
            game.getGameMessageService().receiveGameScreenMessage(user, messageMap);
        }
    }

    private void createGame(Session user, Map<String, String> message) {
        Game game = new Game(message.get("gameName"), Integer.parseInt(message.get("gamePlayers")));
        Main.games.put(game.getGameName(), game);
        game.getGameListeners().add(user);
        game.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(game.getGameListeners(), game.toJSON());
    }
    

}