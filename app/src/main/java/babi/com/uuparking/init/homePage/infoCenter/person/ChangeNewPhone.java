package babi.com.uuparking.init.homePage.infoCenter.person;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/9/28.
 */

public class ChangeNewPhone extends AppCompatActivity {

    SharedPreferences user;
    OkHttpClient okHttpClient;
    @BindView(R.id.et_my_person_phone_numer)
    EditText etMyPersonPhoneNumer;
    @BindView(R.id.btn_my_person_get_code)
    Button btnMyPersonGetCode;
    @BindView(R.id.et_my_person_phone_code)
    EditText etMyPersonPhoneCode;
    @BindView(R.id.btn_my_person_ok)
    Button btnMyPersonOk;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_person_phone_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("新手机号");
        user = getSharedPreferences("user_info", 0);
        okHttpClient = new OkHttpClient();
    }

    @OnClick({R.id.btn_my_person_get_code, R.id.btn_my_person_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_person_get_code:
                RequestBody body = new FormBody.Builder()
                        .add("phoneNumber", "" + etMyPersonPhoneNumer.getText().toString())
                        .add("userID", user.getString("userID", ""))
                        .add("sessionID", user.getString("sessionID", ""))
                        .build();
                Request request = new Request.Builder()
                        .url(UrlAddress.UUPgetSMSUrl).post(body).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
                CountDownTimer timer = new CountDownTimer(30 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btnMyPersonGetCode.setClickable(false);
                        btnMyPersonGetCode.setText(millisUntilFinished / 1000 + "s");
                    }

                    @Override
                    public void onFinish() {
                        btnMyPersonGetCode.setClickable(true);
                        btnMyPersonGetCode.setText("获取验证码");
                    }
                }.start();
                break;
            case R.id.btn_my_person_ok:
                RequestBody bodyOk = new FormBody.Builder()
                        .add("phoneNumber", "" + etMyPersonPhoneNumer.getText().toString())
                        .add("userID", user.getString("userID", ""))
                        .add("SmsCode", etMyPersonPhoneCode.getText().toString())
                        .add("mark", "2")
                        .build();
                Request requestOk = new Request.Builder()
                        .url(UrlAddress.updatePhoneNumberUrl).post(bodyOk).build();
                Call callOk = okHttpClient.newCall(requestOk);
                callOk.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());

                            if (jsonObject.getString("code").contains("201")) {
                                user.edit().putString("phoneNumber", etMyPersonPhoneNumer.getText().toString());
                                user.edit().commit();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
        }
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
