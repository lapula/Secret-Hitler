import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Subheader from 'material-ui/Subheader';
import Paper from 'material-ui/Paper';

import styles from './playerapp-style.css'

class OptionList extends React.Component {

    constructor(props) {
      super(props);
      this.state = {
        choices: null,
        subheader: null,
        previousChoice: null
      };
    }

    createListItem(key, text) {
        let listClickEvent = this.handleClick.bind(this, key, text);
        return (
          <ListItem
            className={styles.itemStyle}
            primaryText={text}
            key={key}
            onClick={listClickEvent}
          />
        )
    }

    handleClick(responseKey, text, elem) {
        this.props.sendQueryResponse(responseKey)
        this.setState({
          choices: null,
          previousChoice: text
        });
    }

    componentWillReceiveProps(nextProps) {
      if (nextProps.queryData != null) {
        this.setState({
          choices: nextProps.queryData.choices,
          subheader: nextProps.queryData.subheader
        });
      }
      this.setState({
        previousChoice: null
      });
    }

    getChoicesArray() {
      let list = [];
      Object.entries(this.state.choices).map(([key, value]) => {
        list.push(this.createListItem(key, value))
      })
      return list;
    }

    render() {

        if (!this.props.visible) {
          return null;
        } else if (this.state.choices) {
          return(
              <div className={styles.listContainer}>
                  <List className={styles.list}>
                    <Paper className={styles.listPaper} zDepth={4}>
                      <Subheader className={styles.subheader}>{this.state.subheader}</Subheader>
                      {this.getChoicesArray()}
                    </Paper>
                  </List>
              </div>
          )
        } else if (this.state.previousChoice) {
          return (
            <div className={styles.listContainer}>
              <h2>You chose: <b>{this.state.previousChoice}</b></h2>
            </div>
          );
        } else {
          return (<div className={styles.listContainer}></div>);
        }
    }
}

export default OptionList;
