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
  Modal,
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
      inputHelperSchemes: props.inputHelperSchemes,
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
                                    inputHelperScheme:this.state.inputHelperSchemes[i],
                                    inputHelper:[],
                                    modalVisible:false,
                                    //unitSet:this.state.unitSets[i]
                                    });
            await this.GetDerivedUnits(this.state.unitSets[i],this.state.cLines[i].unitSet);
            if(this.state.cLines[i].unitSet.length>0){
                this.state.cLines[i].thisUnit=this.state.cLines[i].unitSet[0];
            }
            if(this.state.inputHelperSchemes[i]=="Pipe"){
                this.state.cLines[i].inputHelper.push({SelectedNPSS:'',SelectedPS:'', NPSindex:0, Schedindex:0, innerDiameter:(10.26-2*0.889)});
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

  helperModal=function(scheme,cLineKey){

        if(scheme=="Pipe"){
            return <Button title="..."
                           onPress={()=>{
                               let copyArray=[...this.state.cLines];
                               copyArray[cLineKey].modalVisible=true;
                               this.setState({copyArray});

                           }}
                           style={styles.helperButton}/>;

        }else{
            return <View style={styles.helperSpacer}/>;
        }
  }



  openPipeHelp=function(scheme){
        if(scheme=="Pipe"){

            var NPSSizes=["1/8","1/4","3/8","1/2","3/4","1","1 1/4","1 1/2", "2", "2 1/2", "3", "3 1/2",
                                    "4","4 1/2", "5", "6", "7", "8", "9", "10", "12", "14", "16", "18", "20", "22", "24",
                                    "26","28","30","32","34","36","40","42","44","46","48","52","56","60","64","68","72"];
            let NPSSizesSet = NPSSizes.map((item,unitkey) => {
                                return <Picker.Item key={unitkey} value={item} label={item}/>
                            });

            var PipeSchedules=["5s","5","10s","10","20","30","40s","STD","40","60","80s","80","XS","100","120","140","160","XXS"];
            let PipeSchedulesSet = PipeSchedules.map((item,unitkey) => {
                                            return <Picker.Item key={unitkey} value={item} label={item}/>
                                        });



            var Arr;
            this.state.cLines.map((cLine,i)=>
                cLine.inputHelper.map((helper,j) =>
                     {
                         var SelectedNPSS=helper.SelectedNPSS;
                         var SelectedPS=helper.SelectedPS;
                         var NPSindex=helper.NPSindex;
                         var Schedindex=helper.Schedindex;
                         var innerDiameter=helper.innerDiameter;


                         //    innerDiameter = convertToSI(innerDiameter, "mm");
                         //    innerDiameter = convertFromSI(innerDiameter, calcLine.PopupPage.resultDropDown
                         //            .getSelectedItem().toString());



                        Arr=(<Modal
                               animationType="slide"
                               transparent={false}
                               visible={this.state.cLines[i].modalVisible}
                               onRequestClose={() => {
                                                        //let copyArray=[...this.state.cLines];
                                                        //copyArray[cLineKey].modalVisible=false;
                                                        //this.setState({copyArray});
                                                     }
                                              }
                               >
                               <View style={styles.container}>
                                 <View style={styles.row}>
                                     <Text style={{flex:0.5,
                                                     color: 'black',
                                                     textAlign: 'center',
                                                     width:50,
                                                     height:50,
                                                     margin:10,
                                                     fontSize:19}}
                                     >NPS Size
                                     </Text>
                                     <Picker
                                             style={{
                                                            flex:0.5,
                                                            width:90,
                                                            height:50
                                                        }}
                                             itemStyle={styles.pickerItem1}
                                             mode="dropdown"
                                             selectedValue={SelectedNPSS}
                                             onValueChange={(newVal, newValIndex) => {
                                                             this.updateID("NPS",newVal,newValIndex,i);
                                                             }}>
                                             {NPSSizesSet}
                                     </Picker>

                                 </View>
                                 <View style={styles.row}>
                                     <Text style={{flex:0.5,
                                                     color: 'black',
                                                     textAlign: 'center',
                                                     width:50,
                                                     height:50,
                                                     margin:10,
                                                     fontSize:19}}
                                     >Schedule
                                     </Text>

                                     <Picker
                                             style={{
                                                        flex:0.5,
                                                        width:90,
                                                        height:50
                                                    }}
                                             itemStyle={styles.pickerItem1}
                                             mode="dropdown"
                                             selectedValue={SelectedPS}
                                             onValueChange={(newVal, newValIndex) => {
                                                             this.updateID("Schedule",newVal,newValIndex,i);
                                                             }}>
                                             {PipeSchedulesSet}
                                     </Picker>


                                 </View>
                                 <View style={styles.row}>
                                     <Text style={{flex:0.5,
                                                     color: 'black',
                                                     textAlign: 'center',
                                                     width:1200,
                                                     height:50,
                                                     margin:10,
                                                     fontSize:19}}
                                     >{innerDiameter}
                                     </Text>
                                 </View>


                                 <Button title="Confirm"
                                                onPress={()=>{
                                                    let copyArray=[...this.state.cLines];
                                                    copyArray[i].modalVisible=false;
                                                    copyArray[i].input=innerDiameter;
                                                    copyArray[i].displayInput=''+innerDiameter;
                                                    this.setState({copyArray});

                                                }}
                                                style={styles.button}/>
                               </View>

                             </Modal>);
                     }
                )//function
            )
            return (
                    Arr
                    ); //return Modal
        }else{
            return;
        }
  }

    updateID = (whichVal,theVal,theIndex,i)=>{

           var NPSOD=[
                                                    10.26, 13.72, 17.15, 21.34, 26.67, 33.40, 42.16, 48.26, 60.33, 73.03, 88.90, 101.60,
                                                    114.30, 127.00, 141.30, 168.28, 193.68, 219.08, 244.48,
                                                    273.05, 323.85, 355.60, 406.40, 457.20, 508.00, 558.80, 609.60,
                                                    660.400,    711.200,    762.000,    812.800,    863.600,    914.400,
                                                    1016.000,   1066.800,   1117.600,   1168.400,   1219.200,   1320.800,
                                                    1422.400,   1524.000,   1625.600,   1727.200,   1828.800
                                            ];

           var NPSWallThickness=[
                                //5s    5       10s     10      20      30      40s     STD     40
                                //      60      80s     XS      80      100     120     140     160     XXS
                                //1/8
                                [0.889, null,   1.245,  null,   1.245,  1.448,  1.727,  1.727,  1.727,
                                        null,   2.413,  2.413,  2.413,  null,   null,   null,   null,   null],
                                //1/4
                                [1.245, null,   1.651,  null,   1.651,  1.854,  2.235,  2.235,  2.235,
                                        null,   3.023,  3.023,  3.023,  null,   null,   null,   null,   null],
                                //3/8
                                [1.245, null,   1.651,  null,   1.651,  1.854,  2.311,  2.311,  2.311,
                                        null,   3.200,  3.200,  3.200,  null,   null,   null,   null,   null],
                                //1/2
                                [1.651, null,   2.108,  null,   2.108,  2.413,  2.769,  2.769,  2.769,
                                        null,   3.734,  3.734,  3.734,  null,   null,   null,   4.775,  7.468],
                                //3/4
                                [1.651, null,   2.108,  null,   2.108,  2.413,  2.870,  2.870,  2.870,
                                        null,   3.912,  3.912,  3.912,  null,   null,   null,   5.563,  7.823],
                                //1
                                [1.651, null,   2.769,  null,   2.769,  2.896,  3.378,  3.378,  3.378,
                                        null,   4.547,  4.547,  4.547,  null,   null,   null,   6.350,  9.093],
                                //1 1/4
                                [1.651, null,   2.769,  null,   2.769,  2.972,  3.556,  3.556,  3.556,
                                        null,   4.851,  4.851,  4.851,  null,   null,   null,   6.350,  9.703],
                                //1 1/2
                                [1.651, null,   2.769,  null,   2.769,  3.175,  3.683,  3.683,  3.683,
                                        null,   5.080,  5.080,  5.080,  null,   null,   null,   7.137,  10.160],

                                //5s    5       10s     10      20      30      40s     STD     40
                                //      60      80s     XS      80      100     120     140     160     XXS

                                //2
                                [1.651, null,   2.769,  null,   2.769,  3.175,  3.912,  3.912,  3.912,
                                        null,   5.537,  5.537,  5.537,  null,   6.350,  null,   8.738,  11.074],
                                //2 1/2
                                [2.108, null,   3.048,  null,   3.048,  4.775,  5.156,  5.156,  5.156,
                                        null,   7.010,  7.010,  7.010,  null,   7.620,  null,   9.525,  14.021],
                                //3
                                [2.108, null,   3.048,  null,   3.048,  4.775,  5.486,  5.486,  5.486,
                                        null,   7.620,  7.620,  7.620,  null,   8.890,  null,   11.125, 15.240],
                                //3 1/2
                                [2.108, null,   3.048,  null,   3.048,  4.775,  5.740,  5.740,  5.740,
                                        null,   8.077,  8.077,  8.077,  null,   null,   null,   null,   16.154],
                                //4
                                [null,  2.108,  3.048,  3.048,  null,   4.775,  6.020,  6.020,  6.020,
                                        null,   8.560,  8.560,  8.560,  null,   11.100, null,   13.487, 17.120],
                                //4 1/2
                                [null,  null,   null,   null,   null,   null,   6.274,  6.274,  6.274,
                                        null,   9.017,  9.017,  9.017,  null,   null,   null,   null,   18.034],

                                //5s    5       10s     10      20      30      40s     STD     40
                                //      60      80s     XS      80      100     120     140     160     XXS

                                //5
                                [null,  2.769,  3.404,  3.404,  null,   null,   6.553,  6.553,  6.553,
                                        null,   9.525,  9.525,  9.525,  null,   12.700, null,   15.875, 19.050],
                                //6
                                [null,  2.769,  3.404,  3.404,  null,   null,   7.112,  7.112,  7.112,
                                        null,   10.973, 10.973, 10.973, null,   14.275, null,   18.263, 21.946],
                                //7
                                [null,  null,   null,   null,   null,   null,   7.645,  7.645,  7.645,
                                        null,   12.700, 12.700, 12.700, null,   null,   null,   null,   22.225],

                                //8
                                [null,  2.769,  3.759,  3.759,  6.350,  7.036,  8.179,  8.179,  8.179,
                                        10.312, 12.700, 12.700, 12.700, 15.062, 18.263, 20.625, 23.012, 22.226],
                                //9
                                [null,  null,   null,   null,   null,   null,   8.687,  8.687,  8.687,
                                        null,   12.700, 12.700, 12.700, null,   null,   null,   null,   null],

                                //5s    5       10s     10      20      30      40s     STD     40
                                //      60      80s     XS      80      100     120     140     160     XXS
                                //10
                                [3.404, 3.404,  4.191,  4.191,  6.350,  7.798,  9.271,  9.271,  9.271,
                                        12.700, 12.700, 12.700, 15.062, 18.237, 21.412, 25.400, 28.575, null],
                                //12
                                [3.962, 3.962,  4.572,  4.572,  6.350,  8.382,  9.525,  9.525,  10.312,
                                        14.275, 12.700, 12.700, 17.450, 21.412, 25.400, 28.575, 33.325, null],
                                //14
                                [3.962, 3.962,  4.775,  6.350,  7.925,  9.525,  9.525,  9.525,  11.100,
                                        15.062, 12.700, 12.700, 19.050, 23.800, 27.762, 31.750, 35.712, null],
                                //16
                                [4.191, 4.191,  4.775,  6.350,  7.925,  9.525,  9.525,  9.525,  12.700,
                                        16.662, 12.700, 12.700, 21.412, 26.187, 30.937, 36.500, 40.462, null],
                                //18
                                [4.191, 4.191,  4.775,  6.350,  7.925,  11.100, 9.525,  9.525,  14.275,
                                        19.050, 12.700, 12.700, 23.800, 29.362, 34.925, 39.675, 45.237, null],
                                //20
                                [4.775, 4.775,  5.537,  6.350,  9.525,  12.700, 9.525,  9.525,  15.062,
                                        20.625, 12.700, 12.700, 26.187, 32.512, 38.100, 44.450, 49.987, null],

                                //5s    5       10s     10      20      30      40s     STD     40
                                //      60      80s     XS      80      100     120     140     160     XXS

                                //22
                                [4.775, 4.775,  5.537,  6.350,  9.525,  12.700, 9.525,  9.525,  null,
                                        22.225, 12.700, 12.700, 28.575, 34.925, 41.275, 47.625, 53.975, null],

                                //24
                                [5.537, 5.537,  6.350,  6.350,  9.525,  14.275, 9.525,  9.525,  17.450,
                                        24.587, 12.700, 12.700, 30.937, 38.887, 46.025, 52.375, 59.512, null],

                                //26
                                [null,  null,   null,   7.925,  12.700, null,   9.525,  9.525,  null,
                                        null,   null,   null,   null,   null,   null,   null,   null,   null],

                                //28
                                [null,  null,   null,   7.925,  12.700, 15.875, 9.525,  9.525,  null,
                                        null,   null,   null,   null,   null,   null,   null,   null,   null],

                                //30
                                [6.350, null,  7.925,   7.925,  12.700, 15.875, 9.525,  9.525,  null,
                                        null,   null,   null,   null,   null,   null,   null,   null,   null],

                                //5s    5       10s     10      20      30      40s     STD     40
                                //      60      80s     XS      80      100     120     140     160     XXS
                                //32
                                [null,  null,   null,   7.925,  12.700, 15.875, 9.525,  9.525,  17.475,
                                        null,   null,   null,   null,   null,   null,   null,   null,   null],

                                //34
                                [null,  null,   null,   7.925,  12.700, 15.875, 9.525,  9.525,  17.475,
                                        null,   null,   null,   null,   null,   null,   null,   null,   null],

                                //36
                                [null,  null,   null,   7.925,  12.700, null,   9.525,  9.525,  null,
                                        null,   null,   null,   null,   null,   null,   null,   null,   null],

                                //40
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //42
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //44
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //46
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //48
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //52
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //56
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //60
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                                //64
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],
                                //68
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],
                                //72
                                [null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                                        null,   null,   12.700, null,   null,   null,   null,   null,   25.400],

                        ];

           let copyArray=[...this.state.cLines[i].inputHelper];
           if(whichVal=="NPS"){
                copyArray[0].SelectedNPSS=theVal;
                copyArray[0].NPSindex=theIndex;
           }
           else if(whichVal=="Schedule"){
                copyArray[0].SelectedPS=theVal;
                copyArray[0].Schedindex=theIndex;
           }
           var NPSindex=copyArray[0].NPSindex;
           var Schedindex=copyArray[0].Schedindex;
           var outerDiameter = NPSOD[NPSindex];
           var wallThickness = NPSWallThickness[NPSindex][Schedindex];

           if(wallThickness!=null){
                copyArray[0].innerDiameter=(outerDiameter-(2*wallThickness)).toFixed(6);
           }
           else{
                copyArray[0].innerDiameter='N/A';
           }
           this.setState({copyArray});

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
                var modalVisible = cLine.modalVisible;
                //let inputHelpArr=[];
                //inputHelpArr.push(this.helperModal(inputHelperScheme));

                //take the label and input from cLine then push it into the array
                //for the ontext change in TextInput, copy the cLine array, change something
                //then change state of copied array
                Arr.push(
               <View key={i} style={styles.row}>

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
                     {this.helperModal(inputHelperScheme,i)}
                     {this.openPipeHelp(inputHelperScheme)}
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
      flex:0.10,
      height:20,
      width:20,
      backgroundColor: '#ededed',
      //marginTop:10,
      justifyContent: 'center',
      alignItems: 'center'
    },
    helperSpacer: {
          flex:0.1,
          height:10,
          width:20

        },

    row: {
      flexDirection: 'row',
      justifyContent: 'space-around'
    },
    textBox1: {
      flex:0.3,
      color: '#FFFFFF',
      textAlign: 'left',
      width:100,
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
        flex:0.3,
        width:90,
        height:50
    },
    pickerItem1:{

          //color: '#FFFFFF',
          color: 'black',
          textAlign: 'center',
          //width:50,
          //height:50,
          //margin:10,
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
