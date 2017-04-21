package localhost.steven.chemicalengineerhelper;

//import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.sql.Types.NULL;







public class CalculateScreen extends AppCompatActivity {

    public class CalcLine{


        private void setDropDowns(String unitType,Spinner dropDown, Context ctx){
            String [] propSet={"-"};

            int unitIndex=-1;

            switch (unitType){
                case "None":
                    //unitIndex=0;
                    break;
                case "Temperature":
                    unitIndex=1;
                    break;

                case "PressureAbsolute":
                    unitIndex=2;
                    break;

                case "Mass":
                    unitIndex=3;
                    break;

                case "Length":
                    unitIndex=4;
                    break;

                case "Time":
                    unitIndex=5;
                    break;

                case "Volume":
                    propSet=GetDerivedUnits("L3");
                    break;

                case "Density":
                    propSet=GetDerivedUnits("M/L3");
                    break;

                case "MassFlowRate":
                    propSet=GetDerivedUnits("M/Z");
                    break;

                case "Viscosity":
                    propSet=GetDerivedUnits("P*Z");
                    break;

                default:
                    //unitIndex=0;
                    break;

            }

            if (unitIndex!=-1)
                propSet= Arrays.copyOf(allUnits[unitIndex],allUnits[unitIndex].length);



            List<String> propNoEmpty=new ArrayList<String>();
            for(int i=0;i<propSet.length;i++)
            {
                if (propSet[i]!=""){

                    propNoEmpty.add(propSet[i]);
                }
            }
            propNoEmpty.toArray();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
                    R.layout.my_spinner_text_view,propNoEmpty);
            dropDown.setAdapter(adapter);
        }



        //public CalcLine(int pos, int ppfieldAmounts, String hint, String valueType, Context ctx, RelativeLayout theLayout){
        public CalcLine(int pos, int ppfieldAmounts, String hint, String valueType, 
                        Context ctx, RelativeLayout theLayout, boolean inPopup){

            hasPopupPage=false;

            if(ppfieldAmounts>0)
                hasPopupPage=true;

            textLine = new EditText(ctx);
            lineParam=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);
            
            if(inPopup)
                dropDown=new Spinner(ctx, Spinner.MODE_DIALOG);
            else
                dropDown=new Spinner(ctx);
            
