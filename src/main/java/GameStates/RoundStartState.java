/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.GameVariables;
import GameLogic.Player;
import GameLogic.PlayerManager;
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
        GameVariables gameVariables = game.getVariables();
        PlayerManager playerManager = game.getPlayerManager();
        
        Player nextPresident;
        if (gameVariables.getSpecialElectionPhase() == 1) {
            gameVariables.setSpecialElectionPhase(2);
            nextPresident = gameVariables.getSpecialElectionPresident();
        } else if (gameVariables.getSpecialElectionPhase() == 2) {
            Player president = gameVariables.getPresidentBeforeSpecialElection();
            if (!playerManager.getPlayers().contains(president)) {
                int index = playerManager.getPlayersIncludingExecuted().indexOf(president);
                if (index == playerManager.getPlayersIncludingExecuted().size() - 1) {
                    index = -1;
                }
                president = playerManager.getPlayersIncludingExecuted().get(index + 1);
            }
            nextPresident = playerManager.getNextPlayer(president);
            gameVariables.setSpecialElectionPhase(0);
        } else {
            Player president = gameVariables.getPresident();
            nextPresident = playerManager.getNextPlayer(president);
        }
        gameVariables.setPresident(nextPresident);
        gameVariables.setElectionResults(new HashMap<>());
        gameVariables.setGovernmentVotesThisRound(0);
        gameVariables.setChancellor(null);
        gameVariables.setVetoedPolicies(null);
        PlayerWebSocketHandler.clearSpecialRoles(playerManager.getPlayers(), nextPresident);
        PlayerWebSocketHandler.setSpecialRole(nextPresident, "You are the president!");
        
        System.out.println("Liberal policies: " + gameVariables.getLiberalPolicyCount());
        System.out.println("Fascist policies: " + gameVariables.getFascistPolicyCount());
        
        game.changeState(State.NOMINATE_CHANCELLOR);
    }

    @Override
    public void receiveData(String player, String data) {
        // do nothing
    }
    
}
