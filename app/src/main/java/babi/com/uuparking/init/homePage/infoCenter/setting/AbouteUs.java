package babi.com.uuparking.init.homePage.infoCenter.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import babi.com.uuparking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

/**
 * Created by b on 2017/9/15.
 */

public class AbouteUs extends AppCompatActivity {


    @BindView(R.id.imge_my_setting_aboutus_app_photo)
    ImageView imgeMySettingAboutusAppPhoto;
    @BindView(R.id.tv_my_setting_aboutus_app_name)
    TextView tvMySettingAboutusAppName;
    @BindView(R.id.tv_my_setting_aboutus_app_versions)
    TextView tvMySettingAboutusAppVersions;
    @BindView(R.id.tv_my_setting_aboutus_wx)
    TextView tvMySettingAboutusWx;
    @BindView(R.id.tv_my_setting_aboutus_wx_show)
    TextView tvMySettingAboutusWxShow;
    @BindView(R.id.ll_my_setting_aboutus_wx)
    LinearLayout llMySettingAboutusWx;
    @BindView(R.id.tv_my_setting_aboutus_phone)
    TextView tvMySettingAboutusPhone;
    @BindView(R.id.tv_my_setting_aboutus_phone_show)
    TextView tvMySettingAboutusPhoneShow;
    @BindView(R.id.ll_my_setting_aboutus_phone)
    LinearLayout llMySettingAboutusPhone;
    @BindView(R.id.tv_my_setting_aboutus_email)
    TextView tvMySettingAboutusEmail;
    @BindView(R.id.tv_my_setting_aboutus_email_show)
    TextView tvMySettingAboutusEmailShow;
    @BindView(R.id.ll_my_setting_aboutus_email)
    LinearLayout llMySettingAboutusEmail;
    @BindView(R.id.tv_my_setting_aboutus_web)
    TextView tvMySettingAboutusWeb;
    @BindView(R.id.tv_my_setting_aboutus_web_show)
    TextView tvMySettingAboutusWebShow;
    @BindView(R.id.ll_my_setting_aboutus_web)
    LinearLayout llMySettingAboutusWeb;
    @BindView(R.id.textView)
    TextView textView;
    OkHttpClient client;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_setting_layout_aboutus);
        ButterKnife.bind(this);
        client=new OkHttpClient();

    }

    @OnClick({R.id.tv_my_setting_aboutus_app_name, R.id.tv_my_setting_aboutus_app_versions, R.id.ll_my_setting_aboutus_wx, R.id.ll_my_setting_aboutus_phone, R.id.ll_my_setting_aboutus_email, R.id.ll_my_setting_aboutus_web})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_my_setting_aboutus_app_name:
            case R.id.tv_my_setting_aboutus_app_versions:
//                检查版本是否是最新版
                break;
            case R.id.ll_my_setting_aboutus_wx:
//                进入微信公众号
                break;
            case R.id.ll_my_setting_aboutus_phone:
//               点击拨打公司电话
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "021-54391079"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                startActivity(intent);
                break;
            case R.id.ll_my_setting_aboutus_email:
//                公司邮箱
                break;
            case R.id.ll_my_setting_aboutus_web:
//                进入公司官网
                Uri uri = Uri.parse("http://www.parkuu.com/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
        }
    }
}
