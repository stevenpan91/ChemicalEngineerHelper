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
global.inputH = 50;
global.inputFlex=0.8;
import Divider from '../components/Divider/Divider';
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
          <Text style={styles.textBox1}>Mass : </Text>
          <TextInput 
          style={{height:inputH,flex:inputFlex}}
          placeholder="(value)"
          onChangeText={(mass) => this.setState({mass})} />
          <View style={styles.spacer} />
        </View>
        <View style={styles.row}>
          <View style={styles.spacer} />
          <Text style={styles.textBox1}>Volume : </Text>
          <TextInput 
          style={{height:inputH,flex:inputFlex}}
          placeholder="(value)"
          onChangeText={(volume) => this.setState({volume})} />
          <View style={styles.spacer} />
        </View>
        <Divider style={{backgroundColor: '#FFFFFF'}}/>
        <View >
          <Button title="Calculate" onPress={()=>this.calcDensity(this.state)} />
        </View>
        <Divider style={{backgroundColor: '#FFFFFF'}}/>
        <View  style={styles.row}>
          <View style={styles.spacer} />
          <Text style={styles.textBox1}>Results : </Text>
          <Text style={styles.textBox2}>{ this.state.density }</Text>
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
    justifyContent: 'space-around'
  },
  textBox1: {
    flex:0.5,
    color: '#FFFFFF',
    width:100,
    height:20,
    margin:20,
    fontSize:19,
  },
  textBox2: {
    flex:0.5,
    color: '#FFFFFF',
    textAlign: 'right',
    width:50,
    height:20,
    margin:20,
    fontSize:19,
  },
  units:{
    color: '#FFFFFF',
    width:10,
    height:20,
    margin:20,
    fontSize:19,
  },
  spacer: {
    width:50,
    height:10
  }
})

AppRegistry.registerComponent('Density', () => Density);