package babi.com.uuparking.init.homePage.infoCenter.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.UUParkingActivity;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by b on 2017/9/6.
 */

public class Setting extends AppCompatActivity {

    @BindView(R.id.img_my_setting_user_agreement_imge)
    ImageView imgMySettingUserAgreementImge;
    @BindView(R.id.ll_my_setting_user_agreement)
    LinearLayout llMySettingUserAgreement;
    @BindView(R.id.img_my_setting_antecedent_money_imge)
    ImageView imgMySettingAntecedentMoneyImge;
    @BindView(R.id.ll_my_setting_antecedent_money)
    LinearLayout llMySettingAntecedentMoney;
    @BindView(R.id.img_my_setting_recharge_imge)
    ImageView imgMySettingRechargeImge;
    @BindView(R.id.ll_my_setting_recharge)
    LinearLayout llMySettingRecharge;
    @BindView(R.id.switch_my_setting_jpush_imge)
    Switch switchMySettingJpushImge;
    @BindView(R.id.ll_my_setting_jpush)
    LinearLayout llMySettingJpush;
    @BindView(R.id.btn_my_setting_log_off)
    Button btnMySettingLogOff;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_setting_layout);
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.ll_my_setting_user_agreement, R.id.ll_my_setting_antecedent_money, R.id.ll_my_setting_recharge, R.id.switch_my_setting_jpush_imge, R.id.ll_my_setting_jpush, R.id.btn_my_setting_log_off})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_setting_user_agreement:
                Toast.makeText(getBaseContext(),"小猴子正在加紧开发哦！",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), WebviewCommon.class);
//                intent.putExtra("string", "1");
//                startActivity(intent);
                break;
            case R.id.ll_my_setting_antecedent_money:
                Toast.makeText(getBaseContext(),"小猴子正在加紧开发哦！",Toast.LENGTH_SHORT).show();
//                Intent intent2 = new Intent(getApplicationContext(), WebviewCommon.class);
//                intent2.putExtra("string", "2");
//                startActivity(intent2);
                break;
            case R.id.ll_my_setting_recharge:
                Toast.makeText(getBaseContext(),"小猴子正在加紧开发哦！",Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(getApplicationContext(), WebviewCommon.class);
//                intent1.putExtra("string", "3");
//                startActivity(intent1);
                break;
            case R.id.switch_my_setting_jpush_imge:
                if (switchMySettingJpushImge.isChecked()) {
//                    JPushInterface.resumePush(getApplicationContext());
                } else {
//                    JPushInterface.stopPush(getApplicationContext());
                }
                break;
            case R.id.ll_my_setting_jpush:
                break;
        }
    }
}
