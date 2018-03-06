
package GameMessagingInterface;

import GameLogic.Game;
import SithImperative.Main;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@WebSocket
public class GameScreenWebSocketInterface {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(GameScreenWebSocketInterface.class);

    // Day in ms
    private static final Long TIMEOUT_MS = 1000l * 60l * 60l;
    private static final String PING = "PING";
    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String LISTEN_GAME = "LISTEN_GAME";

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        user.setIdleTimeout(TIMEOUT_MS);
        logger.info("Game connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        logger.info("Closed game listener");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        JSONObject obj = new JSONObject(message);
        Map<String, String> messageMap = obj.toMap().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().toString()
        ));

        if (messageMap.get("type").equals(CREATE_GAME)) {
            createGame(user, messageMap);
        } else if (messageMap.get("type").equals(PING)) {
            //sendStatusUpdate(Main.games.get(gameName).getGameListeners(), game.toJSON());
        } else if (messageMap.get("type").equals(LISTEN_GAME)) {
            Game game = Main.games.get(messageMap.get("gameName"));
            if (game != null) {
                game.addGameListener(user);
                logger.info("Added listener to game: " + game.getGameName());
                game.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(game.getGameListeners(), game.toJSON());
            }
        }
    }

    private void createGame(Session user, Map<String, String> message) {
        try {
            Map<String, Game> games = Main.games;
            String gameName = RandomStringUtils.random(6, true, false).toUpperCase();
            Game existingGame = games.get(gameName);

            if (existingGame != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(existingGame.getTimestamp()));
                cal.add(Calendar.HOUR_OF_DAY, 3);
                cal.getTime();
                if (cal.getTime().after(new Date())) {
                    // try with new name
                    logger.warn("Name collusion, trying with new random.");
                    createGame(user, message);
                }
            }
            logger.info("Creating game with name: " + gameName);
            Game game = new Game(gameName, Integer.parseInt(message.get("gamePlayers")));
            Main.games.put(game.getGameName(), game);
            game.getGameListeners().add(user);
            game.getGameMessageService().getGameScreenMessageActions().sendStatusUpdate(game.getGameListeners(), game.toJSON());
        } catch (Exception e) {
            logger.error("Exception when creating new game: " + e.getMessage());
        }
    }
    

}