package babi.com.uuparking.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import babi.com.uuparking.R;

public class GuideViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private List<String> list;

    public GuideViewPagerAdapter(List<View> views, List<String> list) {
        super();
        this.views = views;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views.get(position);
        ImageView imageView = null;
        if (list.size() > 2) {
            switch (position) {
                case 0:
                    imageView = view.findViewById(R.id.wallet_details);
                    break;
                case 1:
                    imageView = view.findViewById(R.id.wallet_details);
                    break;
                case 2:
                    imageView = view.findViewById(R.id.wallet_details);
                    break;
                case 3:
                    imageView = view.findViewById(R.id.wallet_details);
                    break;
            }
            imageView.setImageURI(Uri.parse(list.get(position)));
        }
        // TODO: 2018/1/10 更改引导页
//		imageView.setImageResource();

        ((ViewPager) container).removeView(view);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

}
