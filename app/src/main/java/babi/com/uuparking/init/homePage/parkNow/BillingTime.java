package babi.com.uuparking.init.homePage.parkNow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.ShareAdapter;
import babi.com.uuparking.init.utils.commentUtil.FinalConstant;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.WaitingDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.CarportDetailsGson;
import babi.com.uuparking.init.utils.gsonFormatObject.ShareInfoGson;
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
 * Created by BGD on 2018/1/26.
 */

public class BillingTime extends AppCompatActivity {
    @BindView(R.id.btn_home_parking_billing_uplock)
    Button btnHomeParkingBillingUplock;
    @BindView(R.id.tv_home_parking_billing_carparkid)
    TextView tvHomeParkingBillingCarparkid;
    @BindView(R.id.tv_home_parking_billing_price)
    TextView tvHomeParkingBillingPrice;
    @BindView(R.id.btn_home_parking_billing_help)
    Button btnHomeParkingBillingHelp;
    @BindView(R.id.tv_home_parking_billing_carparkid_show)
    TextView tvHomeParkingBillingCarparkidShow;
    @BindView(R.id.lv_home_parking_billing_price)
    ListView lvHomeParkingBillingPrice;
    @BindView(R.id.tv_home_parking_billing_address)
    TextView tvHomeParkingBillingAddress;
    @BindView(R.id.cot_home_parking_chronoter)
    Chronometer cotHomeParkingChronoter;
    OkHttpClient client;
    Bundle bundle;
    int clickNum = 1;
    boolean finalTry = false;
    ShareAdapter shareBillAdapter;
    CarportDetailsGson carportDetailsGson;
    List<ShareInfoGson> shareInfoGsons;
    @BindView(R.id.tv_home_parking_billing_orderid_show)
    TextView tvHomeParkingBillingOrderidShow;
    @BindView(R.id.tv_home_parking_billing_orderid)
    TextView tvHomeParkingBillingOrderid;
    @BindView(R.id.image_billing_go_back)
    ImageView imageBillingGoBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_parking_time_billing_layout);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        bundle = getIntent().getBundleExtra("bundle");
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        getData(bundle);
        shareBillAdapter = new ShareAdapter(this, shareInfoGsons);
        lvHomeParkingBillingPrice.setAdapter(shareBillAdapter);
        tvHomeParkingBillingOrderidShow.setText(bundle.getString("orderId"));
        tvHomeParkingBillingCarparkidShow.setText(carportDetailsGson.getLockId());
        tvHomeParkingBillingAddress.setText(carportDetailsGson.getCity()
                + carportDetailsGson.getDistrict() + carportDetailsGson.getDetailAddress());
        startBill();
    }

    /*开始计时*/
    private void startBill() {
        cotHomeParkingChronoter.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = System.currentTimeMillis() - cArg.getBase();
                Date d = new Date(time);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                cotHomeParkingChronoter.setText(sdf.format(d));
            }
        });
        if (bundle.getString("downLockTime") == null) {
            cotHomeParkingChronoter.setBase(System.currentTimeMillis());
        } else {
            cotHomeParkingChronoter.setBase(System.currentTimeMillis() - Long.valueOf(bundle.getString("downLockTime")));
        }
        cotHomeParkingChronoter.start();
    }

    /*解析carparkDetail数据*/
    private void getData(Bundle bundle) {
        carportDetailsGson = CarportDetailsGson.objectFromData(bundle.getString("carparkDetail"));
        shareInfoGsons = new ArrayList<>();
        shareInfoGsons.addAll(carportDetailsGson.getShareInfo());
    }

    @OnClick({R.id.btn_home_parking_billing_uplock, R.id.btn_home_parking_billing_help,R.id.image_billing_go_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_home_parking_billing_uplock:
                if (clickNum >= 2) {
                    finalTry = true;
                } else {
                    finalTry = false;
                }
                requsetEndparking();
                break;
            case R.id.btn_home_parking_billing_help:
                // TODO: 2018/1/26 用户找车 
                break;
            case R.id.image_billing_go_back:
                finish();
                break;
        }
    }

    /*请求结束停车*/
    private void requsetEndparking() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", bundle.getString("orderId"));
        map.put("finalTry", finalTry);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPcloseLockUrl).post(body).build();
        Call call = client.newBuilder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(FinalConstant.REQUESTTIMEDOUT, TimeUnit.SECONDS).build().newCall(request);
        WaitingDialog.showWaitingDialog(this);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    //判断超时异常
                    updateUI("连接车锁失败！请重试");

                }
                if (e instanceof ConnectException) {
                    //判断连接异常
                    updateUI("网络异常，请检查网络连接！");

                }
                WaitingDialog.cancelWaitingDialog();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                dealResult(string);
                WaitingDialog.cancelWaitingDialog();
                Log.e("关闭车锁", string);
            }
        });
    }

    /**
     * 处理关闭车锁返回结果
     */
    private void dealResult(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String code = jsonObject.getString("code");

            if (code.equals("J202")) {
                updateUI("网络连接错误！");
                return;
            }
            if (code.equals("J203")) {
                updateUI("车锁操作失败，请重试！");
                clickNum++;
                return;
            }

            if (code.equals("J204")) {
                updateUI("车锁异常，已为您结束计费");
                Bundle bundleIntent = new Bundle();
                bundleIntent.putString("costDetails", jsonObject.getJSONObject("resultMap").toString());
                Intent intent = new Intent(getBaseContext(), PayParkingFee.class);
                intent.putExtra("bundle", bundleIntent);
                startActivity(intent);
                cotHomeParkingChronoter.stop();
                finish();
                return;
            }
            if (code.equals("J201")) {
                Bundle bundleIntent = new Bundle();
                bundleIntent.putString("costDetails", jsonObject.getJSONObject("resultMap").toString());
                bundleIntent.putString("orderId", bundle.getString("orderId"));
                Intent intent = new Intent(getBaseContext(), PayParkingFee.class);
                intent.putExtra("bundle", bundleIntent);
                startActivity(intent);
                cotHomeParkingChronoter.stop();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*更新UI*/
    private void updateUI(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
