/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameStates.State;
import GameStates.GameState;
import GameStates.StateFactory;
import SithImperative.GameWebSocketHandler;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


public class Game {
    
    private StateFactory stateFactory;
    private PlayerManager playerManager;
    private GameState gameState;
    private GameVariables gameVariables;
    private PolicyDeck policyDeck;
    private List<Session> gameListeners;
    
    public Game(Integer numberOfPlayers) {
        
        this.gameListeners = new LinkedList<>();
        this.stateFactory = new StateFactory();
        this.gameState = null;
        this.gameVariables = new GameVariables();
        this.policyDeck = new PolicyDeck();
        
        this.gameVariables.setGamePlayers(numberOfPlayers);
        playerManager = new PlayerManager(this);
    }
    
    public void changeState(State state) {
        this.gameState =  this.stateFactory.getGameState(this, state);
        this.gameState.doAction();
        GameWebSocketHandler.sendStatusUpdate(this.gameListeners, this.toJSON());
   }
    
    public void receiveData(String player, String data) {
        this.gameState.receiveData(player, data);
        GameWebSocketHandler.sendStatusUpdate(this.gameListeners, this.toJSON());
    }
    
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public GameVariables getVariables() {
        return this.gameVariables;
    }
    
    public PolicyDeck getPolicyDeck() {
        return this.policyDeck;
    }
    
    public void addGameListener(Session session) {
        this.gameListeners.add(session);
    }
    
    public List<Session> getGameListeners() {
        return this.gameListeners;
    }
    
    public JSONObject toJSON() {
        
        String supremeChancellorName = "";
        String viceChairName = "";
        JSONObject electionResults = new JSONObject(gameVariables.getElectionResults());
        
        if (gameVariables.getSupremeChancellor() != null) {
            supremeChancellorName = gameVariables.getSupremeChancellor().getName();
        }
        if (gameVariables.getViceChair() != null) {
            viceChairName = gameVariables.getViceChair().getName();
        }
        System.out.println(playerManager.getPlayers().toString());
        JSONObject json = new JSONObject();
        json.put("supremeChancellor", supremeChancellorName);
        json.put("viceChair", viceChairName);
        json.put("gamePlayers", gameVariables.getGamePlayers());
        json.put("governmentVotesThisRound", gameVariables.getSenateVotesThisRound());
        json.put("loyalistPoliciesPassed", gameVariables.getLoyalistPolicyCount());
        json.put("separatistPoliciesPassed", gameVariables.getSeparatistPolicyCount());
        json.put("electionResults", electionResults);
        json.put("players", playerManager.getPlayerNames());
        
        return json;
    }
}
