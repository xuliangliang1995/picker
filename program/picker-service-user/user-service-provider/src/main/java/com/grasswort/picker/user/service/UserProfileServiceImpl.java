package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserProfileService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.UserProfile;
import com.grasswort.picker.user.dao.persistence.UserProfileMapper;
import com.grasswort.picker.user.dto.*;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname UserProfileServiceImpl.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserProfileServiceImpl implements IUserProfileService {

    @Autowired UserProfileMapper userProfileMapper;

    /**
     * 修改个人简介
     *
     * @param introEditRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public UserIntroEditResponse editIntro(UserIntroEditRequest introEditRequest) {
        UserIntroEditResponse editResponse = new UserIntroEditResponse();
        String intro = introEditRequest.getIntro();
        Long userId = introEditRequest.getUserId();

        Long profileId = userProfileMapper.selectIdByPkUserId(userId);
        Date now = new Date();
        if (profileId == null) {
            UserProfile profile = new UserProfile();
            profile.setPkUserId(userId);
            profile.setIntro(intro);
            profile.setGithub(null);
            profile.setGmtCreate(now);
            profile.setGmtModified(now);
            userProfileMapper.insert(profile);
        } else {
            UserProfile profileSelective = new UserProfile();
            profileSelective.setId(profileId);
            profileSelective.setIntro(intro);
            profileSelective.setGmtModified(now);
            userProfileMapper.updateByPrimaryKeySelective(profileSelective);
        }
        editResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editResponse;
    }

    /**
     * 编辑 github 链接
     *
     * @param editGithubUrlRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public EditGithubUrlResponse editGithubUrl(EditGithubUrlRequest editGithubUrlRequest) {
        EditGithubUrlResponse editResponse = new EditGithubUrlResponse();
        String github = editGithubUrlRequest.getGithub();
        Long userId = editGithubUrlRequest.getUserId();

        Long profileId = userProfileMapper.selectIdByPkUserId(userId);
        Date now = new Date();
        if (profileId == null) {
            UserProfile profile = new UserProfile();
            profile.setPkUserId(userId);
            profile.setIntro(null);
            profile.setGithub(github);
            profile.setGmtCreate(now);
            profile.setGmtModified(now);
            userProfileMapper.insert(profile);
        } else {
            UserProfile profileSelective = new UserProfile();
            profileSelective.setId(profileId);
            profileSelective.setGithub(github);
            profileSelective.setGmtModified(now);
            userProfileMapper.updateByPrimaryKeySelective(profileSelective);
        }
        editResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editResponse;
    }

    /**
     * 用户资料
     *
     * @param profileRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public UserProfileResponse profile(UserProfileRequest profileRequest) {
        UserProfileResponse profileResponse = new UserProfileResponse();
        Long pickerUserId = profileRequest.getUserId();

        Example example = new Example(UserProfile.class);
        example.createCriteria().andEqualTo("pkUserId", pickerUserId);

        UserProfile profile = userProfileMapper.selectOneByExample(example);
        if (profile != null) {
            profileResponse.setIntro(profile.getIntro());
            profileResponse.setGithub(profile.getGithub());
        }

        profileResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        profileResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return profileResponse;
    }
}
