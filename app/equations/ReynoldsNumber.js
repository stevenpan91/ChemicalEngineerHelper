import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

export default class ReynoldsNumber extends Component {
  static navigationOptions = {
    title: 'Reynolds Number (Diameter)',
  };
  render(){
    const { navigate } = this.props.navigation;
    return (
      <View style={{flex:1, backgroundColor: '#03D6F3'}}>
        <Text style={styles.hard}>Reynolds Number (Diameter)</Text>
      </View>
    )
  };
}

const styles = StyleSheet.create({
  hard: {
    color: '#FFFFFF', 
    fontSize: 100
  },
})

AppRegistry.registerComponent('ReynoldsNumber', () => ReynoldsNumber);