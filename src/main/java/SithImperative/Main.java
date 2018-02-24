package SithImperative;

import GameLogic.Game;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import GameMessagingInterface.GamePlayerWebSocketInterface;
import SocketInterface.GameWebSocketHandler;
import spark.ModelAndView;
import static spark.Spark.*;

public class Main {

    public static Map<String, Game> games = new ConcurrentHashMap<>();


    public static void main(String[] args) {

        //For debugging
        Game initialGame = new Game(2);
        games.put("gg", initialGame);

        // Init spark and websockets
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        webSocket("/players", GamePlayerWebSocketInterface.class);
        webSocket("/games", GameWebSocketHandler.class);
        init();

        // Main app path
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index");
        }, new ThymeleafTemplateEngine());

        // Debugging stats path
        get("/stats", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("freeMemory", "" + (Runtime.getRuntime().freeMemory() / 1000000));
            model.put("totalMemory", "" + (Runtime.getRuntime().totalMemory() / 1000000));
            model.put("runningGames", games);

            return new ModelAndView(model, "stats");
        }, new ThymeleafTemplateEngine());
    }

    //return default port if heroku-port isn't set (i.e. on localhost)
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
