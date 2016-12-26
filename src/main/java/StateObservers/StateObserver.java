/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateObservers;

import GameLogic.Game;

/**
 *
 * @author pulli
 */
public interface StateObserver {
    
    public void informObserver(Game game);
    
}
