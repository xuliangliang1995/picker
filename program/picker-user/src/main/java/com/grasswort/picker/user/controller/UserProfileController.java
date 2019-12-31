package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserProfileService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import com.grasswort.picker.user.vo.EditGithubForm;
import com.grasswort.picker.user.vo.EditIntroForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author xuliangliang
 * @Classname UserProfileController.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Api(tags = "用户资料")
@RestController
@RequestMapping("/{pickerID}/profile")
public class UserProfileController {
    @Reference(version = "1.0", timeout = 10000)
    IUserProfileService iUserProfileService;

    private static final Pattern GITHUB_USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9-]+");

    @Anoymous
    @ApiOperation(value = "查看")
    @GetMapping
    public ResponseData userProfile(@PathVariable("pickerID") String pickerID) {
        Long pickerId = PickerIdEncrypt.decrypt(pickerID);
        if (pickerId == null) {
            return new ResponseUtil<>().setErrorMsg("用户不存在");
        }
        UserProfileResponse profileResponse = iUserProfileService.profile(new UserProfileRequest(pickerId));
        return Optional.ofNullable(profileResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(profileResponse)
                    : new ResponseUtil<>().setErrorMsg(profileResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改个人简介")
    @PatchMapping("/intro")
    public ResponseData setIntro(@RequestBody @Validated EditIntroForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        UserIntroEditRequest editRequest = UserIntroEditRequest.Builder.anUserIntroEditRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withIntro(form.getIntro())
                .build();
        UserIntroEditResponse editResponse = iUserProfileService.editIntro(editRequest);
        return Optional.ofNullable(editResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(null)
                    : new ResponseUtil<>().setErrorMsg(editResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改 GitHub 链接，只需传入用户名即可")
    @PatchMapping("/github")
    public ResponseData setGithubUrl(@RequestBody @Validated EditGithubForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        String github = form.getGithub();

        boolean githubUsernameIsNotValid = ! GITHUB_USERNAME_PATTERN.matcher(github).matches();
        if (githubUsernameIsNotValid) {
            return new ResponseUtil<>().setErrorMsg("github 用户名有误");
        }
        EditGithubUrlRequest editGithubUrlRequest = EditGithubUrlRequest.Builder.anEditGithubUrlRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withGithub(github)
                .build();
        EditGithubUrlResponse urlResponse = iUserProfileService.editGithubUrl(editGithubUrlRequest);
        return Optional.ofNullable(urlResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(null)
                    : new ResponseUtil<>().setErrorMsg(urlResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }


}
