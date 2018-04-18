package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.gsonFormatObject.NearbyCarports;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 2018/1/22.
 */

public class AppiontmentParkingAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<NearbyCarports> nearbyCarportsList;
    NearbyCarports nearbyCarport;

    public AppiontmentParkingAdapter(Context context, List<NearbyCarports> nearbyCarportsList, Callback callback) {
        super();
        this.context = context;
        this.nearbyCarportsList = nearbyCarportsList;
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return nearbyCarportsList.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hone_lv_parking, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        nearbyCarport = nearbyCarportsList.get(position);
        holder.tvItemHomeLvParkingAddress.setText(nearbyCarport.getCarparkLocation().getDetailAddress());
        holder.tvItemHomeLvParkingDistance.setText(nearbyCarport.getLockId());
        holder.imageItemHomeLvParkingStop.setImageResource(R.mipmap.appiontment_parking);
        holder.imageItemHomeLvParkingStop.setOnClickListener(this);
        holder.imageItemHomeLvParkingStop.setTag(position);
        if (nearbyCarport.getCarparkShareInfo() == null) {
            return convertView;
        }
        holder.tvItemHomeLvParkingType.setText((nearbyCarport.getCarparkType() == 2 ? "可充电+" : "不可充电+") +
                        (nearbyCarport.getIsOutdoor() == 1 ? "露天+" : "地下+") +
                        (nearbyCarport.getHasColumn() == 1 ? "有立柱" : "无立柱"));
        NearbyCarports.CarparkShareInfoBean shareInfoGsonList = nearbyCarport.getCarparkShareInfo();
        holder.tvItemHomeLvParkingPrice.setText(shareInfoGsonList.getUnitprice() + "");
                    holder.tvItemHomeLvParkingShareTime.setText(shareInfoGsonList.getStartTime()
                            + "--" + shareInfoGsonList.getEndTime());

        return convertView;
    }

    private Callback mCallback;

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    public interface Callback {
        public void click(View v);
    }

    class ViewHolder {
        @BindView(R.id.tv_item_home_lv_parking_address)
        TextView tvItemHomeLvParkingAddress;
        @BindView(R.id.tv_item_home_lv_parking_share_time)
        TextView tvItemHomeLvParkingShareTime;
        @BindView(R.id.tv_item_home_lv_parking_price)
        TextView tvItemHomeLvParkingPrice;
        @BindView(R.id.tv_item_home_lv_parking_distance)
        TextView tvItemHomeLvParkingDistance;
        @BindView(R.id.tv_item_home_lv_parking_type)
        TextView tvItemHomeLvParkingType;
        @BindView(R.id.textView53)
        TextView textView53;
        @BindView(R.id.image_item_home_lv_parking_stop)
        ImageView imageItemHomeLvParkingStop;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
