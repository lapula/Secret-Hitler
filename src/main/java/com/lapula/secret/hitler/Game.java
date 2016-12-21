/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapula.secret.hitler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pulli
 */
class Game {
    
    private int numberOfPlayers;
    private HashMap<String, Player> players;
    
    public Game(int numberOfPlayers) {
        players = new HashMap<>();
        this.numberOfPlayers = numberOfPlayers;
    }
    
    public void addPlayer(String name, Player player) {
        players.put(name, player);
        
        if (players.size() == numberOfPlayers) {
            initGame();
            List<Player> targets = new ArrayList(players.values());
            PlayerWebSocketHandler.sendChoiceMessage(targets, new String[]{"a","b","c"});
        }
    }
    
    public void initGame() {
        
    }
}
