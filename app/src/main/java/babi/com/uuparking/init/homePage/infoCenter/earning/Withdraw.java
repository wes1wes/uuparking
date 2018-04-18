package babi.com.uuparking.init.homePage.infoCenter.earning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/9/27.
 */

public class Withdraw extends AppCompatActivity {

    SharedPreferences user;
    String money;
    OkHttpClient okHttpClient;
    @BindView(R.id.tv_my_wallet_withdraw_come_show)
    TextView tvMyWalletWithdrawComeShow;
    @BindView(R.id.tv_my_wallet_withdraw_all)
    TextView tvMyWalletWithdrawAll;
    @BindView(R.id.et_my_wallet_withdraw_money)
    EditText etMyWalletWithdrawMoney;
    @BindView(R.id.et_my_wallet_withdraw_ali)
    EditText etMyWalletWithdrawAli;
    @BindView(R.id.et_my_wallet_withdraw_band)
    EditText etMyWalletWithdrawBand;
    @BindView(R.id.btn_my_wallet_withdraw_ok)
    Button btnMyWalletWithdrawOk;
    @BindView(R.id.textView23)
    TextView textView23;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_earnings_withdraw_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("提现");

        okHttpClient = new OkHttpClient();
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        etMyWalletWithdrawMoney.setText(money);
        tvMyWalletWithdrawComeShow.setText(money);
        user = getSharedPreferences("user_info", 0);
    }

    @OnClick({R.id.btn_my_wallet_withdraw_ok,R.id.tv_my_wallet_withdraw_all,R.id.image_common_diy_toolbar_back})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.btn_my_wallet_withdraw_ok:
                if (etMyWalletWithdrawMoney.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                } else if (Float.valueOf(etMyWalletWithdrawMoney.getText().toString()) <= 100) {
                    Toast.makeText(this, "提现最低100元哦!!", Toast.LENGTH_SHORT).show();
                } else if (etMyWalletWithdrawAli.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入银行卡号", Toast.LENGTH_SHORT).show();
                } else if (etMyWalletWithdrawBand.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请输入开户行支行名", Toast.LENGTH_SHORT).show();
                } else {
                    RequestBody body = new FormBody.Builder()
                            .add("userID", user.getString("userID", ""))
                            .add("sessionID", user.getString("sessionID", ""))
                            .add("walletID", user.getString("walletID", ""))
                            .add("money", etMyWalletWithdrawMoney.getText().toString())
                            .add("account", etMyWalletWithdrawAli.getText().toString()).build();
                    Request request = new Request.Builder().url(UrlAddress.withdrawUrl).post(body).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            finish();
                        }
                    });
                }
                break;
            case R.id.tv_my_wallet_withdraw_all:
                etMyWalletWithdrawMoney.setText(money);
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
        }
    }
}
