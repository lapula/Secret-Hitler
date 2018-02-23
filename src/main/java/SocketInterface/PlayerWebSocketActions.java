package SocketInterface;

import GameLogic.Player;
import GameLogic.Role;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerWebSocketActions {

    private static final String PLAYER_QUERY = "PLAYER_QUERY";
    private static final String PLAYER_INIT = "PLAYER_INIT";
    private static final String SET_SPECIAL_ROLE = "SET_SPECIAL_ROLE";
    private static final String ALERT_PLAYER = "ALERT_PLAYER";

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


    public static void sendQueryAndInfoMessages(List<Player> players, List<Player> targets, Map<String, String> choices, String header, String subheader, String gameState) {

        JSONObject jsonChoices = new JSONObject(choices);
        JSONObject mainObj = new JSONObject();
        mainObj.put("type", PLAYER_QUERY);
        mainObj.put("gameState", gameState);
        mainObj.put("header", header);
        mainObj.put("subheader", subheader);
        mainObj.put("choices", jsonChoices);

        targets.forEach(target -> {
            try {
                if (target.getSession() == null) {
                    try {
                        System.out.println("trying again in 10s");
                        TimeUnit.SECONDS.sleep(10);
                        sendQueryAndInfoMessages(players, targets, choices, header, subheader, gameState);
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
