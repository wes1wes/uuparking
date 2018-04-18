package babi.com.uuparking.init.utils.commentUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import babi.com.uuparking.R;

/**
 * Created by b on 2017/10/30.
 */

public class RequsetPermissionUtil {
    Context context;
    Activity activity;


    /**
     * 向用户说明为什么需要此权限,若权限没有开启，则请求权限
     */
    public void requestPermission(Context context, Activity activity) {
        this.activity=activity;
        this.context=context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //第一次被拒绝后，第二次访问时，向用户说明为什么需要此权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(context, "开启后使用定位功能", Toast.LENGTH_SHORT).show();
            }
            //若权限没有开启，则请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //第一次被拒绝后，第二次访问时，向用户说明为什么需要此权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "开启后使用相机功能", Toast.LENGTH_SHORT).show();
            }
            //若权限没有开启，则请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
            //第一次被拒绝后，第二次访问时，向用户说明为什么需要此权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_SETTINGS)) {
                Toast.makeText(context, "开启后使用相机功能", Toast.LENGTH_SHORT).show();
            }
            //若权限没有开启，则请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_SETTINGS}, 3);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //第一次被拒绝后，第二次访问时，向用户说明为什么需要此权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_WIFI_STATE)) {
                Toast.makeText(context, "开启后访问wifi", Toast.LENGTH_SHORT).show();
            }
            //若权限没有开启，则请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }
        if (Build.VERSION.SDK_INT >= 24){

        }
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

//    /**
//     * Open camera
//     */
//    private void showCameraAction() {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission
//                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    getString(R.string.mis_permission_rationale_write_storage),
//                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
//        } else {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (intent.resolveActivity(activity.getPackageManager()) != null) {
//                try {
//                    mTmpFile = FileUtils.createTmpFile(activity);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Uri imageUri;
//                if (mTmpFile != null && mTmpFile.exists()) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        String authority = activity.getPackageName() + ".provider";
//                        imageUri = FileProvider.getUriForFile(activity, authority, mTmpFile);
//                    } else {
//                        imageUri = Uri.fromFile(mTmpFile);
//                    }
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    startActivityForResult(intent, REQUEST_CAMERA);
//                } else {
//                    Toast.makeText(getActivity(), R.string.mis_error_image_not_exist, Toast
//                            .LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getActivity(), R.string.mis_msg_no_camera, Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }

    //----------以下动态获取权限---------


    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    /**
     * 检查权限
     *
     * @param
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        //获取权限列表
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            //list.toarray将集合转化为数组
            ActivityCompat.requestPermissions(activity,
                    needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }


    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(activity,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {      //没有授权
                showMissingPermissionDialog();              //显示提示信息
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }


    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        context.startActivity(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            this.finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
