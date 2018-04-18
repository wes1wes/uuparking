package babi.com.uuparking.init.homePage.parkNow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.ShareAdapter;
import babi.com.uuparking.init.utils.commentUtil.FinalConstant;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
import babi.com.uuparking.init.utils.dialog.WaitingDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.CarportDetailsGson;
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
 * Created by bgd on 2018/1/18.
 */

public class PlaceOrderParking extends AppCompatActivity implements INaviInfoCallback {


    @BindView(R.id.tv_home_parking_time)
    TextView tvHomeParkingTime;
    @BindView(R.id.tv_home_parking_location)
    TextView tvHomeParkingLocation;
    @BindView(R.id.tv_home_parking_location_show)
    TextView tvHomeParkingLocationShow;
    @BindView(R.id.tv_home_parking_id)
    TextView tvHomeParkingId;
    @BindView(R.id.tv_home_parking_id_show)
    TextView tvHomeParkingIdShow;
    @BindView(R.id.tv_home_parking_keep_time)
    TextView tvHomeParkingKeepTime;
    @BindView(R.id.tv_home_parking_type)
    TextView tvHomeParkingType;
    @BindView(R.id.tv_home_parking_type_show)
    TextView tvHomeParkingTypeShow;
    @BindView(R.id.btn_home_cancle_parking)
    Button btnHomeCancleParking;
    @BindView(R.id.btn_home_no_parking)
    Button btnHomeNoParking;
    @BindView(R.id.btn_home_parking_nagitive)
    Button btnHomeParkingNagitive;
    @BindView(R.id.btn_home_start_parking)
    Button btnHomeStartParking;
    @BindView(R.id.image_home_parking_photo)
    ImageView imageHomeParkingPhoto;
    OkHttpClient client;
    Bundle bundle;
    int clickNum = 1;
    boolean finalTry = false;

    ShareAdapter shareAdapter;
    List<ShareInfoGson> list;
    @BindView(R.id.lv_home_parking_price)
    ListView lvHomeParkingPrice;
    @BindView(R.id.tv_home_parking_order_id_show)
    TextView tvHomeParkingOrderIdShow;
    @BindView(R.id.textView34)
    TextView textView34;
    @BindView(R.id.tv_home_parking_order_id)
    TextView tvHomeParkingOrderId;
    @BindView(R.id.tv_home_parking_usable_protocol)
    TextView tvHomeParkingUsableProtocol;
    @BindView(R.id.tv_home_parking_back)
    TextView tvHomeParkingBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_parking_layout);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        bundle = getIntent().getBundleExtra("bundle");
        list = new ArrayList<>();
        shareAdapter = new ShareAdapter(this, list);
        lvHomeParkingPrice.setAdapter(shareAdapter);
        requestCarparkDetailByCarpark();
        tvHomeParkingOrderIdShow.setText(bundle.getString("orderId"));

    }

    @OnClick({R.id.tv_home_parking_back,R.id.btn_home_cancle_parking, R.id.btn_home_no_parking,
            R.id.btn_home_parking_nagitive, R.id.btn_home_start_parking})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_home_parking_back:
                finish();
                break;
            case R.id.btn_home_cancle_parking:
                if (bundle.getString("orderId").isEmpty()) {
                    return;
                }
                requestCancleParking();
                break;
            case R.id.btn_home_no_parking:
                // TODO: 2018/1/19 无法停车
