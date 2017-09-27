'use strict';

import React, { Component } from 'react';
import {
  Alert,
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
global.inputFlex=0.33;
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
        Alert.alert('Return value is',''+newval);
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
      resultUnitSet: props.resultUnitSet,
      resultUnitSetArray:[],
      cLines: [],//[{label:"Mass",input:0},{label:"Volume",input:0}],
      resultVal: 'N/A',//props.calcResult,
      calcFunction: props.calcFunction

    };

    this.GetDerivedUnits(this.state.resultUnitSet,this.state.resultUnitSetArray)

    this.initiateLines();
  }

  GetDerivedUnits= async (scheme,unitArray)=>{
      try{
          let asyncUnitSet = await CPPConnection.GetDerivedUnits(scheme);
          for (let i=0;i<asyncUnitSet.length;i++){
              unitArray.push(asyncUnitSet[i])
              this.setState({
                              unitArray: unitArray
                          })
          }
          console.log(asyncUnitSet);
      }catch(e){
          console.error(e);
      }
    }

  //make calc lines
  initiateLines = async () => {
        for(let i=0;i<this.state.varLabels.length;i++){


            this.state.cLines.push({label:this.state.varLabels[i],
                                    input:this.state.calcVals[i],
                                    displayInput:this.state.calcVals[i],
                                    SIInput:'',
                                    unitSet:[],
                                    thisUnit:''
                                    //unitSet:this.state.unitSets[i]
                                    });
            await this.GetDerivedUnits(this.state.unitSets[i],this.state.cLines[i].unitSet);
            //let copyArray=[...this.state.cLines];
            //copyArray[i].thisUnit=copyArray[i].unitSet[0];
            //this.setState({copyArray});
            this.state.cLines[i].thisUnit=this.state.cLines[i].unitSet[0];
            this.setState({
                cLines: this.state.cLines
            });
        }
  }

  convertToSI=async(value,fromUnit,callBack)=>{
        var SIValue=await CPPConnection.CPPConvertToSI(value,fromUnit);
        callBack(SIValue);
  }

  convertFromSI=async(value,toUnit,callBack)=>{
        var NewValue=await CPPConnection.CPPConvertFromSI(value,toUnit);
        callBack(NewValue);

  }



  convertUnit=async(value,fromUnit,toUnit,callBack)=>{
        var SIValue=await CPPConnection.CPPConvertToSI(value,fromUnit);
        var NewValue=await CPPConnection.CPPConvertFromSI(SIValue,toUnit);
        callBack(NewValue);

  }


    render() {
        let Arr=[];
        this.state.cLines.map((cLine,i) => {
                //set variables bound to different array elements of Calc Lines
                var label=cLine.label;
                var input=cLine.input;
                var displayInput=cLine.displayInput;
                let unitSet = cLine.unitSet.map((item,unitkey) => {
                    return <Picker.Item key={unitkey} value={item} label={item}/>
                });
                var thisUnit=cLine.thisUnit;

                //Alert.alert('checkUnit',thisUnit);

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
                         onChangeText={(newValue) => {
                            Alert.alert('New Val',newValue);
                            if(newValue && !isNaN(parseFloat(newValue)) && isFinite(newValue)){
                                //let copyArray=[...this.state.cLines];
                                //copyArray[i].input=newValue; //set input of Calc Line to new value
                                //copyArray[i].displayInput=''+newValue; //set display to new value
                                this.convertToSI(parseFloat(newValue),thisUnit,
                                              function (val){
                                              let copyArray=[...this.state.cLines];
                                              copyArray[i].input=newValue; //set input of Calc Line to new value
                                              copyArray[i].displayInput=''+newValue; //set display to new value
                                              copyArray[i].SIInput=val;
                                              this.setState({copyArray});
                                              this.state.calcFunction(this.state,    //do calculation
                                                   this.updateDisplayResult.bind(this));
                                                  }.bind(this));

                                //this.setState({copyArray});            //update calc line attributes
                                //this.state.calcFunction(this.state,    //do calculation
                                                         //this.updateDisplayResult.bind(this));

                            }
                            else{

                                this.setState({resultVal: 'N/A'})      //if input is not valid no calculation to be done
                                                                       //update the result value to 'N/A'

                            }
                         }



                         } >{displayInput}</TextInput>
                     <Picker
                        style={styles.picker1}
                        itemStyle={styles.pickerItem1}
                        mode="dropdown"
                        selectedValue={thisUnit}
                        onValueChange={(newUnit, newUnitIndex) => {

                                                    if(input && !isNaN(parseFloat(input)) && isFinite(input)){
                                                        this.convertUnit(parseFloat(input),thisUnit,newUnit,
                                                          function (val){
                                                          let copyArray=[...this.state.cLines];
                                                          copyArray[i].input=val;
                                                          copyArray[i].displayInput=''+val.toFixed(4);
                                                          copyArray[i].thisUnit=newUnit;
                                                          this.setState({copyArray});
                                                          this.state.calcFunction(this.state,
                                                                                  this.updateDisplayResult.bind(this));
                                                              }.bind(this));
                                                    }
                                                }
                                              }>
                        {unitSet}
                     </Picker>

                     <View style={styles.spacer}/>
                </View>
               )


        })


      let resultUnitSet = this.state.resultUnitSetArray.map((item,unitkey) => {
                          return <Picker.Item key={unitkey} value={item} label={item}/>
                      });
      var firstResultUnit=resultUnitSet[0];

      return (
        <View style={styles.container}>
          { Arr }
          <View  style={styles.row}>
                    <View style={styles.spacer} />
                    <Text style={styles.textBox1}>Results : </Text>
                    <Text style={styles.textBox2}>{ this.state.resultVal }</Text>
                    <Picker style={styles.picker1}
                            itemStyle={styles.pickerItem1}
                            mode="dropdown"
                            selectedValue={firstResultUnit}>
                                            {resultUnitSet}
                    </Picker>
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
      flex:0.33,
      color: '#FFFFFF',
      textAlign: 'left',
      width:100,
      height:20,
      margin:10,
      fontSize:16,
    },
    textBox2: {
      flex:0.33,
      color: '#FFFFFF',
      textAlign: 'left',
      width:50,
      height:20,
      margin:10,
      fontSize:19,
    },
    picker1:{
        width:60
    },
    pickerItem1:{
        flex:0.33,
          //color: '#FFFFFF',
          color: 'black',
          textAlign: 'center',
          width:50,
          height:50,
          margin:10,
          fontSize:12,
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
