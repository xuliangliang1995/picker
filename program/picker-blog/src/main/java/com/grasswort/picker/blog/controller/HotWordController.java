package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IHotWordService;
import com.grasswort.picker.blog.dto.HotWordRequest;
import com.grasswort.picker.blog.dto.HotWordResponse;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname HotWordController
 * @Description 热词
 * @Date 2019/12/3 14:59
 * @blame Java Team
 */
@Api(tags = "热词")
@RestController
@RequestMapping("/hotWord")
public class HotWordController {

    @Reference(version = "1.0", timeout = 10000)
    IHotWordService iHotWordService;

    @ApiOperation(value = "热词")
    @GetMapping
    public ResponseData hotWords() {
        HotWordResponse hotWordResponse = iHotWordService.hotWords(new HotWordRequest(10));
        return Optional.ofNullable(hotWordResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(r.getHotwords())
                        : new ResponseUtil<>().setErrorMsg(r.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}


