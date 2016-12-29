/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameStates.State;
import com.lapula.secret.hitler.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author pulli
 */
public class PlayerManager {
    
    private List<Player> players;
    private Game game;
    private int gamePlayers;
    private List<String> roleList;
    
    public PlayerManager(Game game) {
        players = new ArrayList<>();
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
            Player president = getRandomPlayer();
            game.getVariables().setPresident(president);
            PlayerWebSocketHandler.setSpecialRole(president, "You are the president!");
            game.changeState(State.NOMINATE_CHANCELLOR);
        }
    }
    
    public Player getNextPlayer(Player player) {
        System.out.println(player.getName());
        int oldIndex = players.indexOf(player);
        if (oldIndex == players.size() - 1) {
            oldIndex = -1;
        }
        System.out.println(oldIndex);
        System.out.println(players.get(oldIndex + 1).getName());
        return players.get(oldIndex + 1);
    }
    
    public List<Player> getPlayers() {
        return players;
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
    
    public Player getRandomPlayer() {
        Random random = new Random();
        int index = random.nextInt(gamePlayers - 1);
        return players.get(index);
    }
    
    private String getRole() {
        Random random = new Random();
        int index = random.nextInt(this.roleList.size());
        String role = this.roleList.get(index);
        this.roleList.remove(index);
        return role;
    }
    
    private List<String> initRoles(int gamePlayers) {
        
        String liberal = "LIBERAL";
        String fascist = "FASCIST";
        String hitler = "HITLER";
        
        int liberals;
        
        switch(gamePlayers) {
        case 5 :
            liberals = 3;
        case 6 :
            liberals = 4;
        case 7 :
            liberals = 4;
        case 8 :
            liberals = 4;
        case 9 :
            liberals = 4;
        case 10 :
            liberals = 4;
        default :
            liberals = 1;
     }
        
     int fascists = gamePlayers - liberals - 1;
     
     List<String> roles = new ArrayList<>();
     roles.add(hitler);
     
     for (int i = 0; i < fascists; i++) {
         roles.add(fascist);
     }
     
     for (int i = 0; i < liberals; i++) {
         roles.add(liberal);
     }
        
     return roles;
    }
    
}
