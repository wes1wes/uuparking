package babi.com.uuparking.init.homePage.appointment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.adapter.ShareAdapter;
import babi.com.uuparking.init.homePage.infoCenter.appointmentHistory.AppointmentHistoryDetails;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.OrderDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.AppointmentNearbyCarpaorts;
import babi.com.uuparking.init.utils.gsonFormatObject.NearbyCarports;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2018/3/19.
 */

public class ApointOrderPopup extends PopupWindow implements TimePickerView.OnTimeSelectListener {
    TextView tvApointParkingOrderLocation;
    Button btnApointParkingOrderSure;
    TextView tvApointParkingOrderLockIdShow;
    TextView tvApointParkingOrderShareTimeShow;
    TextView tvApointParkingOrderParkingInfoShow;
    TextView tvApointParkingOrderParkingStartTimeShow;
    TextView tvApointParkingOrderParkingTimeLengthShow;
    TextView tvApointParkingOrderParkingDateShow;
    @BindView(R.id.lv_apoint_parking_order_share_time)
    ListView lvApointParkingOrderShareTime;
    private Context context;
    private AppointmentNearbyCarpaorts nearbyCarports;
    private ShareAdapter shareAdapter;

    public ApointOrderPopup(Context context, AppointmentNearbyCarpaorts nearbyCarports) {
        View contentView = View.inflate(context, R.layout.appoint_parking_order_popup, null);
        this.context = context;
        this.nearbyCarports = nearbyCarports;
        tvApointParkingOrderParkingInfoShow = contentView.findViewById(R.id.tv_apoint_parking_order_parking_info_show);
        tvApointParkingOrderShareTimeShow = contentView.findViewById(R.id.tv_apoint_parking_order_share_time_show);
        lvApointParkingOrderShareTime = contentView.findViewById(R.id.lv_apoint_parking_order_share_time);
        tvApointParkingOrderParkingDateShow = contentView.findViewById(R.id.tv_apoint_parking_order_parking_date_show);
        tvApointParkingOrderLockIdShow = contentView.findViewById(R.id.tv_apoint_parking_order_lock_id_show);
        tvApointParkingOrderLocation = contentView.findViewById(R.id.tv_apoint_parking_order_location);
        tvApointParkingOrderParkingTimeLengthShow = contentView.findViewById(R.id.tv_apoint_parking_order_parking_time_length_show);
        tvApointParkingOrderParkingStartTimeShow = contentView.findViewById(R.id.tv_apoint_parking_order_parking_start_time_show);
        btnApointParkingOrderSure = contentView.findViewById(R.id.btn_apoint_parking_order_sure);
        initView();
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setIgnoreCheekPress();
        this.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        shareAdapter = new ShareAdapter(context, nearbyCarports.carparkShareInfos);
        lvApointParkingOrderShareTime.setAdapter(shareAdapter);
        tvApointParkingOrderLocation.setText(nearbyCarports.getCarparkLocation().getDetailAddress());
        tvApointParkingOrderLockIdShow.setText(nearbyCarports.getLockId());
        tvApointParkingOrderParkingInfoShow.setText((nearbyCarports.getCarparkType() == 2 ? "可充电+" : "不可充电+") +
                (nearbyCarports.getIsOutdoor() == 1 ? "露天+" : "地下+") +
                (nearbyCarports.getHasColumn() == 1 ? "有立柱" : "无立柱"));
        try {
            tvApointParkingOrderParkingDateShow.setText(TimeCompare.getNowTimeAll().substring(0,10));
            tvApointParkingOrderParkingStartTimeShow.setText(TimeCompare.DateAdd15(TimeCompare.getNowTime(), 30));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvApointParkingOrderParkingStartTimeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvTime1 == null) {
                    initTimePicker();
                }
                pvTime1.show(v);
            }
        });
        tvApointParkingOrderParkingDateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvTime== null) {
                   initTPicker();
                }
                pvTime.show(v);
            }
        });
        tvApointParkingOrderParkingTimeLengthShow.addTextChangedListener(new TextWatcher() {
            private int selectionStart;
            private int selectionEnd;
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                selectionStart = edit.getSelectionStart();
//                selectionEnd = edit.getSelectionEnd();
//                if (!StringUtil.isOnlyPointNumber(edit.getText().toString())){
//                    PromptManager.showToast(context,"您输入的数字保留在小数点后两位");
//                    //删除多余输入的字（不会显示出来）
//                    s.delete(selectionStart - 1, selectionEnd);
//                    edit.setText(s);
//                }
            }
        });
        btnApointParkingOrderSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeLengthStr = tvApointParkingOrderParkingTimeLengthShow.getText().toString();
                if (timeLengthStr == null || timeLengthStr.isEmpty()) {
                    ToastUtil.ToastUtilmsg(context, "请填写停车时长");
                    return;
                }
                try {
                    float timeLength = Float.valueOf(timeLengthStr);

                    OrderDialog.normalDialogPositiveNext(context, "预约下单成功，30分钟内可免费取消，超过我们将收取3元的预约金！", new OrderDialog.Callback() {
                        @Override
                        public void click() {
                            requestCreateOrderPlan();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 用户下预约单
     */
    private void requestCreateOrderPlan() {
        OkHttpClient client = new OkHttpClient();
        Map<String, Object> map = new HashMap<>();
        map.put("carparkId", nearbyCarports.getId());
        map.put("expectParkLength", Float.valueOf(tvApointParkingOrderParkingTimeLengthShow.getText().toString()) * 60);
        map.put("latitude", "");
        map.put("longitude", "");
        map.put("userId", SpUserUtils.getString(context, "userId"));
        try {
            String h=TimeCompare.DateReplaceNowHourAndMinute(tvApointParkingOrderParkingStartTimeShow.getText().toString()).substring(10);
            String time=tvApointParkingOrderParkingDateShow.getText().toString()+h;
            Log.e("时间",time);
            map.put("expectStartParkTime", time);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        map.put("orderDeposit", "2");
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPcreateOrderUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("订单结果", s);
                dealResulte(s);
            }
        });
    }

    Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                OrderDialog.normalDialogResult(context, (String) msg.obj);
            }
            if (msg.arg1 == 2) {
                final String appointId = (String) msg.obj;
                OrderDialog.normalDialogPositiveUncancle(context, "下单成功！现在查看预约详情吗？", new OrderDialog.Callback() {
                    @Override
                    public void click() {
                        Intent intent = new Intent(context, AppointmentHistoryDetails.class);
                        intent.putExtra("appointId", appointId);
                        context.startActivity(intent);
                    }
                });
            }
            return false;
        }
    });

    private void dealResulte(String s) {
        try {
            JSONObject object = new JSONObject(s);
            Message message = new Message();

            switch (object.getString("code")) {
                case "Y002":
                    message.arg1 = 2;
                    message.obj = object.getJSONObject("resultMap").getString("orderPlanId");
                    break;
                default:
                    message.arg1 = 1;
                    message.obj = object.getString("message");
                    break;
            }
            mhandler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    TimePickerView pvTime1;

    /**
     * 时间选择器
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(2017, 9, 1);
        endDate.set(2025, 11, 1);

        pvTime1 = new TimePickerView.Builder(context, this)
                .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .gravity(Gravity.CENTER)
                .setContentSize(30)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setOutSideCancelable(false)//点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setSubmitColor(Color.rgb(255, 102, 0))//确定按钮文字颜色
                .setCancelColor(Color.rgb(255, 102, 0))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色
                .setBgColor(Color.WHITE)//滚轮背景颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", " ", ":", "", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
    }


    /**
     * 时间选择器监听
     */
    @Override
    public void onTimeSelect(Date date, View v) {
        try {
            TextView tv = (TextView) v;
            if (v.getId() == R.id.tv_apoint_parking_order_parking_start_time_show) {
                String time = getTime(date);
                if (TimeCompare.DateCompare(getTime(date), TimeCompare.getNowTime()) <= 30) {
                    time = TimeCompare.DateAdd15(TimeCompare.getNowTime(), 30);
                    ToastUtil.ToastUtilmsg(context, "预约时间>当前时间+30分钟");
                }
                tv.setText(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
    TimePickerView pvTime;

    /**
     * 时间选择器
     */
    private void initTPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(2017, 9, 1);
        endDate.set(2025, 11, 1);

        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                TextView tv = (TextView) v;
                tv.setText(getTime1(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .gravity(Gravity.CENTER)
                .setContentSize(30)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setOutSideCancelable(false)//点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setSubmitColor(Color.rgb(255, 102, 0))//确定按钮文字颜色
                .setCancelColor(Color.rgb(255, 102, 0))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色
                .setBgColor(Color.WHITE)//滚轮背景颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日 ", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
    }

    private String getTime1(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
