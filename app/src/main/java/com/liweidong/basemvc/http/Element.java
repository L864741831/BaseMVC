package com.liweidong.basemvc.http;

/**
 * Created by Administrator on 2018/12/12.
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
