package babi.com.uuparking.init.homePage.infoCenter.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.homePage.infoCenter.setting.AbouteUs;
import babi.com.uuparking.init.homePage.infoCenter.setting.Opinion;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.login.Registered;
import babi.com.uuparking.init.utils.commentUtil.ImgeCompress;
import babi.com.uuparking.init.utils.commentUtil.PicassoImageLoader;
import babi.com.uuparking.init.utils.commentUtil.RequsetPermissionUtil;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import babi.com.uuparking.init.utils.dialog.IdCardDialog;
import babi.com.uuparking.init.utils.gsonFormatObject.UserInfoGson;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/8/11.
 */

public class Person extends AppCompatActivity {

    private static final int IMAGE_PICKER = 200;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;
    @BindView(R.id.tv_my_person_user_photo)
    TextView tvMyPersonUserPhoto;
    @BindView(R.id.circlime_user_photo)
    CircleImageView circlimeUserPhoto;
    @BindView(R.id.tv_my_person_name)
    TextView tvMyPersonName;
    @BindView(R.id.tv_my_person_name_show)
    TextView tvMyPersonNameShow;
    @BindView(R.id.tv_my_person_identification)
    TextView tvMyPersonIdentification;
    @BindView(R.id.tv_my_person_identification_show)
    TextView tvMyPersonIdentificationShow;
    @BindView(R.id.tv_my_person_bindwx)
    TextView tvMyPersonBindwx;
    @BindView(R.id.tv_my_person_bindwx_show)
    TextView tvMyPersonBindwxShow;
    @BindView(R.id.tv_my_person_phone_number)
    TextView tvMyPersonPhoneNumber;
    @BindView(R.id.tv_my_person_phone_number_show)
    TextView tvMyPersonPhoneNumberShow;
    @BindView(R.id.tv_my_person_withdraw_pwd)
    TextView tvMyPersonWithdrawPwd;
    @BindView(R.id.btn_my_person_log_off)
    Button btnMyPersonLogOff;
    @BindView(R.id.imag_user_photo_in)
    ImageView imagUserPhotoIn;
    @BindView(R.id.image_my_person_name_in)
    ImageView imageMyPersonNameIn;
    @BindView(R.id.image_my_person_identification_in)
    ImageView imageMyPersonIdentificationIn;
    @BindView(R.id.image_my_person_phone_number_in)
    ImageView imageMyPersonPhoneNumberIn;
    @BindView(R.id.tv_my_person_aboutus)
    TextView tvMyPersonAboutus;
    @BindView(R.id.img_my_person_aboutus_in)
    ImageView imgMyPersonAboutusIn;
    @BindView(R.id.tv_my_person_opinion)
    TextView tvMyPersonOpinion;
    @BindView(R.id.image_my_person_opinion_in)
    ImageView imageMyPersonOpinionIn;
    @BindView(R.id.image_my_person_make_instructions_in)
    ImageView imageMyPersonMakeInstructionsIn;
    @BindView(R.id.tv_my_person_make_instructions)
    TextView tvMyPersonMakeInstructions;

