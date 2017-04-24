/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

import{
    StackNavigator,
}from 'react-navigation';



class ChemEngHelper extends React.Component {
  static navigationOptions = { title: 'Welcome', };
  render() {

      const{navigate}=this.props.navigation;
      return(
        <Button
            title="Go to Equation Page"
            onPress={() =>
                this.props.navigation.navigate('Equations')
            }
        />
      );
  }
}

//export default class ChemEngHelper extends Component {
//  static navigationOptions = { title: 'Welcome', };
//  render() {
//
//      const{navigate}=this.props.navigation;
//      return(
//        <Button
//            title="Go to Equation Page"
//            onPress={() =>
//                this.props.navigation.navigate('Equations')
//            }
//        />
//      );

//    return (
//      <View style={styles.container}>
//        <Text style={styles.welcome}>
//          Welcome to the Chemical Engineer Helper App!
//        </Text>
//        <Text style={styles.instructions}>
//          To get started, edit index.android.js
//        </Text>
//        <Text style={styles.instructions}>
//          Double tap R on your keyboard to reload,{'\n'}
//          Shake or press menu button for dev menu
//        </Text>
//      </View>
//    );
//  }
//}

class EquationScreen extends React.Component{

    render() {


          return(
            <View style={styles.container}>
                    <Text style={styles.welcome}>
                      This is the equation page
                    </Text>
            </View>
          );
    }
}



const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

const App=StackNavigator({
    Main: {screen: ChemEngHelper},
    Equations: {screen: EquationScreen},
});

AppRegistry.registerComponent('ChemEngHelper', () => ChemEngHelper);
