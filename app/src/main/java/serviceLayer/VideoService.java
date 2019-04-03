package serviceLayer;

import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class deals with data.
 * It receives String data, caches it and return certain data as called
 */
public class VideoService {
    private ArrayList<Float> Xcoordinate;
    private ArrayList<Float> Ycoordinate;
    private ArrayList<Long> timeline;  //Store the timestamp of each point
    private ArrayList<Integer> seqList;  //Store the line sequence number of each point
    private ArrayList<Integer> flags;  //Indicate whether the point is in a draw line or mark/correct line.
    // 0 means draw, 1 means correct.
    private int sequence;  //Indicate which data in the array should be returned
    private int totalPoints;  //Total points number
    private final String TAG = "VideoService";

    /**
     * Build function: cache data.
     * @param str
     */
    public VideoService(String str) {
        try {
            String[] tempStr = str.split(";");
            totalPoints = tempStr.length;
            initData(tempStr);
        } catch (NullPointerException e) {
            Log.d(TAG, "Error in initializing data!");
            e.printStackTrace();
        }

    }

    /**
     * Data format: x, y, time, sequence number, draw/correct flag
     */
    private void initData(String[] strList) throws NullPointerException{
        Xcoordinate = new ArrayList<>();
        Ycoordinate = new ArrayList<>();
        timeline = new ArrayList<>();
        seqList = new ArrayList<>();
        flags = new ArrayList<>();
        sequence = 0;
        long timeDiff = 0;  //To do: should be T1
        Timestamp lastTimeStamp = null;
        Timestamp thisTimeStamp;
        int len = strList.length;
        Log.d(TAG, "Total points cached:"+Integer.toString(len));
        for (int i = 0; i < len; i++) {
            String[] strList2 = strList[i].split(",");
            for (int j = 0; j < 5; j++) {
                switch (j) {
                    case (0):
                        Xcoordinate.add(Float.parseFloat(strList2[j]));
                        break;
                    case (1):
                        Ycoordinate.add(Float.parseFloat(strList2[j]));
                        break;
                    case (2):
                        if (i!=0) {
                            thisTimeStamp = Timestamp.valueOf(strList2[j]);
                            timeDiff = thisTimeStamp.getTime() - lastTimeStamp.getTime();
                            lastTimeStamp = thisTimeStamp;
                        } else {
                            lastTimeStamp = Timestamp.valueOf(strList2[j]);
                        }
                        timeline.add(timeDiff);
                        break;
                    case (3):
                        seqList.add(Integer.parseInt(strList2[j]));
                        break;
                    case (4):
                        flags.add(Integer.parseInt(strList2[j]));
                        break;
                }
            }
        }
        Log.d(TAG, "Initialise data complete!");
    }

    public ArrayList<Long> getTimeline() {
        return this.timeline;
    }

    public float getNextX() {
        return Xcoordinate.get(sequence);
    }

    public float getNextY() {
        return Ycoordinate.get(sequence);
    }

    //Get the line sequence number
    public int getSeq() {
        return seqList.get(sequence);
    }

    // Get the draw/correct flag
    public int getFlag() {
        return flags.get(sequence);
    }

    // Increase the sequence number (counter) by one
    public void increaseSeq() {
        sequence++;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    //Reset the sequence number (counter) when finish playing the video
    public void resetSequence() {
        sequence = 0;
    }

}
