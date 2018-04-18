package babi.com.uuparking.init.homePage.infoCenter.apply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.ImgeCompress;
import babi.com.uuparking.init.utils.commentUtil.PicassoImageLoader;
import babi.com.uuparking.init.utils.commentUtil.RegularExpressionUtil;
import babi.com.uuparking.init.utils.commentUtil.RequsetPermissionUtil;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by b on 2017/8/1.
 */

public class Apply extends AppCompatActivity {

    private static final int IMAGE_PICKER_FRONT = 200;
    private static final int IMAGE_PICKER_BACK = 201;
    private static final int IMAGE_PICKER_PROVE_ONE = 210;
    private static final int IMAGE_PICKER_PROVE_TWO = 211;
    @BindView(R.id.tv_my_apply_linkman)
    TextView tvMyApplyLinkman;
    @BindView(R.id.et_my_apply_linkman)
    EditText etMyApplyLinkman;
    @BindView(R.id.ll_my_apply_linkman)
    LinearLayout llMyApplyLinkman;
    @BindView(R.id.tv_my_apply_phone)
    TextView tvMyApplyPhone;
    @BindView(R.id.et_my_apply_phone)
    EditText etMyApplyPhone;
    @BindView(R.id.ll_my_apply_phone)
    LinearLayout llMyApplyPhone;
    @BindView(R.id.tv_my_apply_location)
    TextView tvMyApplyLocation;
    @BindView(R.id.item_city_name_tv)
    TextView itemCityNameTv;
    @BindView(R.id.ll_my_apply_location)
    LinearLayout llMyApplyLocation;
    @BindView(R.id.tv_my_apply_person_location)
    TextView tvMyApplyPersonLocation;
    @BindView(R.id.et_my_apply_person_location)
    EditText etMyApplyPersonLocation;
    @BindView(R.id.ll_my_apply_person_location)
    LinearLayout llMyApplyPersonLocation;
    @BindView(R.id.tv_my_apply_parking_number)
    TextView tvMyApplyParkingNumber;
    @BindView(R.id.et_my_apply_parking_number)
    EditText etMyApplyParkingNumber;
    @BindView(R.id.ll_my_apply_parking_number)
    LinearLayout llMyApplyParkingNumber;
    @BindView(R.id.btn_my_apply_uploading)
    TextView btnMyApplyUploading;
    @BindView(R.id.image_my_apply_idcard_front)
    ImageView imageMyApplyIdcardFront;
    @BindView(R.id.image_my_apply_idcard_back)
    ImageView imageMyApplyIdcardBack;
    @BindView(R.id.image_my_apply_prove_one)
    ImageView imageMyApplyProveOne;
    @BindView(R.id.image_my_apply_prove_two)
    ImageView imageMyApplyProveTwo;
    @BindView(R.id.btn_my_apply)
    Button btnMyApply;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    private ArrayList<ImageItem> alist;
    String[] sb = new String[4];
    OkHttpClient okhttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_apply_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("申请车位");
        okhttpClient = new OkHttpClient();
        alist = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            new RequsetPermissionUtil().requestPermission(this, this);
        }
        CityPickerView.getInstance().init(this);

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String path = images.get(0).path;
                File file = ImgeCompress.saveimg(path);
                updateToAliyun(file, requestCode);
            } else {
                Toast.makeText(this, "选图错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateToAliyun(File file, final int recode) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
        builder.addFormDataPart("userId", SpUserUtils.getString(getBaseContext(), "userId"));
        MultipartBody multipartBody = builder.build();
        Request request = new Request.Builder().url(UrlAddress.UploadImageURL).post(multipartBody).build();
        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject js = null;
                try {
                    js = new JSONObject(response.body().string());
                    updateToastUI(js.getString("imageURL"), recode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.item_city_name_tv, R.id.ll_my_apply_location, R.id.ll_my_apply_person_location,
            R.id.image_my_apply_idcard_front, R.id.image_my_apply_idcard_back, R.id.image_my_apply_prove_one,
            R.id.image_my_apply_prove_two, R.id.ll_my_apply_linkman, R.id.ll_my_apply_phone, R.id.btn_my_apply_uploading,
            R.id.btn_my_apply, R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.item_city_name_tv:
            case R.id.ll_my_apply_location:
//添加默认的配置，不需要自己定义
                CityPickerView.getInstance().setConfig(new CityConfig.Builder(this)
                        .province("上海").city("上海").district("闵行区").visibleItemsCount(4)
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                        .cancelTextColor("#ff6600").confirTextColor("#ff6600").build());

                CityPickerView.getInstance().setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        itemCityNameTv.setText(province + "-" + city + "-" + district);
                    }

                    @Override
                    public void onCancel() {
//                        ToastUtils.showLongToast(CitypickerWheelActivity.this, "已取消");
                    }
                });

                //显示
                CityPickerView.getInstance().showCityPicker(this);
                break;
            case R.id.btn_my_apply:
                if (itemCityNameTv.getText().toString().contains("点击选所在区")) {
                    Toast.makeText(this, "请选择所在区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etMyApplyPersonLocation.getText().toString().contains("街道、小区、单元") || etMyApplyPersonLocation.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写详细地址，我们尽快给您取得联系", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etMyApplyLinkman.getText().toString().contains("请输入联系人") || etMyApplyLinkman.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写联系人，我们尽快给您取得联系", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etMyApplyPhone.getText().toString().contains("输入手机号") || etMyApplyPhone.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写联系人手机号，以方便我们尽快给您取得联系", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegularExpressionUtil.isMobile(etMyApplyPhone.getText().toString())) {
                    Toast.makeText(this, "手机号格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO: 2018/1/17 没有上传图片
                Map<String, Object> map = new HashMap<>();
                map.put("userId", SpUserUtils.getString(this, "userId"));
                map.put("linkName", etMyApplyLinkman.getText().toString());
                map.put("linkAddress", etMyApplyPersonLocation.getText().toString());
                map.put("linkPhone", etMyApplyPhone.getText().toString());
                map.put("parkinglotPaperPicUrl", sb[2] + "," + sb[3]);
                map.put("idCardPicUrl", sb[0] + "" + sb[1]);
                map.put("carportNum", etMyApplyParkingNumber.getText().toString());
                RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
                map.clear();
                Request request = new Request.Builder().url(UrlAddress.UUPapplyAddCarportUrl).post(body).build();
                Call call = okhttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof ConnectException) { // 其他错误
                            Toast.makeText(getApplicationContext(), "网络连接错误，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONObject js = null;
                        try {
                            js = new JSONObject(s);
                            switch (js.getString("code")) {
                                case "1":
                                    finish();
                                    break;
                            }
                            updateToastUI(js.getString("message"), 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                break;
            case R.id.image_my_apply_idcard_front:
                Intent front = new Intent(getBaseContext(), ImageGridActivity.class);
                startActivityForResult(front, IMAGE_PICKER_FRONT);
                break;
            case R.id.image_my_apply_idcard_back:
                Intent back = new Intent(getBaseContext(), ImageGridActivity.class);
                startActivityForResult(back, IMAGE_PICKER_BACK);
                break;
            case R.id.image_my_apply_prove_one:
                Intent prove_one = new Intent(getBaseContext(), ImageGridActivity.class);
                startActivityForResult(prove_one, IMAGE_PICKER_PROVE_ONE);
                break;
            case R.id.image_my_apply_prove_two:
                Intent prove_two = new Intent(getBaseContext(), ImageGridActivity.class);
                startActivityForResult(prove_two, IMAGE_PICKER_PROVE_TWO);
                break;
        }
    }

    private void updateToastUI(final String message, final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code) {
                    case IMAGE_PICKER_FRONT:
                        sb[0] = message;
                        Picasso.with(getBaseContext()).load(message).error(R.mipmap.phoyo).into(imageMyApplyIdcardFront);
                        break;
                    case IMAGE_PICKER_BACK:
                        sb[1] = message;
                        Picasso.with(getBaseContext()).load(message).error(R.mipmap.phoyo).into(imageMyApplyIdcardBack);
                        break;
                    case IMAGE_PICKER_PROVE_ONE:
                        sb[2] = message;
                        Picasso.with(getBaseContext()).load(message).error(R.mipmap.phoyo).into(imageMyApplyProveOne);
                        break;
                    case IMAGE_PICKER_PROVE_TWO:
                        sb[3] = message;
                        Picasso.with(getBaseContext()).load(message).error(R.mipmap.phoyo).into(imageMyApplyProveTwo);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
    }
}
