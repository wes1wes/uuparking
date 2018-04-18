package babi.com.uuparking.init.homePage.infoCenter.addParking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import babi.com.uuparking.adapter.GridViewAdapter1;
import babi.com.uuparking.R;
import babi.com.uuparking.init.utils.commentUtil.ImgeCompress;
import babi.com.uuparking.init.utils.commentUtil.PicassoImageLoader;
import babi.com.uuparking.init.utils.commentUtil.UrlAddress;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by b on 2017/10/18.
 */

public class ReportForRepair extends AppCompatActivity {

    private static final int IMAGE_PICKER = 200;
    GridViewAdapter1 gridViewAdapter1;
    List<Bitmap> listbit;
    ArrayList<ImageItem> list;
    SharedPreferences user;
    OkHttpClient okHttpClient;
    @BindView(R.id.tv_my_parking_opinion)
    TextView tvMyParkingOpinion;
    @BindView(R.id.et_my_parking_opnion_phenomenon)
    EditText etMyParkingOpnionPhenomenon;
    @BindView(R.id.tv_my_parking_opnion_id)
    TextView tvMyParkingOpnionId;
    @BindView(R.id.et_my_parking_opnion_id)
    EditText etMyParkingOpnionId;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tv_my_parking_opnion_photo)
    TextView tvMyParkingOpnionPhoto;
    @BindView(R.id.gv_my_parking_opnion_photo)
    GridView gvMyParkingOpnionPhoto;
    @BindView(R.id.btn_my_parking_opnion_ok)
    Button btnMyParkingOpnionOk;
    @BindView(R.id.constraintLayout1)
    ConstraintLayout constraintLayout1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_parking_opinion_layout);
        ButterKnife.bind(this);
        listbit = new ArrayList<>();
        list = new ArrayList<>();
        user = getSharedPreferences("user_info", 0);
        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_addpic);
        listbit.add(bp);
        sb = new StringBuffer();
        okHttpClient = new OkHttpClient();

        imagePickerComfig();

        gridViewAdapter1 = new GridViewAdapter1(getApplicationContext(), listbit);
        gvMyParkingOpnionPhoto.setAdapter(gridViewAdapter1);
        gvMyParkingOpnionPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listbit.size() == (position + 1)) {
                    Intent intent = new Intent(getBaseContext(), ImageGridActivity.class);
                    startActivityForResult(intent, IMAGE_PICKER);
                }
            }
        });
    }

    private void imagePickerComfig() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(4);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @OnClick(R.id.btn_my_parking_opnion_ok)
    public void onViewClicked() {
        if (etMyParkingOpnionId.getText().toString().contains("id")) {
            Toast.makeText(getApplicationContext(), "请填写车位编号", Toast.LENGTH_SHORT).show();
        } else if (etMyParkingOpnionPhenomenon.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "请描述一下车位现象", Toast.LENGTH_SHORT).show();
        } else {
            RequestBody body = new FormBody.Builder()
                    .add("userID", user.getString("userID", ""))
                    .add("sessionID", user.getString("session", ""))
                    .add("", "").build();
            Request request = new Request.Builder().url(UrlAddress.reportForRepairURL).post(body).build();
            Call call = okHttpClient.newCall(request);
        }

    }

    StringBuffer sb;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                list.clear();
                list.addAll(images);
                if (!sb.toString().isEmpty()) {
                    sb.delete(0, sb.length());
                }
                for (int i = 0; i < list.size(); i++) {
                    File file = ImgeCompress.saveimg(list.get(i).path);
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    builder.addFormDataPart("imageType", "2").addFormDataPart("userID", user.getString("userID", ""))
                            .addFormDataPart("sessionID", user.getString("sessionID", ""));

                    MultipartBody multipartBody = builder.build();
                    Request request = new Request.Builder().url(UrlAddress.UploadImageURL).post(multipartBody).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            JSONObject js = null;
                            try {
                                js = new JSONObject(response.body().string());
                                sb.append(js.getString("imageURL")).append(",");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                listbit.clear();
                for (int x = 0; x < images.size(); x++) {
                    Bitmap bp = ImgeCompress.getimage(images.get(x).path);
                    listbit.add(bp);
                }
                Bitmap bp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_addpic);
                listbit.add(bp);
                gridViewAdapter1.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
