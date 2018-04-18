package babi.com.uuparking.init.utils.commentUtil;

import okhttp3.MediaType;

/**
 * Created by b on 2017/7/20.
 */

public class UrlAddress {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType IOstream = MediaType.parse("application/octet-stream");
    /**
     * 测试
     */

//    public static String ip1 = "http://192.168.0.110:8086";//李文慧
//    public static String ip1 = "http://192.168.0.104:8086";//劉强
//    public static String ip1 = "http://192.168.0.115:8086";//朱涛
//    public static String ip1= "http://192.168.0.199:8086";//黄鹏
//    public static String ip1 = "http://192.168.0.116:8086";//网络服务器
//    public static String ip1 = "http://192.168.0.200:8086";
    public static String ip1 = "http://106.14.4.176:8086";//网络服务器

    /**
     * 启动页图片,引导页图片
     */
    public static String UUPgetStartPageUrl = ip1 + "/presentation/getStartPage";
    /**
     * 用户获取车位详情
     */
    public static String UUPparticularsUrl = ip1 + "/carpark/particulars";

    /**
     * 获取短信验证码SMS
     */
    public static String UUPgetSMSUrl = ip1 + "/user/getMessageCode";

    /**
     *预约订单记录详情
     */
    public static String UUPgetOrderPlanDetailUrl = ip1 + "/userCenter/getOrderPlanDetail";
    /**
     *预约订单记录
     */
    public static String UUPgetReservationRecUrl = ip1 + "/userCenter/getOrderPlanRec";
    /**
     *取消预约订单
     */
    public static String UUPcancelOrderPlanUrl = ip1 + "/reservationPage/cancelOrderPlan ";
    /**
     *预约订单生成即时单
     */
    public static String UUPorderPlanToOrderUrl = ip1 + "/reservationPage/orderPlanToOrder ";
    /**
     *实名认证
     */
    public static String UUPidentifyUserUrl = ip1 + "/user/identifyUser";
    /**
     * 用户注册
     */
    public static String UUPregisterUrl = ip1 + "/user/register";
    /**
     * 登录
     */
    public static String UUPloginUrl = ip1 + "/loginPage/login";
    /**
     * 微信登录
     */
    public static String UUPWXloginUrl = ip1 + "/loginPage/weChatLogin";
    /**
     *申请添加车位
     */
    public static String UUPapplyAddCarportUrl = ip1 + "/userApplication/saveUserApplication";
    /**
     *申请进度
     */
    public static String UUPfindUserApplyUrl = ip1 + "/userApplication/findUserApplication";
    /**
     *用户名下车位的可用车位
     */
    public static String UUPgetUsableCarportUrl = ip1 + "/carpark/getUsableCarportInfo";
    /**
     *用户名下车位
     */
    public static String UUPgetParkingManagementInfoUrl = ip1 + "/parkingManagementPage/getParkingManagementInfo";
    /**
     *维修中车位
     */
    public static String UUPgetRepairScheduleUrl = ip1 + "/carpark/getRepairSchedule";
    /**
     *获取车位共享
     */
    public static String UUPgetCarparkShareUrl = ip1 + "/carpark/getCarparkShareInfo";
    /**
     *保存车位共享价格时间
     */
    public static String UUPsaveShareInfoUrl = ip1 + "/carparkShareInfo/saveShareInfo";
    /**
     *删除车位共享价格时间
     */
    public static String UUPdeleteShareInfoUrl = ip1 + "/carparkShareInfo/deleteShareInfo";
    /**
     *保存车位共享开关
     */
    public static String UUPchangeCarparkUrl = ip1 + "/carpark/changeCarparkstatus";
    /**
     *用户自用控制升降
     */
    public static String UUPcontrollerUserCarparkUrl = ip1 + "/controlUserCarparkPage/controllerUserCarpark";


    /**
     *微信支付
     */
    public static String UUPWXPayUrl = ip1 + "/transaction/getWeChatOrderString";
    /**
     *支付宝支付
     */
    public static String UUPALiPayUrl = ip1 + "/transaction/getAliPayOrderString";
    /**
     *支付停车费
     */
    public static String UUPpaymentUrl = ip1 + "/paymentPage/payment";
    /**
     *充值支付
     */
    public static String UUPrechargeUrl = ip1 + "/rechargePage/recharge";
    /**
     *单一充值详情
     */
    public static String UUPrechargeparticularUrl = ip1 + "/transactionRecharge/rechargeparticular";
    /**
     *消费详情
     */
    public static String UUPConsumptiondetailsUrl = ip1 + "/order/consumptiondetails";
    /**
     *单一提现详情
     */
    public static String UUPwithdrawparticularUrl = ip1 + "/transactionWithdraw/withdrawparticular";
    /**
     *钱包信息
     */
    public static String UUPUserAccountBalanceUrl = ip1 + "/userAccount/getUserAccountBalance";

