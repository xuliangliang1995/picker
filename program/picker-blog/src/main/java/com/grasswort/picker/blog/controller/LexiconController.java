package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.LexiconService;
import com.grasswort.picker.blog.dto.LexiconResponse;
import com.grasswort.picker.user.annotation.Anoymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname LexiconController
 * @Description 词库
 * @Date 2019/12/3 13:41
 * @blame Java Team
 */
@Api(tags = "词库")
@Anoymous
@RestController
@RequestMapping("/lexicon")
public class LexiconController {

    @Reference(version = "1.0", timeout = 10000)
    LexiconService lexiconService;

    /**
     * 接口返回规范： https://github.com/medcl/elasticsearch-analysis-ik
     * @return
     */
    @ApiOperation(value = "词库获取")
    @GetMapping
    public ResponseEntity<String> lexicon() {
        LexiconResponse lexiconResponse = lexiconService.lexicon();

        if (Optional.ofNullable(lexiconResponse).map(LexiconResponse::isSuccess).orElse(false)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/html;charset=utf-8"));
            headers.set("Last-Modified", String.valueOf(lexiconResponse.getLastModified()));
            headers.set("ETag", lexiconResponse.getETag());
            String content = lexiconResponse.getLexicon().stream().reduce((a, b) -> a + "\n" + b).orElse("");
            return new ResponseEntity<>(
                    content,
                    headers,
                    HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
