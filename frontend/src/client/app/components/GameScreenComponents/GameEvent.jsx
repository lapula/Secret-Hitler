import React, {Component} from 'react';

import {eventImageMap} from '../constants.jsx'
import styles from './gamescreen-style.css';

/*
Event types

LEGISTLATION_PROCESS
ATTEMPTS_USED_SEPARATISTS
ATTEMPTS_USED_LOYALISTS
VOTE_FAILED
LEGISTLATION_SEPARATISTS
LEGISTLATION_LOYALISTS
NO_VETO
VETO
POLICY_PEEK
NOMINATION
LEGISTLATION_SEPARATISTS
LEGISTLATION_LOYALISTS
INVESTIGATION
END_LOYALIST_WIN
END_SEPARATIST_WIN
EXECUTION
EVENT_SPECIAL
*/

class GameEvent extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  getBackgroundImage() {
    if (this.props.gameEvent.eventType != null) {
      const event = this.props.gameEvent.eventType;
      const separatistImage = ['END_SEPARATIST_WIN', 'LEGISTLATION_SEPARATISTS', 'ATTEMPTS_USED_SEPARATISTS'];
      const loyalistsImage = ['END_LOYALIST_WIN', 'LEGISTLATION_LOYALISTS', 'ATTEMPTS_USED_LOYALISTS'];
      const mandaloreanImage = ['EXECUTION'];
      const jediImage = ['INVESTIGATION', 'POLICY_PEEK'];
      const sithImage = ['EVENT_SPECIAL', 'VETO'];

      if (separatistImage.includes(event)) {
        return eventImageMap.separatistSymbol;
      } else if (loyalistsImage.includes(event)) {
        return eventImageMap.loyalistsSymbol;
      } else if (mandaloreanImage.includes(event)) {
        return eventImageMap.mandaloreanSymbol;
      } else if (jediImage.includes(event)) {
        return eventImageMap.jediOrderSymbol;
      } else if (sithImage.includes(event)) {
        return eventImageMap.sithEmpireSymbol;
      }
    }
    return eventImageMap.generic;
  }

  render() {
    if (!this.props.gameEvent) {
      return (
        <div className={styles.gameEventWrapper}>
          <div className={styles.gameEventDisplay}>
            <div className={styles.gameEventBackground}></div>
          </div>
        </div>
      );
    }

    return (
      <div className={styles.gameEventWrapper}>
        <div className={styles.gameEventDisplay}>
          <div className={styles.gameEventBackground}>
            <div className={styles.gameEventImage} style={{backgroundImage: `url(${this.getBackgroundImage()})`}}>
              <div className={styles.gameEventHeader}>{this.props.gameEvent.header}</div>
              <div className={styles.gameEventSubheader}>{this.props.gameEvent.subheader}</div>
            </div>
          </div>
        </div>
      </div>
    );
  }

}

export default GameEvent;
