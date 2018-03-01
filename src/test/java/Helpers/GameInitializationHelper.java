package Helpers;

import GameLogic.Game;
import GameLogic.GameVariables;
import GameLogic.PlayerManager;
import GameStates.State;

import java.util.Optional;
import java.util.stream.IntStream;

public class GameInitializationHelper {

    public static Game getInitializedGame(int gamePlayers, Integer supremeChancellorIndex, Integer viceChairIndex) {
        Game game = new Game("test", gamePlayers);
        IntStream.range(0, gamePlayers).forEach(i -> game.getPlayerManager().addNewPlayer("P" + i, null));
        GameVariables gameVariables = game.getVariables();
        PlayerManager playerManager = game.getPlayerManager();

        gameVariables.cleanElectionResults();
        gameVariables.setSenateVotesThisRound(0);
        Optional.ofNullable(supremeChancellorIndex).ifPresent(i -> gameVariables.setSupremeChancellor(playerManager.getPlayers().get(i)));
        Optional.ofNullable(viceChairIndex).ifPresent(i -> gameVariables.setViceChair(playerManager.getPlayers().get(i)));

        gameVariables.setVetoedPolicies(null);

        return game;
    }
}
