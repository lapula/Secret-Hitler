import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import styles from './gamescreen-style.css';

class GameRoleInfo extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  playerList(props) {
    const players = this.props.players.map((player) =>
      <div key={player}>{player}</div>
    );
    return players;
  }

  render() {
    return (
      <div className={styles.gameRoleInfoWrapper}>
        {this.props.supremeChancellor}
        {this.props.viceChair}
      </div>
    );
  }

}

export default GameRoleInfo;
