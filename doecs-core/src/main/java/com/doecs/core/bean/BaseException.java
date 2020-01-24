package com.doecs.core.bean;

public class BaseException extends RuntimeException {
    private String code;
    private String msg;
    private String subCode;
    private String subMsg;
    private Exception realException;

    public Exception getRealException() {
        return realException;
    }

    public BaseException setRealException(Exception realException) {
        this.realException = realException;
        return this;
    }

    public BaseException(){
        super();
    }
    public BaseException(String code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    public BaseException(String msg){
        super(msg);
        this.code = null;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }
}
