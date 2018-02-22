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
    private Player supremeChancellor;
    private Player viceChair;
    private int gamePlayers;
    private int senateVotesThisRound;
    private int loyalistPoliciesPassed;
    private int separatistPoliciesPassed;
    private int specialElectionPhase;
    private Player specialElectionSupremeChancellor;
    private Player supremeChancellorBeforeSpecialElection;
    
    
    private List<Policy> vetoedPolicies;
    
    public GameVariables() {
        this.electionResults = new HashMap<>();
        this.senateVotesThisRound = 0;
        this.loyalistPoliciesPassed = 0;
        this.separatistPoliciesPassed = 0;
        this.specialElectionPhase = 0;
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

    public Player getSupremeChancellor() {
        return supremeChancellor;
    }

    public void setSupremeChancellor(Player supremeChancellor) {
        this.supremeChancellor = supremeChancellor;
    }

    public Player getViceChair() {
        return viceChair;
    }

    public void setViceChair(Player viceChair) {
        this.viceChair = viceChair;
    }

    public int getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(int gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public int getSenateVotesThisRound() {
        return senateVotesThisRound;
    }

    public void setSenateVotesThisRound(int senateVotesThisRound) {
        this.senateVotesThisRound = senateVotesThisRound;
    }
    
    public void addLoyalistPolicy() {
        this.loyalistPoliciesPassed++;
    }
    
    public int getLoyalistPolicyCount() {
        return this.loyalistPoliciesPassed;
    }
    
    public int getSeparatistPolicyCount() {
        return this.separatistPoliciesPassed;
    }
    
    public void addSeparatistPolicy() {
        this.separatistPoliciesPassed++;
    }
    
    public List<Policy> getVetoedPolicies() {
        return vetoedPolicies;
    }

    public void setVetoedPolicies(List<Policy> vetoedPolicies) {
        this.vetoedPolicies = vetoedPolicies;
    }

    public Player getSpecialElectionSupremeChancellor() {
        return specialElectionSupremeChancellor;
    }

    public void setSpecialElectionSupremeChancellor(Player specialElectionSupremeChancellor) {
        this.specialElectionSupremeChancellor = specialElectionSupremeChancellor;
    }

    public Player getSupremeChancellorBeforeSpecialElection() {
        return supremeChancellorBeforeSpecialElection;
    }

    public void setSupremeChancellorBeforeSpecialElection(Player supremeChancellorBeforeSpecialElection) {
        this.supremeChancellorBeforeSpecialElection = supremeChancellorBeforeSpecialElection;
    }

    public int getSpecialElectionPhase() {
        return specialElectionPhase;
    }

    public void setSpecialElectionPhase(int specialElectionPhase) {
        this.specialElectionPhase = specialElectionPhase;
    }
}
