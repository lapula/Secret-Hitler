const textConstants = {
  /* General constants */

  appTitle: "The Sith Imperative",
  joinGame: "Join game",
  createGame: "Create game",
  openGameScreen: "Open game screen",
  numberOfPlayers: "Number of players",
  gameName: "Game name",
  playerName: "Player name",
  gameStarting: "Waiting for server...",
  enterGame: "Enter game!",
  createdBy: "Created by: Laur Pulliainen",
  github: "Github:",
  rules: 'Rules',
  about: "About",
  playerNameError: "Name must be between 1 and 12 characters",
  gameNameError: "Name must be exactly 6 characters",
  playersError: "Must be a number between 5 and 10",

  /* Player app constants */

  yourRoleIs: "Your role is: ",
  confirmRole: "Understood!",
  confirmAlert: "I see.",
  connecting: "Connecting...",
  roleNotAssigned: "Not assigned yet.",
  confirm: "Confirm",

  /* Game screen constants */

  cardsInDeck: "Cards in deck:",
  governmentVotesThisRound: "Election tracker:",
  supremeChancellor: "Supreme Chancellor",
  viceChair: "Vice Chair",
  separatistsLastSlot: "Separatists win if a policy card is placed here.",
  loyalistsLastSlot: "Loyalists win if a policy card is placed here.",
  gameState: "Game state:",
  openingGameScreen: "Opening game screen (if this is taking long, the game might not exist or was failed to create)."
}

const POLICY_PEEK = "The Supreme Chancellor receives knowledge of the next three policy cards.";
const INVESTIGATE = "The Supreme Chancellor investigates a player's role.";
const SPECIAL_ELECTION = "The Supreme Chancellor nominates the next Supreme Chancellor.";
const EXECUTION = "The Supreme Chancellor must execute a player. "
const EXECUTION_VETO = EXECUTION + "The Vice Chair may from now on veto policies.";

const fiveSix = [null, null, POLICY_PEEK, EXECUTION, EXECUTION_VETO]
const sevenEight = [null, INVESTIGATE, SPECIAL_ELECTION, EXECUTION, EXECUTION_VETO]
const nineTen = [INVESTIGATE, INVESTIGATE, SPECIAL_ELECTION, EXECUTION, EXECUTION_VETO]

const separatistPowers = {
  fiveSixPlayers: fiveSix,
  sevenEightPlayers: sevenEight,
  nineTenPlayers: nineTen,
}

import oldRepublic from '../resources/old_republic_symbol.png';
import republicSymbol from '../resources/republic_symbol.png';
import separatistSymbol from '../resources/separatist_symbol.png';
import mandaloreanSymbol from '../resources/mandalorian_skull.jpg';
import jediOrderSymbol from '../resources/jedi_order_symbol.jpg';
import sithEmpireSymbol from '../resources/sith_empire_symbol.jpg';

const eventImageMap = {
  generic: oldRepublic,
  separatistSymbol: separatistSymbol,
  loyalistsSymbol: republicSymbol,
  mandaloreanSymbol: mandaloreanSymbol,
  jediOrderSymbol: jediOrderSymbol,
  sithEmpireSymbol: sithEmpireSymbol
}

const materialUiOverrides = {
  backgroundDarkGray: {backgroundColor: "#535353"},
  colorWhite: {color: "white"},
  darkGray: "#535353",
  senateGold: "#bf8873"
}

export {textConstants, separatistPowers, eventImageMap, materialUiOverrides};
