import React, {Component} from 'react';

import {textConstants} from '../constants.jsx'
import styles from './gamescreen-style.css';
import ElectionTracker from './ElectionTracker.jsx';
import CardsInDeck from './CardsInDeck.jsx';

class GameGeneralInfo extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  render() {
    return (
      <div className={styles.gameGeneralInfoWrapper}>
        <div className={styles.generalInfoBodyWrapper}>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.gameName}:</div>
            <div className={styles.generalInfoBodyItemValue}>{this.props.gameName}</div>
          </div>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.cardsInDeck}</div>
            <CardsInDeck
              cardsInDeck={this.props.cardsInDeck}
              loyalistPoliciesPassed={this.props.loyalistPoliciesPassed}
              separatistPoliciesPassed={this.props.separatistPoliciesPassed}
            />
          </div>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.governmentVotesThisRound}</div>
            <ElectionTracker governmentVotes={this.props.governmentVotesThisRound} />
          </div>
        </div>
      </div>
    );
  }
}

export default GameGeneralInfo;
