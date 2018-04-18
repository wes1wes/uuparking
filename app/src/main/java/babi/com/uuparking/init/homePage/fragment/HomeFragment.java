package babi.com.uuparking.init.homePage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.AnimationSet;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.HomeParkingAdapter;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.homePage.infoCenter.appointmentHistory.AppointmentHistoryDetails;
import babi.com.uuparking.init.homePage.parkNow.BillingTime;
import babi.com.uuparking.init.homePage.parkNow.MyInfoWinAdapter;
import babi.com.uuparking.init.homePage.parkNow.PayParkingFee;
import babi.com.uuparking.init.homePage.parkNow.PlaceOrderParking;
import babi.com.uuparking.init.utils.DIYview.DIYBaseFragment;
import babi.com.uuparking.init.utils.commentUtil.AMapUtil;
import babi.com.uuparking.init.utils.commentUtil.RequsetPermissionUtil;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.NearbyCarports;
import babi.com.uuparking.init.utils.okhttpParameterObject.SearchParameterOPO;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/12/4.
 */

public class HomeFragment extends DIYBaseFragment implements AMap.OnMarkerClickListener, HomeParkingAdapter.Callback,
        AdapterView.OnItemClickListener, AMap.OnCameraChangeListener, AMap.OnMapClickListener {

    @BindView(R.id.image_fragment_home_refresh)
    ImageView imageFragmentHomeRefresh;
    @BindView(R.id.btn_fragment_home_pile)
    Button btnFragmentHomePile;
    @BindView(R.id.tv_fragment_home_search_scope_show)
    TextView tvFragmentHomeSearchScopeShow;
    @BindView(R.id.tv_fragment_home_time_length_show)
    TextView tvFragmentHomeTimeLengthShow;
    @BindView(R.id.tv_fragment_home_wait_time_show)
    TextView tvFragmentHomeWaitTimeShow;
    @BindView(R.id.image_fragment_home_location)
    ImageView imageFragmentHomeLocation;
    @BindView(R.id.image_home_parking_onoroff)
    ImageView imageHomeParkingOnoroff;
    @BindView(R.id.ll_home_parking_list)
    LinearLayout llHomeParkingList;
    @BindView(R.id.mv_fragment_home_map)
    MapView mvFragmentHomeMap;
    @BindView(R.id.lv_fragemnt_home_parking)
    ListView lvFragemntHomeParking;
    AMap aMap;
    public AMapLocationClient mLocationClient = null;
    MyInfoWinAdapter myInfoWinAdapter;
    HomeParkingAdapter homeAdapter;
    Unbinder unbinder;
    OkHttpClient client;
    Marker clickMader;
    ArrayList<Marker> parkingMaker;
    LatLng latLngcamera = new LatLng(31.2223, 121.4813);
    List<NearbyCarports> nearbyCarports;
    @BindView(R.id.tv_fragment_home_location_show)
    TextView tvFragmentHomeLocationShow;
    @BindView(R.id.tv_fragemnt_home_hide)
    ImageView tvFragemntHomeHide;

    public HomeFragment() {
    }

    //初始化定位
    @Override
    protected void initView() {
        //请求定位、相机权限
        new RequsetPermissionUtil().requestPermission(getContext(), this.getActivity());
    }

    /**
     * 设置定位参数
     */
    private AMapLocationClientOption settingLocationOption() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setWifiScan(true);
        mLocationOption.setOnceLocation(true);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        return mLocationOption;
    }

    @Override
    public int getLayoutId() {
        return R.layout.uuparking_home;
    }

    @Override
    protected void getDataFromServer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        client = new OkHttpClient();
        nearbyCarports = new ArrayList<>();
        imageHomeParkingOnoroff.setTag(8);

        tvFragmentHomeWaitTimeShow.setText(String.valueOf(waitTime));
        tvFragmentHomeSearchScopeShow.setText(String.valueOf(searchScope / 1000));
        tvFragmentHomeTimeLengthShow.setText(String.valueOf(timeLength / 60.0));

        homeAdapter = new HomeParkingAdapter(getContext(), nearbyCarports, this);
        lvFragemntHomeParking.setAdapter(homeAdapter);
        lvFragemntHomeParking.setOnItemClickListener(this);
        mvFragmentHomeMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mvFragmentHomeMap.getMap();
        }
        aMap.setMyLocationStyle(setMyLocationStyle(MyLocationStyle.LOCATION_TYPE_LOCATE));//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
