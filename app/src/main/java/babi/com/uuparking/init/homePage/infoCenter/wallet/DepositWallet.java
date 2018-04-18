package babi.com.uuparking.init.homePage.infoCenter.wallet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.thirdParty.alipay.PayResult;
import babi.com.uuparking.wxapi.WXAppID;
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
 * Created by b on 2017/9/15.
 */

public class DepositWallet extends AppCompatActivity {

    IWXAPI api;
    String depositMoney;
    SharedPreferences user;
    OkHttpClient okHttpClient;
    PayReq requestwx;
    @BindView(R.id.tv_my_wallet_deposit_text)
    TextView tvMyWalletDepositText;
    @BindView(R.id.tv_my_wallet_deposit)
    TextView tvMyWalletDeposit;
    @BindView(R.id.tv_my_wallet_deposit_alpay)
    TextView tvMyWalletDepositAlpay;
    @BindView(R.id.cb_my_wallet_deposit_alpay)
    CheckBox cbMyWalletDepositAlpay;
    @BindView(R.id.ll_my_wallet_deposit_alpay)
    LinearLayout llMyWalletDepositAlpay;
    @BindView(R.id.tv_my_wallet_deposit_wx)
    TextView tvMyWalletDepositWx;
    @BindView(R.id.cb_my_wallet_deposit_wx)
    CheckBox cbMyWalletDepositWx;
    @BindView(R.id.ll_my_wallet_deposit_wx)
    LinearLayout llMyWalletDepositWx;
    @BindView(R.id.btn_my_wallet_deposit)
    Button btnMyWalletDeposit;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_wallet_deposit_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("押金充值");
        api = WXAPIFactory.createWXAPI(getApplicationContext(), WXAppID.wxAppID, true);
        requestwx = new PayReq();
        user = getSharedPreferences("user_info", 0);
        okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("key", "depositMoney")
                .add("userID", "" + user.getString("userID", ""))
                .add("sessionID", "" + user.getString("sessionID", ""))
                .build();
        final Request request = new Request.Builder().url(UrlAddress.getConstantValueUrl).post(body).build();
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
                    depositMoney = Integer.valueOf(jsonObject.getString("constant")) * 100 + "";
                    final JSONObject finalJsonObject = jsonObject;
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            try {
                                tvMyWalletDeposit.setText(finalJsonObject.getString("constant") + "元");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                user.edit().putString("deposit", "1");
                user.edit().commit();

                Toast.makeText(DepositWallet.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                Toast.makeText(DepositWallet.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
            RequestBody body = null;

            body = new FormBody.Builder()
                    .add("payWay", "0")
                    .add("userID", "" + user.getString("userID", ""))
                    .add("sessionID", "" + user.getString("sessionID", ""))
                    .add("walletID", user.getString("walletID", ""))
                    .add("money", depositMoney)
                    .add("billRecordID", aliBillRecord)
                    .add("result", "-1")
                    .build();

            final Request request = new Request.Builder().url(UrlAddress.payDepositUrl).post(body).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());

                        switch (jsonObject.getString("code")) {
                            case "1401":
                                finish();
                                break;
                        }
                        Toast.makeText(getApplicationContext(), jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    };
    String aliBillRecord;
    String orderInfo;

    @OnClick({R.id.image_common_diy_toolbar_back, R.id.cb_my_wallet_deposit_alpay, R.id.ll_my_wallet_deposit_alpay, R.id.ll_my_wallet_deposit_wx, R.id.cb_my_wallet_deposit_wx, R.id.btn_my_wallet_deposit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.ll_my_wallet_deposit_alpay:
            case R.id.cb_my_wallet_deposit_alpay:
                if (cbMyWalletDepositAlpay.isChecked()) {
                    cbMyWalletDepositAlpay.setChecked(true);
                    cbMyWalletDepositWx.setChecked(false);
                } else {
                    cbMyWalletDepositAlpay.setChecked(false);
                }
                break;
            case R.id.ll_my_wallet_deposit_wx:
            case R.id.cb_my_wallet_deposit_wx:
                if (cbMyWalletDepositWx.isChecked()) {
                    cbMyWalletDepositWx.setChecked(true);
                    cbMyWalletDepositAlpay.setChecked(false);
                } else {
                    cbMyWalletDepositWx.setChecked(false);
                }
                break;
            case R.id.btn_my_wallet_deposit:
                if (cbMyWalletDepositAlpay.isChecked()) {
                    // 支付宝支付
                    RequestBody body = new FormBody.Builder()
                            .add("money", depositMoney)
                            .add("userID", "" + user.getString("userID", ""))
                            .add("sessionID", "" + user.getString("sessionID", ""))
                            .build();
                    final Request request = new Request.Builder().url(UrlAddress.walletAliPayUrl).post(body).build();
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
                                aliBillRecord = jsonObject.getString("billRecordID");
                                orderInfo = (String) jsonObject.get("msg");
                                Runnable payRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        PayTask alipay = new PayTask(DepositWallet.this);
                                        Map<String, String> result = alipay.payV2(orderInfo, true);
                                        Message msg = new Message();
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                }; // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else if (cbMyWalletDepositWx.isChecked()) {
                    // 微信支付
                    SharedPreferences WXMoney = this.getSharedPreferences("wxRecharge_money", 0);
                    final SharedPreferences.Editor eWX = WXMoney.edit();
                    api.registerApp(WXAppID.wxAppID);
                    RequestBody body = new FormBody.Builder()
                            .add("money", depositMoney)
                            .add("userID", "" + user.getString("userID", ""))
                            .add("sessionID", "" + user.getString("sessionID", ""))
                            .build();
                    final Request request = new Request.Builder().url(UrlAddress.walletWXPayUrl).post(body).build();
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
                                eWX.putString("URL", "deposit");
                                eWX.putString("recharge", depositMoney);
                                eWX.putString("billRecordID", jsonObject.getString("billRecordID"));
                                eWX.commit();

                                requestwx.partnerId = jsonObject.getString("partnerid");
                                requestwx.prepayId = jsonObject.getString("prepayid");
                                requestwx.packageValue = jsonObject.getString("package");//"Sign=WXPay"
                                requestwx.nonceStr = jsonObject.getString("noncestr");
                                requestwx.timeStamp = jsonObject.getString("timestamp");
                                requestwx.sign = jsonObject.getString("sign");
                                api.sendReq(requestwx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
//                    请选择一种支付方式
                    Toast.makeText(this, "亲，您还没有选择支付方式！！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
