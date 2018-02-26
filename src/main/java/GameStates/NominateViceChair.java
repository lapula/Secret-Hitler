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
    private static final String EVENT_NOMINATION_SUBHEADER = "Representatives will vote to determine legality of the government.";

    private Game game;
    public NominateViceChair(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        String previousViceChairName = game.getVariables().getPreviousViceChairName();
        String previousSupremeChancellorName = game.getVariables().getPreviousSupremeChancellorName();

        //TODO doesn't work after failed government votes
        Map<String, String> choices = game.getPlayerManager().getPlayers().stream()
                .filter(player -> !player.getName().equals(supremeChancellor.getName())
                        && !player.getName().equals(previousViceChairName)
                        && !player.getName().equals(previousSupremeChancellorName))
                .collect(Collectors.toMap(Player::getName, Player::getName));

        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), Arrays.asList(supremeChancellor), choices, HEADER, SUB_HEADER, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        Player viceChair = game.getPlayerManager().getPlayerByName(data);
        game.getVariables().setViceChair(viceChair);
        game.changeState(State.VOTE_ON_GOVERNMENT);

        String nominationHeader = String.format(EVENT_NOMINATION_HEADER, viceChair.getName());
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_NOMINATION, nominationHeader, EVENT_NOMINATION_SUBHEADER);
    }
}
