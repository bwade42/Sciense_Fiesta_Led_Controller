package com.smwg.bwade.colorfiesta;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/*
   Created by Brandon Wade for Science Fiesta 2017
 */
//*************************************************************************************************
//******This class is used to connect a device to a bluetooth when the "Connect" button is clicked**
//******Must paired with your BlueTooth device either "HTC-05" or "HTC-06"**************************
public class BlueToothFragment extends Fragment implements View.OnClickListener
{
 //*************************************************************************************************
 //******Variables used for conenction with BlueTooth Device****************************************
    public final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    public static BluetoothDevice device;
    public static BluetoothSocket socket;
    public static OutputStream outputStream;
   public static InputStream inputStream;

    Button connectButton;

   static boolean  deviceConnected=false;

    Thread thread;
    byte buffer[];
    int bufferPosition;
    static boolean stopThread;
    boolean done;

    TextView title,createdby,author;
//**************************************************************************************************
//******Required Empty Contstructor*****************************************************************
    public BlueToothFragment()
    {
        // Required empty public constructor
    }
//**************************************************************************************************
//******The first method thats called when Home tab is clicked, initializes connect button**********
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_page, container, false);

        connectButton =  (Button)view.findViewById(R.id.connectButton);
         //stopConnectButton =  (Button)view.findViewById(R.id.stopConnection);
        title = (TextView)view.findViewById(R.id.Title);
        createdby = (TextView)view.findViewById(R.id.CreatedBy);
        author = (TextView)view.findViewById(R.id.Author);

        connectButton.setOnClickListener(this);
        //stopConnectButton.setOnClickListener(this);

        setUiEnabled(false);
        rainbowText();

        return view;
    }

    /**************************************************************************************************
     *
     * @param v
     * Controls what happens Connect is clicked
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.connectButton:
               Connect();
                break;
        }
    }

    /**************************************************************************************************
     *
     * @param bool Enabled button True/False
     *  Disables buttons once a connection is made
     */
    public void setUiEnabled(boolean bool)
    {
        connectButton.setEnabled(!bool);
    }

    /**************************************************************************************************
     *
     * @return True if a device is paired with Bluetooth module
     *
     */
    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            //Add some error message if no adapter is found
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            //Display some error message saying a deviced isnt paired
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                device=iterator;
                found=true;
                break;
            }
        }
        return found;
    }

    /**************************************************************************************************
     *
     * @return True if a device is connected
     * "HTC-05" will go from a fast blinking red light to a slow blinking light incicating a
     * connection
     * "HTC-06" is very similar
     */
    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();



            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    /**************************************************************************************************
     *  Parses data that will be read from bluetooth device
     */
    void beginListenForData()
    {

        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];


        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {

                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {


                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    //textView.append(string);
                                    // textView2.append(string);


                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    /*************************************************************************************************
     * This is called when the Connect button is clicked
     */
    public void Connect()
    {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
            }

        }
    }
    /**********************************************************************************************/
    public void rainbowText()
    {

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
    }
}
