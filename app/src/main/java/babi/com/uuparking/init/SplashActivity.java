package babi.com.uuparking.init;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import babi.com.uuparking.R;
import babi.com.uuparking.init.welcome.WelcomeGuideActivity;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.utils.commentUtil.AppConstants;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.SpUtils;
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
 * Created by b on 2017/12/4.
 */

public class SplashActivity extends Activity {
    OkHttpClient okHttpClient;
    @BindView(R.id.imageView)
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        final boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.start_uuparking_layout);
        ButterKnife.bind(this);
        okHttpClient = new OkHttpClient();

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(getApplication());
        Picasso.with(getBaseContext()).load(R.mipmap.uuparking).into(imageView);

        // TODO: 2018/1/10 获得启动也广告图，接口待确定
        StartNetworkRequest();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (SpUserUtils.getString(getBaseContext(), "userId").isEmpty()) {
                    enterLoginActivity();
                } else {
                    enterHomeActivity();
                }
            }
        }, 3000);
    }

    private void StartNetworkRequest() {
        RequestBody body = RequestBody.create(UrlAddress.JSON, "{\"pageType\":\"START_PAGE\"}");
        Request request = new Request.Builder().url(UrlAddress.UUPgetStartPageUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e("startpage---", "网路链接错误！原因：" + e.toString(), null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                JSONObject js = null;
                try {
                    js = new JSONObject(str);
                    if (js.get("code").toString().equals("1")) {
                        JSONObject jso = js.getJSONObject("resultMap");
                        updateImage(jso.getString("PageUrl"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateImage(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getBaseContext()).load(s).error(R.mipmap.uuparking).into(imageView);
            }
        });
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, UUParkingActivity.class);
        startActivity(intent);
        finish();
    }

    private void enterLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.imageView)
    public void onViewClicked() {
//        如果添加广告链接，可以在这里
    }
}
