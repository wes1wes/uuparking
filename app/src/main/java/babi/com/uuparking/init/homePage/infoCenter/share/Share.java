package babi.com.uuparking.init.homePage.infoCenter.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.ShareAdapter;
import babi.com.uuparking.init.homePage.infoCenter.addParking.DetailedParking;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.CarparkShareInfoGson;
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
 * 作者：BGD
 * Created by b on 2018/1/11.
 */

public class Share extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    List<ShareInfoGson> list;
    CarparkShareInfoGson carparkShareInfoGson = new CarparkShareInfoGson();
    ShareAdapter madapter;
    OkHttpClient okHttpClient;
    Bundle bundle = new Bundle();
    Bundle bundleShare = new Bundle();
    @BindView(R.id.tv_mine_parking_management_share)
    TextView tvMineParkingManagementShare;
    @BindView(R.id.swich_mine_parking_management_share)
    Switch swichMineParkingManagementShare;
    @BindView(R.id.tv_mine_parking_management_pice)
    TextView tvMineParkingManagementPice;
    @BindView(R.id.lv_mine_parking_management_pice)
    ListView lvMineParkingManagementPice;
    @BindView(R.id.tv_mine_parking_management_parking_details)
    TextView tvMineParkingManagementParkingDetails;
    @BindView(R.id.srl_share_parking)
    SmartRefreshLayout srlShareParking;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    @BindView(R.id.guideline9)
    Guideline guideline9;
    @BindView(R.id.linearLayout2)
    ConstraintLayout linearLayout2;
    @BindView(R.id.tv_mine_parking_management_lock_make_details)
    TextView tvMineParkingManagementLockMakeDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_parking_management_share_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("共享车位");
        okHttpClient = new OkHttpClient();
        list = new ArrayList<>();
        bundle = getIntent().getBundleExtra("bundle");
        madapter = new ShareAdapter(this, list);
        lvMineParkingManagementPice.setAdapter(madapter);
        lvMineParkingManagementPice.setOnItemClickListener(this);
        lvMineParkingManagementPice.setOnItemLongClickListener(this);
        srlShareParking.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPost();
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPost();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void getPost() {
        Map<String, Object> mapget = new HashMap<>();
        mapget.put("userId", bundle.getString("userId"));
        mapget.put("carparkId", bundle.getString("carparkId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON,
                new Gson().toJson(mapget));
        mapget.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPgetCarparkShareUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = null;
                String string = response.body().string();
                Log.e("111", "" + string);
                try {
                    jsonObject = new JSONObject(string);
                    if (!jsonObject.getString("code").equals("1")) {
                        return;
                    }
                    jsontolist(jsonObject.getJSONObject("resultMap"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void jsontolist(JSONObject result) {
        carparkShareInfoGson = CarparkShareInfoGson.objectFromData(result.toString());
        list.clear();
        list.addAll(carparkShareInfoGson.getShareInfo());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                madapter.notifyDataSetChanged();
                if (carparkShareInfoGson.getStatus().equals("0")) {
                    swichMineParkingManagementShare.setChecked(false);
                } else {
                    swichMineParkingManagementShare.setChecked(true);
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!bundleShare.isEmpty()) {
            bundleShare.clear();
        }
        ShareInfoGson infoGson = list.get(position);
        bundleShare.putString("carparkId", bundle.getString("carparkId"));
        bundleShare.putString("id", infoGson.getId());
        bundleShare.putString("unitprice", String.valueOf(infoGson.getUnitprice()));
        bundleShare.putString("shareStartTime", infoGson.getStartTime());
        bundleShare.putString("shareEndTime", infoGson.getEndTime());
        bundleShare.putString("shareDay", infoGson.getShareDay());
        intentShareParkingModification(bundleShare);
    }

    /**
     * 跳转时间价格设置页面
     */
    private void intentShareParkingModification(Bundle bundle) {
        Intent intent = new Intent(this, ShareParkingModification.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        internetDeleteShareinfo(position);
        return false;
    }

    @OnClick({R.id.tv_mine_parking_management_pice, R.id.tv_mine_parking_management_parking_details,
            R.id.swich_mine_parking_management_share, R.id.image_common_diy_toolbar_back,R.id.tv_mine_parking_management_lock_make_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mine_parking_management_lock_make_details:
                Intent intentlock = new Intent(this, LockUseableRecording.class);
                intentlock.putExtra("lockId", bundle.getString("lockId"));
                startActivity(intentlock);
                break;
            case R.id.swich_mine_parking_management_share:
                internetSaveData();
                break;
            case R.id.tv_mine_parking_management_pice:
                if (!bundleShare.isEmpty()) {
                    bundleShare.clear();
                }
                bundleShare.putString("carparkId", bundle.getString("carparkId"));
                bundleShare.putString("id", "");
                if (list.size() < 3) {
                    intentShareParkingModification(bundleShare);
                } else {
                    Toast.makeText(getBaseContext(), "每个车位最多设置3个共享时间段", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_mine_parking_management_parking_details:
                // 跳转车位基本信息页面
                Intent intent = new Intent(getBaseContext(), DetailedParking.class);
                intent.putExtra("carparkId", bundle.getString("carparkId"));
                startActivity(intent);
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
        }
    }

    /**
     * 删除共享时间价格
     */
    private void internetDeleteShareinfo(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", list.get(position).getId());
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPdeleteShareInfoUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {
                        list.remove(position);
                        updateViewUI();
                    } else {
                        updateToastUI(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateViewUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                madapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 保存共享设置
     */
    private void internetSaveData() {
        if (swichMineParkingManagementShare.isChecked() == true) {
            if (list.size() <= 0) {
                ToastUtil.ToastUtilmsg(this, "您没设置共享时间哦！");
                swichMineParkingManagementShare.setChecked(false);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userID", SpUserUtils.getString(this, "userId"));
        map.put("carparkId", bundle.getString("carparkId"));
        map.put("status", swichMineParkingManagementShare.isChecked() == true ? "1" : "0");
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPchangeCarparkUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Logger.e(str, response);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {
//                        finish();
                    } else {
                        updateToastUI(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.tv_mine_parking_management_lock_make_details)
    public void onViewClicked() {
    }
}
