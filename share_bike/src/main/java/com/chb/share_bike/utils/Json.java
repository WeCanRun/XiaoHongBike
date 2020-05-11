package com.chb.share_bike.utils;

import com.alibaba.fastjson.JSONObject;

public class Json {

    public static String respnose (String code, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("respMsg", msg);
        return jsonObject.toString();
    }

}
