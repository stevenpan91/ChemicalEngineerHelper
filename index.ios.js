/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

// Custom Components
import IosFonts from './app/components/IosFonts/IosFonts';
import Logo from './app/components/Logo/Logo';
import Footer from './app/components/Footer/Footer';

//Import Scenes
import Home from './app/Home';
import History from './app/History';
import Settings from './app/Settings';

//Import Individual equations
import Density from './app/equations/Density'
import VaporDensity from './app/equations/VaporDensity'
import ReynoldsNumber from './app/equations/ReynoldsNumber'
import PipePressureDrop from './app/equations/PipePressureDrop'

// For testing if we can automatically route to equation page if user is logged in.
userLoggedIn = true;

function wait(ms) {
  var d = new Date();
  var d2 = null;
  do { d2 = new Date(); }
  while(d2-d < ms);
}

class Welcome extends Component {
  static navigationOptions = {
    title: 'Welcome',
    rightButton: 'Settings',
  };
  render() {
    const { navigate } = this.props.navigation;
    return (
      <View style={styles.container}>
        <View style={styles.main}>
          <Logo style={styles.image}/>
          <Text style={styles.welcome}> Welcome to the Chemical Engineer Helper App!</Text>
          <View style={styles.row}>
            <TouchableOpacity 
            onPress={() => navigate('Home')} 
            title="Home">
              <Text style={styles.button}>Get Started!</Text>
            </TouchableOpacity>
            <Text> </Text>
            <TouchableOpacity 
            onPress={() => navigate('Home')} 
            title="Home">
              <Text style={styles.button}>Sign In</Text>
            </TouchableOpacity>
          </View>
          <Text style={styles.quote}> "Quote of Deep thought" </Text>
          <Text style={styles.quote}> - Scientist , 1968 </Text>
        </View>
      </View>
    );
  }
}

// Declare your routes
const ChemEngHelper = StackNavigator({
  Welcome: { screen: Welcome },
  Home: { screen: Home },
  History: { screen: History },
  Settings: { screen: Settings },
  Density: { screen: Density },
  "Vapor Density": { screen: VaporDensity },
  "Reynolds Number": { screen: ReynoldsNumber },
  "Pipe Pressure Drop":{ screen: PipePressureDrop },
});

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
  }
});

AppRegistry.registerComponent('ChemEngHelper', () => ChemEngHelper);
