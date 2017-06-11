package com.smwg.bwade.colorfiesta;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

/**************************************************************************************************
 * This Class/Fragment has functions to send data in the format of:
 * < String, Red(0-225), Green(0-255), Blue(0-255), AccelerationFragment(0-Led Strip length), Command(1,2)></>
 *************************************************************************************************/
public class LedControlerFragment extends Fragment implements View.OnClickListener {
    Button sendButton,clear,clearLedButton;
    TextView textView;
    EditText editText;
    BlueToothFragment bluetooth;

    /***********************************************************************************************
     *  Empty constructor required for all Fragment Classes
     **********************************************************************************************/
    public LedControlerFragment()
    {

    }
    /**************************************************************************************************
         The first method thats called when Led tab is clicked, initializes Send Values and Clear Log
     Button.
     ************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_led_controller, container, false);

        sendButton = (Button)v.findViewById(R.id.sendValues);
        clear = (Button)v.findViewById(R.id.clearLog);
        clearLedButton = (Button)v.findViewById(R.id.clearled);

        editText = (EditText) v.findViewById(R.id.editText);
        textView = (TextView) v.findViewById(R.id.textView);



        sendButton.setOnClickListener(this);
        clear.setOnClickListener(this);
        clearLedButton.setOnClickListener(this);

        return v;
    }
    /**********************************************************************************************
    *  adds a listner to when the "Send Values" or "Clear Log" buttton is clicked
     *
     **********************************************************************************************/
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.sendValues:
                SendColorValues();
            case R.id.clearLog:
                clearLog();
            case R.id.clearled:
                ClearLED();

                break;

        }
    }

    /**********************************************************************************************
    *
     *                                 LED Controller
     *
     **********************************************************************************************/
    public void SendColorValues()
    {
        String string = editText.getText().toString();
        string.concat("\n");

        try
        {
            BlueToothFragment.outputStream.write(string.getBytes());

        }
        catch (IOException ex)
        {

        }

        textView.append("\nSent Data:"+string+"\n");
    }


/**************************************************************************************************
*                           Clear Log
 **************************************************************************************************/
    public void clearLog()
    {
        textView.setText("");

    }
//**************************************************************************************************

/**** **********************************************************************************************
*
*                                      Make all pixels white
*
**************************************************************************************************/
    public void ClearLED() {
        editText.setText("<A,0,0,0,0,1>");
    }

}
