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
import babi.com.uuparking.init.utils.gsonFormatObject.AppointmentHistoryGson;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by b on 2017/12/26.
 */

public class AppointmentHistoryAdapter extends BaseAdapter {
    Context context;
    List<AppointmentHistoryGson> list;

    public AppointmentHistoryAdapter(Context context, List<AppointmentHistoryGson> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_lv_appointment_history, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        AppointmentHistoryGson appointmentHistoryGson = list.get(i);
        viewHolder.tvAppointmentHistoryLocation.setText(appointmentHistoryGson.getDetailAddress());
        String status;
        switch (appointmentHistoryGson.getStatus()) {
            case 1:
                status = "进入，去停车";
                break;
            case 2:
                switch (appointmentHistoryGson.getSceneCode()) {
                    case "21":
                        status = "订单超时取消";
                        break;
                    case "22":
                        status = "您取消订单";
                        break;
                    case "23":
                        status = "订单完成";
                        break;
                    default:
                        status = "其他";
                        break;
                }
                break;
            default:
                status = "其它";
                break;
        }
        viewHolder.tvAppointmentHistoryStatus.setText(status);
        viewHolder.tvAppointmentHistoryTime.setText(appointmentHistoryGson.getCreateTime());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_appointment_history_time)
        TextView tvAppointmentHistoryTime;
        @BindView(R.id.tv_appointment_history_status)
        TextView tvAppointmentHistoryStatus;
        @BindView(R.id.imageView2)
        ImageView imageView2;
        @BindView(R.id.tv_appointment_history_location)
        TextView tvAppointmentHistoryLocation;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
