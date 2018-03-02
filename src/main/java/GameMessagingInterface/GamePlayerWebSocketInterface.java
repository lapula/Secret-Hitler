
package GameMessagingInterface;

import GameLogic.Game;
import GameLogic.Player;
import SithImperative.Main;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebSocket
public class GamePlayerWebSocketInterface {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(GamePlayerWebSocketInterface.class);

    // Day in ms
    private static final Long TIMEOUT_MS = 1000l * 60l * 60l;
    private static final String PING = "PING";

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        user.setIdleTimeout(TIMEOUT_MS);
        //user.setIdleTimeout(1000l * 5l);
        logger.debug("Player connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Main.games.values().forEach(game -> {
            if (game.getPlayerManager().getPlayerBySession(user) != null) {
                Optional<Player> player = game.getPlayerManager().getPlayerBySession(user);
                if (player.map(Player::getSession).isPresent()) {
                    // Optional empty
                    player.get().setSession(null);
                    logger.info("Nulled connection, player: " + player.map(Player::getName).orElse(""));
                }
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

        if (game == null) {
            logger.warn("PLAYER TRIED TO CONNECT BUT GAME NOT FOUND");
            user.close();
        } else if (messageMap.get("type").equals(PING)) {
            ping(user);
        } else {
            logger.info("Message received from player with type: " + messageMap.get("type"));
            game.getGameMessageService().receivePlayerMessage(user, messageMap);
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