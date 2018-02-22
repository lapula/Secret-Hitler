/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import SithImperative.PlayerWebSocketHandler;

/**
 *
 * @author pulli
 */
public class PolicyPeekState implements GameState {

    private static final String HEADER = "Policy peek!";

    Game game;
    public PolicyPeekState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        String topThree = game.getPolicyDeck().nextThreeToString();
        String policiesText = "The next policies are: " + topThree;
        PlayerWebSocketHandler.alertPlayer(supremeChancellor, HEADER, policiesText);
        
        game.changeState(State.ROUND_START);
    }

    @Override
    public void receiveData(String player, String data) {
        return;
    }
}
