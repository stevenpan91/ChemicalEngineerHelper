import React, { Component } from 'react';
import { AppRegistry, Text, Image, StyleSheet} from 'react-native';

import beaker from '../../img/beaker.png';

export default class Logo extends Component {
  render(){
    return(
      <Image style={styles.image} source={require('../../img/beaker.png')} />
    );
  }
}

const styles = StyleSheet.create({
  image : {
    tintColor:'#FFFFFF',
    width: 100,
    height: 100,
  }
});

AppRegistry.registerComponent('Logo', () => Logo);