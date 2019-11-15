package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname WxMpTemplateMsgRequest
 * @Description 微信模板消息请求
 * @Date 2019/11/15 15:02
 * @blame Java Team
 */
@Data
public class WxMpTemplateMsgRequest extends AbstractRequest {
    @NotEmpty
    private String templateId;
    @NotEmpty
    private String json;
    @NotNull
    @Size(min = 28, max = 28)
    private String toOpenId;

    private String url;

    private String miniProgramAppid;

    private String miniProgramPagePath;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String templateId;
        private String json;
        private String toOpenId;
        private String url;
        private String miniProgramAppid;
        private String miniProgramPagePath;

        private Builder() {
        }

        public static Builder aWxMpTemplateMsgRequest() {
            return new Builder();
        }

        public Builder withTemplateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Builder withJson(String json) {
            this.json = json;
            return this;
        }

        public Builder withToOpenId(String toOpenId) {
            this.toOpenId = toOpenId;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withMiniProgramAppid(String miniProgramAppid) {
            this.miniProgramAppid = miniProgramAppid;
            return this;
        }

        public Builder withMiniProgramPagePath(String miniProgramPagePath) {
            this.miniProgramPagePath = miniProgramPagePath;
            return this;
        }

        public WxMpTemplateMsgRequest build() {
            WxMpTemplateMsgRequest wxMpTemplateMsgRequest = new WxMpTemplateMsgRequest();
            wxMpTemplateMsgRequest.setTemplateId(templateId);
            wxMpTemplateMsgRequest.setJson(json);
            wxMpTemplateMsgRequest.setToOpenId(toOpenId);
            wxMpTemplateMsgRequest.setUrl(url);
            wxMpTemplateMsgRequest.setMiniProgramAppid(miniProgramAppid);
            wxMpTemplateMsgRequest.setMiniProgramPagePath(miniProgramPagePath);
            return wxMpTemplateMsgRequest;
        }
    }
}
