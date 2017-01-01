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
import java.util.Map;
/**
 *
 * @author pulli
 */
public class VoteOnGovernmentState implements GameState {
    
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
        game.getVariables().setGovernmentVotesThisRound(game.getVariables().getGovernmentVotesThisRound() + 1);
        
        Map<String, String> choices = new HashMap<>();
        choices.put("JA", "Ja!");
        choices.put("NEIN", "Nein!");
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), game.getPlayerManager().getPlayers(), choices, "Vote!", "Do you approve this government?");
    }
    
    
    @Override
    public void receiveData(String player, String data) {
        game.getVariables().addVote(player, data);
        if (game.getVariables().getElectionResults().size() == game.getPlayerManager().getPlayers().size()) {
            handleElectionResults();
        }
    }
    
    private void handleElectionResults() {
        
        int jaVotes = 0;
        int neinVotes = 0;
        
        for (String result : game.getVariables().getElectionResults().values()) {
            if (result.equals("JA")) {
                jaVotes++;
            } else if (result.equals("NEIN")) {
                neinVotes++;
            }
        }
        
        boolean governmentFormed = jaVotes > neinVotes;
        
        if (governmentFormed) {
            PlayerWebSocketHandler.setSpecialRole(game.getVariables().getChancellor(), "You are the chancellor!");
            game.changeState(State.LEGISTLATIVE_SESSION);
        } else {
            
            if (game.getVariables().getGovernmentVotesThisRound() == 3) {
                if (Math.random() > 0.5) {
                    game.getVariables().addFascistPolicy();
                } else {
                    game.getVariables().addLiberalPolicy();
                }
                game.changeState(State.ROUND_START);
            } else {
                game.getVariables().setChancellor(null);
                Player president = game.getVariables().getPresident();
                Player nextPresident = game.getPlayerManager().getNextPlayer(president);
                game.getVariables().setPresident(nextPresident);
                PlayerWebSocketHandler.clearSpecialRoles(game.getPlayerManager().getPlayers(), nextPresident);
                PlayerWebSocketHandler.setSpecialRole(nextPresident, "You are the president!");
                game.changeState(State.NOMINATE_CHANCELLOR);
            }
            
            
        }
        
    }
    
}
