import React, {Component} from 'react';

import {textConstants} from '../constants.jsx'
import styles from './gamescreen-style.css';

const FULL_DECK_SIZE = 17;
const LOYALIST_TOTAL = 6;
const SEPARATIST_TOTAL = 11;

class CardsInDeck extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  getGradientPercentage(reverse) {
    if (reverse) {
      return { width: (100 - Math.round((this.props.cardsInDeck / (FULL_DECK_SIZE - this.props.loyalistPoliciesPassed - this.props.separatistPoliciesPassed)) * 100)) + "%"};
    }
    return {width: Math.round((this.props.cardsInDeck / (FULL_DECK_SIZE - this.props.loyalistPoliciesPassed - this.props.separatistPoliciesPassed)) * 100) + "%"};
  }

  render() {
    return (
      <div className={styles.cardsInDeckContainer}>
        <div className={styles.generalInfoCardsInDeckLoyalist}>{LOYALIST_TOTAL - this.props.loyalistPoliciesPassed}</div>
        <div className={styles.generalInfoCardsInDeckBarBorder}>
          <div className={styles.generalInfoCardsInDeckText}>{this.props.cardsInDeck}</div>
          <div style={this.getGradientPercentage(false)} className={styles.generalInfoCardsInDeckBarFill}></div>
          <div style={this.getGradientPercentage(true)} className={styles.generalInfoCardsInDeckBarEmpty}></div>
        </div>
        <div className={styles.generalInfoCardsInDeckSeparatist}>{SEPARATIST_TOTAL - this.props.separatistPoliciesPassed}</div>
      </div>
    );
  }

}

export default CardsInDeck;
