
package com.lapula.secret.hitler;

import GameLogic.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    
    private static final String PLAYER_QUERY = "PLAYER_QUERY";
    private static final String PLAYER_INIT = "PLAYER_INIT";
    
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        Long dayInMilliseconds = 60000l * 60l * 24l;
        user.setIdleTimeout(dayInMilliseconds);
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
            Main.games.get(gameName).getPlayerManager().addPlayer(new Player(playerName, user));
            System.out.println(gameName);
            
        } else if (type.equals("QUERY_RESPONSE")) {
            String response = obj.getString("response");
            System.out.println(response);
        } else if (type.equals("PING")) {
            JSONObject mainObj = new JSONObject();
            mainObj.put("type", "PONG");
            try {
                user.getRemote().sendString(mainObj.toString());
            } catch (IOException ex) {
                Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void initPlayer(Player player, String role) {
        
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", PLAYER_INIT);
        mainObj.put("role", role);
        
        try {
            player.getSession().getRemote().sendString(mainObj.toString());
        } catch (IOException ex) {
            Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendChoiceMessage(List<Player> targets, Map<String, String> choices, String header, String subheader) {
        
        JSONObject jsonChoices = new JSONObject(choices);
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", PLAYER_QUERY);
        mainObj.put("header", header);
        mainObj.put("subheader", subheader);
        mainObj.put("choices", jsonChoices);
        
        targets.forEach(target -> {
            try {
                target.getSession().getRemote().sendString(mainObj.toString());
            } catch (IOException ex) {
                Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }


}