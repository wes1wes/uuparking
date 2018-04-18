package babi.com.uuparking.init.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import babi.com.uuparking.adapter.GuideViewPagerAdapter;
import babi.com.uuparking.R;
import babi.com.uuparking.init.login.Login;
import babi.com.uuparking.init.utils.commentUtil.AppConstants;
import babi.com.uuparking.init.utils.commentUtil.SpUtils;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/12/4.
 */

public class WelcomeGuideActivity extends Activity implements View.OnClickListener {

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;

    // 引导页图片资源
    private static final int[] pics = {R.layout.guid_view1,
            R.layout.guid_view2, R.layout.guid_view3};
    private static final int[] picsId = {R.id.image_guid_view1,
            R.id.image_guid_view2, R.id.image_guid_view3};
    // 底部小点图片
    private ImageView[] dots;
    List<String> list;
    // 记录当前选中位置
    private int currentIndex;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        views = new ArrayList<View>();
        list = new ArrayList<>();
        client = new OkHttpClient();
        client.newBuilder().connectTimeout(1, TimeUnit.SECONDS).build();
        internetStartPage();
    }

    private void internetStartPage() {
        RequestBody body = RequestBody.create(UrlAddress.JSON, "{\"pageType\":\"GUIDANCE_PAGE\"}");
        Request request = new Request.Builder().url(UrlAddress.UUPgetStartPageUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e("startpage---", "网路链接错误！原因：" + e.toString(), null);
                initView(new String[0]);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                JSONObject js = null;
                try {
                    js = new JSONObject(str);
                    if (js.get("code").toString().equals("1")) {
                        JSONObject jso = js.getJSONObject("resultMap");
                        String[] strings = jso.getString("PageUrl").split(";");
                      updateViewUI(strings);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateViewUI(final String[] strings) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView(strings);
            }
        });
    }

    private void initView(String[] strings) {
        // 初始化引导页视图列表
        for (int i=0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);
            ImageView imageView = view.findViewById(picsId[i]);
            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_login);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }
            if (i<strings.length){
                Picasso.with(this).load(strings[i]).into(imageView);
            }
            views.add(view);
        }
        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views, list);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());
//        initDots();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SpUtils.putBoolean(WelcomeGuideActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    private void initDots() {
//        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
//        dots = new ImageView[pics.length];
//
//        // 循环取得小点图片
//        for (int i = 0; i < pics.length; i++) {
//            // 得到一个LinearLayout下面的每一个子元素
//            dots[i] = (ImageView) ll.getChildAt(i);
//            dots[i].setEnabled(false);// 都设为灰色
//            dots[i].setOnClickListener(this);
//            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
//        }
//        currentIndex = 0;
//        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态
//    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
//    private void setCurDot(int position) {
//        if (position < 0 || position > pics.length || currentIndex == position) {
//            return;
//        }
//        dots[position].setEnabled(true);
//        dots[currentIndex].setEnabled(false);
//        currentIndex = position;
//    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterLoginActivity();
            return;
        }
        int position = (Integer) v.getTag();
        setCurView(position);
//        setCurDot(position);
    }


    private void enterLoginActivity() {
        Intent intent = new Intent(WelcomeGuideActivity.this,
                Login.class);
        startActivity(intent);
        SpUtils.putBoolean(WelcomeGuideActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置
        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
//            setCurDot(position);
        }

    }
}
