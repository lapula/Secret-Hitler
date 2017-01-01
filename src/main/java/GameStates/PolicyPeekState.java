/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import com.lapula.secret.hitler.PlayerWebSocketHandler;

/**
 *
 * @author pulli
 */
public class PolicyPeekState implements GameState {
    
    Game game;
    
    public PolicyPeekState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player president = game.getVariables().getPresident();
        String topThree = game.getPolicyDeck().nextThreeToString();
        PlayerWebSocketHandler.alertPlayer(president, "Policy peek!", "The next policies are: " + topThree);
        
        game.changeState(State.ROUND_START);
    }

    @Override
    public void receiveData(String player, String data) {
        return;
    }
}
