package project.cognitivetest.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.cognitivetest.HomeActivity;
import project.cognitivetest.R;
import project.cognitivetest.until.ServerIP;

public class LoginActivity extends Activity implements OnClickListener{

    protected static final String TAG = "LoginActivity";
    private LinearLayout mUserIdLinearLayout;
    private LinearLayout mLoginLinearLayout; // the container for the login content
    private EditText mIdEditText; // login ID edit box
    private EditText mPwdEditText; // login password edit box
    private Button mLoginButton; // login button
    private ImageView mLoginMoreUserView; // pull down the popup button
    private ListView mUserIdListView; // the ListView object displayed in the drop-down pop-up window
    private CheckBox checkRemPWD; //check box for selecting whether to save the password
    private TextView btnReg;
    private SharedPreferences sharedPreferences;
    private String username;
    private String mIdString;
    private String mPwdString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginButton = (Button) findViewById(R.id.login_btnLogin);
        btnReg = (TextView) findViewById(R.id.login_txtReg);
        mIdEditText=(EditText)findViewById(R.id.login_edtId);
        mPwdEditText=(EditText)findViewById(R.id.login_edtPwd);

        mLoginButton.setOnClickListener(this);

        btnReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view){
        String userName = mIdEditText.getText().toString();
        username = userName;
        String passWord = mPwdEditText.getText().toString();

        if(userName.equals("")||passWord.equals("")){
            Toast.makeText(LoginActivity.this,"the username and password should not be empty",Toast.LENGTH_SHORT).show();
//            showWarnSweetDialog("the username and password should not be empty");
            return;
        }

        String url =ServerIP.LOGINURL;
        getCheckFromServer(url,userName,passWord);
    }

    /**
     * Send the user name and password to the server for comparison.
     * If it is successful, it will jump to the main interface of app.
     * If it is wrong, it will refresh the UI to prompt the wrong login information
     * @param url Server Address
     * @param userName Username
     * @param passWord Password
     */
    private void getCheckFromServer(String url,final String userName,String passWord)
    {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("password", passWord);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
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
                        Toast.makeText(LoginActivity.this,"Server failure.",Toast.LENGTH_SHORT).show();
//                        showWarnSweetDialog("Server does not work now.");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String res = response.body().string();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0"))
                        {
                            Toast.makeText(LoginActivity.this,"The username is not registered.",Toast.LENGTH_SHORT).show();
//                            showWarnSweetDialog("The username is not registered");
                        }
                        else if(res.equals("1"))
                        {
                            Toast.makeText(LoginActivity.this,"Incorrect username or password.",Toast.LENGTH_SHORT).show();
//                            showWarnSweetDialog("password is not right");
                        }
                        else  //success
                        {
                            Toast.makeText(LoginActivity.this,res,Toast.LENGTH_SHORT).show();
                            setLoggingStatus(LoginActivity.this,true);
//                            showSuccessSweetDialog(res);
                            sharedPreferences = getSharedPreferences("UserIDAndPassword", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", userName);
                            editor.apply();
                            goToHomePage();
                        }

                    }
                });
            }
        });
    }

    /**
     * When successfully log in, go to the home page.
     * And pass the username to the next activity.
     */
    private void goToHomePage() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("staffUsername", username);  // This is the username of staff
        startActivity(intent);
    }

//    private void showWarnSweetDialog(String info)
//    {
//        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText(info);
//        pDialog.setCancelable(true);
//        pDialog.show();
//    }

//    private void showSuccessSweetDialog(String info)
//    {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText(info);
//        pDialog.setCancelable(true);
//        pDialog.show();
//        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
//        {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog)
//            {
//                pDialog.dismiss();
//            }
//        });
//    }
    public static void  setLoggingStatus(Context context, boolean status)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userLogStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LogStatus", status);
        editor.apply();
    }

    private void saveLogStatus()
    {
        SharedPreferences sps = getSharedPreferences("userLogStatus",MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putBoolean("LogStatus", true);
        editor.apply();
    }

}
