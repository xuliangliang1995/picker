package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IRetentionCurveService;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.CurveListResponse;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname RetentionCurveController
 * @Description 记忆曲线
 * @Date 2019/11/10 12:23
 * @blame Java Team
 */
@Api(tags = "记忆曲线")
@RestController
@RequestMapping("/retentionCurve")
public class RetentionCurveController {

    @Reference(version = "1.0", timeout = 10000)
    IRetentionCurveService iRetentionCurveService;

    @ApiOperation(value = "获取记忆曲线")
    @GetMapping
    public ResponseData retentionCurve() {
        CurveListResponse curveListResponse = iRetentionCurveService.retentionCurve();

        if (SysRetCodeConstants.SUCCESS.getCode().equals(curveListResponse.getCode())) {
            return new ResponseUtil<>().setData(curveListResponse.getCurves());
        }

        return new ResponseUtil<>().setErrorMsg(curveListResponse.getMsg());
    }
}
