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
    private String data;

    /**
     * Build function
     * @param url the url to be apply POST function on
     * @param userName the userName of this data
     * @param data the data to be sent in String format
     */
    public UploadDataService(String url, String userName, String data) {
        this.url = url;
        this.userName = userName;
        this.data = data;
    }

    public int getIfSuccess() {
        return this.ifSuccess;
    }

    public void send() {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("data", data);
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
