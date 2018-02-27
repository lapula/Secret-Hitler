import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import ReactFontFace from 'react-font-face'

import {textConstants, materialUiOverrides} from '../constants.jsx'
import styles from './playerapp-style.css';
import starfont from '../../resources/Starjedi.ttf';
import republicLogo from '../../resources/Republic_Emblem.png';
import cisLogo from '../../resources/CIS_emblem.png';
import palpatineLogo from '../../resources/palpatine.jpg';

const ROLE_SEPARATIST = "Separatist";
const ROLE_PALPATINE = "Sheev Palpatine";
const ROLE_LOYALIST = "Loyalist";

// Fix for Material-Ui dialog padding-top issue
const dialogStyles = {
  dialogRoot: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    textAlign: "center",
  },
  dialogContent: {
    position: "relative",
    width: "auto",
    transform: ""
  }
};


class RoleDialog extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      open: false
    };
    this.handleRequestClose = this.handleRequestClose.bind(this);
    this.handleTouchTap = this.handleTouchTap.bind(this);
  }

  handleRequestClose() {
    this.setState({
      open: false,
    });
  }

  handleTouchTap() {
    this.setState({
      open: true,
    });
  }

  renderCardImage() {
    if (this.props.role == ROLE_LOYALIST) {
      return (<img src={republicLogo} alt={this.props.role} className={styles.cardImage}/>)
    } else if (this.props.role == ROLE_SEPARATIST) {
      return (<img src={cisLogo} alt={this.props.role} className={styles.cardImage}/>)
    } else if (this.props.role == ROLE_PALPATINE) {
      return (<img src={palpatineLogo} alt={this.props.role} className={styles.cardImage}/>)
    }
  }

  render() {

    const roleDialog = (
      <FlatButton
        label={textConstants.confirmRole}
        primary={true}
        onTouchTap={this.handleRequestClose}
      />
    );

    return (
      <footer className={styles.roleDialog}>
        <Dialog
          open={this.state.open}
          title={textConstants.yourRoleIs}
          actions={roleDialog}
          onRequestClose={this.handleRequestClose}
          contentStyle={ dialogStyles.dialogContent }
          style={ dialogStyles.dialogRoot }
          className={styles.dialogStyle}
        >
          <div className={styles.cardWrapper}>
            {this.renderCardImage()}
            <div className={styles.cardText} style={{fontFamily: 'Starjedi'}}>{this.props.role}</div>
          </div>
        </Dialog>
        <RaisedButton
          primary={false}
          label="Your role"
          labelStyle={{fontSize: '36px'}}
          style={{height: "60px", lineHeight: "0px"}}
          buttonStyle={{overflow: "hidden"}}
          overlayStyle={{paddingTop: "8px"}}
          fullWidth={true}
          backgroundColor={materialUiOverrides.darkGray}
          onTouchTap={this.handleTouchTap}
        />
      </footer>
    );
  }
}

export default RoleDialog;
