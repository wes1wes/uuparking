package babi.com.uuparking.init.homePage.infoCenter.wallet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
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
 * Created by b on 2017/9/7.
 */

public class Recharge extends AppCompatActivity {
    String aliBillRecord;
    Map<String, Object> map;
    IWXAPI api;
    OkHttpClient okHttpClient;
    PayReq requestwx;
    @BindView(R.id.btn_my_wallet_recharge)
    Button btnMyWalletRecharge;
    @BindView(R.id.tv_my_wallet_recharge_200)
    TextView tvMyWalletRecharge200;
    @BindView(R.id.tv_my_wallet_recharge_100)
    TextView tvMyWalletRecharge100;
    @BindView(R.id.tv_my_wallet_recharge_50)
    TextView tvMyWalletRecharge50;
    @BindView(R.id.tv_my_wallet_recharge_20)
    TextView tvMyWalletRecharge20;
    @BindView(R.id.image_my_wallet_recharge_alpay)
    ImageView imageMyWalletRechargeAlpay;
    @BindView(R.id.image_my_wallet_recharge_wx)
    ImageView imageMyWalletRechargeWx;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    @BindView(R.id.btn_my_wallet_user_book)
    TextView btnMyWalletUserBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_wallet_recharge_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("充值中心");
        okHttpClient = new OkHttpClient();
        map = new HashMap<>();
        api = WXAPIFactory.createWXAPI(getApplicationContext(), WXAppID.wxAppID, true);
        requestwx = new PayReq();
        tvMyWalletRecharge200.setTag("0");
        imageMyWalletRechargeAlpay.setTag(2);
        imageMyWalletRechargeWx.setTag(1);
    }

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                internetRecharge();
                Toast.makeText(Recharge.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Recharge.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
/*支付宝支付成功传给服务端充值结果*/
    private void internetRecharge() {
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("money", tvMyWalletRecharge200.getTag());
        map.put("billRecordID", aliBillRecord);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPrechargeUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {
//                            Intent intent = new Intent(getApplicationContext(), HistoryDetails.class);
////                            intent.putExtra("orderId", );
//                            startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.image_my_wallet_recharge_alpay, R.id.image_my_wallet_recharge_wx, R.id.tv_my_wallet_recharge_200,
            R.id.tv_my_wallet_recharge_100, R.id.tv_my_wallet_recharge_50, R.id.tv_my_wallet_recharge_20,
            R.id.btn_my_wallet_recharge, R.id.image_common_diy_toolbar_back, R.id.btn_my_wallet_user_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_my_wallet_recharge_200:
                setColorGray();
                tvMyWalletRecharge200.setBackgroundResource(R.drawable.bg_rectangle_rounded_blue_big);
                tvMyWalletRecharge200.setTag("20000");
                break;
            case R.id.tv_my_wallet_recharge_100:
                setColorGray();
                tvMyWalletRecharge100.setBackgroundResource(R.drawable.bg_rectangle_rounded_blue_big);
                tvMyWalletRecharge200.setTag("10000");
                break;
            case R.id.tv_my_wallet_recharge_50:
                setColorGray();
                tvMyWalletRecharge50.setBackgroundResource(R.drawable.bg_rectangle_rounded_blue_big);
                tvMyWalletRecharge200.setTag("5000");
                break;
            case R.id.tv_my_wallet_recharge_20:
                setColorGray();
                tvMyWalletRecharge20.setBackgroundResource(R.drawable.bg_rectangle_rounded_blue_big);
                tvMyWalletRecharge200.setTag("2000");
                break;
            case R.id.image_my_wallet_recharge_alpay:
                if ((int) imageMyWalletRechargeAlpay.getTag() == 1) {
                    imageMyWalletRechargeAlpay.setTag(2);
                    imageMyWalletRechargeWx.setTag(1);
                    imageMyWalletRechargeAlpay.setImageResource(R.mipmap.radioed);
                    imageMyWalletRechargeWx.setImageResource(R.mipmap.radio);
                } else {
                    imageMyWalletRechargeAlpay.setTag(1);
                    imageMyWalletRechargeWx.setTag(2);
                    imageMyWalletRechargeAlpay.setImageResource(R.mipmap.radio);
                    imageMyWalletRechargeWx.setImageResource(R.mipmap.radioed);
                }
                break;
            case R.id.image_my_wallet_recharge_wx:
                if ((int) imageMyWalletRechargeWx.getTag() == 1) {
                    imageMyWalletRechargeAlpay.setTag(1);
                    imageMyWalletRechargeWx.setTag(2);
                    imageMyWalletRechargeAlpay.setImageResource(R.mipmap.radio);
                    imageMyWalletRechargeWx.setImageResource(R.mipmap.radioed);
                } else {
                    imageMyWalletRechargeAlpay.setTag(2);
                    imageMyWalletRechargeWx.setTag(1);
                    imageMyWalletRechargeAlpay.setImageResource(R.mipmap.radioed);
                    imageMyWalletRechargeWx.setImageResource(R.mipmap.radio);
                }
                break;
            case R.id.btn_my_wallet_recharge:
                if (!tvMyWalletRecharge200.getTag().equals("0")) {
                    if ((int) imageMyWalletRechargeAlpay.getTag() == 2) {
                        aliPayRecharge();
                        return;
                    }
                    if ((int) imageMyWalletRechargeWx.getTag() == 2) {
                        wxPayRecharge();
                        return;
                    }
                    Toast.makeText(this, "亲，您还没有选择支付方式！！", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "亲，请选择充值金额！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.btn_my_wallet_user_book:
                // TODO: 2018/3/31 用户协议
                break;
        }
    }

    /**
     * 使用微信充值
     */
    private void wxPayRecharge() {
        SharedPreferences WXMoney = this.getSharedPreferences("wxRecharge_money", 0);
        final SharedPreferences.Editor eWX = WXMoney.edit();
        // 微信支付
        api.registerApp(WXAppID.wxAppID);
        map.put("money", tvMyWalletRecharge200.getTag());//
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("subject", "充值");
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        final Request request = new Request.Builder().url(UrlAddress.UUPWXPayUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("code").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("resultMap");
                        eWX.putString("type", "recharge");
                        eWX.putString("money", tvMyWalletRecharge200.getTag().toString());
                        eWX.putString("billRecordID", object.getString("billRecordID"));
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

    /**
     * 使用支付宝充值
     */
    private void aliPayRecharge() {
        // 支付宝支付
        map.put("money", tvMyWalletRecharge200.getTag());
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("subject", "充值");
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        final Request request = new Request.Builder().url(UrlAddress.UUPALiPayUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = null;
                // 订单信息
                try {
                    jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("code").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("resultMap");
                        aliBillRecord = object.getString("billRecordID");
                        orderInfo = object.getString("aliPayStr");
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(Recharge.this);
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

    String orderInfo = null; // 订单信息

    /**
     * 设置充值金额背景为蓝色
     */
    private void setColorGray() {
        tvMyWalletRecharge200.setBackgroundResource(R.drawable.border);
        tvMyWalletRecharge100.setBackgroundResource(R.drawable.border);
        tvMyWalletRecharge50.setBackgroundResource(R.drawable.border);
        tvMyWalletRecharge20.setBackgroundResource(R.drawable.border);
    }

}
