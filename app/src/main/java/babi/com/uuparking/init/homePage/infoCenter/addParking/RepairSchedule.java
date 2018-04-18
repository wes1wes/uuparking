package babi.com.uuparking.init.homePage.infoCenter.addParking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.RepairScheduleAdapter;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/11/13.
 */

public class RepairSchedule extends AppCompatActivity {
    RepairScheduleAdapter repairScheduleAdapter;
    @BindView(R.id.lv_my_parking_repair_schedule)
    ListView lvMyParkingRepairSchedule;
    @BindView(R.id.btn_my_parking_repair_schedule)
    Button btnMyParkingRepairSchedule;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    private List<Map<String, Object>> list;
    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_parking_repair_schedule_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("报修进度详情");
        okHttpClient = new OkHttpClient();
        list = new ArrayList<>();
        repairScheduleAdapter = new RepairScheduleAdapter(this, list);
        lvMyParkingRepairSchedule.setAdapter(repairScheduleAdapter);
        String bundle = getIntent().getStringExtra("carportID");

        RequestBody body = new FormBody.Builder()
                .add("carportID", bundle).build();
        Request request = new Request.Builder().url(UrlAddress.getRepairScheduleURL).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray arr = jsonObject.getJSONArray("list");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject json = arr.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();
                        map.put("time", json.getString("time"));
                        map.put("step", json.getString("step"));
                        list.add(map);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            repairScheduleAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick({R.id.image_common_diy_toolbar_back, R.id.btn_my_parking_repair_schedule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                break;
            case R.id.btn_my_parking_repair_schedule:
                break;
        }
    }
}
