package babi.com.uuparking.init.utils.okhttpParameterObject;


/**
 * Created by b on 2018/3/10.
 */

public class SearchParameterOPO {
    private String longitude;
    private String latitude;
    private Byte carparkType;// 车位类型()
    private String expectStartTime;// 预计开始时间 （即时停车，就是当前时间+保留时间（后台算）；预约停车，输入的预计开始停车时间（前台传yyyy-mm-dd hh:mm:ss））
    private Integer expectParkLength;// 预计停车时长
    private Integer availablePrice;// 可接受价格（预约停车中有）
    private Integer delayLength;// 延长时长（预计开始停车时间之后多长时间有车位共享）
    private Integer searchScope;// 搜索范围 （米）
    private Byte status;// 车位状态
    private String searchType;//搜索类型 "J"即使停车，"Y"预约停车；

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Byte getCarparkType() {
        return carparkType;
    }

    public void setCarparkType(Byte carparkType) {
        this.carparkType = carparkType;
    }

    public String getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(String expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public Integer getExpectParkLength() {
        return expectParkLength;
    }

    public void setExpectParkLength(Integer expectParkLength) {
        this.expectParkLength = expectParkLength;
    }

    public Integer getAvailablePrice() {
        return availablePrice;
    }

    public void setAvailablePrice(Integer availablePrice) {
        this.availablePrice = availablePrice;
    }

    public Integer getDelayLength() {
        return delayLength;
    }

    public void setDelayLength(Integer delayLength) {
        this.delayLength = delayLength;
    }

    public Integer getSearchScope() {
        return searchScope;
    }

    public void setSearchScope(Integer searchScope) {
        this.searchScope = searchScope;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
