package babi.com.uuparking.init.homePage.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.init.homePage.appointment.ApointOrderPopup;
import babi.com.uuparking.init.utils.DIYview.DIYBaseFragment;
import babi.com.uuparking.R;
import babi.com.uuparking.adapter.AppiontmentParkingAdapter;
import babi.com.uuparking.init.homePage.appointment.MyAppiontInfoWinAdapter;
import babi.com.uuparking.init.utils.gsonFormatObject.AppointmentNearbyCarpaorts;
import babi.com.uuparking.init.utils.gsonFormatObject.NearbyCarports;
import babi.com.uuparking.init.utils.okhttpParameterObject.SearchParameterOPO;
import babi.com.uuparking.init.utils.commentUtil.AMapUtil;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
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
 * Created by b on 2018/3/9.
 */

public class AppiontmentFragmentMap extends DIYBaseFragment implements AMap.OnMapClickListener, AMap.OnCameraChangeListener,
        AMap.OnMarkerClickListener, GeocodeSearch.OnGeocodeSearchListener, TextWatcher, InputtipsListener {

    @BindView(R.id.mv_fragment_appiontment_map)
    MapView mvFragmentAppiontmentMap;
    @BindView(R.id.tv_fragment_appiontment_search_scope_show)
    TextView tvFragmentAppiontmentSearchScopeShow;
    @BindView(R.id.tv_fragment_appiontment_search_scope)
    TextView tvFragmentAppiontmentSearchScope;
    @BindView(R.id.imagebtn_fragment_appiontment_search_scope_add)
    ImageView imagebtnFragmentAppiontmentSearchScopeAdd;
    @BindView(R.id.imagebtn_fragment_appiontment_search_scope_less)
    ImageView imagebtnFragmentAppiontmentSearchScopeLess;
    @BindView(R.id.imagebtn_fragment_appiontment_wait_time_less)
    ImageView imagebtnFragmentAppiontmentWaitTimeLess;
    @BindView(R.id.imagebtn_fragment_appiontment_wait_time_add)
    ImageView imagebtnFragmentAppiontmentWaitTimeAdd;
    @BindView(R.id.tv_fragment_appiontment_wait_time_show)
    TextView tvFragmentAppiontmentWaitTimeShow;
    @BindView(R.id.tv_fragment_appiontment_wait_time)
    TextView tvFragmentAppiontmentWaitTime;
    @BindView(R.id.imageView16)
    ImageView imageView16;
    Unbinder unbinder;
    AMap aMap;
    OkHttpClient client;
    List<Tip> list;
    Marker clickMader;
    ArrayList<Marker> parkingMaker;
    List<AppointmentNearbyCarpaorts> nearbyCarports;
    LatLng latLngcamera = new LatLng(31.2223, 121.4813);
    GeocodeSearch geocoderSearch;
    @BindView(R.id.sv_appointment_map)
    AutoCompleteTextView svAppointmentMap;
    @BindView(R.id.cl_fragment_appiontment_map)
    ConstraintLayout clFragmentAppiontmentMap;
    @BindView(R.id.btn_fragment_appiontment_search_city)
    TextView btnFragmentAppiontmentSearchCity;

    @Override
    protected void initView() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.uuparking_appiontment_map;
    }

    @Override
    protected void getDataFromServer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initMap(savedInstanceState);
        CityPickerView.getInstance().init(getContext());
        svAppointmentMap.addTextChangedListener(this);
        geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(this);
        return rootView;
    }

    /*初始化地图，控件*/
    private void initMap(Bundle savedInstanceState) {
        client = new OkHttpClient();
        list = new ArrayList<>();

        nearbyCarports = new ArrayList<>();
        tvFragmentAppiontmentWaitTimeShow.setText(String.valueOf(5));
        tvFragmentAppiontmentSearchScopeShow.setText(String.valueOf(searchScope / 1000));
        mvFragmentAppiontmentMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mvFragmentAppiontmentMap.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.023344d, 121.454173d), 14.0f));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mvFragmentAppiontmentMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mvFragmentAppiontmentMap.onPause();
    }


    /**
     * 请求获取附近车位
     */
    private void getAppiontmentCarports(LatLng latLng) {

        SearchParameterOPO searchParameterOPO = new SearchParameterOPO();
        searchParameterOPO.setLatitude(AMapUtil.laLngTosix(latLng.latitude) + "");
        searchParameterOPO.setLongitude(AMapUtil.laLngTosix(latLng.longitude) + "");
        searchParameterOPO.setAvailablePrice(maxPrice);
        searchParameterOPO.setSearchScope(searchScope);
        RequestBody body = RequestBody.create(UrlAddress.JSON,
                new Gson().toJson(searchParameterOPO));

//        Log.e("预约停车搜索条件", new Gson().toJson(searchParameterOPO));
        Request request = new Request.Builder().url(UrlAddress.UUPgetAroundCarparkInCarparkUrl).post(body).build();
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
        Message message = new Message();
        message.arg1 = 1;
        try {
            jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("Y_SEARCH_SUC")) {
                JSONObject object = jsonObject.getJSONObject("resultMap");
                JSONArray array = object.getJSONArray("carparkList");
                if (array.length() <= 0) {
                    updateToastUI("附近暂无车位！", 0);
                }
                nearbyCarports.addAll(AppointmentNearbyCarpaorts.arrayAppointmentNearbyCarpaortsFromData(array.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mhandler.sendMessage(message);
    }

    int searchScope = 3000;
    int maxPrice = 5;

    @OnClick({R.id.imagebtn_fragment_appiontment_search_scope_add, R.id.btn_fragment_appiontment_search_city,
            R.id.imagebtn_fragment_appiontment_search_scope_less, R.id.imagebtn_fragment_appiontment_wait_time_less,
            R.id.imagebtn_fragment_appiontment_wait_time_add, R.id.imageView16})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_appiontment_search_city:
                CityPickerView.getInstance().setConfig(new CityConfig.Builder(getContext())
                        .province("上海").city("上海").visibleItemsCount(4)
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY)
                        .cancelTextColor("#ff6600").confirTextColor("#ff6600")
                        .build());

                CityPickerView.getInstance().setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        btnFragmentAppiontmentSearchCity.setText(city.getName());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                //显示
                CityPickerView.getInstance().showCityPicker(getContext());
                break;
            case R.id.imagebtn_fragment_appiontment_search_scope_add:
                if (searchScope >= 5 * 1000) {
                    Toast.makeText(getContext(), "停车距离太远了", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchScope = searchScope + 2000;
                tvFragmentAppiontmentSearchScopeShow.setText(String.valueOf(searchScope / 1000));
                getAppiontmentCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_appiontment_search_scope_less:
                if (searchScope <= 1000) {
                    Toast.makeText(getContext(), "距离太近了！", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchScope = searchScope - 2000;
                tvFragmentAppiontmentSearchScopeShow.setText(String.valueOf(searchScope / 1000));
                getAppiontmentCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_appiontment_wait_time_less:
                if (maxPrice <= 1) {
                    Toast.makeText(getContext(), "给车位主点收益吧！", Toast.LENGTH_SHORT).show();
                    return;
                }
                maxPrice = maxPrice - 1;
                tvFragmentAppiontmentWaitTimeShow.setText(String.valueOf(maxPrice));
                getAppiontmentCarports(latLngcamera);
                break;
            case R.id.imagebtn_fragment_appiontment_wait_time_add:
                if (maxPrice >= 100) {
                    Toast.makeText(getContext(), "土豪做个朋友", Toast.LENGTH_SHORT).show();
                    return;
                }
                maxPrice = maxPrice + 1;
                tvFragmentAppiontmentWaitTimeShow.setText(String.valueOf(maxPrice));
                getAppiontmentCarports(latLngcamera);
                break;
            case R.id.imageView16:
                break;
        }
    }

    /**
     * 字体监听改变
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        list.clear();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, btnFragmentAppiontmentSearchCity.getText().toString());
            Inputtips inputTips = new Inputtips(getContext(), inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (list.size() >= 1) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(list.get(0).getPoint()), 14.0f));
        }
    }

    /**
     * 字体输入提示
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            if (!list.isEmpty()) {
                list.clear();
            }
            list.addAll(tipList);
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            Log.e("搜索地址个数", listString.size() + "");
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getContext(),
                    R.layout.route_inputs, listString);
            svAppointmentMap.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
            if (list.size() >= 1) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(list.get(0).getPoint()), 14.0f));
            }
        } else {
            Log.e("搜索地址错误", "" + rCode);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng latLngUp = cameraPosition.target;
        if (AMapUtils.calculateLineDistance(latLngcamera, latLngUp) > 500) {
            getAppiontmentCarports(cameraPosition.target);
        }
        latLngcamera = latLngUp;
    }

    /**
     * 地图点击时间监听
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (clickMader != null) {
            clickMader.hideInfoWindow();
        }
    }

    /**
     * marker监听
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        int i = 0;
        while (!marker.getTitle().toString().equals(nearbyCarports.get(i).getId())) {
            i++;
        }
//        WindowManager manager = getActivity().getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        int width = outMetrics.widthPixels/outMetrics.densityDpi;
        ApointOrderPopup apointOrderPopup = new ApointOrderPopup(getContext(), nearbyCarports.get(i));
        changeActivityAlpha(0.5f);
        apointOrderPopup.showAsDropDown(btnFragmentAppiontmentSearchCity, 20, 100, Gravity.BOTTOM);
        apointOrderPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeActivityAlpha(1.0f);
            }
        });

        return true;
    }

    private void changeActivityAlpha(float v) {
        WindowManager.LayoutParams parms = ((Activity) getContext()).getWindow().getAttributes();
        parms.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(parms);
    }

    /**
     * 逆地理编码
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//        if (i == 1000) {
//            svAppointmentMap.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
//        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 更新UI
     */
    private void updateToastUI(String mage, int i) {
        Message message = new Message();
        message.arg1 = i;
        Bundle bun = new Bundle();
        bun.putString("message", mage);
        message.setData(bun);
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
                    updateUIlistView();
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
    }

    /**
     * 添加地图mader
     */
    private void addmaker() {
        ArrayList<MarkerOptions> listMader = new ArrayList<>();
        for (int i = 0; i < nearbyCarports.size(); i++) {
            AppointmentNearbyCarpaorts nearby = nearbyCarports.get(i);
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
        for (int x = 0; x < markers.size(); x++) {
            DecimalFormat df = new DecimalFormat("#.000000");
            markers.get(x).remove();
        }
    }
}
