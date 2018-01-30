package in.sae_akgec.www.innovacion18;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BotActivity extends AppCompatActivity {
    ImageView upButton, downButton, rightButton, leftButton, stopButton;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);
        Bundle bundle = this.getIntent().getExtras();
        address = bundle.getString("address");
        BluetoothTask bluetoothTask = new BluetoothTask();
        bluetoothTask.execute();
        upButton = (ImageView) findViewById(R.id.bot_up);
        downButton = (ImageView) findViewById(R.id.bot_down);
        rightButton = (ImageView) findViewById(R.id.bot_right);
        leftButton = (ImageView) findViewById(R.id.bot_left);
        stopButton = (ImageView) findViewById(R.id.dummy);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveUp();
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveDown();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnRight();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnLeft();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCar();
            }
        });
    }

    public class BluetoothTask extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(BotActivity.this, "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void msgShort(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void turnLeft() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("L".toString().getBytes());
                msgShort("Turning Left");
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void turnRight() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("R".toString().getBytes());
                msgShort("Turning Right");
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void moveUp() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("F".toString().getBytes());
                msgShort("Moving Forward");
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void moveDown() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("B".toString().getBytes());
                msgShort("Moving Backward");
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void stopCar() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("S".toString().getBytes());
                msgShort("!!Stop!!");
            } catch (IOException e) {
                msg("Error");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Disconnect();
    }
}
