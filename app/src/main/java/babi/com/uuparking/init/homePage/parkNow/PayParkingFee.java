package babi.com.uuparking.init.homePage.parkNow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.infoCenter.history.HistoryDetails;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.PayResultGson;
import babi.com.uuparking.thirdParty.alipay.PayResult;
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
 * Created by b on 2018/1/27.
 */

public class PayParkingFee extends AppCompatActivity {
    @BindView(R.id.tv_home_parking_deposit_text)
    TextView tvHomeParkingDepositText;
    @BindView(R.id.tv_home_parking_deposit)
    TextView tvHomeParkingDeposit;
    @BindView(R.id.tv_home_parking_deposit_alipay)
    TextView tvHomeParkingDepositAlipay;
    @BindView(R.id.ll_home_parking_deposit_alpay)
    LinearLayout llHomeParkingDepositAlpay;
    @BindView(R.id.ll_home_parking_deposit_wx)
    LinearLayout llHomeParkingDepositWx;
    @BindView(R.id.tv_home_parking_deposit_icome)
    TextView tvHomeParkingDepositIcome;
    @BindView(R.id.ll_home_parking_deposit_icome)
    LinearLayout llHomeParkingDepositIcome;
    @BindView(R.id.btn_home_parking_deposit)
    Button btnHomeParkingDeposit;
    OkHttpClient client;
    IWXAPI api;
    PayReq requestwx;
    String aliBillRecord;
    String orderInfo = null; // 订单信息
    Bundle bundle;
    @BindView(R.id.tv_home_parking_deposit_orderid)
    TextView tvHomeParkingDepositOrderid;
    @BindView(R.id.tv_home_parking_deposit_orderid_show)
    TextView tvHomeParkingDepositOrderidShow;
    @BindView(R.id.tv_home_parking_deposit_time)
    TextView tvHomeParkingDepositTime;
    @BindView(R.id.tv_home_parking_deposit_time_show)
    TextView tvHomeParkingDepositTimeShow;
    public static PayParkingFee payparkingFee;
    @BindView(R.id.image_home_parking_deposit_icome)
    ImageView imageHomeParkingDepositIcome;
    @BindView(R.id.image_home_parking_deposit_alpay)
    ImageView imageHomeParkingDepositAlpay;
    @BindView(R.id.image_home_parking_deposit_wx)
    ImageView imageHomeParkingDepositWx;
    @BindView(R.id.tv_home_parking_charge_method)
    TextView tvHomeParkingChargeMethod;
    @BindView(R.id.tv_home_parking_charge_method_show)
    TextView tvHomeParkingChargeMethodShow;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    @BindView(R.id.tv_home_parking_deposit_wx)
    TextView tvHomeParkingDepositWx;

