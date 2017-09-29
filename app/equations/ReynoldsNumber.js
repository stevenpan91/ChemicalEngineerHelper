import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  Button,
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';
import CalculationClass from '../classes/CalculationClass'

export default class ReynoldsNumber extends Component {
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'Reynolds Number (Diameter)',
      headerRight: <Button 
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };

  calcReynolds = function(state,updateResult) {
     var d = parseFloat(state.cLines[0].SIInput);
     var mfr = parseFloat(state.cLines[1].SIInput);
     var pid = parseFloat(state.cLines[2].SIInput);
     var visc = parseFloat(state.cLines[3].SIInput);

     var vel = mfr/d/(Math.PI*Math.pow(pid,2)/4);

     var Re = vel*d*pid/visc;
     updateResult(Re)
  }

  constructor(props) {
      super(props);
      this.state = {
        density: '',
        mass_flow_rate: '',
        pipe_id: '',
        viscosity:'',
        Re:'N/A'
      };

    }

  render(){
    const { navigate } = this.props.navigation;
    const { params } = this.props.navigation.state;
    return (
      <CalculationClass varLabels={["Density","Mass Flow", "Pipe ID", "Viscosity"]}
                                calcVals={[this.state.density,this.state.mass_flow_rate,
                                           this.state.pipe_id,this.state.viscosity]}
                                unitSets={["M/L3","M/Z","L","P*Z"]}
                                resultUnitSet={""}
                                inputHelperSchemes={["","","Pipe",""]}
                                //calcResult={this.state.density}
                                calcFunction = {this.calcReynolds.bind(this)}/>
    )
  };
}

const styles = StyleSheet.create({
  hard: {
    color: '#FFFFFF', 
    fontSize: 20
  },
})

AppRegistry.registerComponent('ReynoldsNumber', () => ReynoldsNumber);