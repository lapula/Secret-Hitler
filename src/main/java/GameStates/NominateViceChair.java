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
public class NominateViceChair implements GameState {

    private static final String HEADER = "The Supreme Chancellor is nominating his Vice Chair!";
    private static final String SUB_HEADER = "Supreme Chancellor, who do you wish to nominate?";

    private static final String EVENT_NOMINATION = "NOMINATION";
    private static final String EVENT_NOMINATION_HEADER = "%s has been nominated as the Vice Chair!";
    private static final String EVENT_NOMINATION_SUBHEADER = "A surprise to be sure, but a welcome one. Representatives will now to determine legality of the government.";

    private Game game;
    private Player supremeChancellor;
    protected Map<String, String> choices;
    private String nominationHeader;

    public NominateViceChair(Game game) {
        this.game = game;
        this.supremeChancellor = game.getVariables().getSupremeChancellor().get();
    }
    
    @Override
    public void doAction() {
        String previousViceChairName = game.getVariables().getPreviousViceChair().map(Player::getName).orElse("");
        String previousSupremeChancellorName = game.getVariables().getPreviousSupremeChancellor().map(Player::getName).orElse("");
        choices = game.getPlayerManager().getPlayers().stream()
                .filter(player -> !player.getName().equals(supremeChancellor.getName()))
                .filter(player -> !player.getName().equals(previousViceChairName))
                .filter(player -> !player.getName().equals(previousSupremeChancellorName) || game.getPlayerManager().getPlayers().size() <= 5)
                .collect(Collectors.toMap(Player::getName, Player::getName));
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(),
                Collections.singletonList(supremeChancellor), choices, HEADER, SUB_HEADER, State.NOMINATE_VICE_CHAIR.toString());

    }

    @Override
    public void receiveData(String player, String data) {
        Player viceChair = game.getPlayerManager().getPlayerByName(data).get();
        game.getVariables().setViceChair(viceChair);
        nominationHeader = String.format(EVENT_NOMINATION_HEADER, viceChair.getName());
        game.stateStatusUpdate(State.VOTE_ON_GOVERNMENT);
    }


    @Override
    public void sendEndMessages() {
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_NOMINATION, nominationHeader, EVENT_NOMINATION_SUBHEADER);
    }
}
