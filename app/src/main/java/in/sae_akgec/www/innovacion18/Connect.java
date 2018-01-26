package in.sae_akgec.www.innovacion18;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Connect extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton imageButton8;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_CONNECTION = 2;

    ConnectedThread connectedThread;

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothDevice myDevice = null;
    BluetoothSocket mySocket = null;

    public boolean connecto = false;
    private static String MAC = null;

    UUID M_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        imageButton8 = (ImageButton) findViewById(R.id.imageButton8);



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Your Device Not Have Bluetooth",Toast.LENGTH_LONG).show();
        }else if (!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BLUETOOTH);
        }


        imageButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connecto) {
                    //disconnect
                    try {
                        mySocket.close();
                        connecto = false;
                        Toast.makeText(getApplicationContext(), "BLUETOOTH was Disconnected", Toast.LENGTH_LONG).show();
                    } catch (IOException error){
                        Toast.makeText(getApplicationContext(), "An ERROR Has Occurred: " + error, Toast.LENGTH_LONG).show();

                    }
                } else {
                    // connect
                    Intent openList = new Intent(Connect.this, ListDevices.class);
                    startActivityForResult(openList, REQUEST_CONNECTION);
                }
            }
        });

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
                    myDevice = bluetoothAdapter.getRemoteDevice(MAC);
                    try {
                        mySocket = myDevice.createRfcommSocketToServiceRecord(M_UUID);

                        mySocket.connect();

                        connecto = true;

                        connectedThread = new ConnectedThread(mySocket);
                        connectedThread.start();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.nav_home:
                Intent h = new Intent(Connect.this, Home.class);
                startActivity(h);
                break;
            case R.id.nav_led:
                Intent i = new Intent(Connect.this, LED.class);
                startActivity(i);
                break;
            case R.id.nav_bot:
                Intent g = new Intent(Connect.this, BOT.class);
                startActivity(g);
                break;
            case R.id.nav_connect:
                Intent s = new Intent(Connect.this, Connect.class);
                startActivity(s);
                break;
            case R.id.nav_developer:
                Intent r = new Intent(Connect.this, Developer.class);
                startActivity(r);
                break;
            case R.id.nav_society:
                Intent t = new Intent(Connect.this, Society.class);
                startActivity(t);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mySocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs



    /*          while (true) {
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
            } catch (IOException e) { }
        }

    }






}



