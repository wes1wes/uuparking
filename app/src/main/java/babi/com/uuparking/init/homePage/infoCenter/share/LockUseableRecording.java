package babi.com.uuparking.init.homePage.infoCenter.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
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
import babi.com.uuparking.adapter.LockRecordingAdapter;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.LockRecordingGson;
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
 * Created by b on 2018/2/1.
 */

public class LockUseableRecording extends AppCompatActivity {
    @BindView(R.id.lv_mine_parking_management_lock_details)
    ListView lvMineParkingManagementLockDetails;
    LockRecordingAdapter lockRecordingAdapter;
    List<LockRecordingGson> lockRecordingGsons;
    List<LockRecordingGson> lockRecordingGsons_s;
    int i = 1;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    @BindView(R.id.tv_mine_parking_management_lock_details_id)
    TextView tvMineParkingManagementLockDetailsId;
    @BindView(R.id.srl_mine_parking_management_lock_details)
    SmartRefreshLayout srlMineParkingManagementLockDetails;
    //    @BindView(R.id.tv_mine_parking_management_lock_details_location)
//    TextView tvMineParkingManagementLockDetailsLocation;
    private boolean lodermore = true;
    String lockId;
    OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_parking_management_lock_list_view);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("车锁使用记录");

        lockRecordingGsons = new ArrayList<>();
        lockRecordingGsons_s = new ArrayList<>();
        lockRecordingAdapter = new LockRecordingAdapter(this, lockRecordingGsons_s);
        lvMineParkingManagementLockDetails.setAdapter(lockRecordingAdapter);
        lockId = getIntent().getStringExtra("lockId");
        client = new OkHttpClient();//.newBuilder().connectTimeout(10000, TimeUnit.MILLISECONDS).build()
        requestLockRecording(i);
        srlMineParkingManagementLockDetails.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                i = 1;
                lockRecordingGsons_s.clear();
                requestLockRecording(1);
                refreshlayout.finishRefresh();
            }
        });
        srlMineParkingManagementLockDetails.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (lodermore) {
                    requestLockRecording(i++);
                    refreshlayout.finishLoadmore();
                } else {
                    Toast.makeText(getBaseContext(), "前面没有操作了哦", Toast.LENGTH_SHORT).show();
                    refreshlayout.finishLoadmore();
                }
            }
        });

    }

    private void requestLockRecording(int i) {
        Map<String, Object> map = new HashMap<>();
        map.put("lockId", lockId);
        map.put("page", i);
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPgetActionLogUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Logger.e("车锁使用记录结果", str);
                dealResult(str);
                srlMineParkingManagementLockDetails.finishLoadmore();
                srlMineParkingManagementLockDetails.finishRefresh();
            }
        });
    }

    private void dealResult(String str) {
        try {
            if (!lockRecordingGsons.isEmpty()) {
                lockRecordingGsons.clear();
            }
            JSONObject jsonObject = new JSONObject(str);
            JSONObject object = jsonObject.getJSONObject("resultMap");
            JSONArray arry = object.getJSONArray("lockLog");
            if (arry.length() <= 9) {
                lodermore = false;
            } else {
                lodermore = true;
            }
            lockRecordingGsons.addAll(LockRecordingGson.arrayLockRecordingGsonFromData(arry.toString()));
            lockRecordingGsons_s.addAll(lockRecordingGsons);
            updateViewUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateViewUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lockRecordingGsons_s.size() > 0) {
                    tvMineParkingManagementLockDetailsId.setText(lockRecordingGsons_s.get(0).getLockId());
//                    tvMineParkingManagementLockDetailsLocation.setText(lockRecordingGsons_s.get(0).get);
                }
                lockRecordingAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
