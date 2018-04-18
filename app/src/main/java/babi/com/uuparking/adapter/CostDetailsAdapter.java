package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.gsonFormatObject.CostDetailsGson;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 2018/2/7.
 */

public class CostDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<CostDetailsGson> costDetailsGsons;

    public CostDetailsAdapter(Context context, List<CostDetailsGson> costDetailsGsons) {
        this.context = context;
        this.costDetailsGsons = costDetailsGsons;
    }

    @Override
    public int getCount() {
        return costDetailsGsons.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cost_listview, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        CostDetailsGson costDetailsGson=costDetailsGsons.get(position);
        viewHolder.tvCostOrderIdShow.setText(costDetailsGson.getId());
        viewHolder.tvCostResultShow.setText(costDetailsGson.getStatus()==4?"支付完成":"待支付");
        viewHolder.tvCostTypeShow.setText(costDetailsGson.getPlanId()==null?"即时停车":"预约停车");
        viewHolder.tvCostShowTime.setText(costDetailsGson.getOrderCreateTime());
        viewHolder.tvCostMoneyShow.setText(costDetailsGson.getCost());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_cost_show_time)
        TextView tvCostShowTime;
        @BindView(R.id.tv_cost_order_id_show)
        TextView tvCostOrderIdShow;
        @BindView(R.id.tv_cost_type_show)
        TextView tvCostTypeShow;
        @BindView(R.id.tv_cost_money_show)
        TextView tvCostMoneyShow;
        @BindView(R.id.tv_cost_result_show)
        TextView tvCostResultShow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
