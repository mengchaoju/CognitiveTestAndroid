package serviceLayer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UploadDataService {

    private int ifSuccess = 0;  // 0 means fail, 1 means success.
    private String url;
    private String userName;
    private String staffID = "";
    private String data1, data2;

    /**
     * Build function
     * @param url the url to be apply POST function on
     * @param userName the userName of this data
     * @param data1,data2 the data to be sent in String format
     *                    data1 is pixels data, data2 is time data
     */
    public UploadDataService(String url, String userName, String data1, String data2) {
        this.url = url;
        this.userName = userName;
        this.data1 = data1;
        this.data2 = data2;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public int getIfSuccess() {
        return this.ifSuccess;
    }

    public void send() {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        if (!staffID.equals("")) {
            formBuilder.add("staffID", staffID);
        }
        formBuilder.add("pixelData", data1);
        formBuilder.add("timeData", data2);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                ifSuccess = 0;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String res = response.body().string();
                ifSuccess = Integer.parseInt(res);
            }
        });
    }
}