            dDParam=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);

            position=pos;

            int offset;
            offset = pos*100;


            lineParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lineParam.setMargins(0, offset, 0, 0);
            textLine.setTextSize(12);
            textLine.setLayoutParams(lineParam);
            textLine.setHint(hint);


            dDParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            dDParam.setMargins(0,offset,120,0);
            dropDown.setLayoutParams(dDParam);

            setDropDowns(valueType,dropDown,ctx);
            
            theLayout.addView(textLine);
            theLayout.addView(dropDown);




            if(ppfieldAmounts>0) {

                popupWindow = new PopupWindow();
                inputHelpButton = new Button(ctx);
                iHelpBParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);

                popupLayoutWrap = new LinearLayout(ctx);
                popupLayout = new RelativeLayout(ctx);

                popupLayoutWrap.addView(popupLayout);

                isClicked = true;


                popupLayout.setBackgroundColor(Color.WHITE);


                inputHelpButton = new Button(ctx);
                inputHelpButton.setText("...");


                iHelpBParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                iHelpBParam.setMargins(0, position * 100, 0, 0);

                inputHelpButton.setTextSize(12);
                inputHelpButton.setBackgroundResource(outValue.resourceId);
                inputHelpButton.setLayoutParams(iHelpBParam);

                popupWindow = new PopupWindow();
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

                popupWindow.setOutsideTouchable(true);

                popupWindow.setFocusable(true);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                int thisPosition = position;


                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        isClicked=true;
                        popupWindow.dismiss();


                    }
                });


                inputHelpButton.setOnClickListener(new View.OnClickListener() {
                                                       //boolean isClicked=true;

                                                       public void onClick(View v) {


                       if (isClicked) {
                           isClicked = false;
                           popupWindow.showAtLocation(popupLayoutWrap, Gravity.CENTER, 10, 10);
                           //popupWindow.update(0, 0, popupWindow.getWidth(), popupWindow.getHeight());
                       } else {
                           isClicked = true;
                           popupWindow.dismiss();
                       }
                   }


               }
                );



                popupWindow.setContentView(popupLayoutWrap);
                
                theLayout.addView(inputHelpButton);
            }


        }


        Button ConfirmButton;
        Button ExitButton;

        RelativeLayout.LayoutParams confParams;
        RelativeLayout.LayoutParams exitParams;

        PopupOutput popupOut;

        EditText textLine;
        RelativeLayout.LayoutParams lineParam;

        Spinner dropDown;
        RelativeLayout.LayoutParams dDParam;

        int position;
        PopupWindow popupWindow;

        Button inputHelpButton;
        RelativeLayout.LayoutParams iHelpBParam;

        RelativeLayout popupLayout;
        LinearLayout popupLayoutWrap;
        
        CalcLine [] PopupCalcLines;

        CalcPage PopupPage;

        boolean isClicked;

        boolean hasPopupPage;
        //int ppfieldAmounts;


    }

    public class CalcPage{

        public CalcPage returnThis()
        {
            return this;
        }

        int pageCalcType;
        
        public void SetConfirmExitButtons(Context ctx){

            ParentLine.ConfirmButton = new Button(ctx);
            ParentLine.ConfirmButton.setText("Confirm");
            ParentLine.ConfirmButton.setTextSize(12);


            ParentLine.ConfirmButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        ParentLine.textLine.setText(ParentLine.popupOut.getPopupOutput());
                        ParentLine.popupWindow.dismiss();
                        ParentLine.isClicked = true;
                    }

                }
            );

            ParentLine.ExitButton = new Button(ctx);
            ParentLine.ExitButton.setText("Exit");
            ParentLine.ExitButton.setTextSize(12);


            ParentLine.ExitButton.setOnClickListener(new View.OnClickListener() {

                                                         public void onClick(View v) {
                                                             ParentLine.popupWindow.dismiss();
                                                             ParentLine.isClicked = true;
                                                         }

                                                     }
            );



            ParentLine.popupLayout.
                    measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


            int pUHeight = ParentLine.popupLayout.getMeasuredHeight();

            ParentLine.confParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);

            ParentLine.ConfirmButton.setLayoutParams(ParentLine.confParams);//debugged on SHARP SH90B


            ParentLine.confParams.setMargins(0, pUHeight, 0, 0);





            ParentLine.exitParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);
            ParentLine.exitParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ParentLine.exitParams.setMargins(0, pUHeight, 0, 0);

            ParentLine.ExitButton.setLayoutParams(ParentLine.exitParams);


            ParentLine.popupLayout.addView(ParentLine.ExitButton);
            ParentLine.popupLayout.addView(ParentLine.ConfirmButton);
            
        }

        public void SetResultsGeneral(String property,Context ctx, boolean isPopup, int fieldAmounts){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            //int calcLineAmounts = CalcLines.length;
            int calcLineAmounts=fieldAmounts;
            //int offset=height-300;

            int offset=calcLineAmounts*100+200;

            resultText = new TextView(ctx);
            resultText.setTextSize(12);
            rTParams=
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);
            rTParams.setMargins(0,offset+30,0,0);


            resultText.setLayoutParams(rTParams);

            if(isPopup)
                resultDropDown = new Spinner(ctx,Spinner.MODE_DIALOG);
            else
                resultDropDown=new Spinner(ctx);

            rDDParams=
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);

            rDDParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);





            rDDParams.setMargins(0,offset,120,0);

            resultDropDown.setLayoutParams(rDDParams);

            setDropDowns(property,resultDropDown);
        }

        
        //Selection type pages
        public CalcPage(String property,RelativeLayout theLayout, Context ctx,CalcLine parentLine,
                        int fieldAmounts)
        {
            ParentLine=parentLine;
            BaseLayout=theLayout;
            calcReady=false; //dont allow calculations to proceed until everything is added or there
            //will be crashes

            if(ParentLine!=null)
                isPopup=true;
            else
                isPopup=false;


            SetResultsGeneral(property,ctx,isPopup,fieldAmounts);

            theLayout.addView(resultText);
            theLayout.addView(resultDropDown);

            if (ParentLine != null) {

                SetConfirmExitButtons(ctx);

            }

            resultDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                String previous=resultDropDown.getSelectedItem().toString();
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id){
                    ((TextView)parentView.getChildAt(0)).setTextSize(12);



                        if (previous!=null && !resultText.getText().toString().matches("")) {

                            try {
                                double theValue = Double.parseDouble(resultText.getText().toString());
                                theValue = convertFromSI(convertToSI(theValue, previous)
                                        , resultDropDown.getSelectedItem().toString()
                                );
                                resultText.setText(Double.toString(theValue));

                            } catch(final NumberFormatException e){
                                //no one cares
                            }

                        }

                        previous=resultDropDown.getSelectedItem().toString();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView){

                    }


            });


            


        }
        
        
        //calcline type pages
        public CalcPage(String property, RelativeLayout theLayout, CalcLine[] CLines, Context ctx,
                        int calcType,CalcLine parentLine){

            ParentLine=parentLine;
            CalcLines=CLines;
            BaseLayout=theLayout;
            calcReady=false; //dont allow calculations to proceed until everything is added or there
            //will be crashes

            pageCalcType=calcType;


            if(ParentLine!=null)
                isPopup=true;
            else
                isPopup=false;

            int fieldAmounts = CalcLines.length;

            SetResultsGeneral(property,ctx,isPopup, fieldAmounts);


            resultDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id){
                    ((TextView)parentView.getChildAt(0)).setTextSize(12);
                    calcResult(returnThis(),pageCalcType);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView){

                }

            });


            for(int i = 0; i< CalcLines.length; i++){
                final int x =i;
                if(CalcLines[i].hasPopupPage){



                    CalcLines[i].popupOut=new PopupOutput() {
                        @Override
                        public String getPopupOutput() {


                            String resultString=CalcLines[x].PopupPage.resultText.getText().toString();
                            String resultUnit = CalcLines[x].PopupPage.resultDropDown
                                    .getSelectedItem().toString();
                            double resultDouble;

                            if (!resultString.matches("") && !resultString.matches("No result")) {
                                resultDouble = Double.parseDouble(resultString);
                                resultDouble= convertToSI(resultDouble,resultUnit);
                                resultDouble=convertFromSI(resultDouble,CalcLines[x].dropDown
                                            .getSelectedItem().toString());

                                resultString = String.valueOf(resultDouble);

                            }
                            else
                                resultString="";


                            return resultString;
                        }
                    };
                }

                CalcLines[i].textLine.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        calcResult(returnThis(),pageCalcType);
                    }

                });



                CalcLines[i].dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    String previous=CalcLines[x].dropDown.getSelectedItem().toString();
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                               int position, long id){
                        ((TextView)parentView.getChildAt(0)).setTextSize(12);








                        if (previous!=null && !CalcLines[x].textLine.getText().toString().matches("")) {

                            try {
                                double theValue = eTxtToDbl(CalcLines[x].textLine);
                                theValue = convertFromSI(convertToSI(theValue, previous)
                                        , CalcLines[x].dropDown.getSelectedItem().toString()
                                );
                                CalcLines[x].textLine.setText(Double.toString(theValue));

                            } catch(final NumberFormatException e){
                                //no one cares
                            }

                        }

                        previous=CalcLines[x].dropDown.getSelectedItem().toString();



                        calcResult(returnThis(),pageCalcType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView){

                    }

                });
            }



            theLayout.addView(resultText);
            theLayout.addView(resultDropDown);


            
            if (ParentLine != null) {

               SetConfirmExitButtons(ctx);
            }

            


        } //Calctype constructor end
        
        
        CalcLine ParentLine; //if popup window it will have a parent calc line
        CalcLine [] CalcLines;

        //CalcLine ResultLine;

        TextView resultText;
        RelativeLayout.LayoutParams rTParams;
        Spinner resultDropDown;
        RelativeLayout.LayoutParams rDDParams;

        RelativeLayout BaseLayout;

        boolean isPopup;
        boolean calcReady;
    }


    RelativeLayout calcLayout;
    TypedValue outValue; //For background of buttons



    interface PopupOutput{
        String getPopupOutput();
    }


    CalcPage MainCalcPage;
    CalcLine [] CalcLines;

    TextView resultText;
    RelativeLayout.LayoutParams rTParams;
    Spinner resultDropDown;
    RelativeLayout.LayoutParams rDDParams;

    String whichButton;

    public native double convertToSI(double value, String unit);

    public native double convertFromSI(double value, String unit);

    public native double ReynoldsNumber(double density, double massflow, double pipeId, double viscosity);

    public native double FrictionFactor(double density, double massflow, double pipeId, double viscosity, double roughness);

    public native double PipePressureDropIncompressible(double density, double massflow, double pipeId, double fricFrac, double pipeLen);

    public native double PipePressureDropCompressible(double initPressure, double initTemp, double MW, double Z,
                                                      double mu, double massFlow, double pipeId, double pipeLen,
                                                      double roughness);

    static {
        System.loadLibrary("native-lib");

    }

    public class DerivedUnitHelper{
        String[] unitSet;
        int position;
        double power;
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

    /*
    Use a scheme such as "L/T" to mean length over time and this section
    will add all the possible units to the set
     */
    public String[] GetDerivedUnits(String scheme){
        List<String> returnSet = new ArrayList<String>();
        //Set first value so that there is a lead value
        returnSet.add("");

        int position=0; //0 is num 1 is denom
        String tempUnit="";
        String tempPower="";
        List<DerivedUnitHelper> HelpUnits=new ArrayList<DerivedUnitHelper>();

        for (int i=0; i<scheme.length(); i++){
            char c= scheme.charAt(i);

            if(Character.isLetter(c)){
                tempUnit+=c;
            }
            else if(Character.isDigit(c)){
                tempPower+=c;
            }
            
            else{

                SetLastDerivedUnit(HelpUnits,position,tempUnit,tempPower);
                tempUnit="";
                tempPower="";
            }

            if(c=='/') {
                position = 1;
            }


        }

        SetLastDerivedUnit(HelpUnits,position,tempUnit,tempPower);
        tempUnit="";
        tempPower="";

        //Add all permutations of unit sets
        for (int j = 0; j < HelpUnits.size(); j++) {
            String[] thisSet = HelpUnits.get(j).unitSet;
            String thisPosChar="";

            if (j!=0) {
                if (HelpUnits.get(j).position == 0) {
                    thisPosChar = "*";
                } else
                    thisPosChar = "/";
            }
            int currentSize=returnSet.size();
            //Traverse all units of this set
            for (int k =0; k<thisSet.length; k++){
                if (thisSet[k]!="") {

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
        String []returnArray = new String[returnSet.size()];
        returnArray=returnSet.toArray(returnArray);

        return returnArray;
    }


    public final static String EXTRA_MESSAGE="PocketEngineer.Message";

    public String [][] allUnits={
            {"-","","","",""},
            {"C","K","F","R",""},
            {"Pa","kPa","bar","atm","psia"},
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

    public String [][] GetAllUnits(){
        return allUnits;
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }


    public class PipeHelp {

        Spinner NPSDD;
        Spinner ScheduleDD;
        List<String> NPSNoEmpty;
        List<String> SchedNoEmpty;

        public PipeHelp(CalcLine CL, int index, Context ctx) {
            //PW.setHeight(300);
            //PW.setWidth(500);


            CL.PopupPage = new CalcPage("Length",CL.popupLayout,ctx,CL,2);

            NPSDD = new Spinner(ctx, Spinner.MODE_DIALOG);

            RelativeLayout.LayoutParams NPSDDParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);

            //NPSDDParams.gravity=Gravity.LEFT;

            //NPSDDParams.weight=0;
            NPSDDParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            NPSDDParams.setMargins(0, 0, 0, 0);
            NPSDD.setLayoutParams(NPSDDParams);

            ScheduleDD = new Spinner(ctx, Spinner.MODE_DIALOG);

            RelativeLayout.LayoutParams ScheduleDDParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);

            //ScheduleDDParams.gravity=Gravity.LEFT;
            ScheduleDDParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            //ScheduleDDParams.weight=0;
            ScheduleDDParams.setMargins(0, 200, 0, 0);
            ScheduleDD.setLayoutParams(ScheduleDDParams);

            String[] NPS = Arrays.copyOf(NPSSizes, NPSSizes.length);
            String[] Schedule = Arrays.copyOf(PipeSchedules, PipeSchedules.length);


            NPSNoEmpty = new ArrayList<String>();
            for (int i = 0; i < NPS.length; i++) {
                if (NPS[i] != "") {

                    NPSNoEmpty.add(NPS[i]);
                }
            }
            NPSNoEmpty.toArray();

            ArrayAdapter<String> NPSadapter = new ArrayAdapter<String>(ctx,
                    R.layout.my_spinner_text_view, NPSNoEmpty);
            NPSDD.setAdapter(NPSadapter);

            SchedNoEmpty = new ArrayList<String>();
            for (int i = 0; i < Schedule.length; i++) {
                if (Schedule[i] != "") {

                    SchedNoEmpty.add(Schedule[i]);
                }
            }
            SchedNoEmpty.toArray();

            ArrayAdapter<String> Schedadapter = new ArrayAdapter<String>(ctx,
                    R.layout.my_spinner_text_view, SchedNoEmpty);
            ScheduleDD.setAdapter(Schedadapter);


            TextView NPSLabel = new TextView(ctx);
            NPSLabel.setText("Nominal Size(in)");
            NPSLabel.setTextSize(12);

            RelativeLayout.LayoutParams NPSLParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);
            NPSLParams.setMargins(260, 60, 0, 0);
            NPSLabel.setLayoutParams(NPSLParams);

            TextView SchedLabel = new TextView(ctx);
            SchedLabel.setText("Pipe Schedule");
            SchedLabel.setTextSize(12);

            RelativeLayout.LayoutParams SchedLParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);
            SchedLParams.setMargins(260, 260, 0, 0);
            SchedLabel.setLayoutParams(SchedLParams);

            final int x = index;

//            CL.popupOut = new PopupOutput() {
//                public String getPopupOutput()
//
//                {
//                    int NPSindex = NPSNoEmpty.indexOf(NPSDD.getSelectedItem().toString());
//                    int Schedindex = SchedNoEmpty.indexOf(ScheduleDD.getSelectedItem().toString());
//
//                    Double outerDiameter = NPSOD[NPSindex];
//                    Double wallThickness = NPSWallThickness[NPSindex][Schedindex];
//
//                    double innerDiameter;
//
//                    if (outerDiameter != null && wallThickness != null) {
//                        innerDiameter = outerDiameter - (2 * wallThickness);
//                        innerDiameter = convertToSI(innerDiameter, "mm");
//                        innerDiameter = convertFromSI(innerDiameter, calcLine.dropDown
//                                .getSelectedItem().toString());
//
//
//                        return Double.toString(
//
//                                innerDiameter
//                        );
//                    } else
//                        return "";
//
//                }
//
//                CalcLine calcLine;
//
//                private PopupOutput init(CalcLine var) {
//                    calcLine = var;
//                    return this;
//                }
//
//
//            }.init(CL);

            NPSDD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id){
                    ((TextView)parentView.getChildAt(0)).setTextSize(12);
                    int NPSindex = NPSNoEmpty.indexOf(NPSDD.getSelectedItem().toString());
                    int Schedindex = SchedNoEmpty.indexOf(ScheduleDD.getSelectedItem().toString());

                    Double outerDiameter = NPSOD[NPSindex];
                    Double wallThickness = NPSWallThickness[NPSindex][Schedindex];

                    double innerDiameter;

                    if (outerDiameter != null && wallThickness != null) {
                        innerDiameter = outerDiameter - (2 * wallThickness);
                        innerDiameter = convertToSI(innerDiameter, "mm");
                        innerDiameter = convertFromSI(innerDiameter, calcLine.PopupPage.resultDropDown
                                .getSelectedItem().toString());


                        calcLine.PopupPage.resultText.setText(String.valueOf(innerDiameter));

                    } else
                        calcLine.PopupPage.resultText.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView){

                }

                CalcLine calcLine;

                private AdapterView.OnItemSelectedListener init(CalcLine var) {
                    calcLine = var;
                    return this;
                }


//            }.init(CL);

            }.init(CL));

            ScheduleDD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id){
                    ((TextView)parentView.getChildAt(0)).setTextSize(12);
                    int NPSindex = NPSNoEmpty.indexOf(NPSDD.getSelectedItem().toString());
                    int Schedindex = SchedNoEmpty.indexOf(ScheduleDD.getSelectedItem().toString());

                    Double outerDiameter = NPSOD[NPSindex];
                    Double wallThickness = NPSWallThickness[NPSindex][Schedindex];

                    double innerDiameter;

                    if (outerDiameter != null && wallThickness != null) {
                        innerDiameter = outerDiameter - (2 * wallThickness);
                        innerDiameter = convertToSI(innerDiameter, "mm");
                        innerDiameter = convertFromSI(innerDiameter, calcLine.PopupPage.resultDropDown
                                .getSelectedItem().toString());


                        calcLine.PopupPage.resultText.setText(String.valueOf(innerDiameter));

                    } else
                        calcLine.PopupPage.resultText.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView){

                }

                CalcLine calcLine;

                private AdapterView.OnItemSelectedListener init(CalcLine var) {
                    calcLine = var;
                    return this;
                }


