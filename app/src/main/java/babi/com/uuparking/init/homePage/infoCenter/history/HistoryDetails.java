package babi.com.uuparking.init.homePage.infoCenter.history;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.ShareAdapter;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.HistoryDetailsGson;
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
 * Created by b on 2018/1/27.
 */

public class HistoryDetails extends AppCompatActivity {
    @BindView(R.id.tv_mine_history_details_carport_id)
    TextView tvMineHistoryDetailsCarportId;
    @BindView(R.id.tv_mine_history_details_address)
    TextView tvMineHistoryDetailsAddress;
    @BindView(R.id.tv_mine_history_details_unpice)
    TextView tvMineHistoryDetailsUnpice;
    @BindView(R.id.tv_mine_history_details_start_time)
    TextView tvMineHistoryDetailsStartTime;
    @BindView(R.id.tv_mine_history_details_end_time)
    TextView tvMineHistoryDetailsEndTime;
    @BindView(R.id.tv_mine_history_details_carport_id_show)
    TextView tvMineHistoryDetailsCarportIdShow;
    @BindView(R.id.tv_mine_history_details_address_show)
    TextView tvMineHistoryDetailsAddressShow;
    @BindView(R.id.tv_mine_history_details_start_time_show)
    TextView tvMineHistoryDetailsStartTimeShow;
    @BindView(R.id.tv_mine_history_details_end_time_show)
    TextView tvMineHistoryDetailsEndTimeShow;
    @BindView(R.id.tv_mine_history_details_time)
    TextView tvMineHistoryDetailsTime;
    @BindView(R.id.tv_mine_history_details_time_show)
    TextView tvMineHistoryDetailsTimeShow;
    @BindView(R.id.tv_mine_history_details_pay)
    TextView tvMineHistoryDetailsPay;
    @BindView(R.id.tv_mine_history_details_pay_show)
    TextView tvMineHistoryDetailsPayShow;
    @BindView(R.id.tv_mine_history_details_pay_way)
    TextView tvMineHistoryDetailsPayWay;
    @BindView(R.id.tv_mine_history_details_pay_way_show)
    TextView tvMineHistoryDetailsPayWayShow;
    @BindView(R.id.tv_mine_history_details_order_id)
    TextView tvMineHistoryDetailsOrderId;
    @BindView(R.id.tv_mine_history_details_order_id_show)
    TextView tvMineHistoryDetailsOrderIdShow;
    @BindView(R.id.lv_mine_history_details_price_time)
    ListView lvMineHistoryDetailsPriceTime;
    OkHttpClient client;
    ShareAdapter shareAdapter;
    List<ShareInfoGson> list;
    HistoryDetailsGson historyDetailsGson;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_history_details_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("停车详情");
        String id = getIntent().getStringExtra("id");
        client = new OkHttpClient();
        list = new ArrayList<>();
        shareAdapter = new ShareAdapter(this, list);
        lvMineHistoryDetailsPriceTime.setAdapter(shareAdapter);
        requestHistoryDetails(id);
    }

    private void requestHistoryDetails(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPparkingHistoryDetailsUrl).post(body).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();
                Logger.e("sss", string);
                dealResult(string);
            }
        });
    }

    private void dealResult(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONObject object = jsonObject.getJSONObject("resultMap");
            JSONObject orderParkingDetail = object.getJSONObject("orderParkingDetail");
            historyDetailsGson = HistoryDetailsGson.objectFromData(orderParkingDetail.toString());
            updateUI(historyDetailsGson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(final HistoryDetailsGson historyDetails) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMineHistoryDetailsAddressShow.setText(historyDetails.getLocation().getCity()
                        + historyDetails.getLocation().getDistrict() +
                        historyDetails.getLocation().getDetailAddress());
                tvMineHistoryDetailsCarportIdShow.setText(historyDetails.getCarparkId());
                tvMineHistoryDetailsOrderIdShow.setText(historyDetails.getOrderId());
                tvMineHistoryDetailsEndTimeShow.setText(historyDetails.getEndParkTime());
                tvMineHistoryDetailsStartTimeShow.setText(historyDetails.getStartParkTime());
                tvMineHistoryDetailsPayShow.setText(historyDetails.getCost() + "");

                int ss = 0;
                try {
                    ss = TimeCompare.DateCompareAllss(historyDetails.getEndParkTime(), historyDetails.getStartParkTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int h = ss / 60 / 60;
                int m = ss / 60 % 60;
                int s = ss % 60;
                tvMineHistoryDetailsTimeShow.setText((h > 9 ? h : ("0" + h)) + "时"
                        + (m > 9 ? m : ("0" + m)) + "分" + (s > 9 ? s : ("0" + s)) + "秒");
                if (!list.isEmpty()) {
                    list.clear();
                }
                list.addAll(historyDetails.getCarparkShareInfos());
                shareAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
