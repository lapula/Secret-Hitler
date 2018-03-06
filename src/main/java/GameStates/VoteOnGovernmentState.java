/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;
import GameLogic.Policy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pulli
 */
public class VoteOnGovernmentState implements GameState {

    private static final String YES_VOTE = "In favour!";
    private static final String NO_VOTE = "Against!";
    protected static final String YES = "YES";
    protected static final String NO = "NO";
    private static final String INFORM_VICE_CHAIR = "You are the Vice Chair!";
    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";
    private static final String VOTE_HEADER = "Vote!";
    private static final String VOTE_SUB_HEADER = "Do you approve this government?";

    private static final String EVENT_VOTE_FAIL = "VOTE_FAILED";
    private static final String EVENT_VOTE_FAIL_HEADER = "Vote failed!";
    private static final String EVENT_VOTE_FAIL_SUBHEADER = "The representatives were not able to form a government.";

    private static final String EVENT_ATTEMPTS_USED_SEPARATISTS = "ATTEMPTS_USED_SEPARATISTS";
    private static final String EVENT_ATTEMPTS_USED_LOYALISTS = "ATTEMPTS_USED_LOYALISTS";
    private static final String EVENT_ATTEMPTS_USED_HEADER = "Democracy failed!";
    private static final String EVENT_ATTEMPTS_USED_SUBHEADER = "The vote has failed three times, the forces of fate will decide next policy.";

    private static final String EVENT_LEGISTLATION_PROCESS = "LEGISTLATION_PROCESS";
    private static final String EVENT_LEGISTLATION_PROCESS_HEADER = "Silence!";
    private static final String EVENT_LEGISTLATION_PROCESS_SUBHEADER = "The government is in session.";

    private Game game;
    private Map<String, String> choices;
    private boolean governmentFormed;
    protected Player nextSupremeChancellor;
    private String attemptsUsedEventText;

    public VoteOnGovernmentState(Game game) {
        this.game = game;
        choices = new HashMap<>();
        governmentFormed = false;
        attemptsUsedEventText = "";
    }



    @Override
    public void doAction() {
        game.getVariables().cleanElectionResults();
        game.getVariables().setSenateVotesThisRound(game.getVariables().getSenateVotesThisRound() + 1);
        choices.put(YES, YES_VOTE);
        choices.put(NO, NO_VOTE);
    }

    @Override
    public int sendData() {
        return game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(),
                game.getPlayerManager().getPlayers(), choices, VOTE_HEADER, VOTE_SUB_HEADER, State.VOTE_ON_GOVERNMENT.toString());
    }
    
    @Override
    public void receiveData(String player, String data) {
        game.getVariables().addVote(player, data);
        if (game.getVariables().getElectionResults().size() == game.getPlayerManager().getPlayers().size()) {

            Collection<String> votes = game.getVariables().getElectionResults().values();
            long yesVotes = votes.stream().filter(v -> v.equals(YES)).count();
            long noVotes = votes.stream().filter(v -> v.equals(NO)).count();
            governmentFormed = yesVotes > noVotes;

            if (governmentFormed) {
                formGovernment();
            } else if (game.getVariables().getSenateVotesThisRound() == 3) {
                assignRandomPolicyStartNewRound();
            } else {
                startNewNomination();
            }
        }
    }


    @Override
    public void sendEndMessages() {
        if (governmentFormed) {
            game.getGamePlayerMessageActions().setSpecialRole(game.getVariables().getViceChair().get(), INFORM_VICE_CHAIR);
            game.getGameScreenMessageActions().sendGameEvent(
                    game.getGameListeners(), EVENT_LEGISTLATION_PROCESS, EVENT_LEGISTLATION_PROCESS_HEADER, EVENT_LEGISTLATION_PROCESS_SUBHEADER);

        } else if (game.getVariables().getSenateVotesThisRound() == 3) {
            game.getGameScreenMessageActions().sendGameEvent(
                    game.getGameListeners(), attemptsUsedEventText, EVENT_ATTEMPTS_USED_HEADER, EVENT_ATTEMPTS_USED_SUBHEADER);
        } else {
            game.getGamePlayerMessageActions().clearSpecialRoles(game.getPlayerManager().getPlayers(), nextSupremeChancellor);
            game.getGamePlayerMessageActions().setSpecialRole(nextSupremeChancellor, INFORM_SUPREME_CHANCELLOR);
            game.getGameScreenMessageActions().sendGameEvent(
                    game.getGameListeners(), EVENT_VOTE_FAIL, EVENT_VOTE_FAIL_HEADER, EVENT_VOTE_FAIL_SUBHEADER);
        }
    }

    private void formGovernment() {
        game.getVariables().setPreviousViceChair(game.getVariables().getViceChair().get());
        game.getVariables().setPreviousSupremeChancellor(game.getVariables().getSupremeChancellor().get());

        game.stateStatusUpdate(State.LEGISTLATIVE_SESSION);
    }

    private void assignRandomPolicyStartNewRound() {
        Policy policy = game.getPolicyDeck().drawNext();
        if (policy.equals(Policy.SEPARATIST_POLICY)) {
            game.getVariables().addSeparatistPolicy();
            attemptsUsedEventText = EVENT_ATTEMPTS_USED_SEPARATISTS;
        } else {
            game.getVariables().addLoyalistPolicy();
            attemptsUsedEventText = EVENT_ATTEMPTS_USED_LOYALISTS;
        }
        game.getVariables().setPreviousViceChair(null);
        game.getVariables().setPreviousSupremeChancellor(null);
        game.stateStatusUpdate(State.ROUND_START);
    }

    private void startNewNomination() {
        game.getVariables().setViceChair(null);
        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
        nextSupremeChancellor = game.getPlayerManager().getNextPlayer(supremeChancellor);
        game.getVariables().setSupremeChancellor(nextSupremeChancellor);

        game.stateStatusUpdate(State.NOMINATE_VICE_CHAIR);
    }
}
