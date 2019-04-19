package project.cognitivetest.newTest;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import project.cognitivetest.trials.IntroductionView;
import serviceLayer.util.ServerIP;

/**
 * Created by 50650 on 2019/4/16
 */
public class Activity_PtReg extends Activity {
    private String participantID;
    private static final String TAG = "ParticipantRegisterActivity";


    @BindView(R.id.pt_reg_input_id)
    EditText inputID;
    @BindView(R.id.pt_reg_input_firstName)
    EditText inputFirstName;
    @BindView(R.id.pt_reg_input_FamilyName)
    EditText inputFamilyName;
    @BindView(R.id.btn_enter_test)
    AppCompatButton btnStartTest;
    @BindView(R.id.pt_reg_gender)
    EditText inputGender;
    @BindView(R.id.pt_reg_input_DateofBirth)
    EditText inputDoB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_partcipant_reg);
        ButterKnife.bind(this);

        initDatePicker();

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    signUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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

        inputDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(Activity_PtReg.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                inputDoB.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void signUp() throws Exception {
        Log.d(TAG,"Participant sign up");

        if (!validate()){
            onSignUpFailed();
            return;
        }

        btnStartTest.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Activity_PtReg.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Participant...");
        progressDialog.show();

        String participantsID = inputID.getText().toString();
        this.participantID = participantsID;  // Update global variable
        String firstName = inputFirstName.getText().toString();
        String familyName = inputFamilyName.getText().toString();
        String gender = inputGender.getText().toString();
        String DOB = inputDoB.getText().toString();

        String url = ServerIP.PARTICIPANTSIGNUP;
        registerParticipantToServer(url,participantsID,firstName,
                familyName,DOB,gender);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSignupSuccess();
                //onSignupFailed();
            }
        },3000);

    }

    private void onSignupSuccess(){
        btnStartTest.setEnabled(true);
        setResult(RESULT_OK,null);
        onDestroy();

    }


    private void onSignUpFailed(){

        Toast.makeText(getBaseContext(),"Sign Up Failed",Toast.LENGTH_LONG).show();
        btnStartTest.setEnabled(true);

    }

    private void registerParticipantToServer(String url,
                                         final String participantsID,
                                         String firstName,
                                         String familyName,
                                         String dateOfBirth,
                                         String gender)
    {
        OkHttpClient client1 = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("participantid", participantsID);
        formBuilder.add("firstname",firstName);
        formBuilder.add("familyname",familyName);
        formBuilder.add("gender", gender);
        formBuilder.add("dateofbirth",dateOfBirth);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call1 = client1.newCall(request);
        call1.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(Activity_PtReg.this,"Server does not work.",Toast.LENGTH_SHORT).show();
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
                        if (res.equals("participant ID has been registered"))
                        {
                            Toast.makeText(Activity_PtReg.this,"The username has been registered.",Toast.LENGTH_SHORT).show();
                            //                            showWarnSweetDialog("The username has been registered.");
                        }
                        else
                        {
                            Toast.makeText(Activity_PtReg.this,res,Toast.LENGTH_LONG).show();
                            gotoTrials();
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

    public boolean validate() {
        boolean valid = true;

        String name = inputID.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String familyName = inputFamilyName.getText().toString();
        String gender = inputGender.getText().toString();
        String DOB = inputDoB.getText().toString();

        //unfinished it should sent a request to database to verify whether the username exists and then get and show the result immedimately
        if (name.isEmpty() || name.length() < 3) {
            inputID.setError("at least 3 characters");
            valid = false;
        } else {
            inputID.setError(null);
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


        if (gender.isEmpty()) {
            inputGender.setError("Gender should not be empty");
            valid = false;
        } else {
            inputGender.setError(null);
        }

        if (DOB.isEmpty()) {
            inputDoB.setError("You must enter your birthday");
            valid = false;
        } else {
            inputDoB.setError(null);
        }
        return valid;
    }

    private void gotoTrials() {
        Log.d(TAG, "Navigate to trials, with participantID:"+participantID);
        Intent intent = new Intent(Activity_PtReg.this, IntroductionView.class);
        intent.putExtra("participantID", participantID);
        startActivity(intent);
    }
}
