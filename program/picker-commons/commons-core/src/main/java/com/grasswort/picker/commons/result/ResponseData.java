package com.grasswort.picker.commons.result;

/**
 * @author xuliangliang
 * @Classname ResponseData
 * @Description TODO
 * @Date 2019/9/21 16:31
 * @blame Java Team
 */
public class ResponseData<T> {
    /**
     * 系统异常
     */
    public static final ResponseData SYSTEM_ERROR = new ResponseUtil<>().setErrorMsg("系统错误");

    private boolean success;

    private String message;

    private int code;

    private T result;

    private Long total;
    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTotal() {
        return total;
    }

    public ResponseData setTotal(Long total) {
        this.total = total;
        return this;
    }
}
