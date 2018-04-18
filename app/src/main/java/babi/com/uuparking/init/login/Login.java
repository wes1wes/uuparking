package babi.com.uuparking.init.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.dialog.WaitingDialog;
import babi.com.uuparking.init.utils.commentUtil.RegularExpressionUtil;
import babi.com.uuparking.init.utils.commentUtil.RequsetPermissionUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.wxapi.WXAppID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/7/14.
 */

public class Login extends AppCompatActivity {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_code)
    EditText etLoginCode;
    @BindView(R.id.btn_login_code)
    Button btnLoginCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.image_login_wx)
    ImageView imageLoginWx;
    @BindView(R.id.image_login_wb)
    ImageView imageLoginWb;
    @BindView(R.id.image_login_qq)
    ImageView imageLoginqq;
    @BindView(R.id.ll_login_user_book)
    LinearLayout llLoginUserBook;
    private Handler handler = new Handler();
    OkHttpClient okHttpClient;
    public static Login login;
    IWXAPI mWxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        login = this;
        okHttpClient = new OkHttpClient();
        registToWX();
        //请求定位、相机权限
        new RequsetPermissionUtil().requestPermission(this, this);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void registToWX() {
        mWxApi = WXAPIFactory.createWXAPI(this, WXAppID.wxAppID, true);
        // 将该app注册到微信
        mWxApi.registerApp(WXAppID.wxAppID);
    }


    @OnClick({R.id.btn_login_code, R.id.btn_login, R.id.image_login_wx, R.id.image_login_wb, R.id.image_login_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_code:
                if (etLoginPhone.getText().toString() == null || etLoginPhone.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputPhoneNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegularExpressionUtil.isMobile(etLoginPhone.getText().toString())) {
                    Toast.makeText(this, R.string.isPhoneToast, Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody body = RequestBody.create(UrlAddress.JSON, "{\"phone\":\"" + etLoginPhone.getText().toString() + "\",\"type\":\"2\"}");
                Request request = new Request.Builder()
                        .url(UrlAddress.UUPgetSMSUrl)
                        .post(body)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Logger.d(getString(R.string.netNoConnet));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String s = response.body().string();
                        Log.e("发短信", s);
                        requsetSendMassageResult(s);
                    }
                });
                break;
            case R.id.btn_login:
                if (etLoginPhone.getText().toString() == null || etLoginPhone.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputPhoneNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegularExpressionUtil.isMobile(etLoginPhone.getText().toString())) {
                    Toast.makeText(this, R.string.isPhoneToast, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etLoginCode.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputSMSCodeToast, Toast.LENGTH_SHORT).show();
                    return;
                }
                requsetLogin();
                WaitingDialog.showWaitingDialog(Login.this);
                break;
            case R.id.image_login_wx:
                wxLogin();
                break;
            case R.id.image_login_wb:
                // TODO: 2018/1/13 微博登录
                ToastUtil.ToastUtilmsg(this, "亲，小由正在加紧开发哦！");
                break;
            case R.id.image_login_qq:
                // TODO: 2018/1/13 微博登录
                ToastUtil.ToastUtilmsg(this, "亲，小由正在加紧开发哦！");
                break;
        }
    }

    /*微信登录注册*/
    public void wxLogin() {
        if (!mWxApi.isWXAppInstalled()) {
            Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "uuparking_wx_login";
        mWxApi.sendReq(req);
    }

    /**
     * 处理手机验证码结果
     */
    private void requsetSendMassageResult(String string) {
        JSONObject js = null;
        try {
            js = new JSONObject(string);
            Logger.e(string);
            final JSONObject finalJs = js;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        switch (finalJs.get("code").toString()) {
                            case "M001":
                                Toast.makeText(getBaseContext(), "短信发送太过频繁！请稍后重试", Toast.LENGTH_SHORT).show();
                                break;
                            case "ERROR":
                                Toast.makeText(getBaseContext(), "验证码发送失败！请稍后重试", Toast.LENGTH_SHORT).show();
                                break;
                            case "SUCCESS":
                                Toast.makeText(getBaseContext(), "验证码已发送，请注意查收", Toast.LENGTH_SHORT).show();
                                CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        btnLoginCode.setClickable(false);
                                        btnLoginCode.setText(millisUntilFinished / 1000 + "s");
                                    }

                                    @Override
                                    public void onFinish() {
                                        btnLoginCode.setClickable(true);
                                        btnLoginCode.setText("获取验证码");
                                    }
                                }.start();
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*请求登陆*/
    private void requsetLogin() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etLoginPhone.getText().toString());
        map.put("smsCode", etLoginCode.getText().toString());
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder()
                .url(UrlAddress.UUPloginUrl)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                WaitingDialog.cancelWaitingDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = response.body().string();
                Log.e("登陆", s);
                requsetLoginResult(s);
                WaitingDialog.cancelWaitingDialog();
            }
        });

    }

    private void requsetLoginResult(String string) {

        try {
            final JSONObject jsonObject = new JSONObject(string);
            switch (jsonObject.getString("code")) {

                case "SUCCESS":
                    sharedPreferenceMakeJson(jsonObject.getJSONObject("resultMap"));
                    Intent intent = new Intent(this, UUParkingActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    updateToastUI(jsonObject.get("message").toString());
                    break;
            }
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
//            JPushInterface.setAlias(getApplicationContext(), 0, json.getString("userID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_login_user_book)
    public void onViewClicked() {
        // TODO: 2018/3/5 用户协议
    }
}
