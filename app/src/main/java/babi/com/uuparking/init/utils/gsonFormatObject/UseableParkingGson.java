package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by bgd on 2018/1/19.
 */

public class UseableParkingGson {

    /**
     * "lockId": "ABGBA1801000002"
     * carparkId : 123465
     * detailAddress : 上海上海上海小区1234号
     * status : 5
     * 	"lockStatus": 2
     */

    public String carparkId;
    public String lockId;
    public String detailAddress;
    public String status;
    public int lockStatus;

    public static UseableParkingGson objectFromData(String str) {

        return new Gson().fromJson(str, UseableParkingGson.class);
    }

    public static List<UseableParkingGson> arrayUseableParkingGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<UseableParkingGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
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

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
