package babi.com.uuparking.init.homePage.infoCenter.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import babi.com.uuparking.adapter.TimelineAdapter;
import babi.com.uuparking.init.homePage.parkNow.BillingTime;
import babi.com.uuparking.init.homePage.parkNow.PayParkingFee;
import babi.com.uuparking.init.homePage.parkNow.PlaceOrderParking;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.HistoryGson;
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
 * 作者：BGD
 * Created by b on 2018/1/8.
 */

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.my_history_listview)
    ListView myHistoryListview;
    @BindView(R.id.srl_my_history)
    SmartRefreshLayout srlMyHistory;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    private TimelineAdapter timelineAdapter;
    boolean endhistory = false;
    int i = 1;
    OkHttpClient okHttpClient;
    Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_history_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("停车记录");
        okHttpClient = new OkHttpClient();
        map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("page", 1);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        okHttpPost(body);
        timelineAdapter = new TimelineAdapter(this, historyGsonList);
        myHistoryListview.setAdapter(timelineAdapter);
        myHistoryListview.setOnItemClickListener(this);

        srlMyHistory.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                i = 1;
                historyGsonList.clear();
                map.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
                map.put("page", i);
                RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
                map.clear();
                okHttpPost(body);
            }
        });
        srlMyHistory.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (!endhistory) {
                    map.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
                    map.put("page", i++);
                    RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
                    map.clear();
                    okHttpPost(body);
                    refreshlayout.finishLoadmore();
                } else {
                    refreshlayout.finishLoadmore();
                    Toast.makeText(getApplicationContext(), "沒有更多数据了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    List<HistoryGson> historyGsons = new ArrayList<>();
    List<HistoryGson> historyGsonList = new ArrayList<>();

    /**
     * 请求后台服务，获取用户停车历史数据
     */
    private void okHttpPost(RequestBody body) {
//        RequestBody
        Request request = new Request.Builder()
                .url(UrlAddress.UUPparkingHistoryUrl)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("aaa", "onResponse: " + s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONObject object = jsonObject.getJSONObject("resultMap");
                    if (object == null) {
                        return;
                    }
                    jsontolist(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 从后台获取到的json数据转化为HistoryGson对象的list集合
     */
    private void jsontolist(JSONObject result) {
        try {
            JSONArray array = result.getJSONArray("data");
            if (array.length() <= 9) {
                endhistory = true;
            } else {
                endhistory = false;
            }
            historyGsons.clear();
            historyGsons = HistoryGson.arrayHistoryGsonFromData(array.toString());
            historyGsonList.addAll(historyGsons);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    srlMyHistory.finishRefresh();
                    srlMyHistory.finishLoadmore();
                    timelineAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HistoryGson history = historyGsonList.get(position);
        switch (history.getStatus()) {
            case 4:
                if (history.getCost() != null) {
                    Intent intent = new Intent(getBaseContext(), HistoryDetails.class);
                    intent.putExtra("id", history.getOrderId());
                    startActivity(intent);
                }
                break;
            default:
                internetUserStatus();
//                ToastUtil.ToastUtilmsg(this, "小由正加紧开发！！");
                break;
        }

    }

    /**
     * 请求获取用户当前状态
     */
    private void internetUserStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UserUncompledteOrder).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("用户当前订单状态", "onResponse: " + str);
                dealResult(str);
            }
        });
    }

    /**
     * 处理用户状态返回结果
     */
    private void dealResult(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            Bundle bundle = new Bundle();
            String code = jsonObject.getString("code");
            if (code.equals("JC02")) {
                updateToastUI(jsonObject.getString("message"));
                return;
            }
            JSONObject object = jsonObject.getJSONObject("resultMap");
            switch (code) {
                case "JE01":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    intentPlaceOrderParking(bundle);
                    break;
                case "JE04":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    intentPlaceOrderParking(bundle);
                    break;
                case "JE02":
                    //计时
                    Intent intentBilling = new Intent(getBaseContext(), BillingTime.class);
                    Bundle bundleBill = new Bundle();
                    bundleBill.putString("orderId", object.getString("orderId"));
                    bundleBill.putString("carparkDetail", object.getJSONObject("carparkResult").toString());
                    bundleBill.putString("lockId", object.getJSONObject("carparkResult").getString("lockId"));
                    bundleBill.putString("downLockTime", object.getString("downLockTime"));
                    intentBilling.putExtra("bundle", bundleBill);
                    startActivity(intentBilling);
                    break;
                case "JE03":
                    //结费
                    Bundle bundleIntent = new Bundle();
                    bundleIntent.putString("costDetails", object.toString());
                    Intent intentPay = new Intent(getBaseContext(), PayParkingFee.class);
                    intentPay.putExtra("bundle", bundleIntent);
                    startActivity(intentPay);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.ToastUtilmsg(getBaseContext(), message);
            }
        });
    }

    /**
     * 跳转订单页面
     */
    private void intentPlaceOrderParking(Bundle bundle) {
        Intent intent = new Intent(this, PlaceOrderParking.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }


    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
