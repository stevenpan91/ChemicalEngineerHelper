import React, { Component } from 'react';
import { 
  AppRegistry, 
  Text, 
  View, 
  Button,
  StyleSheet, 
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';
import CPPConnection from '../classes/CPPConnection';

export default class PipePressureDrop extends Component {
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'Pipe Pressure Drop',
      headerRight: <Button 
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };

  calcPipeDP = function(state,updateResult) {
       var d = parseFloat(state.cLines[0].SIInput);
       var mfr = parseFloat(state.cLines[1].SIInput);
       var pid = parseFloat(state.cLines[2].SIInput);
       var visc = parseFloat(state.cLines[3].SIInput);

       var vel = mfr/d/(Math.PI*Math.pow(pid,2)/4);

       var Re = vel*d*pid/visc;
       updateResult(Re)
    }

    FrictionFactor=async(density,massFlow,pipeId,viscosity,roughness)=>{
        let frictFactor = await CPPConnection.CPPFrictionFactor(density,massFlow,pipeId,viscosity,roughness);
        
    }

    constructor(props) {
        super(props);
        this.state = {
          density: '',
          mass_flow_rate: '',
          pipe_id: '',
          pipe_length:'',
          viscosity:'',
          roughness: 4.57e-5,
          DP:'N/A'
        };

      }


  render(){
    const { navigate } = this.props.navigation;
    const { params } = this.props.navigation.state;
    return (
          <CalculationClass varLabels={["Density","Mass Flow", "Pipe ID", "Pipe Length", "Viscosity", "Roughness"]}
                                calcVals={[this.state.density,this.state.mass_flow_rate,
                                           this.state.pipe_id,this.pipe_length,
                                           this.state.viscosity,this.roughness]}
                                unitSets={["M/L3","M/Z","L", "L", "P*Z", "L"]}
                                resultUnitSet={"P"}
                                inputHelperSchemes={["","","Pipe","","",""]}
                                //calcResult={this.state.density}
                                calcFunction = {this.calcPipeDP.bind(this)}/>
    )
  };
}

const styles = StyleSheet.create({
  hard: {
    color: '#FFFFFF', 
    fontSize: 20
  },
})

AppRegistry.registerComponent('PipePressureDrop', () => PipePressureDrop);