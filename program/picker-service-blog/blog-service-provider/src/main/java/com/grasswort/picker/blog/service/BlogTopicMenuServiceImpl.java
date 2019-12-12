package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogTopicMenuService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.constant.TopicMenuTypeEnum;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dao.entity.TopicMenu;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dao.persistence.TopicMenuMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.dto.topic.TopicMenuItem;
import com.grasswort.picker.blog.dto.topic.TopicMenuItemBuilder;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogTopicMenuServiceImpl
 * @Description 专题菜单服务
 * @Date 2019/12/11 14:09
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogTopicMenuServiceImpl implements IBlogTopicMenuService {

    @Autowired TopicMenuMapper topicMenuMapper;

    @Autowired BlogDao blogDao;

    @Autowired TopicMapper topicMapper;
    /**
     * 菜单创建
     *
     * @param menuCreateRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicMenuCreateResponse createMenu(TopicMenuCreateRequest menuCreateRequest) {
        TopicMenuCreateResponse menuCreateResponse = new TopicMenuCreateResponse();

        Long parentMenuId = menuCreateRequest.getParentMenuId();
        String menuName = menuCreateRequest.getName();
        Long topicId = TopicIdEncrypt.decrypt(menuCreateRequest.getTopicId());
        Long blogId = Optional.ofNullable(menuCreateRequest.getBlogId())
                .map(BlogIdEncrypt::decrypt)
                .map(BlogIdEncrypt.BlogKey::getBlogId)
                .filter(bid -> bid != null)
                .orElse(0L);
        Integer menuType = menuCreateRequest.getType();
        Long pkUserId = menuCreateRequest.getPkUserId();

        // topic
        Topic topic = topicMapper.selectByPrimaryKey(topicId);
        if (topic == null || ! Objects.equals(topic.getPkUserId(), pkUserId)) {
            menuCreateResponse.setCode(SysRetCodeConstants.TOPIC_NOT_EXISTS.getCode());
            menuCreateResponse.setMsg(SysRetCodeConstants.TOPIC_NOT_EXISTS.getMsg());
            return menuCreateResponse;
        }

        // 当前菜单是 link
        boolean currentIsLink =  Objects.equals(TopicMenuTypeEnum.LINK.getId(), menuType);
        if (currentIsLink) {
            boolean blogExists = blogId > 0L && blogDao.existsAndNormal(blogId);
            if (! blogExists) {
                menuCreateResponse.setCode(SysRetCodeConstants.MENU_NOT_VALID.getCode());
                menuCreateResponse.setMsg(SysRetCodeConstants.MENU_NOT_VALID.getMsg());
                return menuCreateResponse;
            }
        }

        if (parentMenuId > 0) {
            TopicMenu tm =  topicMenuMapper.selectByPrimaryKey(parentMenuId);
            // 父级菜单不存在
            if (tm == null || ! Objects.equals(tm.getTopicId(), topicId)) {
                menuCreateResponse.setCode(SysRetCodeConstants.PARENT_MENU_NOT_EXISTS.getCode());
                menuCreateResponse.setMsg(SysRetCodeConstants.PARENT_MENU_NOT_EXISTS.getMsg());
                return menuCreateResponse;
            }
            // link 下不允许创建菜单
            boolean parentIsLink = Objects.equals(TopicMenuTypeEnum.LINK.getId(), tm.getMenuType());
            if (parentIsLink) {
                menuCreateResponse.setCode(SysRetCodeConstants.MENU_NOT_VALID.getCode());
                menuCreateResponse.setMsg(SysRetCodeConstants.MENU_NOT_VALID.getMsg());
                return menuCreateResponse;
            }
            // group 下只能创建 link
            boolean parentIsGroup = Objects.equals(TopicMenuTypeEnum.GROUP.getId(), tm.getMenuType());
            if (parentIsGroup && ! currentIsLink) {
                menuCreateResponse.setCode(SysRetCodeConstants.MENU_NOT_VALID.getCode());
                menuCreateResponse.setMsg(SysRetCodeConstants.MENU_NOT_VALID.getMsg());
                return menuCreateResponse;
            }
            // 父级还有父级
            if (tm.getParentMenuId() > 0L) {
                // 排除 group 菜单。最多允许二级菜单
                if (parentIsGroup) {
                    tm =  topicMenuMapper.selectByPrimaryKey(tm.getParentMenuId());
                    if (tm.getParentMenuId() > 0L) {
                        // 菜单层级过高
                        menuCreateResponse.setCode(SysRetCodeConstants.MENU_LEVEL_TOO_HIGH.getCode());
                        menuCreateResponse.setMsg(SysRetCodeConstants.MENU_LEVEL_TOO_HIGH.getMsg());
                        return menuCreateResponse;
                    }
                } else {
                    // 菜单层级过高
                    menuCreateResponse.setCode(SysRetCodeConstants.MENU_LEVEL_TOO_HIGH.getCode());
                    menuCreateResponse.setMsg(SysRetCodeConstants.MENU_LEVEL_TOO_HIGH.getMsg());
                    return menuCreateResponse;
                }
            }
        }

        Integer maxWeight = topicMenuMapper.equativeMaxWeight(topicId, parentMenuId);

        TopicMenu topicMenu = new TopicMenu();
        topicMenu.setTopicId(topicId);
        topicMenu.setMenuName(menuName);
        topicMenu.setMenuType(menuType);
        topicMenu.setParentMenuId(parentMenuId);
        topicMenu.setBlogId(blogId);
        topicMenu.setWeight(maxWeight == null ? 1 : (maxWeight + 1));
        Date now = new Date();
        topicMenu.setGmtCreate(now);
        topicMenu.setGmtModified(now);
        topicMenuMapper.insertUseGeneratedKeys(topicMenu);

        menuCreateResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        menuCreateResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return menuCreateResponse;
    }

    /**
     * 专题菜单
     *
     * @param topicMenuRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public TopicMenuResponse topicMenu(TopicMenuRequest topicMenuRequest) {
        TopicMenuResponse topicMenuResponse = new TopicMenuResponse();
        Long topicId = TopicIdEncrypt.decrypt(topicMenuRequest.getTopicId());
        Long pkUserId = topicMenuRequest.getPkUserId();
        Topic topic = (topicId != null && topicId > 0L) ? topicMapper.selectByPrimaryKey(topicId) : null;

        boolean topicExists =  topic != null ;
        if (! topicExists) {
            topicMenuResponse.setCode(SysRetCodeConstants.TOPIC_NOT_EXISTS.getCode());
            topicMenuResponse.setMsg(SysRetCodeConstants.TOPIC_NOT_EXISTS.getMsg());
            return topicMenuResponse;
        }

        Example example = new Example(TopicMenu.class);
        example.createCriteria().andEqualTo("topicId", topicId).andEqualTo("parentMenuId", 0L);
        example.setOrderByClause("weight asc");

        List<TopicMenuItem> topicMenus = topicMenuMapper.selectByExample(example)
                .stream().map(tm -> topicMenu2Item(tm)).collect(Collectors.toList());

        topicMenuResponse.setMenu(topicMenus);
        topicMenuResponse.setEditable(Objects.equals(pkUserId, topic.getPkUserId()));
        topicMenuResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        topicMenuResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return topicMenuResponse;
    }

    /**
     * 删除专题菜单
     *
     * @param deleteRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public DeleteTopicMenuResponse deleteTopicMenu(DeleteTopicMenuRequest deleteRequest) {
        DeleteTopicMenuResponse deleteTopicMenuResponse = new DeleteTopicMenuResponse();

        Long menuId = deleteRequest.getMenuId();
        Long pkUserId = deleteRequest.getPkUserId();
        TopicMenu topicMenu = topicMenuMapper.selectByPrimaryKey(menuId);
        if (topicMenu != null) {
            Topic topic = topicMapper.selectByPrimaryKey(topicMenu.getTopicId());
            if (Objects.equals(topic.getPkUserId(), pkUserId)) {
                boolean hasChildren = topicMenuMapper.hasChildren(topicMenu.getId());
                if (hasChildren) {
                    deleteTopicMenuResponse.setCode(SysRetCodeConstants.MENU_CAN_NOT_DELETE.getCode());
                    deleteTopicMenuResponse.setMsg(SysRetCodeConstants.MENU_CAN_NOT_DELETE.getMsg());
                    return deleteTopicMenuResponse;
                }
                topicMenuMapper.deleteByPrimaryKey(menuId);
            }
        }
        deleteTopicMenuResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        deleteTopicMenuResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return deleteTopicMenuResponse;
    }

    /**
     * 菜单上移
     *
     * @param moveUpRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicMenuMoveUpResponse moveUp(TopicMenuMoveUpRequest moveUpRequest) {
        TopicMenuMoveUpResponse topicMenuMoveUpResponse = new TopicMenuMoveUpResponse();

        Long pkUserId = moveUpRequest.getPkUserId();
        Long topicId = TopicIdEncrypt.decrypt(moveUpRequest.getTopicId());
        Long menuId = moveUpRequest.getMenuId();

        Topic topic = Optional.ofNullable(topicId).map(topicMapper::selectByPrimaryKey)
                .filter(t -> Objects.equals(t.getPkUserId(), pkUserId))
                .orElse(null);
        if (topic == null) {
            topicMenuMoveUpResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            topicMenuMoveUpResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return topicMenuMoveUpResponse;
        }

        TopicMenu topicMenu = topicMenuMapper.selectByPrimaryKey(menuId);
        if (topicMenu == null) {
            topicMenuMoveUpResponse.setCode(SysRetCodeConstants.MENU_NOT_EXISTS.getCode());
            topicMenuMoveUpResponse.setMsg(SysRetCodeConstants.MENU_NOT_EXISTS.getMsg());
            return topicMenuMoveUpResponse;
        }

        this.move(topicMenu, true);

        topicMenuMoveUpResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        topicMenuMoveUpResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return topicMenuMoveUpResponse;
    }

    /**
     * 菜单下移
     *
     * @param moveDownRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicMenuMoveDownResponse moveDown(TopicMenuMoveDownRequest moveDownRequest) {
        TopicMenuMoveDownResponse topicMenuMoveDownResponse = new TopicMenuMoveDownResponse();

        Long pkUserId = moveDownRequest.getPkUserId();
        Long topicId = TopicIdEncrypt.decrypt(moveDownRequest.getTopicId());
        Long menuId = moveDownRequest.getMenuId();

        Topic topic = Optional.ofNullable(topicId).map(topicMapper::selectByPrimaryKey)
                .filter(t -> Objects.equals(t.getPkUserId(), pkUserId))
                .orElse(null);
        if (topic == null) {
            topicMenuMoveDownResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            topicMenuMoveDownResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return topicMenuMoveDownResponse;
        }

        TopicMenu topicMenu = topicMenuMapper.selectByPrimaryKey(menuId);
        if (topicMenu == null) {
            topicMenuMoveDownResponse.setCode(SysRetCodeConstants.MENU_NOT_EXISTS.getCode());
            topicMenuMoveDownResponse.setMsg(SysRetCodeConstants.MENU_NOT_EXISTS.getMsg());
            return topicMenuMoveDownResponse;
        }

        this.move(topicMenu, false);

        topicMenuMoveDownResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        topicMenuMoveDownResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return topicMenuMoveDownResponse;
    }

    /**
     * 重命名
     *
     * @param renameMenuRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public RenameMenuResponse rename(RenameMenuRequest renameMenuRequest) {
        RenameMenuResponse renameMenuResponse = new RenameMenuResponse();

        Long pkUserId = renameMenuRequest.getPkUserId();
        Long topicId = TopicIdEncrypt.decrypt(renameMenuRequest.getTopicId());
        Long menuId = renameMenuRequest.getMenuId();
        String menuName = renameMenuRequest.getMenuName();

        Topic topic = Optional.ofNullable(topicId).map(topicMapper::selectByPrimaryKey)
                .filter(t -> Objects.equals(t.getPkUserId(), pkUserId))
                .orElse(null);
        if (topic == null) {
            renameMenuResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            renameMenuResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return renameMenuResponse;
        }

        TopicMenu topicMenu = topicMenuMapper.selectByPrimaryKey(menuId);
        if (topicMenu == null) {
            renameMenuResponse.setCode(SysRetCodeConstants.MENU_NOT_EXISTS.getCode());
            renameMenuResponse.setMsg(SysRetCodeConstants.MENU_NOT_EXISTS.getMsg());
            return renameMenuResponse;
        }

        TopicMenu menuSelective = new TopicMenu();
        menuSelective.setId(menuId);
        menuSelective.setMenuName(menuName);
        menuSelective.setGmtModified(new Date());
        topicMenuMapper.updateByPrimaryKeySelective(menuSelective);

        renameMenuResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        renameMenuResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return renameMenuResponse;
    }

    /**
     * topicMenu2Item
     * @param topicMenu
     * @return
     */
    private TopicMenuItem topicMenu2Item(TopicMenu topicMenu) {
        TopicMenuTypeEnum type = Arrays.stream(TopicMenuTypeEnum.values()).filter(t -> Objects.equals(t.getId(), topicMenu.getMenuType()))
                .findFirst().get();

        String blogId = TopicMenuTypeEnum.LINK == type ? BlogIdEncrypt.encrypt(topicMenu.getBlogId()) : null;

        Example example = new Example(TopicMenu.class);
        example.createCriteria().andEqualTo("topicId", topicMenu.getTopicId()).andEqualTo("parentMenuId", topicMenu.getId());
        example.setOrderByClause("weight asc");
        List<TopicMenuItem> childrenMenus = topicMenuMapper.selectByExample(example)
                .stream().map(tm -> topicMenu2Item(tm)).collect(Collectors.toList());

        TopicMenuItem item = TopicMenuItemBuilder.aTopicMenuItem()
                .withMenuId(topicMenu.getId())
                .withMenuName(topicMenu.getMenuName())
                .withMenuType(type.getType())
                .withBlogId(blogId)
                .withParentMenuId(topicMenu.getParentMenuId())
                .withChildren(childrenMenus)
                .build();

        return item;
    }

    /**
     * 移动菜单
     * @param topicMenu
     * @param up
     */
    private void move(TopicMenu topicMenu, boolean up) {
        RowBounds rowBounds = new RowBounds(0, 1);

        Example example = new Example(TopicMenu.class);
        example.createCriteria()
                .andEqualTo("topicId", topicMenu.getTopicId())
                .andEqualTo("parentMenuId", topicMenu.getParentMenuId())
                .andLessThan("weight", topicMenu.getWeight());
        example.setOrderByClause(up ? "weight desc" : "weight asc");
        List<TopicMenu> upMenus = topicMenuMapper.selectByExampleAndRowBounds(example, rowBounds);

        if (! CollectionUtils.isEmpty(upMenus)) {
            TopicMenu upMenu = upMenus.get(0);
            Date now = new Date();
            // 交换二者权重
            TopicMenu currentMenuSelective = new TopicMenu();
            currentMenuSelective.setId(topicMenu.getId());
            currentMenuSelective.setWeight(upMenu.getWeight());
            currentMenuSelective.setGmtModified(now);

            TopicMenu upMenuSelective = new TopicMenu();
            upMenuSelective.setId(upMenu.getId());
            upMenuSelective.setWeight(topicMenu.getWeight());
            upMenuSelective.setGmtModified(now);

            topicMenuMapper.updateByPrimaryKeySelective(currentMenuSelective);
            topicMenuMapper.updateByPrimaryKeySelective(upMenuSelective);
        }
    }
}

