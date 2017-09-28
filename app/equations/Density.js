import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  TextInput,
  View,
  Button,
  Alert,
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';
import Divider from '../components/Divider/Divider';
import CalculationClass from '../classes/CalculationClass'
import CPPConnection from '../classes/CPPConnection'
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

  calcDensity = function(state,updateResult) {
    var m = parseFloat(state.cLines[0].SIInput)
    var v = parseFloat(state.cLines[1].SIInput)
    var d = m / v
    updateResult(d)
  }



  constructor(props) {
    super(props);
    this.state = {
      mass: '',
      volume: '',
      density: 'N/A',
    };

  }

  //Use custom component CalculationClass
  //attributes:
  //    varLabels - the text used to guide the reader on what to input
  //    calcVals - initial values in the TextInput if there are any
  //    unitSets - use scheme to set unit set. This was migrated from Java native
  //                M: mass, L: length, Z: time, T: temperature, P: pressure
  //                for example L3 is volume (mass cubed)
  //    calcFunction - pass the bound function to calculate the result
  render(){
      const { navigate } = this.props.navigation;
      const { params } = this.props.navigation.state;



      return (
        <CalculationClass varLabels={["Mass","Volume"]}
                          calcVals={[this.state.mass,this.state.volume]}
                          unitSets={["M","L3"]}
                          resultUnitSet={"M/L3"}
                          //calcResult={this.state.density}
                          calcFunction = {this.calcDensity.bind(this)}/>
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