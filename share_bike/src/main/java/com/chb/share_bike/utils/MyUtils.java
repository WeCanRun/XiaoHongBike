package com.chb.share_bike.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class MyUtils {
    // 阿里云密钥
    private final static String accessKeyId = "LTAI4GGTVPwGg6kdnTpwozpC";
    private final static String accessSecret = "Q931itTUSQ0sdsKOdapuRNn4jdN3l5";
    private final static String signName = "小红单车";


    // 阿里云短信接口
    public static boolean sendSms(String phoneNum, String code) {
        boolean res = true;
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", "SMS_189618723");
        request.putQueryParameter("TemplateParam", String.format("{\"code\": %s}", code));
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            System.out.println(data);
        } catch (ServerException e) {
            res = false;
            e.printStackTrace();
        } catch (ClientException e) {
            res = false;
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        boolean b = sendSms("13265156840", "6666");
        System.out.println(b);
    }
}
