package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.constant.TopicMenuTypeEnum;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dao.persistence.TopicCommentMapper;
import com.grasswort.picker.blog.dto.topic.MenuLink;
import com.grasswort.picker.blog.dto.topic.TopicItem;
import com.grasswort.picker.blog.dto.topic.TopicMenuItem;
import com.grasswort.picker.blog.elastic.entity.TopicDoc;
import com.grasswort.picker.blog.service.BlogTopicMenuServiceImpl;
import com.grasswort.picker.blog.service.redisson.TopicMenuCacheable;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
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

    @Resource private TopicCommentMapper topicCommentMapper;

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

        Integer rate = 5;
        Long sumRate = topicCommentMapper.sumRateForTopic(topic.getId());
        if (sumRate > 0L) {
            Long commentTotal = topicCommentMapper.topicCommentCount(topic.getId());
            if (commentTotal > 0L) {
                rate = Long.valueOf(Math.round(sumRate / (double)commentTotal)).intValue();
            }
        }

        return TopicDoc.builder()
                .topicId(topic.getId())
                .pickerId(PickerIdEncrypt.encrypt(topic.getPkUserId()))
                .title(topic.getTitle())
                .summary(topic.getSummary())
                .coverImg(topic.getCoverImg())
                .gmtCreate(topic.getGmtCreate())
                .gmtModified(topic.getGmtModified())
                .links(this.findLinkFromTopicMenu(topicMenus))
                .rate(rate)
                .status(topic.getStatus())
                .ownerAvatar(authorAvatar)
                .ownerName(authorName)
                .build();
    }

    /**
     * topicDoc2Item
     * @param topic
     * @return
     */
    public TopicItem topicDoc2Item(TopicDoc topic) {
        return TopicItem.Builder.aTopicItem()
                .withTopicId(TopicIdEncrypt.encrypt(topic.getTopicId()))
                .withPkUserId(topic.getPickerId())
                .withTitle(topic.getTitle())
                .withSummary(topic.getSummary())
                .withCoverImg(topic.getCoverImg())
                .withOwnerName(topic.getOwnerName())
                .withOwnerAvatar(topic.getOwnerAvatar())
                .withStatus(topic.getStatus())
                .withLinks(topic.getLinks())
                .withRate(topic.getRate())
                .withGmtCreate(topic.getGmtCreate())
                .withGmtModified(topic.getGmtModified())
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
