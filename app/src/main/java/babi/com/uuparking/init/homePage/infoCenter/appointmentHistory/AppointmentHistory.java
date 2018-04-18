package babi.com.uuparking.init.homePage.infoCenter.appointmentHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import babi.com.uuparking.adapter.AppointmentHistoryAdapter;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.AppointmentHistoryGson;
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
 * Created by b on 2017/12/26.
 */

public class AppointmentHistory extends AppCompatActivity {
    AppointmentHistoryAdapter appointmentHistoryAdapter;
    List<AppointmentHistoryGson> listAppoint;
    List<AppointmentHistoryGson> list;
    OkHttpClient okHttpClient;
    int i = 1;
    @BindView(R.id.lv_my_appointment_history)
    ListView lvMyAppointmentHistory;
    @BindView(R.id.srl_my_appointment_history)
    SmartRefreshLayout srlMyAppointmentHistory;
    OkHttpClient client;
    boolean loadMore = false;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_appiontment_history);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("预约记录");
        okHttpClient = new OkHttpClient();
        client = new OkHttpClient();
        srlMyAppointmentHistory.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                i = 1;
                listAppoint.clear();
                requestAppointmentHistory();
            }
        });
        srlMyAppointmentHistory.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (loadMore) {
                    i++;
                    requestAppointmentHistory();
                } else {
                    refreshlayout.finishLoadmore();
                    ToastUtil.ToastUtilmsg(getBaseContext(), "已经到底了");
                }
            }
        });
        list = new ArrayList<>();
        listAppoint = new ArrayList<>();
        appointmentHistoryAdapter = new AppointmentHistoryAdapter(this, listAppoint);
        lvMyAppointmentHistory.setAdapter(appointmentHistoryAdapter);
        lvMyAppointmentHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getBaseContext(), AppointmentHistoryDetails.class);
                intent.putExtra("appointId", listAppoint.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        i = 1;
        if (listAppoint.size() > 0) {
            listAppoint.clear();
        }
        requestAppointmentHistory();
    }

    /**
     * 获取预约记录
     */
    private void requestAppointmentHistory() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        map.put("page", i);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPgetReservationRecUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if (!object.getString("code").equals("Y007")) {
                        updateToastUI(object.getString("message"));
                        return;
                    }
                    JSONArray resultList = object.getJSONObject("resultMap").getJSONArray("orderPlanList");
                    if (resultList.length() < 10) {
                        loadMore = false;
                    } else {
                        loadMore = true;
                    }
                    list.clear();
                    list.addAll(AppointmentHistoryGson.arrayAppointmentHistoryGsonFromData(resultList.toString()));
                    listAppoint.addAll(list);
                    updateViewUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 更新主线程view
     */
    private void updateViewUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appointmentHistoryAdapter.notifyDataSetChanged();
                srlMyAppointmentHistory.finishRefresh();
                srlMyAppointmentHistory.finishLoadmore();
            }
        });
    }

    /**
     * 更新ui主线程Toast
     */
    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.ToastUtilmsg(getBaseContext(), message);
            }
        });
    }

    private void dealResult(String code) {

    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
