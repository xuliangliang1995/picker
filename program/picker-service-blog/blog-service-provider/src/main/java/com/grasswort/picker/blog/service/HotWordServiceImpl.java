package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IHotWordService;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.HotWordRequest;
import com.grasswort.picker.blog.dto.HotWordResponse;
import com.grasswort.picker.blog.service.hotword.SearchHotWordService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xuliangliang
 * @Classname HotWordServiceImpl
 * @Description 热词排行服务
 * @Date 2019/12/3 14:56
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class HotWordServiceImpl implements IHotWordService {
    @Resource
    SearchHotWordService searchHotWordService;
    /**
     * 搜索热词排行
     *
     * @param hotWordRequest
     * @return
     */
    @Override
    public HotWordResponse hotWords(HotWordRequest hotWordRequest) {
        HotWordResponse hotWordResponse = new HotWordResponse();

        Collection<String> hotwords = searchHotWordService.hotSearchWord(hotWordRequest.getSize());

        hotWordResponse.setHotwords(new LinkedList<>(hotwords));
        hotWordResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        hotWordResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return hotWordResponse;
    }
}
