package babi.com.uuparking.init.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.utils.commentUtil.IdentityCard;
import babi.com.uuparking.init.utils.commentUtil.RegularExpressionUtil;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
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
 * Created by BGD on 2018/1/14.
 * 修改：2018年1月20日18点29分
 */

public class Registered extends AppCompatActivity {
    @BindView(R.id.btn_login_registered_ok)
    Button btnLoginRegisteredOk;
    OkHttpClient client;
    String requstResult;
    Message msg = new Message();
    @BindView(R.id.et_login_verified_name)
    EditText etLoginVerifiedName;
    @BindView(R.id.et_login_verified_id_carde)
    EditText etLoginVerifiedIdCarde;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registered_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("实名认证");
        client = new OkHttpClient();
    }

    @OnClick({R.id.btn_login_registered_ok, R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.btn_login_registered_ok:
                //请求实名认证接口
                if (etLoginVerifiedName.getText().toString().isEmpty()) {
                    Toast.makeText(this, R.string.inputRealNameToast, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etLoginVerifiedIdCarde.getText().toString()==null||etLoginVerifiedIdCarde.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入您的身份证号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegularExpressionUtil.isIDCard(etLoginVerifiedIdCarde.getText().toString())) {
                    Toast.makeText(this, R.string.isIDCardToast, Toast.LENGTH_SHORT).show();
                    return;
                }

                String idnumber = etLoginVerifiedIdCarde.getText().toString();
                IdentityCard identityCard = new IdentityCard(idnumber);
                Map<String, Object> map = new HashMap<>();
                map.put("realName", etLoginVerifiedName.getText().toString());
                map.put("idNumber", idnumber);
                map.put("birthday", identityCard.getBirthString());
                map.put("sex", identityCard.getGenderCode());
                map.put("areaCode", identityCard.getAddressCode());
                registeredBecomeUser(map);
                map.clear();
                break;
        }
    }


    /**
     * 用户注册
     */
    private void registeredBecomeUser(Map<String, Object> map) {
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPidentifyUserUrl).post(body).build();
        netOkhttpRequset(request, 2);
    }

    /**
     * 网络请求结果
     */
    private void netOkhttpRequset(final Request request, final int i) {
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getString("code").equals("SUCCESS")) {
                        OrderDialog.normalDialogPositiveUncancle(Registered.this, jsonObject.getString("message"), new OrderDialog.Callback() {
                            @Override
                            public void click() {
                                finish();
                            }
                        });

                    }else {
                        updateToastUI(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
