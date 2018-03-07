import React, {Component} from 'react';

import {textConstants} from '../constants.jsx'
import styles from './gamescreen-style.css';

class GamePolicyCard extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  renderCard() {
    if (this.props.activeKeys.includes(this.props.cardKey)) {
      const cardStyle = this.props.isSeparatist ? styles.policyCardSeparatist : styles.policyCardLoyalist
      return (
        <div className={cardStyle}>
          <img src={this.props.cardImage} className={styles.policyCardImage}/>
        </div>
      );
    } else {
      if (this.props.isLast) {
        const text = this.props.isSeparatist ? textConstants.separatistsLastSlot : textConstants.loyalistsLastSlot;
        return (
          <div className={styles.policyCardPowersText}>{text}</div>
        );
      } else if (this.props.isSeparatist && this.props.separatistPower != null) {
        return (
          <div className={styles.policyCardPowersText}>{this.props.separatistPower}</div>
        );
      }
    }
  }

  winCondition() {
    if (this.props.isSeparatist && this.props.cardKey >= 3) {
      return {backgroundColor: "rgba(0, 0, 0, 0.4)"};
    }
  }

  render() {
    return (
      <div style={this.winCondition()} className={this.props.isLast ? styles.policyCardSlotLast : styles.policyCardSlot}>{this.renderCard()}</div>
    );
  }
}

export default GamePolicyCard;
