/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class CallSpecialElectionState implements GameState {

    private static final String SPECIAL_ELECTION = "Special election!";
    private static final String SPECIAL_ELECTION_DESCRIPTION = "Supreme Chancellor, please nominate the next Supreme Chancellor!";
    private static final String SPECIAL_ELECTION_INFORM_SUPREME_CHANCELLOR = "You are now the Supreme Chancellor!";

    Game game;
    public CallSpecialElectionState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        Map<String, String> choices = new HashMap<>();
        
        game.getPlayerManager().getPlayers().forEach(player -> {
            if (!player.getName().equals(supremeChancellor.getName())) {
                choices.put(player.getName(), player.getName());
            }
        });
        
        List<Player> target = new ArrayList<>();
        target.add(supremeChancellor);
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), target, choices, SPECIAL_ELECTION, SPECIAL_ELECTION_DESCRIPTION, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        Player specialSupremeChancellor = game.getPlayerManager().getPlayerByName(data);
        if (specialSupremeChancellor != null) {
            game.getVariables().setSupremeChancellorBeforeSpecialElection(supremeChancellor);
            game.getVariables().setSpecialElectionSupremeChancellor(specialSupremeChancellor);
            game.getVariables().setSpecialElectionPhase(1);
            game.getGamePlayerMessageActions().alertPlayer(specialSupremeChancellor, SPECIAL_ELECTION, SPECIAL_ELECTION_INFORM_SUPREME_CHANCELLOR);
            game.changeState(State.ROUND_START);
        }
    }
}
