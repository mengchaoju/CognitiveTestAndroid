package project.cognitivetest.presentationLayer.serviceLayer;
import android.util.Log;

import java.sql.Timestamp;

public class Timer {

    /** T0: the time when turning to the drawing page
     * T1: the time when clicking start button
     * T2: the time when drawing on the screen for the first time
     * T3: the time when clicking finish button
     */
    private Timestamp T0, T1, T2, T3, T4;
    private final String TAG = "Timer";


    public Timer () {
        T0 = new java.sql.Timestamp(System.currentTimeMillis());
        Log.d(TAG, "timer_0 set: "+T0.toString());
    }

    //i: the number of timer to be set
    public void setTime (int i) {
        Timestamp curTime = new java.sql.Timestamp(System.currentTimeMillis());
        Log.d(TAG, "timer_"+Integer.toString(i)+" set: "+curTime.toString());
        switch (i) {
            case(1):
                T1 = curTime;
                break;
            case (2):
                T2 = curTime;
                break;
            case (3):
                T3 = curTime;
                break;
            case(4):
                T4 = curTime;
                break;
        }
    }

    public Timestamp[] getTime () {
        Timestamp[] timeArr = {T0, T1, T2, T3};
        return timeArr;
    }

    public Timestamp getT4() {
        return T4;
    }

    public Timestamp getCurTime() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

}
