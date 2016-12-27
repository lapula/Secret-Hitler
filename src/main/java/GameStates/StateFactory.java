/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.State;

/**
 *
 * @author pulli
 */
public class StateFactory {

    public GameState getGameState(Game game, State state) {
        
        if (state.equals(null)) {
            return null;
        }
        if (state.equals(State.NOMINATE_CHANCELLOR)) {
            return new NominateChancellor(game);
        } else if (state.equals(State.VOTE_ON_GOVERNMENT)) {
            return new VoteOnGovernmentState(game);
        }
        
        return null;
    }
    
}
