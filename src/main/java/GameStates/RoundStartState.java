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

import java.util.HashMap;

/**
 *
 * @author pulli
 */
public class RoundStartState implements GameState {

    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";

    private Game game;
    public RoundStartState(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        GameVariables gameVariables = game.getVariables();
        PlayerManager playerManager = game.getPlayerManager();
        
        Player nextSupremeChancellor;
        Player supremeChancellor;
        if (gameVariables.getSpecialElectionPhase() == 1) {
            gameVariables.setSpecialElectionPhase(2);
            nextSupremeChancellor = gameVariables.getSpecialElectionSupremeChancellor();
        } else if (gameVariables.getSpecialElectionPhase() == 2) {
            supremeChancellor = gameVariables.getSupremeChancellorBeforeSpecialElection();

            /*if (!playerManager.getPlayers().contains(supremeChancellor)) {
                int index = playerManager.getPlayersIncludingExecuted().indexOf(supremeChancellor);
                if (index == playerManager.getPlayersIncludingExecuted().size() - 1) {
                    index = -1;
                }
                supremeChancellor = playerManager.getPlayersIncludingExecuted().get(index + 1);
            }
            nextSupremeChancellor = playerManager.getNextPlayer(supremeChancellor);
            */
            nextSupremeChancellor = playerManager.getNextPlayer(supremeChancellor);
            gameVariables.setSpecialElectionPhase(0);
        } else {
            supremeChancellor = gameVariables.getSupremeChancellor();
            nextSupremeChancellor = playerManager.getNextPlayer(supremeChancellor);
        }
        gameVariables.setPreviousViceChair(null);
        gameVariables.setPreviousViceChair(game.getVariables().getViceChair());
        gameVariables.setPreviousSupremeChancellor(null);
        gameVariables.setPreviousSupremeChancellor(game.getVariables().getSupremeChancellor());
        gameVariables.setSupremeChancellor(nextSupremeChancellor);
        gameVariables.setElectionResults(new HashMap<>());
        gameVariables.setSenateVotesThisRound(0);
        gameVariables.setViceChair(null);
        gameVariables.setVetoedPolicies(null);
        game.getGamePlayerMessageActions().clearSpecialRoles(playerManager.getPlayers(), nextSupremeChancellor);
        game.getGamePlayerMessageActions().setSpecialRole(nextSupremeChancellor, INFORM_SUPREME_CHANCELLOR);
        
        game.changeState(State.NOMINATE_VICE_CHAIR);
    }

    @Override
    public void receiveData(String player, String data) {
        // do nothing
    }
    
}
