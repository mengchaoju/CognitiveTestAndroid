package project.cognitivetest.presentationLayer;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.cognitivetest.R;
import serviceLayer.Settings;

public class ContinuePage extends AppCompatActivity {

    private TextView text;
    private Button button;
    private int time;
    private final String TAG = "ContinuePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_page);

        initView();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ContinuePage.this, SecondTrialView.class);
                startActivity(intent);
            }
        });

        new ContinuePage.LongOperation().execute("");
    }

    /**
     * Initialise the view and settings.
     */
    private void initView() {
        Settings settings = new Settings();
        text = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        time = settings.getTimeBetween2Trials();
        text.setText(R.string.continue_text);
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
            if (time == 60) {
                oneMinWarning();
            }
        }
    }

    /**
     * One minute before the set time, warn the user.
     * Using both a ring tone and a vibrator
     */
    private void oneMinWarning() {
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
