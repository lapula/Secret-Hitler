import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import AppBar from 'material-ui/AppBar';

import {textConstants, materialUiOverrides} from './constants.jsx'
import styles from './general-style.css';
import RegisterPlayer from './RegisterPlayer.jsx';
import RegisterGame from './RegisterGame.jsx';

const PLAYER_APP = "PLAYER_APP";
const CREATE_GAME = "CREATE_GAME";
const LISTEN_GAME = "LISTEN_GAME";

class Main extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      renderComponent: null
    };
    this.showPlayerScreen = this.showPlayerScreen.bind(this);
    this.showGameCreationScreen = this.showGameCreationScreen.bind(this);
    this.showGameScreen = this.showGameScreen.bind(this);
  }

  showPlayerScreen() {this.setState({renderComponent: PLAYER_APP});}
  showGameCreationScreen() {this.setState({renderComponent: CREATE_GAME});}
  showGameScreen() {this.setState({renderComponent: LISTEN_GAME});}

  renderNavigationView() {
    if (this.state.renderComponent == PLAYER_APP) {
      return (<RegisterPlayer />)
    } else if (this.state.renderComponent == CREATE_GAME) {
      return(<RegisterGame createNewGame={true}/>)
    } else if (this.state.renderComponent == LISTEN_GAME) {
      return(<RegisterGame createNewGame={false}/>)
    } else {
      return(
        <div className={styles.buttonContainer}>
           <RaisedButton
             label={textConstants.joinGame}
             primary={false}
             className={styles.menuButton}
             backgroundColor={materialUiOverrides.darkGray}
             onTouchTap={this.showPlayerScreen} /><br />
           <RaisedButton
             label={textConstants.createGame}
             primary={false}
             className={styles.menuButton}
             backgroundColor={materialUiOverrides.darkGray}
             onTouchTap={this.showGameCreationScreen} /><br />
           <RaisedButton
             label={textConstants.openGameScreen}
             primary={false}
             className={styles.menuButton}
             backgroundColor={materialUiOverrides.darkGray}
             onTouchTap={this.showGameScreen} /><br />
         </div>
      )
    }
  }

  render() {
    return (
      <div className={styles.container}>
        <AppBar
          title={textConstants.appTitle}
          style={materialUiOverrides.backgroundDarkGray}
          titleStyle={materialUiOverrides.colorWhite}
          iconElementLeft={<div></div>}
         />
        {this.renderNavigationView()}
      </div>
      )
  }
}

export default Main;
