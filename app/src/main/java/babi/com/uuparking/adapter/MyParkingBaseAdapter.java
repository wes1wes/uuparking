package babi.com.uuparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.init.utils.gsonFormatObject.UserParkingGson;
import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.TimeCompare;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by b on 2017/10/24.
 */

public class MyParkingBaseAdapter extends BaseExpandableListAdapter {
    private List<String> gData;
    private Context mContext;
    UserParkingGson userParkingGson=new UserParkingGson();

    public MyParkingBaseAdapter(List<String> gData, UserParkingGson userParkingGson, Context mContext) {
        this.gData = gData;
        this.userParkingGson = userParkingGson;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int i = 0;
        if (userParkingGson==null){
            return 0;
        }
        switch (groupPosition) {
            case 0:
                if (userParkingGson.getUsableCarporList()==null) {
                    return 0;
                }
                i = userParkingGson.getUsableCarporList().size();
                break;
            case 1:
                if (userParkingGson.getApplicationList()==null) {
                    return 0;
                }
                i = userParkingGson.getApplicationList().size();
                break;
            case 2:
                if (userParkingGson.getRepairScheduleList()==null) {
                    return 0;
                }
                i = userParkingGson.getRepairScheduleList().size();
                break;
        }
        return i;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_mine_parking_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.image_my_parking_group = (ImageView) convertView.findViewById(R.id.image_my_parking_group);
            groupHolder.tv_my_parking_group = (TextView) convertView.findViewById(R.id.tv_my_parking_group);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
//判断isExpanded就可以控制是按下还是关闭，同时更换图片
        if (isExpanded) {
            groupHolder.image_my_parking_group.setBackgroundResource(R.mipmap.offlinearrow_down);
        } else {
            groupHolder.image_my_parking_group.setBackgroundResource(R.mipmap.rightarrow);
        }
        groupHolder.tv_my_parking_group.setText("" + gData.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        switch (groupPosition) {
            case 0:
                ViewHolderItemParking itemHolder;//
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_parking_listview, parent, false);
                itemHolder = new ViewHolderItemParking();
                itemHolder.circlimge_parking_state = (CircleImageView) convertView.findViewById(R.id.circlimge_parking_state);
                itemHolder.item_lv_parkingname = (TextView) convertView.findViewById(R.id.item_lv_parkingname);
                itemHolder.item_lv_parkingaddress = (TextView) convertView.findViewById(R.id.item_lv_parkingaddress);
                UserParkingGson.UsableCarporListBean useableParkingGson = userParkingGson.getUsableCarporList().get(childPosition);

                switch (useableParkingGson.getStatus() + "") {
                    case "0":
                        itemHolder.circlimge_parking_state.setImageResource(R.color.colorBabiButtonGray);
                        break;
                    case "1":
                        itemHolder.circlimge_parking_state.setImageResource(R.color.colorBabiGreen);
                        break;
                    case "2":
                        itemHolder.circlimge_parking_state.setImageResource(R.color.colorBabiYellow);
                        break;
                    case "3":
                        itemHolder.circlimge_parking_state.setImageResource(R.color.colorBabiRed);
                        break;
                    case "4":
                        itemHolder.circlimge_parking_state.setImageResource(R.color.colorBlack);
                        break;
                }
                itemHolder.item_lv_parkingname.setText(useableParkingGson.getLockId());
                itemHolder.item_lv_parkingaddress.setText(useableParkingGson.getDetailAddress());
                return convertView;

            case 1:
                ViewHolderItemApply itemApply;

                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_parking_apply_progress, parent, false);
                itemApply = new ViewHolderItemApply();
                itemApply.tv_item_parking_apply_create_time = (TextView) convertView.findViewById(R.id.tv_item_parking_apply_create_time);
                itemApply.tv_item_parking_apply_update_time = (TextView) convertView.findViewById(R.id.tv_item_parking_apply_update_time);
                itemApply.tv_item_parking_apply_id = (TextView) convertView.findViewById(R.id.tv_item_parking_apply_id);
                itemApply.tv_item_parking_apply_number = (TextView) convertView.findViewById(R.id.tv_item_parking_apply_number);

                UserParkingGson.ApplicationListBean applyParkingGson = userParkingGson.getApplicationList().get(childPosition);
                itemApply.tv_item_parking_apply_id.setText("" + applyParkingGson.getId().toString());
                itemApply.tv_item_parking_apply_create_time.setText(applyParkingGson.getCreateTime());
                itemApply.tv_item_parking_apply_update_time.setText(applyParkingGson.getUpdateTime());
                itemApply.tv_item_parking_apply_number.setText(applyParkingGson.getCarportNum() + "");
                return convertView;
            default:
                ViewHolderItemRepair viewHolderItemRepair;
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_parking_repair_progress, parent, false);
                viewHolderItemRepair = new ViewHolderItemRepair();
                viewHolderItemRepair.tv_item_parking_repair_create_time = (TextView) convertView.findViewById(R.id.tv_item_parking_repair_create_time);
                viewHolderItemRepair.tv_item_parking_repair_update_time = (TextView) convertView.findViewById(R.id.tv_item_parking_repair_update_time);
                viewHolderItemRepair.tv_item_parking_repair_id = (TextView) convertView.findViewById(R.id.tv_item_parking_repair_id);
                viewHolderItemRepair.tv_item_parking_repair_number = (TextView) convertView.findViewById(R.id.tv_item_parking_repair_number);

                UserParkingGson.RepairScheduleListBean repairScheduleGson = userParkingGson.getRepairScheduleList().get(childPosition);
                viewHolderItemRepair.tv_item_parking_repair_id.setText(repairScheduleGson.getCarparkId()
                        + "suoid" + repairScheduleGson.getLockId() + "status" + repairScheduleGson.getStatus());
                viewHolderItemRepair.tv_item_parking_repair_create_time.setText(repairScheduleGson.getCreateTime());
                viewHolderItemRepair.tv_item_parking_repair_update_time.setText(repairScheduleGson.getHandleTime());
                viewHolderItemRepair.tv_item_parking_repair_number.setText(repairScheduleGson.getDetailAddress());
                return convertView;

        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolderGroup {
        private TextView tv_my_parking_group;
        private ImageView image_my_parking_group;
    }

    private static class ViewHolderItemParking {
        private CircleImageView circlimge_parking_state;
        private TextView item_lv_parkingname;
        private TextView item_lv_parkingaddress;
    }

    private static class ViewHolderItemApply {

        private TextView tv_item_parking_apply_id;
        private TextView tv_item_parking_apply_number;
        private TextView tv_item_parking_apply_create_time;
        private TextView tv_item_parking_apply_update_time;
    }

    private static class ViewHolderItemRepair {

        private TextView tv_item_parking_repair_id;
        private TextView tv_item_parking_repair_number;
        private TextView tv_item_parking_repair_create_time;
        private TextView tv_item_parking_repair_update_time;
    }
}
