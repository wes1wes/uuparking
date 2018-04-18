package babi.com.uuparking.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.homePage.infoCenter.history.HistoryDetails;
import babi.com.uuparking.init.homePage.infoCenter.person.BindPhoneNumber;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    SharedPreferences WXMoney;
    OkHttpClient okHttpClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        WXMoney = this.getSharedPreferences("wxRecharge_money", 0);
        okHttpClient = new OkHttpClient();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WXAppID.wxAppID, false);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("微信支付进入", "onReq: ");
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.e("微信支付进入", "onResp: ");
        int code = resp.errCode;
        switch (resp.getType()) {
            case RETURN_MSG_TYPE_LOGIN:
                switch (code) {
                    case 0:
                        String loginCode = ((SendAuth.Resp) resp).code;
                        internetWXLogin(loginCode);
                        break;
                    case -1:
                        Toast.makeText(this, "登录授权失败", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
        //微信支付后续操作，失败，成功，取消
        finish();
    }

    private void internetWXLogin(final String loginCode) {
        Map<String, Object> mappay = new HashMap<>();
        mappay.put("code", loginCode);
        RequestBody bodyAli = RequestBody.create(UrlAddress.JSON, new Gson().toJson(mappay));
        mappay.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPWXloginUrl).post(bodyAli).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Logger.e(string);
                dealLoginData(string, loginCode);
            }
        });
    }

    private void dealLoginData(String string, String loginCode) {
        JSONObject jsonObject;
        String code;
        try {
            jsonObject = new JSONObject(string);
            code = jsonObject.getString("code");
            switch (code) {
                case "L002":
                    updateToastUI("登录失败");
                    break;
                case "SUCCESS":
                    sharedPreferenceMakeJson(jsonObject.getJSONObject("resultMap"));
                    Intent intent = new Intent(this, UUParkingActivity.class);
                    startActivity(intent);
                    break;
                case "L001":
                    Intent intentlogin = new Intent(this, BindPhoneNumber.class);
                    intentlogin.putExtra("access_token", jsonObject.getJSONObject("resultMap").getString("access_token"));
                    startActivity(intentlogin);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取到的保存用户信息保存至本地
     */
    public void sharedPreferenceMakeJson(JSONObject json) {
        SharedPreferences.Editor user1 = getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
        user1.clear();
        try {
            user1.putString("creditScore", json.getString("creditScore"));
            user1.putString("phone", json.getString("phone"));
            user1.putString("hasIdentified", json.getString("hasIdentified"));
            user1.putString("headIconUrl", json.getString("headIconUrl"));
            user1.putString("nickName", json.getString("nickName"));
            user1.putString("userId", json.getString("userId"));
            user1.putString("accountId", json.getString("accountId"));
            user1.putBoolean("identifyUser", json.getBoolean("identifyUser"));
            user1.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}