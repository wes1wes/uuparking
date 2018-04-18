package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/27.
 */

public class HistoryDetailsGson {


    /**
     * lockId : AAGAA1711000009
     * endParkTime : null
     * carparkId : 333
     * cost : 36
     * orderId : 402880f361321e32016132223ac10000
     * paymentId : 222
     * carparkShareInfos : [{"shareDay":"1;2;3;4;6;7;","startTime":"00:00","endTime":"24:00","unitprice":15.2}]
     * location : {"id":"4","longitude":121.453137,"latitude":31.02384,"province":"上海","city":"闵行","district":"紫竹科学园","detailAddress":"6号楼306A","createTime":"2018-01-22 18:02:11"}
     * startParkTime : 2018-01-26 19:02:43
     */

    public String lockId;
    public String endParkTime;
    public String carparkId;
    public String cost;
    public String orderId;
    public String paymentId;
    public LocationBean location;
    public String startParkTime;
    public List<ShareInfoGson> carparkShareInfos;

    public static HistoryDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, HistoryDetailsGson.class);
    }

    public static List<HistoryDetailsGson> arrayHistoryDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<HistoryDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getEndParkTime() {
        return endParkTime;
    }

    public void setEndParkTime(String endParkTime) {
        this.endParkTime = endParkTime;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public String getStartParkTime() {
        return startParkTime;
    }

    public void setStartParkTime(String startParkTime) {
        this.startParkTime = startParkTime;
    }

    public List<ShareInfoGson> getCarparkShareInfos() {
        return carparkShareInfos;
    }

    public void setCarparkShareInfos(List<ShareInfoGson> carparkShareInfos) {
        this.carparkShareInfos = carparkShareInfos;
    }

    public static class LocationBean {
        /**
         * id : 4
         * longitude : 121.453137
         * latitude : 31.02384
         * province : 上海
         * city : 闵行
         * district : 紫竹科学园
         * detailAddress : 6号楼306A
         * createTime : 2018-01-22 18:02:11
         */

        public String id;
        public double longitude;
        public double latitude;
        public String province;
        public String city;
        public String district;
        public String detailAddress;
        public String createTime;

        public static LocationBean objectFromData(String str) {

            return new Gson().fromJson(str, LocationBean.class);
        }

        public static List<LocationBean> arrayLocationBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<LocationBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
