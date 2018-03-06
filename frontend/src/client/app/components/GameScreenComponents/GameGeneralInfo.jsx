import React, {Component} from 'react';

import {textConstants} from '../constants.jsx'
import styles from './gamescreen-style.css';
import ElectionTracker from './ElectionTracker.jsx';

class GameGeneralInfo extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  getGradientPercentage(reverse) {
    if (reverse) {
      return { width: (100 - Math.round((this.props.cardsInDeck / 17) * 100)) + "%"};
    }
    return {width: Math.round((this.props.cardsInDeck / 17) * 100) + "%"};
  }

  render() {
    this.getGradientPercentage();
    return (
      <div className={styles.gameGeneralInfoWrapper}>
        <div className={styles.generalInfoBodyWrapper}>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.gameName}:</div>
            <div className={styles.generalInfoBodyItemValue}>{this.props.gameName}</div>
          </div>
          <div className={styles.generalInfoBodyItem}>
            <div className={styles.generalInfoBodyItemHeader}>{textConstants.cardsInDeck}</div>
            <div className={styles.cardsInDeckContainer}>
              <div className={styles.generalInfoCardsInDeckBarBorder}>
                <div className={styles.generalInfoCardsInDeckText}>{this.props.cardsInDeck}</div>
                <div style={this.getGradientPercentage(false)} className={styles.generalInfoCardsInDeckBarFill}></div>
                <div style={this.getGradientPercentage(true)} className={styles.generalInfoCardsInDeckBarEmpty}></div>
              </div>
            </div>
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
