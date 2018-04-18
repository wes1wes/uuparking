package babi.com.uuparking.init.homePage.infoCenter.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by b on 2018/4/2.
 */

public class PasswordSetting extends AppCompatActivity {
    @BindView(R.id.btn_mine_person_password_ok)
    Button btnMinePersonPasswordOk;
    @BindView(R.id.tv_mine_person_password_name)
    EditText tvMinePersonPasswordName;
    @BindView(R.id.tv_mine_person_password_carde)
    EditText tvMinePersonPasswordCarde;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_person_password_setting);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("设置密码");
    }

    @OnClick({R.id.btn_mine_person_password_ok, R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_mine_person_password_ok:
                if (tvMinePersonPasswordName.getText().toString()==null){
                    ToastUtil.ToastUtilmsg(this,"请输入密码");
                 return;
                }
                if (tvMinePersonPasswordCarde.getText().toString()==null){
                    ToastUtil.ToastUtilmsg(this,"请输入确认密码");
                 return;
                }
                // TODO: 2018/4/2 设置密码
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
        }
    }
}