//        aMap.getUiSettings().setScaleControlsEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.020000d, 121.400000d), 14.0f));
        myInfoWinAdapter = new MyInfoWinAdapter(getContext());
        aMap.setInfoWindowAdapter(myInfoWinAdapter);
        addmaker();
        mLocationClient = new AMapLocationClient(getActivity().getBaseContext());
        mLocationClient.setLocationListener(mLocationListener);
        //设置定位参数
        mLocationClient.setLocationOption(settingLocationOption());
        //启动定位
        mLocationClient.startLocation();
        return rootView;
    }

    /**
     * 定位样式
     */
    private MyLocationStyle setMyLocationStyle(int i) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_car));
        myLocationStyle.myLocationType(i);
        return myLocationStyle;
    }

    /**
     * 定位结果监听
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    tvFragmentHomeLocationShow.setText(aMapLocation.getAddress());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 14.0f));
                    aMap.setMyLocationStyle(setMyLocationStyle(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
                } else {
                    String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                    dealLocationError(aMapLocation.getErrorCode());
                }
            }
        }
    };

    /**
     * 定位失败可能原因
     */
    private void dealLocationError(int errorCode) {
        switch (errorCode) {
            case 11:
                Toast.makeText(getContext(), "定位失败,请检查是否安装SIM卡", Toast.LENGTH_SHORT).show();
                break;
            case 12:
                Toast.makeText(getContext(), "定位失败,缺少定位权限", Toast.LENGTH_SHORT).show();
                break;
            case 13:
                Toast.makeText(getContext(), "定位失败,检查GPS是否开启", Toast.LENGTH_SHORT).show();
                break;
            case 14:
                Toast.makeText(getContext(), "定位失败,GPS信号差", Toast.LENGTH_SHORT).show();
                break;
            case 18:
                Toast.makeText(getContext(), "定位失败,请关闭飞行模式", Toast.LENGTH_SHORT).show();
                break;
            case 19:
                Toast.makeText(getContext(), "定位失败,请插上SIM卡", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 添加地图mader
     */
    private void addmaker() {
        ArrayList<MarkerOptions> listMader = new ArrayList<>();
        for (int i = 0; i < nearbyCarports.size(); i++) {
            NearbyCarports nearby = nearbyCarports.get(i);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(nearby.getCarparkLocation().getLatitude(), nearby.getCarparkLocation()
                            .getLongitude())).title(nearby.getId());
            if (nearby.getCarparkType() == 2) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pile_parking));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.general_parking));
            }
            listMader.add(markerOptions);
        }
        parkingMaker = aMap.addMarkers(listMader, false);
    }

    /**
     * 清除地图mader车位图标
     */
    private void removeMader() {
        List<Marker> markers = aMap.getMapScreenMarkers();
        if (markers == null || markers.size() < 1) {
            return;
        }
        for (int x = 1; x < markers.size(); x++) {
            DecimalFormat df = new DecimalFormat("#.000000");
            markers.get(x).remove();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationClient.startLocation();

        mvFragmentHomeMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvFragmentHomeMap.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    /**
     * 地图滑动完成处理
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng latLngUp = cameraPosition.target;
        if (AMapUtils.calculateLineDistance(latLngcamera, latLngUp) > 500) {
            getNearbyCarports(cameraPosition.target);
            removeMader();
        }
        latLngcamera = latLngUp;
        Log.e("aaaaa", "地图滑动监听完成" + latLngcamera.toString());
    }

    /**
     * 请求获取附近车位
     */
    private void getNearbyCarports(LatLng latLng) {
        SearchParameterOPO searchParameterOPO = new SearchParameterOPO();
        searchParameterOPO.setLatitude(AMapUtil.laLngTosix(latLng.latitude) + "");
        searchParameterOPO.setLongitude(AMapUtil.laLngTosix(latLng.longitude) + "");
        searchParameterOPO.setCarparkType(isPile);
        searchParameterOPO.setSearchScope(searchScope);
        searchParameterOPO.setExpectParkLength(timeLength);
        searchParameterOPO.setDelayLength(waitTime);
        searchParameterOPO.setSearchType("J");
        RequestBody body = RequestBody.create(UrlAddress.JSON,
                new Gson().toJson(searchParameterOPO));

        Request request = new Request.Builder().url(UrlAddress.UUPgetAroundCarparkInShareInfoUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("home", s);
                jsonToNearby(s);
            }
        });

    }

    /**
     * 解析json附近车位
     */
    private void jsonToNearby(String s) {
        if (!nearbyCarports.isEmpty()) {
            nearbyCarports.clear();
        }
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("J_SEARCH_SUC")) {
                JSONObject object = jsonObject.getJSONObject("resultMap");
                JSONArray array = object.getJSONArray("carparkList");
                nearbyCarports.addAll(NearbyCarports.arrayNearbyCarportsFromData(array.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateToastUI(null, 4, null);
    }

    /**
     * marker点击事件处理
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        int i = 0;
        while (!marker.getTitle().toString().equals(nearbyCarports.get(i).getId())) {
            i++;
        }
        myInfoWinAdapter.setParkingData(nearbyCarports.get(i), timeLength, mLocationClient.getLastKnownLocation().getLatitude(), mLocationClient.getLastKnownLocation().getLongitude());
//        marker.setAnimation(scaleAnimationMaker());
        clickMader = marker;
        clickMader.showInfoWindow();
        return true;
    }

    /**
     * 放大图标动画
     */
    private AnimationSet scaleAnimationMaker() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.5f, 0.5f, 1.5f);
        //3秒完成动画
        scaleAnimation.setDuration(3000);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(scaleAnimation);
        return animationSet;
    }

    /**
     * 地图点击事件处理
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (clickMader != null) {
            clickMader.hideInfoWindow();
        }
    }

    /**
     * 页面控件点击事件处理
     */
    @OnClick({R.id.image_home_parking_onoroff, R.id.imagebtn_fragment_home_time_length_add,
            R.id.image_fragment_home_refresh, R.id.btn_fragment_home_pile,
            R.id.imagebtn_fragment_home_time_length_less, R.id.imagebtn_fragment_home_search_scope_add,
            R.id.imagebtn_fragment_home_search_scope_less, R.id.imagebtn_fragment_home_wait_time_less,
            R.id.imagebtn_fragment_home_wait_time_add, R.id.image_fragment_home_location})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.image_home_parking_onoroff:
                adjustListHeight();
                break;
            case R.id.image_fragment_home_refresh:
                refreshRotatingAnimation();
                getNearbyCarports(latLngcamera);
                break;
            case R.id.btn_fragment_home_pile:
                selectIsPile();
                getNearbyCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_home_time_length_add:
                if (timeLength >= 24 * 60) {
                    Toast.makeText(getContext(), "最长的时间就是24小时！", Toast.LENGTH_SHORT).show();
                    return;
                }
                timeLength = timeLength + 30;
                tvFragmentHomeTimeLengthShow.setText(timeLength / 60.0 + "");
                getNearbyCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_home_time_length_less:
                if (timeLength <= 30) {
                    Toast.makeText(getContext(), "还是留点时间吧！", Toast.LENGTH_SHORT).show();
                    return;
                }
                timeLength = timeLength - 30;
                tvFragmentHomeTimeLengthShow.setText(timeLength / 60.0 + "");
                getNearbyCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_home_search_scope_add:
                if (searchScope >= 10000) {
                    Toast.makeText(getContext(), "求你别再大了！", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchScope = searchScope + 1000;
                tvFragmentHomeSearchScopeShow.setText(searchScope / 1000 + "");
                getNearbyCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_home_search_scope_less:
                if (searchScope <= 1000) {
                    Toast.makeText(getContext(), "再小就找不到车位了！", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchScope = searchScope - 1000;
                tvFragmentHomeSearchScopeShow.setText(searchScope / 1000 + "");
                getNearbyCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_home_wait_time_less:
                if (waitTime <= 0) {
                    Toast.makeText(getContext(), "已经最小了，！", Toast.LENGTH_SHORT).show();
                    return;
                }
                waitTime = waitTime - 5;
                tvFragmentHomeWaitTimeShow.setText(waitTime + "");
                getNearbyCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_home_wait_time_add:
                if (waitTime >= 15) {
                    Toast.makeText(getContext(), "已经最大了，约个车位吧！", Toast.LENGTH_SHORT).show();
                    return;
                }
                waitTime = waitTime + 5;
                tvFragmentHomeWaitTimeShow.setText(waitTime + "");
                getNearbyCarports(latLngcamera);
                break;
            case R.id.image_fragment_home_location:
                // 重新定位
                Animation scale = AnimationUtils.loadAnimation(getContext(), R.anim.scale_anim);
                aMap.setMyLocationStyle(setMyLocationStyle(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE));
                if (scale != null) {
                    imageFragmentHomeLocation.startAnimation(scale);
                } else {
                    imageFragmentHomeLocation.setAnimation(scale);
                    imageFragmentHomeLocation.startAnimation(scale);
                }
                mLocationClient.startLocation();
                break;
        }
    }

    /**
     * 旋转动画
     */
    private void refreshRotatingAnimation() {
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        if (rotate != null) {
            imageFragmentHomeRefresh.startAnimation(rotate);
        } else {
            imageFragmentHomeRefresh.setAnimation(rotate);
            imageFragmentHomeRefresh.startAnimation(rotate);
        }
    }

    /**
     * 动态改变列表高度
     */
    private void adjustListHeight() {
        if ((int) imageHomeParkingOnoroff.getTag() == 8) {
            llHomeParkingList.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 8.0f));
            imageHomeParkingOnoroff.setTag(1);
            imageHomeParkingOnoroff.setImageResource(R.mipmap.list_down);
        } else {
            llHomeParkingList.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 2.0f));
            imageHomeParkingOnoroff.setTag(8);
            imageHomeParkingOnoroff.setImageResource(R.mipmap.list_up);
        }
    }

    /**
     * 选择充电桩车位
     */
    private void selectIsPile() {
        if (isPile == 2) {
            btnFragmentHomePile.setBackgroundResource(R.mipmap.index_info_bg);
            isPile = 1;
            return;
        }
        btnFragmentHomePile.setBackgroundResource(R.mipmap.index_info_bg1);
        isPile = 2;
    }

    private byte isPile = 1;
    private int timeLength = 30;//分钟
    private int waitTime = 0;//分钟
    private int searchScope = 2000;//米

    /**
     * 点击列表车位，该车位地图中心点
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NearbyCarports nearby = nearbyCarports.get(position);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(nearby.getCarparkLocation().getLatitude(), nearby.getCarparkLocation().getLongitude()), 14.0f));
    }

    @Override
    public void click(View v) {

        NearbyCarports nearby = nearbyCarports.get((Integer) v.getTag());
        if (v.getId() == R.id.image_item_home_lv_parking_stop) {
            requestOrderParking(nearby);
            Log.e("点击停车按钮1", nearby.getLockId());
        }

        Log.e("点击停车按钮", nearby.getId());
    }

    /**
     * 用户下订单
     */
    private void requestOrderParking(final NearbyCarports nearby) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(getContext(), "userId"));
        map.put("carparkId", nearby.getId());
        map.put("expectParkLength", timeLength);
        map.put("shareInfoId", nearby.getCarparkShareInfo().getId());
        map.put("longitude", mLocationClient.getLastKnownLocation() == null ? latLngcamera.longitude : mLocationClient.getLastKnownLocation().getLongitude());
        map.put("latitude", mLocationClient.getLastKnownLocation() == null ? latLngcamera.latitude : mLocationClient.getLastKnownLocation().getLatitude());
        RequestBody body = RequestBody.create(UrlAddress.JSON,
                new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPstartParkingUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                dealResult(string, nearby);

            }
        });
    }

    /**
     * 下单结果处理
     */
    private void dealResult(String string, NearbyCarports nearby) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(string);
            String code = jsonObject.getString("code");
            if (code.equals("J002")) {
                updateToastUI("下单失败", 0, null);
                return;
            }
            JSONObject object = jsonObject.getJSONObject("resultMap");
            Bundle bundle = new Bundle();
            switch (code) {
                case "J001":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", nearby.getId());
                    intentPlaceOrderParking(bundle);
                    break;
                case "JE01":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    updateToastUI(null, 1, bundle);
                case "JE04":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    updateToastUI(null, 1, bundle);
                    break;
                case "Y003":
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    bundle.putString("message", "您有一个预约订单，现在要取消吗？");
                    updateToastUI(null, 5, bundle);
                    break;
                case "JE05":
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    bundle.putString("message", "你预约了另一个车位快要到时间，如果您想现在立即停车，请先取消预约！");
                    updateToastUI(null, 5, bundle);
                    break;
                case "JE06":
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    bundle.putString("message", "你预约了该车位，现在就要停车吗？");
                    updateToastUI(null, 5, bundle);
                    break;
                case "JE02":
                    //计时
                    Bundle bundleBill = new Bundle();
                    bundleBill.putString("orderId", object.getString("orderId"));
                    bundleBill.putString("carparkDetail", object.getJSONObject("carparkResult").toString());
                    bundleBill.putString("lockId", object.getJSONObject("carparkResult").getString("lockId"));
                    bundleBill.putString("downLockTime", object.getString("downLockTime"));
                    updateToastUI(null, 2, bundleBill);
                    break;
                case "JE03":
                    //结费
                    Bundle bundleIntent = new Bundle();
                    bundleIntent.putString("costDetails", object.toString());
                    updateToastUI(null, 3, bundleIntent);
                    break;
            }
            updateToastUI(jsonObject.getString("message"), 0, null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * intent准备停车
     */
    private void intentPlaceOrderParking(Bundle bundle) {
        Intent intent = new Intent(getContext(), PlaceOrderParking.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    /**
     * 更新UI
     */
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

    Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    OrderDialog.normalDialogResult(mContext, "你存在一个未完成订单", msg.getData(), PlaceOrderParking.class);
                    break;
                case 2:
                    OrderDialog.normalDialogResult(mContext, "你存在一个未完成订单", msg.getData(), BillingTime.class);
                    break;
                case 3:
                    OrderDialog.normalDialogResult(mContext, "你存在一个未完成订单", msg.getData(), PayParkingFee.class);
                    break;
                case 4:
                    updateUIlistView();
                    break;
                case 5:
                    final String appontId =(String) msg.getData().getString("planOrderId");
                    OrderDialog.normalDialogPositiveUncancle(mContext,  msg.getData().getString("message"), new OrderDialog.Callback() {
                        @Override
                        public void click() {
                            Intent intent = new Intent(mContext, AppointmentHistoryDetails.class);
                            intent.putExtra("appointId", appontId);
                            startActivity(intent);
                        }
                    },true);
                    break;
            }
            return false;
        }
    });

    /**
     * 更新附近车位
     */
    private void updateUIlistView() {
        removeMader();
        addmaker();
        homeAdapter.notifyDataSetChanged();
        if (nearbyCarports.size() >= 1) {
            tvFragemntHomeHide.setVisibility(View.INVISIBLE);
        } else {
            tvFragemntHomeHide.setVisibility(View.VISIBLE);
        }
    }
}
