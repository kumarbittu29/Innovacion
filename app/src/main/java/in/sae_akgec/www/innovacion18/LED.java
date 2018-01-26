package in.sae_akgec.www.innovacion18;

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
import android.widget.ImageButton;
import android.widget.Toast;

public class LED extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton imageButton9, imageButton10;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);


        imageButton9 = findViewById(R.id.imageButton9);
        imageButton10 = findViewById(R.id.imageButton10);



        imageButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (connecto) {
                    connectedThread.send("1");

                    Toast.makeText(getApplicationContext(), "LED is ON", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is not CONNECTED", Toast.LENGTH_LONG).show();
                }
            }
        });

        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Cnct) {
                    connectedThread.send("0");

                    Toast.makeText(getApplicationContext(), "LED is OFF", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is not CONNECTED", Toast.LENGTH_LONG).show();
                }
            }
        });







        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                Intent h = new Intent(LED.this, Home.class);
                startActivity(h);
                break;
            case R.id.nav_led:
                Intent i = new Intent(LED.this, LED.class);
                startActivity(i);
                break;
            case R.id.nav_bot:
                Intent g = new Intent(LED.this, BOT.class);
                startActivity(g);
                break;
            case R.id.nav_connect:
                Intent s = new Intent(LED.this, Connect.class);
                startActivity(s);
                break;
            case R.id.nav_developer:
                Intent r = new Intent(LED.this, Developer.class);
                startActivity(r);
                break;
            case R.id.nav_society:
                Intent t = new Intent(LED.this, Society.class);
                startActivity(t);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
