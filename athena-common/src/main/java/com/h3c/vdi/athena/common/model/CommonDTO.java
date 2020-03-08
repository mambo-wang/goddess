package com.h3c.vdi.athena.common.model;

/**
 * 公用的返回结果封装，因为Web页面直接接收基本类型不方便，用这个类封装一下。
 * 注意：返回页面结果时，最好所有的参数都带。从页面接收数据，一般只带有data。
 */
public class CommonDTO<T> {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public enum Result {
        /** 成功 */
        SUCCESS,
        /** 失败 */
        FAILURE,
        /** 部分成功 */
        PARTIAL_SUCCESS
    }

    /** 最终结果 */
    private Result result = Result.SUCCESS;

    /** 提示消息 */
    private String message;

    /** 附带数据 */
    private T data;

    private int status = 200;

    public CommonDTO() {
    }

    public CommonDTO(String message) {
        this.message = message;
    }

    public CommonDTO(Result result, String message) {
        this.result = result;
        this.message = message;
        if(result.equals(Result.FAILURE)){
            this.status = 500;
        }
    }

    public CommonDTO(Result result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
        if(result.equals(Result.FAILURE)){
            this.status = 500;
        }
    }

    public CommonDTO(Result result, String message, int status) {
        this.result = result;
        this.message = message;
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
