package project.cognitivetest.login;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.cognitivetest.R;
import project.cognitivetest.until.ServerIP;

/**
 * Created by 50650 on 2019/4/2
 */
public class RegisterActivity extends Activity {

    private static final String TAG = "SignUpActivity";



    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_firstName)
    EditText inputFirstName;
    @BindView(R.id.input_FamilyName)
    EditText inputFamilyName;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.link_login)
    TextView linkLogin;
    @BindView(R.id.input_conPassword)
    EditText inputConPassword;
    @BindView(R.id.input_DateofBirth)
    EditText inputDoB;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg);
        ButterKnife.bind(this);

        initDatePicker();

        btnSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    signUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        linkLogin.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                            finish();
                                         }
                                     }
        );
    }

    private void initDatePicker(){
        inputDoB.setInputType(InputType.TYPE_NULL);
        inputDoB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    showDatePickerDialog();
                }
            }
        });

        inputDoB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                inputDoB.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }


    private void signUp() throws Exception {
        Log.d(TAG, "SignUp");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String userName = inputName.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String famliyName = inputFamilyName.getText().toString();
        String pwd = inputConPassword.getText().toString();
        String dob = inputDoB.getText().toString();

//        Security mSecurity = new Security(userName, pwd);
//        User mUser = new User(userName, firstName, famliyName, pwd);


        //unfinished waiting for backend, it should send request to varify whether the account exist and receive the reply whethe the
        //database have successfully stored the info
//        mUser.toJson();
//        mSecurity.toJson();
        String url = ServerIP.SIGNUPURL;
        registeNameWordToServer(url,userName,firstName,famliyName
        ,dob,pwd);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSignupSuccess();
                //onSignupFailed();
            }
        },3000);

    }

    private void registeNameWordToServer(String url,
                                         final String userName,
                                         String firstName,
                                         String familyName,
                                         String dateOfBirth,
                                         String passWord)
    {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("password", passWord);
        formBuilder.add("firstname",firstName);
        formBuilder.add("familyname",familyName);
        formBuilder.add("dateofbirth",dateOfBirth);
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
                        Toast.makeText(RegisterActivity.this,"Server does not work.",Toast.LENGTH_SHORT).show();
//                       showWarnSweetDialog("server wrong");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException
            {
                final String res = response.body().string();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0"))
                        {
                            Toast.makeText(RegisterActivity.this,"The username has been registered.",Toast.LENGTH_SHORT).show();
//                            showWarnSweetDialog("The username has been registered.");
                        }
                        else
                        {
                            finish();
//                            sharedPreferences = getSharedPreferences("UserIDAndPassword", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("username", userName);
//                            editor.apply();
                        }

                    }
                });
            }
        });

    }

    private void onSignupSuccess(){

        btnSignup.setEnabled(true);
        setResult(RESULT_OK,null);
        onDestroy();

    }

    private void onSignUpFailed(){

        Toast.makeText(getBaseContext(),"Sign Up Failed",Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String name = inputName.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String familyName = inputFamilyName.getText().toString();
        String password = inputPassword.getText().toString();
        String confPassword = inputConPassword.getText().toString();

        //unfinished it should sent a request to database to verify whether the username exists and then get and show the result immedimately
        if (name.isEmpty() || name.length() < 3) {
            inputName.setError("at least 3 characters");
            valid = false;
        } else {
            inputName.setError(null);
        }

        if (familyName.isEmpty()) {
            inputFamilyName.setError("Please enter your family name.");
            valid = false;
        } else {
            inputFamilyName.setError(null);
        }

        if (firstName.isEmpty()) {
            inputFirstName.setError("Please enter your first name.");
            valid = false;
        } else {
            inputFamilyName.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            inputPassword.setError("between 4 and 20 alphanumeric characters.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (!password.equals(confPassword)) {
            inputPassword.setError("The passwords you entered do not match.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }



        return valid;
    }
}
