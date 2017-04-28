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
  Button,
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

// Custom Components
import IosFonts from './app/components/IosFonts/IosFonts'; //unused
import Logo from './app/components/Logo/Logo';
import Footer from './app/components/Footer/Footer'; //unused
import RouteButton from './app/components/RouteButton/RouteButton';

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
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'Welcome',
      headerRight: <Button 
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };
  
  render() {
    const { navigate } = this.props.navigation;
    const { params } = this.props.navigation.state;
    return (
      <View style={styles.container}>
        <View style={styles.main}>
          <Logo style={styles.image}/>
          <Text style={styles.welcome}> Welcome to the Chemical Engineer Helper App!</Text>
          <View style={styles.row}>
            <RouteButton title="Home" navigate={this.props.navigation} text="Get Started!" />
            <RouteButton title="Home" navigate={this.props.navigation} text="Sign In" />
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
    paddingTop : 5,
  },
  main: {
    flex: 1,
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
