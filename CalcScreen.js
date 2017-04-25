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
  TextInput,
  Navigator
} from 'react-native';

class CalcScreen extends Component {
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
       <View style={{flex: 1}}>
            <TextInput style={{textAlign: 'left', marginTop: 100}}>Input Test</TextInput>




          <Text style={{textAlign: 'left', marginTop:200}}>Result</Text>

      </View>
    );
  }

//    gotoMain() {
//        this.props.navigator.push({
//          id: 'MainScreen',
//          sceneConfig: Navigator.SceneConfigs.FloatFromBottom,
//        });
//      }
}

var NavigationBarRouteMapper = {
  LeftButton(route, navigator, index, nextState) {
    return (
      <TouchableOpacity style={{flex: 1, justifyContent: 'center'}}
          onPress={() => navigator.parentNavigator.pop()}>
        <Text style={{color: 'white', margin: 10,}}>
          Back
        </Text>
      </TouchableOpacity>
    );
  },
  RightButton(route, navigator, index, nextState) {
    return (
      <TouchableOpacity style={{flex: 1, justifyContent: 'center'}}
          onPress={() => navigator.parentNavigator.push({id: 'unknown'})}>
        <Text style={{color: 'white', margin: 10,}}>
          Test
        </Text>
      </TouchableOpacity>
    );
  },
  Title(route, navigator, index, nextState) {
    return (
      <TouchableOpacity style={{flex: 1, justifyContent: 'center'}}
        onPress={() => navigator.push({
                                                                     id: 'MainScreen',
                                                                     sceneConfig: Navigator.SceneConfigs.FloatFromBottom,
                                                                   })}>
        <Text style={{color: 'white', margin: 10, fontSize: 16}}>
          Main Page
        </Text>
      </TouchableOpacity>
    );
  }

};

module.exports = CalcScreen;