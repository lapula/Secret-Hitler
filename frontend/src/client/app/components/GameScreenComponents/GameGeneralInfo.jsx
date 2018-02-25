import React, {Component} from 'react';

import styles from './gamescreen-style.css';

const CARDS_IN_DECK = "Cards remaining in deck:"
const GOVERNMENT_VOTES = "Attempted government votes this round:"

class GameGeneralInfo extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  render() {
    return (
      <div className={styles.gameGeneralInfoWrapper}>
        <div className={styles.generalInfoHeader}>{this.props.state}</div>
        <div className={styles.generalInfoBodyWrapper}>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{GOVERNMENT_VOTES}</div>
            <div className={styles.generalInfoBodyItemValue}>{this.props.governmentVotesThisRound}</div>
          </div>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{CARDS_IN_DECK}</div>
            <div className={styles.generalInfoBodyItemValue}>{this.props.cardsInDeck}</div>
          </div>
        </div>
      </div>
    );
  }

}

export default GameGeneralInfo;
