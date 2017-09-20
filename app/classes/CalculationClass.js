'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  Text,
  TextInput,
  View,
  Button,
  StyleSheet,
  TouchableHighlight,
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

var index = 0
export default class CalculationClass extends Component {
  static navigationOptions = ({ navigation }) => {
    const {state, setParams, navigate} = navigation;
    return {
      title: 'CalculationClass',
      headerRight: <Button
      title="Settings" onPress={()=>navigate('Settings')}/>,
    }
  };

  calcDensity = function(state) {
    var m = parseFloat(state.cLines[0].input)
    var v = parseFloat(state.cLines[1].input)
    var d = m / v
    this.setState({
      resultVal: d
    })
  }

  constructor(props) {
    super(props);
    this.state = {

      varLabels: props.varLabels,
      cLines: [],//[{label:"Mass",input:0},{label:"Volume",input:0}],
      resultVal: 'N/A',

    };
    this.initiateLines();
  }

  //make calc lines
  initiateLines = () => {
        for(let i=0;i<this.state.varLabels.length;i++){
            this.state.cLines.push({label:this.state.varLabels[index],input:0})
            let temp = index ++
            this.setState({
                cLines: this.state.cLines
            })
        }
  }

    render() {
      //const { navigate } = this.props.navigation;
      //const { params } = this.props.navigation.state;
        let Arr=[];
        this.state.cLines.map((cLine,i) => {
                var label=cLine.label;
                var input=cLine.input;
                //take the label and input from cLine then push it into the array
                //for the ontext change in TextInput, copy the cLine array, change something
                //then change state of copied array
                Arr.push(
               <View key={i} style={styles.row}>
                     <View style={styles.spacer}/>
                     <Text style={styles.textBox1}> {label} </Text>
                     <TextInput
                         style={{height:inputH,flex:inputFlex}}
                         placeholder="(value)"
                         onChangeText={(input) => {
                            let copyArray=[...this.state.cLines];
                            copyArray[i].input=input;
                            this.setState({copyArray});
                            }
                         } />

                     <View style={styles.spacer}/>
                </View>
               )


        })


      return (
        <View style={styles.container}>
          { Arr }
          <TouchableHighlight style={ styles.button } onPress={ () => this.calcDensity(this.state) }>
              <Text>Calculate</Text>
          </TouchableHighlight>
          <View  style={styles.row}>
                    <View style={styles.spacer} />
                    <Text style={styles.textBox1}>Results : </Text>
                    <Text style={styles.textBox2}>{ this.state.resultVal }</Text>
                    <View style={styles.spacer} />
          </View>
        </View>

      );
    }
}


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
});

AppRegistry.registerComponent('CalculationClass', () => CalculationClass);
