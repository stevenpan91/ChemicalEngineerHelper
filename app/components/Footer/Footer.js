import React, { Component } from 'react';
import { AppRegistry, Text, View, Button, StyleSheet} from 'react-native';

// Add in response to opening keyboard
export default class Footer extends Component {
  render(){
    return(
      <View style={{
        flex:0.06,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems:'center',
        backgroundColor: 'powderblue',
        }}>
          <View style={{
            flex:1,
            flexDirection:'row',
            justifyContent:'space-around',
            alignItems:'flex-start'
            }}>
            <Button 
            title="Home"
            accessibilityLabel="Take to Equation Screen">
            </Button>
            <Button 
            title="^"
            accessibilityLabel="Back to Current Calculation">
            </Button>
            <Button 
            title="History"
            accessibilityLabel="View Past Calculations">
            </Button>
          </View>
        </View>
    );
  }
}

const styles = StyleSheet.create({
  bar: {
    height: 50,
    backgroundColor: 'red'
  },
  button: {
    textAlign: 'center',
    color: '#FFFFFF',
    backgroundColor: '#033BE5',
    marginBottom: 5,
    padding: 12,
    overflow: 'hidden',
    borderRadius: 6,
  },
});

AppRegistry.registerComponent('Footer', () => Footer);