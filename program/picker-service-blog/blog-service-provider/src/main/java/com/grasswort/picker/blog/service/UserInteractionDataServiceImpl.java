package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IUserInteractionDataService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.persistence.BlogLikeMapper;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.blog.dto.UserInteractionDataRequest;
import com.grasswort.picker.blog.dto.UserInteractionDataResponse;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuliangliang
 * @Classname UserInteractionDataServiceImpl
 * @Description 用户交互数据查询服务
 * @Date 2019/12/3 23:38
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserInteractionDataServiceImpl implements IUserInteractionDataService {

    @Autowired BlogLikeMapper blogLikeMapper;

    @Autowired BlogDao blogDao;
    /**
     * 用户交互数据查询
     *
     * @param interactionDataRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public UserInteractionDataResponse userInteractionData(UserInteractionDataRequest interactionDataRequest) {
        UserInteractionDataResponse interactionDataResponse = new UserInteractionDataResponse();

        Long userId = interactionDataRequest.getUserId();
        interactionDataResponse.setBlogCount(blogDao.getBlogCount(userId));
        interactionDataResponse.setLikedCount(blogLikeMapper.getUserLikedCount(userId));

        interactionDataResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        interactionDataResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return interactionDataResponse;
    }
}
