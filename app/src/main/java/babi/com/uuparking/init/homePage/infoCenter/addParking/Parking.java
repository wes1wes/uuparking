package babi.com.uuparking.init.homePage.infoCenter.addParking;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.MyParkingBaseAdapter;
import babi.com.uuparking.init.homePage.infoCenter.share.Share;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.UserParkingGson;
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
 * Created by bgd on 2018/1/20.
 */


public class Parking extends AppCompatActivity {

    @BindView(R.id.img_my_parking)
    ImageView imgMyParking;
    @BindView(R.id.elv_my_parking_lock)
    ExpandableListView elvMyParkingLock;
    @BindView(R.id.srl_my_parking_lock)
    SmartRefreshLayout srlMyParkingLock;
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    private List<String> gData;
    private UserParkingGson userParkingGson;
    MyParkingBaseAdapter myParkingBaseAdapter;
    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_parking_layout);
        ButterKnife.bind(this);
tvCommonDiyToolbarTitle.setText("车位管理");
        okHttpClient = new OkHttpClient();
        userParkingGson = new UserParkingGson();
        gData = new ArrayList<>();
        gData.clear();
        gData.add("可用车位");
        gData.add("申请中的车位");
        gData.add("维修中的车位");
        getpostpark();
        srlMyParkingLock.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getpostpark();
                refreshlayout.finishRefresh();
            }
        });
        myParkingBaseAdapter = new MyParkingBaseAdapter(gData, userParkingGson, getApplicationContext());
        elvMyParkingLock.setAdapter(myParkingBaseAdapter);
        elvMyParkingLock.setGroupIndicator(null);
        elvMyParkingLock.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        UserParkingGson.UsableCarporListBean useableParkingGson = userParkingGson.getUsableCarporList().get(childPosition);
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", SpUserUtils.getString(getBaseContext(),"userId"));
                        bundle.putString("carparkId", "" + useableParkingGson.getCarparkId());
                        bundle.putString("lockId", "" + useableParkingGson.getLockId());
                        Intent intent = new Intent(getApplicationContext(), Share.class);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        break;
                    case 1:
                        UserParkingGson.ApplicationListBean applyParkingGson = userParkingGson.getApplicationList().get(childPosition);
                        if ("4".equals(applyParkingGson.getStatus())) {
                            // TODO: 2017/12/26 待处理，进入激活页面
                            // 审核完成，进入激活页面
//                            Bundle bundle1 = new Bundle();
//                            bundle1.putString("applicationID", iData.get(groupPosition).get(childPosition).get("applicationID").toString());
//                            Intent intent1 = new Intent(getApplicationContext(), JiGuangPushActivity.class);
//                            intent1.putExtra("applicationID", bundle1);
//                            startActivity(intent1);
                        } else if ("6".equals(applyParkingGson.getStatus())) {
                            Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "021-54391079"));
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Parking.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                            }
                            startActivity(intent2);
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getpostpark();
    }

    private void getpostpark() {
        requestUseableParking();
    }

    private void requestFindUserApply() {
        RequestBody body = RequestBody.create(UrlAddress.JSON, " {\"userId\":\"" + SpUserUtils.getString(getBaseContext(),"userId") + "\"}");
        Request request = new Request.Builder().url(UrlAddress.UUPfindUserApplyUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = null;

                String s = response.body().string();
                Logger.e(s, s);
                try {
                    jsonObject = new JSONObject(s.toString());
                    if (!jsonObject.getString("code").equals("1")) {
                        return;
                    }
                    jsonToApplicationList(jsonObject.getJSONObject("resultMap"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void requestRepairSchedule() {
        RequestBody body = RequestBody.create(UrlAddress.JSON, " {\"userId\":\"" + SpUserUtils.getString(getBaseContext(),"userId") + "\"}");
        Request request = new Request.Builder().url(UrlAddress.UUPgetRepairScheduleUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = null;

                String s = response.body().string();
                try {
                    jsonObject = new JSONObject(s.toString());
                    if (!jsonObject.getString("code").equals("1")) {
                        return;
                    }
                    jsonToRepairList(jsonObject.getJSONObject("resultMap"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*请求可用车位*/
    private void requestUseableParking() {
        RequestBody body = RequestBody.create(UrlAddress.JSON, " {\"userId\":\"" + SpUserUtils.getString(getBaseContext(),"userId") + "\"}");
        Request request = new Request.Builder().url(UrlAddress.UUPgetParkingManagementInfoUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject jsonUseable;
                String string = response.body().string();
                try {
                    jsonUseable = new JSONObject(string);
                    Logger.e(string, jsonUseable);
                    if (jsonUseable.getString("code").equals("1")) {
                        jsonToList(jsonUseable.getJSONObject("resultMap"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void jsonToList(JSONObject result) {
        if (result == null || result.length() < 1) {
            return;
        }
        UserParkingGson upg = UserParkingGson.objectFromData(result.toString());
        userParkingGson.setUsableCarporList(upg.getUsableCarporList());
        userParkingGson.setApplicationList(upg.getApplicationList());
        userParkingGson.setRepairScheduleList(upg.getRepairScheduleList());
        //更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myParkingBaseAdapter.notifyDataSetChanged();
            }
        });
    }

    private void jsonToApplicationList(JSONObject result) {

        if (result == null || result.length() < 1) {
            return;
        }

        //更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myParkingBaseAdapter.notifyDataSetChanged();
            }
        });

    }

    private void jsonToRepairList(JSONObject result) {
        try {
            JSONArray ar = result.getJSONArray("data");
            if (ar == null || ar.length() < 1) {
                return;
            }
            //更新UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myParkingBaseAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gData.clear();
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }

}
