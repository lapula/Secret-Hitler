import React, {Component} from 'react';

import {textConstants} from '../constants.jsx'
import styles from './gamescreen-style.css';

class GameGeneralInfo extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  render() {
    return (
      <div className={styles.gameGeneralInfoWrapper}>
          <div className={styles.generalInfoBodyWrapper}>
            <div className={styles.generalInfoBodyItem}>
              <div className={styles.generalInfoBodyItemHeader}>{textConstants.gameState}</div>
              <div className={styles.generalInfoBodyItemValue}>{this.props.state}</div>
            </div>
            <div className={styles.generalInfoBodyItem}>
              <div className={styles.generalInfoBodyItemHeader}>{textConstants.gameName}:</div>
              <div className={styles.generalInfoBodyItemValue}>{this.props.gameName}</div>
            </div>
          </div>
        <div className={styles.generalInfoBodyWrapper}>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.governmentVotesThisRound}</div>
            <div className={styles.generalInfoBodyItemValue}>{this.props.governmentVotesThisRound}</div>
          </div>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.cardsInDeck}</div>
            <div className={styles.generalInfoBodyItemValue}>{this.props.cardsInDeck}</div>
          </div>
        </div>
      </div>
    );
  }

}

export default GameGeneralInfo;
