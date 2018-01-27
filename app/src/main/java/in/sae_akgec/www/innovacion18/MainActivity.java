package in.sae_akgec.www.innovacion18;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView bluetooth_button, bot_button, led_button, developer_button, society_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declaring button main screen buttons
        bluetooth_button = (ImageView) findViewById(R.id.main_bluetooth_button);
        bot_button = (ImageView) findViewById(R.id.main_bot_button);
        led_button = (ImageView) findViewById(R.id.main_led_button);
        developer_button = (ImageView) findViewById(R.id.main_developer_button);
        society_button = (ImageView) findViewById(R.id.main_club_button);

        // This make all imageview to acts as a button
        buttonListenerActivator();
    }



    private void buttonListenerActivator(){
        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "bluetooth button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        bot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, BOT.class);
                startActivity(intent);
            }
        });

        led_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, LED.class);
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
}
