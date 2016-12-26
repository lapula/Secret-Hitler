/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateObservers;

import GameLogic.Game;
import GameLogic.State;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author pulli
 */
public class ElectionObserver implements StateObserver {
    
    private int attempt;
    
    public ElectionObserver() {
        attempt = 0;
    }

    @Override
    public void informObserver(Game game) {
        if (game.getState().equals(State.NOMINATE_CHANCELLOR)) {
            nominateChancellor(game);
        } else if (game.getState().equals(State.VOTE_ON_GOVERNMENT)) {
            holdElection(game);
        }
    }
    
    private void nominateChancellor(Game game) {
        
    }
    
    private void holdElection(Game game) {
        Map<String, String> choices = new HashMap<>();
            choices.put("JA", "Ja!");
            choices.put("NEIN", "Nein!");
            PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), choices, "Vote!", "Do you approve this government?");
    }
    
}
