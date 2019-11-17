package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserSettingService;
import com.grasswort.picker.user.constants.*;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.entity.UserConfig;
import com.grasswort.picker.user.dao.persistence.UserConfigMapper;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.*;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname UserSettingServiceImpl
 * @Description 用户设置
 * @Date 2019/11/4 15:55
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserSettingServiceImpl implements IUserSettingService {

    @Autowired UserConfigMapper userConfigMapper;

    @Autowired UserMapper userMapper;
    /**
     * 保存用户设置
     *
     * @param saveSettingRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public SaveSettingResponse saveSetting(SaveSettingRequest saveSettingRequest) {
        SaveSettingResponse settingResponse = new SaveSettingResponse();

        Long pkUserId = saveSettingRequest.getUserId();

        Example example = new Example(UserConfig.class);
        example.createCriteria().andEqualTo("pkUserId", pkUserId);
        UserConfig userConfig = userConfigMapper.selectOneByExample(example);
        boolean insert = userConfig == null;
        Date now = DateTime.now().toDate();
        if (insert) {
            userConfig = new UserConfig();
            userConfig.setGmtCreate(now);
            userConfig.setPkUserId(saveSettingRequest.getUserId());
        }
        userConfig.setMarkdownTheme(saveSettingRequest.getMarkdownTheme());
        userConfig.setSafetyCheckMode(saveSettingRequest.getSafetyCheckMode());
        userConfig.setOpenBlogPush(saveSettingRequest.getOpenBlogPush());
        userConfig.setBlogPushMode(saveSettingRequest.getBlogPushMode());
        userConfig.setGmtModified(now);

        if (insert) {
            userConfigMapper.insert(userConfig);
        } else {
            userConfigMapper.updateByPrimaryKey(userConfig);
        }

        settingResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        settingResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return settingResponse;
    }

    /**
     * 获取用户配置
     *
     * @param getSettingRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public GetSettingResponse getSetting(GetSettingRequest getSettingRequest) {
        GetSettingResponse getSettingResponse = new GetSettingResponse();

        Long userId = getSettingRequest.getUserId();
        Example example = new Example(UserConfig.class);
        example.createCriteria().andEqualTo("pkUserId", userId);
        UserConfig userConfig = userConfigMapper.selectOneByExample(example);

        getSettingResponse.setMarkdownTheme(Optional.ofNullable(userConfig).map(UserConfig::getMarkdownTheme).orElse(MarkdownTheme.GITHUB));
        getSettingResponse.setSafetyCheckMode(Optional.ofNullable(userConfig).map(UserConfig::getSafetyCheckMode).orElse(SafetyCheckMode.EMAIL.getId()));
        getSettingResponse.setOpenBlogPush(Optional.ofNullable(userConfig).map(UserConfig::isOpenBlogPush).orElse(false));
        getSettingResponse.setBlogPushMode(Optional.ofNullable(userConfig).map(UserConfig::getBlogPushMode).orElse(BlogPushMode.EMAIL.getId()));

        getSettingResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        getSettingResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());

        return getSettingResponse;
    }

    /**
     * 获取博客推送设置
     *
     * @param blogPushSettingRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogPushSettingResponse getBlogPushSetting(BlogPushSettingRequest blogPushSettingRequest) {
        BlogPushSettingResponse blogPushSettingResponse = new BlogPushSettingResponse();

        Long userId = blogPushSettingRequest.getUserId();
        Example example = new Example(UserConfig.class);
        example.createCriteria().andEqualTo("pkUserId", userId);
        UserConfig userConfig = userConfigMapper.selectOneByExample(example);

        boolean openBlogPush = Optional.ofNullable(userConfig).map(UserConfig::isOpenBlogPush).orElse(false);
        BlogPushMode blogPushMode = Optional.ofNullable(userConfig)
                .map(UserConfig::getBlogPushMode)
                .map(mode -> Arrays.stream(BlogPushMode.values()).filter(m -> Objects.equals(m.getId(), mode)).findFirst().get())
                .orElse(BlogPushMode.EMAIL);

        blogPushSettingResponse.setOpenBlogPush(openBlogPush);
        blogPushSettingResponse.setMode(blogPushMode);
        if (openBlogPush) {
            User user = userMapper.selectByPrimaryKey(userId);
            switch (blogPushMode) {
                case EMAIL:
                    blogPushSettingResponse.setEmail(user.getEmail());
                    break;
                case WX:
                    blogPushSettingResponse.setOpenId(user.getMpOpenId());
                    break;
                default:
                    // ignore
                    break;
            }
        }
        blogPushSettingResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        blogPushSettingResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());

        return blogPushSettingResponse;
    }
}
