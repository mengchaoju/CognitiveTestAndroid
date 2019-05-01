package project.cognitivetest.trials;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.cognitivetest.R;
import serviceLayer.Settings;

public class ContinuePage extends AppCompatActivity {

    private TextView text;
    private Button button;
    private int time;
    private String userName;
    private final String TAG = "ContinuePage";
    private int ifCancel = 0; //Indicate whether the background task is canceled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_page);

        initView();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueBtn();
            }
        });

        new ContinuePage.LongOperation().execute("");
    }

    /**
     * Disable "back" key on the tablet
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    /**
     * Initialise the view and settings.
     */
    private void initView() {
        Settings settings = new Settings();
        this.ifCancel = 0;
        text = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        time = settings.getTimeBetween2Trials();
        text.setText(R.string.continue_text);
        Intent intent = getIntent();
        userName = intent.getStringExtra("p_username");
        Log.d(TAG, "Activity view initialized.");
        Log.d(TAG, "Participant username:"+userName);
    }

    private void continueBtn() {
        this.ifCancel = 1;
        Intent intent=new Intent(ContinuePage.this, SecondTrialView.class);
        // Pass the username of participant to next activity.
        intent.putExtra("p_username", userName);
        startActivity(intent);
    }

    /**
     * Handling with timer.
     */
    private void timeWarning() {
        while (time > 0) {
            try {
                Log.d(TAG, Integer.toString(time)+" seconds left.");
                Thread.sleep(1000);  //1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
            if ((time == 60)&&(ifCancel == 0)) {
                WarningTone();
            }
        }
        if (ifCancel == 0) {
            //When it comes to the expected time, go to the next page immediately
            WarningTone();
            continueBtn();
        }
    }

    /**
     * One minute before the set time, warn the user.
     * Using both a ring tone and a vibrator
     */
    private void WarningTone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();  //play the ring tone
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            vibrator.vibrate(1000);  //Using vibrator
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * This is an operation to be carried out in background.
     */
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            timeWarning();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
