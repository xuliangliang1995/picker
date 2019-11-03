package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserPrivilegeService;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.Captcha;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.CaptchaMapper;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.UserPrivilegeRequest;
import com.grasswort.picker.user.dto.UserPrivilegeResponse;
import com.grasswort.picker.user.service.token.UserTokenGenerator;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author xuliangliang
 * @Classname UserPrivilegeServiceImpl
 * @Description 提权
 * @Date 2019/10/12 11:55
 * @blame Java Team
 */
@Service(
        version = "1.0",
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_OVER,
        timeout = 1000
)
public class UserPrivilegeServiceImpl implements IUserPrivilegeService {

    @Autowired CaptchaMapper captchaMapper;

    @Autowired UserMapper userMapper;

    @Autowired UserTokenGenerator userTokenGenerator;
    /**
     * 提高用户操作权限
     *
     * @param privilegeRequest
     * @return
     */
    @Override
    public UserPrivilegeResponse upgradePrivilege(UserPrivilegeRequest privilegeRequest) {
        UserPrivilegeResponse privilegeResponse = new UserPrivilegeResponse();
        try {
            String captcha = privilegeRequest.getCaptch();
            Example example = new Example(Captcha.class);
            example.createCriteria().andEqualTo("captcha", captcha).andGreaterThan("expireTime", DateTime.now().toDate());
            List<Captcha> captchs = captchaMapper.selectByExample(example);
            if (! CollectionUtils.isEmpty(captchs)) {
                Long userId = privilegeRequest.getUserId();
                User user = userMapper.selectByPrimaryKey(userId);
                // 不认为此处 user 会为 null
                boolean privilegeSuccess = captchs.stream()
                        .filter(c -> Objects.equals(c.getPhone(), user.getPhone()) || Objects.equals(c.getEmail(), user.getEmail()))
                        .findFirst().isPresent();
                if (privilegeSuccess) {
                    String privilegeAccessToken = userTokenGenerator.generatePrivilegeAccessToken(user, privilegeRequest.getIp());
                    privilegeResponse.setAccessToken(privilegeAccessToken);
                    privilegeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
                    privilegeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
                    return privilegeResponse;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        privilegeResponse.setCode(SysRetCodeConstants.SECURITY_CODE_ERROR.getCode());
        privilegeResponse.setMsg(SysRetCodeConstants.SECURITY_CODE_ERROR.getMsg());
        return privilegeResponse;
    }
}
