package babi.com.uuparking.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2018/2/4.
 */

public class OrderTimerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        CountDownTimer timer = new CountDownTimer(15*60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                requestCancleParking();
            }
        }.start();

        return null;
    }
    /**
     * 取消订单请求
     */
    private void requestCancleParking() {
        OkHttpClient client=new OkHttpClient();
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", SpUserUtils.getString(getApplicationContext(),"orderId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPcancelOrderByHandUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(string);
                    if (jsonObject.getString("code").equals("1")) {

                    } else {
                        Log.e("222", "订单取消失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("222", "" + string);
            }
        });
    }
}
