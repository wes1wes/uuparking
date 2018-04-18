package babi.com.uuparking.init.homePage.parkNow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.homePage.infoCenter.appointmentHistory.AppointmentHistoryDetails;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.NearbyCarports;
import babi.com.uuparking.init.utils.gsonFormatObject.ShareInfoGson;
import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2018/1/17.
 */

public class MyInfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {

    private TextView tvHomeLocation;
    private Button btnHomeSure;
    TextView tvHomeUnitprice;
    TextView tvHomeTime;
    TextView tvHomeMessage;
    TextView tvHomeLockId;
    NearbyCarports nearbyCarports;

    private Context mContext;
    private List<NearbyCarports> list;
    private LatLng latLng;
    private int timelenth;

    public MyInfoWinAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        client = new OkHttpClient();
        initData(marker);
        return initView();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @SuppressLint("SetTextI18n")
    private View initView() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.home_parking_popup, null);
        btnHomeSure = view.findViewById(R.id.btn_home_sure);
        tvHomeLocation = view.findViewById(R.id.tv_home_location);
        tvHomeTime = view.findViewById(R.id.tv_home_time_show);
        tvHomeUnitprice = view.findViewById(R.id.tv_home_unitprice_show);
        tvHomeMessage = view.findViewById(R.id.tv_home_message_show);
        tvHomeLockId = view.findViewById(R.id.tv_home_lock_id_show);
        btnHomeSure.setOnClickListener(this);

        tvHomeLockId.setText(nearbyCarports.getLockId());
        tvHomeLocation.setText(nearbyCarports.getCarparkLocation().getCity() + nearbyCarports.getCarparkLocation().getDetailAddress());
        tvHomeMessage.setText((nearbyCarports.getCarparkType() == 2 ? "可充电+" : "不可充电+") +
                (nearbyCarports.getIsOutdoor() == 1 ? "露天+" : "地下+") +
                (nearbyCarports.getHasColumn() == 1 ? "有立柱" : "无立柱"));

        if (nearbyCarports.getCarparkShareInfo() == null) {
            tvHomeUnitprice.setText("暂无");
            tvHomeTime.setText("暂不共享");
        } else {
            tvHomeUnitprice.setText(nearbyCarports.getCarparkShareInfo().getUnitprice() + "");
            tvHomeTime.setText(nearbyCarports.getCarparkShareInfo().getStartTime()
                    + "--" + nearbyCarports.getCarparkShareInfo().getEndTime());

        }
        return view;
    }

    public void setParkingData(NearbyCarports nearbyCarports, int timelenth, double latitude, double longitude) {
        this.nearbyCarports = nearbyCarports;
        latLng = new LatLng(latitude, longitude);
        this.timelenth = timelenth;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home_sure:
                requestOrderParking();
                btnHomeSure.setClickable(false);
                break;
        }

    }

    private OkHttpClient client;

    private void requestOrderParking() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(mContext, "userId"));
        map.put("carparkId", nearbyCarports.getId());
        map.put("shareInfoId", nearbyCarports.getCarparkShareInfo().getId());
        map.put("longitude", latLng.longitude);
        map.put("latitude", latLng.latitude);
        map.put("expectParkLength", timelenth);
        RequestBody body = RequestBody.create(UrlAddress.JSON,new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPstartParkingUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                btnHomeSure.setClickable(true);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                btnHomeSure.setClickable(true);
                dealResult(string);
            }
        });
    }

    private void dealResult(String string) {
        JSONObject jsonObject = null;
        try {
            Logger.e(string, jsonObject);
            jsonObject = new JSONObject(string);
            String code = jsonObject.getString("code");
            if (code.equals("J002")) {
                updateToastUI("下单失败", 0, null);
                return;
            }
            if (code.equals("J003")) {
                updateToastUI("车位被占用", 0, null);
                return;
            }
            Bundle bundle = new Bundle();
            JSONObject object = jsonObject.getJSONObject("resultMap");
            switch (code) {
                case "J001":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", nearbyCarports.getId());
                    intentPlaceOrderParking(bundle);
                    break;
                case "JE01":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    updateToastUI(null, 1, bundle);
                    break;
                case "JE04":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    updateToastUI(null, 1, bundle);
                    break;
                case "JE05":
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    bundle.putString("message", "你预约了另一个车位快要到时间，如果您想现在立即停车，请先取消预约！");
                    updateToastUI(null, 4, bundle);
                    break;
                case "JE06":
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    bundle.putString("message", "你预约了该车位，现在就要停车吗？");
                    updateToastUI(null, 4, bundle);
                    break;
                case "Y003":
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    bundle.putString("message","您有一个预约订单，现在要取消吗？" );
                    updateToastUI(null, 4, bundle);
                    break;
                case "JE02":
                    //计时
                    Bundle bundleBill = new Bundle();
                    bundleBill.putString("orderId", object.getString("orderId"));
                    bundleBill.putString("carparkDetail", object.getJSONObject("carparkResult").toString());
                    bundleBill.putString("carparkId", object.getString("carparkId"));
                    bundleBill.putString("downLockTime", object.getString("downLockTime"));
                    updateToastUI(null, 2, bundleBill);
                    break;
                case "JE03":
                    //结费
                    Bundle bundleIntent = new Bundle();
                    bundleIntent.putString("costDetails",object.toString());
                    updateToastUI(null, 3, bundleIntent);
                    break;
            }
            updateToastUI(jsonObject.getString("message"), 0, null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("11111", "" + string);
    }

    private void intentPlaceOrderParking(Bundle bundle) {
        Intent intent = new Intent(mContext, PlaceOrderParking.class);
        intent.putExtra("bundle", bundle);
        mContext.startActivity(intent);
    }

    private void updateToastUI(String mage, int i, Bundle bundle) {
        Message message = new Message();
        message.arg1 = i;
        Bundle bun = new Bundle();
        bun.putString("message", mage);
        if (i == 0) {
            message.setData(bun);
        } else {
            message.setData(bundle);
        }

        mhandler.sendMessage(message);
    }

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.arg1) {
                case 0:
                    Toast.makeText(mContext, message.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    OrderDialog.normalDialogResult(mContext, "你存在一个未完成订单", message.getData(), PlaceOrderParking.class);
                    break;
                case 2:
                    OrderDialog.normalDialogResult(mContext, "你存在一个未完成订单", message.getData(), BillingTime.class);
                    break;
                case 3:
                    OrderDialog.normalDialogResult(mContext, "你存在一个未完成订单", message.getData(), PayParkingFee.class);
                    break;
                case 4:
                    final String appontId =(String) message.getData().getString("planOrderId");
                    OrderDialog.normalDialogPositiveUncancle(mContext, message.getData().getString("message"), new OrderDialog.Callback() {
                        @Override
                        public void click() {
                            Intent intent = new Intent(mContext, AppointmentHistoryDetails.class);
                            intent.putExtra("appointId", appontId);
                            mContext.startActivity(intent);
                        }
                    },true);
                    break;
            }
            return false;
        }
    });
}
