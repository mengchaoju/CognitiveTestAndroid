package serviceLayer;

import java.util.ArrayList;

public class DataCache {
    private ArrayList<String> arrayList;

    public DataCache() {
        arrayList = new ArrayList<String>();
    }

    public void store (String str) {
        arrayList.add(str);
    }

    public ArrayList getArr() {
        return this.arrayList;
    }
}
