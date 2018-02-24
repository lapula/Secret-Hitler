import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

import styles from './gamescreen-style.css';

//{this.props.loyalistPoliciesPassed}
//{this.props.separatistPoliciesPassed}

class GamePolicies extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  render() {
    return (
      <div className={styles.gamePoliciesWrapper}>
        <div className={styles.policiesView}>
          <div className={styles.policyCardWrapper}>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
          </div>
          <div className={styles.policyCardWrapper}>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
            <div className={styles.policyCard}></div>
          </div>
        </div>
      </div>
    );
  }

}

export default GamePolicies;
