/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author pulli
 */
public class Player implements Comparable<Player> {
    
    private String name;
    private String gameName;
    private Session session;
    private Role role;
    
    public Player() {}

    public Player(String name, String gameName, Session session) {
        this.name = name;
        this.gameName = gameName;
        this.session = session;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int compareTo(Player comparePlayer) {

        return this.getName().compareTo(comparePlayer.getName());
    }

}
