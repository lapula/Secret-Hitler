/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import GameLogic.Game;
import GameLogic.Player;

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
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String INFORM_VICE_CHAIR = "You are the Vice Chair!";
    private static final String INFORM_SUPREME_CHANCELLOR = "You are the Supreme Chancellor!";
    private static final String VOTE_HEADER = "Vote!";
    private static final String VOTE_SUB_HEADER = "Do you approve this government?";

    private static final String EVENT_VOTE_FAIL = "VOTE_FAILED";
    private static final String EVENT_VOTE_FAIL_HEADER = "Vote failed!";
    private static final String EVENT_VOTE_FAIL_SUBHEADER = "The representatives were not able to form a government.";

    private static final String EVENT_ATTEMPTS_USED = "ATTEMPTS_USED";
    private static final String EVENT_ATTEMPTS_USED_HEADER = "Democracy failed!";
    private static final String EVENT_ATTEMPTS_USED_SUBHEADER = "The vote has failed three times, the forces of fate will decide next policy.";

    private int attempt;
    private Game game;
    
    public VoteOnGovernmentState(Game game) {
        this.game = game;
        attempt = 0;
    }
    
    @Override
    public void doAction() {
        game.getVariables().setElectionResults(new HashMap<>());
        game.getVariables().setSenateVotesThisRound(game.getVariables().getSenateVotesThisRound() + 1);
        
        Map<String, String> choices = new HashMap<>();
        choices.put(YES, YES_VOTE);
        choices.put(NO, NO_VOTE);
        game.getGamePlayerMessageActions().sendQueryAndInfoMessages(game.getPlayerManager().getPlayers(), game.getPlayerManager().getPlayers(), choices, VOTE_HEADER, VOTE_SUB_HEADER, game.getGameStateType().toString());
    }
    
    
    @Override
    public void receiveData(String player, String data) {
        game.getVariables().addVote(player, data);
        if (game.getVariables().getElectionResults().size() == game.getPlayerManager().getPlayers().size()) {

            Collection<String> votes = game.getVariables().getElectionResults().values();
            long yesVotes = votes.stream().filter(v -> v.equals(YES)).count();
            long noVotes = votes.stream().filter(v -> v.equals(NO)).count();
            boolean governmentFormed = yesVotes > noVotes;

            if (governmentFormed) {
                game.getVariables().setPreviousViceChair(game.getVariables().getViceChair().get());

                game.getVariables().setPreviousSupremeChancellor(game.getVariables().getSupremeChancellor().get());
                game.getGamePlayerMessageActions().setSpecialRole(game.getVariables().getViceChair().get(), INFORM_VICE_CHAIR);
                game.changeState(State.LEGISTLATIVE_SESSION);
            } else if (game.getVariables().getSenateVotesThisRound() == 3) {
                assignRandomPolicyStartNewRound();
            } else {
                startNewNomination();
            }
        }
    }

    private void assignRandomPolicyStartNewRound() {
        if (Math.random() > 0.5) {
            game.getVariables().addSeparatistPolicy();
        } else {
            game.getVariables().addLoyalistPolicy();
        }
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_ATTEMPTS_USED, EVENT_ATTEMPTS_USED_HEADER, EVENT_ATTEMPTS_USED_SUBHEADER);

        game.changeState(State.ROUND_START);
    }

    private void startNewNomination() {
        game.getVariables().setViceChair(null);
        Player supremeChancellor = game.getVariables().getSupremeChancellor().get();
        Player nextSupremeChancellor = game.getPlayerManager().getNextPlayer(supremeChancellor);
        game.getVariables().setSupremeChancellor(nextSupremeChancellor);
        game.getGamePlayerMessageActions().clearSpecialRoles(game.getPlayerManager().getPlayers(), nextSupremeChancellor);
        game.getGamePlayerMessageActions().setSpecialRole(nextSupremeChancellor, INFORM_SUPREME_CHANCELLOR);
        game.getGameScreenMessageActions().sendGameEvent(
                game.getGameListeners(), EVENT_VOTE_FAIL, EVENT_VOTE_FAIL_HEADER, EVENT_VOTE_FAIL_SUBHEADER);

        game.changeState(State.NOMINATE_VICE_CHAIR);
    }
}
