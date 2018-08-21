package localhost.steven.chemicalengineerhelper;

import android.util.TypedValue;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainPage extends AppCompatActivity {

    TypedValue outValue;
    OnClickListener bOpen;

    public native String stringFromJNI();

    //public native double convertToSI();

    static {
        System.loadLibrary("native-lib");

    }
    public String[] buttonText={"Unit Converter", "Internal Diameter Calculator (NPS)",
            "Density","Vapor Density","Reynolds Number (Diameter)",//0 to 2
            "Pipe Pressure Drop Incompressible (Darcy-Weisbach)","Pipe Pressure Drop Compressible (Darcy-Weisbach)",
            };
    //public final static String EXTRA_MESSAGE="PocketEngineer.Message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.mainscreen);

        //global button settings
        TextView textView = new TextView(this);
        //textView.setText(Double.toString(convertToSI()));
        relativeLayout.addView(textView);

        //background attr
            outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground,outValue,true);
        //Open calc side window
            bOpen = new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                openCalc(v);
                            }

                        };


        for(int buttonInit=0;buttonInit<buttonText.length;buttonInit++){
            Button thisButton=new Button(this);
            thisButton.setText(buttonText[buttonInit]);
            setupButton(thisButton,buttonInit);
            thisButton.setId(buttonInit);

            relativeLayout.addView(thisButton);
        }



    }

    //Calculation result page
    public void openCalc(View view){
        Intent intent=new Intent(MainPage.this,CalculateScreen.class);
        intent.putExtra("UniqueId","BID:"+Integer.toString(view.getId()));
        startActivity(intent);

    }

    public double eTxtToDbl(EditText eText){
        return Double.parseDouble(eText.getText().toString());

    }

    //Initiate button parameters
    public void setupButton(Button button, int position){

        //layout
        RelativeLayout.LayoutParams mpButtonParams;
        mpButtonParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mpButtonParams.setMargins(0,position*150,0,0);

        button.setLayoutParams(mpButtonParams);
        button.setBackgroundResource(outValue.resourceId);
        button.setOnClickListener(bOpen);
    }



}