    private int REQUEST_IMAGE = 1;
    String url = new String();
    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_person_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("个人中心");
        okHttpClient = new OkHttpClient();
        imagePickerComfig();
        initView();
//        if (Build.VERSION.SDK_INT >= 23) {
        new RequsetPermissionUtil().requestPermission(this, this);
//        }
    }

    private void imagePickerComfig() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(600);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(600);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);//保存文件的高度。单位像素
    }

    private void initView() {
        tvMyPersonNameShow.setText(SpUserUtils.getString(this, "nickName"));
        tvMyPersonPhoneNumberShow.setText(SpUserUtils.getString(this, "phone"));
        Picasso.with(this).load(SpUserUtils.getString(this, "headIconUrl")).into(circlimeUserPhoto);
    }

    public void inputTitleDialog(Context context) {
        final String[] nickName = new String[1];
        final EditText inputServer = new EditText(context);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入：").setIcon(
                R.mipmap.uuparking).setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        if (name.trim() == null) {
                            return;
                        }
                        tvMyPersonNameShow.setText(name);
                        Map<String, Object> map = new HashMap<>();
                        map.put("nickName", name);
                        updateUserNameToAli(map);
                        map.clear();
                    }
                });
        builder.show();
    }

    /*更改用户信息到服务器*/
    private void updateUserheadiconToAli(Map<String, Object> map) {
        map.put("userId", SpUserUtils.getString(this, "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request re = new Request.Builder().url(UrlAddress.UpupheadiconURL).post(body).build();
        Call call = okHttpClient.newCall(re);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("更新用户信息", s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUserDataFromAli();
                    }
                });
            }
        });
    }

    /*更改用户信息到服务器*/
    private void updateUserNameToAli(Map<String, Object> map) {
        map.put("userId", SpUserUtils.getString(this, "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request re = new Request.Builder().url(UrlAddress.UpnicknameURL).post(body).build();
        Call call = okHttpClient.newCall(re);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("更新用户信息", s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUserDataFromAli();
                    }
                });
            }
        });
    }

    /*更新本地用户信息*/
    private void updateUserDataFromAli() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SpUserUtils.getString(this, "userId"));
        RequestBody body = RequestBody.create(UrlAddress.JSON, new Gson().toJson(map));
        map.clear();
        Request re = new Request.Builder().url(UrlAddress.UpgetUserInfoURL).post(body).build();
        Call call = okHttpClient.newCall(re);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("获取用户信息", s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    String user = object.getString("resultMap");
                    UserInfoGson userInfoGson = UserInfoGson.objectFromData(user);
                    SpUserUtils.putString(getBaseContext(), "headIconUrl", userInfoGson.headIconUrl);
                    SpUserUtils.putString(getBaseContext(), "nickName", userInfoGson.nickName);
                    updateToastUI(userInfoGson.headIconUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /*选取照片返回值*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                url = images.get(0).path;
                if (images.size() != 0) {
                    File file = ImgeCompress.saveimg(images.get(0).path);
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    builder.addFormDataPart("userId", SpUserUtils.getString(this, "userId"));

                    MultipartBody multipartBody = builder.build();
                    Request request = new Request.Builder().url(UrlAddress.UploadImageURL).post(multipartBody).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String s = response.body().string();
                            Log.e("图片", s);
                            JSONObject js = null;
                            try {
                                js = new JSONObject(s);
                                if (!js.getString("code").equals("1")) {
                                    updateToastUI(js.getString("message"));
                                    return;
                                }
                                Map<String, Object> map = new HashMap<>();
                                map.put("headIconUrl", js.getJSONObject("resultMap").getString("imageURL"));
                                updateUserheadiconToAli(map);
                                map.clear();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*更新UI*/
    private void updateToastUI(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getBaseContext()).load(message).into(circlimeUserPhoto);
            }
        });
    }

    @OnClick({R.id.image_common_diy_toolbar_back, R.id.tv_my_person_user_photo, R.id.circlime_user_photo,
            R.id.tv_my_person_name, R.id.tv_my_person_name_show, R.id.tv_my_person_identification,
            R.id.tv_my_person_identification_show, R.id.tv_my_person_bindwx, R.id.tv_my_person_bindwx_show,
            R.id.tv_my_person_phone_number, R.id.tv_my_person_phone_number_show, R.id.tv_my_person_withdraw_pwd,
            R.id.btn_my_person_log_off, R.id.imag_user_photo_in, R.id.image_my_person_name_in,
            R.id.image_my_person_identification_in, R.id.image_my_person_phone_number_in, R.id.tv_my_person_aboutus,
            R.id.img_my_person_aboutus_in, R.id.tv_my_person_opinion, R.id.image_my_person_opinion_in,
            R.id.image_my_person_make_instructions_in, R.id.tv_my_person_make_instructions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.tv_my_person_user_photo:
            case R.id.circlime_user_photo:
            case R.id.imag_user_photo_in:
                Intent intent = new Intent(getBaseContext(), ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.tv_my_person_name:
            case R.id.tv_my_person_name_show:
            case R.id.image_my_person_name_in:
                inputTitleDialog(this);
                break;
            case R.id.tv_my_person_identification:
            case R.id.tv_my_person_identification_show:
            case R.id.image_my_person_identification_in:
                Intent intentid = new Intent(this, Registered.class);
                startActivity(intentid);
                break;
            case R.id.tv_my_person_bindwx:
            case R.id.tv_my_person_bindwx_show:

                break;
            case R.id.tv_my_person_phone_number:
            case R.id.tv_my_person_phone_number_show:
            case R.id.image_my_person_phone_number_in:
                Intent intentPhone = new Intent(this, ChangePhone.class);
                intentPhone.putExtra("phone", SpUserUtils.getString(this, "phone"));
                startActivity(intentPhone);
                break;
            case R.id.tv_my_person_withdraw_pwd:
                Intent intentPwd = new Intent(this, PasswordSetting.class);
                startActivity(intentPwd);
                break;
            case R.id.btn_my_person_log_off:
                startActivity(new Intent(getApplicationContext(), Login.class));
                SpUserUtils.clearUserShared(getBaseContext());
                UUParkingActivity.uuParkingActivity.finish();
                finish();
                break;
            case R.id.tv_my_person_aboutus:
            case R.id.img_my_person_aboutus_in:
                Intent aboutusintent = new Intent(this, AbouteUs.class);
                startActivity(aboutusintent);
                break;
            case R.id.tv_my_person_opinion:
            case R.id.image_my_person_opinion_in:
                Intent opinionIntent = new Intent(this, Opinion.class);
                startActivity(opinionIntent);
                break;
            case R.id.image_my_person_make_instructions_in:
            case R.id.tv_my_person_make_instructions:
                break;
        }
    }
}
