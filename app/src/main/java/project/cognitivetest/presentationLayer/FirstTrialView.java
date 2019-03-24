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
import serviceLayer.ClientService;
import serviceLayer.DataCache;
import serviceLayer.Settings;
import serviceLayer.Timer;

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
    private Timer timer;
    private DataCache dataCache;
    private Settings settings;

    // The available colours
    private int startColour;
    private int endColour;
    private int[] colourRange;
    private int colourFlag = 0;
    private int colourFlag2 = 0;

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
        showToast("Updating... please wait!");
        new LongOperation().execute("");
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
            porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN);
            paint.setXfermode(porterDuffXfermode);
            paint.setStrokeWidth(settings.getMarkPenWidth());
            paint.setColor(0xff9900);
        } else {
            Log.d(TAG, "finish correcting!");
            this.ifMark = 0;
            correctBtn.setText("correct");
            paint.setXfermode(null);
            paint.setStrokeWidth(settings.getStrokeWidth());
            paint.setColor(startColour);
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

        //Initialise the view
        sketchpad = (ImageView) findViewById(R.id.sketchpad);
        startBtn = (Button) findViewById(R.id.start);
        finishBtn = (Button) findViewById(R.id.finish);
        correctBtn = (Button) findViewById(R.id.correct);

        //Set the button listener
        startBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        correctBtn.setOnClickListener(this);
    }

    //Change the colour of painting
    public void changeColor(View view) {
        if (colourFlag2%50 == 0 && colourFlag2!=0) {
            startColour = colourRange[colourFlag2/50%colourRange.length];
            endColour = colourRange[(colourFlag2/50+1)%colourRange.length];
        }
        paint.setColor(getNextColor(startColour, endColour, colourFlag%50));
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
        return "{\"command\":\"firstTrialData\",\"message\":\""+tempStr+"\"}";
    }

    //Construct the timer data to be sent to the server to String
    private String dataConstructor2() {
        String tempStr = "";
        Timestamp[] tm;
        tm = timer.getTime();
        for (int i=0; i<tm.length; i++) {
            tempStr = tempStr + tm[i].toString()+";";
        }
        return "{\"command\":\"firstTrialTimer\",\"message\":\""+tempStr+"\"}";
    }

    /**
     * Do in background, dealing with getting data from and data transmission
     */
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"saving data");
            //Send the data then close the socket
            ClientService client = new ClientService();
            client.sendData(dataConstructor());
            client.closeCon();
            ClientService client2 = new ClientService();
            client2.sendData(dataConstructor2());
            client2.closeCon();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent=new Intent(FirstTrialView.this, ContinuePage.class);
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
