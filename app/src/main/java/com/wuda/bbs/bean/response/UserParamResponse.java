package com.wuda.bbs.bean.response;

import java.util.List;

public class UserParamResponse extends BaseResponse {
    List<Boolean> paramValues;

    public List<Boolean> getParamValues() {
        return paramValues;
    }

    public void setParamValues(List<Boolean> paramValues) {
        this.paramValues = paramValues;
    }
}
