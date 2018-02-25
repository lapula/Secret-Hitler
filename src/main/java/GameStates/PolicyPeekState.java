/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

/**
 *
 * @author pulli
 */
public class PolicyPeekState implements GameState {

    private static final String HEADER = "Policy peek!";
    private static final String EVENT_POLICY_PEEK = "POLICY_PEEK";
    private static final String EVENT_POLICY_PEEK_HEADER = "I sense a plot to destroy the Jedi!";
    private static final String EVENT_POLICY_PEEK_SUBHEADER = "The acting Supreme Chancellor has received knowledge of the next three policies.";

    Game game;
    public PolicyPeekState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        String topThree = game.getPolicyDeck().nextThreeToString();
        String policiesText = "The next policies are: " + topThree;
        game.getGamePlayerMessageActions().alertPlayer(supremeChancellor, HEADER, policiesText);
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_POLICY_PEEK, EVENT_POLICY_PEEK_HEADER, EVENT_POLICY_PEEK_SUBHEADER);

        game.changeState(State.ROUND_START);
    }

    @Override
    public void receiveData(String player, String data) {
        return;
    }
}
