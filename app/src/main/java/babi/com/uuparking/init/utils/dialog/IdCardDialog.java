package babi.com.uuparking.init.utils.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.IdentityCard;
import babi.com.uuparking.init.utils.commentUtil.SpUserUtils;
import babi.com.uuparking.init.utils.commentUtil.ToastUtil;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * Created by b on 2018/2/5.
 */

public class IdCardDialog {
    public static void inputIdentificationDialog(final Context context) {
        @SuppressLint("InflateParams") final View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.my_person_identification_dialog, null);
        final AlertDialog.Builder customizeDialog;
        customizeDialog = new AlertDialog.Builder(context);
        final OkHttpClient okHttpClient=new OkHttpClient();

        customizeDialog.setTitle("实名认证");
        customizeDialog.setView(dialogView);
        customizeDialog.setCancelable(false);
        final EditText et_name =
                (EditText) dialogView.findViewById(R.id.et_my_person_identification_name);
        final EditText et_num =
                (EditText) dialogView.findViewById(R.id.et_my_person_identification_num);

        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et_name.getText().toString().isEmpty()){
                            ToastUtil.ToastUtilmsg(context,"请输入姓名！");
                            return;
                        }
                        if (et_num.getText().toString().isEmpty()){
                            ToastUtil.ToastUtilmsg(context,"请输入身份证号！");
                            return;
                        }
                        IdentityCard identityCard=new IdentityCard(et_num.getText().toString());
                        if (!identityCard.validate()){
                            ToastUtil.ToastUtilmsg(context,"身份证号输入有误！");
                            return;
                        }

                            Map<String ,Object> map=new HashMap<>();
                            map.put("realName",et_name.getText().toString());
                            map.put("idNumber",et_num.getText().toString());
                            map.put("birthday",identityCard.getBirthString());
                            map.put("userId", SpUserUtils.getString(context,"userId"));
                            map.put("sex",identityCard.getGenderCode());
                            RequestBody body =RequestBody.create(UrlAddress.JSON,new Gson().toJson(map));
                            map.clear();
                            Request re = new Request.Builder().url(UrlAddress.UUPidentifyUserUrl).post(body).build();
                            Call call = okHttpClient.newCall(re);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String string=response.body().string();
                                    JSONObject jsonObject= null;
                                    try {
                                        jsonObject = new JSONObject(string);
//                                        code:A001": "传入的参数有空值","U002":"身份证与姓名不相符, "U001""错误的身份证格式",,
                                        if (jsonObject.getString("code").equals("SUCCESS")){

                                        };
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Logger.e(string);

                                }
                            });
                    }
                });

        customizeDialog.show();
    }
}
