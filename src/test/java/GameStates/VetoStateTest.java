package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import Helpers.GameInitializationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VetoStateTest {

    private Game game;
    private VetoState vetoState;
    private LegistlativeSessionState legistlativeSessionState;

    @BeforeEach
    public void beforeEach() {
        game = GameInitializationHelper.getInitializedGame(6, 0, 1);
        legistlativeSessionState = new LegistlativeSessionState(game);
        vetoState = new VetoState(game);
    }

    @Test
    void testActionProposingVeto() {
        vetoState.doAction();
        assertEquals(2, vetoState.choices.size());
        assertEquals(0, vetoState.policyIdMapper.size());
        assertEquals(game.getVariables().getSupremeChancellor().get(), vetoState.legistlator);
    }

    @Test
    void testReceiveDataProposingNo() {
        vetoState.doAction();
        vetoState.receiveData(vetoState.legistlator.getName(), VetoState.NO);
        assertFalse(game.getGameStateType().isPresent());
    }

    @Test
    void testReceiveDataProposingYes() {
        vetoState.doAction();
        vetoState.receiveData(vetoState.legistlator.getName(), VetoState.YES);
        assertEquals(State.NOMINATE_VICE_CHAIR, game.getGameStateType().orElse(null));
    }

    @Test
    void testActionNoVeto() {
        IntStream.range(0, 5).forEach(i -> game.getVariables().addSeparatistPolicy());
        legistlativeSessionActionAndReceiveData(2);
        vetoState = new VetoState(game);

        vetoState.doAction();
        vetoState.receiveData(vetoState.legistlator.getName(), VetoState.NO);
        vetoState.doAction();

        assertEquals(2, vetoState.choices.size());
        assertEquals(2, vetoState.policyIdMapper.size());
        assertEquals(game.getVariables().getViceChair().get(), vetoState.legistlator);
    }

    @Test
    void receiveDataNoVeto() {
        IntStream.range(0, 5).forEach(i -> game.getVariables().addSeparatistPolicy());
        legistlativeSessionActionAndReceiveData(2);
        vetoState = new VetoState(game);

        vetoState.doAction();
        vetoState.receiveData(vetoState.legistlator.getName(), VetoState.NO);
        vetoState.doAction();
        vetoState.receiveData(vetoState.legistlator.getName(), vetoState.policyIdMapper.entrySet().iterator().next().getKey());

        assertEquals(1, vetoState.vetoedPolicies.size());
        assertTrue(game.getVariables().getSeparatistPolicyCount() == 6
                || game.getVariables().getLoyalistPolicyCount() == 1);

        assertEquals(State.DETERMINE_EXECUTIVE_ACTION, game.getGameStateType().orElse(null));
    }

    private void legistlativeSessionActionAndReceiveData(int times) {
        IntStream.range(0, times).forEach(i -> {
            legistlativeSessionState.doAction();
            legistlativeSessionState.receiveData(legistlativeSessionState.legistlator.getName(),
                    legistlativeSessionState.policyIdMapper.entrySet().iterator().next().getKey());
        });
    }
    /*
    @Override
    public void receiveData(String player, String data) {
        if (proposingVeto) {
            if (data.equals(NO)) {
                proposingVeto = false;
                vetoUnused = true;
                game.stateStatusUpdate(null);
            } else {
                game.stateStatusUpdate(State.ROUND_START);
            }
        } else {
            Policy discard = policyIdMapper.get(data).equals(Policy.LOYALIST_POLICY.toString()) ? Policy.LOYALIST_POLICY : Policy.SEPARATIST_POLICY;
            policyIdMapper.remove(data);
            vetoedPolicies.remove(discard);

            if (vetoedPolicies.get(0).equals(Policy.LOYALIST_POLICY)) {
                game.getVariables().addLoyalistPolicy();
            } else {
                game.getVariables().addSeparatistPolicy();
            }
            game.stateStatusUpdate(State.DETERMINE_EXECUTIVE_ACTION);
        }
    }
     */

}