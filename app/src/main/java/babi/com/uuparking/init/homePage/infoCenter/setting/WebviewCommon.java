package babi.com.uuparking.init.homePage.infoCenter.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import babi.com.uuparking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by b on 2017/10/10.
 */

public class WebviewCommon extends AppCompatActivity {

    WebSettings wSet;
    @BindView(R.id.common_webview)
    WebView commonWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_common_layout);
        ButterKnife.bind(this);
        wSet = commonWebview.getSettings();
        wSet.setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String s = intent.getStringExtra("string");
        switch (s) {
            case "1":
                commonWebview.loadUrl("http://47.92.135.66:8090/uuparking/userDelegate.html");
                break;
            case "2":
                commonWebview.loadUrl("http://47.92.135.66:8090/uuparking/depositDeclare.html");
                break;
            case "3":
                commonWebview.loadUrl("http://47.92.135.66:8090/uuparking/chargeDelegate.html");
                break;
            case "4":
                commonWebview.loadUrl("http://47.92.135.66:8090/uuparking/useIntroduction.html");
                break;
        }

    }

    @OnClick(R.id.common_webview)
    public void onViewClicked() {
    }
}
