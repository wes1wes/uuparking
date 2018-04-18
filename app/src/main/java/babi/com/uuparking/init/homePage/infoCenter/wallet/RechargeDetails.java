package babi.com.uuparking.init.homePage.infoCenter.wallet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by b on 2017/11/24.
 */

public class RechargeDetails extends AppCompatActivity {

    String billRecordID;
    OkHttpClient okHttpClient;
    @BindView(R.id.textView14)
    TextView textView14;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.textView16)
    TextView textView16;
    @BindView(R.id.textView20)
    TextView textView20;
    @BindView(R.id.tv_my_wallet_details_order)
    TextView tvMyWalletDetailsOrder;
    @BindView(R.id.tv_my_wallet_details_time)
    TextView tvMyWalletDetailsTime;
    @BindView(R.id.tv_my_wallet_details_money)
    TextView tvMyWalletDetailsMoney;
    @BindView(R.id.tv_my_wallet_details_way)
    TextView tvMyWalletDetailsWay;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_wallet_recharge_details_layout);
        ButterKnife.bind(this);

        tvCommonDiyToolbarTitle.setText("充值结果");
        billRecordID = getIntent().getStringExtra("BillRecordID");
        okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("billRecordID", billRecordID)
                .build();
        Request request = new Request.Builder().url(UrlAddress.BillDetailUrl).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_invoice) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, Invoice.class);
            intent.putExtra("billRecordID", billRecordID);
            startActivity(intent);
        }
        return true;
    }

    @OnClick(R.id.image_common_diy_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
