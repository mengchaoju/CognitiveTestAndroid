package project.cognitivetest.presentationLayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;

import project.cognitivetest.R;
import serviceLayer.ClientService;
import serviceLayer.Settings;
import serviceLayer.VideoService;

public class VideoView extends AppCompatActivity implements View.OnClickListener {

    private Button play;
    private Button pause;
    private ImageView video;
    private TextView infoText;
    private ClientService client;
    private Bitmap copyBitmap;
    private Paint paint;
    private Canvas canvas;
    private Settings settings;
    private VideoService videoService;
    private float startX;
    private float startY;
    private int seq = 0;  //The sequence number of line currently drawn
    private String participantID = "abc";
    private int isPause = 0;  //Indicate whether the video is paused
    private ArrayList<Long> timeLine;
    private int totalPoints;
    private final String TAG = "VideoView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        initView();
    }

    private void initView() {
        play = (Button) findViewById(R.id.play_button);
        pause = (Button) findViewById(R.id.pause_button);
        video = (ImageView) findViewById(R.id.video_imageView);
        infoText = (TextView) findViewById(R.id.video_information);
        timeLine = new ArrayList<>();
        settings = new Settings();

        play.setEnabled(false);
        infoText.setText(R.string.video_info_getData);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);

        new FetchData().execute("");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.play_button):
                play.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                playBtn();
                break;
            case(R.id.pause_button):
                pauseBtn();
                break;
        }
    }

    private void playBtn() {
        Log.d(TAG, "Click on play button.");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_background);
        copyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        // Create a pen instance
        paint = new Paint();
        // Create a sketchpad instance
        paint.setStrokeWidth(12);
        canvas = new Canvas(copyBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        video.setImageBitmap(copyBitmap);
        Log.d(TAG, "Video player initialised!");
    }

    private void pauseBtn() {
        Log.d(TAG, "Click on pause button.");
        if (isPause == 0) {
            isPause = 1;
            pause.setText("Continue");
        } else {
            isPause = 0;
            pause.setText("Pause");
        }
    }

    private void playVideo() {
        if (videoService.getSeq()!=seq) {
            seq = videoService.getSeq();
            startX =videoService.getNextX();
            startY = videoService.getNextY();
            canvas.drawPoint(startX, startY, paint);
            videoService.increaseSeq();
        } else {
            float curX = videoService.getNextX();
            float curY = videoService.getNextY();
            canvas.drawLine(startX, startY, curX, curY, paint);
            videoService.increaseSeq();
        }

    }

    private void getDataFromServer() {
        client = new ClientService();
        client.sendData("{\"command\":\"getVideo\",\"message\":\""+participantID+"\"}");
        Log.d(TAG, "getting data from remote server.");
        while (true) {
            client.getData();
        }
//        client.closeCon();
    }

    /**
     * Do in background, dealing with getting data from server
     */
    private class FetchData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
//            getDataFromServer();
            String str = settings.getSamplePixelData();  //For testing
            videoService = new VideoService(str);
            totalPoints = videoService.getTotalPoints();
            timeLine = videoService.getTimeline();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //When finish fetching data, enable the play button, show information
            play.setEnabled(true);
            infoText.setVisibility(View.INVISIBLE);
            showToast("Data fetching success!");
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    /**
     * Do in background, dealing with getting data from server
     */
    private class VideoHandler extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < totalPoints; i++) {
                try {
                    Thread.sleep(timeLine.get(i));
                    playVideo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "thread ended unexpected!");
                }

            }
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


    // To show information on screen
    private void showToast(final String text) {

        VideoView.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoView.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
