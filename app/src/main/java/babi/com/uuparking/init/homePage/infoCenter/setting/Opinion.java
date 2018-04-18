package babi.com.uuparking.init.homePage.infoCenter.setting;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import babi.com.uuparking.R;
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
 * Created by b on 2017/9/15.
 */

public class Opinion extends AppCompatActivity {

    OkHttpClient okHttpClient;
    SharedPreferences user;
    @BindView(R.id.tv_my_setting_opinion)
    TextView tvMySettingOpinion;
    @BindView(R.id.et_my_setting_opinion)
    EditText etMySettingOpinion;
    @BindView(R.id.btn_my_setting_opinion)
    Button btnMySettingOpinion;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_setting_layout_opinion);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("意见反馈");
        user = getSharedPreferences("user_info", 0);
        okHttpClient = new OkHttpClient();
    }

    @OnClick({R.id.btn_my_setting_opinion, R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_setting_opinion:
                if (!etMySettingOpinion.getText().toString().isEmpty()) {
                    RequestBody body = RequestBody.create(UrlAddress.JSON, "{\"userID\":\"1235355\"," +
                            "\"content\":\"" + etMySettingOpinion.getText().toString() + "\"}");
                    Request request = new Request.Builder().url(UrlAddress.UUPfeedbackUrl).post(body).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("111", "错误！" + e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e("111", "" + response.body().string());
                        }
                    });
                    Toast.makeText(this, "" + etMySettingOpinion.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请输入您的宝贵意见", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
        }
    }

}
