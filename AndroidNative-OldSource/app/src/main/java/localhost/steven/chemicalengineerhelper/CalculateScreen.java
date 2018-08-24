package localhost.steven.chemicalengineerhelper;

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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CalculateScreen extends AppCompatActivity {

    public String[] RemoveEmpty(String[] sourceArray){
        String[] sourceCopy = Arrays.copyOf(sourceArray, sourceArray.length);

        ArrayList<String> sourceCopyNoEmpty = new ArrayList<String>();
        for (int i = 0; i < sourceCopy.length; i++) {
            if (sourceCopy[i] != "") {

                sourceCopyNoEmpty.add(sourceCopy[i]);
            }
        }
        return sourceCopyNoEmpty.toArray(new String[0]);
    }

    public Double GetNPSInnerDiameter(DropDownLine NPSDD,DropDownLine ScheduleDD){
        int NPSindex =
                Arrays.asList(RemoveEmpty(NPSSizes)).indexOf(((Spinner)NPSDD.textLine).getSelectedItem().toString());
        int Schedindex =
                Arrays.asList(RemoveEmpty(PipeSchedules)).indexOf(((Spinner)ScheduleDD.textLine).getSelectedItem().toString());

        Double outerDiameter = NPSOD[NPSindex];
        Double wallThickness = NPSWallThickness[NPSindex][Schedindex];

        double innerDiameter;

        if (outerDiameter != null && wallThickness != null) {
            innerDiameter = outerDiameter - (2 * wallThickness);
            innerDiameter = convertToSI(innerDiameter, "mm");
            return innerDiameter;//in SI units
        } else
            return null;
    }

    public AdapterView.OnItemSelectedListener
        ConvertThisLine(CalcPage theCalcPage,CalcLine theCalcLine,int pageCalcType,String resultMessage,boolean calculateYN){
        return new AdapterView.OnItemSelectedListener(){

            CalcPage theCalcPage;
            CalcLine theCalcLine;
            int pageCalcType;
            String resultMessage;
            boolean calculateYN;

            String previous;
            private AdapterView.OnItemSelectedListener
            init(CalcPage theCP,CalcLine theCL, int pageCT, String resMsg, boolean calcYN) {

                theCalcPage = theCP;
                theCalcLine= theCL;
                pageCalcType=pageCT;
                resultMessage=resMsg;
                calculateYN=calcYN;

                previous= theCalcLine.dropDown.getSelectedItem().toString();
                return this;
            }


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id){
                if(parentView.getChildAt(0)!=null)
                    ((TextView) parentView.getChildAt(0)).setTextSize(12);

                String lineText="";
                if (theCalcLine.textLine instanceof TextView)
                    lineText = ((TextView)theCalcLine.textLine).getText().toString();

                if (theCalcLine.textLine instanceof EditText)
                    lineText = ((EditText) theCalcLine.textLine).getText().toString();


                if (previous!=null && !lineText.matches("")) {

                    try {
                        double theValue = Double.parseDouble(lineText);
                        theValue = convertFromSI(convertToSI(theValue, previous)
                                , theCalcLine.dropDown.getSelectedItem().toString()
                        );

                        if (theCalcLine.textLine instanceof TextView)
                            ((TextView)theCalcLine.textLine).setText(Double.toString(theValue));

                        if (theCalcLine.textLine instanceof EditText)
                            ((EditText) theCalcLine.textLine).setText(Double.toString(theValue));

                    } catch(final NumberFormatException e){
                        //no one cares
                    }

                }

                previous = theCalcLine.dropDown.getSelectedItem().toString();
                if(calculateYN)
                    calcResult(theCalcPage, pageCalcType, resultMessage);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){
            }
        }.init(theCalcPage,theCalcLine,pageCalcType,resultMessage,calculateYN);
    }
    
    public String[]UnitTypes={"None","Temperature","PressureAbsolute","Mass","Length","Time","Volume","Density",
            "MassFlowRate","Viscosity"};

    //pos is the vertical position of the line
    //sourceArray fills the dropDown
    //dropDownLabel explains what the dropDown is
    public class DropDownLine extends CalcLine{
        public DropDownLine(int pos, String[] sourceArray, Context ctx, RelativeLayout theLayout, boolean inPopup,String dropDownLabel){

            hasPopupPage=false;

            if(inPopup)
                textLine=new Spinner(ctx, Spinner.MODE_DIALOG);
            else
                textLine = new Spinner(ctx);

            Spinner spinnerTextLine=(Spinner)textLine;

            if(dropDownLabel.length()>0)
                lineParam=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);
            else
                lineParam=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 150);

            position=pos;

            int offset;
            offset = pos*100;

            lineParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lineParam.setMargins(0, offset, 0, 0);
            textLine.setLayoutParams(lineParam);

            ArrayAdapter<String> DDadapter = new ArrayAdapter<String>(ctx,
                    R.layout.my_spinner_text_view, RemoveEmpty(sourceArray)){
                public View getView(int position, View convertView,ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextSize(12);
                    return v;
                }

                public View getDropDownView(int position, View convertView,ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView,parent);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    return v;

                }
            };

            spinnerTextLine.setAdapter(DDadapter);


            theLayout.addView(textLine);

            if(dropDownLabel.length()>0){
                DropDownLabel = new TextView(ctx);
                DropDownLabel.setText(dropDownLabel);
                DropDownLabel.setTextSize(12);

                DropDownLabelParams =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);
                DropDownLabelParams.setMargins(260, 60+(position*100), 0, 0);
                DropDownLabel.setLayoutParams(DropDownLabelParams);
                theLayout.addView(DropDownLabel);
            }
        }
        public TextView DropDownLabel;
        private RelativeLayout.LayoutParams DropDownLabelParams;

    }

    public class TextOnlyLine extends CalcLine{
        public TextOnlyLine(String property,Context ctx,RelativeLayout theLayout, boolean isPopup, int fieldAmounts){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            //int calcLineAmounts = CalcLines.length;
            int calcLineAmounts=fieldAmounts;
            //int offset=height-300;

            int offset=calcLineAmounts*100+200;

            textLine = new TextView(ctx);
            TextView textOnlyLine=(TextView)textLine;
            textOnlyLine.setTextSize(12);
            lineParam=
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);
            lineParam.setMargins(0,offset+30,0,0);


            textLine.setLayoutParams(lineParam);

            if(isPopup)
                dropDown = new Spinner(ctx,Spinner.MODE_DIALOG);
            else
                dropDown=new Spinner(ctx);

            dDParam=
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 70);

            dDParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);





            dDParam.setMargins(0,offset,120,0);

            dropDown.setLayoutParams(dDParam);

            setDropDowns(property,ctx);

            theLayout.addView(textLine);
            theLayout.addView(dropDown);
        }
    }

    public class CalcLine{
        public CalcLine(){

        }
        protected void setDropDowns(String unitType,Context ctx){
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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
                    R.layout.my_spinner_text_view,RemoveEmpty(propSet));
            dropDown.setAdapter(adapter);
        }

        public CalcLine(int pos, int ppfieldAmounts, String hint, String valueType, 
                        Context ctx, RelativeLayout theLayout, boolean inPopup){



            hasPopupPage=false;

            if(ppfieldAmounts>0)
                hasPopupPage=true;

            textLine = new EditText(ctx);
            EditText editTextLine = (EditText)textLine;

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
            editTextLine.setTextSize(12);
            textLine.setLayoutParams(lineParam);
            editTextLine.setHint(hint);


            dDParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            dDParam.setMargins(0,offset,120,0);
            dropDown.setLayoutParams(dDParam);

            setDropDowns(valueType,ctx);
            
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

        //EditText textLine;
        View textLine; //doesn't have to be EditText
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
                        if(ParentLine.textLine instanceof EditText)
                            ((EditText)ParentLine.textLine).setText(ParentLine.popupOut.getPopupOutput());

                        //no popups for spinners for now

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

            ResultLine = new TextOnlyLine(property,ctx,theLayout,isPopup,fieldAmounts);

            if (ParentLine != null) {
                SetConfirmExitButtons(ctx);
            }
            ResultLine.dropDown.setOnItemSelectedListener
                    (ConvertThisLine(returnThis(),ResultLine,-1,"",false));

        }
        
        
        //calcline type pages
        public CalcPage(String property, RelativeLayout theLayout, CalcLine[] CLines, Context ctx,
                        int calcType,CalcLine parentLine,String resultMsg){

            resultMessage=resultMsg;
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

            ResultLine=new TextOnlyLine(property,ctx,theLayout,isPopup,fieldAmounts);
            ResultLine.dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id){
                    if(parentView.getChildAt(0)!=null)
                        ((TextView)parentView.getChildAt(0)).setTextSize(12);
                        calcResult(returnThis(),pageCalcType,resultMessage);
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


                            String resultString=((TextView)CalcLines[x].PopupPage.ResultLine.textLine).getText().toString();
                            String resultUnit = CalcLines[x].PopupPage.ResultLine.dropDown
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

                if(CalcLines[x].textLine instanceof EditText) {
                    ((EditText)CalcLines[i].textLine).addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            calcResult(returnThis(), pageCalcType, resultMessage);
                        }

                    });
                }


                if(CalcLines[i].dropDown!=null) {
                    CalcLines[i].dropDown.setOnItemSelectedListener
                            (ConvertThisLine(returnThis(),CalcLines[i],pageCalcType,resultMessage,true));
                }
            }
            if (ParentLine != null) {

               SetConfirmExitButtons(ctx);
            }
        } //Calctype constructor end
        
        
        CalcLine ParentLine; //if popup window it will have a parent calc line
        CalcLine [] CalcLines;
        TextOnlyLine ResultLine;
        String resultMessage;
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
            {"-","","","","",""},
            {"C","K","F","R","",""},
            {"Pa","kPa","bar","atm","psi","mPa"},
            {"kg","lb","","","",""},
            {"m","ft","in","mm","",""},
            {"s","hr","day","yr","",""}

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

        DropDownLine NPSDD;
        DropDownLine ScheduleDD;
        //List<String> NPSNoEmpty;
        //List<String> SchedNoEmpty;

        public PipeHelp(CalcLine CL, int index, Context ctx) {
            //PW.setHeight(300);
            //PW.setWidth(500);

            CL.PopupPage = new CalcPage("Length",CL.popupLayout,ctx,CL,2);
            //line of nominal pipe sizes
            NPSDD = new DropDownLine(0,NPSSizes,ctx,CL.popupLayout,true,"Nominal Size(in)");
            //line of schedules
            //original offset was "200"
            ScheduleDD = new DropDownLine(2,PipeSchedules,ctx,CL.popupLayout,true,"Pipe Schedule");

            final int x = index;

            AdapterView.OnItemSelectedListener getSizeListener=new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id){
                    ((TextView)parentView.getChildAt(0)).setTextSize(12);
                    Double innerDiameter=GetNPSInnerDiameter(NPSDD,ScheduleDD);
                    if(innerDiameter!=null){
                        innerDiameter = convertFromSI(innerDiameter,
                                calcLine.PopupPage.ResultLine.dropDown
                                .getSelectedItem().toString());
                        ((TextView)calcLine.PopupPage.ResultLine.textLine).setText(String.valueOf(innerDiameter));
                    } else
                        ((TextView)calcLine.PopupPage.ResultLine.textLine).setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView){

                }

                CalcLine calcLine;

                private AdapterView.OnItemSelectedListener init(CalcLine var) {
                    calcLine = var;
                    return this;
                }

            }.init(CL);


            ((Spinner)NPSDD.textLine).setOnItemSelectedListener(getSizeListener);
            ((Spinner)ScheduleDD.textLine).setOnItemSelectedListener(getSizeListener);
        }
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


        outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless,outValue,true);


        switch(whichButton.substring(4)){
            //Unit Conversion
            case "0":
                CalcLines = new CalcLine[2];
                SetCalculationScheme(CalcLines,calcLayout,6,false,this);
                MainCalcPage = new CalcPage("None",calcLayout,CalcLines,this,0,null,"To Value");

                ((Spinner)CalcLines[0].textLine).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    Context thisCtx;

                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                               int position, long id){
                        ((TextView)parentView.getChildAt(0)).setTextSize(12);
                        ((TextView)parentView.getChildAt(0)).setGravity(Gravity.CENTER);

                        CalcLines[1].setDropDowns(((Spinner)CalcLines[0].textLine).getSelectedItem().toString(),thisCtx);
                        if(MainCalcPage.ResultLine.dropDown!=null)
                            MainCalcPage.ResultLine.setDropDowns(((Spinner)CalcLines[0].textLine).getSelectedItem().toString(),thisCtx);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView){

                    }

                    private AdapterView.OnItemSelectedListener init(Context var){
                        thisCtx = var;
                        return this;
                    }

                }.init(this));

                break;

            //NPS Sizes
            case "1":
                CalcLines = new CalcLine[2];
                SetCalculationScheme(CalcLines,calcLayout,7,false,this);
                MainCalcPage = new CalcPage("Length",calcLayout,CalcLines,this,1,null,"Inner Diameter");
                AdapterView.OnItemSelectedListener calcSizeListener=new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                               int position, long id){
                        calcResult(MainCalcPage,1,MainCalcPage.resultMessage);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView){

                    }
                };
                ((Spinner)CalcLines[0].textLine).setOnItemSelectedListener(calcSizeListener);
                ((Spinner)CalcLines[1].textLine).setOnItemSelectedListener(calcSizeListener);
                break;
            //Density
            case "2":
                CalcLines = new CalcLine[2];
                SetCalculationScheme(CalcLines,calcLayout,0,false,this);
                MainCalcPage = new CalcPage("Density",calcLayout,CalcLines,this,2,null,"Density");
                break;

            case "3":
                CalcLines=new CalcLine[4];
                SetCalculationScheme(CalcLines,calcLayout,1,false,this);
                MainCalcPage = new CalcPage("Density",calcLayout,CalcLines,this,3,null,"Vapor Density");
                break;

            case "4":
                CalcLines = new CalcLine[4];
                SetCalculationScheme(CalcLines,calcLayout,2,false,this);
                MainCalcPage = new CalcPage("None",calcLayout,CalcLines,this,4,null, "Reynolds Number");
                break;

            case "5":
                CalcLines = new CalcLine[6];
                SetCalculationScheme(CalcLines,calcLayout,3,false,this);
                MainCalcPage = new CalcPage("PressureAbsolute",calcLayout,CalcLines,this,5,null, "Differential Pressure");
                break;

            case "6":
                CalcLines=new CalcLine[9];
                SetCalculationScheme(CalcLines,calcLayout,5,false,this);
                MainCalcPage=new CalcPage("PressureAbsolute",calcLayout,CalcLines,this,7,null, "Final Pressure");
                break;

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
        calcLayout.addView(backButton);*/

    }

    
    public void SetCalculationScheme(CalcLine[] CLines,RelativeLayout theLayout, int schemeIndex, 
                                     boolean inPopup, Context ctx){
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

                break;
            
            //Pressure Drop
            case 3:
                CLines[0]=new CalcLine(0,0,"Enter Fluid Density","Density",this,theLayout,inPopup);
                CLines[1]=new CalcLine(1,0,"Enter Mass Flow Rate","MassFlowRate",this,theLayout,inPopup);
                CLines[2]=new CalcLine(2,2, "Enter Pipe Internal Diameter", "Length",this,theLayout,inPopup);
                CLines[3]=new CalcLine(3,0, "Enter Pipe Length", "Length",this,theLayout,inPopup);
                //CLines[4]=new CalcLine(4,4, "Enter Friction Factor", "None",this,theLayout,inPopup);
                CLines[4]=new CalcLine(4,0, "Enter Viscosity", "Viscosity",this,theLayout,inPopup);
                CLines[5]=new CalcLine(5,0, "Enter Roughness", "Length",this,theLayout,inPopup);
                ((EditText)CLines[5].textLine).setText("4.57e-5");
                pipeHelp=new PipeHelp(CLines[2],0,this);
                break;
            //friction factor
            case 4:
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
                ((EditText)CLines[4].textLine).setText("4.57e-5");
                pipeHelp=new PipeHelp(CLines[2],0,this);
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
                ((EditText)CLines[8].textLine).setText("4.57e-5");
                pipeHelp=new PipeHelp(CLines[6],0,this);
                break;
            //unit conversion
            case 6:
                CLines[0]=new DropDownLine(0,UnitTypes,this,theLayout,inPopup,"");
                CLines[1]=new CalcLine(2,0,"From Value","None",this,theLayout,inPopup);
                break;
            //NPS pipes
            case 7:
                CLines[0]=new DropDownLine(0,NPSSizes,this,theLayout,inPopup,"Nominal Size(in)");
                CLines[1]=new DropDownLine(2,PipeSchedules,this,theLayout,inPopup,"Pipe Schedule");
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
    public void calcResult(CalcPage CPage, int calcType, String resultMessage) {


        if (CPage.calcReady) {

            String result = resultMessage;
            Double resultDbl = null;


            double[] lineVals = new double[CPage.CalcLines.length];
            String[] lineUnits = new String[CPage.CalcLines.length];

            boolean emptyVals = false;
            //get all line values
            //note that this converts all line values to SI units
            for (int i = 0; i < CPage.CalcLines.length; i++) {


                if(CPage.CalcLines[i].textLine instanceof EditText) {
                    EditText editTextLine=(EditText)CPage.CalcLines[i].textLine;
                    if (!editTextLine.getText().toString().matches("")) {

                        try {
                            lineVals[i] = eTxtToDbl(editTextLine);
                        } catch (final NumberFormatException e) {
                            emptyVals = true;
                        }

                        lineUnits[i] = CPage.CalcLines[i].dropDown.getSelectedItem().toString();
                        //Convert to SI
                        lineVals[i] = convertToSI(lineVals[i], lineUnits[i]);
                    } else
                        emptyVals = true;
                }


            }

            if (!emptyVals) {
                switch (calcType) {
                case 0:
                    resultDbl = lineVals[1]; //will be converted from SI below
                    break;
                case 1:
                    resultDbl= GetNPSInnerDiameter((DropDownLine)CPage.CalcLines[0],
                                                (DropDownLine)CPage.CalcLines[1]);
                    break;
                case 2:
                    resultDbl = (lineVals[0] / lineVals[1]);
                    break;
                case 3:
                    resultDbl = (lineVals[0] * lineVals[2] /
                            lineVals[1] / lineVals[3] / 8.314 / 1000
                    );
                    break;
                case 4:
                    resultDbl=ReynoldsNumber(lineVals[0],lineVals[1],lineVals[2],lineVals[3]);
                    break;
                case 5:
                    double fricFrac=FrictionFactor(lineVals[0],lineVals[1],lineVals[2],lineVals[4],lineVals[5]);
                    resultDbl=PipePressureDropIncompressible(lineVals[0],lineVals[1],lineVals[2],lineVals[3],fricFrac);
                    break;
                case 6:
                    resultDbl=FrictionFactor(lineVals[0],lineVals[1],lineVals[2],lineVals[3],lineVals[4]);
                    break;
                case 7:
                    resultDbl=PipePressureDropCompressible(lineVals[0],lineVals[1],lineVals[2],lineVals[3],
                            lineVals[4],lineVals[5],lineVals[6],lineVals[7],lineVals[8]);
                    break;
                }
            }
            if (resultDbl != null) {
                resultDbl = convertFromSI(resultDbl, CPage.ResultLine.dropDown.getSelectedItem().toString());
                result = Double.toString(resultDbl);

            }

            ((TextView)CPage.ResultLine.textLine).setText(result);
            //intent.putExtra(EXTRA_MESSAGE,result);
            //startActivity(intent);
        }
    }

    public double eTxtToDbl(EditText eText){
        return Double.parseDouble(eText.getText().toString());
    }
}
