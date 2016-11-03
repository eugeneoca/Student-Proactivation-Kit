package ph.edu.icct.spk.studentsproactivationkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    // Set Properties
    SharedPreferences prefs = null; // Preferences Storage
    int increment;
    boolean firstrun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set essential variables;
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        final ProgressBar splash_load = (ProgressBar)findViewById(R.id.loading_bar);

        splash_load.setMax(3000);
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            public void run() {
                while (splash_load.getProgress() < 3000) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar

                    handler.post(new Runnable() {
                        public void run() {
                            splash_load.setProgress(increment);
                        }
                    });
                    increment++;
                }
            }
        }).start();
        Thread splash = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3500);
                    if(firstrun){
                        Intent intent = new Intent(MainActivity.this, FirstRunActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        splash.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            firstrun = true;
        }
    }


}
