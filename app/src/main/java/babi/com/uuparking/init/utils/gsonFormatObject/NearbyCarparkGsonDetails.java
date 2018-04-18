package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/25.
 */

public class NearbyCarparkGsonDetails {

    /**
     * id : aaa
     * lockId : ABGBA1801000001
     * courtId : 1
     * locationId : 1
     * carparkType : 1
     * carparkSize : 1
     * hasColumn : 1
     * isOutdoor : 1
     * scenePicUrl : url
     * locationPicUrl : url
     * status : 1
     * createTime : 1515744520000
     * updateTime : 1515744524000
     * carparkLocation : {"id":"1","longitude":121.453522,"latitude":31.023554,"province":"sheng","city":"市","district":"区","detailAddress":"详细地址","createTime":1515744575000}
     */

    public String id;
    public String lockId;
    public int courtId;
    public String locationId;
    public int carparkType;
    public int carparkSize;
    public int hasColumn;
    public int isOutdoor;
    public String scenePicUrl;
    public String locationPicUrl;
    public int status;
    public long createTime;
    public long updateTime;
    public CarparkLocationBean carparkLocation;

    public static NearbyCarparkGsonDetails objectFromData(String str) {

        return new Gson().fromJson(str, NearbyCarparkGsonDetails.class);
    }

    public static List<NearbyCarparkGsonDetails> arrayNearbyCarparkGsonDetailsFromData(String str) {

        Type listType = new TypeToken<ArrayList<NearbyCarparkGsonDetails>>() {
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

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getCarparkType() {
        return carparkType;
    }

    public void setCarparkType(int carparkType) {
        this.carparkType = carparkType;
    }

    public int getCarparkSize() {
        return carparkSize;
    }

    public void setCarparkSize(int carparkSize) {
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

    public String getScenePicUrl() {
        return scenePicUrl;
    }

    public void setScenePicUrl(String scenePicUrl) {
        this.scenePicUrl = scenePicUrl;
    }

    public String getLocationPicUrl() {
        return locationPicUrl;
    }

    public void setLocationPicUrl(String locationPicUrl) {
        this.locationPicUrl = locationPicUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public CarparkLocationBean getCarparkLocation() {
        return carparkLocation;
    }

    public void setCarparkLocation(CarparkLocationBean carparkLocation) {
        this.carparkLocation = carparkLocation;
    }

    public static class CarparkLocationBean {
        /**
         * id : 1
         * longitude : 121.453522
         * latitude : 31.023554
         * province : sheng
         * city : 市
         * district : 区
         * detailAddress : 详细地址
         * createTime : 1515744575000
         */

        public String id;
        public double longitude;
        public double latitude;
        public String province;
        public String city;
        public String district;
        public String detailAddress;
        public long createTime;

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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
