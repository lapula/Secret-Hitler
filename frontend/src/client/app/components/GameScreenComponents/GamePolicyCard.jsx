import React, {Component} from 'react';

import styles from './gamescreen-style.css';

class GamePolicyCard extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  renderCard() {
    if (this.props.activeKeys.includes(this.props.cardKey)) {
      return (
        <div className={styles.policyCard}>
          <img src={this.props.cardImage} className={styles.policyCardImage}/>
        </div>
      );
    } else {
      if (this.props.isSeparatist && this.props.separatistPower != null) {
        return (
          <div className={styles.policyCardPowersText}>{this.props.separatistPower}</div>
        );
      }
    }
  }

  render() {
    return (
      <div className={this.props.isLast ? styles.policyCardSlotLast : styles.policyCardSlot}>{this.renderCard()}</div>
    );
  }
}

export default GamePolicyCard;
