package localhost.steven.pocketengineer;

import android.content.res.TypedArray;
import android.util.TypedValue;
import android.os.Bundle;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.provider.Settings;
import android.view.View.OnClickListener;

public class MainPage extends AppCompatActivity {

    TypedValue outValue;
    OnClickListener bOpen;

    public native String stringFromJNI();

    //public native double convertToSI();

    static {
        System.loadLibrary("native-lib");

    }
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

        //density button
        Button densityButton = new Button(this);
        densityButton.setText("Density");
        setupButton(densityButton,0); //position so margins can be set
        densityButton.setId(0);


        //vapor density button
        Button vDensityButton = new Button(this);
        vDensityButton.setText("Vapor Density");
        setupButton(vDensityButton,1);
        vDensityButton.setId(1);

        //reynolds number button
        Button ReDButton = new Button(this);
        ReDButton.setText("Reynolds Number (Diameter)");
        setupButton(ReDButton,2);
        ReDButton.setId(2);

        //friction factor button
        Button PipePDropButton = new Button(this);
        PipePDropButton.setText("Pipe Pressure Drop (Darcy-Weisbach)");
        setupButton(PipePDropButton,3);
        PipePDropButton.setId(3);

       
        relativeLayout.addView(densityButton);
        relativeLayout.addView(vDensityButton);
        relativeLayout.addView(ReDButton);
        relativeLayout.addView(PipePDropButton);


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
