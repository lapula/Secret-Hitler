/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Role;
import SocketInterface.PlayerWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class InvestigateLoyaltyState implements GameState {

    private static final String HEADER = "Investigation of loyalty!";
    private static final String SUB_HEADER = "Supreme Chancellor, who do you wish to investigate?";

    Game game;
    public InvestigateLoyaltyState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        Map<String, String> choices = new HashMap<>();
        
        game.getPlayerManager().getPlayers().forEach(player -> {
            if (!player.getName().equals(supremeChancellor.getName())) {
                choices.put(player.getName(), player.getName());
            }
        });
        
        List<Player> target = new ArrayList<>();
        target.add(supremeChancellor);
        PlayerWebSocketHandler.sendChoiceMessage(game.getPlayerManager().getPlayers(), target, choices, HEADER, SUB_HEADER);
    }

    @Override
    public void receiveData(String player, String data) {
        Player supremeChancellor = game.getVariables().getSupremeChancellor();
        Player investigate = game.getPlayerManager().getPlayerByName(data);
        Role investigateRole = investigate.getRole();
        if (investigateRole.equals(Role.SHEEV_PALPATINE)) {
            investigateRole = Role.SEPARATIST;
        }
        String investigationInfo = "Player " + investigate.getName() + " is a " + investigateRole.toString() + "!";
        if (investigate != null) {
            PlayerWebSocketHandler.alertPlayer(supremeChancellor, HEADER, investigationInfo);
            game.changeState(State.ROUND_START);
        }
    }
}
