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
      <View style={styles.container}>
        <View style={styles.row}>
          <View style={styles.spacer} />
          <Text style={styles.textBox}>Mass : </Text>
          <TextInput 
          style={styles.textBox}
          placeholder="(value)"
          onChangeText={(mass) => this.setState({mass})} />
          <View style={styles.spacer} />
        </View>
        <View style={styles.row}>
          <View style={styles.spacer} />
          <Text style={styles.textBox}>Volume : </Text>
          <TextInput 
          style={styles.textBox}
          placeholder="(value)"
          onChangeText={(volume) => this.setState({volume})} />
          <View style={styles.spacer} />
        </View>
        <View>
          <Button title="Calculate" onPress={()=>this.calcDensity(this.state)} />
        </View>
        <View  style={styles.row}>
          <View style={styles.spacer} />
          <Text style={styles.textBox}>Results : </Text>
          <Text style={styles.textBox}>{ this.state.density }</Text>
          <View style={styles.spacer} />
        </View>
      </View>
    )
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#03D6F3',
    paddingTop : 5,
  },
  row: {
    flexDirection: 'row',
    justifyContent:'space-around',
  },
  textBox: {
    color: '#FFFFFF',
    flex:0.5,
    width:80,
    height:20,
    margin:20,
  },
  spacer: {
    width:75,
    height:10
  }
})

AppRegistry.registerComponent('Density', () => Density);