#include <jni.h>
#include <string>
#include <vector>
#include <cmath>
#include <iostream>
#include <sstream>
#include <ctype.h>
#include <stdlib.h>
#define elif else if

double FetchConversionFactorSI(std::string unit){
    //for future note, conversion factor means how many of the unit
    //being passed to this function would make up the unit in SI

    double returnVal;

    //length
    if(unit=="mm")
        returnVal=1000.0;
    elif(unit=="in")
        returnVal=12*3.28084;
    elif(unit=="ft")
        returnVal=3.28084;
    //pressure
    elif(unit=="kPa")
        returnVal=1.0/1000;
    elif(unit=="psi")
        returnVal=14.6959/1.01325e5;
    elif(unit=="bar")
        returnVal=1.0/1e5;
    elif(unit=="atm")
        returnVal=1.0/1.01325e5;

        //temperature (scale)
    elif(unit=="F"||unit=="R")
        returnVal=(5.0/9);

        //mass
    elif(unit=="lb")
        returnVal=2.20462;

        //time
    elif(unit=="hr")
        returnVal=1/3600.0;
    elif(unit=="day")
        returnVal=1/24.0/3600.0;
    elif(unit=="yr")
        returnVal=1/365.2422/24.0/3600.0;



    else
        returnVal=1;


    return returnVal;

}

double DoCalculation(double value,std::string unit, std::string powerstr, int position, bool toSI){

    double returnVal=value;

    double power=1; //default


    //Get conversion factor for this unit to SI
    double convFac=FetchConversionFactorSI(unit);


    //Else power is just 1
    if (!powerstr.empty()) {
        power = atof(powerstr.c_str());
    }

    //Numerator op
    if(position==0) {
        if (toSI) // to SI use the factor one way, from SI use the other
            returnVal = returnVal / pow(convFac, power);
        else
            returnVal=returnVal*(pow(convFac,power));
    }
        //Denom op
    else{
        if (toSI)
            returnVal=returnVal*(pow(convFac,power));
        else
            returnVal = returnVal / pow(convFac, power);

    }

    return returnVal;
}


double StringParseConvert (double value,std::string unit, bool toSI){
    double returnVal=value;

    bool inParenth = false; //if open parenthesis is parsed then front  slash will make
                                   //The positions switch
    bool doCalc =false; //if store previous is true, everything up to this point is enough information
                               //to create a conversion instruction set

    int numCalcs=0;

    std::string tempunit;

    int position=0; //0 is numerator 1 is denominator

    std::string temppowerstr ; //power of unit such as 3 for cubic meters m3 for volume
    //double temppower=1;

    double convFac; //Conversion factor

    //Parse from string
    for(char& c: unit){
        if (isalpha(c)){
            tempunit+=c;
        }
        else if (isdigit(c)){
            temppowerstr+=c;
        }
        else{
            doCalc=true;
        }


        if(doCalc){



            returnVal=DoCalculation(returnVal,tempunit,temppowerstr,position,toSI);

            //Reset everything
            tempunit.clear();
            temppowerstr.clear();
            //temppower=1;


            doCalc=false;
            numCalcs++;
        }

        if (c=='('){
            inParenth=true;
        }
        else if (c==')'){
            inParenth=false;
        }
        else if (c=='/' && inParenth==false){
            position=1; //If there's no parenthesis involved then pos should be denom when divide sign shows up    
        }
        else if (c=='/' && inParenth== true) {
            position = 0; //In denom of denom is in num
        }



    }

    //Just temperature conversion
    if (numCalcs==0&&(tempunit=="C"||tempunit=="F"||tempunit=="R")){
        if(tempunit=="C")
            returnVal=returnVal+273.15;
        elif(tempunit=="F")
            returnVal=(5.0/9*(returnVal-32))+273.15;
        elif(tempunit=="R")
            returnVal=(5.0/9*returnVal-32-459.67)+273.15;
    }

    else {

        returnVal=DoCalculation(returnVal,tempunit,temppowerstr,position,toSI);

    }

    //Reset everything
    temppowerstr.clear();
    tempunit.clear();
    //temppower=1;






    return returnVal;
}



extern "C"
jstring
Java_localhost_steven_chemicalengineerhelper_MainPage_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Testing C++ Connection.";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
jdouble
Java_localhost_steven_chemicalengineerhelper_CalculateScreen_convertToSI(
        JNIEnv* env,
        jobject thisObj, jdouble value, jstring unit){

        jboolean iscopy=true;
        const char* getUnitCC = env->GetStringUTFChars(unit,&iscopy);
        std::string getUnit = getUnitCC;

        double result = StringParseConvert(value,getUnit,true);


        env->ReleaseStringUTFChars(unit,getUnitCC);
        return (jdouble) result;
}

extern "C"
jdouble
Java_localhost_steven_chemicalengineerhelper_CalculateScreen_convertFromSI(
        JNIEnv* env,
        jobject thisObj, jdouble value, jstring unit){

    jboolean iscopy=true;
    const char* getUnitCC = env->GetStringUTFChars(unit,&iscopy);
    std::string getUnit = getUnitCC;

    double result = StringParseConvert(value,getUnit,false);


    env->ReleaseStringUTFChars(unit,getUnitCC);
    return (jdouble) result;
}

double FlowVelocity(double massFlow, double density, double pipeId){
    double flow=(massFlow/density/(M_PI*pow(pipeId/2.0,2.0)));
    return flow;
}

