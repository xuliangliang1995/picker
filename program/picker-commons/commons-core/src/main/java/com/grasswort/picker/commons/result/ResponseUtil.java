package com.grasswort.picker.commons.result;

/**
 * @author xuliangliang
 * @Classname ResponseUtil
 * @Description TODO
 * @Date 2019/9/21 16:31
 * @blame Java Team
 */
public class ResponseUtil<T> {
    private ResponseData<T> responseData;
    private static final String SUCCESS = "success";
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;

    public ResponseUtil() {
        responseData = new ResponseData<>();
        responseData.setSuccess(true);
        responseData.setMessage(SUCCESS);
        responseData.setCode(SUCCESS_CODE);
    }

    public ResponseData<T> setData(T t) {
        this.responseData.setResult(t);
        this.responseData.setSuccess(true);
        responseData.setCode(SUCCESS_CODE);
        return this.responseData;
    }

    public ResponseData<T> setData(T t, String msg) {
        this.responseData.setResult(t);
        this.responseData.setSuccess(true);
        this.responseData.setMessage(msg);
        responseData.setCode(SUCCESS_CODE);
        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(String msg) {
        this.responseData.setSuccess(false);
        this.responseData.setMessage(msg);
        responseData.setCode(ERROR_CODE);
        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(Integer code, String msg) {
        this.responseData.setSuccess(false);
        this.responseData.setMessage(msg);
        responseData.setCode(ERROR_CODE);
        return this.responseData;
    }
}
