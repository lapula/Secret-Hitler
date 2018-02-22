
package SithImperative;

import GameLogic.Game;
import GameLogic.Player;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import GameLogic.Role;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


@WebSocket
public class PlayerWebSocketHandler {
    
    private static final String PLAYER_QUERY = "PLAYER_QUERY";
    private static final String PLAYER_INIT = "PLAYER_INIT";
    private static final String SET_SPECIAL_ROLE = "SET_SPECIAL_ROLE";
    private static final String ALERT_PLAYER = "ALERT_PLAYER";
    
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
            if (Main.games.get(gameName).getPlayerManager().getPlayerByName(playerName) == null) {
                Main.games.get(gameName).getPlayerManager().addPlayer(new Player(playerName, gameName, user));
            } else {
                Main.games.get(gameName).getPlayerManager().getPlayerByName(playerName).setSession(user);
            }
        } else if (type.equals("QUERY_RESPONSE")) {
            String response = obj.getString("response");
            Main.games.get(gameName).receiveData(playerName, response);
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
    
    public static void initPlayer(Player player, Role role) {
        
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", PLAYER_INIT);
        mainObj.put("role", role.toString());
        
        try {
            player.getSession().getRemote().sendString(mainObj.toString());
        } catch (IOException ex) {
            Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void sendChoiceMessage(List<Player> players, List<Player> targets, Map<String, String> choices, String header, String subheader) {
        
        JSONObject jsonChoices = new JSONObject(choices);
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", PLAYER_QUERY);
        mainObj.put("header", header);
        mainObj.put("subheader", subheader);
        mainObj.put("choices", jsonChoices);
        
        targets.forEach(target -> {
            try {
                if (target.getSession() == null) {
                    try {
                        System.out.println("trying again in 10s");
                        TimeUnit.SECONDS.sleep(10);
                        sendChoiceMessage(players, targets, choices, header, subheader);
                        return;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                target.getSession().getRemote().sendString(mainObj.toString());
            } catch (IOException ex) {
                Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        mainObj.put("choices", "");
        mainObj.put("subheader", "");
                
        players.forEach(player -> {
            try {
                if (!targets.contains(player)) {
                    player.getSession().getRemote().sendString(mainObj.toString());
                }
            } catch (IOException ex) {
                Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static void setSpecialRole(Player target, String role) {
        
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", SET_SPECIAL_ROLE);
        mainObj.put("role", role);
        
        try {
            if (target.getSession() == null) {
                try {
                    System.out.println("trying again in 10s");
                    TimeUnit.SECONDS.sleep(10);
                    setSpecialRole(target, role);
                    return;
                } catch (InterruptedException ex) {
                    Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            target.getSession().getRemote().sendString(mainObj.toString());
        } catch (IOException ex) {
            Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void clearSpecialRoles(List<Player> players, Player ignore) {
        
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", SET_SPECIAL_ROLE);
        mainObj.put("role", "");
        
        String ignoreName = "";
        if (ignore != null) {
            ignoreName = ignore.getName();
        }
        
        for (Player player : players) {
            try {
                if (!player.getName().equals(ignoreName)) {
                    player.getSession().getRemote().sendString(mainObj.toString());
                }
                
            } catch (IOException ex) {
                Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
    }
    
    public static void alertPlayer(Player player, String header, String text) {
        
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", ALERT_PLAYER);
        mainObj.put("header", header);
        mainObj.put("text", text);
        
        try {
            player.getSession().getRemote().sendString(mainObj.toString());
        } catch (IOException ex) {
            Logger.getLogger(PlayerWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}