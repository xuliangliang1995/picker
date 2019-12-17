package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.constant.TopicMenuTypeEnum;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dto.topic.MenuLink;
import com.grasswort.picker.blog.dto.topic.TopicMenuItem;
import com.grasswort.picker.blog.elastic.entity.TopicDoc;
import com.grasswort.picker.blog.service.BlogTopicMenuServiceImpl;
import com.grasswort.picker.blog.service.redisson.TopicMenuCacheable;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname TopicDocConverter
 * @Description TopicDocConverter
 * @Date 2019/12/17 11:45
 * @blame Java Team
 */
@Service
public class TopicDocConverter {

    @Reference(version = "1.0", timeout = 10000) IUserBaseInfoService iUserBaseInfoService;

    @Resource BlogTopicMenuServiceImpl blogTopicMenuServiceImpl;

    @Resource private TopicMenuCacheable topicMenuCacheable;

    /**
     *
     * @param topic
     * @return
     */
    public TopicDoc topic2Doc(Topic topic) {
        List<TopicMenuItem> topicMenus = blogTopicMenuServiceImpl.topicMenus(topic.getId());

        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(
                UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                .withUserId(topic.getPkUserId())
                .build()
        );
        String authorName = Optional.ofNullable(baseInfoResponse).filter(UserBaseInfoResponse::isSuccess)
                .map(UserBaseInfoResponse::getName).orElse("--");
        String authorAvatar = Optional.ofNullable(baseInfoResponse).filter(UserBaseInfoResponse::isSuccess)
                .map(UserBaseInfoResponse::getAvatar).orElse(null);
        // 重新缓存
        topicMenuCacheable.cacheTopicMenus(topic.getId(), topicMenus);

        return TopicDoc.builder()
                .topicId(topic.getId())
                .pickerId(PickerIdEncrypt.encrypt(topic.getPkUserId()))
                .title(topic.getTitle())
                .summary(topic.getSummary())
                .coverImg(topic.getCoverImg())
                .gmtCreate(topic.getGmtCreate())
                .gmtModified(topic.getGmtModified())
                .links(this.findLinkFromTopicMenu(topicMenus))
                .status(topic.getStatus())
                .ownerAvatar(authorAvatar)
                .ownerName(authorName)
                .build();
    }



    /**
     * 从菜单中寻找所有 link 并排序
     * @param menus
     * @return
     */
    private List<MenuLink> findLinkFromTopicMenu(List<TopicMenuItem> menus) {
        List<MenuLink> links = new ArrayList<>();
        for (TopicMenuItem menu: menus) {
            boolean isLink = Objects.equals(menu.getMenuType(), TopicMenuTypeEnum.LINK.getType());
            if (isLink) {
                links.add(new MenuLink(menu.getMenuName(), menu.getBlogId()));
            } else if (! CollectionUtils.isEmpty(menu.getChildren())) {
                links.addAll(findLinkFromTopicMenu(menu.getChildren()));
            }
        }
        return links;
    }

}
