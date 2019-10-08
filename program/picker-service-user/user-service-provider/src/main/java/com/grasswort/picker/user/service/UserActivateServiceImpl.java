package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.commons.email.freemarker.FreeMarkerUtil;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.IUserActivateService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.PickerActivateMetaData;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.entity.UserActivationCode;
import com.grasswort.picker.user.dao.persistence.UserActivationCodeMapper;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.UserActivateRequest;
import com.grasswort.picker.user.dto.UserActivateResponse;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname UserActivateServiceImpl
 * @Description 用户激活服务
 * @Date 2019/10/6 22:40
 * @blame Java Team
 */
@Slf4j
@Service(
        version = "1.0",
        timeout = 2000,
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_SAFE,
        validation = TOrF.FALSE
)
public class UserActivateServiceImpl implements IUserActivateService {
    @Autowired
    UserActivationCodeMapper userActivationCodeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    KafkaTemplate<String, Mail> kafkaTemplate;
    /**
     * 账号激活链接模板
     */
    final String ACTIVATED_URL_TEMPLATE = "https://picker.grasswort.com/user/activate?username=%s&code=%s&activateId=%s";
    /**
     * 账号激活链接有效分钟数
     */
    final int ACTIVATED_URL_EFFECTIVE_MINUTES = 10;
    /**
     * 激活码长度（固定 32）
     */
    final int ACTIVATED_CODE_LENGTH = 32;

    @Override
    @DB(DBGroup.SLAVE)
    public UserActivateResponse activate(UserActivateRequest request) {
        UserActivateResponse response = new UserActivateResponse();
        // 判断是否有效
        Long activateId = request.getActivateId();
        UserActivationCode activationCode = userActivationCodeMapper.selectByPrimaryKey(activateId);
        boolean efficacious = activationCode != null && activationCode.getUsername().equals(request.getUsername())
                && activationCode.getActivationCode().equals(request.getActivationCode()) && activationCode.getExpireTime().after(DateTime.now().toDate());

        boolean loseEfficacy = ! efficacious;
        if (loseEfficacy) {
            // 链接已失效
            response.setCode(SysRetCodeConstants.ACTIVATE_URL_LOSE_EFFICACY.getCode());
            response.setMsg(SysRetCodeConstants.ACTIVATE_URL_LOSE_EFFICACY.getMsg());
            return response;
        }

        boolean isNotActivated = ! activationCode.isActivated();
        if (isNotActivated) {
            // 账号尚未激活
            DBLocalHolder.selectDBGroup(DBGroup.MASTER);
            this.executeActivate(activationCode);
        }
        // 激活成功
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;

    }

    /**
     * 执行真正的激活
     * @param activationCode
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeActivate(UserActivationCode activationCode) {
        activationCode.setActivated(true);
        activationCode.setGmtModified(DateTime.now().toDate());
        userActivationCodeMapper.updateByPrimaryKey(activationCode);

        User user = new User();
        user.setId(activationCode.getPkUserId());
        user.setActivated(true);
        user.setGmtModified(DateTime.now().toDate());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 发送激活邮件（允许极小几率发送失败）
     * @param userId
     */
    @DB(DBGroup.MASTER)
    public void sendActivateEmail(long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null || user.isActivated()) {
            return ;
        }
        // 生成激活链接
        Date now = DateTime.now().toDate();
        UserActivationCode activationCode = new UserActivationCode();
        activationCode.setPkUserId(user.getId());
        activationCode.setUsername(user.getUsername());
        activationCode.setActivated(false);
        activationCode.setExpireTime(DateTime.now().plusMinutes(ACTIVATED_URL_EFFECTIVE_MINUTES).toDate());
        activationCode.setGmtCreate(now);
        activationCode.setGmtModified(now);
        activationCode.setActivationCode(RandomStringUtils.randomAlphabetic(ACTIVATED_CODE_LENGTH));
        userActivationCodeMapper.insertUseGeneratedKeys(activationCode);
        final String ACTIVATE_URL = String.format(ACTIVATED_URL_TEMPLATE, activationCode.getUsername(), activationCode.getActivationCode(), activationCode.getId());

        // 封装邮件内容
        Mail mail = new Mail();
        mail.setSubject(PickerActivateMetaData.SUBJECT);
        mail.setToAddress(Collections.singletonList(user.getEmail()));
        mail.setCcAddress(Collections.emptyList());
        try {
            Map<String, Object> map = new HashMap<>();
            map.put(PickerActivateMetaData.Key.TITLE, PickerActivateMetaData.SUBJECT);
            map.put(PickerActivateMetaData.Key.URL, ACTIVATE_URL);
            mail.setContent(FreeMarkerUtil.getMailTextForTemplate(PickerActivateMetaData.TEMPLATE_PATH, PickerActivateMetaData.TEMPLATE_NAME, map));
            mail.setHtml(true);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            log.info("\n邮件模板解析失败");
            mail.setContent("激活链接：".concat(ACTIVATE_URL));
            mail.setHtml(false);
        }

        // 发布到 kafka
        kafkaTemplate.send(PickerActivateMetaData.PICKER_ACTIVATE_TOPIC, mail);
    }
}
