package babi.com.uuparking.init.homePage.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babi.com.uuparking.init.utils.DIYview.DIYBaseFragment;
import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.AMapUtil;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;

/**
 * Created by b on 2017/12/4.
 */

public class AppiontmentFragment extends DIYBaseFragment implements TextWatcher, Inputtips.InputtipsListener {


    @BindView(R.id.searchView_appointment)
    AutoCompleteTextView searchViewAppointment;
    @BindView(R.id.btn_appointment_sure)
    Button btnAppointmentSure;
    @BindView(R.id.tv_appointment_start_time)
    TextView tvAppointmentStartTime;
    @BindView(R.id.tv_appointment_price_max)
    TextView tvAppointmentPriceMax;
    @BindView(R.id.tv_appointment_price)
    TextView tvAppointmentPrice;
    @BindView(R.id.tv_appointment_end_time)
    TextView tvAppointmentEndTime;
    @BindView(R.id.tv_appointment_start_time_show)
    TextView tvAppointmentStartTimeShow;
    @BindView(R.id.tv_appointment_end_time_show)
    TextView tvAppointmentEndTimeShow;
    @BindView(R.id.tv_appointment_distance)
    TextView tvAppointmentDistance;
    Unbinder unbinder;
    List<Tip> list;
    OkHttpClient client;
    int positon = 0;

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.uuparking_appiontment;
    }

    @Override
    protected void getDataFromServer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initTimePicker();
        list = new ArrayList<>();
        client = new OkHttpClient();
        searchViewAppointment.addTextChangedListener(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_appointment_sure, R.id.searchView_appointment, R.id.tv_appointment_start_time,
            R.id.tv_appointment_start_time_show, R.id.tv_appointment_price_max, R.id.tv_appointment_price,
            R.id.tv_appointment_end_time, R.id.tv_appointment_end_time_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.searchView_appointment:

                break;
            case R.id.btn_appointment_sure:
                if (list.size() <= 0) {
                    Toast.makeText(getContext(), "请输入正确地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (searchViewAppointment.getText().length()<=1) {
                    Toast.makeText(getContext(), "请完善地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                Log.e("选择地址个数+选择地址", list.size() + "" + list.get(0).toString());
                map.put("latitude", list.get(0).getPoint().getLatitude());
                map.put("longitude", list.get(0).getPoint().getLongitude());
                map.put("availablePrice", "");
                map.put("carparkType", "");
                map.put("endTime", "");
                map.put("startTime", "");
                map.put("orderDeposit", "");
                requestSearchParking(map);
                map.clear();
                break;
            case R.id.tv_appointment_start_time:
            case R.id.tv_appointment_start_time_show:
                if (pvTime1 != null) {
                    pvTime1.show(view);
                }
                break;
            case R.id.tv_appointment_price_max:
                break;
            case R.id.tv_appointment_price:
                break;
            case R.id.tv_appointment_end_time:
            case R.id.tv_appointment_end_time_show:
                if (pvTime1 != null) {
                    pvTime1.show(view);
                }
                break;
        }
//        Toast.makeText(getContext(), "小猴子正在加紧开发哦！", Toast.LENGTH_SHORT).show();
    }

    private void requestSearchParking(Map<String, Object> map) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        list.clear();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "上海");
            Inputtips inputTips = new Inputtips(getContext(), inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

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
            searchViewAppointment.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
//            ToastUtil.showerror(this, rCode);
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

        pvTime1 = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                TextView tv = (TextView) v;
                if (v.getId() == R.id.tv_appointment_start_time_show) {
                    try {
//                        if (new TimeCompare().DateCompare(getTime(date), tvAppointmentStartTimeShow.getText().toString()) >= 15) {
//                            tv.setText(getTime(date));
//                        } else {
//                            tv.setText(new TimeCompare().DateAdd15(tvAppointmentStartTimeShow.getText().toString()));
//                            Toast.makeText(getContext(), "结束时间小于开始时间", Toast.LENGTH_SHORT).show();
//                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (new TimeCompare().DateCompare(getTime(date), "23:45") >= 0) {
                            tv.setText("23:45");
                            Toast.makeText(getContext(), "开始时间不能超过 23:45", Toast.LENGTH_SHORT).show();
                        } else {
                            tv.setText(getTime(date));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        })
                .setType(new boolean[]{false, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .gravity(Gravity.BOTTOM)
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(Color.GRAY)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", " ", ":", "", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
}
