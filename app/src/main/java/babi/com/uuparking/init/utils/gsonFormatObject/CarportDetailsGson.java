package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/19.
 */

public class CarportDetailsGson {

    /**
     * lockId : 1
     * shareInfo : [{"shareDay":"1,2,3,6","startTime":"12:00","endTime":"19:56","unitprice":12.5},{"shareDay":"1,2,5","startTime":"20:00","endTime":"23:00","unitprice":10}]
     * scencePicUrl : url
     * province : sheng2
     * city : 市2
     * district : 区2
     * detailAddress : 详细地址2
     * carparkType : 1
     * isOutDoor : 1
     * carparkSize : 1
     * status : 1
     * hasColumn : 1
     */

    public String lockId;
    public String scencePicUrl;
    public String province;
    public String city;
    public String district;
    public String detailAddress;
    public int carparkType;
    public int isOutDoor;
    public String carparkSize;
    public int status;
    public int hasColumn;
    public double longitude;
    public double latitude;
    public List<ShareInfoGson> shareInfo;

    public static CarportDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, CarportDetailsGson.class);
    }

    public static List<CarportDetailsGson> arrayCarportDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<CarportDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
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

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getScencePicUrl() {
        return scencePicUrl;
    }

    public void setScencePicUrl(String scencePicUrl) {
        this.scencePicUrl = scencePicUrl;
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

    public int getCarparkType() {
        return carparkType;
    }

    public void setCarparkType(int carparkType) {
        this.carparkType = carparkType;
    }

    public int getIsOutDoor() {
        return isOutDoor;
    }

    public void setIsOutDoor(int isOutDoor) {
        this.isOutDoor = isOutDoor;
    }

    public String getCarparkSize() {
        return carparkSize;
    }

    public void setCarparkSize(String carparkSize) {
        this.carparkSize = carparkSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasColumn() {
        return hasColumn;
    }

    public void setHasColumn(int hasColumn) {
        this.hasColumn = hasColumn;
    }

    public List<ShareInfoGson> getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(List<ShareInfoGson> shareInfo) {
        this.shareInfo = shareInfo;
    }

}
