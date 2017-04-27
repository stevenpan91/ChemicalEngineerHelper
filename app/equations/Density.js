import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

export default class Density extends Component {
  static navigationOptions = {
    title: 'Density',
  };
  render(){
    const { navigate } = this.props.navigation;
    return (
      <View style={{flex:1, backgroundColor: 'red'}}>
        <Text style={styles.hard}>THE PAGE GOES HARD!</Text>
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

AppRegistry.registerComponent('Density', () => Density);