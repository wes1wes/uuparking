package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/2/1.
 */

public class UserParkingGson {

    public List<UsableCarporListBean> usableCarporList;
    public List<RepairScheduleListBean> RepairScheduleList;
    public List<ApplicationListBean> applicationList;

    public static UserParkingGson objectFromData(String str) {

        return new Gson().fromJson(str, UserParkingGson.class);
    }

    public static List<UserParkingGson> arrayUserParkingGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<UserParkingGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public List<UsableCarporListBean> getUsableCarporList() {
        return usableCarporList;
    }

    public void setUsableCarporList(List<UsableCarporListBean> usableCarporList) {
        this.usableCarporList = usableCarporList;
    }

    public List<RepairScheduleListBean> getRepairScheduleList() {
        return RepairScheduleList;
    }

    public void setRepairScheduleList(List<RepairScheduleListBean> repairScheduleList) {
        RepairScheduleList = repairScheduleList;
    }

    public List<ApplicationListBean> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(List<ApplicationListBean> applicationList) {
        this.applicationList = applicationList;
    }

    public static class UsableCarporListBean {
        /**
         *  "lockId": "ABGBA1801000002"
         * carparkId : 123465
         * detailAddress : sheng2市2区2详细地址2
         * status : 0
         * "lockStatus": 2,
         */

        public String carparkId;
        public String detailAddress;
        public String status;
        public String lockId;
        public String lockStatus;

        public static UsableCarporListBean objectFromData(String str) {

            return new Gson().fromJson(str, UsableCarporListBean.class);
        }

        public static List<UsableCarporListBean> arrayUsableCarporListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<UsableCarporListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getLockStatus() {
            return lockStatus;
        }

        public void setLockStatus(String lockStatus) {
            this.lockStatus = lockStatus;
        }

        public String getLockId() {
            return lockId;
        }

        public void setLockId(String lockId) {
            this.lockId = lockId;
        }

        public String getCarparkId() {
            return carparkId;
        }

        public void setCarparkId(String carparkId) {
            this.carparkId = carparkId;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class RepairScheduleListBean {
        /**
         * lockId : AAGAA1711000008
         * carparkId : 111
         * detailAddress : 上海市市辖区闵行区东川路555号
         * status : 2
         * createTime : 2018-02-01 15:57:37
         * handleTime : 2018-02-01 15:57:39
         */

        public String lockId;
        public String carparkId;
        public String detailAddress;
        public int status;
        public String createTime;
        public String handleTime;

        public static RepairScheduleListBean objectFromData(String str) {

            return new Gson().fromJson(str, RepairScheduleListBean.class);
        }

        public static List<RepairScheduleListBean> arrayRepairScheduleListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<RepairScheduleListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getLockId() {
            return lockId;
        }

        public void setLockId(String lockId) {
            this.lockId = lockId;
        }

        public String getCarparkId() {
            return carparkId;
        }

        public void setCarparkId(String carparkId) {
            this.carparkId = carparkId;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getHandleTime() {
            return handleTime;
        }

        public void setHandleTime(String handleTime) {
            this.handleTime = handleTime;
        }
    }

    public static class ApplicationListBean {
        /**
         * createTime : 2018-01-17 20:34:01
         * carportNum : 3
         * updateTime : 2018-01-17 20:34:01
         * id : 4028368161041af80161041ca3100000
         * status : 1
         */

        public String createTime;
        public int carportNum;
        public String updateTime;
        public String id;
        public int status;

        public static ApplicationListBean objectFromData(String str) {

            return new Gson().fromJson(str, ApplicationListBean.class);
        }

        public static List<ApplicationListBean> arrayApplicationListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<ApplicationListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getCarportNum() {
            return carportNum;
        }

        public void setCarportNum(int carportNum) {
            this.carportNum = carportNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
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
}
