package com.chb.share_bike.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import com.chb.share_bike.constant.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class MyUtils {


    // 阿里云短信接口
    public static boolean sendSms(String phoneNum, String code) {
        boolean res = true;
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.accessKeyId, Constant.accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", Constant.signName);
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


    /**
     * 微信登录认证
     *
     * @param code
     * @return
     */
    public static String authCode2Session(String code) throws URISyntaxException, IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet();
        String url = String.format(Constant.wxUrl, Constant.appId, Constant.secret, code);
        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
        InputStream is = response.getEntity().getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String text = bufferedReader.readLine();
        System.out.println(text);
        return text;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
//        boolean b = sendSms("13265156840", "6666");
        System.out.println(authCode2Session("043aruwV0FVEUY1Wy7vV00SrwV0aruwk"));
    }
}
