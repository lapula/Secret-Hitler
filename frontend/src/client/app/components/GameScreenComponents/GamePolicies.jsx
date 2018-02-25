import React, {Component} from 'react';

import styles from './gamescreen-style.css';
import republicLogo from '../../resources/Republic_Emblem.png';
import cisLogo from '../../resources/CIS_emblem.png';
import GamePolicyCard from './GamePolicyCard.jsx'

class GamePolicies extends React.Component {
  constructor(props, context) {
    super(props, context);

    this.state = {
      loyalistActiveKeys: 0,
      separatistActiveKeys: 0
    };
  }

  componentWillMount() {
    this.setActiveKeysArrays();
  }

  componentWillReceiveProps() {
    this.setActiveKeysArrays();
  }

  setActiveKeysArrays() {
    const loyalistActiveKeys = Array.from(new Array(this.props.loyalistPoliciesPassed),(val,index) => index);
    const separatistActiveKeys = Array.from(new Array(this.props.separatistPoliciesPassed),(val,index) => index);
    this.setState({
      loyalistActiveKeys: loyalistActiveKeys,
      separatistActiveKeys: separatistActiveKeys
    })
  }

  renderCards(amount, activeKeys, image) {
    let cards = Array.from(new Array(amount),(val,index) => {
      return (<GamePolicyCard
        key={index}
        cardKey={index}
        activeKeys={activeKeys}
        cardImage={image}
      />)
    });
    return cards;
  }

  render() {
    return (
      <div className={styles.gamePoliciesWrapper}>
        <div className={styles.policiesView}>
          <div className={styles.policyCardWrapper}>
            {this.renderCards(5, this.state.loyalistActiveKeys, republicLogo)}
          </div>
          <div className={styles.policyCardWrapper}>
            {this.renderCards(6, this.state.separatistActiveKeys, cisLogo)}
          </div>
        </div>
      </div>
    );
  }

}

export default GamePolicies;
