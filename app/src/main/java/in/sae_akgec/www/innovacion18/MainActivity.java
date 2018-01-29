package in.sae_akgec.www.innovacion18;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView bluetooth_button, bot_button, led_button, developer_button, society_button;
    BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_CONNECTION = 2;

    String address = null;

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
                bluetoothHandler();
            }
        });

        bot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address != null) {
                    Intent intent = new Intent(MainActivity.this, BotActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("address", address);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please connect to bluetooth device first", Toast.LENGTH_LONG).show();
                }
            }
        });

        led_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address != null) {
                    Intent intent = new Intent(MainActivity.this, LedActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("address", address);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please connect to bluetooth device first", Toast.LENGTH_LONG).show();
                }
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
                Intent intent = new Intent(MainActivity.this, SocietyActivity.class);
                startActivity(intent);
            }
        });
    }

    public void bluetoothHandler() {
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getApplicationContext(), "Sorry your device doesn't support Bluetooth Sensor", Toast.LENGTH_LONG).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }
        if(mBluetoothAdapter.isEnabled()){
            Intent openList = new Intent(MainActivity.this, ListDevices.class);
            startActivityForResult(openList, REQUEST_CONNECTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Has Been Activated", Toast.LENGTH_LONG).show();
                    Intent openList = new Intent(MainActivity.this, ListDevices.class);
                    startActivityForResult(openList, REQUEST_CONNECTION);
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth Has Not Been Activated", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {
                    address = data.getExtras().getString(ListDevices.MAC_ADDRESS);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Get MAC", Toast.LENGTH_LONG).show();
                }
        }
    }


}
