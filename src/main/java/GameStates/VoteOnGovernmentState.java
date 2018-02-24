/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author pulli
 */
public class VoteOnGovernmentState implements GameState {

    private static final String YES_VOTE = "In favour!";
    private static final String NO_VOTE = "Against!";
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String INFORM_VICE_CHAIR = "You are the Vice Chair!";
    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";
    private static final String VOTE_HEADER = "Vote!";
    private static final String VOTE_SUB_HEADER = "Do you approve this government?";

    private int attempt;
    private Game game;
    
    public VoteOnGovernmentState(Game game) {
        this.game = game;
        attempt = 0;
    }
    
    @Override
    public void doAction() {
        
        game.getVariables().setElectionResults(new HashMap<>());
        
        // increase government formation attempt counter
        game.getVariables().setSenateVotesThisRound(game.getVariables().getSenateVotesThisRound() + 1);
        
        Map<String, String> choices = new HashMap<>();
        choices.put(YES, YES_VOTE);
        choices.put(NO, NO_VOTE);
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), game.getPlayerManager().getPlayers(), choices, VOTE_HEADER, VOTE_SUB_HEADER, game.getGameStateType().toString());
    }
    
    
    @Override
    public void receiveData(String player, String data) {
        game.getVariables().addVote(player, data);
        if (game.getVariables().getElectionResults().size() == game.getPlayerManager().getPlayers().size()) {
            handleElectionResults();
        }
    }
    
    private void handleElectionResults() {
        
        int yesVotes = 0;
        int noVotes = 0;
        
        for (String result : game.getVariables().getElectionResults().values()) {
            if (result.equals(YES)) {
                yesVotes++;
            } else if (result.equals(NO)) {
                noVotes++;
            }
        }
        
        boolean governmentFormed = yesVotes > noVotes;
        
        if (governmentFormed) {
            game.getGamePlayerMessageActions().setSpecialRole(game.getVariables().getViceChair(), INFORM_VICE_CHAIR);
            game.changeState(State.LEGISTLATIVE_SESSION);
        } else {

            //TODO inform players about this
            if (game.getVariables().getSenateVotesThisRound() == 3) {
                if (Math.random() > 0.5) {
                    game.getVariables().addSeparatistPolicy();
                } else {
                    game.getVariables().addLoyalistPolicy();
                }
                game.changeState(State.ROUND_START);
            } else {
                game.getVariables().setViceChair(null);
                Player supremeChancellor = game.getVariables().getSupremeChancellor();
                Player nextSupremeChancellor = game.getPlayerManager().getNextPlayer(supremeChancellor);
                game.getVariables().setSupremeChancellor(nextSupremeChancellor);
                game.getGamePlayerMessageActions().clearSpecialRoles(game.getPlayerManager().getPlayers(), nextSupremeChancellor);
                game.getGamePlayerMessageActions().setSpecialRole(nextSupremeChancellor, INFORM_SUPREME_CHANCELLOR);
                game.changeState(State.NOMINATE_VICE_CHAIR);
            }
            
            
        }
        
    }
    
}
