package project.cognitivetest.history;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.cognitivetest.R;
import project.cognitivetest.adapter.HistoryAdaptor;
import project.cognitivetest.modules.ParticipantBean;

/**
 * Created by 50650 on 2019/4/15
 */
public class Activity_history extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private Context context=Activity_history.this;
    private static final int Activity_Num = 1;

    RecyclerView mRecycleView;
    HistoryAdaptor adaptor;
    ImageButton searchBTN;
    EditText editTextString;
    private TextView headTitle;

    private String mJson;
    private ArrayList<ParticipantBean.participant> mResults;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d("History:","Created Search Activity");

        hideSoftKeyboard();

        searchBTN= (ImageButton) findViewById(R.id.search_btn);
        editTextString=(EditText) findViewById(R.id.search_field);
        headTitle = (TextView) findViewById(R.id.heading_label);
        mRecycleView = (RecyclerView)findViewById(R.id.result_list);

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headTitle.setText("Searching...");
                String target = editTextString.getText().toString().trim();
                searchForMatch(target);
            }
        });
    }

    private void processData(){
        Gson gson = new Gson();
        ParticipantBean participantJson = gson.fromJson(mJson,ParticipantBean.class);
        Log.d(TAG,"the data extracted by Gson:" + participantJson);
        mResults = participantJson.result;
        Log.d(TAG,"The result is:" + mResults);
        Log.d(TAG,"The name of first participant is: " + mResults.get(0).getFirstName()+mResults.get(0).getFamilyName());

    }


    private void searchForMatch(final String keyword){
        Log.d(TAG, "searchForMatch: searching for a match:" + keyword);
        if (keyword.length()==0){
            Log.d(TAG,"null input");
            Toast.makeText(context, "Please enter your searching keyword",Toast.LENGTH_SHORT);

        }else {
            Log.d(TAG,"Searching for the text");

        }
    }

    private void getListFromServer(String url,final String keyword)
    {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("keyword", keyword);
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
                        Toast.makeText(Activity_history.this,"Server does not work now.",Toast.LENGTH_SHORT).show();
                        //                        showWarnSweetDialog("Server does not work now.");
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
                        if (res.equals("no content"))
                        {
                            Toast.makeText(Activity_history.this,"Nothing is found in the database",Toast.LENGTH_SHORT).show();
                            //                            showWarnSweetDialog("The username is not registered");
                        }
                        else//success
                        {
                            Gson gson = new Gson();
                            ParticipantBean ptJson = gson.fromJson(res,ParticipantBean.class);
                            mResults=ptJson.result;
                            Log.d(TAG,"the result is :"+mResults);
                            Log.d(TAG,"the name of the first participant:"+mResults.get(0).getParticipantID());
                            initAdapter();
                            Toast.makeText(Activity_history.this,"Found Targets",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }


    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initAdapter(){
        adaptor = new HistoryAdaptor(Activity_history.this,mResults);
        mRecycleView.setAdapter(adaptor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,false);
        mRecycleView.setLayoutManager(layoutManager);
    }




}
