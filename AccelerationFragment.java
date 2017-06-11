package com.smwg.bwade.colorfiesta;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import static com.smwg.bwade.colorfiesta.R.id.SpaceText;


public class AccelerationFragment extends Fragment implements View.OnClickListener{


    TextView spacetext;
    MainActivity activity;

    Button blastoff,buttonclear;
    public AccelerationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acceleration, container, false);

        blastoff =  (Button)view.findViewById(R.id.blastoff);
        buttonclear   =  (Button)view.findViewById(R.id.clearSpace);
        spacetext = (TextView)view.findViewById(R.id.SpaceText);

        blastoff.setOnClickListener(this);
        buttonclear.setOnClickListener(this);


        return view;
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.blastoff:
                blastOff();
            case R.id.clearSpace:
                ClearSpace();
                break;
        }
    }

    /***********************************************************************************************
     *
     *          Planet           Distance in AU            Travel time
     *       ....................................................................
     *        Mercury              0.387        193.0 seconds   or    3.2 minutes
     *        Venus                0.723        360.0 seconds   or    6.0 minutes
     *        Earth                1.000        499.0 seconds   or    8.3 minutes
     *        Mars                 1.523        759.9 seconds   or   12.6 minutes
     *        Jupiter              5.203       2595.0 seconds   or   43.2 minutes
     *        Saturn               9.538       4759.0 seconds   or   79.3 minutes
     *        Uranus              19.819       9575.0 seconds   or  159.6 minutes
     *        Neptune             30.058      14998.0 seconds   or    4.1 hours
     *        Pluto               39.44       19680.0 seconds   or    5.5 hours
     *        ....................................................................
     *
     *
     *
     *
     *
     *
     **********************************************************************************************/



    public void blastOff() {


        new CountDownTimer(5000, 1000) {
            float X = 0;
            float Y = 0;
            float Z = 0;
            float average = 0;
            float total = 0;


            float Mercury = 1;
            float Venus = 1;
            float Earth = 1;
            float Mars = 2;
            float Jupiter = 5;
            float Saturn =  9;
            float Uranus = 15;
            float Neptune = 19;
            float Pluto = 24;

            float Light = 0;

            int count = 5;
            int Red = 0;
            int Blue = 0;
            int Green = 0;
            String Command = "2";

            public void onTick(long millisUntilFinished) {

                X =  X + activity.deltaX;
                Y =  Y + activity.deltaY;
                Z =   Z + activity.deltaZ;
                count--;
                spacetext.append("You have " + count +" seconds left "+ "\n");
                //activity.RunAnimation();

            }

            public void onFinish()
            {
                spacetext.append("Times Up!" + "\n");
                total = X + Y + Z;
                average = total;

                if(average <= 10)
                {
                    Light = Earth;
                    Red = 0;
                    Green = 255;
                    Blue = 0;
                }
               else if(average > 10 && average <= 20)
                {
                    Light = Mars;
                    Red = 255;
                    Green = 0;
                    Blue = 0;
                }
               else if(average > 20 && average <= 30)
                {
                    Light = Jupiter;
                    Command = "3";
                }

                else if(average > 30 && average <= 50)
                {
                    Light = Saturn;
                    Command = "4";
                }
                else if(average > 50 && average <= 70)
                {
                    Light = Uranus;
                    Command = "5";
                }
                else if(average > 70 && average <= 90)
                {
                    Light = Neptune;
                    Command = "6";
                }
                else if(average > 90)
                {
                    Light = Pluto;
                    Command = "7";
                }

                try
                {
                    String currentdelta ="<" + 'B' + "," + Red + "," + Green + "," + Blue + "," + Light + "," + Command + ">";
                    spacetext.append("You Reached  " + Light + " years " + "\n");
                    spacetext.append(currentdelta + "\n");

                    BlueToothFragment.outputStream.write(currentdelta.getBytes());
                }
                catch (IOException ex)
                {

                }
            }


        }.start();

    }

    public void ClearSpace()
    {
        //textView.setText("");
       spacetext.setText("");
    }


}
