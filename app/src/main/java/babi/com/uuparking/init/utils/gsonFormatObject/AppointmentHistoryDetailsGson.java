package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/3/17.
 */

public class AppointmentHistoryDetailsGson {

    /**
     * expectParkLength : 60
     * shareInfo : {"id":"CS1802071200000012","unitprice":0.1,"startTime":"08:00","endTime":"23:30","shareDay":"1;2;3;4;5;6;7;","status":1}
     * city : 上海市市辖区
     * freeLength : -33660597
     * carparkType : 1
     * carparkSize : 0
     * isOutdoor : 1
     * lockId : ABGBA1801000012
     * sceneCode : 22
     * district : 闵行区
     * caparkId : 3707841802045400000012
     * expectStartParkTime : 2018-03-19 09:00:44
     * detailAddress : 紫竹数码港5号楼
     * id : Y18031999000001
     * status : 2
     * hasColumn : 0
     */

    public int expectParkLength;
    public ShareInfoBean shareInfo;
    public String city;
    public String freeLength;
    public int carparkType;
    public String carparkSize;
    public int isOutdoor;
    public String lockId;
    public int sceneCode;
    public String district;
    public String caparkId;
    public String expectStartParkTime;
    public String detailAddress;
    public String id;
    public int status;
    public int hasColumn;

    public static AppointmentHistoryDetailsGson objectFromData(String str) {

        return new Gson().fromJson(str, AppointmentHistoryDetailsGson.class);
    }

    public static List<AppointmentHistoryDetailsGson> arrayAppointmentHistoryDetailsGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<AppointmentHistoryDetailsGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static class ShareInfoBean {
        /**
         * id : CS1802071200000012
         * unitprice : 0.1
         * startTime : 08:00
         * endTime : 23:30
         * shareDay : 1;2;3;4;5;6;7;
         * status : 1
         */

        public String id;
        public double unitprice;
        public String startTime;
        public String endTime;
        public String shareDay;
        public int status;

        public static ShareInfoBean objectFromData(String str) {

            return new Gson().fromJson(str, ShareInfoBean.class);
        }

        public static List<ShareInfoBean> arrayShareInfoBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<ShareInfoBean>>() {
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

    public int getExpectParkLength() {
        return expectParkLength;
    }

    public void setExpectParkLength(int expectParkLength) {
        this.expectParkLength = expectParkLength;
    }

    public ShareInfoBean getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfoBean shareInfo) {
        this.shareInfo = shareInfo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFreeLength() {
        return freeLength;
    }

    public void setFreeLength(String freeLength) {
        this.freeLength = freeLength;
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

    public int getIsOutdoor() {
        return isOutdoor;
    }

    public void setIsOutdoor(int isOutdoor) {
        this.isOutdoor = isOutdoor;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public int getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(int sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCaparkId() {
        return caparkId;
    }

    public void setCaparkId(String caparkId) {
        this.caparkId = caparkId;
    }

    public String getExpectStartParkTime() {
        return expectStartParkTime;
    }

    public void setExpectStartParkTime(String expectStartParkTime) {
        this.expectStartParkTime = expectStartParkTime;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    public int getHasColumn() {
        return hasColumn;
    }

    public void setHasColumn(int hasColumn) {
        this.hasColumn = hasColumn;
    }
}
