/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Role;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author pulli
 */
public class InvestigateLoyaltyState implements GameState {

    private static final String HEADER = "Investigation of loyalty!";
    private static final String SUB_HEADER = "Supreme Chancellor, who do you wish to investigate?";
    private static final String INVESTIGATION_INFO = "Player %s is a %s!";

    private static final String EVENT_INVESTIGATION = "INVESTIGATION";
    private static final String EVENT_INVESTIGATION_HEADER = "Investigation of loyalty: ";
    private static final String EVENT_INVESTIGATION_SUBHEADER = "The acting Supreme Chancellor has received knowledge of a player's role.";

    Game game;
    public InvestigateLoyaltyState(Game game) {
        this.game = game;
    }

    @Override
    public void doAction() {
        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
        Map<String, String> choices = game.getPlayerManager().getPlayers().stream()
                .filter(player -> !player.getName().equals(supremeChancellor.getName()))
                .collect(Collectors.toMap(Player::getName, Player::getName));

        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), Arrays.asList(supremeChancellor), choices, HEADER, SUB_HEADER, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
        Player investigate = game.getPlayerManager().getPlayerByName(data);
        Role investigateRole = investigate.getRole();
        if (investigateRole.equals(Role.SHEEV_PALPATINE)) {
            investigateRole = Role.SEPARATIST;
        }
        String investigationInfo = String.format(INVESTIGATION_INFO, investigate.getName(), investigateRole.toString());
        game.getGamePlayerMessageActions().alertPlayer(supremeChancellor, HEADER, investigationInfo);
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_INVESTIGATION, EVENT_INVESTIGATION_HEADER + investigate.getName(), EVENT_INVESTIGATION_SUBHEADER);

        game.changeState(State.ROUND_START);
    }
}
