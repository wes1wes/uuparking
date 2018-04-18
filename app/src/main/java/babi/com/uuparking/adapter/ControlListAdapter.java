package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.gsonFormatObject.UseableParkingGson;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BGD on 2018/3/19.
 */

public class ControlListAdapter extends BaseAdapter {

    private List<UseableParkingGson> iData;
    private Context mContext;
    private int selectItem;

    public ControlListAdapter(List<UseableParkingGson> iData, Context mContext) {
        this.iData = iData;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return iData.size();
    }

    @Override
    public Object getItem(int position) {
        return iData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_elv_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UseableParkingGson useableParkingGson = iData.get(position);
        switch (useableParkingGson.getStatus()) {
            case "0":
                viewHolder.itemImageElvItemStatus.setImageResource(R.mipmap.p_no);
                break;
            case "1":
                viewHolder.itemImageElvItemStatus.setImageResource(R.mipmap.p_yes);
                break;
            default:
                viewHolder.itemImageElvItemStatus.setImageResource(R.mipmap.p_ready);
                break;
        }
        viewHolder.itemTvElvItemId.setText(useableParkingGson.getLockId());
        viewHolder.itemTvElvItemLocation.setText(useableParkingGson.getDetailAddress());
        if (selectItem == position) {
            viewHolder.itemImageElvItemSelect.setSelected(true);
            viewHolder.itemImageElvItemSelect.setPressed(true);
            viewHolder.itemImageElvItemSelect.setImageResource(R.mipmap.checked);
        } else {
            viewHolder.itemImageElvItemSelect.setSelected(false);
            viewHolder.itemImageElvItemSelect.setPressed(false);
            viewHolder.itemImageElvItemSelect.setImageResource(R.mipmap.radios);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_image_elv_item_status)
        ImageView itemImageElvItemStatus;
        @BindView(R.id.item_tv_elv_item_id)
        TextView itemTvElvItemId;
        @BindView(R.id.item_tv_elv_item_location)
        TextView itemTvElvItemLocation;
        @BindView(R.id.item_image_elv_item_select)
        ImageView itemImageElvItemSelect;
        @BindView(R.id.item_ll_elv_linear)
        LinearLayout itemLlElvLinear;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
