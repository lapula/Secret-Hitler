package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VoteOnGovernmentStateTest {

    private Game game;
    private VoteOnGovernmentState voteState;
    private int gamePlayers;

    @BeforeEach
    public void beforeEach() {
        gamePlayers = 6;
        game = GameInitializationHelper.getInitializedGame(gamePlayers, 0, 1);
        game.stateStatusUpdate(State.VOTE_ON_GOVERNMENT);
        voteState = new VoteOnGovernmentState(game);

    }

    @Test
    void doAction() {
        voteState.doAction();
        assertEquals(game.getVariables().getSenateVotesThisRound(), 1);
    }

    @Test
    void receiveYesData() {
        String supremeChancellorName = game.getVariables().getSupremeChancellor().map(Player::getName).orElse("");
        String viceChairName = game.getVariables().getViceChair().map(Player::getName).orElse("");
        voteState.doAction();
        IntStream.range(0, gamePlayers).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.YES));
        long yesVotes = game.getVariables().getElectionResults().values().stream().filter(v -> v.equals(VoteOnGovernmentState.YES)).count();

        assertEquals(1, game.getVariables().getSenateVotesThisRound());
        assertEquals((long) gamePlayers, yesVotes);
        assertEquals(State.LEGISTLATIVE_SESSION.toString(), game.getGameStateType().map(State::toString).orElse(""));
        assertEquals(supremeChancellorName, game.getVariables().getPreviousSupremeChancellor().map(Player::getName).orElse(""));
        assertEquals(viceChairName, game.getVariables().getPreviousViceChair().map(Player::getName).orElse(""));
    }

    @Test
    void receiveNoData() {
        voteState.doAction();
        IntStream.range(0, gamePlayers).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.NO));
        long noVotes = game.getVariables().getElectionResults().values().stream().filter(v -> v.equals(VoteOnGovernmentState.NO)).count();

        assertEquals(1, game.getVariables().getSenateVotesThisRound());
        assertEquals((long) gamePlayers, noVotes);
        assertEquals(State.NOMINATE_VICE_CHAIR.toString(), game.getGameStateType().map(State::toString).orElse(""));
        assertFalse(game.getVariables().getViceChair().isPresent());
        assertEquals(game.getPlayerManager().getPlayers().get(1).getName(),
                game.getVariables().getSupremeChancellor().map(Player::getName).orElse(""));
        assertNotNull(voteState.nextSupremeChancellor);
    }

    @Test
    void receiveEqualData() {
        voteState.doAction();
        IntStream.range(0, gamePlayers / 2).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.NO));
        IntStream.range(gamePlayers / 2, gamePlayers).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.YES));
        long noVotes = game.getVariables().getElectionResults().values().stream().filter(v -> v.equals(VoteOnGovernmentState.NO)).count();
        long yesVotes = game.getVariables().getElectionResults().values().stream().filter(v -> v.equals(VoteOnGovernmentState.YES)).count();

        assertEquals(1, game.getVariables().getSenateVotesThisRound());
        assertEquals((long) gamePlayers / 2, noVotes);
        assertEquals((long) gamePlayers / 2, yesVotes);
        assertEquals(State.NOMINATE_VICE_CHAIR.toString(), game.getGameStateType().map(State::toString).orElse(""));
        assertFalse(game.getVariables().getViceChair().isPresent());
        assertEquals(game.getPlayerManager().getPlayers().get(1).getName(),
                game.getVariables().getSupremeChancellor().map(Player::getName).orElse(""));
    }

    @Test
    void receiveNoThreeTimes() {
        game.getVariables().setPreviousSupremeChancellor(game.getPlayerManager().getPlayers().get(4));
        game.getVariables().setPreviousViceChair(game.getPlayerManager().getPlayers().get(3));

        voteState.doAction();
        IntStream.range(0, gamePlayers).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.NO));
        voteState.doAction();
        IntStream.range(0, gamePlayers).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.NO));
        voteState.doAction();
        IntStream.range(0, gamePlayers).forEach(i -> voteState.receiveData(game.getPlayerManager().getPlayers().get(i).getName(), VoteOnGovernmentState.NO));

        assertEquals(3, game.getVariables().getSenateVotesThisRound());
        assertTrue(game.getVariables().getSeparatistPolicyCount() == 1
                || game.getVariables().getLoyalistPolicyCount() == 1);
        assertEquals(State.ROUND_START.toString(), game.getGameStateType().map(State::toString).orElse(""));
        assertFalse(game.getVariables().getPreviousSupremeChancellor().isPresent());
        assertFalse(game.getVariables().getPreviousViceChair().isPresent());
        assertFalse(game.getVariables().hasExecutivePower());
    }
}