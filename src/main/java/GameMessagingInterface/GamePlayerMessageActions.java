package GameMessagingInterface;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Role;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePlayerMessageActions {

    private static final String PLAYER_QUERY = "PLAYER_QUERY";
    private static final String PLAYER_INIT = "PLAYER_INIT";
    private static final String SET_SPECIAL_ROLE = "SET_SPECIAL_ROLE";
    private static final String ALERT_PLAYER = "ALERT_PLAYER";
    private static final Integer RESPONSE_MESSAGE_SEND_ATTEMPTS = 60;
    private static final Integer MESSAGE_SEND_ATTEMPTS = 20;
    private GameMessageService gameMessageService = null;

    public GamePlayerMessageActions(GameMessageService gameMessageService) {
        this.gameMessageService = gameMessageService;
    }

    public void initPlayer(Player player, Role role) {

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("type", PLAYER_INIT);
        jsonMessage.put("role", role.toString());

        gameMessageService.sendPlayerMessage(player, jsonMessage, MESSAGE_SEND_ATTEMPTS);
    }


    public void sendQueryAndInfoMessages(List<Player> players, List<Player> targets, Map<String, String> choices, String header, String subheader, String gameState) {

        JSONObject jsonChoices = new JSONObject(choices);
        JSONObject jsonTargetMessage = new JSONObject();
        jsonTargetMessage.put("type", PLAYER_QUERY);
        jsonTargetMessage.put("gameState", gameState);
        jsonTargetMessage.put("header", header);
        jsonTargetMessage.put("subheader", subheader);
        jsonTargetMessage.put("choices", jsonChoices);

        targets.forEach(target -> {
            gameMessageService.sendPlayerMessageRequiredResponse(target, jsonTargetMessage, RESPONSE_MESSAGE_SEND_ATTEMPTS);
        });

        JSONObject jsonOthersMessage = new JSONObject();
        jsonOthersMessage.put("type", PLAYER_QUERY);
        jsonOthersMessage.put("gameState", gameState);
        jsonOthersMessage.put("header", header);
        jsonOthersMessage.put("subheader", subheader);
        jsonOthersMessage.put("choices", "");

        players.forEach(player -> {
            if (!targets.contains(player)) {
                System.out.println("Sent info to player: " + player.getName());
                gameMessageService.sendPlayerMessage(player, jsonOthersMessage, MESSAGE_SEND_ATTEMPTS);
            }
        });
    }

    public void setSpecialRole(Player target, String role) {

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("type", SET_SPECIAL_ROLE);
        jsonMessage.put("role", role);

        gameMessageService.sendPlayerMessage(target, jsonMessage, MESSAGE_SEND_ATTEMPTS);
    }

    public void clearSpecialRoles(List<Player> players, Player ignore) {

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("type", SET_SPECIAL_ROLE);
        jsonMessage.put("role", "");

        players.forEach(player -> {
            if (!player.getName().equals(ignore.getName())) {
                gameMessageService.sendPlayerMessage(player, jsonMessage, MESSAGE_SEND_ATTEMPTS);
            }
        });
    }

    public void alertPlayer(Player player, String header, String text) {

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("type", ALERT_PLAYER);
        jsonMessage.put("header", header);
        jsonMessage.put("text", text);

        gameMessageService.sendPlayerMessage(player, jsonMessage, MESSAGE_SEND_ATTEMPTS);
    }
}
