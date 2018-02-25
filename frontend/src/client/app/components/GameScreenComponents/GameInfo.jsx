import React, {Component} from 'react';

import styles from './gamescreen-style.css';
import GameGeneralInfo from './GameGeneralInfo.jsx';
import GameEvent from './GameEvent.jsx';

class GameInfo extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  render() {
    return (
      <div className={styles.gameInfoWrapper}>
        <GameEvent />
        <GameGeneralInfo
          cardsInDeck={this.props.cardsInDeck}
          governmentVotesThisRound={this.props.governmentVotesThisRound}
        />
      </div>
    );
  }

}

export default GameInfo;
