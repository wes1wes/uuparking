package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/1/19.
 */

public class CarparkShareInfoGson {


    /**
     * shareInfo : [{"shareDay":"1,2,3,6","startTime":"12:00","endTime":"19:56","unitprice":12.5},{"shareDay":"1;2;5","startTime":"20:00","endTime":"23:00","unitprice":10}]
     * status : 1
     */

    public String status;
    public List<ShareInfoGson> shareInfo;

    public static CarparkShareInfoGson objectFromData(String str) {

        return new Gson().fromJson(str, CarparkShareInfoGson.class);
    }

    public static List<CarparkShareInfoGson> arrayCarparkShareInfoGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<CarparkShareInfoGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ShareInfoGson> getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(List<ShareInfoGson> shareInfo) {
        this.shareInfo = shareInfo;
    }

}
