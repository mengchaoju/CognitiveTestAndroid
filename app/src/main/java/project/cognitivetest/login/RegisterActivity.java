package project.cognitivetest.login;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.cognitivetest.R;
import project.cognitivetest.modules.Security;
import project.cognitivetest.modules.User;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg);
        ButterKnife.bind(this);

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
                                            onDestroy();
                                         }
                                     }
        );
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


        Security mSecurity = new Security(userName, pwd);
        User mUser = new User(userName, firstName, famliyName, pwd);

        //unfinished waiting for backend, it should send request to varify whether the account exist and receive the reply whethe the
        //database have successfully stored the info
        mUser.toJson();
        mSecurity.toJson();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSignupSuccess();
                //onSignupFailed();
            }
        },3000);

    }

    private void onSignupSuccess(){

        btnSignup.setEnabled(true);
        setResult(RESULT_OK,null);
        onDestroy();

    }

    private void onSignUpFailed(){

        Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_LONG).show();
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
