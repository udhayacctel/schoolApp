package com.sstutor.security.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OTPLoginRequest {
    private String mobile;
    private String otp;

    //This annotation is required for Jackson to build the object else it will fail saying it doesn't have a proper constructor
    @JsonCreator
    public OTPLoginRequest(@JsonProperty("mobile") String mobile, @JsonProperty("otp") String otp)
    {
        this.mobile = mobile;
        this.otp = otp;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOtp() {
        return otp;
    }
}
