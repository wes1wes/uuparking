package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/18.
 */

public class ApplyParkingGson {

    /**
     * createTime : 2018-01-17 14:46:02
     * carportNum : 1
     * updateTime : 2018-01-17 20:33:03
     * id : 40283681610231b1016102de0f1c0000
     * status : 1
     */

    public String createTime;
    public int carportNum;
    public String updateTime;
    public String id;
    public int status;

    public static ApplyParkingGson objectFromData(String str) {

        return new Gson().fromJson(str, ApplyParkingGson.class);
    }

    public static List<ApplyParkingGson> arrayApplyParkingGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<ApplyParkingGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCarportNum() {
        return carportNum;
    }

    public void setCarportNum(int carportNum) {
        this.carportNum = carportNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
