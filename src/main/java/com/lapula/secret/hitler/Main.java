package com.lapula.secret.hitler;

import GameLogic.Game;
import org.eclipse.jetty.websocket.api.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import spark.ModelAndView;
import static spark.Spark.*;

public class Main {
    
    static Map<String, Game> games = new ConcurrentHashMap<>();
    
    
    public static void main(String[] args) {
        
        Game initialGame = new Game(2);
        games.put("gg", initialGame);
        
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        webSocket("/players", PlayerWebSocketHandler.class);
        init();
        
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "client");
        }, new ThymeleafTemplateEngine());
        
        get("/refresh", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            Game game = new Game(2);
            games.put("gg", game);
            return new ModelAndView(model, "game");
        }, new ThymeleafTemplateEngine());
    }
    
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
