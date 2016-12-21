/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapula.secret.hitler;

import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author pulli
 */
public class Player {
    
    String name;
    String game;
    Session session;
    
    public Player() {}

    public Player(String name, Session session) {
        this.name = name;
        this.session = session;
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
}
