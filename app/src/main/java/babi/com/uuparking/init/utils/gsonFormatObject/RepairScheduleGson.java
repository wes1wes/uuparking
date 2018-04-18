package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/19.
 */

public class RepairScheduleGson {

    /**
     * lockId : 1
     * carparkId : 111
     * handleTime : 2018-01-19 18:10:28
     * createTime : 2018-01-19 18:10:26
     * detailAddress : sheng2市2区2详细地址2
     * status : 2
     */

    public String lockId;
    public String carparkId;
    public String handleTime;
    public String createTime;
    public String detailAddress;
    public int status;

    public static RepairScheduleGson objectFromData(String str) {

        return new Gson().fromJson(str, RepairScheduleGson.class);
    }

    public static List<RepairScheduleGson> arrayRepairScheduleGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<RepairScheduleGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getCarparkId() {
        return carparkId;
    }

    public void setCarparkId(String carparkId) {
        this.carparkId = carparkId;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
