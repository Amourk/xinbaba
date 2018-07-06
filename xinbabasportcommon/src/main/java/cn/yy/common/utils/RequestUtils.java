package cn.yy.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//
public class RequestUtils {
    public static String  getCSESSIONID(HttpServletRequest request, HttpServletResponse response)
    {
        //取出cookie
        Cookie[] cookies = request.getCookies();
        if (null!=cookies&&cookies.length>0) {
            for (Cookie cookie : cookies) {
                //判断cookie中是否含有CSESSIONID
                if ("CSESSIONID".equals(cookie.getName()))
                {
                    return cookie.getValue();
                }
            }
        }
        //没有CSESSIONID 则生成一个 写入cookie 并写回浏览器中
        String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
        Cookie cookie = new Cookie("CSESSIONID",csessionid);
        //设置存活时间
        cookie.setMaxAge(-1);
        //设置路径
        cookie.setPath("/");
        //设置跨域
        //写回浏览器
        response.addCookie(cookie);
        return csessionid;
    }
}
