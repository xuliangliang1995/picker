package com.grasswort.picker.commons.result;

/**
 * @author xuliangliang
 * @Classname ResponseData
 * @Description TODO
 * @Date 2019/9/21 16:31
 * @blame Java Team
 */
public class ResponseData<T> {

    private boolean success;

    private String message;

    private int code;

    private T result;
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
}
