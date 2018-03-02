/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class PolicyPeekState implements GameState {

    private static final String HEADER = "Policy peek!";
    private static final String SUB_HEADER = "The next policies are: ";
    private static final String CONFIRM = "Act on this, we will.";

    private static final String EVENT_POLICY_PEEK = "POLICY_PEEK";
    private static final String EVENT_POLICY_PEEK_HEADER = "I sense a plot to destroy the Jedi!";
    private static final String EVENT_POLICY_PEEK_SUBHEADER = "The Supreme Chancellor that enacted the latest policy has received knowledge of the next three policies.";

    private Game game;
    private Player supremeChancellor;
    protected Map<String, String> choices;
    private String topThree;

    public PolicyPeekState(Game game) {
        this.game = game;
        choices = new HashMap<>();
        topThree = game.getPolicyDeck().nextThreeToString();
        supremeChancellor = game.getVariables().getSupremeChancellor().get();
    }

    @Override
    public void doAction() {
        choices.put(CONFIRM, CONFIRM);
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(),
                Collections.singletonList(supremeChancellor), choices, HEADER, SUB_HEADER + topThree, State.POLICY_PEEK.toString());

    }

    @Override
    public void receiveData(String player, String data) {
        game.getPlayerManager().getPlayerByName(player).ifPresent(p -> {
            if (p.equals(supremeChancellor)) game.stateStatusUpdate(State.ROUND_START);
        });
    }

    @Override
    public void sendEndMessages() {
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_POLICY_PEEK, EVENT_POLICY_PEEK_HEADER, EVENT_POLICY_PEEK_SUBHEADER);
    }
}
