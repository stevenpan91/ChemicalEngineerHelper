/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
import React, { Component } from 'react';
import { AppRegistry, Text, View, StyleSheet } from 'react-native';

import IosFonts from './app/components/IosFonts/IosFonts';
import Logo from './app/components/Logo/Logo';

class ChemEngHelper extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Logo style={styles.image}/>
        <Text style={styles.welcome}>Welcome to the Chemical Engineer Helper App!</Text>
        <Text style={styles.button}>Get Started!</Text>
        <Text style={styles.quote}>"Quote of Deep thought"</Text>
        <Text style={styles.quote}>- Scientist , 1968</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#03D6F3',
    paddingTop : 40,
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
    tintColor: '#FFFFFF',
  }
});

AppRegistry.registerComponent('ChemEngHelper', () => ChemEngHelper);
