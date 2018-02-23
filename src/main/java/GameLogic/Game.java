/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameStates.State;
import GameStates.GameState;
import GameStates.StateFactory;
import SocketInterface.GameWebSocketHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


public class Game {
    
    private StateFactory stateFactory;
    private PlayerManager playerManager;
    private GameState gameState;

    private State gameStateType;
    private GameVariables gameVariables;
    private PolicyDeck policyDeck;
    private List<Session> gameListeners;

    private boolean gameStarted;
    
    public Game(Integer numberOfPlayers) {
        
        this.gameListeners = new LinkedList<>();
        this.stateFactory = new StateFactory();
        this.gameVariables = new GameVariables();
        this.policyDeck = new PolicyDeck();
        gameStarted = false;
        
        this.gameVariables.setGamePlayers(numberOfPlayers);
        playerManager = new PlayerManager(this);
        this.changeState(State.GAME_START);
    }
    
    public void changeState(State state) {
        if (checkGameEndConditions()) {
            this.gameState = this.stateFactory.getGameState(this, State.GAME_END);
        } else {
            this.gameState =  this.stateFactory.getGameState(this, state);
        }
        this.gameStateType = state;
        this.gameState.doAction();
        GameWebSocketHandler.sendStatusUpdate(this.gameListeners, this.toJSON());

   }
    
    public void receiveData(String player, String data) {
        this.gameState.receiveData(player, data);
        GameWebSocketHandler.sendStatusUpdate(this.gameListeners, this.toJSON());
    }
    
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public GameVariables getVariables() {
        return this.gameVariables;
    }
    
    public PolicyDeck getPolicyDeck() {
        return this.policyDeck;
    }
    
    public void addGameListener(Session session) {
        this.gameListeners.add(session);
    }
    
    public List<Session> getGameListeners() {
        return this.gameListeners;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted() {
        this.gameStarted = true;
    }

    public State getGameStateType() {
        return gameStateType;
    }


    private boolean checkGameEndConditions() {
        if (!gameStarted) {
            return false;
        }
        if (this.gameVariables.getSeparatistPolicyCount() >= 6) {
            return true;
        }
        if (this.gameVariables.getViceChair() != null) {
            if (this.gameVariables.getViceChair().getRole().equals(Role.SHEEV_PALPATINE)
                    && this.gameVariables.getSeparatistPolicyCount() >= 3
                    && !this.gameState.equals(State.VOTE_ON_GOVERNMENT)
                    && !this.gameState.equals(State.CALL_SPECIAL_ELECTION)) {
                return true;
            }
        }
        if (this.gameVariables.getLoyalistPolicyCount() >= 5) {
            return true;
        }
        List<Player> palpatineInGame = this.playerManager.getPlayers().stream()
                .filter(player -> player.getRole().equals(Role.SHEEV_PALPATINE))
                .collect(Collectors.toList());
        if (palpatineInGame.size() == 0) {
            return true;
        }
        return false;
    }

    public JSONObject toJSON() {

        String supremeChancellorName = "";
        String viceChairName = "";
        JSONObject electionResults = new JSONObject(gameVariables.getElectionResults());

        if (gameVariables.getSupremeChancellor() != null) {
            supremeChancellorName = gameVariables.getSupremeChancellor().getName();
        }
        if (gameVariables.getViceChair() != null) {
            viceChairName = gameVariables.getViceChair().getName();
        }
        System.out.println(playerManager.getPlayers().toString());
        JSONObject json = new JSONObject();
        json.put("supremeChancellor", supremeChancellorName);
        json.put("viceChair", viceChairName);
        json.put("gamePlayers", gameVariables.getGamePlayers());
        json.put("governmentVotesThisRound", gameVariables.getSenateVotesThisRound());
        json.put("loyalistPoliciesPassed", gameVariables.getLoyalistPolicyCount());
        json.put("separatistPoliciesPassed", gameVariables.getSeparatistPolicyCount());
        json.put("electionResults", electionResults);
        json.put("players", playerManager.getPlayerNames());
        json.put("cardsInDeck", this.getPolicyDeck().getDeckCardsCount());
        
        return json;
    }
}
