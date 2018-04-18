package babi.com.uuparking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import babi.com.uuparking.R;

/**
 * Created by b on 2017/10/17.
 */

public class GridViewAdapter1 extends BaseAdapter {
    Context context;
    List<Bitmap> strings;
    public GridViewAdapter1(Context context, List<Bitmap> strings){
        this.context=context;
        this.strings=strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHodler viewHodler=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_apply_recycler,null);
            viewHodler=new ViewHodler();
            viewHodler.imageView= (ImageView) convertView.findViewById(R.id.item_icon);
            convertView.setTag(viewHodler);

        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }
       viewHodler.imageView.setImageBitmap(strings.get(position));

        return convertView;
    }
    class ViewHodler{
        ImageView imageView;
    }
}
