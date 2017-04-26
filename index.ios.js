/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 

View, StyleSheet, TouchableOpacity } from 'react-native';

import IosFonts from './app/components/IosFonts/IosFonts';
import Logo from './app/components/Logo/Logo';
import Footer from './app/components/Footer/Footer';

class ChemEngHelper extends Component {
  render() {
    return (
      <View style={styles.container}>
        <View style={styles.main}>
          <Logo style={styles.image}/>
          <Text style={styles.welcome}> Welcome to the Chemical Engineer Helper App!</Text>
          <View style={styles.row}>
            <TouchableOpacity>
              <Text style={styles.button}>Get Started!</Text>
            </TouchableOpacity>
            <Text> </Text>
            <TouchableOpacity>
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
