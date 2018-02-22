/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameStates.State;
import SithImperative.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author pulli
 */
public class PlayerManager {

    private static final String IS_SUPREME_CHANCELLOR_TEXT = "You are the Supreme Chancellor!";
    private static final String LOYALIST = "LOYALIST";
    private static final String SEPARATIST = "SEPARATIST";
    private static final String SUPREME_CHANCELLOR = "SUPREME_CHANCELLOR";
    
    private List<Player> playersIncludingExecuted;
    private List<Player> players;
    private Game game;
    private int gamePlayers;
    private List<Role> roleList;
    
    public PlayerManager(Game game) {
        this.players = new ArrayList<>();
        this.game = game;
        this.gamePlayers = game.getVariables().getGamePlayers();
        this.roleList = initRoles(gamePlayers);
    }
    
    public void addPlayer(Player player) {
        player.setRole(this.getRole());
        players.add(player);
        PlayerWebSocketHandler.initPlayer(player, player.getRole());
        System.out.println(players.size());
        System.out.println(this.gamePlayers);
        if (players.size() == this.gamePlayers) {
            Player supremeChancellor = getRandomPlayer();
            game.getVariables().setSupremeChancellor(supremeChancellor);
            PlayerWebSocketHandler.setSpecialRole(supremeChancellor, IS_SUPREME_CHANCELLOR_TEXT);
            playersIncludingExecuted = new ArrayList<>(players);
            game.changeState(State.NOMINATE_VICE_CHAIR);
        }
    }
    
    public Player getNextPlayer(Player player) {
        int oldIndex = players.indexOf(player);
        if (oldIndex == players.size() - 1) {
            oldIndex = -1;
        }
        return players.get(oldIndex + 1);
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public List<Player> getPlayersIncludingExecuted() {
        return playersIncludingExecuted;
    }
    
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            names.add(players.get(i).getName());
        }
        return names;
    }
    
    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    
    public Player getPlayerSession(Session session) {
        for (Player p : players) {
            if (p.getSession().equals(session)) {
                return p;
            }
        }
        return null;
    }
    
    public Player getRandomPlayer() {
        Random random = new Random();
        int index = random.nextInt(gamePlayers - 1);
        return players.get(index);
    }
    
    private Role getRole() {
        Random random = new Random();
        int index = random.nextInt(this.roleList.size());
        Role role = this.roleList.get(index);
        this.roleList.remove(index);
        return role;
    }
    
    private List<Role> initRoles(int gamePlayers) {
        int loyalists;
        
        switch(gamePlayers) {
        case 5 :
            loyalists = 3;
        case 6 :
            loyalists = 4;
        case 7 :
            loyalists = 4;
        case 8 :
            loyalists = 4;
        case 9 :
            loyalists = 4;
        case 10 :
            loyalists = 4;
        default :
            loyalists = 1;
     }
        
     int separatists = gamePlayers - loyalists - 1;
     
     List<Role> roles = new ArrayList<>();
     roles.add(Role.SHEEV_PALPATINE);

     for (int i = 0; i < separatists; i++) {
         roles.add(Role.SEPARATIST);
     }

     for (int i = 0; i < loyalists; i++) {
         roles.add(Role.LOYALIST);
     }
        
     return roles;
    }
    
}
