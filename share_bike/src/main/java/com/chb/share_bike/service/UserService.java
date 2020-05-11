package com.chb.share_bike.service;


import com.chb.share_bike.pojo.User;

public interface UserService {
    String getVerifyCode(String countryCode, String phoneNum);

    User register(User user);

    void update(User user);

    User info(String phoneNum);

    boolean genVerifyCode(User user);
}
