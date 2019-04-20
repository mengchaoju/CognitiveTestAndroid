package project.cognitivetest.video_image;

import android.content.Intent;
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

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.cognitivetest.R;
import serviceLayer.Settings;
import serviceLayer.VideoService;
import serviceLayer.util.ServerIP;

public class VideoView extends AppCompatActivity implements View.OnClickListener {

    private Button play, pause, image, finish, play2, image2, retry;
    private ImageView video;
    private TextView infoText;
    private Bitmap copyBitmap;
    private Paint paint;
    private Canvas canvas;
    private Settings settings;
    private VideoService videoService, videoService2;
    private ServerIP serverIP;

    private String pixelData, pixelData2;  // pixelData is copy trial data,
                                            // pixelData2 is recall trial data
    private float startX;
    private float startY;
    private int seq = 0;  //The sequence number of line currently drawn
    private int isResponse = 0;  // Indicating whether the server respond or not. 0 means no response yet
    private int isPause = 0;  //Indicate whether the video is paused
    private int trialCode = 0;  // 0 means copy trial, 1 means recall trial
    private ArrayList<Long> timeLine, timeLine2;
    private int totalPoints, totalPoints2;
    private String participantID = "sampleUser";
    private final String TAG = "VideoView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        initView();
    }

    /**
     * Initialize the views and the setting, timeLine and serverIP instance.
     */
    private void initView() {
        play = (Button) findViewById(R.id.play_button);
        play2 = (Button) findViewById(R.id.play_button2);
        pause = (Button) findViewById(R.id.pause_button);
        image = (Button) findViewById(R.id.image_button);
        image2 = (Button) findViewById(R.id.image_button2);
        finish = (Button) findViewById(R.id.finish_button);
        retry = (Button) findViewById(R.id.retry_button);
        video = (ImageView) findViewById(R.id.video_imageView);
        infoText = (TextView) findViewById(R.id.video_information);

        timeLine = new ArrayList<>();
        settings = new Settings();
        serverIP = new ServerIP();
        isResponse = 0;

        play.setEnabled(false);
        play2.setEnabled(false);
        image.setEnabled(false);
        image2.setEnabled(false);
        infoText.setText(R.string.video_info_getData);
        play.setOnClickListener(this);
        play2.setOnClickListener(this);
        pause.setOnClickListener(this);
        image.setOnClickListener(this);
        image2.setOnClickListener(this);
        finish.setOnClickListener(this);
        retry.setOnClickListener(this);

        Intent intent = getIntent();
        try {
            participantID = intent.getStringExtra("participantID");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        new FetchData().execute("");
    }

    /**
     * The listener for the buttons.
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.play_button):
                play.setVisibility(View.INVISIBLE);
                play2.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                trialCode = 0;
                playBtn();
                break;
            case(R.id.play_button2):
                play.setVisibility(View.INVISIBLE);
                play2.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                trialCode = 1;
                playBtn();
                break;
            case(R.id.pause_button):
                pauseBtn();
                break;
            case(R.id.image_button):
                play.setVisibility(View.INVISIBLE);
                play2.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                trialCode = 0;
                imageBtn();
                break;
            case(R.id.image_button2):
                play.setVisibility(View.INVISIBLE);
                play2.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                trialCode = 1;
                imageBtn();
                break;
            case(R.id.finish_button):
                finishBtn();
                break;
            case(R.id.retry_button):
                // TODO: Not functional yet.
                Log.d(TAG, "Click on retry button");
                new FetchData().execute("");
                break;
        }
    }

    /**
     * Dealing with the play button.
     * When clicking, play the video based on the requested user data.
     */
    private void playBtn() {
        Log.d(TAG, "Click on play button.");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_background);
        copyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        // Create a pen instance
        paint = new Paint();
        // Create a sketchpad instance
        paint.setStrokeWidth(settings.getStrokeWidth());
        canvas = new Canvas(copyBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        video.setImageBitmap(copyBitmap);
        Log.d(TAG, "Video player initialised!");
        new VideoTimeLineHandler().execute("");
    }

    /**
     * The pause button. When clicking, pause the video play and change to a continue button.
     */
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

    /**
     * The image button. When clicking, show the image.
     */
    private void imageBtn() {
        Log.d(TAG,"Click on showImage button");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_background);
        copyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        // Create a pen instance
        paint = new Paint();
        // Create a sketchpad instance
        paint.setStrokeWidth(settings.getStrokeWidth());
        canvas = new Canvas(copyBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        video.setImageBitmap(copyBitmap);
        Log.d(TAG, "ImageView initialised!");
        finish.setVisibility(View.VISIBLE);
        for (int i=0;i<totalPoints;i++) {
            playVideo();
        }
    }

    /**
     * When click on finish button, restart the view and clear the drawing bitmap
     * And reset the counters
     */
    private void finishBtn() {
        Log.d(TAG, "Click on finish button");
        play.setVisibility(View.VISIBLE);
        play2.setVisibility(View.VISIBLE);
        video.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);
        finish.setVisibility(View.INVISIBLE);
        seq = 0;  // Reset the counter.
        videoService.resetSequence();
        videoService2.resetSequence();
        copyBitmap.recycle();
        paint.reset();
    }

    /**
     * This function deals with drawing all the pixels on the screen.
     */
    private void playVideo() {
        if (trialCode == 0) {
            Log.d(TAG, "copy trial!");
            if (videoService.getSeq()!=seq) {
                seq = videoService.getSeq();
                startX =videoService.getNextX();
                startY = videoService.getNextY();
                canvas.drawPoint(startX, startY, paint);
                video.setImageBitmap(copyBitmap);
                videoService.increaseSeq();
            } else {
                float curX = videoService.getNextX();
                float curY = videoService.getNextY();
                canvas.drawLine(startX, startY, curX, curY, paint);
                startX = curX;
                startY = curY;
                video.setImageBitmap(copyBitmap);
                videoService.increaseSeq();
            }
        } else {
            Log.d(TAG, "recall trial!");
            if (videoService2.getSeq()!=seq) {
                seq = videoService2.getSeq();
                startX =videoService2.getNextX();
                startY = videoService2.getNextY();
                canvas.drawPoint(startX, startY, paint);
                video.setImageBitmap(copyBitmap);
                videoService2.increaseSeq();
            } else {
                float curX = videoService2.getNextX();
                float curY = videoService2.getNextY();
                canvas.drawLine(startX, startY, curX, curY, paint);
                startX = curX;
                startY = curY;
                video.setImageBitmap(copyBitmap);
                videoService2.increaseSeq();
            }
        }

    }

    /**
     * Request pixel data from server using okHttp POST() function
     */
    private void getDataFromServer() {
        String url = serverIP.TRIALSDATAURL;
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        Log.d(TAG, "participantID:"+participantID);
        formBuilder.add("username", participantID);
        Request request1 = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request1);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(VideoView.this,"Server failure.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try {
                    String resp = response.body().string();  //The response from server
                    // Containing pixel data of both trials, separated by |
                    Log.d(TAG, "Server response:"+resp);
                    String[] dataSet = resp.split("&");
                    pixelData = dataSet[0];
                    Log.d(TAG, "copyPixels:"+pixelData);
                    pixelData2 = dataSet[1];
                    videoService = new VideoService(pixelData);
                    videoService2 = new VideoService(pixelData2);
                    totalPoints = videoService.getTotalPoints();
                    totalPoints2 = videoService2.getTotalPoints();
                    timeLine = videoService.getTimeline();
                    timeLine2 = videoService2.getTimeline();
                    isResponse = 1;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
        while(true) {
            if (isResponse == 1) {
                retry.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    /**
     * Do in background, dealing with getting data from server
     */
    private class FetchData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            getDataFromServer();
//            pixelData = settings.getSamplePixelData();  //For testing
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //When finish fetching data, enable the play button, show information
            play.setEnabled(true);
            play2.setEnabled(true);
            image.setEnabled(true);
            image2.setEnabled(true);
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
     * Do in background, dealing with the timeline of video playing.
     */
    private class VideoTimeLineHandler extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "Total points to draw:"+Integer.toString(totalPoints));
            if (trialCode == 0) {
                // Copy trial
                int i =0;  //The counter
                while (true) {
                    if (i == totalPoints) {
                        break;
                    } else if (isPause!=1) {
                        try {
                            Log.d(TAG, "Will sleep:"+timeLine.get(i).toString()+" milliseconds");
                            Thread.sleep(timeLine.get(i));
                            playVideo();
                            Log.d(TAG, "Draw:"+Integer.toString(i+1)+"point");
                            i++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d(TAG, "thread ended unexpected!");
                        }
                    }
                }  //When finish playing, show finish button on the screen.
            } else {
                // Recall trial
                int i =0;  //The counter
                while (true) {
                    if (i == totalPoints2) {
                        break;
                    } else if (isPause!=1) {
                        try {
                            Thread.sleep(timeLine2.get(i));
                            playVideo();
                            i++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d(TAG, "thread ended unexpected!");
                        }
                    }
                }  //When finish playing, show finish button on the screen.
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG,"Finish playing video");
            finish.setVisibility(View.VISIBLE);
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
