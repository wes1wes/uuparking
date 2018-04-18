package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/2/2.
 */

public class EarningDetailsGson {


    /**
     * profit : 3
     * createTime : 2018-02-03 11:07:19
     * orderId : J18020377000005
     * carparkId : 123465
     */

    public String profit;
    public String createTime;
    public String orderId;
    public String carparkId;
    public String lockId;

    public static EarningDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, EarningDetailsGson.class);
    }

    public static List<EarningDetailsGson> arrayEarningDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<EarningDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }


    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCarparkId() {
        return carparkId;
    }

    public void setCarparkId(String carparkId) {
        this.carparkId = carparkId;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }
}
