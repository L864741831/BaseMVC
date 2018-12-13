package com.liweidong.basemvc.http;

/**
 * Element为接口返回json对应实体类，参数规则根据接口来定。
 */
public class Element {
    public boolean error;
    public String results;

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
        return "Element [error=" + error + ", results=" + results + "]";
    }

}
