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
global.inputFlex=0.3;
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



   updateDisplayResult(newval){

        if(newval && !isNaN(newval) && isFinite(newval)){
             this.state.rLine.map((rLine,i) => {
                 this.convertFromSI(newval,rLine.thisUnit,
                    function(val){
                        //Update result Line if value is actually a number
                        let copyArray=[...this.state.rLine];
                        copyArray[0].resultVal=val;
                        this.setState({copyArray});
                    }.bind(this));
             })
        }
        else{
            //Otherwise return text N/A
            let copyArray=[...this.state.rLine];
            copyArray[0].resultVal='N/A';
            this.setState({copyArray});
        }
   }


  constructor(props) {
    super(props);
    this.state = {

      varLabels: props.varLabels,
      calcVals: props.calcVals,
      unitSets: props.unitSets,
      resultUnitSet: props.resultUnitSet,
      cLines: [],
      rLine: [],
      inputHelperSchemes: [],
      calcFunction: props.calcFunction

    };

    this.initiateLines();
  }

  //call Java method GetDerivedUnits. Use scheme such as "M" for mass and "L3" for Length^3 (Volume) and get set of units
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

        //Set calculation input lines
            this.state.cLines.push({label:this.state.varLabels[i],
                                    input:this.state.calcVals[i],
                                    displayInput:this.state.calcVals[i],
                                    SIInput:'',
                                    unitSet:[],
                                    thisUnit:'',
                                    inputHelperScheme:this.state.inputHelperSchemes[i]
                                    //unitSet:this.state.unitSets[i]
                                    });
            await this.GetDerivedUnits(this.state.unitSets[i],this.state.cLines[i].unitSet);
            if(this.state.cLines[i].unitSet.length>0){
                this.state.cLines[i].thisUnit=this.state.cLines[i].unitSet[0];
            }
            this.setState({
                cLines: this.state.cLines
            });
        }

        //Set result line
        this.state.rLine.push({label:'Results',
                               resultVal:'N/A',
                               unitSet:[],
                               thisUnit:''})

        await this.GetDerivedUnits(this.state.resultUnitSet,this.state.rLine[0].unitSet)
        if(this.state.rLine[0].unitSet.length>0){
            this.state.rLine[0].thisUnit=this.state.rLine[0].unitSet[0];
        }
        this.setState({
            rLine:this.state.rLine
        });


  }

  //call C++ method convertToSI
  convertToSI=async(value,fromUnit,callBack)=>{
        var SIValue=await CPPConnection.CPPConvertToSI(value,fromUnit);
        callBack(SIValue);
  }

  //call C++ method convertFromSI
  convertFromSI=async(value,toUnit,callBack)=>{
        var NewValue=await CPPConnection.CPPConvertFromSI(value,toUnit);
        callBack(NewValue);

  }


  //Convert from one unit to another using C++ methods
  convertUnit=async(value,fromUnit,toUnit,callBack)=>{
        var SIValue=await CPPConnection.CPPConvertToSI(value,fromUnit);
        var NewValue=await CPPConnection.CPPConvertFromSI(SIValue,toUnit);
        callBack(NewValue);

  }

  helperModal=function(scheme){

        if(scheme=="Pipe"){
            Alert.alert('Arrived','Arrived');
            return <Button style={styles.helperButton}>Test</Button>;

        }else{
            return;
        }
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
                var inputHelperScheme=cLine.inputHelperScheme;
                let inputHelpArr=[];
                inputHelpArr.push(this.helperModal(inputHelperScheme));

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
                            //check new input is numeric and not null
                            if(newValue && !isNaN(parseFloat(newValue)) && isFinite(newValue)){
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

                            }
                            else{
                                //reset input values
                                let copyArray=[...this.state.cLines];
                                copyArray[i].input=''; //set input of Calc Line to new value
                                copyArray[i].SIInput='';
                                this.setState({copyArray});

                                //reset result display
                                this.updateDisplayResult(NaN);
                                //this.setState({resultVal: 'N/A'})      //if input is not valid no calculation to be done
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
                                                    //if this input value is a number then recalculate
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
                                                    //otherwise just set the unit
                                                    else{
                                                        let copyArray=[...this.state.cLines];
                                                        copyArray[i].thisUnit=newUnit;
                                                        this.setState({copyArray});
                                                    }
                                                }
                                              }>
                        {unitSet}
                     </Picker>
                     {inputHelpArr}
                     <View style={styles.spacer}/>
                </View>
               )//end of Arr push


        })

      let rArr=[];
      this.state.rLine.map((rLine,i) => {

          var resultlabel=rLine.label;
          var resultVal=rLine.resultVal;
          let resultUnitSet = rLine.unitSet.map((item,unitkey) => {
                              return <Picker.Item key={unitkey} value={item} label={item}/>
                          });
          var resultUnit=rLine.thisUnit;

          rArr.push(
                <View  style={styles.row}>
                      <View style={styles.spacer} />
                      <Text style={styles.textBox1}>Results : </Text>
                      <Text style={styles.textBox2}>{ resultVal }</Text>
                      <Picker style={styles.picker1}
                              itemStyle={styles.pickerItem1}
                              mode="dropdown"
                              selectedValue={resultUnit}
                              onValueChange={(newUnit, newUnitIndex) => {
                                                     //if result value is a number convert it and set new unit
                                                     if(resultVal && !isNaN(parseFloat(resultVal)) && isFinite(resultVal)){
                                                         this.convertUnit(parseFloat(resultVal),resultUnit,newUnit,
                                                           function (val){
                                                           let copyArray=[...this.state.rLine];
                                                           copyArray[i].thisUnit=newUnit;
                                                           copyArray[i].resultVal=val;
                                                           this.setState({copyArray});
                                                          }.bind(this));
                                                     }
                                                     //else just update the result unit
                                                     else{
                                                          let copyArray=[...this.state.rLine];
                                                          copyArray[i].thisUnit=newUnit;
                                                          this.setState({copyArray});
                                                     }
                                                 }
                                               }>
                                              {resultUnitSet}
                      </Picker>
                      <View style={styles.spacer} />
                </View>

          )//end of rArr push

      })

      return (
        <View style={styles.container}>
          { Arr }
          { rArr}
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

    helperButton: {
      flex:0.1,
      height:20,
      width:30,
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
      flex:0.3,
      color: '#FFFFFF',
      textAlign: 'left',
      width:150,
      height:20,
      margin:10,
      fontSize:16,
    },
    textBox2: {
      flex:0.3,
      color: '#FFFFFF',
      textAlign: 'left',
      width:50,
      height:20,
      margin:10,
      fontSize:16,
    },
    picker1:{
        width:90
    },
    pickerItem1:{
        flex:0.3,
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
