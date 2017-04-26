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

import Logo from './app/components/Logo/Logo';

class MainScreen extends Component {
  //static navigationOptions = { title: 'Welcome', };

  render() {
      return (
        <Navigator
            renderScene={this.renderScene.bind(this)}
            navigator={this.props.navigator}
            navigationBar={
              <Navigator.NavigationBar style={styles.navBar}
                  routeMapper={NavigationBarRouteMapper} />
            } />
      );
  }
    renderScene(route, navigator) {
      return (
        <View style={styles.container}>
          <View style={styles.main}>
            <Logo style={styles.image}/>
            <Text style={styles.welcome}> Welcome to the Chemical Engineer Helper App!</Text>
            <View style={styles.row}>
              <TouchableOpacity onPress={this.gotoEquations.bind(this)}>
                <Text style={styles.button}>Get Started!</Text>
              </TouchableOpacity>
              <Text> </Text>
              <TouchableOpacity onPress={this.gotoEquations.bind(this)}>
                <Text style={styles.button}>Sign In</Text>
              </TouchableOpacity>
            </View>
            <Text style={styles.quote}> "Quote of Deep thought" </Text>
            <Text style={styles.quote}> - Scientist , 1968 </Text>
          </View>
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
      <TouchableOpacity style={styles.main}
          onPress={() => navigator.parentNavigator.pop()}>
        <Text style={styles.navTitle}>
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
      <TouchableOpacity style={styles.main}>
        <Text style={styles.navTitle}>
          Main Page
        </Text>
      </TouchableOpacity>
    );
  }
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#03D6F3',
    paddingTop : 40,
  },
  main: {
    flex: .9,
    justifyContent:'center',
    alignItems: 'center'
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
    color: '#FFFFFF',
    fontWeight: 'bold',
  },
  button: {
    textAlign: 'center',
    color: '#FFFFFF',
    backgroundColor: '#033BE5',
    marginBottom: 5,
    padding: 12,
    overflow: 'hidden',
    borderRadius: 6,
  },
  quote : {
    textAlign: 'center',
    color: '#FFFFFF',
    fontFamily: 'snell roundhand',
  },
  image : {
    tintColor: '#ffffff',
  },
  row: {
    flexDirection: 'row'
  },
  navBar: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#246dd5',
    paddingTop : 40,
  },
  navTitle: {
    color: 'white',
    margin: 10,
    fontSize: 16,
  },
});

module.exports=MainScreen;
