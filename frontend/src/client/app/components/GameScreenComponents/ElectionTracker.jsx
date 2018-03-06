import React, {Component} from 'react';

import styles from './gamescreen-style.css';
import senateSymbol from '../../resources/senate_symbol.png';

class ElectionTracker extends React.Component {
  constructor(props, context) {
    super(props, context);
  }

  renderTrackerCards() {
    let result = [];
    const card = <div className={styles.electionTrackerMarker}><img src={senateSymbol} className={styles.electionTrackerImage}/></div>;
    [...Array(3).keys()].forEach(i => {
      if (i < this.props.governmentVotes) {
        result.push(<div key={i} className={styles.electionTrackerSlot}>{card}</div>)
      } else {
        result.push(<div key={i} className={styles.electionTrackerSlot}></div>)
      }
    });
    console.log(result);
    return result;
  }

  render() {
    return (
      <div className={styles.electionTrackerContainer}>
        <div className={styles.electionTrackerTrack}>
          {this.renderTrackerCards()}
        </div>
      </div>
    );
  }

}

export default ElectionTracker;
