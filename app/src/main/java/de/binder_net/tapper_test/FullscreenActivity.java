package de.binder_net.tapper_test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FullscreenActivity extends AppCompatActivity {


    public FullscreenActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        View mContentView = findViewById(R.id.fullscreen_content);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    textView.setText("Touch coordinates : " +
//                            String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));

                    final Context context = getApplicationContext();
                    CharSequence text = String.format(Locale.ENGLISH, "X: %f | Y: %f", event.getX(), event.getY());
                    final int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(context, text, duration).show();
                }

                return true;
            }
        });


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                long startTime = System.currentTimeMillis();


                try {
                    Process process = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(process.getOutputStream());

                    Float x,y;

                    float minX = 0.0f;
                    float maxX = 1080.0f;
                    float minY = 0.0f;
                    float maxY = 1920.0f;

                    Random rand = new Random();

                    x = rand.nextFloat() * (maxX - minX) + minX;
                    y = rand.nextFloat() * (maxY - minY) + minY;

                    String cmd = String.format(Locale.ENGLISH, "/system/bin/input tap %f %f\n", x, y);
                    os.writeBytes(cmd);
                    os.writeBytes("exit\n");
                    os.flush();
                    os.close();
                    process.waitFor();

                    Log.d("My-Tag", cmd);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                long endTime   = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                Log.d("My-Tag", String.format(Locale.ENGLISH, "Delta: %d", totalTime));
            }
        }, 1000, 5000);
    }


}
