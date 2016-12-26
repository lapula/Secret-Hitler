/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class PlayerManager {
    
    private List<Player> players;
    private Game game;
    
    public PlayerManager(Game game) {
        players = new ArrayList<>();
        this.game = game;
    }
    
    public void addPlayer(Player player) {
        players.add(player);
        PlayerWebSocketHandler.initPlayer(player, "Fascist");
        
        int numberOfPlayers = Integer.parseInt(game.getVariables().get("numberOfPlayers"));
        if (players.size() == numberOfPlayers) {
            game.changeState(State.VOTE_ON_GOVERNMENT, null);
            
            
            Map<String, String> choices = new HashMap<>();
            choices.put("JA", "Ja!");
            choices.put("NEIN", "Nein!");
            PlayerWebSocketHandler.sendChoiceMessage(players, choices, "Vote!", "Do you approve this government?");
        }
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    
}
