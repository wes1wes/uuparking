package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.gsonFormatObject.HistoryGson;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by b on 2017/8/8.
 */

public class TimelineAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryGson> list;
    private LayoutInflater inflater;

    public TimelineAdapter(Context context, List<HistoryGson> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_history_listview, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HistoryGson historyGson=list.get(position);
        String timeStr = historyGson.getEndParkTime();
        String consumeStr = historyGson.getCost();
        String locationStr = historyGson.getLocation().detailAddress;
        String showtimeStr = historyGson.getOrderCreateTime();
        switch (historyGson.getStatus()){
            case 1:
                viewHolder.tvCostStatus.setText("待停车");
                viewHolder.consumeHistory.setText("--");
                viewHolder.timeHistory.setText("待停车");
                viewHolder.image.setImageResource(R.mipmap.radios);
                break;
            case 2:
                viewHolder.tvCostStatus.setText("停车中");
                viewHolder.consumeHistory.setText("计费中");
                viewHolder.timeHistory.setText("计时中");
                viewHolder.image.setImageResource(R.mipmap.radios);
                break;
            case 3:
                viewHolder.tvCostStatus.setText("待支付");
                viewHolder.consumeHistory.setText(consumeStr);
                viewHolder.timeHistory.setText(showtimeStr);
                viewHolder.image.setImageResource(R.mipmap.warn);
                break;
            case 4:
                switch (historyGson.getSceneCode()){
                    case 31:
                    case 32:
                        viewHolder.tvCostStatus.setText("订单取消");
                        viewHolder.consumeHistory.setText("--");
                        viewHolder.timeHistory.setText("未停车");
                        viewHolder.image.setImageResource(R.mipmap.radios);
                        break;
                    case 33:
                        viewHolder.tvCostStatus.setText("开锁异常");
                        viewHolder.consumeHistory.setText("--");
                        viewHolder.timeHistory.setText("未停车");
                        viewHolder.image.setImageResource(R.mipmap.radios);
                        break;
                    case 34://车锁升起
                    case 36:
                    case 35:
                    case 37:
                        viewHolder.tvCostStatus.setText("订单完成");
                        viewHolder.timeHistory.setText(timeStr);
                        viewHolder.consumeHistory.setText(consumeStr);
                        viewHolder.image.setImageResource(R.mipmap.checked);
                        break;
                }
                break;
        }
        viewHolder.showTime.setText(showtimeStr);
        viewHolder.locationHistory.setText("地址：" + locationStr);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        CircleImageView image;
        @BindView(R.id.show_time)
        TextView showTime;
        @BindView(R.id.location_history)
        TextView locationHistory;
        @BindView(R.id.time_history)
        TextView timeHistory;
        @BindView(R.id.consume_history)
        TextView consumeHistory;
        @BindView(R.id.lock_ll_history)
        LinearLayout lockLlHistory;
        @BindView(R.id.tv_cost_status)
        TextView tvCostStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}