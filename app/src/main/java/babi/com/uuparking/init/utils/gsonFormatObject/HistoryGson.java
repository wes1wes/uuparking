package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/18.
 */

public class HistoryGson {

    /**
     * "orderCreateTime":"2018-03-30 18:10:19"
     * endParkTime : 2018-03-25 15:51:31
     * cost : 20
     * orderId : J18032588000002
     * sceneCode : 34
     * location : {"id":"CL1803254300000002","longitude":121.451061,"latitude":31.023574,"province":"上海市","city":"上海市市辖区","district":"闵行区","detailAddress":"东川路555号黄鹏","createTime":"2018-03-25 13:07:44","updateTime":"2018-03-25 13:07:44"}
     * startParkTime : 2018-03-25 15:51:23
     * status : 4
     */

    public String orderCreateTime;
    public String endParkTime;
    public String cost;
    public String orderId;
    public int sceneCode;
    public LocationBean location;
    public String startParkTime;
    public int status;

    public static HistoryGson objectFromData(String str) {

        return new Gson().fromJson(str, HistoryGson.class);
    }

    public static List<HistoryGson> arrayHistoryGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<HistoryGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static class LocationBean {
        /**
         * id : CL1803254300000002
         * longitude : 121.451061
         * latitude : 31.023574
         * province : 上海市
         * city : 上海市市辖区
         * district : 闵行区
         * detailAddress : 东川路555号黄鹏
         * createTime : 2018-03-25 13:07:44
         * updateTime : 2018-03-25 13:07:44
         */

        public String id;
        public double longitude;
        public double latitude;
        public String province;
        public String city;
        public String district;
        public String detailAddress;
        public String createTime;
        public String updateTime;

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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public String getEndParkTime() {
        return endParkTime;
    }

    public void setEndParkTime(String endParkTime) {
        this.endParkTime = endParkTime;
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

    public int getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(int sceneCode) {
        this.sceneCode = sceneCode;
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

    public int getStatus() {
        return status;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
