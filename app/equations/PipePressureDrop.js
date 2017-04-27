import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

export default class PipePressureDrop extends Component {
  static navigationOptions = {
    title: 'Pipe Pressure Drop',
  };
  render(){
    const { navigate } = this.props.navigation;
    return (
      <View style={{flex:1, backgroundColor: '#03D6F3'}}>
        <Text style={styles.hard}>Pipe Pressure Drop (Darcy-Weisbach)</Text>
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

AppRegistry.registerComponent('PipePressureDrop', () => PipePressureDrop);