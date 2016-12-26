/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameLogic.State;
import StateObservers.ElectionObserver;
import StateObservers.StateObserver;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class Game {
    
    private PlayerManager playerManager;
    private State state;
    private Map<String, String> stateVariables;
    private List<StateObserver> stateObservers;
    
    public Game(Integer numberOfPlayers) {
        this.state = State.STARTING_GAME;
        this.stateVariables = new HashMap<>();
        this.stateVariables.put("numberOfPlayers", numberOfPlayers.toString());
        playerManager = new PlayerManager(this);
        stateObservers = new LinkedList<>();
        initObservers();
    }
    
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public State getState() {
        return this.state;
    }
    
    public Map<String, String> getVariables() {
        return this.stateVariables;
    }
    
    public void changeState(State state, Map<String, String> variables) {
        this.state = state;
        if (variables != null) {
            stateVariables.putAll(variables);
        }
        notifyAllObservers();
    }
    
    public void notifyAllObservers() {
        stateObservers.forEach(observer -> {
            observer.informObserver(this);
        });
    }
    
    public void initObservers() {
        stateObservers.add(new ElectionObserver());
    }
}
