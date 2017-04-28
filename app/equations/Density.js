import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  TextInput,
  View,
  Button, 
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';

// Temporary Logic Area 
/*
function calcDensity(props){
  var mass=parseFloat(props.mass)
  var volume=parseFloat(props.volume)
  var density=props.density
  density=mass/volume
  console.log(density)
  return(density);
}
// End Temporary Logic Area
*/
export default class Density extends Component {
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'Density',
      headerRight: <Button 
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };

  calcDensity = function(state) {
    var m = parseFloat(state.mass)
    var v = parseFloat(state.volume)
    var d = m / v
    this.setState({
      density: d
    })
  }

  constructor(props) {
    super(props);
    this.state = {
      mass: '',
      volume: '',
      density: 'N/A',
    };
  }

  render(){
    const { navigate } = this.props.navigation;
    const { params } = this.props.navigation.state;
    return (
      <View style={{flex:1, }}>
        <View>
          <Text>Mass : </Text>
          <TextInput 
          style={{height: 40}}
          placeholder="(value)"
          onChangeText={(mass) => this.setState({mass})} />
        </View>
        <View>
          <Text>Volume : </Text>
          <TextInput 
          style={{height: 40}}
          placeholder="(value)"
          onChangeText={(volume) => this.setState({volume})} />
        </View>
        <View>
          <Button title="Calculate" onPress={()=>this.calcDensity(this.state)} />
        </View>
        <View  style={styles.row}>
          <Text>Results : </Text>
          <Text>{ this.state.density }</Text>
        </View>
      </View>
    )
  };
}

const styles = StyleSheet.create({
  hard: {
    color: '#FFFFFF', 
    fontSize: 100
  },
  row: {
    flexDirection: 'row'
  }
})

AppRegistry.registerComponent('Density', () => Density);