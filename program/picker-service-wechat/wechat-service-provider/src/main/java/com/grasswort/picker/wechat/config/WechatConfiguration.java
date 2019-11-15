package com.grasswort.picker.wechat.config;

import com.binarywang.spring.starter.wxjava.mp.config.WxMpAutoConfiguration;
import com.grasswort.picker.wechat.service.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

/**
 * @author xuliangliang
 * @Classname WechatConfiguration
 * @Description 微信配置
 * @Date 2019/11/14 15:27
 * @blame Java Team
 */
@Configuration
@Import(WxMpAutoConfiguration.class)
public class WechatConfiguration {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private KfSessionHandler kfSessionHandler;
    @Autowired
    private LocationHandler locationHandler;
    @Autowired
    private LogHandler logHandler;
    @Autowired
    private MenuHandler menuHandler;
    @Autowired
    private MsgHandler msgHandler;
    @Autowired
    private NullHandler nullHandler;
    @Autowired
    private StoreCheckNotifyHandler storeCheckNotifyHandler;
    @Autowired
    private SubscribeHandler subscribeHandler;
    @Autowired
    private UnsubscribeHandler unsubscribeHandler;
    @Autowired
    private ScanHandler scanHandler;

    /**
     * 消息处理 router
     * @return WxMpMessageRouter
     */
    @Bean
    public WxMpMessageRouter wxMpMessageRouter() {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 记录所有事件的日志
        router.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(this.kfSessionHandler).end();
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(this.kfSessionHandler).end();
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler).end();

        // 门店审核事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(this.storeCheckNotifyHandler)
                .end();

        // 自定义菜单事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler)
                .end();

        // 取消关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.UNSUBSCRIBE).handler(this.unsubscribeHandler)
                .end();

        // 上报地理位置事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
                .handler(this.locationHandler).end();

        // 扫码事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        router.rule().async(false).handler(this.msgHandler).end();

        return router;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        return client;
    }

}
