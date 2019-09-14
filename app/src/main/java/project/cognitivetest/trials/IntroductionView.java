package project.cognitivetest.trials;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.cognitivetest.R;

public class IntroductionView extends AppCompatActivity implements View.OnClickListener {
    private TextView text;
    private Button button1, button2;
    private TextToSpeech textToSpeech;
    private String participantID, staffID;
    private String TAG = "Introduction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_view);

        text = (TextView) findViewById(R.id.textView);
        button1 = (Button) findViewById(R.id.read_button);
        button2 = (Button) findViewById(R.id.continue_button);

        initView();

        //Read out the text
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
    }

    private void initView() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        text.setText(R.string.introduction_text);
        // Get participant ID from last activity
        Intent intent = getIntent();
        participantID = intent.getStringExtra("participantID");
        staffID = intent.getStringExtra("staffID");
    }

    //Cases clicking on the buttons
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.read_button:
                read();
                break;
            case R.id.continue_button:
                continue_btn();
                break;
            default:
                break;
        }
    }

    private void read() {
        Log.d(TAG, "click on read button.");
        textToSpeech.speak(text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,
                null);
    }

    private void continue_btn() {
        Log.d(TAG, "click on continue button.");
        Intent intent=new Intent(IntroductionView.this, FirstTrialView.class);
        intent.putExtra("participantID", participantID);
        intent.putExtra("staffID", staffID);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null)
            textToSpeech.shutdown();
        super.onDestroy();
    }

}
