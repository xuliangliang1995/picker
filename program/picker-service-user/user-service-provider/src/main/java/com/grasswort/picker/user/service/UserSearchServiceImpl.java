package com.grasswort.picker.user.service;

import com.grasswort.picker.blog.IUserInteractionDataService;
import com.grasswort.picker.blog.dto.UserInteractionDataRequest;
import com.grasswort.picker.blog.dto.UserInteractionDataResponse;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserSearchService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dao.persistence.UserSubscribeAuthorMapper;
import com.grasswort.picker.user.dto.UserSearchRequest;
import com.grasswort.picker.user.dto.UserSearchResponse;
import com.grasswort.picker.user.dto.user.InteractionData;
import com.grasswort.picker.user.dto.user.UserItem;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
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

    @Reference(version = "1.0", timeout = 10000)
    IUserInteractionDataService iUserInteractionDataService;


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
        List<User> users = userMapper.selectAll();
        List<UserItem> userItems = users.stream().map(user -> {
            InteractionData interactionData = new InteractionData();

            UserInteractionDataResponse interactionDataResponse = iUserInteractionDataService.userInteractionData(new UserInteractionDataRequest(user.getId()));
            if (interactionDataResponse != null && interactionDataResponse.isSuccess()) {
                interactionData.setBlogCount(interactionDataResponse.getBlogCount());
                interactionData.setLikedCount(interactionDataResponse.getLikedCount());
            } else {
                interactionData.setBlogCount(0L);
                interactionData.setLikedCount(0L);
            }

            interactionData.setSubscribeCount(userSubscribeAuthorMapper.subscribeCount(user.getId()));
            interactionData.setFansCount(userSubscribeAuthorMapper.fansCount(user.getId()));

            return UserItem.Builder.anUserItem()
                    .withUserId(PickerIdEncrypt.encrypt(user.getId()))
                    .withNickName(user.getName())
                    .withAvatar(user.getAvatar())
                    .withSex(user.getSex())
                    .withInteractionData(interactionData)
                    .build();
        }).collect(Collectors.toList());

        userSearchResponse.setUsers(userItems);
        userSearchResponse.setTotal(userItems.size());
        userSearchResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        userSearchResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return userSearchResponse;
    }
}
