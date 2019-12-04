package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserSearchService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.dto.UserSearchRequest;
import com.grasswort.picker.user.dto.UserSearchResponse;
import com.grasswort.picker.user.model.PickerInfo;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.SearchUserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname UserSearchController
 * @Description 用户查询
 * @Date 2019/12/3 23:23
 * @blame Java Team
 */
@Api(tags = "用户查询")
@Anoymous(resolve = true)
@RestController
@RequestMapping("/search")
public class UserSearchController {
    @Reference(version = "1.0", timeout = 10000)
    IUserSearchService iUserSearchService;

    @ApiOperation(value = "搜索")
    @GetMapping
    public ResponseData searchUser(@Validated SearchUserForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        UserSearchRequest searchRequest = UserSearchRequest.Builder.anUserSearchRequest()
                .withKeyword(form.getKeyword())
                .withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .withPkUserId(
                        Optional.ofNullable(PickerInfoHolder.getPickerInfo())
                        .map(PickerInfo::getId).orElse(null)
                )
                .build();
        UserSearchResponse searchResponse = iUserSearchService.search(searchRequest);
        return Optional.ofNullable(searchResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(searchResponse.getUsers()).setTotal(searchResponse.getTotal())
                    : new ResponseUtil<>().setErrorMsg(searchResponse.getMsg())
                ).orElse(ResponseData.SYSTEM_ERROR);
    }
}
