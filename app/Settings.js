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
      <View style={styles.container}>
        <View style={{flexDirection: 'row'}}>
          <Text style={{color: '#FFFFFF'}}>Pretend there's radio Buttons or a slider</Text>
        </View>
        <View style={{flexDirection: 'row'}}>
          <Text style={{color: '#FFFFFF'}}>Units : </Text>
          <Text style={{color: '#FFFFFF'}}>o SI </Text>
          <Text style={{color: '#FFFFFF'}}>o British </Text>
        </View>
        <View style={{flexDirection: 'row'}}>
          <Text style={{color: '#FFFFFF'}}>Theme : </Text>
          <Text style={{color: '#FFFFFF'}}>o Blue </Text>
          <Text style={{color: '#FFFFFF'}}>o Dark </Text>
          <Text style={{color: '#FFFFFF'}}>o Cheerful </Text>
        </View>
        <View style={{flexDirection: 'row'}}>
          <Text style={{color: '#FFFFFF'}}>Receive Notifications : </Text>
          <Text style={{color: '#FFFFFF'}}>o Yeah, Baby! </Text>
          <Text style={{color: '#FFFFFF'}}>o maybe no... </Text>
        </View>
        <View style={{flexDirection: 'row'}}>
          <Text style={{color: '#FFFFFF'}}>Receive Emails : </Text>
          <Text style={{color: '#FFFFFF'}}>o Yeah, Baby! </Text>
          <Text style={{color: '#FFFFFF'}}>o maybe no... </Text>
        </View>
      </View>
    )
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#03D6F3',
    paddingTop : 5,
  },
});

AppRegistry.registerComponent('Settings', () => Settings);