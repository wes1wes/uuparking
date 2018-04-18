package babi.com.uuparking.init.homePage.infoCenter.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
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
 * Created by b on 2017/8/11.
 */

public class Wallet extends AppCompatActivity {

    OkHttpClient okHttpClient;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    @BindView(R.id.tv_my_wallet_balance)
    TextView tvMyWalletBalance;
    @BindView(R.id.tv_my_wallet_money)
    TextView tvMyWalletMoney;
    @BindView(R.id.image_my_wallet_recharge)
    ImageView imageMyWalletRecharge;
    @BindView(R.id.tv_my_wallet_recharge)
    TextView tvMyWalletRecharge;
    @BindView(R.id.image_my_wallet_apply)
    ImageView imageMyWalletApply;
    @BindView(R.id.tv_my_wallet_apply)
    TextView tvMyWalletApply;
    @BindView(R.id.tv_my_wallet_all_details)
    TextView tvMyWalletAllDetails;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_wallet_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("钱包");
        okHttpClient = new OkHttpClient();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initWallet();
//        initUserCost();
    }

    /**
     * 获取钱包金额
     */
    private void initWallet() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPUserAccountBalanceUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("code").equals("1")) {
                        updateViewUI(jsonObject.getJSONObject("resultMap").getString("deposit"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获取消费列表
     */
    private void initUserCost() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("page", 1);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPConsumptiondetailsUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
//                if (costDetailsGsons.size() > 0) {
//                    costDetailsGsons.clear();
//                }
                Log.e("cost", str);
                Logger.e(str);
                final JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {
                        JSONObject resultMap = jsonObject.getJSONObject("resultMap");
                        JSONArray array = resultMap.getJSONArray("orderPage");
//                        costDetailsGsons.addAll(CostDetailsGson.arrayCostDetailsGsonFromData(array.toString()));
//                        updateCostViewUI();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 更新UI
     */
    private void updateViewUI(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMyWalletMoney.setText(string);
            }
        });
    }

    /**
     * 更新UI
     */
//    private void updateCostViewUI() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                costDetailsAdapter.notifyDataSetChanged();
//            }
//        });
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.wallet_details) {
            Bundle bundle = new Bundle();

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.image_common_diy_toolbar_back, R.id.image_my_wallet_recharge, R.id.tv_my_wallet_recharge,
            R.id.image_my_wallet_apply, R.id.tv_my_wallet_apply,R.id.tv_my_wallet_all_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.tv_my_wallet_all_details:
                Intent intentDetails = new Intent(this, BillDetails.class);
                startActivity(intentDetails);
                break;
            case R.id.image_my_wallet_recharge:
            case R.id.tv_my_wallet_recharge:
                Intent intentRecharge = new Intent(this, Recharge.class);
                startActivity(intentRecharge);
                break;
            case R.id.image_my_wallet_apply:
            case R.id.tv_my_wallet_apply:
                // TODO: 2018/3/31 提现功能
                break;
        }
    }
}
