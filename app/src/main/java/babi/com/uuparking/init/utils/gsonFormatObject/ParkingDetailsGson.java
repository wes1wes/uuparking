package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/28.
 */

public class ParkingDetailsGson {

    /**
     * scenePicUrl : url
     * carparkId : aaa
     * province : sheng
     * city : 市
     * createTime : 1515744520000
     * district : 区
     * locationPicUrl : url
     * detailAddress : 详细地址
     * hasColumn : 1
     * isOutdoor : 1
     */

    public String scenePicUrl;
    public String carparkId;
    public String province;
    public String city;
    public String createTime;
    public String district;
    public String locationPicUrl;
    public String detailAddress;
    public int hasColumn;
    public int isOutdoor;

    public static ParkingDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, ParkingDetailsGson.class);
    }

    public static List<ParkingDetailsGson> arrayParkingDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<ParkingDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getScenePicUrl() {
        return scenePicUrl;
    }

    public void setScenePicUrl(String scenePicUrl) {
        this.scenePicUrl = scenePicUrl;
    }

    public String getCarparkId() {
        return carparkId;
    }

    public void setCarparkId(String carparkId) {
        this.carparkId = carparkId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocationPicUrl() {
        return locationPicUrl;
    }

    public void setLocationPicUrl(String locationPicUrl) {
        this.locationPicUrl = locationPicUrl;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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
}
