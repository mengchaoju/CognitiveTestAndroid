package project.cognitivetest.modules;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import project.cognitivetest.until.SHA256Encryption;
/**
 * Created by 50650 on 2019/3/27
 */
public class User {

    private String username;
    private String firstname;
    private String familyname;
    private String dateOfBirth;

    private static final String Json_ID = "User_ID"; /*User ID*/
    private static final String Json_FirstName = "FirstName"; /*firstname*/
    private static final String Json_FamilyName = "FamilyName"; /*familyname*/
    private static final String Json_DoB = "DoB"; /*date of birth*/

    private static final String TAG = "USER";


    public User(){

    }

    public User (String username, String firstname,String familyname,
                 String dataOfBirth){

        this.username = username;
        this.firstname = firstname;
        this.familyname = familyname;
        this.dateOfBirth = dataOfBirth;

    }

    public User (JSONObject jsonObject) throws JSONException {
        if (jsonObject.has(Json_ID)){
            String id = jsonObject.getString(Json_ID);
            String Jfirstname = jsonObject.getString(Json_FirstName);
            String Jfamilyname = jsonObject.getString(Json_FamilyName);
            String JDoB = jsonObject.getString(Json_DoB);

            username = id;
            firstname = Jfirstname;
            familyname = Jfamilyname;
            dateOfBirth = JDoB;

        }
    }

    public JSONObject toJson() throws Exception {

        Log.i(TAG,"The user info after encryption:"
        +"Username "+username + "Firstname: "
        +firstname+"FamilyName: "+familyname
        +"DoB: "+dateOfBirth);

        JSONObject json = new JSONObject();
        try{
            json.put(Json_ID, username);
            json.put(Json_FirstName,firstname);
            json.put(Json_FamilyName,familyname);
            json.put(Json_DoB,dateOfBirth);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public String getUsername(){
        return this.username;
    }

    public String getFirstname(){
        return this.firstname;
    }

    public String getFamilyname(){
        return this.familyname;
    }

    public String getDateOfBirth(){
        return this.dateOfBirth;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public void setFamilyname(String familyname){
        this.familyname = familyname;
    }

}
