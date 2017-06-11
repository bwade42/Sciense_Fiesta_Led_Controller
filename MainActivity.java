package com.smwg.bwade.colorfiesta;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    BlueToothFragment bluetooth;

    private float mLastX, mLastY, mLastZ;
    static float deltaX;
    static float deltaY;
    static float deltaZ;
    boolean done = false;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_menu);
        fragmentManager = getSupportFragmentManager();

        mInitialized = false;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.Home:
                        fragment = new BlueToothFragment();
                        break;

                    case R.id.colorchange:
                        fragment = new LedControlerFragment();
                        break;

                    case R.id.spaceace:
                        fragment = new AccelerationFragment();
                        break;


                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
    }

    public void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void onPause() {

        super.onPause();

        mSensorManager.unregisterListener(this);

    }

    @Override

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

// can be safely ignored for this demo

    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        float x = event.values[0];

        float y = event.values[1];

        float z = event.values[2];

        if (!mInitialized) {

            mLastX = x;

            mLastY = y;

            mLastZ = z;


            mInitialized = true;

        } else {

            deltaX = Math.abs(mLastX - x);

            deltaY = Math.abs(mLastY - y);
            deltaZ = Math.abs(mLastZ - z);

            float deltaZ = Math.abs(mLastZ - z);

            if (deltaX < NOISE) deltaX = (float)0.0;

            if (deltaY < NOISE) deltaY = (float)0.0;

            if (deltaZ < NOISE) deltaZ = (float)0.0;

            mLastX = x;

            mLastY = y;

            mLastZ = z;


            if (deltaX > deltaY) {

                // iv.setImageResource(R.drawable.FigureA);

            } else if (deltaY > deltaX) {

                // iv.setImageResource(R.drawable.FigureB);

            } else {



            }

        }

    }
    public void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.textanimation);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.SpaceText);
        tv.clearAnimation();
        tv.startAnimation(a);
    }


    public void rainbowText()
    {
        TextView title = (TextView)findViewById(R.id.Title);
        TextView createdby = (TextView)findViewById(R.id.CreatedBy);
        TextView author = (TextView)findViewById(R.id.Author);

        TextView buttonstart = (TextView)findViewById(R.id.connectButton);


        // TextView home = (TextView)findViewById(R.id.Home);
        //TextView ledcontroller = (TextView)findViewById(R.id.Led_Controller);


        Shader textShader=new LinearGradient(0, 0, 0, 20,
                new int[]{getResources().getColor(R.color.violet),getResources().getColor(R.color.indigo),
                        getResources().getColor(R.color.blue),
                        getResources().getColor(R.color.green),
                        getResources().getColor(R.color.yellow),
                        getResources().getColor(R.color.orange),
                        getResources().getColor(R.color.red)},
                new float[]{0,0.2f,0.4f,0.6f,0.8f,0.9f,1}, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
        title.setTextSize(40);

        createdby.getPaint().setShader(textShader);
        createdby.setTextSize(20);

        author.getPaint().setShader(textShader);
        author.setTextSize(20);

        buttonstart.getPaint().setShader(textShader);
        buttonstart.setTextSize(20);


        // home.getPaint().setShader(textShader);
        //home.setTextSize(20);

        //ledcontroller.getPaint().setShader(textShader);
        // ledcontroller.setTextSize(20);

    }
}