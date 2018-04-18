package babi.com.uuparking.init.homePage.infoCenter.addParking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.gsonFormatObject.ParkingDetailsGson;
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
 * Created by b on 2017/9/5.
 */

public class DetailedParking extends AppCompatActivity {

    String carparkId;
    Map<String, Object> map;
    OkHttpClient okHttpClient;
    ParkingDetailsGson parkingDetails;
    @BindView(R.id.tv_my_parking_details_id)
    TextView tvMyParkingDetailsId;
    @BindView(R.id.tv_my_parking_details_id_show)
    TextView tvMyParkingDetailsIdShow;
    @BindView(R.id.ll_my_parking_details_id)
    LinearLayout llMyParkingDetailsId;
    @BindView(R.id.tv_my_parking_details_location)
    TextView tvMyParkingDetailsLocation;
    @BindView(R.id.tv_my_parking_details_location_show)
    TextView tvMyParkingDetailsLocationShow;
    @BindView(R.id.ll_my_parking_details_location)
    LinearLayout llMyParkingDetailsLocation;
    @BindView(R.id.tv_my_parking_details_type)
    TextView tvMyParkingDetailsType;
    @BindView(R.id.tv_my_parking_details_type_show)
    TextView tvMyParkingDetailsTypeShow;
    @BindView(R.id.ll_my_parking_details_linkman)
    LinearLayout llMyParkingDetailsLinkman;
    @BindView(R.id.tv_my_parking_details_outdoors)
    TextView tvMyParkingDetailsOutdoors;
    @BindView(R.id.tv_my_parking_details_outdoors_show)
    TextView tvMyParkingDetailsOutdoorsShow;
    @BindView(R.id.ll_my_parking_details_phone)
    LinearLayout llMyParkingDetailsPhone;
    @BindView(R.id.tv_my_parking_details_hascolumn)
    TextView tvMyParkingDetailsHascolumn;
    @BindView(R.id.tv_my_parking_details_hascolumn_show)
    TextView tvMyParkingDetailsHascolumnShow;
    @BindView(R.id.ll_my_parking_details_hascolumn)
    LinearLayout llMyParkingDetailsHascolumn;
    @BindView(R.id.btn_my_parking_details_uploading)
    TextView btnMyParkingDetailsUploading;
    @BindView(R.id.grid_my_parking_details_photo1)
    ImageView gridMyParkingDetailsPhoto1;
    @BindView(R.id.grid_my_parking_details_photo2)
    ImageView gridMyParkingDetailsPhoto2;
    @BindView(R.id.grid_my_parking_details_photo3)
    ImageView gridMyParkingDetailsPhoto3;
    @BindView(R.id.btn_my_parking_details_bad)
    Button btnMyParkingDetailsBad;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_parking_details_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("车位详情");
        map = new HashMap<>();
        okHttpClient = new OkHttpClient();
        Intent intent = getIntent();
        carparkId = intent.getStringExtra("carparkId");
        map.put("carparkId", carparkId);
        okHttpPost();
    }

    private void okHttpPost() {
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request request = new Request.Builder().url(UrlAddress.UUPparticularsUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.e("aaaaaa", "onResponse: " + str);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(str);
                    jsonToList(jsonObject);
                    Message message = new Message();//获取Message对象
                    message.arg1 = 1;//设置Message对象附带的参数
                    mhandler.sendMessage(message);//向主线程发送消息
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void jsonToList(JSONObject result) {
        try {
            JSONObject resultMap = result.getJSONObject("resultMap");
            JSONObject profitrate = resultMap.getJSONObject("profitrate");
            parkingDetails = ParkingDetailsGson.objectFromData(profitrate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("HandlerLeak")
    Handler mhandler = new Handler() {
        /**重写handleMessage方法*/
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {

            if (msg.arg1 == 1) {
                tvMyParkingDetailsIdShow.setText(parkingDetails.getCarparkId());
                if (parkingDetails.getIsOutdoor() == 1) {
                    tvMyParkingDetailsHascolumnShow.setText("是");
                } else {
                    tvMyParkingDetailsHascolumnShow.setText("否");
                }
                if (parkingDetails.getHasColumn() == 1) {
                    tvMyParkingDetailsOutdoorsShow.setText("是");
                } else {
                    tvMyParkingDetailsOutdoorsShow.setText("否");
                }

                tvMyParkingDetailsLocationShow.setText(parkingDetails.getCity() + parkingDetails.getDetailAddress());
                tvMyParkingDetailsTypeShow.setText(parkingDetails.getCreateTime() + "");
//                if (map.size() >= 6) {
//                    String[] ss = map.get("scenePicUrl").toString().split(",");
//                    for (int i = 0; i < ss.length; i++) {
//                        if (i == 0) {
//                            Picasso.with(getBaseContext()).load(ss[0]).into(gridMyParkingDetailsPhoto1);
//                        } else if (i == 1) {
//                            Picasso.with(getApplicationContext()).load(ss[1]).into(gridMyParkingDetailsPhoto2);
//                        } else if (i == 2) {
//                            Picasso.with(getApplicationContext()).load(ss[2]).into(gridMyParkingDetailsPhoto3);
//                        }
//                    }
//                }
            }
        }
    };


    @OnClick({R.id.btn_my_parking_details_bad,R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_parking_details_bad:
                if (btnMyParkingDetailsBad.getText().toString().contains("查看报修详情")) {
                    Intent intent = new Intent(this, RepairSchedule.class);
                    startActivity(intent);
                } else {
                    postreportForRepair();
                }
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
        }
    }
    private void postreportForRepair() {
    }
}
