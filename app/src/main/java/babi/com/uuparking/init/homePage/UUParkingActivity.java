package babi.com.uuparking.init.homePage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.init.homePage.infoCenter.appointmentHistory.AppointmentHistoryDetails;
import babi.com.uuparking.init.login.Registered;
import babi.com.uuparking.init.utils.DIYview.NoSlidingViewPaper;
import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.dialog.IdCardDialog;
import babi.com.uuparking.init.homePage.fragment.AppiontmentFragmentMap;
import babi.com.uuparking.init.homePage.fragment.ControlFragment;
import babi.com.uuparking.init.homePage.fragment.HomeFragment;
import babi.com.uuparking.init.homePage.fragment.MineFragment;
import babi.com.uuparking.init.homePage.parkNow.BillingTime;
import babi.com.uuparking.init.homePage.parkNow.PayParkingFee;
import babi.com.uuparking.init.homePage.parkNow.PlaceOrderParking;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.utils.commentUtil.BottomNavigationViewHelper;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：BGD
 * 创建时间：2018/1/8
 */
public class UUParkingActivity extends AppCompatActivity {


    @BindView(R.id.nosliding_vp_main_uuparking_diy)
    NoSlidingViewPaper noslidingVpMainUuparkingDiy;
    @BindView(R.id.bnv_main_uuparking_navigation)
    BottomNavigationView bnvMainUuparkingNavigation;
    @BindView(R.id.container)
    LinearLayout container;
    OkHttpClient client;
    public static UUParkingActivity uuParkingActivity;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    noslidingVpMainUuparkingDiy.setCurrentItem(0);
                    return true;
                case R.id.navigation_appiontment:
                    noslidingVpMainUuparkingDiy.setCurrentItem(1);
                    return true;
                case R.id.navigation_control:
                    noslidingVpMainUuparkingDiy.setCurrentItem(2);
                    return true;
                case R.id.navigation_mine:
                    noslidingVpMainUuparkingDiy.setCurrentItem(3);
                    return true;
            }
            return false;
        }

    };

    List<Fragment> fgLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uuparking);
        ButterKnife.bind(this);
        uuParkingActivity = this;
        client = new OkHttpClient();
        if (Login.login != null) {
            Login.login.finish();
        }
        if (SpUserUtils.getString(this, "userId").isEmpty()) {

        } else {
            internetUserStatus();
        }
        initFragment();
        if (SpUserUtils.getString(this, "hasIdentified").equals("0") && SpUserUtils.getBoolean(this, "identifyUser") == true) {
            Intent intentid = new Intent(this, Registered.class);
            startActivity(intentid);
        }
    }

    /**
     * 初始化四个fragment
     */
    private void initFragment() {
        fgLists = new ArrayList<>(4);
        fgLists.add(new HomeFragment());
        fgLists.add(new AppiontmentFragmentMap());
        fgLists.add(new ControlFragment());
        fgLists.add(new MineFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
        noslidingVpMainUuparkingDiy.setAdapter(fragmentPagerAdapter);
        noslidingVpMainUuparkingDiy.setOffscreenPageLimit(3); //预加载剩下
        BottomNavigationViewHelper.disableShiftMode(bnvMainUuparkingNavigation);
        bnvMainUuparkingNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 请求获取用户当前状态
     */
    private void internetUserStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UserUncompledteOrder).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("用户当前订单状态", "onResponse: " + str);
                dealResult(str);
            }
        });
    }

    /**
     * 处理用户状态返回结果
     */
    private void dealResult(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            Bundle bundle = new Bundle();
            String code = jsonObject.getString("code");
            if (code.equals("JC02")) {
                updateToastUI(jsonObject.getString("message"));
                return;
            }
            JSONObject object = jsonObject.getJSONObject("resultMap");
            switch (code) {
                case "JE01":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    intentPlaceOrderParking(bundle);
                    break;
                case "JE04":
                    bundle.putString("orderId", object.getString("orderId"));
                    bundle.putString("carparkId", object.getString("carparkId"));
                    bundle.putString("timeslot", object.getString("timeslot"));
                    bundle.putString("planOrderId", object.getString("planOrderId"));
                    intentPlaceOrderParking(bundle);
                    break;
                case "Y003":
                    Message message = new Message();
                    message.arg2 = 300;
                    message.obj=object.getString("planOrderId");
                    mhandler.sendMessage(message);
                    break;
                case "JE02":
                    //计时
                    Intent intentBilling = new Intent(getBaseContext(), BillingTime.class);
                    Bundle bundleBill = new Bundle();
                    bundleBill.putString("orderId", object.getString("orderId"));
                    bundleBill.putString("carparkDetail", object.getJSONObject("carparkResult").toString());
                    bundleBill.putString("lockId", object.getJSONObject("carparkResult").getString("lockId"));
                    bundleBill.putString("downLockTime", object.getString("downLockTime"));
                    intentBilling.putExtra("bundle", bundleBill);
                    startActivity(intentBilling);
                    break;
                case "JE03":
                    //结费
                    Bundle bundleIntent = new Bundle();
                    bundleIntent.putString("costDetails", object.toString());
                    Intent intentPay = new Intent(getBaseContext(), PayParkingFee.class);
                    intentPay.putExtra("bundle", bundleIntent);
                    startActivity(intentPay);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.ToastUtilmsg(getBaseContext(), message);
            }
        });
    }

    /**
     * 跳转订单页面
     */
    private void intentPlaceOrderParking(Bundle bundle) {
        Intent intent = new Intent(this, PlaceOrderParking.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg2 ==300) {
                final String appontId =(String) msg.obj;
                OrderDialog.normalDialogPositiveUncancle(UUParkingActivity.this, "你有一个预约，进入\"个人中心-->预约记录\"中查看详情", new OrderDialog.Callback() {
                    @Override
                    public void click() {
                        Intent intent = new Intent(UUParkingActivity.this, AppointmentHistoryDetails.class);
                        intent.putExtra("appointId", appontId);
                        startActivity(intent);
                    }
                });
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
