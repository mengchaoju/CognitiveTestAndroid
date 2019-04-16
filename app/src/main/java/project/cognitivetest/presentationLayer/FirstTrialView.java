package project.cognitivetest.presentationLayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;

import project.cognitivetest.R;
import serviceLayer.DataCache;
import serviceLayer.Settings;
import serviceLayer.Timer;
import serviceLayer.UploadDataService;
import util.ServerIP;

public class FirstTrialView extends AppCompatActivity implements View.OnClickListener {

    private ImageView sketchpad;
    private Button startBtn, finishBtn, correctBtn;
    private Bitmap copyBitmap;
    private Paint paint;
    private Canvas canvas;
    private PorterDuffXfermode porterDuffXfermode;
    private float startX;
    private float startY;
    private int seq = 1;  //Sequence number of the current line
    private int ifMark = 0;  //0 means draw, 1 means correct(mark).
    //Indicating whether drawing activity is running or not, 1 = running.
    private int runningFlag = 0;
    private String userName = "sampleUser";  // The username of this participant
    private Timer timer;
    private DataCache dataCache;
    private Settings settings;
    private UploadDataService uploadService, uploadService2;

    // The available colours
    private int startColour;
    private int endColour;
    private int[] colourRange;
    private int colourFlag = 0;
    private int colourFlag2 = 0;
    private int enableColour;

