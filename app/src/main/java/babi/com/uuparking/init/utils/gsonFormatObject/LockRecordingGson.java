package babi.com.uuparking.init.utils.gsonFormatObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 2018/2/1.
 */

public class LockRecordingGson {

    /**
     * lockId : AAGAA1711000009
     * actionType : 2
     * createTime : 2018-01-29 10:34:27
     * nickName : AFDS
     * action : 1
     * status : 1
     */

    public String lockId;
    public int actionType;
    public String createTime;
    public String nickName;
    public int action;
    public int status;

    public static LockRecordingGson objectFromData(String str) {
        return new Gson().fromJson(str, LockRecordingGson.class);
    }

    public static List<LockRecordingGson> arrayLockRecordingGsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<LockRecordingGson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
