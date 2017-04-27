import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text,
  View, 
  ScrollView, 
  StyleSheet, 
  Button,
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

import RouteButtonWide from './components/RouteButtonWide/RouteButtonWide';

export default class Home extends Component {
  static navigationOptions = {
    title: 'Home',
  };
  render(){
    const { navigate } = this.props.navigation;
    return (
      <ScrollView style={styles.main}>
        <RouteButtonWide title="Density" navigate={this.props.navigation} style={styles.test} />
        <RouteButtonWide title="Vapor Density" navigate={this.props.navigation} />
        <RouteButtonWide title="Reynolds Number" navigate={this.props.navigation} style={{backgroundColor:'purple'}}/>
        <RouteButtonWide title="Pipe Pressure Drop" navigate={this.props.navigation} />
      </ScrollView>
    )
  };
}

const styles = StyleSheet.create({
  main: {
    flex: 1,
    backgroundColor: '#03D6F3',
  },
  test: {
    color: 'green',
    backgroundColor: 'yellow',
  },
})



AppRegistry.registerComponent('Home', () => Home);