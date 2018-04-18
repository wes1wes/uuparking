package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BGD on 2018/1/20.
 * 更改2018年1月28日，增加接收值id,status
 */

public class ShareInfoGson {

    /**
     * id : 123
     * unitprice : 10
     * startTime : 20:00
     * endTime : 23:00
     * shareDay : 1;2;5
     * status : 1
     */

    public String id;
    public String unitprice;
    public String startTime;
    public String endTime;
    public String shareDay;
    public int status;

    public static ShareInfoGson objectFromData(String str) {

        return new Gson().fromJson(str, ShareInfoGson.class);
    }

    public static List<ShareInfoGson> arrayShareInfoGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<ShareInfoGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
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
