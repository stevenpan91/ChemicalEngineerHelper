
import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text,
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

/*
Steps:
  1. Create a Scene file. In this case it would be in the equations folder.

in index.ios.js or index.android.js
  1. add an import for your new Equation.
    import VaporDensity from './app/equations/VaporDensity';
  2. add the route to your StackNavigator
    "Vapor Density": {screen: VaporDensity},

in the scene you want to call the RouteButtonWide in 
  1. import RouteButtonWide
    import RouteButtonWide from './components/RouteButtonWide/RouteButtonWide';
  2. add the RouteButtonWide in your render.
    <RouteButtonWide title="Vapor Density" navigate={this.props.navigation} />
  Note: title and navigate are require. The Title should match the route added to your StackNavigator. 
  This is also displayed on the top of the app.
  Optional: you can pass a style={{}} object to overwrite default styling. Component is a <Text />
  
*/


export default class RouteButtonWide extends Component {
  render(props){
    const { navigate } = this.props.navigate;
    return (
      <TouchableOpacity
      onPress={() => navigate(this.props.title)}
      title= {this.props.title}
      >
        <Text style={[styles.default, this.props.style]}>{ this.props.title }</Text>
      </TouchableOpacity>
    )
  };
}

//Default Styles
const styles = StyleSheet.create({
  default: {
    textAlign: 'center',
    color: '#FFFFFF',
    backgroundColor: '#033BE5',
    marginBottom: 5,
    padding: 12,
    overflow: 'hidden',
    borderRadius: 6,
    marginTop: 2,
  },
})



AppRegistry.registerComponent('RouteButtonWide', () => RouteButtonWide);