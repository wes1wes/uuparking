package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgd on 2018/1/17.
 * 更改2018年1月28日 更改时间返回格式
 */

public class NearbyCarports {

    /**
     * id : 3101121802082300000001
     * lockId : ABGBA1801000001
     * carparkType : 1
     * carparkSize : C
     * hasColumn : 0
     * isOutdoor : 1
     * scenePicUrl : null
     * status : 1
     * carparkLocation : {"id":"CL1802071200000001 ","longitude":121.466534,"latitude":31.028829,"province":"上海市","city":"上海市市辖区","district":"闵行区","detailAddress":"东川路测试车位"}
     * carparkShareInfo : {"id":"CS1802071200000001","unitprice":1,"startTime":"08:08","endTime":"23:59","shareDay":"1;2;3;4;5;6;7;","status":1}
     * carparkShareInfos : null
     */

    public String id;
    public String lockId;
    public int carparkType;
    public String carparkSize;
    public int hasColumn;
    public int isOutdoor;
    public Object scenePicUrl;
    public int status;
    public CarparkLocationBean carparkLocation;
    public CarparkShareInfoBean carparkShareInfo;
    public Object carparkShareInfos;

    public static NearbyCarports objectFromData(String str) {

        return new Gson().fromJson(str, NearbyCarports.class);
    }

    public static List<NearbyCarports> arrayNearbyCarportsFromData(String str) {

        Type listType = new TypeToken<ArrayList<NearbyCarports>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public int getCarparkType() {
        return carparkType;
    }

    public void setCarparkType(int carparkType) {
        this.carparkType = carparkType;
    }

    public String getCarparkSize() {
        return carparkSize;
    }

    public void setCarparkSize(String carparkSize) {
        this.carparkSize = carparkSize;
    }

    public int getHasColumn() {
        return hasColumn;
    }

    public void setHasColumn(int hasColumn) {
        this.hasColumn = hasColumn;
    }

    public int getIsOutdoor() {
        return isOutdoor;
    }

    public void setIsOutdoor(int isOutdoor) {
        this.isOutdoor = isOutdoor;
    }

    public Object getScenePicUrl() {
        return scenePicUrl;
    }

    public void setScenePicUrl(Object scenePicUrl) {
        this.scenePicUrl = scenePicUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CarparkLocationBean getCarparkLocation() {
        return carparkLocation;
    }

    public void setCarparkLocation(CarparkLocationBean carparkLocation) {
        this.carparkLocation = carparkLocation;
    }

    public CarparkShareInfoBean getCarparkShareInfo() {
        return carparkShareInfo;
    }

    public void setCarparkShareInfo(CarparkShareInfoBean carparkShareInfo) {
        this.carparkShareInfo = carparkShareInfo;
    }

    public static class CarparkLocationBean {
        /**
         * id : CL1802071200000001 
         * longitude : 121.466534
         * latitude : 31.028829
         * province : 上海市
         * city : 上海市市辖区
         * district : 闵行区
         * detailAddress : 东川路测试车位
         */

        public String id;
        public double longitude;
        public double latitude;
        public String province;
        public String city;
        public String district;
        public String detailAddress;

        public static CarparkLocationBean objectFromData(String str) {

            return new Gson().fromJson(str, CarparkLocationBean.class);
        }

        public static List<CarparkLocationBean> arrayCarparkLocationBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<CarparkLocationBean>>() {
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
    }

    public static class CarparkShareInfoBean {
        /**
         * id : CS1802071200000001
         * unitprice : 1
         * startTime : 08:08
         * endTime : 23:59
         * shareDay : 1;2;3;4;5;6;7;
         * status : 1
         */

        public String id;
        public double unitprice;
        public String startTime;
        public String endTime;
        public String shareDay;
        public int status;

        public static CarparkShareInfoBean objectFromData(String str) {

            return new Gson().fromJson(str, CarparkShareInfoBean.class);
        }

        public static List<CarparkShareInfoBean> arrayCarparkShareInfoBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<CarparkShareInfoBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(double unitprice) {
            this.unitprice = unitprice;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getShareDay() {
            return shareDay;
        }

        public void setShareDay(String shareDay) {
            this.shareDay = shareDay;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}
