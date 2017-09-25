package com.chemenghelper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 9/25/17.
 */

public class CPPConnection extends ReactContextBaseJavaModule {

    public CPPConnection(ReactApplicationContext reactContext){
        super(reactContext);
    }

    @Override
    public String getName(){
        return "CPPConnection";
    }

    @ReactMethod
    public native double convertToSI(double value, String unit);

    @ReactMethod
    public native double convertFromSI(double value, String unit);


    public class DerivedUnitHelper{
        String[] unitSet;
        int position;
        double power;
    }


    /*
    Use a scheme such as "L/T" to mean length over time and this section
    will add all the possible units to the set
     */
    @ReactMethod
    public void GetDerivedUnits(String scheme,final Promise promise){


        try {

            List<String> returnSet = new ArrayList<String>();
            //Set first value so that there is a lead value
            returnSet.add("");

            int position = 0; //0 is num 1 is denom
            String tempUnit = "";
            String tempPower = "";
            List<DerivedUnitHelper> HelpUnits = new ArrayList<DerivedUnitHelper>();

            for (int i = 0; i < scheme.length(); i++) {
                char c = scheme.charAt(i);

                if (Character.isLetter(c)) {
                    tempUnit += c;
                } else if (Character.isDigit(c)) {
                    tempPower += c;
                } else {

                    SetLastDerivedUnit(HelpUnits, position, tempUnit, tempPower);
                    tempUnit = "";
                    tempPower = "";
                }

                if (c == '/') {
                    position = 1;
                }


            }

            SetLastDerivedUnit(HelpUnits, position, tempUnit, tempPower);
            tempUnit = "";
            tempPower = "";

            //Add all permutations of unit sets
            for (int j = 0; j < HelpUnits.size(); j++) {
                String[] thisSet = HelpUnits.get(j).unitSet;
                String thisPosChar = "";

                if (j != 0) {
                    if (HelpUnits.get(j).position == 0) {
                        thisPosChar = "*";
                    } else
                        thisPosChar = "/";
                }
                int currentSize = returnSet.size();
                //Traverse all units of this set
                for (int k = 0; k < thisSet.length; k++) {
                    if (thisSet[k] != "") {

                        //Append pos marker plus unit to every existing unit in returnSet
                        for (int i = 0; i < currentSize; i++) {
                            returnSet.add(returnSet.get(i) + thisPosChar + thisSet[k]);
                        }

                    }
                }

                //remove the original set
                for (int i = 0; i < currentSize; i++) {
                    returnSet.remove(0); //The indices will shift so you should always delete the first one
                    //As many times as current size
                }


            }


            //Remove lead "" value
            //returnSet.remove(0);
            String[] returnArray = new String[returnSet.size()];
            returnArray = returnSet.toArray(returnArray);

            WritableArray promiseArray=Arguments.createArray();
            for(int i=0;i<returnArray.length;i++){
                promiseArray.pushString(returnArray[i]);
            }

            promise.resolve(promiseArray);

        }
        catch(Exception e){
            promise.reject(e);
        }

//        //return returnArray;
    }

    public void SetLastDerivedUnit(List<DerivedUnitHelper> HelpUnits, int position, String tempUnit, String tempPower){
        //Process previous
        HelpUnits.add(new DerivedUnitHelper());

        DerivedUnitHelper lastDH = HelpUnits.get(HelpUnits.size()-1);
        lastDH.position=position;

        int unitIndex=-1;
        switch(tempUnit){
            //time
            case "T":
                unitIndex=1;
                break;
            //pressure
            case "P":
                unitIndex=2;
                break;
            //mass
            case "M":
                unitIndex=3;
                break;
            //length
            case "L":
                unitIndex=4;
                break;
            //time (german:zeit)
            case "Z":
                unitIndex=5;
                break;
        }
        lastDH.unitSet= Arrays.copyOf(allUnits[unitIndex],allUnits[unitIndex].length);

        //add power
        for(int j=0; j<lastDH.unitSet.length;j++){
            if (lastDH.unitSet[j]!="")
                lastDH.unitSet[j]=lastDH.unitSet[j]+tempPower;
        }
    }


    public String [][] allUnits={
            {"-","","","",""},
            {"C","K","F","R",""},
            {"Pa","kPa","bar","atm","psi"},
            {"kg","lb","","",""},
            {"m","ft","in","mm",""},
            {"s","hr","day","yr",""}

    };

