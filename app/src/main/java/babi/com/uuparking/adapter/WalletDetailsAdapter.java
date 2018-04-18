package babi.com.uuparking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.gsonFormatObject.WellatDetailsGson;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by b on 2017/7/21.
 */

public class WalletDetailsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<WellatDetailsGson> strings;

    public WalletDetailsAdapter(Context context, List<WellatDetailsGson> list) {
        this.mInflater = LayoutInflater.from(context);
        strings = list;
    }

    @Override
    public int getCount() {
        return strings.size();
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
        ViewHolder viewHolder;
        //观察convertView随ListView滚动情况
        Log.e("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_wallet_details,
                    null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);//绑定ViewHolder对象
        } else {
            viewHolder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        WellatDetailsGson map = strings.get(position);
  /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        switch (map.getPayWay()) {
            case 1:
                viewHolder.tvItemWalletBillDetailsPayway.setText("支付宝");
                break;
            default:
                viewHolder.tvItemWalletBillDetailsPayway.setText("微信");
                break;
        }
        switch (map.getStatus()) {
            case 0:
                viewHolder.tvItemWalletBillDetailsMoney.setTextColor(Color.RED);
                break;
            case 1:
                viewHolder.tvItemWalletBillDetailsMoney.setTextColor(Color.BLUE);
                break;
        }

        viewHolder.tvItemWalletBillDetailsMoney.setText(map.getMoney());
        viewHolder.tvItemWalletBillDetailsTime.setText(map.getCreateTime());
        viewHolder.tvItemWalletBillDetailsOrderNumber.setText(map.getId());
//        viewHolder.tvItemWalletBillDetailsType.setText("充值");
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_item_wallet_bill_details_order_number)
        TextView tvItemWalletBillDetailsOrderNumber;
        @BindView(R.id.tv_item_wallet_bill_details_time)
        TextView tvItemWalletBillDetailsTime;
        @BindView(R.id.tv_item_wallet_bill_details_payway)
        TextView tvItemWalletBillDetailsPayway;
        @BindView(R.id.tv_item_wallet_bill_details_money)
        TextView tvItemWalletBillDetailsMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
