// This file will set APP wide variables such as color, email opt out,
// Decide rest as it becomes apparent of user need.
import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

export default class Settings extends Component {
  static navigationOptions = {
    title: 'Settings',
  };
  render(){
    return (
      <View style={{backgroundColor: '#03D6F3'}}>
        <Text style={{color: '#FFFFFF'}}>There are no settings to show.</Text>
      </View>
    )
  };
}

AppRegistry.registerComponent('Settings', () => Settings);