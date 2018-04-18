package babi.com.uuparking.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.parkNow.PayParkingFee;
import babi.com.uuparking.init.homePage.infoCenter.history.HistoryDetails;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    SharedPreferences WXMoney;
    OkHttpClient okHttpClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        WXMoney=this.getSharedPreferences("wxRecharge_money",0);
        okHttpClient=new OkHttpClient();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this,WXAppID.wxAppID, false);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("微信支付进入", "onReq: paywx" );
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.e("微信支付进入", "onResp: paywx" );
        int code = resp.errCode;
        switch (code){
            case 0://支付成功后的界面
                if (WXMoney.getString("type","").equals("parking")){
                    internetWXPay();
                }else if (WXMoney.getString("type","").equals("recharge")){
                    internetWXRecharge();
                }
                Toast.makeText(this,"支付成功", Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(this,"支付失败", Toast.LENGTH_SHORT).show();
                break;
            case -2:
                //用户取消支付后的界面
                Toast.makeText(this,"取消支付", Toast.LENGTH_SHORT).show();
                break;
        }
        //微信支付后续操作，失败，成功，取消
        finish();
    }

    private void internetWXRecharge() {
        Map<String, Object> mappay = new HashMap<>();
        mappay.put("money",WXMoney.getString("money","") );
        mappay.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
        mappay.put("billRecordID",WXMoney.getString("billRecordID",""));
        RequestBody bodyAli = RequestBody.create(UrlAddress.JSON, new Gson().toJson(mappay));
        mappay.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPrechargeUrl).post(bodyAli).build();
        Call call = okHttpClient.newCall(request);
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
                       updateToastUI("充值成功");
                    } else {
                        updateToastUI(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void internetWXPay() {
        Map<String, Object> mappay = new HashMap<>();
        mappay.put("money",WXMoney.getString("money","") );
        mappay.put("orderId", WXMoney.getString("orderId",""));
        mappay.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
        mappay.put("billRecordID",WXMoney.getString("billRecordID",""));
        RequestBody bodyAli = RequestBody.create(UrlAddress.JSON, new Gson().toJson(mappay));
        mappay.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPpaymentUrl).post(bodyAli).build();
        Call call = okHttpClient.newCall(request);
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
                        intent.putExtra("id",WXMoney.getString("orderId",""));
                        startActivity(intent);
                        PayParkingFee.payparkingFee.finish();
                    } else {
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
                Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}