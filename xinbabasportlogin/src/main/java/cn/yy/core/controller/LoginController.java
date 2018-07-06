package cn.yy.core.controller;

import cn.yy.common.utils.RequestUtils;
import cn.yy.core.bean.user.Buyer;
import cn.yy.core.service.user.BuyerService;
import cn.yy.core.service.user.SessionProvider;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class LoginController {
    //去登录界面
    @RequestMapping(value = "/login.aspx",method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }
    //判断用户是否界面
    @RequestMapping(value = "/isLogin.aspx")
    public @ResponseBody
    MappingJacksonValue isLogin(String callback,HttpServletRequest request,HttpServletResponse response)
    {
        Integer result = 0;
        String name = sessionProvider.getAttribuerForUsername(RequestUtils.getCSESSIONID(request, response));
        if (null!=name)
        {
            result=1;
        }
        MappingJacksonValue mjv = new MappingJacksonValue(result);
        mjv.setJsonpFunction(callback);

        return mjv;
    }
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private SessionProvider sessionProvider;
    @RequestMapping(value = "/login.aspx",method = RequestMethod.POST)
    //提交登录
    public String login(String username, String password, String returnUrl, HttpServletRequest request,
                        HttpServletResponse response, Model model)
    {
        //用户名不能为空
        if (null!=username)
        {
            //密码不能为空
            if (null!=password)
            {
                Buyer buyer = buyerService.selectBuyerByUsername(username);
                //用户名必须正确
                if (null!=buyer)
                {

                    //密码必须正确
                    if (encodePassword(password).equals(buyer.getPassword()))
                    {
                        //保存用户名到session中
                        sessionProvider.setAttribuerForUsername(RequestUtils.getCSESSIONID(request,response),
                                buyer.getUsername());
                        //跳转到之前页面
                        return "redirect:"+returnUrl;
                    }else
                    {
                        model.addAttribute("error","密码必须正确！");
                    }
                }
                else
                {
                    model.addAttribute("error","用户名必须正确！");
                }

            }else
            {
                model.addAttribute("error","密码不能空！");
            }

        }else
        {
            model.addAttribute("error","用户名不能空！");
        }
        return "login";
    }
    //加密(MD5+十六进制)
    public String encodePassword(String password)
    {
        //MD5
        String algorithm = "MD5";
        char[] encodeHex=null;
        try {
            //MD5加密
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            //加密后
            byte[] digest = instance.digest(password.getBytes());
            //十六进制
            encodeHex = Hex.encodeHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(encodeHex);
    }
}
