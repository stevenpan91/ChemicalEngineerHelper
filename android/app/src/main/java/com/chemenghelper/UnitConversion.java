package com.chemenghelper;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by root on 9/25/17.
 */

public class UnitConversion extends ReactContextBaseJavaModule {

    public UnitConversion(ReactApplicationContext reactContext){
        super(reactContext);
    }

    @Override
    public String getName(){
        return "UnitConversion";
    }

}