package com.chb.share_bike.service;

import com.chb.share_bike.pojo.User;
import com.chb.share_bike.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String getVerifyCode(String countryCode, String phoneNum) {
        // 生成随机六位验证码
        String verifyCode = String.valueOf((int) ((Math.random() + 1) * 100000));

        // 发送短信
        MyUtils.sendSms(phoneNum, verifyCode);
        // 存入redis
        String key = countryCode + phoneNum;
        stringRedisTemplate.opsForValue().set(key, verifyCode, 300L, TimeUnit.SECONDS);
        return verifyCode;
    }

    @Override
    public User register(User user) {
        User exsit = mongoTemplate.findOne(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())), User.class);
        if (exsit != null)
            return null;

        return mongoTemplate.insert(user);
    }

    @Override
    public void update(User user) {
        Query query = new Query(Criteria.where("phoneNum").is(user.getPhoneNum()));

        Update update = new Update();

        if (user.getDeposit() != null) {
            update.set("deposit", user.getDeposit());
        }

        if (user.getStatus() != null) {
            update.set("status", user.getStatus());
        }

        if (user.getName() != null) {
            update.set("name", user.getName());
        }

        if (user.getIdNum() != null) {
            update.set("idNum", user.getIdNum());
        }

        mongoTemplate.updateFirst(query, update, User.class);
    }

    @Override
    public User info(String phoneNum) {
        User user = mongoTemplate.findOne(new Query(Criteria.where("phoneNum").is(phoneNum)), User.class);
        return user;
    }

    @Override
    public boolean genVerifyCode(User user) {
        String key = user.getCountryCode() + user.getPhoneNum();
        String code = stringRedisTemplate.opsForValue().get(key);

        return code != null && code.equals(user.getVerifyCode());
    }
}
