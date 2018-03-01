/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author pulli
 */
public class PlayerManager {

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

    public Player addNewPlayer(String playerName, Session user) {
        Player player = new Player(playerName, game.getGameName(), user, this.assignRole());
        players.add(player);
        playersIncludingExecuted.add(player);
        Collections.sort(players);
        Collections.sort(playersIncludingExecuted);
        return player;
    }

    public void reconnectPlayer(String playerName, Session user) {
        this.getPlayerByName(playerName).ifPresent(p -> p.setSession(user));
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

    public void setPlayersIncludingExecuted(List<Player> playersIncludingExecuted) {
        this.playersIncludingExecuted = playersIncludingExecuted;
    }
    
    public List<String> getPlayerNames() {
        return players.stream().map(p -> p.getName()).collect(Collectors.toList());
    }

    public Optional<Player> getPlayerByName(String name) {
        return players.stream().filter(p -> p.getName().equals(name)).findFirst();
    }
    
    public Optional<Player> getPlayerBySession(Session session) {
        return players.stream().filter(p -> p.getSession().equals(session)).findFirst();
    }
    
    public Player getRandomPlayer() {
        return players.get(new Random().nextInt(gamePlayers - 1));
    }
    
    private Role assignRole() {
        int index = new Random().nextInt(this.roleList.size());
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
            case 5 : loyalists = 3; break;
            case 6 : loyalists = 4; break;
            case 7 : loyalists = 4; break;
            case 8 : loyalists = 5; break;
            case 9 : loyalists = 5; break;
            case 10 : loyalists = 6; break;
            default : loyalists = 1;
        }
        
     int separatists = gamePlayers - loyalists - 1;
     List<Role> roles = new ArrayList<>();
     roles.add(Role.SHEEV_PALPATINE);
     IntStream.range(0, separatists).forEach(i -> roles.add(Role.SEPARATIST));
     IntStream.range(0, loyalists).forEach(i -> roles.add(Role.LOYALIST));
     Collections.shuffle(roles);

     return roles;
    }
    
}
