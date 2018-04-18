package babi.com.uuparking.init.utils.commentUtil;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by b on 2018/3/16.
 */

public class OkHttpComment {
    public OkHttpComment() {
    }

    public JSONObject OkhttpRequst(Map<String,Object> map, String url){
        OkHttpClient client=new OkHttpClient();
        JSONObject jsonObject;
        RequestBody body=RequestBody.create(UrlAddress.JSON,new Gson().toJson(map));
        Request request=new Request.Builder().url(url).post(body).build();
        okhttp3.Call call=client.newCall(request);
        try {
            String s=call.execute().body().string();
            jsonObject=new JSONObject(s);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
