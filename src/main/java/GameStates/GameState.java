/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author pulli
 */
public interface GameState {
    
    public void doAction();
    public void receiveData(String player, String data);
    public int sendData();
    public void sendEndMessages();
    
}
