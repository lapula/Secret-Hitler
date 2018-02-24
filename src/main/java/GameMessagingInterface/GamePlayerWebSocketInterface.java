
package GameMessagingInterface;

import GameLogic.Game;
import GameLogic.Player;
import SithImperative.Main;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@WebSocket
public class GamePlayerWebSocketInterface {

    private static final String PING = "PING";

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        Long dayInMilliseconds = 1000l * 60l * 60l * 24l;
        user.setIdleTimeout(10000l);
        System.out.println("Player connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Main.games.values().forEach(game -> {
            if (game.getPlayerManager().getPlayerSession(user) != null) {
                game.getPlayerManager().getPlayerSession(user).setSession(null);
                System.out.println("Nulled player connection!");
            }
        });
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        JSONObject obj = new JSONObject(message);
        Map<String, String> messageMap = obj.toMap().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().toString()
        ));
        Game game = Main.games.get(messageMap.get("gameName"));

        if (messageMap.get("type").equals(PING)) {
            ping(user);
        } else {
            game.getGameMessageService().receiveMessage(user, messageMap);
        }
    }

    private void ping(Session user) {
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", "PONG");
        try {
            user.getRemote().sendString(mainObj.toString());
        } catch (IOException ex) {
            Logger.getLogger(GamePlayerWebSocketInterface.class.getName()).log(Level.SEVERE, "Ping failed.", ex);
        }
    }
}