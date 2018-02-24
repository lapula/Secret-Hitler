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
    public GameStartState(Game game) {
        this.game = game;
    }
    
    @Override
    public void doAction() {

    }

    @Override
    public void receiveData(String player, String data) {
        PlayerManager playerManager = game.getPlayerManager();
        //Player newPlayer = playerManager.getPlayerByName(player);
        //game.getGamePlayerMessageActions().initPlayer(newPlayer, newPlayer.getRole());

        Integer playersExpected = playerManager.getGamePlayers();
        System.out.println(playerManager.getPlayers().size());
        System.out.println(playersExpected);
        if (playerManager.getPlayers().size() == playersExpected) {
            Player supremeChancellor = playerManager.getRandomPlayer();
            game.getVariables().setSupremeChancellor(supremeChancellor);
            game.getGamePlayerMessageActions().setSpecialRole(supremeChancellor, IS_SUPREME_CHANCELLOR_TEXT);
            playerManager.setPlayersIncludingExecuted(new ArrayList<>(playerManager.getPlayers()));
            game.setGameStarted();
            game.changeState(State.NOMINATE_VICE_CHAIR);
        }
    }
    
}
