
package SocketInterface;

import GameLogic.Game;
import GameLogic.Player;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import GameLogic.Role;
import SithImperative.Main;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


@WebSocket
public class PlayerWebSocketHandler {
    
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        Long dayInMilliseconds = 1000l * 60l * 60l * 24l;
        user.setIdleTimeout(10000l);
        System.out.println("Player connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        for (Game game : Main.games.values()) {
            if (game.getPlayerManager().getPlayerSession(user) != null) {
                game.getPlayerManager().getPlayerSession(user).setSession(null);
                System.out.println("nulled player connection!");
            }
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(message);
        JSONObject obj = new JSONObject(message);
        String type = obj.getString("type");
        String gameName = obj.getString("gameName");
        String playerName = obj.getString("playerName");
        
        // TODO Differentiate register and reconnect
        if (type.equals("REGISTER_PLAYER")) {
            registerPlayer(gameName, playerName, user);
        } else if (type.equals("QUERY_RESPONSE")) {
            queryResponse(obj, gameName, playerName);
        } else if (type.equals("PING")) {
            ping(user);
        }
    }

    private void registerPlayer(String gameName, String playerName, Session user) {
        if (Main.games.get(gameName).getPlayerManager().getPlayerByName(playerName) == null) {
            Main.games.get(gameName).getPlayerManager().addPlayer(new Player(playerName, gameName, user));
            Main.games.get(gameName).receiveData(playerName, null);
        } else {
            Main.games.get(gameName).getPlayerManager().getPlayerByName(playerName).setSession(user);
        }
    }

    private void queryResponse(JSONObject obj, String gameName, String playerName) {
        String response = obj.getString("response");
        Main.games.get(gameName).receiveData(playerName, response);
    }

    private void ping(Session user) {
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", "PONG");
        try {
            user.getRemote().sendString(mainObj.toString());
        } catch (IOException ex) {
            Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}