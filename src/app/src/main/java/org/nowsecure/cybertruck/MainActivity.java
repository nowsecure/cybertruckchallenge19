package org.nowsecure.cybertruck;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.nowsecure.cybertruck.detections.HookDetector;
import org.nowsecure.cybertruck.keygenerators.Challenge1;
import org.nowsecure.cybertruck.bluetooth.Pairing;
import org.nowsecure.cybertruck.keygenerators.Challenge2;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CyberTruckChallenge";
    private static Context context;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create context
        MainActivity.context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch sw_tamperproof = (Switch) findViewById(R.id.tamperproof);
        sw_tamperproof.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HookDetector hookdetector = new HookDetector();

                    if (hookdetector.isFridaServerInDevice()) {
                        Toast.makeText(context, "Tampering detected!", Toast.LENGTH_SHORT).show();
                        new CountDownTimer(2000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }
                            public void onFinish() {
                                System.exit(0);
                            }
                        }.start();
                    }
                }
            }
        });


        Switch sw_wiping = (Switch) findViewById(R.id.wipingkeys);
        sw_wiping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, "Wiping keys...!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        final Button unlock =  (Button) findViewById(R.id.unlock_car);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unlock != null)
                {
                    Toast.makeText(getApplicationContext(),"Unlocking cars...",Toast.LENGTH_SHORT).show();
                    unlockcars();
                }
            }
        });

        // This does nothing, just add "dead" bluetooth code
        Pairing pairing = new Pairing();

    }


    protected void unlockcars() {
        Challenge1 car1 = new Challenge1();
        Challenge2 car2 = new Challenge2(this.context);
        init();   //Challenge3 is in native
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void init();

}
