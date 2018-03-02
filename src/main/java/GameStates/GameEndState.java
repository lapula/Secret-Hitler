/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.GameVariables;
import GameLogic.Player;
import GameLogic.Role;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author pulli
 */
public class GameEndState implements GameState {

    private static final String HEADER = "Game ended!";
    protected static final String LOYALIST_WIN_CARDS = "Loyalists win by passing all required policies! Democracy has prevailed in the republic!";
    protected static final String LOYALIST_WIN_PALPATINE = "Loyalists win by successfully assassinating the Sith Lord Sheev Palpatine!";
    protected static final String SEPARATISTS_WIN_CARDS = "Separatists win by passing all required policies! The separatist planets will secede from the republic!";
    protected static final String SEPARATISTS_WIN_PALPATINE = "Separatists win by electing Palpatine as Vice Chair! He is now in position to take over the senate.";

    private static final String EVENT_END = "END";

    private Game game;
    private GameVariables gameVariables;
    protected String subheader;

    public GameEndState(Game game) {
        this.game = game;
        gameVariables = game.getVariables();
        subheader = "";
    }
    
    @Override
    public void doAction() {
        List<Player> palpatineInGame = game.getPlayerManager().getPlayers().stream()
                .filter(player -> player.getRole().equals(Role.SHEEV_PALPATINE))    
                .collect(Collectors.toList());
        
        if (this.gameVariables.getSeparatistPolicyCount() >= 6) {
            subheader = SEPARATISTS_WIN_CARDS;
        }
        if (this.gameVariables.getViceChair().map(Player::getRole).orElse(Role.LOYALIST).equals(Role.SHEEV_PALPATINE)
                && this.gameVariables.getSeparatistPolicyCount() > 3
                && game.getGameStateType().orElse(State.GAME_START).equals(State.LEGISTLATIVE_SESSION)) {
            subheader = SEPARATISTS_WIN_PALPATINE;
        }
        if (this.gameVariables.getLoyalistPolicyCount() >= 5) {
            subheader = LOYALIST_WIN_CARDS;
        }
        if (palpatineInGame.size() == 0) {
            subheader = LOYALIST_WIN_PALPATINE;
        }
        game.setTimestamp(new Date().getTime());
    }

    @Override
    public int sendData() {
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), new ArrayList<Player>(), new HashMap<String, String>(), HEADER, "", game.getGameStateType().toString());
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_END, HEADER, subheader);
        return 0;
    }

    @Override
    public void receiveData(String player, String data) {
        // Do nothing
    }

    @Override
    public void sendEndMessages() {
        // Do nothing
    }

}
