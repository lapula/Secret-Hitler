/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author pulli
 */
public class PlayerManager {


    public void setPlayersIncludingExecuted(List<Player> playersIncludingExecuted) {
        this.playersIncludingExecuted = playersIncludingExecuted;
    }

    private List<Player> playersIncludingExecuted;
    private List<Player> players;
    private Game game;

    private int gamePlayers;
    private List<Role> roleList;
    
    public PlayerManager(Game game) {
        this.players = new ArrayList<>();
        this.playersIncludingExecuted = new ArrayList<>();
        this.game = game;
        this.gamePlayers = game.getVariables().getGamePlayers();
        this.roleList = initRoles(gamePlayers);
    }

    //TODO game should have gameName
    public Player addNewPlayer(String gameName, String playerName, Session user) {
        Player player = new Player(playerName, gameName, user);
        player.setRole(this.getRole());
        players.add(player);
        playersIncludingExecuted.add(player);
        Collections.sort(players);
        Collections.sort(playersIncludingExecuted);
        return player;
    }

    public void reconnectPlayer(String playerName, Session user) {
        this.getPlayerByName(playerName).setSession(user);
    }
    
    public Player getNextPlayer(Player player) {
        int nextIndex = playersIncludingExecuted.indexOf(player) + 1;
        nextIndex = (nextIndex == playersIncludingExecuted.size()) ? 0 : nextIndex;

        Player nextPlayer = playersIncludingExecuted.get(nextIndex);
        if (players.contains(nextPlayer)) {
            return playersIncludingExecuted.get(nextIndex);
        } else {
            return getNextPlayer(nextPlayer);
        }
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

    public int getGamePlayers() {
        return gamePlayers;
    }
    
    private List<Role> initRoles(int gamePlayers) {
        int loyalists;
        
        switch(gamePlayers) {
        case 5 :
            loyalists = 3;break;
        case 6 :
            loyalists = 4;break;
        case 7 :
            loyalists = 4;break;
        case 8 :
            loyalists = 4;break;
        case 9 :
            loyalists = 4;break;
        case 10 :
            loyalists = 4;break;
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

     Collections.shuffle(roles);

     return roles;
    }
    
}
