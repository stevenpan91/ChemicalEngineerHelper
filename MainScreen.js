'use strict'

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Button,
  TouchableHighlight,
  TouchableOpacity,
  Navigator
} from 'react-native';


class MainScreen extends Component {
  //static navigationOptions = { title: 'Welcome', };

  render() {
      return (
        <Navigator
            renderScene={this.renderScene.bind(this)}
            navigator={this.props.navigator}
            navigationBar={
              <Navigator.NavigationBar style={{backgroundColor: '#246dd5'}}
                  routeMapper={NavigationBarRouteMapper} />
            } />
      );
  }
    renderScene(route, navigator) {
      return (
        <View style={{flex: 1, alignItems: 'center', justifyContent:'center'}} >
          <TouchableHighlight style={{backgroundColor: 'yellow', padding: 10}}
              onPress={this.gotoEquations.bind(this)}>
            <Text style={{backgrondColor: 'yellow', color: 'green'}}>To Equations</Text>
          </TouchableHighlight>
        </View>
      );
  }
    gotoEquations () {
    console.log(navigator);
      this.props.navigator.push({id: 'EquationScreen', name: 'EquationScreen',});
    }
}

var NavigationBarRouteMapper = {
  LeftButton(route, navigator, index, navState) {
    return (
      <TouchableOpacity style={{flex: 1, justifyContent: 'center'}}
          onPress={() => navigator.parentNavigator.pop()}>
        <Text style={{color: 'white', margin: 10,}}>
          Back
        </Text>
      </TouchableOpacity>
    );
  },
  RightButton(route, navigator, index, navState) {
    return null;
  },
  Title(route, navigator, index, navState) {
    return (
      <TouchableOpacity style={{flex: 1, justifyContent: 'center'}}>
        <Text style={{color: 'white', margin: 10, fontSize: 16}}>
          Main Page
        </Text>
      </TouchableOpacity>
    );
  }
};

module.exports=MainScreen;