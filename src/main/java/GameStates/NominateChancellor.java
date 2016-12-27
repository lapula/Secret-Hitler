/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.State;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author pulli
 */
public class NominateChancellor implements GameState {
    
    private Game game;
    
    private final String HEADER = "Nominate your chancellor!";
    private final String SUB_HEADER = "Who do you wish to nominate?";
    
    public NominateChancellor(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        String presidentName = game.getVariables().get("president");
        Player president = game.getPlayerManager().getPlayerByName(presidentName);
        
        Map<String, String> choices = new HashMap<>();
        
        game.getPlayerManager().getPlayers().forEach(player -> {
            if (!player.getName().equals(presidentName)) {
                choices.put(player.getName(), player.getName());
            }
        });
        
        List<Player> target = new ArrayList<>();
        target.add(president);
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), target, choices, HEADER, SUB_HEADER);
    }
    
    
    @Override
    public void receiveData(String player, String data) {
        if (game.getPlayerManager().getPlayerByName(data) != null) {
            game.getVariables().put("chancellor", data);
            game.changeState(State.VOTE_ON_GOVERNMENT);
        }
    }
}
