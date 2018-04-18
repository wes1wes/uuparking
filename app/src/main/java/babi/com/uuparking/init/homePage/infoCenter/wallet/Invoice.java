package babi.com.uuparking.init.homePage.infoCenter.wallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.RegularExpressionUtil;
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

public class Invoice extends AppCompatActivity {


    String billRecordID;
    OkHttpClient okHttpClient;
    @BindView(R.id.cb_my_wallet_invoice_persion)
    CheckBox cbMyWalletInvoicePersion;
    @BindView(R.id.tv_my_wallet_invoice_persion)
    TextView tvMyWalletInvoicePersion;
    @BindView(R.id.cb_my_wallet_invoice_company)
    CheckBox cbMyWalletInvoiceCompany;
    @BindView(R.id.tv_my_wallet_invoice_company)
    TextView tvMyWalletInvoiceCompany;
    @BindView(R.id.et_fragment_invoice_person_addressee_name)
    EditText etFragmentInvoicePersonAddresseeName;
    @BindView(R.id.et_fragment_invoice_person_addressee_phone)
    EditText etFragmentInvoicePersonAddresseePhone;
    @BindView(R.id.et_fragment_invoice_person_addressee_location)
    EditText etFragmentInvoicePersonAddresseeLocation;
    @BindView(R.id.et_fragment_invoice_person_code)
    EditText etFragmentInvoicePersonCode;
    @BindView(R.id.et_fragment_invoice_person_name)
    EditText etFragmentInvoicePersonName;
    @BindView(R.id.et_fragment_invoice_person_num)
    EditText etFragmentInvoicePersonNum;
    @BindView(R.id.btn_fragment_invoice_person)
    Button btnFragmentInvoicePerson;
    @BindView(R.id.image_common_diy_toolbar_back)
    ImageView imageCommonDiyToolbarBack;
    @BindView(R.id.tv_common_diy_toolbar_title)
    TextView tvCommonDiyToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_wallet_invoice_layout);
        ButterKnife.bind(this);
        tvCommonDiyToolbarTitle.setText("申请发票");
        okHttpClient = new OkHttpClient();
        billRecordID = getIntent().getStringExtra("billRecordID");
    }

    @OnClick({R.id.cb_my_wallet_invoice_persion, R.id.tv_my_wallet_invoice_persion,
            R.id.cb_my_wallet_invoice_company, R.id.image_common_diy_toolbar_back,
            R.id.tv_my_wallet_invoice_company, R.id.btn_fragment_invoice_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_my_wallet_invoice_persion:
                cbMyWalletInvoiceCompany.setChecked(false);
                break;
            case R.id.image_common_diy_toolbar_back:
                finish();
                break;
            case R.id.tv_my_wallet_invoice_persion:
                cbMyWalletInvoicePersion.setChecked(true);
                cbMyWalletInvoiceCompany.setChecked(false);
                break;
            case R.id.cb_my_wallet_invoice_company:
                cbMyWalletInvoicePersion.setChecked(false);
                break;
            case R.id.tv_my_wallet_invoice_company:
                cbMyWalletInvoicePersion.setChecked(false);
                cbMyWalletInvoiceCompany.setChecked(true);
                break;
            case R.id.btn_fragment_invoice_person:
                if (!(cbMyWalletInvoicePersion.isChecked() || cbMyWalletInvoiceCompany.isChecked())) {
                    Toast.makeText(this, "请选择发票类型！", Toast.LENGTH_SHORT).show();
                } else if (etFragmentInvoicePersonAddresseeName.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写收件人姓名！", Toast.LENGTH_SHORT).show();
                } else if (etFragmentInvoicePersonAddresseePhone.getText().toString().isEmpty() || !RegularExpressionUtil.isMobile(etFragmentInvoicePersonAddresseePhone.getText().toString())) {
                    Toast.makeText(this, "请填写收件人手机号！", Toast.LENGTH_SHORT).show();
                } else if (!RegularExpressionUtil.isMobile(etFragmentInvoicePersonAddresseePhone.getText().toString())) {
                    Toast.makeText(this, "手机格式不正确", Toast.LENGTH_SHORT).show();
                } else if (etFragmentInvoicePersonAddresseeLocation.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写收件地址！", Toast.LENGTH_SHORT).show();
                } else if (etFragmentInvoicePersonCode.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写邮编！", Toast.LENGTH_SHORT).show();
                } else if (cbMyWalletInvoiceCompany.isChecked() == true && etFragmentInvoicePersonName.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写发票抬头！", Toast.LENGTH_SHORT).show();
                } else if (cbMyWalletInvoiceCompany.isChecked() == true && etFragmentInvoicePersonNum.getText().toString().isEmpty()) {
                    Toast.makeText(this, "请填写纳税人识别号！", Toast.LENGTH_SHORT).show();
                } else {
                    RequestBody body;
                    if (cbMyWalletInvoiceCompany.isChecked()) {
                        body = new FormBody.Builder()
                                .add("receiverName", etFragmentInvoicePersonAddresseeName.getText().toString())
                                .add("name", etFragmentInvoicePersonName.getText().toString())
                                .add("phoneNumber", etFragmentInvoicePersonAddresseePhone.getText().toString())
                                .add("receiverAddress", etFragmentInvoicePersonAddresseeLocation.getText().toString())
                                .add("mailCode", etFragmentInvoicePersonCode.getText().toString())
                                .add("taxSerialNum", etFragmentInvoicePersonNum.getText().toString())
                                .add("billRecordID", billRecordID)
                                .add("type", "1")
                                .build();
                    } else {
                        body = new FormBody.Builder()
                                .add("receiverName", etFragmentInvoicePersonAddresseeName.getText().toString())
                                .add("name", etFragmentInvoicePersonName.getText().toString())
                                .add("phoneNumber", etFragmentInvoicePersonAddresseePhone.getText().toString())
                                .add("receiverAddress", etFragmentInvoicePersonAddresseeLocation.getText().toString())
                                .add("mailCode", etFragmentInvoicePersonCode.getText().toString())
                                .add("taxSerialNum", etFragmentInvoicePersonNum.getText().toString())
                                .add("billRecordID", billRecordID)
                                .add("type", "0")
                                .build();
                    }
                    Request request = new Request.Builder().url(UrlAddress.applyInvoiceUrl).post(body).build();
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

                break;
        }
    }
}
