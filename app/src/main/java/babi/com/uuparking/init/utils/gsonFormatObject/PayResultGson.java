package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/2/1.
 */

public class PayResultGson {


    /**
     * lockId : 11
     * shareInfoTime : 09:00--22:00
     * carparkId : 3101121802261600000001
     * cost : 1.30
     * normalTime : 362300
     * orderId : J18032465000001
     * orderCreateTime : 1521885352000
     * overTime : null
     * adress : 上海市市辖区黄浦区东川路555号11
     * overTimeRate : 2
     * deposite : 0
     * normalParking : {"startParkTime":"2018-03-24 17:56:03","endParkTime":"2018-03-24 18:02:05","unitPrice":1.3,"normalTimeslot":"0天0时6分2秒","currentTime":"2018-03-24"}
     */

    public String lockId;
    public String shareInfoTime;
    public String carparkId;
    public String cost;
    public long normalTime;
    public String orderId;
    public long orderCreateTime;
    public String overTime;
    public String adress;
    public int overTimeRate;
    public int deposite;
    public NormalParkingBean normalParking;

    public static PayResultGson objectFromData(String str) {

        return new Gson().fromJson(str, PayResultGson.class);
    }

    public static List<PayResultGson> arrayPayResultGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<PayResultGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static class NormalParkingBean {
        /**
         * startParkTime : 2018-03-24 17:56:03
         * endParkTime : 2018-03-24 18:02:05
         * unitPrice : 1.3
         * normalTimeslot : 0天0时6分2秒
         * currentTime : 2018-03-24
         */

        public String startParkTime;
        public String endParkTime;
        public double unitPrice;
        public String normalTimeslot;
        public String currentTime;

        public static NormalParkingBean objectFromData(String str) {

            return new Gson().fromJson(str, NormalParkingBean.class);
        }

        public static List<NormalParkingBean> arrayNormalParkingBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<NormalParkingBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getStartParkTime() {
            return startParkTime;
        }

        public void setStartParkTime(String startParkTime) {
            this.startParkTime = startParkTime;
        }

        public String getEndParkTime() {
            return endParkTime;
        }

        public void setEndParkTime(String endParkTime) {
            this.endParkTime = endParkTime;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getNormalTimeslot() {
            return normalTimeslot;
        }

        public void setNormalTimeslot(String normalTimeslot) {
            this.normalTimeslot = normalTimeslot;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getShareInfoTime() {
        return shareInfoTime;
    }

    public void setShareInfoTime(String shareInfoTime) {
        this.shareInfoTime = shareInfoTime;
    }

    public String getCarparkId() {
        return carparkId;
    }

    public void setCarparkId(String carparkId) {
        this.carparkId = carparkId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public long getNormalTime() {
        return normalTime;
    }

    public void setNormalTime(long normalTime) {
        this.normalTime = normalTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getOverTimeRate() {
        return overTimeRate;
    }

    public void setOverTimeRate(int overTimeRate) {
        this.overTimeRate = overTimeRate;
    }

    public int getDeposite() {
        return deposite;
    }

    public void setDeposite(int deposite) {
        this.deposite = deposite;
    }

    public NormalParkingBean getNormalParking() {
        return normalParking;
    }

    public void setNormalParking(NormalParkingBean normalParking) {
        this.normalParking = normalParking;
    }
}