//                ToastUtil.ToastUtilmsg(this,"小由正加紧开发！");
                showUnstopCar();
                break;
            case R.id.btn_home_parking_nagitive:
                Poi end = new Poi(carportDetailsGson.getCity() +
                        carportDetailsGson.getDistrict() + carportDetailsGson.getDetailAddress(),
                        new LatLng(carportDetailsGson.getLatitude(), carportDetailsGson.getLongitude()), "");
                AmapNaviPage.getInstance().showRouteActivity(this,
                        new AmapNaviParams(null, null, end, AmapNaviType.DRIVER),
                        this);
                break;
            case R.id.btn_home_start_parking:
                if (carparkDetail == null) {
                    return;
                }
                if (clickNum >= FinalConstant.OPEN_PARKING_NUMBER) {
                    finalTry = true;
                } else {
                    finalTry = false;
                }
                requestOperationLock();
                break;
        }
    }

    /**
     * 无法停车弹框
     */
    private void showUnstopCar() {

        View popView = View.inflate(this, R.layout.home_parking_unstop_popup, null);

        Button callLock = (Button) popView.findViewById(R.id.btn_parking_pop_call_lock);
        Button lockMalfunction = (Button) popView.findViewById(R.id.btn_parking_pop_lock_malfunction);
        Button cancelPop = (Button) popView.findViewById(R.id.btn_parking_pop_cancle);
        Button linkUU = (Button) popView.findViewById(R.id.btn_parking_pop_link_uu);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置同意在外点击消失

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_parking_pop_call_lock:
                        if (bundle.getString("orderId").isEmpty()) {
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "正在呼叫车锁", Toast.LENGTH_SHORT).show();
                        requestCallParking();
                        break;
                    case R.id.btn_parking_pop_lock_malfunction:
// TODO: 2018/3/10 报修功能 
                        break;
                    case R.id.btn_parking_pop_cancle:
                        popWindow.dismiss();
                        break;
                    case R.id.btn_parking_pop_link_uu:
// TODO: 2018/3/10 联系客服 
                        break;
                }
            }
        };

        lockMalfunction.setOnClickListener(listener);
        linkUU.setOnClickListener(listener);
        cancelPop.setOnClickListener(listener);
        callLock.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(Color.rgb(11, 22, 11));
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(imageHomeParkingPhoto, Gravity.BOTTOM,
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
//        setBackgroundAlpha(0.5f);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    CarportDetailsGson carportDetailsGson = new CarportDetailsGson();

    /**
     * 获取单个车位请求
     */
    String carparkDetail = null;

    private void requestCarparkDetailByCarpark() {
        Map<String, Object> map = new HashMap<>();
        map.put("carparkId", bundle.getString("carparkId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPgetCarparkDetailByCarparkIdUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Log.e("获取单个车位详情", string);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(string);
                    if (jsonObject.isNull("resultMap")) {
                        return;
                    }
                    JSONObject object = jsonObject.getJSONObject("resultMap");
                    carparkDetail = object.getJSONObject("carparkDetail").toString();
                    carportDetailsGson = CarportDetailsGson.objectFromData(carparkDetail);
                    if (!list.isEmpty()) {
                        list.clear();
                    }
                    list.addAll(carportDetailsGson.getShareInfo());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateViewUI();
            }
        });
    }

    private void updateViewUI() {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                shareAdapter.notifyDataSetChanged();
                tvHomeParkingIdShow.setText(carportDetailsGson.getLockId());
                tvHomeParkingLocationShow.setText(carportDetailsGson.getCity() + carportDetailsGson.getDetailAddress());
                tvHomeParkingTypeShow.setText((carportDetailsGson.getCarparkType() == 2 ? "可充电、" : "不可充电、") +
                        (carportDetailsGson.getIsOutDoor() == 1 ? "露天、" : "地下、") +
                        (carportDetailsGson.getHasColumn() == 1 ? "有立柱" : "无立柱"));

                long m = FinalConstant.ORDER_KEEP_TIME * 60 * 1000;
                if (bundle.getString("timeslot") != null) {
                    m = m - Long.valueOf(bundle.getString("timeslot"));
                }
                if (bundle.getString("planOrderId") != null) {
                    m = Long.valueOf(bundle.getString("timeslot"));
                }

                // TODO: 2018/2/2 保留时间改为动态 
                CountDownTimer timer = new CountDownTimer(m, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int m = (int) (millisUntilFinished / 60 / 1000);
                        int s = (int) (millisUntilFinished / 1000 % 60);
                        tvHomeParkingKeepTime.setText((m > 9 ? m : ("0" + m)) + ":" + (s > 9 ? s : ("0" + s)));
                    }

                    @Override
                    public void onFinish() {
                        requestCancleParking();
                    }
                }.start();
            }
        });
    }

    /**
     * 车位响铃
     */
    private void requestCallParking() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", bundle.getString("orderId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPCallLockUrl).post(body).build();
        Call call = client.newCall(request);
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

                    String code = jsonObject.getString("code");
                    if (code.equals("J301")) {
                        updateToastUI(jsonObject.getString("message"), 1);
                    }
                    if (code.equals("J302")) {
                        updateToastUI("订单错误", 1);
                        return;
                    }
                    if (code.equals("J303")) {
                        updateToastUI("响铃失败，请重试！", 1);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("222", string);
            }
        });
    }

    /**
     * 取消订单请求
     */
    private void requestCancleParking() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", bundle.getString("orderId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPcancelOrderByHandUrl).post(body).build();
        Call call = client.newCall(request);
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
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("取消订单", "" + string);
            }
        });
    }

    /**
     * 降下车锁请求
     */
    private void requestOperationLock() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", bundle.getString("orderId"));
        map.put("finalTry", finalTry);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPopenLockUrl).post(body).build();

        WaitingDialog.showWaitingDialog(this);
        Call call = client.newBuilder().connectTimeout(FinalConstant.REQUESTTIMEDOUT, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                WaitingDialog.cancelWaitingDialog();
                updateToastUI("连接车锁失败！请重试", 1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Log.e("打开车锁", string);
                dealResult(string);
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }

    /**
     * 处理开锁结果
     */
    private void dealResult(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String code = jsonObject.getString("code");

            if (code.equals("J102")) {
                updateToastUI("网络连接错误！", 1);
                return;
            }
            if (code.equals("J103")) {
                updateToastUI("车锁操作失败，请重试！", 1);
                clickNum++;
                return;
            }
            if (code.equals("J104")) {
                updateToastUI("车锁可能发生故障，请选择其他车位停放！", 2);
                return;
            }
            if (code.equals("J101")) {
                Intent intent = new Intent(getBaseContext(), BillingTime.class);
                Bundle bundleBill = new Bundle();
                bundleBill.putString("orderId", bundle.getString("orderId"));
                bundleBill.putString("carparkDetail", carparkDetail);
                bundleBill.putString("carparkId", bundle.getString("carparkId"));
                intent.putExtra("bundle", bundleBill);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateToastUI(final String message, final Integer i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (i) {
                    case 1:
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        OrderDialog.normalDialogPositiveNext(PlaceOrderParking.this, "车锁故障，请重新选择车位停车", new OrderDialog.Callback() {
                            @Override
                            public void click() {
                                PlaceOrderParking.this.finish();
                            }
                        });
                        break;
                }

            }
        });
    }

    /**
     * 导航组件实现方法
     */
    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {
//      AmapNaviPage
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

}
