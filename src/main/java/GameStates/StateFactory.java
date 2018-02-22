/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;

/**
 *
 * @author pulli
 */
public class StateFactory {

    public GameState getGameState(Game game, State state) {
        
        if (state.equals(null)) {
            return null;
        }
        if (state.equals(State.NOMINATE_VICE_CHAIR)) {
            return new NominateViceChair(game);
        } else if (state.equals(State.VOTE_ON_GOVERNMENT)) {
            return new VoteOnGovernmentState(game);
        } else if (state.equals(State.ROUND_START)) {
            return new RoundStartState(game);
        } else if (state.equals(State.LEGISTLATIVE_SESSION)) {
            return new LegistlativeSessionState(game);
        } else if (state.equals(State.VETO)) {
            return new VetoState(game);
        } else if (state.equals(State.DETERMINE_EXECUTIVE_ACTION)) {
            return new DetermineExecutiveActionState(game);
        } else if (state.equals(State.INVESTIGATE_LOYALTY)) {
            return new InvestigateLoyaltyState(game);
        } else if (state.equals(State.CALL_SPECIAL_ELECTION)) {
            return new CallSpecialElectionState(game);
        } else if (state.equals(State.POLICY_PEEK)) {
            return new PolicyPeekState(game);
        } else if (state.equals(State.EXECUTION)) {
            return new ExecutionState(game);
        }
        
        return null;
    }
    
}
