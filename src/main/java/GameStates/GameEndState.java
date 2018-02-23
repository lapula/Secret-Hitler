/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import SocketInterface.PlayerWebSocketActions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author pulli
 */
public class GameEndState implements GameState {

    private static final String HEADER = "Game ended!";

    private Game game;
    public GameEndState(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        PlayerWebSocketActions.sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), new ArrayList<Player>(), new HashMap<String, String>(), HEADER, "", game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        // Do nothing
    }
    
}
