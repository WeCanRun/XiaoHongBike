package com.chb.share_bike.controller;

import com.chb.share_bike.pojo.User;
import com.chb.share_bike.service.UserService;
import com.chb.share_bike.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/get_verify_code")
    public String getVerifyCode(@RequestBody User user) {
        String countryCode = user.getCountryCode();
        String phoneNum = user.getPhoneNum();
        if (countryCode == null || phoneNum == null || "".equals(countryCode) || "".equals(phoneNum))
            return Json.respnose("500", "获取验证码失败");

        String verifyCode = "";
        try {
            verifyCode = userService.getVerifyCode(countryCode, phoneNum);
        } catch (Exception e) {
            System.out.println("getVerifyCode | userService.getVerifyCode fail");
            e.printStackTrace();
        }

        return Json.respnose("200", verifyCode);
    }

    @GetMapping("/user/genVerifyCode")
    public boolean genVerifyCode(User user) {
        return userService.genVerifyCode(user);
    }

    @PostMapping("/user/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/user/deposit")
    public boolean deposit(@RequestBody User user) {
         boolean res = true;
         try {
             userService.update(user);
         } catch (Exception e) {
             res = false;
             e.printStackTrace();
         }
         return res;
    }

    @PostMapping("user/identify")
    public boolean identify(@RequestBody User user) {
        boolean res = true;
        try {
            userService.update(user);
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
        }
        return res;
    }

    @GetMapping("user/info")
    public User userInfo(String phoneNum) {
        User user = userService.info(phoneNum);
        return user;
    }
}
