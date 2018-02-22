import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import ReactFontFace from 'react-font-face'

import starfont from '../resources/Starjedi.ttf';
import styles from './general-style.css';
import republicLogo from '../resources/Republic_Emblem.svg.png';


class Footer extends React.Component {

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
    return (<img src={republicLogo} alt={this.props.role} className={styles.cardImage}/>)
  }

  render() {

    const roleDialog = (
      <FlatButton
        label="Got it!"
        primary={true}
        onTouchTap={this.handleRequestClose}
      />
    );

    return (
      <footer className={styles.roleDialog}>
        <Dialog
          open={this.state.open}
          title="Your role is:"
          actions={roleDialog}
          onRequestClose={this.handleRequestClose}
        >
          <div className={styles.cardWrapper}>
            {this.renderCardImage()}
            <div className={styles.cardText} style={{fontFamily: 'Starjedi'}}>{this.props.role}</div>
          </div>
        </Dialog>
        <RaisedButton
          primary={true}
          label="Your role"
          labelStyle={{fontSize: '36px'}}
          style={{height: "60px", lineHeight: "0px"}}
          buttonStyle={{overflow: "hidden"}}
          overlayStyle={{paddingTop: "8px"}}
          fullWidth={true}
          onTouchTap={this.handleTouchTap}
        />
      </footer>
    );
  }
}

export default Footer;
