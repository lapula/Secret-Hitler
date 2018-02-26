const textConstants = {
  /* General constants */

  appTitle: "The Sith Imperative",
  joinGame: "Join game",
  createGame: "Create game",
  openGameScreen: "Open game screen",
  numberOfPlayers: "Number of players",
  gameName: "Game name",
  playerName: "Player name",


  /* Player app constants */

  yourRoleIs: "Your role is: ",
  confirmRole: "Understood!",
  confirmAlert: "I see.",
  connecting: "Connecting...",
  gameStarting: "Game is starting...",
  roleNotAssigned: "Not assigned yet.",

  /* Game screen constants */

  cardsInDeck: "Cards remaining in deck:",
  governmentVotesThisRound: "Attempted government votes this round:",
  supremeChancellor: "Supreme Chancellor",
  viceChair: "Vice Chair",
}

const INVESTIGATE = "The Supreme Chancellor investigates a player's role.";
const SPECIAL_ELECTION = "The Supreme Chancellor nominates the next Supreme Chancellor.";
const EXECUTION = "The Supreme Chancellor must execute a player."
const EXECUTION_VETO = EXECUTION + "\n\n" + "The Vice Chair may veto policies.";

const fiveSix = [null, null, INVESTIGATE, EXECUTION, EXECUTION_VETO]
const sevenEight = [null, INVESTIGATE, SPECIAL_ELECTION, EXECUTION, EXECUTION_VETO]
const nineTen = [INVESTIGATE, INVESTIGATE, SPECIAL_ELECTION, EXECUTION, EXECUTION_VETO]

const separatistPowers = {
  fiveSixPlayers: fiveSix,
  sevenEightPlayers: sevenEight,
  nineTenPlayers: nineTen,
}

import senateGray from '../resources/Senate_gray.jpg';
import palpatineSenate from '../resources/Palpatine_yoda.jpg';

const eventImageMap = {
  generic: senateGray,
  LEGISTLATION_SEPARATISTS: palpatineSenate
}

export {textConstants, separatistPowers, eventImageMap};
