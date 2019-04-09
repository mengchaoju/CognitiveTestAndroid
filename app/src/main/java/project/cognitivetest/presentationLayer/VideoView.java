package project.cognitivetest.presentationLayer;

import android.content.SharedPreferences;
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
import project.cognitivetest.presentationLayer.serviceLayer.Settings;
import project.cognitivetest.presentationLayer.serviceLayer.VideoService;
import project.cognitivetest.presentationLayer.until.ServerIP;

public class VideoView extends AppCompatActivity implements View.OnClickListener {

    private Button play, pause, image, finish;
    private ImageView video;
    private TextView infoText;
    private Bitmap copyBitmap;
    private Paint paint;
    private Canvas canvas;
    private Settings settings;
    private VideoService videoService;
    private ServerIP serverIP;

    private String pixelData;
    private float startX;
    private float startY;
    private int seq = 0;  //The sequence number of line currently drawn
    private String participantID = "abc";
    private int isPause = 0;  //Indicate whether the video is paused
    private ArrayList<Long> timeLine;
    private int totalPoints;
    private String userName = "sampleUser";
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
        image = (Button) findViewById(R.id.image_button);
        finish = (Button) findViewById(R.id.finish_button);
        video = (ImageView) findViewById(R.id.video_imageView);
        infoText = (TextView) findViewById(R.id.video_information);
        timeLine = new ArrayList<>();
        settings = new Settings();
        serverIP = new ServerIP();

        play.setEnabled(false);
        image.setEnabled(false);
        infoText.setText(R.string.video_info_getData);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        image.setOnClickListener(this);
        finish.setOnClickListener(this);

        new FetchData().execute("");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.play_button):
                play.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                playBtn();
                break;
            case(R.id.pause_button):
                pauseBtn();
                break;
            case(R.id.image_button):
                imageBtn();
                break;
            case(R.id.finish_button):
                finishBtn();
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
        new VideoHandler().execute("");
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

    private void imageBtn() {
        Log.d(TAG,"Click on showImage button");

    }

    /**
     * When click on finish button, restart the view and clear the drawing bitmap
     * And reset the counters
     */
    private void finishBtn() {
        Log.d(TAG, "Click on finish button");
        play.setVisibility(View.VISIBLE);
        video.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        finish.setVisibility(View.INVISIBLE);
        seq = 0;  // Reset the counter.
        videoService.resetSequence();
        copyBitmap.recycle();
        paint.reset();
    }

    private void playVideo() {
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
    }

//    private void getDataFromServer() {
//        client = new ClientService();
//        client.sendData("{\"command\":\"getVideo\",\"message\":\""+participantID+"\"}");
//        Log.d(TAG, "getting data from remote server.");
//        while (true) {
//            client.getData();
//        }
////        client.closeCon();
//    }

    private void getDataFromServer() {
        String url = serverIP.getTRIALSDATAURL();
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
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
                                                                   // 1 means success, 0 means failure.
//                    switch (resp) {
//                        case ("0"):
//                            break;
//                        case ("1"):
//                            requestPixelData();
//                            break;
//                    }
                    Log.d(TAG, "Server response:"+resp);
                    pixelData = resp;
                    Log.d(TAG, "pixel data:"+pixelData);
                    videoService = new VideoService(pixelData);
                    totalPoints = videoService.getTotalPoints();
                    timeLine = videoService.getTimeline();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Request pixel data from server using okHttp GET() function
     */
    private void requestPixelData() {
        String url = serverIP.getTRIALSDATAURL();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        try {
            Log.d(TAG, "Successfully get pixel data from server.");
            Response resp = call.execute();
            pixelData = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
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
            image.setEnabled(true);
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
            Log.d(TAG, "Total points to draw:"+Integer.toString(totalPoints));
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
