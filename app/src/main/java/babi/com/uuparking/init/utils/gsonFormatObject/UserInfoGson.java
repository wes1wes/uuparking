package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/3/19.
 */

public class UserInfoGson {

    /**
     * accountId : UA18030251300000001
     * creditScore : 100
     * hasIdentified : 0
     * phone : 18848967907
     * nickName : null
     * headIconUrl : null
     * userId : U1803025700000002
     * identifyUser : true
     */

    public String accountId;
    public int creditScore;
    public int hasIdentified;
    public String phone;
    public String nickName;
    public String headIconUrl;
    public String userId;
    public boolean identifyUser;

    public static UserInfoGson objectFromData(String str) {

        return new Gson().fromJson(str, UserInfoGson.class);
    }

    public static List<UserInfoGson> arrayUserInfoGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<UserInfoGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public int getHasIdentified() {
        return hasIdentified;
    }

    public void setHasIdentified(int hasIdentified) {
        this.hasIdentified = hasIdentified;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isIdentifyUser() {
        return identifyUser;
    }

    public void setIdentifyUser(boolean identifyUser) {
        this.identifyUser = identifyUser;
    }
}
