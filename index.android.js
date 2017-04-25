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
  TouchableOpacity,
  Navigator
} from 'react-native';


var MainScreen = require('./MainScreen');
var EquationScreen = require('./EquationScreen');





export default class ChemEngHelper extends Component {
  //static navigationOptions = { title: 'Welcome', };

  render() {
      return (
        <Navigator
            initialRoute={{id: 'MainScreen', name: 'Index'}}
            renderScene={this.renderScene.bind(this)}
            configureScene={(route) => {
              if (route.sceneConfig) {
                return route.sceneConfig;
              }
              return Navigator.SceneConfigs.FloatFromRight;
            }} />
      );
    }
    renderScene(route, navigator) {
      var routeId = route.id;

      if (routeId === 'MainScreen') {
        return (
          <MainScreen
              navigator={navigator} />
        );
      }
      if (routeId === 'EquationScreen') {
        return (
          <EquationScreen
            navigator={navigator} />
        );
      }

      return this.noRoute(navigator);

    }
    noRoute(navigator) {
      return (
        <View style={{flex: 1, alignItems: 'stretch', justifyContent: 'center'}}>
          <TouchableOpacity style={{flex: 1, alignItems: 'center', justifyContent: 'center'}}
              onPress={() => navigator.pop()}>
            <Text style={{color: 'red', fontWeight: 'bold'}}>请在 index.js 的 renderScene 中配置这个页面的路由</Text>
          </TouchableOpacity>
        </View>
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
//module.exports=EquationScreen;
AppRegistry.registerComponent('ChemEngHelper', () => ChemEngHelper);
