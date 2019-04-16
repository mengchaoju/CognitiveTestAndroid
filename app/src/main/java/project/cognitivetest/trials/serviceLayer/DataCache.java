package project.cognitivetest.presentationLayer.serviceLayer;

import android.util.Log;

import java.util.ArrayList;

public class DataCache {
    private ArrayList<String> arrayList;
    private final String TAG = "DataCache";

    public DataCache() {
        arrayList = new ArrayList<String>();
    }

    /**
     * Store the pixel data in the format:
     * x, y, time, sequence number, draw/correct flag
     * @param str: data in format.
     */
    public void save (String str) {
        arrayList.add(str);
//        Log.d(TAG, "Data saved:"+str);
    }

    public ArrayList<String> getArr() {
        return this.arrayList;
    }
}
