import React, { Component } from 'react';
import { AppRegistry, Text, View, StyleSheet } from 'react-native';


export default class History extends Component {
  render() {
    return (
      <View style={{backgroundColor: '#03D6F3'}}>
        <Text style={color: '#FFFFFF'}>There is no history.</Text>
      </View>
    );
  }
}

AppRegistry.registerComponent.('History', () => History);