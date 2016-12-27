/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameLogic.State;
import GameStates.NominateChancellor;
import GameStates.GameState;
import GameStates.StateFactory;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Game {
    
    private StateFactory stateFactory;
    private PlayerManager playerManager;
    private GameState gameState;
    private Map<String, String> stateVariables;
    private Map<String, String> electionResults;
    
    public Game(Integer numberOfPlayers) {
        
        this.stateFactory = new StateFactory();
        this.gameState = null;
        this.stateVariables = new HashMap<>();
        this.electionResults = new HashMap<>();
        initVariables(numberOfPlayers);
        playerManager = new PlayerManager(this);
    }
    
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public Map<String, String> getVariables() {
        return this.stateVariables;
    }
    
    public void changeState(State state) {
        this.gameState =  this.stateFactory.getGameState(this, state);
        this.gameState.doAction();
   }
    
    public void receiveData(String player, String data) {
        this.gameState.receiveData(player, data);
    }
    
    public void initVariables(Integer numberOfPlayers) {
        this.stateVariables.put("numberOfPlayers", numberOfPlayers.toString());
        this.stateVariables.put("president", null);
        this.stateVariables.put("chancellor", null);
    }
    
    public Map<String, String> getElectionResults() {
        return this.electionResults;
    }
    
    public void addVote(String player, String vote) {
        this.electionResults.put(player, vote);
    }
    
    public void setElectionResults(Map<String, String> electionResults) {
        this.electionResults = electionResults;
    }
}
