package babi.com.uuparking.init.homePage.infoCenter.earning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.EarningDetailsAdapter;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.EarningDetailsGson;
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
 * Created by b on 2017/12/22.
 */

public class Earning extends AppCompatActivity {

    OkHttpClient okHttpClient;
    @BindView(R.id.tv_my_earning_come)
    TextView tvMyEarningCome;
    @BindView(R.id.tv_my_earning_money)
    TextView tvMyEarningMoney;
    @BindView(R.id.lv_mine_earning_money_details)
    ListView lvMineEarningMoneyDetails;
    List<EarningDetailsGson> earningDetailsGsons;
    EarningDetailsAdapter earningDetailsAdapter;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_earnings_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("车位收益");
        okHttpClient = new OkHttpClient();
        earningDetailsGsons = new ArrayList<>();
        earningDetailsAdapter = new EarningDetailsAdapter(this, earningDetailsGsons);
        lvMineEarningMoneyDetails.setAdapter(earningDetailsAdapter);
        initEarnings();
    }

    private void initEarnings() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("page", 1);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPmyProfitUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("车位收益", "onResponse: " + str);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    if (!jsonObject.getString("code").equals("JP01")) {
                        updateToastUI(jsonObject.getString("message"));
                        return;
                    }
                    JSONObject resultMap = jsonObject.getJSONObject("resultMap");
                    String totalProfit = resultMap.getString("totalProfit");
                    JSONArray list = resultMap.getJSONArray("list");
                    earningDetailsGsons.clear();
                    earningDetailsGsons.addAll(EarningDetailsGson.arrayEarningDetailsGsonFromData(list.toString()));
                    updateViewUI(totalProfit);

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
                ToastUtil.ToastUtilmsg(getBaseContext(), message);
                earningDetailsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateViewUI(final String totalProfit) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMyEarningMoney.setText(totalProfit);
                earningDetailsAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
