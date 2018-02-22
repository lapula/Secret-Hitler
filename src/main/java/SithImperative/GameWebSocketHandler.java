
package SithImperative;

import GameLogic.Game;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


@WebSocket
public class GameWebSocketHandler {
    
    private static final String LISTEN_GAME = "LISTEN_GAME";
    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String STATUS_UPDATE = "STATUS_UPDATE";
    private static final String POLL = "POLL";
    
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        Long dayInMilliseconds = 60000l * 60l * 24l;
        user.setIdleTimeout(dayInMilliseconds);
        System.out.println("Player connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closed game listener");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(message);
        JSONObject obj = new JSONObject(message);
        String type = obj.getString("type");
        String gameName = obj.getString("gameName");
        
        if (type.equals(LISTEN_GAME)) {
            Main.games.get(gameName).addGameListener(user);
        } else if (type.equals(CREATE_GAME)) {
            String gamePlayers = obj.getString("gamePlayers");
            Game game = new Game(Integer.parseInt(gamePlayers));
            Main.games.put(gameName, game);
            game.getGameListeners().add(user);
        } else if (type.equals(POLL)) {
            Game game = Main.games.get(gameName);
            //sendStatusUpdate(Main.games.get(gameName).getGameListeners(), game.toJSON());
        }
    }
    
    public static void sendStatusUpdate(List<Session> gameListeners, JSONObject update) {
        
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", STATUS_UPDATE);
        mainObj.put("data", update);
        
        gameListeners.forEach(listener -> {
            try {
                listener.getRemote().sendString(mainObj.toString());
            } catch (IOException ex) {
                Logger.getLogger(GameWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}