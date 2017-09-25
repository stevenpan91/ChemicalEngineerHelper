'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  Text,
  TextInput,
  Picker,
  Item,
  View,
  Button,
  StyleSheet,
  TouchableHighlight,
  TouchableOpacity} from 'react-native';
import { StackNavigator } from 'react-navigation';
global.inputH = 50;
global.inputFlex=0.4;
global.inputW = 50;
import Divider from '../components/Divider/Divider';
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

export default class CalculationClass extends Component {
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'CalculationClass',
      headerRight: <Button
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };

//  calcDensity = function(state) {
//    var m = parseFloat(state.cLines[0].input)
//    var v = parseFloat(state.cLines[1].input)
//    var d = m / v
//    this.setState({
//      resultVal: d
//    })
//  }


   updateDisplayResult(newval){

        if(!isNaN(newval)){

            this.setState({
                resultVal:newval
            })
        }
   }


  constructor(props) {
    super(props);
    this.state = {

      varLabels: props.varLabels,
      calcVals: props.calcVals,
      unitSets: props.unitSets,
      cLines: [],//[{label:"Mass",input:0},{label:"Volume",input:0}],
      resultVal: 'N/A',//props.calcResult,
      calcFunction: props.calcFunction

    };

    this.initiateLines();
  }

  //make calc lines
  initiateLines = () => {
        for(let i=0;i<this.state.varLabels.length;i++){
            this.state.cLines.push({label:this.state.varLabels[i],
                                    input:this.state.calcVals[i],
                                    unitSet:this.state.unitSets[i]})
            this.setState({
                cLines: this.state.cLines
            })
        }
  }

  convertUnit=function(value,fromUnit,toUnit){
        var SIValue=CPPConnection.convertToSI(value,fromUnit);
        var NewValue=CPPConnection.convertFromSI(SIValue.toUnit);
  }


    render() {
        let Arr=[];
        this.state.cLines.map((cLine,i) => {
                var label=cLine.label;
                var input=cLine.input;

                let unitSet = cLine.unitSet.map((item,unitkey) => {
                    return <Picker.Item key={unitkey} value={item} label={item}/>
                });
                var firstUnit=unitSet[0];


                //take the label and input from cLine then push it into the array
                //for the ontext change in TextInput, copy the cLine array, change something
                //then change state of copied array
                Arr.push(
               <View key={i} style={styles.row}>
                     <View style={styles.spacer}/>
                     <Text style={styles.textBox1}> {label} </Text>
                     <TextInput
                         style={{height:inputH,width: inputW, flex:inputFlex}}
                         placeholder="(value)"
                         onChangeText={(input) => {
                            let copyArray=[...this.state.cLines];
                            copyArray[i].input=input;
                            this.setState({copyArray});
                            this.state.calcFunction(this.state,
                                                    this.updateDisplayResult.bind(this));
                            }
                         } />
                     <Picker
                        style={styles.picker1}
                        itemStyle={styles.pickerItem1}
                        mode="dropdown"
                        selectedValue={firstUnit}
                        onValueChange={(itemValue, itemIndex) => {
                                                    let copyArray=[...this.state.cLines];
                                                    copyArray[i].unit=itemValue;
                                                    this.setState({copyArray});
                                                    }
                                                  }>
                        {unitSet}
                     </Picker>

                     <View style={styles.spacer}/>
                </View>
               )


        })


//onValueChange={(itemValue, itemIndex) => {
//                            let copyArray=[...this.state.cLines];
//                            copyArray[i].unit=unit;
//                            this.setState({copyArray});
//                            }
//                          }>
//                        <Picker.Item label="m" value="m"/>

      return (
        <View style={styles.container}>
          { Arr }
          <View  style={styles.row}>
                    <View style={styles.spacer} />
                    <Text style={styles.textBox1}>Results : </Text>
                    <Text style={styles.textBox2}>{ this.state.resultVal }</Text>
                    <Picker style={styles.picker1}>
                                            <Picker.Item label="m" value="m"/>
                                         </Picker>
                    <View style={styles.spacer} />
          </View>
        </View>

      );
    }
}

//selectedValue={unit}
//        onValueChange={(itemValue, itemIndex) => {
//          let copyArray=[...this.state.cLines];
//          copyArray[i].unit=unit;
//          this.setState({copyArray});
//          }
//        }


const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop:60
  },
  button: {
    height:60,
    backgroundColor: '#ededed',
    marginTop:10,
    justifyContent: 'center',
    alignItems: 'center'
  },

    row: {
      flexDirection: 'row',
      justifyContent: 'space-around'
    },
    textBox1: {
      flex:0.4,
      color: '#FFFFFF',
      textAlign: 'left',
      width:100,
      height:20,
      margin:10,
      fontSize:16,
    },
    textBox2: {
      flex:0.4,
      color: '#FFFFFF',
      textAlign: 'left',
      width:50,
      height:20,
      margin:10,
      fontSize:19,
    },
    picker1:{
        width:50
    },
    pickerItem1:{
        flex:0.2,
          //color: '#FFFFFF',
          color: 'black',
          textAlign: 'center',
          width:50,
          height:50,
          margin:10,
          fontSize:16,
    },
    units:{
      color: '#FFFFFF',
      width:10,
      height:20,
      margin:20,
      fontSize:19,
    },
    spacer: {
      width:20,
      height:10
    }
});

AppRegistry.registerComponent('CalculationClass', () => CalculationClass);
