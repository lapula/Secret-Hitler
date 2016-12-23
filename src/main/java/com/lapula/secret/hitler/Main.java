package com.lapula.secret.hitler;

import org.eclipse.jetty.websocket.api.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import spark.ModelAndView;
import static spark.Spark.*;

public class Main {
    
    static Map<String, Game> games = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        webSocket("/players", PlayerWebSocketHandler.class);
        init();
        
        get("/", (request, response) -> {
            Game game = new Game(2);
            games.put("jammailu", game);
            return "test game created";
        });
        
        get("/game/:name", (request, response) -> {
            Game game = new Game(2);
            games.put(request.params(":name"), game);
            System.out.println(request.params(":name"));
            response.redirect("/game");
            return null;
        });
        
        get("/game", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "game");
        }, new ThymeleafTemplateEngine());
        
        get("/client", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "client");
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
