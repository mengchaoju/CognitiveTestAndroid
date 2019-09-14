package project.cognitivetest.login;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import project.cognitivetest.modules.Security;


/**
 * Created by 50650 on 2019/3/28
 */
public class LocalUserList {
    private  static  final String FILENAME = "userinfo.json";
    private static final String TAG = "localUserList";

//    save the user info list
public static void saveUserList(Context context, ArrayList<Security> users)
        throws Exception {
    /* saving */
    Log.i(TAG, "Saving data");
    Writer writer = null;
    OutputStream out = null;
    JSONArray array = new JSONArray();
    for (Security user : users) {
        array.put(user.toJson());
    }
    try {
        out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE); // cover the records
        writer = new OutputStreamWriter(out);
        Log.i(TAG, "Value of JSON:" + array.toString());
        writer.write(array.toString());
    } finally {
        if (writer != null)
            writer.close();
    }

}

    /* get the user list */
    public static ArrayList<Security> getUserList(Context context) {
        /* load list */
        FileInputStream in = null;
        ArrayList<Security> users = new ArrayList<Security>();
        try {

            in = context.openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            JSONArray jsonArray = new JSONArray();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            Log.i(TAG, jsonString.toString());
            jsonArray = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue(); // convert the String to JSONArray object
            for (int i = 0; i < jsonArray.length(); i++) {
                Security user = new Security(jsonArray.getJSONObject(i));
                users.add(user);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }}
