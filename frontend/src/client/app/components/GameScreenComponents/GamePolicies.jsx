import React, {Component} from 'react';

import {separatistPowers} from '../constants.jsx'
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

  getSeparatistPower(index) {
    let powers = separatistPowers.fiveSixPlayers;
    if (this.props.gamePlayers >= 9) {
      powers = separatistPowers.nineTenPlayers;
    } else if (this.props.gamePlayers >= 7)  {
      powers = separatistPowers.sevenEightPlayers;
    }
    return powers[index];
  }

  renderCards(amount, activeKeys, image, isSeparatist) {
    let cards = Array.from(new Array(amount),(val,index) => {
      return (<GamePolicyCard
        key={index}
        cardKey={index}
        activeKeys={activeKeys}
        cardImage={image}
        separatistPower={this.getSeparatistPower(index)}
        isSeparatist={isSeparatist}
        isLast={amount == (index + 1)}
      />)
    });
    return cards;
  }

  render() {
    return (
      <div className={styles.gamePoliciesWrapper}>
        <div className={styles.policiesView}>
          <div className={styles.policyCardWrapper}>
            /*<div>Loyalists</div>*/
            {this.renderCards(5, this.state.loyalistActiveKeys, republicLogo, false)}
          </div>
          <div className={styles.policyCardWrapper}>
            /*<div>Separatists</div>*/
            {this.renderCards(6, this.state.separatistActiveKeys, cisLogo, true)}
          </div>
        </div>
      </div>
    );
  }

}

export default GamePolicies;
