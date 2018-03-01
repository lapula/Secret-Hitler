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

/**
 *
 * @author pulli
 */
public class RoundStartState implements GameState {

    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";

    private static final String EVENT_START_ROUND = "START_ROUND";
    private static final String EVENT_START_ROUND_HEADER = "Supreme Chancellor %s is nominating his Vice Chair!";
    private static final String EVENT_START_ROUND_SUBHEADER = "The nomination must be confirmed by the senate.";

    private Game game;
    private PlayerManager playerManager;
    Player nextSupremeChancellor;

    public RoundStartState(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        playerManager = game.getPlayerManager();
        GameVariables gameVariables = game.getVariables();

        Player supremeChancellor;
        if (gameVariables.getSpecialElectionPhase() == 1) {
            gameVariables.setSpecialElectionPhase(2);
            nextSupremeChancellor = gameVariables.getSpecialElectionSupremeChancellor().get();
        } else if (gameVariables.getSpecialElectionPhase() == 2) {
            supremeChancellor = gameVariables.getSupremeChancellorBeforeSpecialElection().get();
            nextSupremeChancellor = playerManager.getNextPlayer(supremeChancellor);
            gameVariables.setSpecialElectionPhase(0);
        } else {
            supremeChancellor = gameVariables.getSupremeChancellor().get();
            nextSupremeChancellor = playerManager.getNextPlayer(supremeChancellor);
        }
        gameVariables.setSupremeChancellor(nextSupremeChancellor);
        gameVariables.cleanElectionResults();
        gameVariables.setSenateVotesThisRound(0);
        gameVariables.setViceChair(null);
        gameVariables.setVetoedPolicies(null);

        
        game.stateStatusUpdate(State.NOMINATE_VICE_CHAIR);
    }

    @Override
    public void receiveData(String player, String data) {
        // do nothing
    }

    @Override
    public int sendData() {
        game.stateStatusAction();
        return 0;
    }

    @Override
    public void sendEndMessages() {
        game.getGamePlayerMessageActions().clearSpecialRoles(playerManager.getPlayers(), nextSupremeChancellor);
        game.getGamePlayerMessageActions().setSpecialRole(nextSupremeChancellor, INFORM_SUPREME_CHANCELLOR);
        //game.getGameScreenMessageActions().sendGameEvent(
        //        game.getGameListeners(), EVENT_START_ROUND, EVENT_START_ROUND_HEADER, EVENT_START_ROUND_SUBHEADER);
    }

}
