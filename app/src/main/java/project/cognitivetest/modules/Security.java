package project.cognitivetest.modules;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import serviceLayer.util.SHA256Encryption;

/**
 * Created by 50650 on 2019/4/2
 */
public class Security {
    private String username;
    private String password;
    private static final String Json_ID = "User_ID"; /*User ID*/
    private static final String Json_pwd = "pwd"; /*password*/

    private static final String TAG = "USER";

    public Security(){

    }

    public Security(String username, String pwd){
        this.username=username;
        this.password=pwd;
    }

    public Security(JSONObject json) throws JSONException {
        String mUsername = json.getString(Json_ID);
        String mPWD = json.getString(Json_pwd);

        this.username=mUsername;
        this.password=mPWD;
    }

    public JSONObject toJson() throws Exception {

        String encryPwd = SHA256Encryption.Encrypt(this.password,"");
        String encryUserID = SHA256Encryption.Encrypt(this.username,"");

        Log.i(TAG,"The user info after encryption:"
                +"Username "+username + "Password"
                + encryPwd);

        JSONObject json = new JSONObject();
        try{
            json.put(Json_ID, encryUserID);
            json.put(Json_pwd,encryPwd);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
