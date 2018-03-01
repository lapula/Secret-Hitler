/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author pulli
 */
public class ExecutionState implements GameState {

    private static final String EXECUTION_HEADER = "Execution!";
    private static final String EXECUTION_SUB_HEADER = "Supreme Chancellor, who do you wish to execute?";
    private static final String EXECUTED_HEADER = "You are executed!";
    private static final String EXECUTED_SUB_HEADER = "You may not speak or reveal your role!";
    private static final String EVENT_EXECUTION = "EXECUTION";
    private static final String EVENT_EXECUTION_HEADER = "Execute order 66.";
    private static final String EVENT_EXECUTION_SUBHEADER = "The acting Supreme Chancellor has assassinated %s!";

    private Game game;
    private String executionSubheader;
    private Player supremeChancellor;
    protected Map<String, String> choices;
    protected Player executed;

    public ExecutionState(Game game) {
        this.game = game;
        supremeChancellor = game.getVariables().getSupremeChancellor().get();
    }

    @Override
    public void doAction() {
        choices = game.getPlayerManager().getPlayers().stream()
                .filter(player -> !player.getName().equals(supremeChancellor.getName()))
                .collect(Collectors.toMap(Player::getName, Player::getName));
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), Collections.singletonList(supremeChancellor), choices, EXECUTION_HEADER, EXECUTION_SUB_HEADER, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        executed = game.getPlayerManager().getPlayerByName(data).get();
        game.getPlayerManager().getPlayers().remove(executed);
        executionSubheader = String.format(EVENT_EXECUTION_SUBHEADER, executed.getName());
        game.stateStatusUpdate(State.ROUND_START);
    }

    @Override
    public void sendEndMessages() {
        game.getGamePlayerMessageActions().alertPlayer(executed, EXECUTED_HEADER, EXECUTED_SUB_HEADER);
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_EXECUTION, EVENT_EXECUTION_HEADER, executionSubheader);
    }
}
