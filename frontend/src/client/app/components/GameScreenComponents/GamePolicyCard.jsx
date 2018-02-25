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
        </div>);
    }
  }

  render() {
    return (
      <div className={styles.policyCardSlot}>{this.renderCard()}</div>
    );
  }
}

export default GamePolicyCard;
