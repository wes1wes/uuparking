package babi.com.uuparking.init.homePage.infoCenter.person;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.utils.commentUtil.RegularExpressionUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
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
 * Created by b on 2018/3/1.
 */

public class BindPhoneNumber extends AppCompatActivity {

    @BindView(R.id.toolbar_common_center)
    Toolbar toolbar;
    @BindView(R.id.et_my_person_bind_phone_numer)
    EditText etMyPersonBindPhoneNumer;
    @BindView(R.id.btn_my_bind_person_get_code)
    Button btnMyBindPersonGetCode;
    @BindView(R.id.et_my_bind_person_phone_code)
    EditText etMyBindPersonPhoneCode;
    @BindView(R.id.btn_my_bind_person_ok)
    Button btnMyBindPersonOk;
    OkHttpClient client;
    String access_token;
    @BindView(R.id.toolbar_common_center_title)
    TextView toolbarCommonTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_person_bind_phone_layout);
        ButterKnife.bind(this);
        toolbarCommonTitle.setText("绑定手机号");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        client = new OkHttpClient();
        access_token = getIntent().getStringExtra("access_token");
    }

    @OnClick({R.id.btn_my_bind_person_get_code, R.id.btn_my_bind_person_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_bind_person_get_code:
                if (etMyPersonBindPhoneNumer.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputPhoneNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!RegularExpressionUtil.isMobile(etMyPersonBindPhoneNumer.getText().toString())) {
                    Toast.makeText(this, R.string.isPhoneToast, Toast.LENGTH_LONG).show();
                    return;
                }
                btnMyBindPersonGetCode.setClickable(false);
                requestGetSMS();
                break;
            case R.id.btn_my_bind_person_ok:
                if (etMyPersonBindPhoneNumer.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputPhoneNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!RegularExpressionUtil.isMobile(etMyPersonBindPhoneNumer.getText().toString())) {
                    Toast.makeText(this, R.string.isPhoneToast, Toast.LENGTH_LONG).show();
                    return;
                }
                if (etMyBindPersonPhoneCode.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputSMSCode, Toast.LENGTH_LONG).show();
                    return;
                }
                requestBindPhone();
                break;
        }
    }
    /**绑定手机，登录*/
    private void requestBindPhone() {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", access_token);
        map.put("phone", etMyPersonBindPhoneNumer.getText().toString());
        map.put("smsCode", etMyBindPersonPhoneCode.getText().toString());
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        Request request = new Request.Builder()
                .url(UrlAddress.UUPloginUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Logger.e(string);
                saveUserData(string);
            }
        });
    }

    private void saveUserData(String string) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(string);
            if (!jsonObject.getString("code").equals("SUCCESS")) {
                updateToastUI(jsonObject.getString("message"));
                return;
            }
            sharedPreferenceMakeJson(jsonObject.getJSONObject("resultMap"));
            Intent intent = new Intent(getApplicationContext(), UUParkingActivity.class);
            startActivity(intent);
//            Login.login.finish();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateToastUI(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), string, Toast.LENGTH_SHORT).show();
            }
        });
    }
/**发送验证码*/
    private void requestGetSMS() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etMyPersonBindPhoneNumer.getText().toString());
        map.put("type", "2");
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        Request request = new Request.Builder()
                .url(UrlAddress.UUPgetSMSUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                btnMyBindPersonGetCode.setClickable(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                requsetSendMassageResult(string);
            }
        });
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
                                againGetSMSCode();
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    btnMyBindPersonGetCode.setClickable(true);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/**
 * 计时**秒重新获取*/
    private void againGetSMSCode() {
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                btnMyBindPersonGetCode.setClickable(false);
                btnMyBindPersonGetCode.setBackgroundResource(R.drawable.bg_button_dark_gray);
                btnMyBindPersonGetCode.setText(millisUntilFinished / 1000 + "后重新获取");
            }

            @Override
            public void onFinish() {
                btnMyBindPersonGetCode.setClickable(true);
                btnMyBindPersonGetCode.setBackgroundResource(R.drawable.bg_button_blue_radius);
                btnMyBindPersonGetCode.setText("获取验证码");
            }
        }.start();
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

}
