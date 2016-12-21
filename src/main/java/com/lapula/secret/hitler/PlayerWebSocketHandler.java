
package com.lapula.secret.hitler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;
import org.json.JSONObject;


@WebSocket
public class PlayerWebSocketHandler {
    
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("Player connected to server");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(message);
        JSONObject obj = new JSONObject(message);
        String type = obj.getString("type");
        
        if (type.equals("REGISTER_PLAYER")) {
            JSONObject content = obj.getJSONObject("content");
            String gameName = content.getString("gameName");
            String playerName = content.getString("playerName");
            System.out.println(playerName + gameName);
            Main.games.get(gameName).addPlayer(playerName, new Player(playerName, user));
            System.out.println(gameName);
            
        } else {
            
        }
    }
    
    public static void sendChoiceMessage(List<Player> targets, String[] choices) {
        
        JSONArray jsonChoices = new JSONArray(Arrays.asList(choices));
        
        targets.forEach(target -> {
            try {
                target.getSession().getRemote().sendString(jsonChoices.toString());
            } catch (IOException ex) {
                Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }

}