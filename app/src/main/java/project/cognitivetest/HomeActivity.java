package project.cognitivetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1, btn2;
    private String staffUsername;
    private String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {
        btn1 = findViewById(R.id.homepage_button1);
        btn2 = findViewById(R.id.homepage_button2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        // Get staff username from the last activity.
        Intent intent = getIntent();
        staffUsername = intent.getStringExtra("staffUsername");
        Log.d(TAG, "homeActivity view initialized.");
        Log.d(TAG, "staff username: "+staffUsername);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homepage_button1:
                selectExisting();
                break;
            case R.id.homepage_button2:
                createNew();
                break;
        }
    }

    /**
     * Dealing with clicking on "select existing participant button."
     */
    private void selectExisting() {
        //TODO
        Log.d(TAG, "click on select existing participant button");
    }

    /**
     * Dealing with clicking on "create a new participant button."
     */
    private void createNew() {
        //TODO
        Log.d(TAG, "click on create a new participant button.");
    }
}