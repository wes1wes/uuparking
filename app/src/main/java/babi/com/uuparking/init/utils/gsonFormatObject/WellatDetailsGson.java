package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/2/2.
 */

public class WellatDetailsGson {

    /**
     * money : 64
     * createTime : 1518603600000
     * payWay : 6
     * hasInvoice : 12
     * id : 44444111
     * status : 1充值状态（0，失败；1，成功）
     */

    public String money;
    public String createTime;
    public int payWay;
    public int hasInvoice;
    public String id;
    public int status;

    public static WellatDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, WellatDetailsGson.class);
    }

    public static List<WellatDetailsGson> arrayWellatDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<WellatDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(int hasInvoice) {
        this.hasInvoice = hasInvoice;
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
