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
  View,
  Button,
  TouchableHighlight,
  Navigator
} from 'react-native';

//import{
//    StackNavigator,
//}from 'react-navigation';



export default class ChemEngHelper extends Component {
  //static navigationOptions = { title: 'Welcome', };
  render() {
    const routes = [
      {title: 'First Scene', index: 0},
      {title: 'Second Scene', index: 1},
    ];
    return (
      <Navigator
        initialRoute={routes[0]}
        initialRouteStack={routes}
        renderScene={(route, navigator) =>
          <TouchableHighlight onPress={() => {
            if (route.index === 0) {
              navigator.push(routes[1]);
            } else {
              navigator.pop();
            }
          }}>
          <Text>Hello {route.title}!</Text>
          </TouchableHighlight>
        }
        style={{padding: 100}}
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

//class EquationScreen extends React.Component{
//
//   static navigationOptions = ({navigation}) => ({
//       title: navigation.state.params.name,
//     });
//     render() {
//       const { goBack } = this.props.navigation;
//       return (
//         <Button
//           title="Go back"
//           onPress={() => goBack()}
//         />
//       );
//     }
//}



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

//const App=StackNavigator({
//    Main: {screen: ChemEngHelper},
//    Equations: {screen: EquationScreen},
//});

AppRegistry.registerComponent('ChemEngHelper', () => ChemEngHelper);
