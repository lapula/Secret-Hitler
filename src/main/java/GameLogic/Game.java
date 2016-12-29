/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameStates.State;
import GameStates.NominateChancellor;
import GameStates.GameState;
import GameStates.StateFactory;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;


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
    
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public GameVariables getVariables() {
        return this.gameVariables;
    }
    
    public PolicyDeck getPolicyDeck() {
        return this.policyDeck;
    }
    
    public void changeState(State state) {
        this.gameState =  this.stateFactory.getGameState(this, state);
        this.gameState.doAction();
   }
    
    public void receiveData(String player, String data) {
        this.gameState.receiveData(player, data);
    }
    
    public void addGameListener(Session session) {
        this.gameListeners.add(session);
    }
    
    public List<Session> getGameListeners() {
        return this.gameListeners;
    }
}