    /**
     * 用户主动取消订单
     */
    public static String UUPcancelOrderByHandUrl = ip1 + "/order/cancelOrderByHand";
    /**
     * 停车记录
     */
    public static String UUPparkingHistoryUrl = ip1+ "/order/getOrderParkingByUserId";
    /**
     * 停车记录详情
     */
    public static String UUPparkingHistoryDetailsUrl = ip1 + "/order/getParkingById";

    /**
     * 预约停车位去停车
     */
    public static String UUPcreateOrderUrl = ip1 + "/reservationPage/createOrderPlan ";
   /**
    *预约停车搜索车位 */
    public static String UUPgetAroundCarparkInCarparkUrl = ip1 + "/reservationPage/getAroundCarparkInCarpark";
    /**
    *即时停车搜索车位 */
    public static String UUPgetAroundCarparkInShareInfoUrl = ip1 + "/immediatelyParking/getAroundCarparkInShareInfo";
    /**
     * 车锁使用记录
     */
    public static String UUPgetActionLogUrl = ip1 + "/lockActionLog/getActionLog";
    /**
     * 即时停车获取单个车位详情
     */
    public static String UUPgetCarparkDetailByCarparkIdUrl = ip1 + "/carpark/getCarparkDetailByCarparkId";


    /**
     * 获取order
     */
    public static String UserUncompledteOrder = ip1 + "/userCenter/getUserUncompledteOrder";
    /**
     * 降下车锁
     */
    public static String UUPopenLockUrl = ip1 + "/order/openLock";
    /**
     * 车锁响铃
     */
    public static String UUPCallLockUrl = ip1 + "/order/callLock";
    /**
     * 升起车锁
     */
    public static String UUPcloseLockUrl = ip1+ "/order/closeLock";
    /**
     * 车位报修
     */
    public static String UUPsaveLockBreakUrl = ip1 + "/lockBreakdown/saveLockBreakdown";
    /**
     * 下订单
     */
    public static String UUPstartParkingUrl = ip1 + "/order/startParking";

    /**
     * 我的车位收益
     */
    public static String UUPmyProfitUrl = ip1 + "/userCenter/myProfit";
    /**
     * 用户指南
     */
    public static String UUPgetConstantValueUrl = ip1 + "/user/getConstantValueForKey";
    /**
     * 关于由由
     */
    public static String UUPaboutUUUrl = ip1 + "/user/aboutUUparking";
    /**
     * 意见反馈
     */
    public static String UUPfeedbackUrl = ip1 + "/user/feedback";

    //    上传图片
    public static String UploadImageURL = ip1 + "/picture/uploadImg";
    //    更新用户昵称
    public static String UpnicknameURL = ip1 + "/user/nickname";
    //    更新用户头像
    public static String UpupheadiconURL = ip1 + "/user/upheadicon";
    //    获取用户数据
    public static String UpgetUserInfoURL = ip1 + "/user/getUserInfo";


    //    更换手机号
    public static String updatePhoneNumberUrl = ip1 + "/pocketParking-01/user/updatePhoneNumber.do";
    /*钱包*/
    //    获取押金金额
    public static String getConstantValueUrl = ip1 + "/pocketParking-01/wallet/getConstantValueForKey.do";
    //    交押金
    public static String payDepositUrl = ip1 + "/pocketParking-01/wallet/payDeposit.do";
    //    退押金
    public static String refundDepositUrl = ip1 + "/pocketParking-01/wallet/refundDeposit.do";

    //    提现
    public static String withdrawUrl = ip1 + "/pocketParking-01/wallet/withdraw.do";
    //    支付宝支付接口信息
    public static String walletAliPayUrl = ip1 + "/pocketParking-01/wallet/getAliPayOrderString.do";
    //    微信支付接口信息
    public static String walletWXPayUrl = ip1 + "/pocketParking-01/wallet/getWeChatOrderString.do";

    //    充值详情界面和是否已开发票
    public static String BillDetailUrl = ip1 + "/pocketParking-01/wallet/getBillDetail.do";
    //    申请开发票
    public static String applyInvoiceUrl = ip1 + "/pocketParking-01/wallet/applyInvoice.do";


    /*上传图片*/

    //    车位报修
    public static String reportForRepairURL = ip1 + "/pocketParking-01/faultCarport/reportForRepair.do";
    //    车位报修详情
    public static String getRepairScheduleURL = ip1 + "/pocketParking-01/faultCarport/getRepairSchedule.do";


}
