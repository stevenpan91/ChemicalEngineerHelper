import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

export default class History extends Component {
  static navigationOptions = {
    title: 'History',
  };
  render(){
    return (
      <View style={{backgroundColor: '#03D6F3'}}>
        <Text style={{color: '#FFFFFF'}}>No History to show.</Text>
      </View>
    )
  };
}

AppRegistry.registerComponent('History', () => History);