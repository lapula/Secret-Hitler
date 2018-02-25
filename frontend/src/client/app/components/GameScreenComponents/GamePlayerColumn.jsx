import React, {Component} from 'react';

import textConstants from '../textConstants.jsx'
import styles from './gamescreen-style.css';

class GamePlayerColumn extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  addPlayerToken(player) {
    if (player == this.props.supremeChancellor) {
      return (<div className={styles.playerColumnToken}>{textConstants.supremeChancellor}</div>)
    } else if (player == this.props.viceChair) {
      return (<div className={styles.playerColumnToken}>{textConstants.viceChair}</div>)
    }
  }

  playerList() {
    const players = this.props.players.map((player) =>
      <div key={player} className={styles.playerColumnNeutral}>
        {this.addPlayerToken(player)}
        <div className={styles.playerColumnElectionItemPlainName}>{player}</div>
      </div>
    );
    return players;
  }

  playerElectionList() {
    let players = [];
    Object.entries(this.props.electionResults).map(([key, value]) => {
      const itemStyle = (value === "YES") ? styles.playerColumnYes : styles.playerColumnNo;
      players.push(
        <div key={key} className={itemStyle}>
          {this.addPlayerToken(key)}
          <div className={styles.playerColumnElectionItemName}>{key}</div>
          <div className={styles.playerColumnElectionItemValue}>{value}</div>
        </div>
      );
    });
    return players;
  }

  renderList() {
    if (!Object.keys(this.props.electionResults).length == 0) {
      return this.playerElectionList()
    } else {
      return this.playerList()
    }
  }

  render() {
    return (
      <div className={styles.gamePlayerColumnWrapper}>
        {this.renderList()}
      </div>
    );
  }

}

export default GamePlayerColumn;
