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
public class ExecutionState implements GameState {

    private static final String EXECUTION_HEADER = "Execution!";
    private static final String EXECUTION_SUB_HEADER = "Supreme Chancellor, who do you wish to execute?";
    private static final String EXECUTED_HEADER = "You are executed!";
    private static final String EXECUTED_SUB_HEADER = "You may not speak or reveal your role!";

    Game game;
    public ExecutionState(Game game) {
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
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), target, choices, EXECUTION_HEADER, EXECUTION_SUB_HEADER, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        Player execute = game.getPlayerManager().getPlayerByName(data);
        if (execute != null) {
            game.getPlayerManager().getPlayers().remove(execute);
            game.getGamePlayerMessageActions().alertPlayer(execute, EXECUTED_HEADER, EXECUTED_SUB_HEADER);
            game.changeState(State.ROUND_START);
        }
    }
}
