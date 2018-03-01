/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.PlayerManager;

import java.util.ArrayList;

/**
 *
 * @author pulli
 */
public class GameStartState implements GameState {

    private static final String IS_SUPREME_CHANCELLOR_TEXT = "You are the Supreme Chancellor!";

    private Game game;
    private Player supremeChancellor;

    public GameStartState(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {
        // do nothing
    }

    @Override
    public int sendData() {
        return 0;
    }

    @Override
    public void receiveData(String player, String data) {
        PlayerManager playerManager = game.getPlayerManager();
        Integer playersExpected = playerManager.getGamePlayers();

        if (playerManager.getPlayers().size() == playersExpected) {
            supremeChancellor = playerManager.getRandomPlayer();
            game.getVariables().setSupremeChancellor(supremeChancellor);
            playerManager.setPlayersIncludingExecuted(new ArrayList<>(playerManager.getPlayers()));
            game.setGameStarted();
            game.stateStatusUpdate(State.NOMINATE_VICE_CHAIR);
        }
    }



    @Override
    public void sendEndMessages() {
        game.getGamePlayerMessageActions().setSpecialRole(supremeChancellor, IS_SUPREME_CHANCELLOR_TEXT);
    }

}
