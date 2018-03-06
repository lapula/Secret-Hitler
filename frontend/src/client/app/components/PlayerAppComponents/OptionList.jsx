import React, {Component} from 'react';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import Subheader from 'material-ui/Subheader';
import Paper from 'material-ui/Paper';
import RaisedButton from 'material-ui/RaisedButton';

import {textConstants} from '../constants.jsx'
import styles from './playerapp-style.css'

const LEGISTLATIVE_SESSION = "LEGISTLATIVE_SESSION";

class OptionList extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        choices: null,
        subheader: null,
        previousChoice: null,
        gameState: null,
        previousSubheader: null,
        selectedCombinedValue: null
      };
      this.handleClick = this.handleClick.bind(this);
      this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleClick(e, value) {
      this.setState({selectedCombinedValue: value});
    }

    handleSubmit() {
      if (this.state.selectedCombinedValue != null) {
        this.props.sendQueryResponse(this.state.selectedCombinedValue.split(";")[0])
        this.setState({
          choices: null,
          previousChoice: this.state.selectedCombinedValue.split(";")[1],
          selectedCombinedValue: null
        });
      }
    }

    componentWillReceiveProps(nextProps) {
      if (nextProps.queryData != null) {
        this.setState({
          choices: nextProps.queryData.choices,
          subheader: nextProps.queryData.subheader
        });
        if (nextProps.queryData.gameState !== this.state.gameState) {
          this.setState({
            previousChoice: null,
            previousSubheader: nextProps.queryData.subheader,
            gameState: nextProps.queryData.gameState
          })
        }
      }
    }

    getChoicesArray() {
      let list = [];
      Object.entries(this.state.choices).map(([key, value]) => {
        list.push(this.createListItem(key, value))
      })
      return list;
    }

    createListItem(key, text) {
      const combinedValue = key + ";" + text;
      let itemStyle = this.determineListItemStyle(combinedValue);

      return (
        <RadioButton
          className={itemStyle}
          label={text}
          value={combinedValue}
          key={key}
        />
      );
    }

    determineListItemStyle(combinedValue) {
      let itemStyle = styles.radioNormal;

      if (this.state.selectedCombinedValue != null) {
        if (this.props.queryData.gameState == LEGISTLATIVE_SESSION) {
          itemStyle = (this.state.selectedCombinedValue == combinedValue) ? styles.radioNotSelected : styles.radioSelected;
        } else {
          itemStyle = (this.state.selectedCombinedValue == combinedValue) ? styles.radioSelected : styles.radioNotSelected;
        }
      }
      return itemStyle;
    }

    render() {
      if (!this.props.visible) {
        return null;
      } else if (this.state.choices) {
        return(
          <div className={styles.listContainer}>
              <Paper className={styles.listPaper} zDepth={4}>
                <Subheader className={styles.subheader}>{this.state.subheader}</Subheader>
                <div className={styles.optionListRadioButtonsWrapper}>
                  <RadioButtonGroup name="playerChoice" onChange={this.handleClick}>
                    {this.getChoicesArray()}
                  </RadioButtonGroup>
                </div>
                <RaisedButton
                  label={textConstants.confirm}
                  primary={true}
                  className={styles.formButton}
                  onTouchTap={this.handleSubmit}
                />
              </Paper>

          </div>
        );
      } else if (this.state.previousChoice) {
        return (
          <div className={styles.listContainer}>
            <Paper className={styles.listPaper} zDepth={4}>
              <Subheader className={styles.subheader}>{this.state.previousSubheader}</Subheader>
              <div className={styles.previousChoice}>You chose: {this.state.previousChoice}</div>
            </Paper>
          </div>
        );
      } else {
        return (<div className={styles.listContainer}></div>);
      }
    }
}

export default OptionList;
