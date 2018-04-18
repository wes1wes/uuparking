package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/3/16.
 */

public class AppointmentHistoryGson {


    /**
     * "sceneCode":22
     * createTime : 2018-03-19 10:43:29
     * expectStartParkTime : 2018-03-09 02:12:41
     * detailAddress : 上海市闵行区东川路测试车位
     * id : Y18031979000002
     * status : 2
     */

    public String sceneCode;
    public String createTime;
    public String expectStartParkTime;
    public String detailAddress;
    public String id;
    public int status;

    public static AppointmentHistoryGson objectFromData(String str) {

        return new Gson().fromJson(str, AppointmentHistoryGson.class);
    }

    public static List<AppointmentHistoryGson> arrayAppointmentHistoryGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<AppointmentHistoryGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpectStartParkTime() {
        return expectStartParkTime;
    }

    public void setExpectStartParkTime(String expectStartParkTime) {
        this.expectStartParkTime = expectStartParkTime;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }
}
