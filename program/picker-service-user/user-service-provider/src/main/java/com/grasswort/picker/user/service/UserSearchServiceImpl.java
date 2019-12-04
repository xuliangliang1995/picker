package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserSearchService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dao.persistence.UserSubscribeAuthorMapper;
import com.grasswort.picker.user.dto.UserSearchRequest;
import com.grasswort.picker.user.dto.UserSearchResponse;
import com.grasswort.picker.user.dto.user.UserItem;
import com.grasswort.picker.user.elastic.entity.UserDoc;
import com.grasswort.picker.user.service.elastic.UserDocConverter;
import com.grasswort.picker.user.service.elastic.UserSearchService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname UserSearchServiceImpl
 * @Description 用户查询
 * @Date 2019/12/3 23:04
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserSearchServiceImpl implements IUserSearchService {

    @Autowired UserMapper userMapper;

    @Autowired UserSubscribeAuthorMapper userSubscribeAuthorMapper;

    @Autowired UserSearchService userSearchService;

    @Autowired UserDocConverter userDocConverter;

    /**
     * 用户查询
     *
     * @param userSearchRequest
     * @return
     */
    @DB(DBGroup.SLAVE)
    @Override
    public UserSearchResponse search(UserSearchRequest userSearchRequest) {
        UserSearchResponse userSearchResponse = new UserSearchResponse();

        String keyword = userSearchRequest.getKeyword();
        Integer pageNo = userSearchRequest.getPageNo();
        Integer pageSize = userSearchRequest.getPageSize();
        Long pickerUserId = userSearchRequest.getPkUserId();

        Page<UserDoc> users =  userSearchService.search(keyword, pageNo, pageSize);
        List<UserItem> userItems = users.stream().map(userDoc -> {
            UserItem userItem = userDocConverter.userDoc2Item(userDoc);
            if (pickerUserId != null && pickerUserId > 0L) {
                userItem.setSubscribe(userSubscribeAuthorMapper.isSubscribe(pickerUserId, userDoc.getUserId()));
            } else {
                userItem.setSubscribe(false);
            }
            return userItem;
        }).collect(Collectors.toList());

        userSearchResponse.setUsers(userItems);
        userSearchResponse.setTotal(users.getTotalElements());
        userSearchResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        userSearchResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return userSearchResponse;
    }
}
