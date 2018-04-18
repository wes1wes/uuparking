package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/2/7.
 */

public class CostDetailsGson {

    /**
     * planId: 1
     * cost : 0.01
     * orderCreateTime : 1517819518000
     * id : J18020560000002
     * status : 4
     */

    public Object planId;
    public String cost;
    public String orderCreateTime;
    public String id;
    public int status;

    public static CostDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, CostDetailsGson.class);
    }

    public static List<CostDetailsGson> arrayCostDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<CostDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public Object getPlanId() {
        return planId;
    }

    public void setPlanId(Object planId) {
        this.planId = planId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
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
