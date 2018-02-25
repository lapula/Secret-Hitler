/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author pulli
 */
public class NominateViceChair implements GameState {

    private static final String HEADER = "The Supreme Chancellor is nominating his Vice Chair!";
    private static final String SUB_HEADER = "Supreme Chancellor, who do you wish to nominate?";

    private static final String EVENT_NOMINATION = "NOMINATION";
    private static final String EVENT_NOMINATION_HEADER = "A Vice Chair has been nominated!";
    private static final String EVENT_NOMINATION_SUBHEADER = "Representatives will vote to determine legality of the government.";

    private Game game;
    public NominateViceChair(Game game) {
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
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), target, choices, HEADER, SUB_HEADER, game.getGameStateType().toString());
    }

    @Override
    public void receiveData(String player, String data) {
        if (game.getPlayerManager().getPlayerByName(data) != null) {
            Player viceChair = game.getPlayerManager().getPlayerByName(data);
            game.getVariables().setViceChair(viceChair);
            game.changeState(State.VOTE_ON_GOVERNMENT);
        }

        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_NOMINATION, EVENT_NOMINATION_HEADER, EVENT_NOMINATION_SUBHEADER);
    }
}
