/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.*;

/**
 *
 * @author pulli
 */
public class GameVariables {
    
    private Map<String, String> electionResults;
    private Optional<Player> supremeChancellor;
    private Optional<Player> viceChair;
    private Optional<Player> previousViceChair;
    private Optional<Player> previousSupremeChancellor;
    private int gamePlayers;
    private int senateVotesThisRound;
    private int loyalistPoliciesPassed;
    private int separatistPoliciesPassed;
    private boolean lastPolicyPassedSeparatist;
    private int specialElectionPhase;
    private Optional<Player> specialElectionSupremeChancellor;
    private Optional<Player> supremeChancellorBeforeSpecialElection;
    
    
    private List<Policy> vetoedPolicies;
    
    public GameVariables() {
        this.electionResults = new TreeMap<>();
        this.senateVotesThisRound = 0;
        this.loyalistPoliciesPassed = 0;
        this.separatistPoliciesPassed = 0;
        this.specialElectionPhase = 0;
        this.lastPolicyPassedSeparatist = false;
        this.supremeChancellor = Optional.empty();
        this.viceChair = Optional.empty();
        this.previousViceChair = Optional.empty();
        this.previousSupremeChancellor = Optional.empty();

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

    public Optional<Player> getSupremeChancellor() {
        return supremeChancellor;
    }

    public void setSupremeChancellor(Player supremeChancellor) {
        this.supremeChancellor = Optional.ofNullable(supremeChancellor);
    }

    public Optional<Player> getViceChair() {
        return viceChair;
    }

    public void setViceChair(Player viceChair) {
        this.viceChair = Optional.ofNullable(viceChair);
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
        this.lastPolicyPassedSeparatist = false;
    }
    
    public int getLoyalistPolicyCount() {
        return this.loyalistPoliciesPassed;
    }
    
    public int getSeparatistPolicyCount() {
        return this.separatistPoliciesPassed;
    }
    
    public void addSeparatistPolicy() {
        this.separatistPoliciesPassed++;
        this.lastPolicyPassedSeparatist = true;
    }
    
    public List<Policy> getVetoedPolicies() {
        return vetoedPolicies;
    }

    public void setVetoedPolicies(List<Policy> vetoedPolicies) {
        this.vetoedPolicies = vetoedPolicies;
    }

    public Optional<Player> getSpecialElectionSupremeChancellor() {
        return specialElectionSupremeChancellor;
    }

    public void setSpecialElectionSupremeChancellor(Player specialElectionSupremeChancellor) {
        this.specialElectionSupremeChancellor = Optional.ofNullable(specialElectionSupremeChancellor);
    }

    public Optional<Player> getSupremeChancellorBeforeSpecialElection() {
        return supremeChancellorBeforeSpecialElection;
    }

    public void setSupremeChancellorBeforeSpecialElection(Player supremeChancellorBeforeSpecialElection) {
        this.supremeChancellorBeforeSpecialElection = Optional.ofNullable(supremeChancellorBeforeSpecialElection);
    }

    public int getSpecialElectionPhase() {
        return specialElectionPhase;
    }

    public void setSpecialElectionPhase(int specialElectionPhase) {
        this.specialElectionPhase = specialElectionPhase;
    }

    public void setPreviousViceChair(Player previousViceChair) {
        this.previousViceChair = Optional.ofNullable(previousViceChair);
    }

    public void setPreviousSupremeChancellor(Player previousSupremeChancellor) {
        this.previousSupremeChancellor = Optional.ofNullable(previousSupremeChancellor);
    }

    public boolean isLastPolicyPassedSeparatist() {
        return lastPolicyPassedSeparatist;
    }

    public Optional<Player> getPreviousViceChair() {
        return previousViceChair;
    }

    public Optional<Player> getPreviousSupremeChancellor() {
        return previousSupremeChancellor;
    }
}