//            }.init(CL);

            }.init(CL));




            CL.popupLayout.addView(NPSLabel);
            CL.popupLayout.addView(SchedLabel);
            CL.popupLayout.addView(NPSDD);
            CL.popupLayout.addView(ScheduleDD);

        }
    }


    //boolean calcReady;

    private void setDropDowns(String unitType,Spinner dropDown){
        String [] propSet={"-"};

        int unitIndex=-1;

        switch (unitType){
            case "None":
                //unitIndex=0;
                break;
            case "Temperature":
                unitIndex=1;
                break;

            case "PressureAbsolute":
                unitIndex=2;
                break;

            case "Mass":
                unitIndex=3;
                break;

            case "Length":
                unitIndex=4;
                break;

            case "Time":
                unitIndex=5;
                break;

            case "Volume":
                propSet=GetDerivedUnits("L3");
                break;

            case "Density":
                propSet=GetDerivedUnits("M/L3");
                break;

            case "MassFlowRate":
                propSet=GetDerivedUnits("M/Z");
                break;

            case "Viscosity":
                propSet=GetDerivedUnits("P*Z");
                break;

            default:
                //unitIndex=0;
                break;

        }

        if (unitIndex!=-1)
            propSet=Arrays.copyOf(allUnits[unitIndex],allUnits[unitIndex].length);



        List<String> propNoEmpty=new ArrayList<String>();
        for(int i=0;i<propSet.length;i++)
        {
            if (propSet[i]!=""){

                propNoEmpty.add(propSet[i]);
            }
        }
        propNoEmpty.toArray();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_spinner_text_view,propNoEmpty);
        dropDown.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_screen);

        //calcReady=false;
        //MainCalcPage.calcReady=false;

        whichButton="BID:-1";
        Intent intent=this.getIntent();
        if(intent !=null)
        {
            whichButton = intent.getExtras().getString("UniqueId");

        }


        calcLayout=(RelativeLayout) findViewById(R.id.calcscreen);


        //setContentView(calcLayout);

        /*//Calculation button
        Button calcButton = new Button(this);
        RelativeLayout.LayoutParams calcButtonParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        calcButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        calcButton.setText("Calculate");
        calcButton.setLayoutParams(calcButtonParams);

        calcButton.setBackgroundResource(outValue.resourceId);
        calcButton.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View v)
                                          {
                                              calcResult();
                                          }

                                      }

        );*/

        outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue,true);




        switch(whichButton.substring(4)){
            case "0":

                    CalcLines = new CalcLine[2];
                    SetCalculationScheme(CalcLines,calcLayout,0,false);

                    MainCalcPage = new CalcPage("Density",calcLayout,CalcLines,this,0,null);

                    break;
            case "1":

                    CalcLines=new CalcLine[4];
                    SetCalculationScheme(CalcLines,calcLayout,1,false);

                    MainCalcPage = new CalcPage("Density",calcLayout,CalcLines,this,1,null);

                    break;

            case "2":

                    CalcLines = new CalcLine[4];

                    SetCalculationScheme(CalcLines,calcLayout,2,false);

                     MainCalcPage = new CalcPage("None",calcLayout,CalcLines,this,2,null);

                    break;

            case "3":
                //generateResultSection("PressureAbsolute");
                CalcLines = new CalcLine[6];

                SetCalculationScheme(CalcLines,calcLayout,3,false);

                MainCalcPage = new CalcPage("PressureAbsolute",calcLayout,CalcLines,this,3,null);

                //friction factor aid

                //CalcLines[4].PopupCalcLines = new CalcLine[4];

                //SetCalculationScheme(CalcLines[4].PopupCalcLines,CalcLines[4].popupLayout,4,true);

                //CalcLines[4].PopupPage = new CalcPage(
                 //       "None",CalcLines[4].popupLayout,CalcLines[4].PopupCalcLines,this,4,CalcLines[4]);

               // CalcLines[4].PopupPage.calcReady=true;

                    break;

            case "4":
                CalcLines=new CalcLine[9];
                SetCalculationScheme(CalcLines,calcLayout,5,false);
                MainCalcPage=new CalcPage("PressureAbsolute",calcLayout,CalcLines,this,5,null);

        }

        MainCalcPage.calcReady=true;

        /*Button backButton = new Button(this);
        RelativeLayout.LayoutParams backButtonParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        backButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        backButton.setBackgroundResource(outValue.resourceId);
        backButtonParams.setMargins(0,0,0,100);
        backButton.setLayoutParams(backButtonParams);

        backButton.setText("Back");
        backButton.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View v){
                                              finish();
                                          }
                                      }
        );

        //Add buttons to layout
        calcLayout.addView(calcButton);
        calcLayout.addView(backButton);*/



    }

    
    public void SetCalculationScheme(CalcLine[] CLines,RelativeLayout theLayout, int schemeIndex, 
                                     boolean inPopup){

        PipeHelp pipeHelp;

        switch(schemeIndex){
            //density general
            case 0:

                //generateResultSection("Density",theLayout);

                //CLines= new CalcLine[2];
                CLines[0]=new CalcLine(0,0,"Enter Mass","Mass", this,theLayout,inPopup);
                CLines[1]=new CalcLine(1,0, "Enter Volume", "Volume", this,theLayout,inPopup);

                break;
            
            //density vapor
            case 1:

                //generateResultSection("Density",theLayout,inPopup);
                //CLines = new CalcLine[4];
                CLines[0]=new CalcLine(0,0,"Enter Pressure(Absolute)","PressureAbsolute",this,theLayout,inPopup);
                CLines[1]=new CalcLine(1,0,"Enter Temperature","Temperature",this,theLayout,inPopup);
                CLines[2]=new CalcLine(2,0, "Enter Molecular Weight", "None",this,theLayout,inPopup);
                CLines[3]=new CalcLine(3,0, "Enter Compressibility (Z)", "None",this,theLayout,inPopup);


                break;

            //Reynolds number (diameter)
            case 2:

                //generateResultSection("None",theLayout,inPopup);
                //CLines = new CalcLine[4];

                CLines[0]=new CalcLine(0,0,"Enter Density","Density",this,theLayout,inPopup);
                CLines[1]=new CalcLine(1,0,"Enter Mass Flow Rate","MassFlowRate",this,theLayout,inPopup);
                CLines[2]=new CalcLine(2,2, "Enter Pipe Internal Diameter", "Length",this,theLayout,inPopup);
                CLines[3]=new CalcLine(3,0, "Enter Viscosity (Dynamic)", "Viscosity",this,theLayout,inPopup);

                pipeHelp=new PipeHelp(CLines[2],0,this);
                //initPipeHelp(CLines[2],0);



                break;
            
            //Pressure Drop
            case 3:
                //generateResultSection("PressureAbsolute",theLayout,inPopup);
                //CLines = new CalcLine[5];

                CLines[0]=new CalcLine(0,0,"Enter Fluid Density","Density",this,theLayout,inPopup);
                CLines[1]=new CalcLine(1,0,"Enter Mass Flow Rate","MassFlowRate",this,theLayout,inPopup);
                CLines[2]=new CalcLine(2,2, "Enter Pipe Internal Diameter", "Length",this,theLayout,inPopup);
                CLines[3]=new CalcLine(3,0, "Enter Pipe Length", "Length",this,theLayout,inPopup);
                //CLines[4]=new CalcLine(4,4, "Enter Friction Factor", "None",this,theLayout,inPopup);
                CLines[4]=new CalcLine(4,0, "Enter Viscosity", "Viscosity",this,theLayout,inPopup);
                CLines[5]=new CalcLine(5,0, "Enter Roughness", "Length",this,theLayout,inPopup);
                CLines[5].textLine.setText("4.57e-5");
                //initPipeHelp(CLines[2],0);
                pipeHelp=new PipeHelp(CLines[2],0,this);
                break;
            
            //friction factor
            case 4:
                //friction factor aid

                //generateResultSection("None",theLayout,inPopup);

                //CLines = new CalcLine[4];

                CLines[0]=
                        new CalcLine(0,0,"Enter Density","Density",this,theLayout,inPopup);
                CLines[1]=
                        new CalcLine(1,0,"Enter Mass Flow Rate","MassFlowRate",this,theLayout,inPopup);
                CLines[2]=
                        new CalcLine(2,2, "Enter Pipe Internal Diameter", "Length",this,theLayout,inPopup);
                CLines[3]=
                        new CalcLine(3,0, "Enter Viscosity (Dynamic)", "Viscosity",this,theLayout,inPopup);
                CLines[4]=
                        new CalcLine(4,0, "Enter Roughness", "Length",this, theLayout, inPopup);
                CLines[4].textLine.setText("4.57e-5");

                pipeHelp=new PipeHelp(CLines[2],0,this);
                //initPipeHelp(CLines[2],0);

                break;

            //compressible pressure drop
            case 5:
                CLines[0]=new CalcLine(0,0,"Enter Initial Pressure(Absolute)","PressureAbsolute",this,theLayout,inPopup);
                CLines[1]=new CalcLine(1,0,"Enter Temperature","Temperature",this,theLayout,inPopup);
                CLines[2]=new CalcLine(2,0,"Enter Molecular Weight","None",this,theLayout,inPopup);
                CLines[3]=new CalcLine(3,0,"Enter Compressibility Factor","None",this,theLayout,inPopup);

                CLines[4]=new CalcLine(4,0, "Enter Viscosity", "Viscosity",this,theLayout,inPopup);
                CLines[5]=new CalcLine(5,0,"Enter Mass Flow Rate","MassFlowRate",this,theLayout,inPopup);
                CLines[6]=new CalcLine(6,2, "Enter Pipe Internal Diameter", "Length",this,theLayout,inPopup);
                CLines[7]=new CalcLine(7,0, "Enter Pipe Length", "Length",this,theLayout,inPopup);
                CLines[8]=
                        new CalcLine(8,0, "Enter Roughness", "Length",this, theLayout, inPopup);
                CLines[8].textLine.setText("4.57e-5");

                pipeHelp=new PipeHelp(CLines[6],0,this);


                break;

        }


    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_page,menu);
        menu.add(Menu.NONE,1,Menu.NONE,"Back");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //Back button
            case 1:
                finish();
                return true;

            default:
                return false;

        }

    }