    private PayResultGson payResultGson;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_parking_deposit_layout);
        ButterKnife.bind(this);
        payparkingFee = this;

        tvCommonDiyToolbarTitle.setText("停车支付");
        client = new OkHttpClient();
        api = WXAPIFactory.createWXAPI(getApplicationContext(), "wx03f0d3029fee2a42", true);
        requestwx = new PayReq();
        bundle = getIntent().getBundleExtra("bundle");
        payResultGson = new PayResultGson();
        payResultGson = PayResultGson.objectFromData(bundle.getString("costDetails"));
        tvHomeParkingDeposit.setText(payResultGson.getCost());
        tvHomeParkingDepositOrderidShow.setText(payResultGson.getOrderId());
        llHomeParkingDepositAlpay.setTag(1);
        llHomeParkingDepositIcome.setTag(2);
        llHomeParkingDepositWx.setTag(1);

        long normalTime = payResultGson.getNormalTime() / 1000;//+Long.valueOf(payResultGson.getOverTime())
        long h = normalTime / 3600;
        long m = normalTime / 60 % 60;
        long s = normalTime % 60;
        tvHomeParkingDepositTimeShow.setText((h > 9 ? h : ("0" + h)) + "时" + (m > 9 ? m : ("0" + m)) + "分" + (s > 9 ? s : ("" + s)) + "秒");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btnHomeParkingDeposit.setClickable(true);
    }

    @OnClick({R.id.tv_home_parking_deposit_alipay, R.id.ll_home_parking_deposit_alpay, R.id.tv_home_parking_deposit_wx,
            R.id.ll_home_parking_deposit_wx, R.id.tv_home_parking_deposit_icome, R.id.ll_home_parking_deposit_icome,
            R.id.btn_home_parking_deposit,R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.tv_home_parking_deposit_alipay:
            case R.id.ll_home_parking_deposit_alpay:
                if ((int) llHomeParkingDepositAlpay.getTag() == 1) {
                    imageHomeParkingDepositAlpay.setImageResource(R.mipmap.radioed);
                    imageHomeParkingDepositWx.setImageResource(R.mipmap.radio);
                    imageHomeParkingDepositIcome.setImageResource(R.mipmap.radio);
                    llHomeParkingDepositAlpay.setTag(2);
                    llHomeParkingDepositWx.setTag(1);
                    llHomeParkingDepositIcome.setTag(1);
                } else {
                    imageHomeParkingDepositAlpay.setImageResource(R.mipmap.radio);
                    llHomeParkingDepositAlpay.setTag(1);
                }
                break;
            case R.id.tv_home_parking_deposit_wx:
            case R.id.ll_home_parking_deposit_wx:
                if ((int) llHomeParkingDepositWx.getTag() == 1) {
                    imageHomeParkingDepositAlpay.setImageResource(R.mipmap.radio);
                    imageHomeParkingDepositWx.setImageResource(R.mipmap.radioed);
                    imageHomeParkingDepositIcome.setImageResource(R.mipmap.radio);
                    llHomeParkingDepositAlpay.setTag(1);
                    llHomeParkingDepositWx.setTag(2);
                    llHomeParkingDepositIcome.setTag(1);
                } else {
                    imageHomeParkingDepositWx.setImageResource(R.mipmap.radio);
                    llHomeParkingDepositWx.setTag(1);
                }
                break;
            case R.id.tv_home_parking_deposit_icome:
            case R.id.ll_home_parking_deposit_icome:
                if ((int) llHomeParkingDepositIcome.getTag() == 1) {
                    imageHomeParkingDepositAlpay.setImageResource(R.mipmap.radio);
                    imageHomeParkingDepositWx.setImageResource(R.mipmap.radio);
                    imageHomeParkingDepositIcome.setImageResource(R.mipmap.radioed);
                    llHomeParkingDepositAlpay.setTag(1);
                    llHomeParkingDepositWx.setTag(1);
                    llHomeParkingDepositIcome.setTag(2);
                } else {
                    imageHomeParkingDepositIcome.setImageResource(R.mipmap.radio);
                    llHomeParkingDepositIcome.setTag(1);
                }
                break;
            case R.id.btn_home_parking_deposit:
                // TODO: 2018/1/27 停车费结算支付
                Map<String, Object> map = new HashMap<>();
                map.put("userId", SpUserUtils.getString(this, "userId"));
                map.put("orderId", payResultGson.getOrderId());
                map.put("money", (int) (Double.valueOf(payResultGson.getCost()) * 100));
                map.put("subject", "消费");
                RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
                map.clear();
                Map<String, Object> mappay = new HashMap<>();
                mappay.put("money", payResultGson.getCost());
                mappay.put("orderId", payResultGson.getOrderId());
                mappay.put("userId", SpUserUtils.getString(this, "userId"));
                mappay.put("billRecordID", "");
                RequestBody bodypay = RequestBody.create(UrlAddress.JSON, new Gson().toJson(mappay));
                mappay.clear();
                btnHomeParkingDeposit.setClickable(false);
                if ((int) llHomeParkingDepositWx.getTag() == 2) {
                    wxPay(body);
                    return;
                }
                if ((int) llHomeParkingDepositAlpay.getTag() == 2) {
                    alpayPay(body);
                    return;
                }
                if ((int) llHomeParkingDepositIcome.getTag() == 2) {
                    icomePay(bodypay);
                    return;
                }
                Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();


                break;
        }
    }

    /**
     * 收益支付
     */
    private void icomePay(RequestBody body) {
        // 余额支付
        Request request = new Request.Builder().url(UrlAddress.UUPpaymentUrl).post(body).build();
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
                        Intent intent = new Intent(getBaseContext(), HistoryDetails.class);
                        intent.putExtra("id", payResultGson.getOrderId());
                        startActivity(intent);
                        finish();
                    } else {
                        updateToastUI(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                btnHomeParkingDeposit.setClickable(true);
            }

        });
    }

    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支付宝支付
     */
    private void alpayPay(RequestBody body) {
        // 支付宝支付
        final Request request = new Request.Builder().url(UrlAddress.UUPALiPayUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.e("支付宝支付", str);
                JSONObject jsonObject = null;
                // 订单信息
                try {
                    jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {

                        JSONObject object = jsonObject.getJSONObject("resultMap");
                        aliBillRecord = object.getString("billRecordID");
                        orderInfo = object.getString("aliPayStr");
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PayParkingFee.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        }; // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 微信支付
     */
    private void wxPay(RequestBody body) {
        SharedPreferences WXMoney = this.getSharedPreferences("wxRecharge_money", 0);
        final SharedPreferences.Editor eWX = WXMoney.edit();
        // 微信支付
        api.registerApp(WXAppID.wxAppID);
        final Request request = new Request.Builder().url(UrlAddress.UUPWXPayUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.e("微信支付", str);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("resultMap");
                        eWX.putString("billRecordID", object.getString("billRecordID"));
                        eWX.putString("orderId", payResultGson.getOrderId());
                        eWX.putString("money", payResultGson.getCost());
                        eWX.putString("type", "parking");
                        eWX.commit();

                        requestwx.appId = object.getString("appid");
                        requestwx.partnerId = object.getString("partnerid");
                        requestwx.prepayId = object.getString("prepayid");
                        requestwx.packageValue = object.getString("package");//"Sign=WXPay"
                        requestwx.nonceStr = object.getString("noncestr");
                        requestwx.timeStamp = object.getString("timestamp");
                        requestwx.sign = object.getString("sign");
                        api.sendReq(requestwx);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // TODO: 2018/1/29 停车费用支付宝支付结果处理
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                internetAliPay();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(getBaseContext(), "支付失败", Toast.LENGTH_SHORT).show();
            }
            btnHomeParkingDeposit.setClickable(true);
        }
    };

    private void internetAliPay() {
        Map<String, Object> mappay = new HashMap<>();
        mappay.put("money", payResultGson.getCost());
        mappay.put("orderId", payResultGson.getOrderId());
        mappay.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
        mappay.put("billRecordID", aliBillRecord);
        RequestBody bodyAli = RequestBody.create(UrlAddress.JSON, new Gson().toJson(mappay));
        mappay.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPpaymentUrl).post(bodyAli).build();
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
                        Intent intent = new Intent(getBaseContext(), HistoryDetails.class);
                        intent.putExtra("id", payResultGson.getOrderId());
                        startActivity(intent);
                        finish();
                    } else {
                        updateToastUI(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