    private String TAG = "FirstTrial";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_trial_view);

        initView();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_background);
        copyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        // Create a pen instance
        paint = new Paint();

        paint.setXfermode(null);
        paint.setStrokeWidth(settings.getStrokeWidth());  //The width of drawing pen
        paint.setStrokeCap(Paint.Cap.ROUND);  //Set the cap to round
        if (enableColour == 1) {
            paint.setColor(startColour);
        } else {
            paint.setColor(Color.BLACK);
        }
        canvas = new Canvas(copyBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        sketchpad.setImageBitmap(copyBitmap);
        Log.d(TAG, "sketchpad created!");

        // When touching the screen to draw
        sketchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    //touch the screen
                    case MotionEvent.ACTION_DOWN:
                        if (seq ==1){
                            // Recording the time when pen down for the first time
                            timer.setTime(2);
                        }
                        Log.d(TAG, "drawing!");
                        startX = event.getX();
                        startY = event.getY();
                        changeColor(v);
                        canvas.drawPoint(startX, startY, paint);
                        sketchpad.setImageBitmap(copyBitmap);
//                        showToast("drawing on: ("+Float.toString(startX)+", "+
//                                Float.toString(startY)+")"+"seq: "+seq);
                        break;
                    //move on screen
                    case MotionEvent.ACTION_MOVE:
                        //The coordinate of drawing
                        float x = event.getX();
                        float y = event.getY();
//                        Log.d(TAG, "Colour flag"+Integer.toString(colourFlag));
                        changeColor(v);
                        canvas.drawLine(startX, startY, x, y, paint);
                        sketchpad.setImageBitmap(copyBitmap);
                        startX = x;
                        startY = y;
//                        showToast("drawing on: ("+Float.toString(startX)+", "+
//                                Float.toString(startY)+")"+"seq: "+seq);
                        colourFlag += 1;
                        colourFlag2 += 1;
                        break;
                    //when the figure leave the screen
                    case MotionEvent.ACTION_UP:
                        float upX = event.getX();
                        float upY = event.getY();
                        // Increase the sequence number when a line is completed
                        seq += 1;
                        break;
                }
                return true;
            }
        });

    }

    //Cases clicking on the buttons
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.start:
                startDraw();
                break;
            case R.id.finish:
                finishDraw();
                break;
            case R.id.correct:
                correct();
                break;
            default:
                break;
        }
    }

    /**
     *  When clicking on finish button, should go to the next page and stop the timer
     *  Also, data will be automatically sent to the server.
     */
    private void finishDraw() {
        timer.setTime(3);
        this.runningFlag = 0;  //Mark that this activity is no longer running
        sketchpad.setEnabled(false);  // Disable the sketchpad.
        showToast("Updating... please wait!");
        new UploadData().execute("");
    }

    /**
     * When clicking on the correct/draw button, the text will change,
     * the pen will change to mark pen,
     * and the flag indicating draw/correct will also change
     */
    private void correct() {
        if (ifMark == 0) {
            Log.d(TAG, "start correcting!");
            this.ifMark = 1;
            correctBtn.setText("draw");
            porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
            paint.setXfermode(porterDuffXfermode);
            paint.setStrokeWidth(settings.getMarkPenWidth());
            paint.setColor(Color.YELLOW);
        } else {
            Log.d(TAG, "finish correcting!");
            this.ifMark = 0;
            correctBtn.setText("correct");
            paint.setXfermode(null);
            paint.setStrokeWidth(settings.getStrokeWidth());
            if (enableColour == 1) {
                paint.setColor(startColour);
            } else {
                paint.setColor(Color.BLACK);
            }
        }
    }

    /**
     * When clicking on the start button, the finish button will appear,
     * the start button will hide, a timestamp is record,
     * and the sketchpad will be enabled.
     */
    private void startDraw() {
        timer.setTime(1);  //record the start time
        startBtn.setVisibility(View.INVISIBLE);  //Hide the start button
        finishBtn.setVisibility(View.VISIBLE);  //Enable the finish button
        sketchpad.setVisibility(View.VISIBLE);  //Enable the sketchpad
        new SaveData().execute("");
        this.runningFlag = 1;
    }

    //Initialise all the views
    private void initView() {
        timer = new Timer();  //Initialise the timer
        dataCache = new DataCache();  //Initialise the data cache
        settings = new Settings();  //Initialise the settings
        //Get the available pen colours
        startColour = settings.getStartColour();
        endColour = settings.getEndColour();
        colourRange = settings.getColourRange();
        enableColour = settings.getEnableColour();

        //Initialise the view
        sketchpad = (ImageView) findViewById(R.id.sketchpad);
        startBtn = (Button) findViewById(R.id.start);
        finishBtn = (Button) findViewById(R.id.finish);
        correctBtn = (Button) findViewById(R.id.correct);

        //Set the button listener
        startBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        correctBtn.setOnClickListener(this);

        Log.d(TAG, "Activity view initialized.");
        Log.d(TAG, "Participant username:"+userName);
    }

    //Change the colour of painting if the function is enabled
    public void changeColor(View view) {
        if (enableColour == 1) {
            if (colourFlag2%50 == 0 && colourFlag2!=0) {
                startColour = colourRange[colourFlag2/50%colourRange.length];
                endColour = colourRange[(colourFlag2/50+1)%colourRange.length];
            }
            paint.setColor(getNextColor(startColour, endColour, colourFlag%50));
        } else if (ifMark == 1){
            paint.setColor(Color.YELLOW);
        } else {
            paint.setColor(Color.BLACK);
        }
    }

    //Get the next colour
    private int getNextColor(int cl1, int cl2,float pixelNum) {
        float r1,g1,b1,r2,g2,b2;
        r1 = Color.red(cl1); g1=Color.green(cl1); b1=Color.blue(cl1);
        r2 = Color.red(cl2); g2=Color.green(cl2); b2=Color.blue(cl2);
        r1 += ((r2 - r1) / 50) * pixelNum;
        g1 += ((g2 - g1) / 50) * pixelNum;
        b1 += ((b2 - b1) / 50) * pixelNum;
        return Color.rgb((int)r1,(int)g1,(int)b1);
    }

    //Saving the data of pixels information to cache
    private void dataSaver() {
        float x = 0f, y = 0f;
        while (runningFlag == 1) {
            if ((startX != x)||(startY != y)) {
                String tempData = strConstructor();
                x = startX;
                y = startY;
                dataCache.save(tempData);
            }
        }
    }

    //Construct the data to save to format of x, y, time, sequence number, draw/correct flag
    private String strConstructor() {
        return Float.toString(startX)+","+Float.toString(startY)+","+timer.getCurTime().toString()
                +","+Integer.toString(seq)+","+Integer.toString(ifMark);
    }

    //Construct the pixel data to be sent to the server to String
    private String dataConstructor() {
        String tempStr = "";
        ArrayList<String> tempArr;
        tempArr = dataCache.getArr();
        for (int i=0; i<tempArr.size(); i++) {
            tempStr = tempStr + tempArr.get(i)+";";
        }
//        System.out.println("tempStr:"+tempStr);
        return tempStr;
    }

    //Construct the timer data to be sent to the server to String
    private String dataConstructor2() {
        String tempStr = "";
        Timestamp[] tm;
        tm = timer.getTime();
        for (int i=0; i<tm.length; i++) {
            tempStr = tempStr + tm[i].toString()+";";
        }
        return tempStr;
    }

    /**
     * Do in background, dealing with getting data from and data transmission
     */
    private class UploadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"saving data");
            String data = dataConstructor();  // The pixel data
            String data2 = dataConstructor2();  // The time line data
            String url = ServerIP.UPLOADCOPY;
            uploadService = new UploadDataService(url, userName, data, data2);
            uploadService.send();
            // Check the state of uploading service, if server fails, retry sending
            int counter = 1;
            while (true) {
                showToast("Try to connect to server...");
                int ifSuc = uploadService.getIfSuccess();
                if (ifSuc == 1) {
                    break;
                }
                if (counter%20 == 0){
                    uploadService.send();  // Retry sending after given time
                }
                try {
                    Thread.sleep(settings.getRetryTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter += 1;
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // When finish uploading, goto next page.
            Intent intent=new Intent(FirstTrialView.this, ContinuePage.class);
            // Pass the username of participant to next activity.
            intent.putExtra("p_username", userName);
            startActivity(intent);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    /**
     * Do in background, dealing with data saving to cache
     */
    private class SaveData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            dataSaver();
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

    // To show coordinate information on screen
    private void showToast(final String text) {

        FirstTrialView.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FirstTrialView.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
