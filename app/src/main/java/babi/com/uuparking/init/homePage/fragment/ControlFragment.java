package babi.com.uuparking.init.homePage.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.ControlListAdapter;
import babi.com.uuparking.init.utils.DIYview.DIYBaseFragment;
import babi.com.uuparking.init.utils.commentUtil.FinalConstant;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.WaitingDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.UseableParkingGson;
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
 * Created by bgd on 2018/1/4.
 */

public class ControlFragment extends DIYBaseFragment {

    List<UseableParkingGson> mlist;
    int pos=0;
    OkHttpClient okHttpClient;
    ControlListAdapter mbelvadapter;
    @BindView(R.id.lv_my_control_lock)
    ListView lvMyControlLock;
    @BindView(R.id.btn_my_control_up)
    Button btnMyControlUp;
    @BindView(R.id.btn_my_control_down)
    Button btnMyControlDown;
    @BindView(R.id.textView6)
    TextView textView6;
    Unbinder unbinder;
    @BindView(R.id.et_my_control_search)
    EditText etMyControlSearch;
    @BindView(R.id.switch_my_control_close)
    Switch switchMyControlClose;

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.uuparking_control;
    }

    @Override
    protected void getDataFromServer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        mlist = new ArrayList<>();
        okHttpClient = new OkHttpClient();
        initSpinner();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        okhttpPark();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initSpinner() {
        mbelvadapter = new ControlListAdapter(mlist, getContext());
        lvMyControlLock.setAdapter(mbelvadapter);
        lvMyControlLock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                mbelvadapter.setSelectItem(position);
                syncSwitchStatus(pos);
                mbelvadapter.notifyDataSetChanged();
                refreshUpAndDorn();
            }
        });
    }

    private void syncSwitchStatus(int pos) {
        switch (mlist.get(pos).getStatus()){
            case "0":
                switchMyControlClose.setChecked(false);
                break;
            default:
                switchMyControlClose.setChecked(true);
                break;
        }
    }

    /**
     * 刷新车锁状态
     */
    private void refreshUpAndDorn() {
        if (mlist==null||mlist.size()<=pos){
            return;
        }
        switch (mlist.get(pos).getLockStatus()) {
            case 0:
                btnMyControlUp.setTextColor(Color.BLUE);
                btnMyControlDown.setTextColor(Color.GRAY);
                btnMyControlUp.setClickable(true);
                btnMyControlDown.setClickable(false);
                break;
            case 1:
                btnMyControlUp.setTextColor(Color.GRAY);
                btnMyControlDown.setTextColor(Color.BLUE);
                btnMyControlUp.setClickable(false);
                btnMyControlDown.setClickable(true);
                break;
            default:
                btnMyControlUp.setTextColor(Color.GRAY);
                btnMyControlDown.setTextColor(Color.GRAY);
                btnMyControlUp.setClickable(false);
                btnMyControlDown.setClickable(false);
                break;
        }
    }


    @OnClick({R.id.btn_my_control_up, R.id.btn_my_control_down, R.id.switch_my_control_close})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.switch_my_control_close:
                if (mlist.size() < 1) {
                    switchMyControlClose.setChecked(false);
                    Toast.makeText(getContext(), "你还没有车位可以操作哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (switchMyControlClose.isChecked() == true) {
                    internetSaveData();
                    return;
                }
                switch (mlist.get(pos).getStatus()) {
                    case "0":
                        Toast.makeText(getContext(), mlist.get(pos).getLockId() + "已被关闭共享", Toast.LENGTH_SHORT).show();
                        break;
                    case "1":
                        internetSaveData();
                        break;
                    case "2":
                        switchMyControlClose.setChecked(true);
                        Toast.makeText(getContext(), mlist.get(pos).getLockId() + "已被预约您暂时不能使用", Toast.LENGTH_SHORT).show();
                        break;
                    case "3":
                        switchMyControlClose.setChecked(true);
                        Toast.makeText(getContext(), mlist.get(pos).getLockId() + "正在使用您暂时不能使用", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        switchMyControlClose.setChecked(true);
                        break;
                }
                break;
            case R.id.btn_my_control_up:
                if (mlist.size() < 1) {
                    Toast.makeText(getContext(), "你还没有车位可以操作哦", Toast.LENGTH_SHORT).show();
                    btnMyControlUp.setClickable(false);
                    return;
                }
                controlLockUpDown("CLOSE");
                break;
            case R.id.btn_my_control_down:
                if (mlist.size() < 1) {
                    Toast.makeText(getContext(), "你还没有车位可以操作哦", Toast.LENGTH_SHORT).show();
                    btnMyControlDown.setClickable(false);
                    return;
                }
                controlLockUpDown("OPEN");
                break;
        }
    }

    Map<String, Object> map = new HashMap<>();

    /**
     * 选择要控制升降的车锁
     */
    private void controlLockUpDown(String open) {
        switch (mlist.get(pos).getStatus()) {
            case "0":
            case "1":
                map.put("userId", SpUserUtils.getString(getContext(), "userId"));
                map.put("carparkId", mlist.get(pos).getCarparkId());
                map.put("action", open);
                RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
                map.clear();
                okhttpUpAndDown(body);
                break;
            case "2":
                Toast.makeText(getContext(), mlist.get(pos).getLockId() + "已被预约您暂时不能使用", Toast.LENGTH_SHORT).show();
                break;
            case "3":
                Toast.makeText(getContext(), mlist.get(pos).getLockId() + "正在使用您暂时不能使用", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 请求车锁动作
     */
    private void okhttpUpAndDown(RequestBody body) {
        Request request = new Request.Builder().url(UrlAddress.UUPcontrollerUserCarparkUrl).post(body).build();
        okHttpClient.newBuilder().connectTimeout(FinalConstant.REQUESTTIMEDOUT, TimeUnit.SECONDS).build();
        Call call = okHttpClient.newCall(request);
        WaitingDialog.showWaitingDialog(getContext());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                WaitingDialog.cancelWaitingDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    Message msg = new Message();
                    msg.obj = jsonObject.getString("message");
                    msg.arg1=2;
                    mhandler.sendMessage(msg);
                    okhttpPark();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }

    /**
     * 保存共享设置
     */
    private void internetSaveData() {

        Map<String, Object> map = new HashMap<>();
        map.put("userID", SpUserUtils.getString(getContext(), "userId"));
        map.put("carparkId", mlist.get(pos).getCarparkId());
        map.put("status", switchMyControlClose.isChecked() == true ? "1" : "0");
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

                    } else {
                        Message msg = new Message();
                        msg.obj = jsonObject.getString("message");
                        msg.arg1=2;
                        mhandler.sendMessage(msg);
                    }
                    okhttpPark();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求用户名下车位
     */
    private void okhttpPark() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(getContext(), "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPgetUsableCarportUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("用户名下可用车位", "结果：" + s);
                jsontolist(s);
            }
        });
    }

    Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.arg1 == 1) {
                if (mlist.size() >= 1) {
                    syncSwitchStatus(pos);
                    mbelvadapter.notifyDataSetChanged();
                    refreshUpAndDorn();
                    btnMyControlDown.setClickable(true);
                    btnMyControlUp.setClickable(true);
                }
            }
            if ( message.arg1==2) {
                ToastUtil.ToastUtilmsg(getContext(), message.obj.toString());
            }
            return false;
        }
    });

    /**
     * 解析json数据
     */
    private void jsontolist(String result) {
            mlist.clear();
            mlist.addAll(UseableParkingGson.arrayUseableParkingGsonFromData(result));
            Message msg = new Message();
            msg.arg1 = 1;
            mhandler.sendMessage(msg);
    }
}
