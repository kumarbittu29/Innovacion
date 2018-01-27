package in.sae_akgec.www.innovacion18;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ImageView bluetooth_button, bot_button, led_button, developer_button, society_button;
    BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_CONNECTION = 2;

    ConnectThread connectThread;

    BluetoothDevice myDevice = null;
    BluetoothSocket mySocket = null;

    private static String MAC = null;
    public boolean connecto = false;
    UUID M_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // declaring button main screen buttons
        bluetooth_button = (ImageView) findViewById(R.id.main_bluetooth_button);
        bot_button = (ImageView) findViewById(R.id.main_bot_button);
        led_button = (ImageView) findViewById(R.id.main_led_button);
        developer_button = (ImageView) findViewById(R.id.main_developer_button);
        society_button = (ImageView) findViewById(R.id.main_club_button);

        // This make all imageView to acts as a button
        buttonListenerActivator();
    }


    private void buttonListenerActivator() {
        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "bluetooth button clicked", Toast.LENGTH_SHORT).show();
                bluetoothHandler();
            }
        });

        bot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BOT.class);
                startActivity(intent);
            }
        });

        led_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LED.class);
                startActivity(intent);
            }
        });

        developer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Developer.class);
                startActivity(intent);
            }
        });

        society_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Society.class);
                startActivity(intent);
            }
        });
    }

    public void bluetoothHandler() {
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Has Been Activated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth Has Not Been Activated", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(ListDevices.MAC_ADDRESS);
                    myDevice = mBluetoothAdapter.getRemoteDevice(MAC);
                    try {
                        mySocket = myDevice.createRfcommSocketToServiceRecord(M_UUID);
                        mySocket.connect();
                        connecto = true;
                        connectThread = new ConnectThread(mySocket);
                        connectThread.start();
                        Toast.makeText(getApplicationContext(), "You Were Connected With: " + MAC, Toast.LENGTH_LONG).show();
                    } catch (IOException error) {
                        connecto = false;
                        Toast.makeText(getApplicationContext(), "An ERROR Has Occured" + error, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Get MAC", Toast.LENGTH_LONG).show();
                }
        }
    }

    private class ConnectThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectThread(BluetoothSocket socket) {
            mySocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            /* while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                   // mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                     //       .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }*/
        }

        public void send(String dataSend) {
            byte[] msgBuffer = dataSend.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
            }
        }
    }
}
