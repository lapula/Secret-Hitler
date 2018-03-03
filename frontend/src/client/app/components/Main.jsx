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
             onTouchTap={this.showPlayerScreen}
           /><br />
           <RaisedButton
             label={textConstants.createGame}
             primary={false}
             className={styles.menuButton}
             backgroundColor={materialUiOverrides.darkGray}
             onTouchTap={this.showGameCreationScreen}
           /><br />
           <RaisedButton
             label={textConstants.openGameScreen}
             primary={false}
             className={styles.menuButton}
             backgroundColor={materialUiOverrides.darkGray}
             onTouchTap={this.showGameScreen}
           /><br />
           <RaisedButton
             label={textConstants.about}
             primary={false}
             className={styles.menuButton}
             href="https://github.com/lapula/Sith-Imperative"
             backgroundColor={materialUiOverrides.darkGray}
          /><br />
         </div>
      )
    }
  }
// <span className={styles.footerItem}>{textConstants.github} <a className={styles.footerGithubLink} href="https://github.com/lapula/Sith-Imperative">https://github.com/lapula/Sith-Imperative</a></span>

  renderFooter() {
    if (this.state.renderComponent == null) {
      return (
        <div className={styles.appFooter}>
          <span className={styles.footerItem}>
            <a className={styles.footerItem} rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="CC SA–BY–NC 4.0" style={{borderWidth: 0}} src="https://i.creativecommons.org/l/by-nc-sa/4.0/80x15.png" /></a>
            {textConstants.createdBy}
          </span>
        </div>
      );
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
         {this.renderFooter()}
      </div>
    );
  }
}

export default Main;
