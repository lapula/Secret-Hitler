/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.HashMap;

/**
 *
 * @author pulli
 */
public class RoundStartState implements GameState {

    private Game game;
    
    public RoundStartState(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        
        
        game.getVariables().setElectionResults(new HashMap<>());
        game.getVariables().setGovernmentVotesThisRound(0);
        game.getVariables().setChancellor(null);
        Player president = game.getVariables().getPresident();
        Player nextPresident = game.getPlayerManager().getNextPlayer(president);
        game.getVariables().setPresident(nextPresident);
        game.getVariables().setVetoedPolicies(null);
        PlayerWebSocketHandler.clearSpecialRoles(game.getPlayerManager().getPlayers(), nextPresident);
        PlayerWebSocketHandler.setSpecialRole(nextPresident, "You are the president!");
        
        System.out.println("Liberal policies: " + game.getVariables().getLiberalPolicyCount());
        System.out.println("Fascist policies: " + game.getVariables().getFascistPolicyCount());
        
        game.changeState(State.NOMINATE_CHANCELLOR);
    }

    @Override
    public void receiveData(String player, String data) {
        // do nothing
    }
    
}
