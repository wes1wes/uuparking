package babi.com.uuparking.init.homePage.infoCenter.share;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
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
 * Created by bgd on 2017/12/3.
 */

public class ShareParkingModification extends AppCompatActivity {

    Bundle bundle;
    OkHttpClient okHttpClient;
    @BindView(R.id.et_share_parking_price)
    EditText etShareParkingPrice;
    @BindView(R.id.cb_share_parking_workday)
    CheckBox cbShareParkingWorkday;
    @BindView(R.id.cb_share_parking_weekend)
    CheckBox cbShareParkingWeekend;
    @BindView(R.id.cb_share_parking_monday)
    CheckBox cbShareParkingMonday;
    @BindView(R.id.cb_share_parking_tuesday)
    CheckBox cbShareParkingTuesday;
    @BindView(R.id.cb_share_parking_wednesday)
    CheckBox cbShareParkingWednesday;
    @BindView(R.id.cb_share_parking_thursday)
    CheckBox cbShareParkingThursday;
    @BindView(R.id.cb_share_parking_friday)
    CheckBox cbShareParkingFriday;
    @BindView(R.id.cb_share_parking_saturday)
    CheckBox cbShareParkingSaturday;
    @BindView(R.id.cb_share_parking_week)
    CheckBox cbShareParkingWeek;
    @BindView(R.id.share_parking_start)
    TextView shareParkingStart;
    @BindView(R.id.tc_share_parking_start)
    TextView tcShareParkingStart;
    @BindView(R.id.share_parking_end)
    TextView shareParkingEnd;
    @BindView(R.id.tc_share_parking_end)
    TextView tcShareParkingEnd;
    @BindView(R.id.btn_share_parking)
    Button btnShareParking;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_modification_activity);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("价格时间");
        okHttpClient = new OkHttpClient();
        initTimePicker();
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        if (bundle.size() < 4) {
            return;
        }
        initView();
    }


    private void initView() {
        if (bundle.getString("shareDay").contains("1")) {
            cbShareParkingMonday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("2")) {
            cbShareParkingTuesday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("3")) {
            cbShareParkingWednesday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("4")) {
            cbShareParkingThursday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("5")) {
            cbShareParkingFriday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("6")) {
            cbShareParkingSaturday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("7")) {
            cbShareParkingWeek.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("1;2;3;4;5")) {
            cbShareParkingWorkday.setChecked(true);
        }
        if (bundle.getString("shareDay").contains("6;7")) {
            cbShareParkingWeekend.setChecked(true);
        }
        etShareParkingPrice.setText(bundle.getString("unitprice"));
        tcShareParkingStart.setText(bundle.getString("shareStartTime"));
        tcShareParkingEnd.setText(bundle.getString("shareEndTime"));
    }

    @OnClick({R.id.cb_share_parking_workday, R.id.cb_share_parking_weekend, R.id.cb_share_parking_monday,
            R.id.cb_share_parking_tuesday, R.id.cb_share_parking_wednesday, R.id.cb_share_parking_thursday,
            R.id.cb_share_parking_friday, R.id.cb_share_parking_saturday, R.id.cb_share_parking_week,
            R.id.tc_share_parking_start, R.id.tc_share_parking_end, R.id.btn_share_parking, R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.cb_share_parking_workday:
                if (cbShareParkingWorkday.isChecked()) {
                    cbShareParkingMonday.setChecked(true);
                    cbShareParkingTuesday.setChecked(true);
                    cbShareParkingWednesday.setChecked(true);
                    cbShareParkingThursday.setChecked(true);
                    cbShareParkingFriday.setChecked(true);
                } else {
                    cbShareParkingMonday.setChecked(false);
                    cbShareParkingTuesday.setChecked(false);
                    cbShareParkingWednesday.setChecked(false);
                    cbShareParkingThursday.setChecked(false);
                    cbShareParkingFriday.setChecked(false);
                }
                break;
            case R.id.cb_share_parking_weekend:
                if (cbShareParkingWeekend.isChecked()) {
                    cbShareParkingSaturday.setChecked(true);
                    cbShareParkingWeek.setChecked(true);
                } else {
                    cbShareParkingSaturday.setChecked(false);
                    cbShareParkingWeek.setChecked(false);
                }
                break;
            case R.id.cb_share_parking_monday:
            case R.id.cb_share_parking_tuesday:
            case R.id.cb_share_parking_wednesday:
            case R.id.cb_share_parking_thursday:
            case R.id.cb_share_parking_friday:
                if (cbShareParkingMonday.isChecked() && cbShareParkingTuesday.isChecked()
                        && cbShareParkingWednesday.isChecked() && cbShareParkingThursday.isChecked()
                        && cbShareParkingFriday.isChecked()) {
                    cbShareParkingWorkday.setChecked(true);
                } else {
                    cbShareParkingWorkday.setChecked(false);
                }
                break;
            case R.id.cb_share_parking_saturday:
            case R.id.cb_share_parking_week:
                if (cbShareParkingSaturday.isChecked() && cbShareParkingWeek.isChecked()) {
                    cbShareParkingWeekend.setChecked(true);
                } else {
                    cbShareParkingWeekend.setChecked(false);
                }
                break;
            case R.id.tc_share_parking_start:
                if (pvTime1 != null) {
                    pvTime1.show(view);
                }
                break;
            case R.id.tc_share_parking_end:
                if (pvTime1 != null) {
                    pvTime1.show(view);
                }
                break;
            case R.id.btn_share_parking:
                //更新共享设置！
                if (etShareParkingPrice.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入车位价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                getWeekendString();
                if (s.length() < 2) {
                    Toast.makeText(this, "请选择共享次数", Toast.LENGTH_SHORT).show();
                    return;
                }

                internetSaveData();
                break;
        }
    }

    private void internetSaveData() {
        Map<String, Object> map = new HashMap<>();
        map.put("carparkId", bundle.getString("carparkId"));
        map.put("startTime", tcShareParkingStart.getText().toString());
        map.put("endTime", tcShareParkingEnd.getText().toString());
        map.put("shareDay", s.toString());
        map.put("id", bundle.getString("id"));
        map.put("unitprice", etShareParkingPrice.getText().toString());
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPsaveShareInfoUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Logger.e("保存", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getString("code").equals("1")) {
                        finish();
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

    StringBuffer s = new StringBuffer();

    /**
     * 将周转化1234567
     */
    private void getWeekendString() {
        if (!s.toString().isEmpty()) {
            s.delete(0, s.length());
        }
        if (cbShareParkingMonday.isChecked()) {
            s.append("1;");
        }
        if (cbShareParkingTuesday.isChecked()) {
            s.append("2;");
        }
        if (cbShareParkingWednesday.isChecked()) {
            s.append("3;");
        }
        if (cbShareParkingThursday.isChecked()) {
            s.append("4;");
        }
        if (cbShareParkingFriday.isChecked()) {
            s.append("5;");
        }
        if (cbShareParkingSaturday.isChecked()) {
            s.append("6;");
        }
        if (cbShareParkingWeek.isChecked()) {
            s.append("7;");
        }
        if (s.toString().length() > 2) {
            s.substring(0, s.length() - 1);
        }
    }

    TimePickerView pvTime1;

    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2017, 9, 1);
        endDate.set(2020, 11, 31);

        pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                TextView tv = (TextView) v;
                if (v.getId() == R.id.tc_share_parking_end) {
                    try {
                        if (new TimeCompare().DateCompare(getTime(date), tcShareParkingStart.getText().toString()) >= 15) {
                            tv.setText(getTime(date));
                        } else {
                            tv.setText(new TimeCompare().DateAdd15(tcShareParkingStart.getText().toString(), 30));
                            Toast.makeText(getApplicationContext(), "结束时间小于开始时间", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (new TimeCompare().DateCompare(getTime(date), "23:45") >= 0) {
                            tv.setText("23:45");
                            Toast.makeText(getApplicationContext(), "开始时间不能超过 23:45", Toast.LENGTH_SHORT).show();
                        } else {
                            tv.setText(getTime(date));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        })
                .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(30)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(Color.BLACK)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
}
