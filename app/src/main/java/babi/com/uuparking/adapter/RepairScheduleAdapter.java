package babi.com.uuparking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import babi.com.uuparking.R;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by b on 2017/11/13.
 */

public class RepairScheduleAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    public RepairScheduleAdapter(Context context, List<Map<String,Object>> list) {
        this.context=context;
        this.list=list;
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
        ViewHodler viewHodler=null;
        if (convertView==null){
            viewHodler=new ViewHodler();
            convertView= View.inflate(context, R.layout.item_lv_repair_schedule,null);
            viewHodler.civ_my_parking_repair_schedule= (CircleImageView) convertView.findViewById(R.id.civ_my_parking_repair_schedule);
            viewHodler.tv_item_repair_schedule_time= (TextView) convertView.findViewById(R.id.tv_item_repair_schedule_time);
            viewHodler.tv_item_repair_schedule= (TextView) convertView.findViewById(R.id.tv_item_repair_schedule);
            convertView.setTag(viewHodler);

        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }
        viewHodler.civ_my_parking_repair_schedule.setBackgroundColor(Color.rgb(100,120,120));
        viewHodler.tv_item_repair_schedule_time.setText(list.get(position).get("time").toString());
        viewHodler.tv_item_repair_schedule.setText(list.get(position).get("step").toString());

        return convertView;
    }
    class ViewHodler{
        private TextView tv_item_repair_schedule;
        private TextView tv_item_repair_schedule_time;
        private CircleImageView civ_my_parking_repair_schedule ;
    }
}
