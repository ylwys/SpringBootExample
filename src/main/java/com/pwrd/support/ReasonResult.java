package com.pwrd.support;

import com.google.common.base.MoreObjects;

public class ReasonResult {
    public boolean success;
    public int code;
    public String reason = "";

    public ReasonResult() {
    }

    public ReasonResult(boolean success) {
        this.success = success;
        if (this.success) code = 0;
    }

    public ReasonResult(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
        if (this.success) code = 0;
    }

    public ReasonResult(boolean success, int code, String reason) {
        this.success = success;
        this.reason = reason;
        if (this.success) this.code = 0;
        else this.code = code;
    }

    public Param params = new Param();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", success)
                .add("code", code)
                .add("reason", reason)
                .add("params", params)
                .toString();
    }
}
