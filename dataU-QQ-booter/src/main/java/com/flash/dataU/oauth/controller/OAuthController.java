package com.flash.dataU.oauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年08月23日 16时13分
 */
@Controller
public class OAuthController {

    private final static String REDIRECT_URI = "http://localhost:8081/index";
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthController.class);
    //需要授权的客户端id
    private final static String CLIENT_ID = "hyd";
    //认证登录的服务端的授权码
    private final static String CODE = "shou_quan_ma";
    public static final String ACCESS_TOKEN = "access_token";



    /**
     * （A）用户访问客户端，后者将前者导向认证服务器，即跳转到登录页面
     */
    @RequestMapping("leadToLogin")
    public String leadToLogin(String client_id, String redirect_uri) {
        // 去白名单里找是否有client_id
        // 看是否对应正确的redirect_uri
        if (CLIENT_ID.equals(client_id) && REDIRECT_URI.equals(redirect_uri)) {
            return "login";
        }
        throw new RuntimeException("client_id和redirect_uri认证失败！");
    }

    /**
     * （C）假设用户给予授权，认证服务器将用户导向客户端事先指定的"重定向URI"（redirection URI），同时附上一个授权码。
     */
    @RequestMapping("login")
    public void login(String username, String password, HttpServletResponse response) throws IOException {
        // 验证用户名密码是否正确
        response.sendRedirect(REDIRECT_URI + "?code=" + CODE);
    }

    /**
     * （E）认证服务器核对了授权码和重定向URI，确认无误后，向客户端发送访问令牌（access token）和更新令牌（refresh token）。
     */
    @RequestMapping("getTokenByCode")
    @ResponseBody
    public String getTokenByCode(String client_id, String redirect_uri, String code) throws IOException {
        // 判断client_id、REDIRECT_URI、code是否正确
        // 返回token
        if (CLIENT_ID.equals(client_id) && REDIRECT_URI.equals(redirect_uri) && CODE.equals(code)) {
            return "access_token";
        }
        throw new RuntimeException("获取访问令牌时，参数认证失败！");
    }

    /**
     * （F）资源服务器确认令牌无误，同意向客户端开放资源。。
     */
    @RequestMapping("getUserinfoByToken")
    @ResponseBody
    public String getUserinfoByToken(String token) throws IOException {
        LOGGER.info("资源服务器获取到客户端传来的token:{}", token);
        // 判断token是否正确
        if (ACCESS_TOKEN.equals(token)) {
            return "Tom 17岁 喜欢搞基";
        }
        throw new RuntimeException("身份认证令牌无效！");
    }

}
