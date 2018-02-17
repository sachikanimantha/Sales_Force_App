package com.example.bellvantage.scadd.Services;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Sachika on 20/07/2017.
 */

public class ServiceHandler {

    Context context;
    JSONObject params;

    public ServiceHandler(Context context) {
        this.context = context;
    }

    public void sendJsonService(JSONObject params,String URL){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, params.toString());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error======");
                Log.e("response error", call.request().body().toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                System.out.println("Success===== "+ response.toString());
                Log.e("response=========", response.body().string());
            }
        });
    }
}
