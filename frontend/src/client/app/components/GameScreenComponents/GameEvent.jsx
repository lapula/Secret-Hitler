import React, {Component} from 'react';

import {eventImageMap} from '../constants.jsx'
import styles from './gamescreen-style.css';

class GameEvent extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  getBackgroundImage() {
    if (eventImageMap[this.props.gameEvent.eventType] != null) {
      return eventImageMap[this.props.gameEvent.eventType];
    } else {
      return eventImageMap.generic;
    }
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
