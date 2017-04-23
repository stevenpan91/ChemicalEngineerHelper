package localhost.steven.chemicalengineerhelper;

import android.app.Activity;
import android.app.Application;
import android.net.Credentials;
import android.util.Log;
import android.util.TypedValue;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
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

        //pipe liquid pressure drop button
        Button PipePDropInCompButton = new Button(this);
        PipePDropInCompButton.setText("Pipe Pressure Drop Incompressible (Darcy-Weisbach)");
        setupButton(PipePDropInCompButton,3);
        PipePDropInCompButton.setId(3);


        //pipe vapor pressure drop button
        Button PipePDropCompButton = new Button(this);
        PipePDropCompButton.setText("Pipe Pressure Drop Compressible (Darcy-Weisbach)");
        setupButton(PipePDropCompButton,4);
        PipePDropCompButton.setId(4);


        relativeLayout.addView(densityButton);
        relativeLayout.addView(vDensityButton);
        relativeLayout.addView(ReDButton);
        relativeLayout.addView(PipePDropInCompButton);
        relativeLayout.addView(PipePDropCompButton);


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
//import com.facebook.react.ReactActivity;
//import com.facebook.react.ReactApplication;
//import com.facebook.react.ReactInstanceManager;
//import com.facebook.react.ReactNativeHost;
//import com.facebook.react.ReactPackage;
//import com.facebook.react.ReactRootView;
//import com.facebook.react.common.LifecycleState;
//import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
//import com.facebook.react.shell.MainReactPackage;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//public class MainPage extends Activity implements DefaultHardwareBackBtnHandler {
//
//    private ReactRootView mReactRootView;
//    private ReactInstanceManager mReactInstanceManager;
//
//    private final String TAG = "TodoLite";
//    private static final int DEFAULT_LISTEN_PORT = 5984;
//    private int listenPort;
//    private Credentials allowedCredentials;
//
//
//    private WebView theWebView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Log.d(TAG, "onCreate method called");
//
//        // 1
//        mReactRootView = new ReactRootView(this);
//        mReactInstanceManager = ReactInstanceManager.builder()
//                .setApplication(getApplication())
//                .setBundleAssetName("index.android.bundle")
//                .setJSMainModuleName("index.android")
//                .addPackage(new MainReactPackage())
//                .setUseDeveloperSupport(BuildConfig.DEBUG)
//                .setInitialLifecycleState(LifecycleState.RESUMED)
//                .build();
//        mReactRootView.startReactApplication(mReactInstanceManager, "ChemEngHelper", null);
//
//        setContentView(mReactRootView);
//        //initCBLite();
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onHostPause(this);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onHostResume(this,this);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onBackPressed();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//        @Override
//        public void invokeDefaultOnBackPressed() {
//            super.onBackPressed();
//        }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
//            mReactInstanceManager.showDevOptionsDialog();
//            return true;
//        }
//        return super.onKeyUp(keyCode, event);
//    }
//
//
//
//}