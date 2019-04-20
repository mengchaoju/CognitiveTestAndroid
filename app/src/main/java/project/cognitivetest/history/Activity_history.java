package project.cognitivetest.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.cognitivetest.R;
import project.cognitivetest.adapter.HistoryAdaptor;
import project.cognitivetest.modules.Participant;
import serviceLayer.util.ServerIP;

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
    Spinner mSpinner;

    private String mJson;
    private ArrayList<Participant> mResults = new ArrayList<Participant>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d("History:","Created Search Activity");

        hideSoftKeyboard();

        searchBTN= (ImageButton) findViewById(R.id.search_btn);
        editTextString=(EditText) findViewById(R.id.search_field);
        headTitle = (TextView) findViewById(R.id.heading_label);
        mRecycleView = (RecyclerView)findViewById(R.id.result_list);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        Intent intent = getIntent();

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        initSpinner();

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headTitle.setText("Searching...");
                String target = editTextString.getText().toString().trim();
                String choice = mSpinner.getSelectedItem().toString();
                switch (choice){
                    case "All":
                        searchForMatchForAll(target);
                        break;
                    case "FirstName":
                        searchForMatchForFirst(target);
                        break;
                    case "FamilyName":
                        searchForMatchForFamily(target);
                        break;
                    case "Gender":
                        searchForMatchForGender(target);
                        break;
                    case "DateOfBirth":
                        searchForMatchForDoB(target);
                        break;
                    default:
                        searchForMatchForAll(target);
                        break;
                }
                Toast.makeText(Activity_history.this, mSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT);
            }
        });
    }

    private void initSpinner(){
        String[] Choices = new String[]{"all","ID","FirstName",
                "FamilyName","DoB"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_history,Choices);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String result = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void searchForMatchForAll(final String keyword){
        Log.d(TAG, "searchForMatch: searching for a match:" + keyword);
        if (keyword.length()==0){
            Log.d(TAG,"null input");
            Toast.makeText(context, "Please enter your searching keyword",Toast.LENGTH_SHORT);

        }else {
            Log.d(TAG,"Searching for the All features");
            String url = ServerIP.QUERYPARTICIPANTALL;
            getListFromServer(url,keyword);

        }
    }

    private void searchForMatchForFirst(final String keyword){
        Log.d(TAG, "searchForMatch: searching for a match:" + keyword);
        if (keyword.length()==0){
            Log.d(TAG,"null input");
            Toast.makeText(context, "Please enter your searching keyword",Toast.LENGTH_SHORT);

        }else {
            Log.d(TAG,"Searching for the FirstName");
            String url = ServerIP.QUERYPARTICIPANTFIRST;
            getListFromServer(url,keyword);

        }
    }

    private void searchForMatchForFamily(final String keyword){
        Log.d(TAG, "searchForMatch: searching for a match:" + keyword);
        if (keyword.length()==0){
            Log.d(TAG,"null input");
            Toast.makeText(context, "Please enter your searching keyword",Toast.LENGTH_SHORT);

        }else {
            Log.d(TAG,"Searching for the FamilyName");
            String url = ServerIP.QUERYPARTICIPANTFAMILY;
            getListFromServer(url,keyword);

        }
    }

    private void searchForMatchForGender(final String keyword){
        Log.d(TAG, "searchForMatch: searching for a match:" + keyword);
        if (keyword.length()==0){
            Log.d(TAG,"null input");
            Toast.makeText(context, "Please enter your searching keyword",Toast.LENGTH_SHORT);

        }else {
            Log.d(TAG,"Searching for Gender");
            String url = ServerIP.QUERYPARTICIPANTGENDER;
            getListFromServer(url,keyword);

        }
    }

    private void searchForMatchForDoB(final String keyword){
        Log.d(TAG, "searchForMatch: searching for a match:" + keyword);
        if (keyword.length()==0){
            Log.d(TAG,"null input");
            Toast.makeText(context, "Please enter your searching keyword",Toast.LENGTH_SHORT);

        }else {
            Log.d(TAG,"Searching for the DoB");
            String url = ServerIP.QUERYPARTICIPANTDOB;
            getListFromServer(url,keyword);

        }
    }

    private void getListFromServer(String url,final String keyword)
    {
        mResults.clear();
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("keyword", keyword);
        final Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
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
                        if (res.equals("no record"))
                        {
                            headTitle.setText("No record");
                            Toast.makeText(Activity_history.this,"Nothing is found in the database",Toast.LENGTH_SHORT).show();
                        }
                        else//success
                        {
                            try {
                                JSONArray jsonArray = new JSONArray(res);
                                for (int i =0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String participantID = jsonObject.getString("participantid");
                                    String firstName = jsonObject.getString("firstname");
                                    String familyName = jsonObject.getString("familyname");
                                    String gender = jsonObject.getString("gender");
                                    String dateOfBirth = jsonObject.getString("dateofbirth");

                                    Participant participant = new Participant(participantID,firstName
                                    ,familyName,gender,dateOfBirth);

                                    mResults.add(participant);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            initAdapter();
                            Toast.makeText(Activity_history.this,"Found Targets",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    private String getStrFromAsset(String name){
        String strData = null;
        try{
            InputStream inputStream = getAssets().open(name);
            byte buf[]=new byte[1024];
            inputStream.read(buf);
            strData=new String(buf);
            strData=strData.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("strData = " + strData);
        return strData;
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

        adaptor.setOnItemClickListener(new HistoryAdaptor.OnItemClickListener() {
            @Override

            public void onItemClick(View view, int position) {

                String participantID = ((TextView) mRecycleView.findViewHolderForAdapterPosition(position).
                        itemView.findViewById(R.id.pt_id_text)).getText().toString();
                Intent intent = new Intent(Activity_history.this, project.cognitivetest.video_image.VideoView.class);
                Bundle bundle = new Bundle();
                bundle.putString("participantID",participantID);
                intent.putExtra("data",bundle);
                startActivity(intent);
                Toast.makeText(Activity_history.this,participantID,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                String participantID = ((TextView) mRecycleView.findViewHolderForAdapterPosition(position).
                        itemView.findViewById(R.id.pt_id_text)).getText().toString();
                Intent intent = new Intent(Activity_history.this, project.cognitivetest.video_image.VideoView.class);
                Bundle bundle = new Bundle();
                bundle.putString("participantID",participantID);
                intent.putExtra("data",bundle);
                startActivity(intent);
                Toast.makeText(Activity_history.this,participantID,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
