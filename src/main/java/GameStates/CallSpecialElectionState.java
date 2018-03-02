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
//TODO fix this
public class CallSpecialElectionState implements GameState {

    private static final String SPECIAL_ELECTION = "Special election!";
    private static final String SPECIAL_ELECTION_DESCRIPTION = "Supreme Chancellor, please nominate the next Supreme Chancellor!";
    private static final String SPECIAL_ELECTION_INFORM_SUPREME_CHANCELLOR = "You are now the Supreme Chancellor!";

    private Game game;
    private Player supremeChancellor;
    protected Map<String, String> choices;
    protected Player specialSupremeChancellor;

    public CallSpecialElectionState(Game game) {
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
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(),
                Collections.singletonList(supremeChancellor), choices, SPECIAL_ELECTION, SPECIAL_ELECTION_DESCRIPTION, State.CALL_SPECIAL_ELECTION.toString());
    }

    @Override
    public void receiveData(String player, String data) {
        specialSupremeChancellor = game.getPlayerManager().getPlayerByName(data).get();
        game.getVariables().setSupremeChancellorBeforeSpecialElection(supremeChancellor);
        game.getVariables().setSpecialElectionSupremeChancellor(specialSupremeChancellor);
        game.getVariables().setSpecialElectionPhase(1);
        game.stateStatusUpdate(State.ROUND_START);
    }

    @Override
    public void sendEndMessages() {
        game.getGamePlayerMessageActions().alertPlayer(specialSupremeChancellor, SPECIAL_ELECTION, SPECIAL_ELECTION_INFORM_SUPREME_CHANCELLOR);

    }
}
