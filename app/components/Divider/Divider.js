import React, { Component } from 'react';
import { AppRegistry, View, StyleSheet} from 'react-native';


export default class Divider extends Component {
  render(props){
    return(
      <View style={styles.row}>
        <View style={{height:1, width:25,}} />
          <View style={[styles.lineColor, this.props.style]} />
        <View style={{height:1, width:25,}} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  row: {
    flexDirection: 'row',
    justifyContent: 'space-around'
  },
  lineColor: {
    height:1, 
    width:300,
    backgroundColor: '#000000'
  }
});

AppRegistry.registerComponent('Divider', () => Divider);