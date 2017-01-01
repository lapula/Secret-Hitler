/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class CallSpecialElectionState implements GameState {
    
    Game game;
    
    private final String HEADER = "Special election!";
    private final String SUB_HEADER = "President, nominate the next president!";
    
    public CallSpecialElectionState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player president = game.getVariables().getPresident();
        Map<String, String> choices = new HashMap<>();
        
        game.getPlayerManager().getPlayers().forEach(player -> {
            if (!player.getName().equals(president.getName())) {
                choices.put(player.getName(), player.getName());
            }
        });
        
        List<Player> target = new ArrayList<>();
        target.add(president);
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), target, choices, HEADER, SUB_HEADER);
    }

    @Override
    public void receiveData(String player, String data) {
        Player president = game.getVariables().getPresident();
        Player specialPresident = game.getPlayerManager().getPlayerByName(data);
        if (specialPresident != null) {
            game.getVariables().setPresidentBeforeSpecialElection(president);
            game.getVariables().setSpecialElectionPresident(specialPresident);
            game.getVariables().setSpecialElectionPhase(1);
            PlayerWebSocketHandler.alertPlayer(specialPresident, "Special election!", "You are the next president!");
            game.changeState(State.ROUND_START);
        }
    }
}
