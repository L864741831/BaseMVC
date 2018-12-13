package com.liweidong.basemvc.http;

/**
 * Element为接口返回json对应实体类，参数规则根据接口来定。
 */
public class Element {
    public boolean error;
    public String results;

    public String msg;
    public int code;
    public String rows;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String toString() {
        return "Element [error=" + error + ", results=" + results + ", msg=" + msg + ", code=" + code + ", rows=" + rows + "]";
    }

}
