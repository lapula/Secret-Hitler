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
    private static final String SUB_HEADER = "The next policies are: ";

    private static final String EVENT_POLICY_PEEK = "POLICY_PEEK";
    private static final String EVENT_POLICY_PEEK_HEADER = "I sense a plot to destroy the Jedi!";
    private static final String EVENT_POLICY_PEEK_SUBHEADER = "The Supreme Chancellor that enacted the latest policy has received knowledge of the next three policies.";

    Game game;
    public PolicyPeekState(Game game) {
        this.game = game;
    }

    // TODO send confirm first to delay process
    @Override
    public void doAction() {
        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
        String topThree = game.getPolicyDeck().nextThreeToString();
        game.getGamePlayerMessageActions().alertPlayer(supremeChancellor, HEADER, SUB_HEADER + topThree);
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_POLICY_PEEK, EVENT_POLICY_PEEK_HEADER, EVENT_POLICY_PEEK_SUBHEADER);

        game.changeState(State.ROUND_START);
    }

    @Override
    public void receiveData(String player, String data) {
        // Do nothing
    }
}