//Calculation result page
    public void calcResult(CalcPage CPage, int calcType) {

        //Intent intent=new Intent(this,DisplayMessageActivity.class);
    //if (resultDropDown!=null)
    //    calcReady=true;


        if (CPage.calcReady) {

            String result = "No result";
            double resultDbl = NULL;

            //double[] lineVals = new double[lines.length];
            //String[] lineUnits = new String[lines.length];

            double[] lineVals = new double[CPage.CalcLines.length];
            String[] lineUnits = new String[CPage.CalcLines.length];

            boolean emptyVals = false;
            for (int i = 0; i < CPage.CalcLines.length; i++) {


                if (!CPage.CalcLines[i].textLine.getText().toString().matches("")) {

                    try {
                        lineVals[i] = eTxtToDbl(CPage.CalcLines[i].textLine);
                    } catch (final NumberFormatException e) {
                        emptyVals = true;
                    }

                    lineUnits[i] = CPage.CalcLines[i].dropDown.getSelectedItem().toString();
                    //Convert to SI
                    lineVals[i] = convertToSI(lineVals[i], lineUnits[i]);
                } else
                    emptyVals = true;

            }

            if (!emptyVals) {
                switch (calcType) {
                case 0:

                    resultDbl = (lineVals[0] / lineVals[1]);
                    break;

                case 1:


                    resultDbl = (lineVals[0] * lineVals[2] /
                            lineVals[1] / lineVals[3] / 8.314 / 1000
                    );
                    break;
                case 2:

                    resultDbl=ReynoldsNumber(lineVals[0],lineVals[1],lineVals[2],lineVals[3]);
                    break;

                case 3:
                    double fricFrac=FrictionFactor(lineVals[0],lineVals[1],lineVals[2],lineVals[4],lineVals[5]);
                    resultDbl=PipePressureDropIncompressible(lineVals[0],lineVals[1],lineVals[2],lineVals[3],fricFrac);
                    break;

                case 4:
                    resultDbl=FrictionFactor(lineVals[0],lineVals[1],lineVals[2],lineVals[3],lineVals[4]);
                    break;

                case 5:
                    resultDbl=PipePressureDropCompressible(lineVals[0],lineVals[1],lineVals[2],lineVals[3],
                            lineVals[4],lineVals[5],lineVals[6],lineVals[7],lineVals[8]);
                    break;
                }
            }
            if (resultDbl != NULL) {
                resultDbl = convertFromSI(resultDbl, CPage.resultDropDown.getSelectedItem().toString());
                result = Double.toString(resultDbl);
            }

            CPage.resultText.setText(result);

            //intent.putExtra(EXTRA_MESSAGE,result);
            //startActivity(intent);

        }
    }

    public double eTxtToDbl(EditText eText){
        return Double.parseDouble(eText.getText().toString());

    }
}
