package babi.com.uuparking.init.homePage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import babi.com.uuparking.R;
import babi.com.uuparking.init.homePage.infoCenter.addParking.Parking;
import babi.com.uuparking.init.homePage.infoCenter.apply.Apply;
import babi.com.uuparking.init.homePage.infoCenter.appointmentHistory.AppointmentHistory;
import babi.com.uuparking.init.homePage.infoCenter.earning.Earning;
import babi.com.uuparking.init.homePage.infoCenter.history.History;
import babi.com.uuparking.init.homePage.infoCenter.person.Person;
import babi.com.uuparking.init.homePage.infoCenter.wallet.Wallet;
import babi.com.uuparking.init.utils.DIYview.DIYBaseFragment;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by b on 2017/12/4.
 */

public class MineFragment extends DIYBaseFragment {

    @BindView(R.id.tv_mine_park)
    TextView tvMinePark;
    @BindView(R.id.tv_mine_credit_points)
    TextView tvMineCreditPoints;
    @BindView(R.id.tv_mine_add_parking)
    TextView tvMineAddParking;
    @BindView(R.id.tv_mine_money)
    TextView tvMineMoney;
    @BindView(R.id.image_mine_photo)
    ImageView imageMinePhoto;
    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.tv_mine_history)
    TextView tvMineHistory;
    @BindView(R.id.tv_mine_eanings)
    TextView tvMineEanings;
    @BindView(R.id.tv_mine_appointment_history)
    TextView tvMineAppointmentHistory;
    Unbinder unbinder;
    @BindView(R.id.tv_mine_phone)
    TextView tvMinePhone;
    @BindView(R.id.image_mine_enter)
    ImageView imageMineEnter;

    @Override
    protected void initView() {
//
    }

    @Override
    public int getLayoutId() {
        return R.layout.uuparking_mine;
    }

    @Override
    protected void getDataFromServer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        tvMineName.setText(SpUserUtils.getString(getContext(), "nickName"));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        String headIconUrl = SpUserUtils.getString(getContext(), "headIconUrl");
        if (headIconUrl == null || headIconUrl.isEmpty()) {
            headIconUrl = "http:\\\\t1.niutuku.com\\960\\34\\34-368766.jpg";
        }
        String phone=SpUserUtils.getString(getContext(), "phone");
        String phonea= phone.replace(phone.substring(3,8),"*****");
        tvMinePhone.setText(phonea);
        tvMineName.setText(SpUserUtils.getString(getContext(), "nickName"));
        Picasso.with(getContext()).load(headIconUrl).error(R.mipmap.uuparking).into(imageMinePhoto);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_mine_park, R.id.tv_mine_credit_points, R.id.tv_mine_add_parking,
            R.id.tv_mine_money, R.id.image_mine_photo, R.id.tv_mine_name, R.id.tv_mine_history,
            R.id.tv_mine_eanings, R.id.tv_mine_appointment_history,R.id.tv_mine_phone, R.id.image_mine_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mine_park:
                intentActivity(Parking.class);
                break;
            case R.id.tv_mine_credit_points:
                Toast.makeText(getContext(), "小猴子正在加紧开发哦！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_mine_add_parking:
                intentActivity(Apply.class);
                break;
            case R.id.tv_mine_money:
                intentActivity(Wallet.class);
                break;
            case R.id.image_mine_photo:
            case R.id.tv_mine_name:
            case R.id.tv_mine_phone:
            case R.id.image_mine_enter:
                intentActivity(Person.class);
                break;
            case R.id.tv_mine_history:
                intentActivity(History.class);
                break;
            case R.id.tv_mine_eanings:
                intentActivity(Earning.class);
                break;
            case R.id.tv_mine_appointment_history:
                intentActivity(AppointmentHistory.class);
                break;

        }
    }

    private void intentActivity(Class intentClass) {
        Intent earnings = new Intent(getContext(), intentClass);
        startActivity(earnings);
    }

}
