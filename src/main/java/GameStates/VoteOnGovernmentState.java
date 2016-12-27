/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.State;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author pulli
 */
public class VoteOnGovernmentState implements GameState {
    
    private int attempt;
    private Game game;
    
    public VoteOnGovernmentState(Game game) {
        this.game = game;
        attempt = 0;
    }
    
    @Override
    public void doAction() {
        Map<String, String> choices = new HashMap<>();
        choices.put("JA", "Ja!");
        choices.put("NEIN", "Nein!");
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), game.getPlayerManager().getPlayers(), choices, "Vote!", "Do you approve this government?");
    }
    
    
    @Override
    public void receiveData(String player, String data) {
        
    }
    
}
