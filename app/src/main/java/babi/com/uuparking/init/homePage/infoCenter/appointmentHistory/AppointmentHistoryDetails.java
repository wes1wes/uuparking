package babi.com.uuparking.init.homePage.infoCenter.appointmentHistory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.parkNow.PlaceOrderParking;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.AppointmentHistoryDetailsGson;
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
 * Created by b on 2018/3/15.
 */

public class AppointmentHistoryDetails extends AppCompatActivity {
    @BindView(R.id.tv_mine_appoint_history_details_appoint_id)
    TextView tvMineAppointHistoryDetailsAppointId;
    @BindView(R.id.tv_mine_appoint_history_details_appoint_id_show)
    TextView tvMineAppointHistoryDetailsAppointIdShow;
    @BindView(R.id.btn_mine_appoint_history_details_start)
    Button btnMineAppointHistoryDetailsStart;
    @BindView(R.id.btn_mine_appoint_history_details_cancle)
    Button btnMineAppointHistoryDetailsCancle;
    @BindView(R.id.tv_mine_appoint_history_details_lock_id)
    TextView tvMineAppointHistoryDetailsLockId;
    @BindView(R.id.tv_mine_appoint_history_details_location)
    TextView tvMineAppointHistoryDetailsLocation;
    @BindView(R.id.tv_mine_appoint_history_details_lock_id_show)
    TextView tvMineAppointHistoryDetailsLockIdShow;
    @BindView(R.id.tv_mine_appoint_history_details_price)
    TextView tvMineAppointHistoryDetailsPrice;
    @BindView(R.id.tv_mine_appoint_history_details_carpark_info)
    TextView tvMineAppointHistoryDetailsCarparkInfo;
    @BindView(R.id.tv_mine_appoint_history_details_start_time)
    TextView tvMineAppointHistoryDetailsStartTime;
    @BindView(R.id.tv_mine_appoint_history_details_time_length)
    TextView tvMineAppointHistoryDetailsTimeLength;
    @BindView(R.id.tv_mine_appoint_history_details_location_show)
    TextView tvMineAppointHistoryDetailsLocationShow;
    @BindView(R.id.tv_mine_appoint_history_details_price_show)
    TextView tvMineAppointHistoryDetailsPriceShow;
    @BindView(R.id.tv_mine_appoint_history_details_start_time_show)
    TextView tvMineAppointHistoryDetailsStartTimeShow;
    @BindView(R.id.tv_mine_appoint_history_details_time_length_show)
    TextView tvMineAppointHistoryDetailsTimeLengthShow;
    @BindView(R.id.tv_mine_appoint_history_details_carpark_info_show)
    TextView tvMineAppointHistoryDetailsCarparkInfoShow;
    String appointId;
    OkHttpClient client;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    private AppointmentHistoryDetailsGson appointmentHistoryDetailsGson;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_appointment_history_details);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("预约详情");
        client = new OkHttpClient();
        appointId = getIntent().getStringExtra("appointId");
        if (appointId != null) {
            requestAppointHistoryDetails();
        }

    }

    /**
     * 请求预约记录详情
     */
    private void requestAppointHistoryDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", appointId);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        Request request = new Request.Builder().url(UrlAddress.UUPgetOrderPlanDetailUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("预约订单详情", s);
                dealResultJson(s);
            }
        });
    }

    /**
     * 预约记录详情结果处理
     */
    private void dealResultJson(String s) {
        try {
            JSONObject object = new JSONObject(s);
            if (!object.getString("code").equals("Y008")) {
                updateToastUI(object.getString("message"));
                return;
            }
            String order = object.getJSONObject("resultMap").getString("order");
            appointmentHistoryDetailsGson = AppointmentHistoryDetailsGson.objectFromData(order);
            updateViewUI();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Toast更新UI线程
     */
    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.ToastUtilmsg(getBaseContext(), message);
            }
        });
    }

    /**
     * 更新UI线程控件
     */
    private void updateViewUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMineAppointHistoryDetailsAppointIdShow.setText(appointmentHistoryDetailsGson.getId());
                tvMineAppointHistoryDetailsLockIdShow.setText(appointmentHistoryDetailsGson.getLockId());
                tvMineAppointHistoryDetailsLocationShow.setText(appointmentHistoryDetailsGson.getDetailAddress());
                tvMineAppointHistoryDetailsCarparkInfoShow.setText(appointmentHistoryDetailsGson.getHasColumn() + "");
                tvMineAppointHistoryDetailsStartTimeShow.setText(appointmentHistoryDetailsGson.getExpectStartParkTime());
                tvMineAppointHistoryDetailsTimeLengthShow.setText(appointmentHistoryDetailsGson.getExpectParkLength() / 60 + "");
                if (appointmentHistoryDetailsGson.getShareInfo() != null) {
                    tvMineAppointHistoryDetailsPriceShow.setText(appointmentHistoryDetailsGson.getShareInfo().getUnitprice() + "");
                }

                switch (appointmentHistoryDetailsGson.getStatus()) {
                    case 1:

                        break;
                    case 2:
                        btnMineAppointHistoryDetailsCancle.setVisibility(View.INVISIBLE);
                        btnMineAppointHistoryDetailsStart.setVisibility(View.INVISIBLE);
                        break;
                }


            }
        });
    }

    @OnClick({R.id.btn_mine_appoint_history_details_start, R.id.btn_mine_appoint_history_details_cancle,R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_mine_appoint_history_details_start:
                requestStartAppoint();
                break;
            case R.id.btn_mine_appoint_history_details_cancle:
                requestCancleAppoint();
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
        }
    }

    /**
     * 请求预约去停车
     */
    private void requestStartAppoint() {
        Map<String, Object> map = new HashMap<>();
        map.put("carparkId", appointmentHistoryDetailsGson.getCaparkId());
        map.put("planOrderId", appointId);
        map.put("userId", SpUserUtils.getString(getBaseContext(), "userId"));
        map.put("longitude", "");
        map.put("latitude", "");
        if (appointmentHistoryDetailsGson.getShareInfo() != null) {
            map.put("shareInfoId", appointmentHistoryDetailsGson.getShareInfo().getId());
        }
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        Request request = new Request.Builder().url(UrlAddress.UUPorderPlanToOrderUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("预约订单详情", s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    String ms = object.getString("message");
                    if (code.equals("Y009")) {
                        JSONObject order = object.getJSONObject("resultMap").getJSONObject("order");
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", order.getString("orderId"));
                        bundle.putString("carparkId", order.getString("carparkId"));
                        bundle.putString("timeslot", order.getString("timeslot"));
                        bundle.putString("planOrderId", appointId);
                        intentPlaceOrderParking(bundle);
                        AppointmentHistoryDetails.this.finish();
                    }
                    updateToastUI(ms);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    /**
     * 请求取消预约
     */
    private void requestCancleAppoint() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", appointId);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        Request request = new Request.Builder().url(UrlAddress.UUPcancelOrderPlanUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("预约订单详情", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("code").equals("Y006")) {
                        final String ms = object.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                OrderDialog.normalDialogPositiveNext(AppointmentHistoryDetails.this, ms, new OrderDialog.Callback() {
                                    @Override
                                    public void click() {
                                        AppointmentHistoryDetails.this.finish();
                                    }
                                });
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}