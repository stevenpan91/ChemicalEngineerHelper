import React, { Component } from 'react';

export default class CEHFunctions extends Component{
    static CheckNumeric(val){
        if((val||val===0) && !isNaN(val) && isFinite(val))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}

