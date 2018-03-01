package GameStates;

import GameLogic.Game;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class LegistlativeSessionStateTest {


    private Game game;
    private LegistlativeSessionState legistlativeSessionState;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        legistlativeSessionState = new LegistlativeSessionState(game);
        game.stateStatusUpdate(State.LEGISTLATIVE_SESSION);
    }

    @Test
    void doActionThreePolicies() {
        legistlativeSessionState.doAction();

        assertEquals(3, legistlativeSessionState.policyIdMapper.size());
        assertEquals(3, legistlativeSessionState.policies.size());
        assertEquals(game.getVariables().getSupremeChancellor().get(), legistlativeSessionState.legistlator);
    }

    @Test
    void ReceiveDataThreePolicies() {
        actionAndReceiveData(1);

        assertEquals(2, legistlativeSessionState.policyIdMapper.size());
        assertEquals(2, legistlativeSessionState.policies.size());
        assertFalse(game.getGameStateType().isPresent());

    }

    @Test
    void doActionTwoPolicies() {
        actionAndReceiveData(1);
        legistlativeSessionState.doAction();

        assertEquals(2, legistlativeSessionState.policyIdMapper.size());
        assertEquals(2, legistlativeSessionState.policies.size());
        assertEquals(game.getVariables().getViceChair().get(), legistlativeSessionState.legistlator);
    }

    @Test
    void ReceiveDataTwoPolicies() {
        actionAndReceiveData(2);

        assertEquals(1, legistlativeSessionState.policyIdMapper.size());
        assertEquals(1, legistlativeSessionState.policies.size());
        assertTrue(game.getVariables().getSeparatistPolicyCount() == 1
                || game.getVariables().getLoyalistPolicyCount() == 1);

        assertEquals(State.DETERMINE_EXECUTIVE_ACTION, game.getGameStateType().orElse(null));
    }

    @Test
    void doActionTwoPoliciesWithVeto() {
        IntStream.range(0, 5).forEach(i -> game.getVariables().addSeparatistPolicy());
        actionAndReceiveData(1);
        legistlativeSessionState.doAction();

        assertEquals(3, legistlativeSessionState.policyIdMapper.size());
        assertEquals(2, legistlativeSessionState.policies.size());
        assertEquals(LegistlativeSessionState.VETO_PROPOSAL, legistlativeSessionState.policyIdMapper.get(LegistlativeSessionState.VETO));
        assertEquals(game.getVariables().getViceChair().get(), legistlativeSessionState.legistlator);
    }

    @Test
    void receiveDataTwoPoliciesWithVeto() {
        IntStream.range(0, 5).forEach(i -> game.getVariables().addSeparatistPolicy());
        actionAndReceiveData(2);

        assertEquals(State.VETO, game.getGameStateType().orElse(null));
        assertEquals(legistlativeSessionState.policies, game.getVariables().getVetoedPolicies());
    }

    private void actionAndReceiveData(int times) {
        IntStream.range(0, times).forEach(i -> {
            legistlativeSessionState.doAction();
            legistlativeSessionState.receiveData(legistlativeSessionState.legistlator.getName(),
                    legistlativeSessionState.policyIdMapper.entrySet().iterator().next().getKey());
        });
    }
}