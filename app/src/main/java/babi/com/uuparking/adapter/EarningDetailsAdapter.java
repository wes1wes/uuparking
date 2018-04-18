package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.init.utils.gsonFormatObject.EarningDetailsGson;
import babi.com.uuparking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by b on 2018/2/2.
 */

public class EarningDetailsAdapter extends BaseAdapter {
    Context mcontext;
    List<EarningDetailsGson> list;

    public EarningDetailsAdapter(Context mcontext, List<EarningDetailsGson> list) {
        this.mcontext = mcontext;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_earning_listview, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        EarningDetailsGson earningDetailsGson=list.get(position);
        viewHolder.tvEarningShowTime.setText(earningDetailsGson.getCreateTime());
        viewHolder.tvEarningMoney.setText(earningDetailsGson.getProfit());
        viewHolder.tvEarningCarparkId.setText(earningDetailsGson.getLockId());
        viewHolder.tvEarningOrderId.setText(earningDetailsGson.getOrderId());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_earning_show_time)
        TextView tvEarningShowTime;
        @BindView(R.id.tv_earning_order_id)
        TextView tvEarningOrderId;
        @BindView(R.id.tv_earning_carpark_id)
        TextView tvEarningCarparkId;
        @BindView(R.id.tv_earning_money)
        TextView tvEarningMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
