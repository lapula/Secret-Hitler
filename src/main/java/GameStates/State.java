/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

/**
 *
 * @author pulli
 */
public enum State {
    GAME_START("GAME_START"),
    ROUND_START("ROUND_START"),
    NOMINATE_VICE_CHAIR("NOMINATE_VICE_CHAIR"),
    VOTE_ON_GOVERNMENT("VOTE_ON_GOVERNMENT"),
    LEGISTLATIVE_SESSION("LEGISTLATIVE_SESSION"),
    VETO("VETO"),
    DETERMINE_EXECUTIVE_ACTION("DETERMINE_EXECUTIVE_ACTION"),
    INVESTIGATE_LOYALTY("INVESTIGATE_LOYALTY"),
    CALL_SPECIAL_ELECTION("CALL_SPECIAL_ELECTION"),
    POLICY_PEEK("POLICY_PEEK"),
    EXECUTION("EXECUTION"),
    GAME_END("GAME_END");

    private String stringValue;

    State(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}
