import React, { Component } from 'react';
import {
  AppRegistry,
  Text,
  View,
  Alert,
  Button,
  StyleSheet,
  TouchableOpacity } from 'react-native';
import { StackNavigator } from 'react-navigation';
import CalculationClass from '../classes/CalculationClass';
import CPPConnection from '../classes/CPPConnection';
import cf from '../modules/CEHFunctions';

export default class PipePressureDropCompressible extends Component {
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'Pipe Pressure Drop Compressible',
      headerRight: <Button
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };

  calcPipeDP =function (state,updateResult) {
       var init_p_abs = parseFloat(state.cLines[0].SIInput);
       var temp = parseFloat(state.cLines[1].SIInput);
       var mw = parseFloat(state.cLines[2].SIInput);
       var z = parseFloat(state.cLines[3].SIInput);
       var visc = parseFloat(state.cLines[4].SIInput);
       var mfr = parseFloat(state.cLines[5].SIInput);
       var pid = parseFloat(state.cLines[6].SIInput);
       var pl = parseFloat(state.cLines[7].SIInput);
       var rough = parseFloat(state.cLines[8].SIInput);

       if(cf.CheckNumeric(init_p_abs) && cf.CheckNumeric(temp) && cf.CheckNumeric(mw) && cf.CheckNumeric(z) &&
          cf.CheckNumeric(visc) && cf.CheckNumeric(mfr) && cf.CheckNumeric(pid) && cf.CheckNumeric(pl) && cf.CheckNumeric(rough) )
       {
            //Alert.alert("Test passed","test passed");
            this.DPCompressible(init_p_abs,temp,mw,z,visc,mfr,pid,pl,rough,
                         function (val){
                             var DP=val;
                             updateResult(DP);
                         });
       }

    }

//    FrictionFactor=async(density,massFlow,pipeId,viscosity,roughness,callBack)=>{
//        var frictFactor = await CPPConnection.CPPFrictionFactor(density,massFlow,pipeId,viscosity,roughness);
//        //Alert.alert("Here?",''+frictFactor);
//        callBack(frictFactor);
//    }

    DPCompressible=async(initPressure,initTemp,MW,Z,mu,massFlow,pipeId,pipeLen,roughness,callBack)=>{
        var dPCompressible = await CPPConnection.CPPPipePressureDropCompressible(initPressure,initTemp,MW,Z,mu,massFlow,pipeId,pipeLen,roughness);
        //Alert.alert("dP",''+dPCompressible);
        callBack(dPCompressible);
    }

    constructor(props) {
        super(props);
        this.state = {
          init_press_abs:'',
          temperature:'',
          mol_weight:'',
          compressibility_z:'',
          viscosity:'',
          mass_flow_rate: '',
          pipe_id: '',
          pipe_length:'',
          roughness: 4.57e-5,
          DP:'N/A'
        };

      }


  render(){
    const { navigate } = this.props.navigation;
    const { params } = this.props.navigation.state;
    return (
          <CalculationClass varLabels={["Init Press (Abs)","Temp","Mol Weight", "Z Factor (Compress)", "Viscosity","Mass Flow", "Pipe ID", "Pipe Length", "Roughness"]}
                                calcVals={[this.state.init_press_abs,this.state.temperature,
                                           this.state.mol_weight,this.state.compressibility_z,
                                           this.state.viscosity,this.state.mass_flow_rate,
                                           this.state.pipe_id,this.state.pipe_length,
                                           this.state.roughness]}
                                unitSets={["P","T","","", "P*Z", "M/Z", "L","L","L"]}
                                resultUnitSet={"P"}
                                inputHelperSchemes={["","","","","","","Pipe","",""]}
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

AppRegistry.registerComponent('PipePressureDropCompressible', () => PipePressureDropCompressible);