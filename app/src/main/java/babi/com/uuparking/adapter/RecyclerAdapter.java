package babi.com.uuparking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import babi.com.uuparking.R;

/**
 * Created by b on 2017/8/9.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {


    /**
     * 继承RecyclerView.Adapter,用于显示数据
     * 需要定义并且使用 ViewHolder ,必须要使用
     */

    //自定义监听事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    private Context context;
    private List<Bitmap> strings;

    public RecyclerAdapter(List<Bitmap> strings, Context context) {
        this.strings = strings;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (strings != null) {
            ret = strings.size();
        }
        return ret;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder holder = null;
        // 不需要检查是否复用，因为只要进入此方法，必然没有复用
        // 因为RecyclerView 通过Holder检查复用
        View v = LayoutInflater.from(context).inflate(R.layout.item_apply_recycler, viewGroup, false);
        holder = new ViewHolder(v);
        //给布局设置点击和长点击监听
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.imageView.setImageBitmap(strings.get(i));
        //判断是否设置了监听器
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position); // 2
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView, position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }

    }
}

/**
 * 创建自己的ViewHolder ，必须要继承RecyclerView.ViewHolder
 */
class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public ViewHolder(View itemView) {
        super(itemView);
        // 通常ViewHolder的构造，就是用于获取控件视图的
        imageView = (ImageView) itemView.findViewById(R.id.item_icon);
        // TODO 后续处理点击事件的操作
    }

}