    public String [] NPSSizes={
            "1/8","1/4","3/8","1/2","3/4","1","1 1/4","1 1/2", "2", "2 1/2", "3", "3 1/2",
            "4","4 1/2", "5", "6", "7", "8", "9", "10", "12", "14", "16", "18", "20", "22", "24",
            "26","28","30","32","34","36","40","42","44","46","48","52","56","60","64","68","72"

    };

    public double[] NPSOD={
            10.26, 13.72, 17.15, 21.34, 26.67, 33.40, 42.16, 48.26, 60.33, 73.03, 88.90, 101.60,
            114.30, 127.00, 141.30, 168.28, 193.68, 219.08, 244.48,
            273.05, 323.85, 355.60, 406.40, 457.20, 508.00, 558.80, 609.60,
            660.400,    711.200,    762.000,    812.800,    863.600,    914.400,
            1016.000,   1066.800,   1117.600,   1168.400,   1219.200,   1320.800,
            1422.400,   1524.000,   1625.600,   1727.200,   1828.800
    };

    public Double[][]NPSWallThickness={
            //5s    5       10s     10      20      30      40s     STD     40
            //      60      80s     XS      80      100     120     140     160     XXS
            //1/8
            {0.889, null,   1.245,  null,   1.245,  1.448,  1.727,  1.727,  1.727,
                    null,   2.413,  2.413,  2.413,  null,   null,   null,   null,   null},
            //1/4
            {1.245, null,   1.651,  null,   1.651,  1.854,  2.235,  2.235,  2.235,
                    null,   3.023,  3.023,  3.023,  null,   null,   null,   null,   null},
            //3/8
            {1.245, null,   1.651,  null,   1.651,  1.854,  2.311,  2.311,  2.311,
                    null,   3.200,  3.200,  3.200,  null,   null,   null,   null,   null},
            //1/2
            {1.651, null,   2.108,  null,   2.108,  2.413,  2.769,  2.769,  2.769,
                    null,   3.734,  3.734,  3.734,  null,   null,   null,   4.775,  7.468},
            //3/4
            {1.651, null,   2.108,  null,   2.108,  2.413,  2.870,  2.870,  2.870,
                    null,   3.912,  3.912,  3.912,  null,   null,   null,   5.563,  7.823},
            //1
            {1.651, null,   2.769,  null,   2.769,  2.896,  3.378,  3.378,  3.378,
                    null,   4.547,  4.547,  4.547,  null,   null,   null,   6.350,  9.093},
            //1 1/4
            {1.651, null,   2.769,  null,   2.769,  2.972,  3.556,  3.556,  3.556,
                    null,   4.851,  4.851,  4.851,  null,   null,   null,   6.350,  9.703},
            //1 1/2
            {1.651, null,   2.769,  null,   2.769,  3.175,  3.683,  3.683,  3.683,
                    null,   5.080,  5.080,  5.080,  null,   null,   null,   7.137,  10.160},

            //5s    5       10s     10      20      30      40s     STD     40
            //      60      80s     XS      80      100     120     140     160     XXS

            //2
            {1.651, null,   2.769,  null,   2.769,  3.175,  3.912,  3.912,  3.912,
                    null,   5.537,  5.537,  5.537,  null,   6.350,  null,   8.738,  11.074},
            //2 1/2
            {2.108, null,   3.048,  null,   3.048,  4.775,  5.156,  5.156,  5.156,
                    null,   7.010,  7.010,  7.010,  null,   7.620,  null,   9.525,  14.021},
            //3
            {2.108, null,   3.048,  null,   3.048,  4.775,  5.486,  5.486,  5.486,
                    null,   7.620,  7.620,  7.620,  null,   8.890,  null,   11.125, 15.240},
            //3 1/2
            {2.108, null,   3.048,  null,   3.048,  4.775,  5.740,  5.740,  5.740,
                    null,   8.077,  8.077,  8.077,  null,   null,   null,   null,   16.154},
            //4
            {null,  2.108,  3.048,  3.048,  null,   4.775,  6.020,  6.020,  6.020,
                    null,   8.560,  8.560,  8.560,  null,   11.100, null,   13.487, 17.120},
            //4 1/2
            {null,  null,   null,   null,   null,   null,   6.274,  6.274,  6.274,
                    null,   9.017,  9.017,  9.017,  null,   null,   null,   null,   18.034},

            //5s    5       10s     10      20      30      40s     STD     40
            //      60      80s     XS      80      100     120     140     160     XXS

            //5
            {null,  2.769,  3.404,  3.404,  null,   null,   6.553,  6.553,  6.553,
                    null,   9.525,  9.525,  9.525,  null,   12.700, null,   15.875, 19.050},
            //6
            {null,  2.769,  3.404,  3.404,  null,   null,   7.112,  7.112,  7.112,
                    null,   10.973, 10.973, 10.973, null,   14.275, null,   18.263, 21.946},
            //7
            {null,  null,   null,   null,   null,   null,   7.645,  7.645,  7.645,
                    null,   12.700, 12.700, 12.700, null,   null,   null,   null,   22.225},

            //8
            {null,  2.769,  3.759,  3.759,  6.350,  7.036,  8.179,  8.179,  8.179,
                    10.312, 12.700, 12.700, 12.700, 15.062, 18.263, 20.625, 23.012, 22.226},
            //9
            {null,  null,   null,   null,   null,   null,   8.687,  8.687,  8.687,
                    null,   12.700, 12.700, 12.700, null,   null,   null,   null,   null},

            //5s    5       10s     10      20      30      40s     STD     40
            //      60      80s     XS      80      100     120     140     160     XXS
            //10
            {3.404, 3.404,  4.191,  4.191,  6.350,  7.798,  9.271,  9.271,  9.271,
                    12.700, 12.700, 12.700, 15.062, 18.237, 21.412, 25.400, 28.575, null},
            //12
            {3.962, 3.962,  4.572,  4.572,  6.350,  8.382,  9.525,  9.525,  10.312,
                    14.275, 12.700, 12.700, 17.450, 21.412, 25.400, 28.575, 33.325, null},
            //14
            {3.962, 3.962,  4.775,  6.350,  7.925,  9.525,  9.525,  9.525,  11.100,
                    15.062, 12.700, 12.700, 19.050, 23.800, 27.762, 31.750, 35.712, null},
            //16
            {4.191, 4.191,  4.775,  6.350,  7.925,  9.525,  9.525,  9.525,  12.700,
                    16.662, 12.700, 12.700, 21.412, 26.187, 30.937, 36.500, 40.462, null},
            //18
            {4.191, 4.191,  4.775,  6.350,  7.925,  11.100, 9.525,  9.525,  14.275,
                    19.050, 12.700, 12.700, 23.800, 29.362, 34.925, 39.675, 45.237, null},
            //20
            {4.775, 4.775,  5.537,  6.350,  9.525,  12.700, 9.525,  9.525,  15.062,
                    20.625, 12.700, 12.700, 26.187, 32.512, 38.100, 44.450, 49.987, null},

            //5s    5       10s     10      20      30      40s     STD     40
            //      60      80s     XS      80      100     120     140     160     XXS

            //22
            {4.775, 4.775,  5.537,  6.350,  9.525,  12.700, 9.525,  9.525,  null,
                    22.225, 12.700, 12.700, 28.575, 34.925, 41.275, 47.625, 53.975, null},

            //24
            {5.537, 5.537,  6.350,  6.350,  9.525,  14.275, 9.525,  9.525,  17.450,
                    24.587, 12.700, 12.700, 30.937, 38.887, 46.025, 52.375, 59.512, null},

            //26
            {null,  null,   null,   7.925,  12.700, null,   9.525,  9.525,  null,
                    null,   null,   null,   null,   null,   null,   null,   null,   null},

            //28
            {null,  null,   null,   7.925,  12.700, 15.875, 9.525,  9.525,  null,
                    null,   null,   null,   null,   null,   null,   null,   null,   null},

            //30
            {6.350, null,  7.925,   7.925,  12.700, 15.875, 9.525,  9.525,  null,
                    null,   null,   null,   null,   null,   null,   null,   null,   null},

            //5s    5       10s     10      20      30      40s     STD     40
            //      60      80s     XS      80      100     120     140     160     XXS
            //32
            {null,  null,   null,   7.925,  12.700, 15.875, 9.525,  9.525,  17.475,
                    null,   null,   null,   null,   null,   null,   null,   null,   null},

            //34
            {null,  null,   null,   7.925,  12.700, 15.875, 9.525,  9.525,  17.475,
                    null,   null,   null,   null,   null,   null,   null,   null,   null},

            //36
            {null,  null,   null,   7.925,  12.700, null,   9.525,  9.525,  null,
                    null,   null,   null,   null,   null,   null,   null,   null,   null},

            //40
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //42
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //44
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //46
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //48
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //52
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //56
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //60
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

            //64
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},
            //68
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},
            //72
            {null,  null,   null,   null,   null,   null,   null,   9.525,  null,
                    null,   null,   12.700, null,   null,   null,   null,   null,   25.400},

    };

    public String[] PipeSchedules={
            "5s","5","10s","10","20","30","40s","STD","40","60","80s","80","XS","100","120","140","160","XXS"

    };
}