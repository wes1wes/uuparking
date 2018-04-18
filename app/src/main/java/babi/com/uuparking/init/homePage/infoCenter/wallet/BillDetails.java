package babi.com.uuparking.init.homePage.infoCenter.wallet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.WalletDetailsAdapter;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.WellatDetailsGson;
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
 * Created by BGD on 2017/10/8.
 */

public class BillDetails extends AppCompatActivity {
    Bundle bundle;
    Map<String, Object> map;
    boolean endhistory = false;
    List<WellatDetailsGson> list;
    List<WellatDetailsGson> list1;
    WalletDetailsAdapter eadapter;
    int i = 1;
    OkHttpClient okHttpClient;
    @BindView(R.id.lv_wallet_details_recharge)
    ListView lvWalletDetailsRecharge;
    @BindView(R.id.srl_mine_wallet_recharge_details)
    SmartRefreshLayout srlMineWalletRechargeDetails;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_wallet_details);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("钱包明细");
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        okHttpClient = new OkHttpClient();
        map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("page", 1);
        okhttpWalletDetails(map);
        map.clear();
        srlMineWalletRechargeDetails.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list1.clear();
                map.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
                map.put("page", 1);
                okhttpWalletDetails(map);
                map.clear();
                refreshlayout.finishRefresh();
            }
        });

        srlMineWalletRechargeDetails.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (!endhistory) {
                    map.put("page", i++);
                    map.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
                    okhttpWalletDetails(map);
                    map.clear();
                    okhttpWalletDetails(map);
                } else {
                    Toast.makeText(getBaseContext(), "沒有更多了", Toast.LENGTH_SHORT).show();
                    refreshlayout.finishLoadmore();
                }

            }
        });
        eadapter = new WalletDetailsAdapter(this, list1);
        lvWalletDetailsRecharge.setAdapter(eadapter);
    }

    private void okhttpWalletDetails(Map<String, Object> map) {
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPrechargeparticularUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                srlMineWalletRechargeDetails.finishLoadmore();
                srlMineWalletRechargeDetails.finishRefresh();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("获取充值详情", "onResponse: " + str);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    if (!jsonObject.getString("code").equals("1")) {
                        updateToastUI(jsonObject.getString("message"));
                        return;
                    }
                    JSONObject resultMap = jsonObject.getJSONObject("resultMap");
                    JSONArray arr = resultMap.getJSONArray("recharges");
                    if (arr.length() <= 9) {
                        endhistory = true;
                    } else {
                        endhistory = false;
                    }
                    list.clear();
                    list.addAll(WellatDetailsGson.arrayWellatDetailsGsonFromData(arr.toString()));
                    list1.addAll(list);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            srlMineWalletRechargeDetails.finishLoadmore();
                            srlMineWalletRechargeDetails.finishRefresh();
                            eadapter.notifyDataSetChanged();
                        }
                    });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list1.clear();
        list.clear();
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
