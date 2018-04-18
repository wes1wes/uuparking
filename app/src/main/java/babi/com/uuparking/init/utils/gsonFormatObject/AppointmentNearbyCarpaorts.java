package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/4/4.
 */

public class AppointmentNearbyCarpaorts {


    /**
     * id : 3101121803259700000001
     * lockId : BBGBA1801000004
     * carparkType : 1
     * carparkSize : C
     * hasColumn : 0
     * isOutdoor : 1
     * status : 1
     * carparkLocation : {"id":"CL1803254100000001","longitude":121.454151,"latitude":31.024079,"province":"上海市","city":"上海市市辖区","district":"黄浦区","detailAddress":"东川路555号6号楼306A刘强"}
     * carparkShareInfos : [{"id":"CS1803251200000001","unitprice":20,"startTime":"01:30","endTime":"23:50","shareDay":"1;2;3;4;5;6;7;","status":1}]
     */

    public String id;
    public String lockId;
    public int carparkType;
    public String carparkSize;
    public int hasColumn;
    public int isOutdoor;
    public int status;
    public CarparkLocationBean carparkLocation;
    public List<ShareInfoGson> carparkShareInfos;

    public static AppointmentNearbyCarpaorts objectFromData(String str) {

        return new Gson().fromJson(str, AppointmentNearbyCarpaorts.class);
    }

    public static List<AppointmentNearbyCarpaorts> arrayAppointmentNearbyCarpaortsFromData(String str) {

        Type listType = new TypeToken<ArrayList<AppointmentNearbyCarpaorts>>() {
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

    public List<ShareInfoGson> getCarparkShareInfos() {
        return carparkShareInfos;
    }

    public void setCarparkShareInfos(List<ShareInfoGson> carparkShareInfos) {
        this.carparkShareInfos = carparkShareInfos;
    }

    public static class CarparkLocationBean {
        /**
         * id : CL1803254100000001
         * longitude : 121.454151
         * latitude : 31.024079
         * province : 上海市
         * city : 上海市市辖区
         * district : 黄浦区
         * detailAddress : 东川路555号6号楼306A刘强
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

}
