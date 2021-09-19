package com.hitty.hmall.config;

import com.alibaba.fastjson.JSON;
import com.hitty.hmall.util.HttpClientUtil;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tocken = request.getParameter("newTocken");
        if (tocken != null) {
            CookieUtil.setCookie(request,response,"tocken",tocken,WebConst.COOKIE_MAXAGE,false);
        } else {
            tocken = CookieUtil.getCookieValue(request, "tocken", false);
        }
        if (tocken != null) {
            Map map = getUserMapByTocken(tocken);
            String nickName = (String) map.get("nickName");
            request.setAttribute("nickName",nickName);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequire loginRequireAnnotation = handlerMethod.getMethodAnnotation(LoginRequire.class);
        if (loginRequireAnnotation!=null){
            String salt = request.getHeader("x-forwarded-for");
            String result = HttpClientUtil.doGet(WebConst.VERIFY_ADDRESS + "?tocken=" + tocken + "&salt=" + salt);
            if ("success".equals(result)){
                Map map = getUserMapByTocken(tocken);
                String userId = (String) map.get("userId");
                request.setAttribute("userId",userId);
                return true;
            } else {
                if (loginRequireAnnotation.autoRedirect()){
                    String requestUrl = request.getRequestURI().toString();
                    String encodeUrl = URLEncoder.encode(requestUrl, "UTF-8");
                    response.sendRedirect(WebConst.LOGIN_ADDRESS+"?originUrl="+encodeUrl);
                    return false;
                }
            }
        }
        return true;
    }

    private Map getUserMapByTocken(String tocken) {
        String tockenUserInfo = StringUtils.substringBetween(tocken,".");
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] decode = base64UrlCodec.decode(tockenUserInfo);
        String strUserInfo = null;
        try {
            strUserInfo = new String(decode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(strUserInfo,Map.class);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
