import React, {Component} from 'react';

import styles from './gamescreen-style.css';
import senateGray from '../../resources/Senate_gray.jpg';

class GameEvent extends React.Component {
  constructor(props, context) {
    super(props, context);
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
            <div className={styles.gameEventImage} style={{backgroundImage: `url(${senateGray})`}}>
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
