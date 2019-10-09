package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.UserBaseInfoEditRequest;
import com.grasswort.picker.user.dto.UserBaseInfoEditResponse;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoServiceImpl
 * @Description 用户信息服务
 * @Date 2019/10/9 12:44
 * @blame Java Team
 */
@Slf4j
@Service(
        version = "1.0",
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_OVER,
        validation = TOrF.FALSE
)
public class UserBaseInfoServiceImpl implements IUserBaseInfoService {
    @Autowired
    UserMapper userMapper;
    /**
     * 获取用户基本信息
     *
     * @param baseInfoRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public UserBaseInfoResponse userBaseInfo(UserBaseInfoRequest baseInfoRequest) {
        UserBaseInfoResponse baseInfoResponse = new UserBaseInfoResponse();

        User user = userMapper.selectByPrimaryKey(baseInfoRequest.getUserId());
        if (null == user) {
            baseInfoResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            baseInfoResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return baseInfoResponse;
        }

        baseInfoResponse.setName(user.getName());
        baseInfoResponse.setSex(user.getSex());
        baseInfoResponse.setEmail(user.getEmail());
        baseInfoResponse.setPhone(user.getPhone());
        baseInfoResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        baseInfoResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return baseInfoResponse;
    }

    /**
     * 编辑用户基本信息
     *
     * @param editRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public UserBaseInfoEditResponse editUserBaseInfo(UserBaseInfoEditRequest editRequest) {
        UserBaseInfoEditResponse editResponse = new UserBaseInfoEditResponse();

        boolean userExists = userMapper.existsWithPrimaryKey(editRequest.getUserId());
        if (! userExists) {
            editResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            editResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return editResponse;
        }

        User userSelective = new User();
        userSelective.setId(editRequest.getUserId());
        userSelective.setName(editRequest.getName());
        userSelective.setSex(editRequest.getSex());
        userSelective.setEmail(editRequest.getEmail());
        userSelective.setPhone(editRequest.getPhone());
        userSelective.setGmtModified(DateTime.now().toDate());
        userMapper.updateByPrimaryKeySelective(userSelective);

        User user = userMapper.selectByPrimaryKey(editRequest.getUserId());
        editResponse.setName(user.getName());
        editResponse.setSex(user.getSex());
        editResponse.setPhone(user.getPhone());
        editResponse.setEmail(user.getEmail());
        editResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editResponse;
    }
}
