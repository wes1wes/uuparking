package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import babi.com.uuparking.init.utils.gsonFormatObject.ShareInfoGson;
import babi.com.uuparking.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：BGD
 * Created by b on 2018/1/11.
 */

public class ShareAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
//    CarparkShareInfoGson.ShareInfoBean shareInfoBean;
ShareInfoGson shareInfoBean;
    StringBuffer sb = new StringBuffer();
    List<ShareInfoGson> list;
    Context context;

    public ShareAdapter(Context context, List<ShareInfoGson> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_share_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Logger.e(list.get(position).toString(),holder);
        shareInfoBean = list.get(position);
        holder.tvItemMineParkingManagementPiceShow.setText(shareInfoBean.getUnitprice() + "");
        holder.tvItemMineParkingManagementTimeShow.setText(shareInfoBean.getStartTime() + "--" + shareInfoBean.getEndTime());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_item_mine_parking_management_pice_show)
        TextView tvItemMineParkingManagementPiceShow;
        @BindView(R.id.tv_item_mine_parking_management_time_show)
        TextView tvItemMineParkingManagementTimeShow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
