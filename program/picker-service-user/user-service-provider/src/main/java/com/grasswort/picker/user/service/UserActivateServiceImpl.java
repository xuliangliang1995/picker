package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.commons.email.freemarker.FreeMarkerUtil;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.IUserActivateService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.PickerActivateMetaData;
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
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        timeout = 1000,
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

    @Override
    public UserActivateResponse activate(UserActivateRequest request) {
        return null;
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
        Date now = new Date();
        UserActivationCode activationCode = new UserActivationCode();
        activationCode.setPkUserId(user.getId());
        activationCode.setUsername(user.getUsername());
        activationCode.setActivated(false);
        activationCode.setExpireTime(DateTime.now().plusMinutes(10).toDate());
        activationCode.setActivated(false);
        activationCode.setGmtCreate(now);
        activationCode.setGmtModified(now);
        activationCode.setActivationCode(RandomStringUtils.randomAlphabetic(32));
        userActivationCodeMapper.insert(activationCode);

        final String ACTIVATE_URL = String.format("https://picker.grasswort.com/user/activate?username=&s&code=%s&activateId=%s", activationCode.getUsername(), activationCode.getActivationCode(), activationCode.getId());

        Map<String, Object> map = new HashMap<>();
        map.put(PickerActivateMetaData.Key.TITLE, PickerActivateMetaData.SUBJECT);
        map.put(PickerActivateMetaData.Key.URL, ACTIVATE_URL);

        Mail mail = new Mail();
        mail.setSubject(PickerActivateMetaData.SUBJECT);
        try {
            mail.setContent(FreeMarkerUtil.getMailTextForTemplate(PickerActivateMetaData.TEMPLATE_PATH, PickerActivateMetaData.TEMPLATE_NAME, map));
            mail.setHtml(true);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            log.info("\n邮件模板解析失败");
            mail.setContent("激活链接：".concat(ACTIVATE_URL));
            mail.setHtml(false);
        }
        mail.setToAddress(Collections.singletonList(user.getEmail()));
        mail.setCcAddress(Collections.emptyList());
        ListenableFuture<SendResult<String, Mail>> future = kafkaTemplate.send(PickerActivateMetaData.PICKER_ACTIVATE_TOPIC, mail);
        try {
            log.info(future.get().getProducerRecord().value().getContent());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
