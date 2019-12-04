package com.grasswort.picker.user.service.elastic;

import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.elastic.entity.UserDoc;
import com.grasswort.picker.user.elastic.repository.UserDocRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname UserDocInitService
 * @Description UserDocInitService
 * @Date 2019/12/4 12:00
 * @blame Java Team
 */
@Service
public class UserDocInitService {
    @Resource
    private UserDocRepository userDocRepository;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserDocConverter userDocConverter;

    /**
     * 初始化 es 存储
     */
    public void init() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("activated", 1);
        List<User> users = userMapper.selectByExample(example);
        users.forEach(user -> userDocRepository.save(userDocConverter.user2Doc(user)));
    }

    //@PostConstruct
    /*public void search() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("nickName", "鞋"));
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDoc> users = userDocRepository.search(boolQueryBuilder, pageable);
        System.out.println("\n匹配结果：" + users.getTotalElements());
        users.forEach(user -> System.out.println(user.getNickName()));
    }*/
}
