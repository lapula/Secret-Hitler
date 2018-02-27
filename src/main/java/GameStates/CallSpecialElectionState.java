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

    private static final String EVENT_SPECIAL = "EVENT_SPECIAL";
    private static final String EVENT_SPECIAL_HEADER = "Emergency powers granted!";
    private static final String EVENT_SPECIAL_SUBHEADER = "The Supreme Chancellor will nominate the next Supreme Chancellor.";

    Game game;
    public CallSpecialElectionState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_SPECIAL, EVENT_SPECIAL_HEADER, EVENT_SPECIAL_SUBHEADER);

        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
        Map<String, String> choices = game.getPlayerManager().getPlayers().stream()
                .filter(player -> !player.getName().equals(supremeChancellor.getName()))
                .collect(Collectors.toMap(Player::getName, Player::getName));

        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), Arrays.asList(supremeChancellor), choices, SPECIAL_ELECTION, SPECIAL_ELECTION_DESCRIPTION, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
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
