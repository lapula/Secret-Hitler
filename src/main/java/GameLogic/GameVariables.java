/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class GameVariables {
    
    private Map<String, String> electionResults;
    private Player president;
    private Player chancellor;
    private int gamePlayers;
    private int governmentVotesThisRound;
    private int liberalPoliciesPassed;
    private int fascistPoliciesPassed;
    
    private List<Policy> vetoedPolicies;
    
    public GameVariables() {
        this.electionResults = new HashMap<>();
        this.governmentVotesThisRound = 0;
        this.liberalPoliciesPassed = 0;
        this.fascistPoliciesPassed = 0;
    }

    public Map<String, String> getElectionResults() {
        return electionResults;
    }
    
    public void addVote(String player, String vote) {
        this.electionResults.put(player, vote);
    }

    public void setElectionResults(Map<String, String> electionResults) {
        this.electionResults = electionResults;
    }

    public Player getPresident() {
        return president;
    }

    public void setPresident(Player president) {
        this.president = president;
    }

    public Player getChancellor() {
        return chancellor;
    }

    public void setChancellor(Player chancellor) {
        this.chancellor = chancellor;
    }

    public int getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(int gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public int getGovernmentVotesThisRound() {
        return governmentVotesThisRound;
    }

    public void setGovernmentVotesThisRound(int governmentVotesThisRound) {
        this.governmentVotesThisRound = governmentVotesThisRound;
    }
    
    public void addLiberalPolicy() {
        this.liberalPoliciesPassed++;
    }
    
    public int getLiberalPolicyCount() {
        return this.liberalPoliciesPassed;
    }
    
    public int getFascistPolicyCount() {
        return this.fascistPoliciesPassed;
    }
    
    public void addFascistPolicy() {
        this.fascistPoliciesPassed++;
    }
    
    public List<Policy> getVetoedPolicies() {
        return vetoedPolicies;
    }

    public void setVetoedPolicies(List<Policy> vetoedPolicies) {
        this.vetoedPolicies = vetoedPolicies;
    }
    
}
