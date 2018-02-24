import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import styles from './gamescreen-style.css';

class GamePlayerColumn extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  playerList() {
    const players = this.props.players.map((player) =>
      <div key={player}>{player}</div>
    );
    return players;
  }

  playerElectionList() {
    let players = [];
    Object.entries(this.props.electionResults).map(([key, value]) => {
      const itemStyle = (value === "YES") ? styles.playerColumnYes : styles.playerColumnNo;
      players.push(
        <div key={key} className={itemStyle}>
          <div className={styles.playerColumnElectionItemName}>{key}</div>
          <div className={styles.playerColumnElectionItemValue}>{value}</div>
        </div>
      );
    });
    return players;
  }

  renderList() {
    if (this.props.electionResults) {
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
