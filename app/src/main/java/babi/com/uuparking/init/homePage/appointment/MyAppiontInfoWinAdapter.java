package babi.com.uuparking.init.homePage.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
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

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.gsonFormatObject.NearbyCarports;
import babi.com.uuparking.init.homePage.parkNow.BillingTime;
import babi.com.uuparking.init.homePage.parkNow.PayParkingFee;
import babi.com.uuparking.init.homePage.parkNow.PlaceOrderParking;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
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

public class MyAppiontInfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {

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
    private String expectStartParkTime;
    private double orderDeposit;
    private ApointOrderPopup apointOrderPopup;

    public MyAppiontInfoWinAdapter(Context mContext) {
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
        btnHomeSure.setText("预约");
        btnHomeSure.setOnClickListener(this);

        tvHomeLockId.setText(nearbyCarports.getLockId());
        tvHomeLocation.setText(nearbyCarports.getCarparkLocation().getCity() + nearbyCarports.getCarparkLocation().getDetailAddress());
        tvHomeMessage.setText(nearbyCarports.getCarparkType()==2?"充电桩车位":"普通车位");

        if ( nearbyCarports.getCarparkShareInfo()==null){
            tvHomeUnitprice.setText("暂无");
            tvHomeTime.setText("暂不共享");
        }else {
            tvHomeUnitprice.setText(nearbyCarports.getCarparkShareInfo().getUnitprice() + "");
            tvHomeTime.setText(nearbyCarports.getCarparkShareInfo().getStartTime()
                    + "--" + nearbyCarports.getCarparkShareInfo().getEndTime());
        }
        return view;
    }
    public void setParkingData(NearbyCarports nearbyCarports,int timelenth,String expectStartParkTime,double orderDeposit, double latitude, double longitude) {
        this.nearbyCarports = nearbyCarports;
        latLng = new LatLng(latitude, longitude);
        this.timelenth=timelenth;
        this.expectStartParkTime=expectStartParkTime;
        this.orderDeposit=orderDeposit;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home_sure:
//               apointOrderPopup= new ApointOrderPopup(mContext);
//               apointOrderPopup.showAsDropDown(btnHomeSure,0,0, Gravity.BOTTOM);
                btnHomeSure.setClickable(false);
                break;
        }

    }

    private OkHttpClient client;
    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.arg1) {
                case 0:
                    Toast.makeText(mContext, message.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private void requestOrderParking() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(mContext,"userId"));
        map.put("carparkId", nearbyCarports.getId());
        map.put("longitude", latLng.longitude);
        map.put("latitude", latLng.latitude);
        map.put("expectParkLength", timelenth);
        map.put("expectStartParkTime", expectStartParkTime);
        map.put("orderDeposit", orderDeposit);
        RequestBody body = RequestBody.create(UrlAddress.JSON,
                new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPcreateOrderUrl).post(body).build();
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
                Logger.e(string);
//                dealResult(string);
            }
        });
    }

    private void dealResult(String string) {
        JSONObject jsonObject = null;
        try {
            Logger.e(string, jsonObject);
            jsonObject = new JSONObject(string);
            Bundle bundle=new Bundle();
            JSONObject object=jsonObject.getJSONObject("resultMap");
            switch (jsonObject.getString("code")){
                case "1":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", nearbyCarports.getId());
                    intentPlaceOrderParking(bundle);
                    break;
                case "0":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    intentPlaceOrderParking(bundle);
                    break;
                case "4":
                    //计时
                    Intent intentBilling = new Intent(mContext, BillingTime.class);
                    Bundle bundleBill = new Bundle();
                    bundleBill.putString("orderId", object.getString("orderId"));
                    bundleBill.putString("carparkDetail", object.getJSONObject("carparkResult").toString());
                    bundleBill.putString("carparkId",  object.getString("carparkId"));
                    bundleBill.putString("downLockTime", object.getString("downLockTime"));
                    intentBilling.putExtra("bundle", bundleBill);
                    mContext.startActivity(intentBilling);
                    break;
                case "5":
                    //结费
                    Bundle bundleIntent = new Bundle();
                    bundleIntent.putString("money", object.getString("cost"));
                    bundleIntent.putString("payDetails",object.getJSONArray("normalParking").toString());
                    bundleIntent.putString("orderId", object.getString("orderId"));
                    bundleIntent.putString("normalTime", object.getString("normalTime"));
                    bundleIntent.putString("carparkId", object.getString("carparkId"));
                    Intent intentPay = new Intent(mContext, PayParkingFee.class);
                    intentPay.putExtra("bundle", bundleIntent);
                    mContext.startActivity(intentPay);
                    break;
            }
            updateToastUI(jsonObject.getString("message"), 0);

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

    private void updateToastUI(String mage, int i) {
        Message message = new Message();
        message.arg1 = i;
        Bundle bun = new Bundle();
        bun.putString("message", mage);
        message.setData(bun);
        mhandler.sendMessage(message);
    }
}
