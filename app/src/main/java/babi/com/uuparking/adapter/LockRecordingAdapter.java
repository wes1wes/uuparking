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
import babi.com.uuparking.init.utils.gsonFormatObject.LockRecordingGson;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 2018/2/1.
 */

public class LockRecordingAdapter extends BaseAdapter {

    Context mcontext;
    List<LockRecordingGson> list;

    public LockRecordingAdapter(Context mcontext, List<LockRecordingGson> list) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_parking_management_lv_lock_recording, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LockRecordingGson lockRecordingGson = list.get(position);
        viewHolder.tvItemParkingManagementLockRecordingTime.setText(lockRecordingGson.getCreateTime());
        viewHolder.tvItemParkingManagementLockRecordingUser.setText(lockRecordingGson.getActionType() == 1 ? "自用" : ("共享：" + lockRecordingGson.getNickName()));
        viewHolder.tvItemParkingManagementLockRecordingAction.setText((lockRecordingGson.getAction() == 2 ? "车锁升起" : "车锁降下") + (lockRecordingGson.getStatus() == 1 ? "成功" : "失败"));

        return convertView;
    }
    class ViewHolder {
        @BindView(R.id.tv_item_parking_management_lock_recording_time)
        TextView tvItemParkingManagementLockRecordingTime;
        @BindView(R.id.tv_item_parking_management_lock_recording_user)
        TextView tvItemParkingManagementLockRecordingUser;
        @BindView(R.id.tv_item_parking_management_lock_recording_action)
        TextView tvItemParkingManagementLockRecordingAction;
        @BindView(R.id.imageView3)
        ImageView imageView3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
