package com.grasswort.picker.user.service.elastic;

import com.grasswort.picker.blog.IUserInteractionDataService;
import com.grasswort.picker.blog.dto.UserInteractionDataRequest;
import com.grasswort.picker.blog.dto.UserInteractionDataResponse;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserSubscribeAuthorMapper;
import com.grasswort.picker.user.dto.user.InteractionData;
import com.grasswort.picker.user.dto.user.UserItem;
import com.grasswort.picker.user.elastic.entity.UserDoc;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname UserDocConverter
 * @Description
 * @Date 2019/12/4 13:26
 * @blame Java Team
 */
@Service
public class UserDocConverter {
    @Reference(version = "1.0", timeout = 10000)
    IUserInteractionDataService iUserInteractionDataService;
    @Resource
    UserSubscribeAuthorMapper userSubscribeAuthorMapper;

    /**
     * user2Doc
     * @param user
     * @return
     */
    @DB(DBGroup.SLAVE)
    public UserDoc user2Doc(User user) {
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

        return UserDoc.Builder.anUserDoc()
                .withUserId(user.getId())
                .withNickName(user.getName())
                .withAvatar(user.getAvatar())
                .withSex((int)user.getSex())
                .withSignature(user.getSignature())
                .withBlogCount(interactionData.getBlogCount())
                .withFansCount(interactionData.getFansCount())
                .withSubscribeCount(interactionData.getSubscribeCount())
                .withLikedCount(interactionData.getLikedCount())
                .build();
    }

    /**
     * UserDoc2Item
     * @param userDoc
     * @return
     */
    public UserItem userDoc2Item(UserDoc userDoc) {
        InteractionData interactionData = new InteractionData();
        interactionData.setBlogCount(userDoc.getBlogCount());
        interactionData.setFansCount(userDoc.getFansCount());
        interactionData.setSubscribeCount(userDoc.getSubscribeCount());
        interactionData.setLikedCount(userDoc.getLikedCount());

        return UserItem.Builder.anUserItem()
                .withUserId(PickerIdEncrypt.encrypt(userDoc.getUserId()))
                .withNickName(userDoc.getNickName())
                .withAvatar(userDoc.getAvatar())
                .withSex(Byte.valueOf(String.valueOf(userDoc.getSex())))
                .withSignature(userDoc.getSignature())
                .withInteractionData(interactionData)
                .build();

    }
}