double ReynoldsNumber(double density, double massFlow, double pipeId, double viscosity){
    double result = density
                    * FlowVelocity(massFlow,density,pipeId)
                    *pipeId
                    /viscosity;
    return result;
}

double FrictionFactor(double density, double massFlow, double pipeId, double viscosity,double roughness){
    double Re = ReynoldsNumber(density,massFlow,pipeId,viscosity);

    double fricFac;

    fricFac=64/Re;

    if(Re>4000){
        double f = fricFac;
        double d = pipeId;
        double eps = roughness;

        double tol=1e-6;

        double left,right,error;
        double d_left,d_right;


        bool finished=false;

        int iterations=0;

        do {

            //left term in Colebrook-White equation
            left = 1 / sqrt(f);

            //right term in CW equation
            right = 2 * log(
                    (eps / (3.7 * d)) +
                    (2.51 / (Re * sqrt(f)))
            );

            d_left = -1/(2*pow(f,3/2.0));
            d_right= -2.51/
                    (
                            (2.51*f)+
                            (Re*(eps/(3.7*d))*pow(f,3/2.0))

                    );

            error = pow(left + right, 2.0);

            if (error > tol) {

                f=f-(left+right)/(d_left+d_right);



            } else {
                finished = true; // should probably use do while loop, fix later
                fricFac=f;
            }


            iterations++;

            if (iterations>200) {
                finished = true;
                //fricFac=f;
            }

        }while(!finished);
    }


    return fricFac;
}

double VaporDensityZFactor(double pressure,double temp, double MW, double Z){
    //MW is in g/mol need to convert to kg/mol so factor of 1000
    double density = pressure*MW/1000/Z/8.3144598/temp;
    return density;

}

double PipePressureDropCompressible(double initPressure,double initTemp, double MW, double Z, double mu,
                                    double massFlow, double pipeId, double pipeLen, double roughness){

    //use algorithm for total Length L, first cut segment L1, arbitrary alpha a
    //L=L1*(a^0 + a^1 + ... + a^9) where a is less than 1 such that the segments at the end are
    //smallest

    double alpha=7/8.0;

    //sum series of alpha^0 to alpha^9
    double summation = (1-pow(alpha,10.0))/(1-alpha);

    //segment length, segment DP
    double thisLength;
    double thisDP;
    double thisPressure=initPressure;

    double totalDP;
    double density=VaporDensityZFactor(thisPressure,initTemp,MW,Z);
    double fricFrac;

    double totalLen=0;

    double L1 = pipeLen/summation;

    for(int i=0; i<10; i++){
        thisLength=L1*pow(alpha,i);
        totalLen+=thisLength;
        //density=(density+VaporDensityZFactor(thisPressure,initTemp,MW,Z))/2;
        density=VaporDensityZFactor(thisPressure,initTemp,MW,Z);
        fricFrac=FrictionFactor(density,massFlow,pipeId,mu,roughness);
        thisDP=fricFrac*thisLength*density/2*pow(FlowVelocity(massFlow,density,pipeId),2.0)/pipeId;

        if(thisDP<thisPressure)
            thisPressure=thisPressure-thisDP;
        else
            thisPressure=0;

        totalDP+=thisDP;
    }

    return thisPressure;
}

double PipePressureDropIncompressible(double density, double massFlow, double pipeId, double pipeLen, double fricFrac){

    //use algorithm for total Length L, first cut segment L1, arbitrary alpha a
    //L=L1*(a^0 + a^1 + ... + a^9) where a is less than 1 such that the segments at the end are
    //smallest
    double totalDP=fricFrac*pipeLen*density/2*pow(FlowVelocity(massFlow,density,pipeId),2.0)/pipeId;


    return totalDP;
}

extern "C"
jdouble
Java_localhost_steven_chemicalengineerhelper_CalculateScreen_ReynoldsNumber(
        JNIEnv* env,
        jobject thisObj, jdouble density, jdouble massFlow, jdouble pipeId, jdouble viscosity){


    double result = ReynoldsNumber(density,massFlow,pipeId,viscosity);

    return (jdouble) result;
}

extern "C"
jdouble
Java_localhost_steven_chemicalengineerhelper_CalculateScreen_FrictionFactor(
        JNIEnv* env,
        jobject thisObj, jdouble density, jdouble massFlow, jdouble pipeId, jdouble viscosity, jdouble roughness){


    double result= FrictionFactor(density,massFlow,pipeId,viscosity, roughness);


    return (jdouble) result;
}

extern "C"
jdouble
Java_localhost_steven_chemicalengineerhelper_CalculateScreen_PipePressureDropIncompressible(
        JNIEnv* env,
        jobject thisObj, jdouble density, jdouble massFlow, jdouble pipeId, jdouble pipeLen, jdouble fricFac){


    double result= PipePressureDropIncompressible(density,massFlow,pipeId,pipeLen,fricFac);


    return (jdouble) result;
}

extern "C"
jdouble
Java_localhost_steven_chemicalengineerhelper_CalculateScreen_PipePressureDropCompressible(
        JNIEnv* env,
        jobject thisObj, jdouble initPressure, jdouble initTemp, jdouble MW,
        jdouble Z, jdouble mu,jdouble massFlow, jdouble pipeId, jdouble pipeLen,
        jdouble roughness){


    double result= PipePressureDropCompressible(initPressure,initTemp,MW,Z,mu,massFlow,pipeId,pipeLen,roughness);


    return (jdouble) result;
}