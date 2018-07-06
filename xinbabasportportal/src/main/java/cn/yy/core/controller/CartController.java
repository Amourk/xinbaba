package cn.yy.core.controller;

import cn.yy.common.utils.RequestUtils;
import cn.yy.common.web.Constants;
import cn.yy.core.bean.BuyerCart;
import cn.yy.core.bean.BuyerItem;
import cn.yy.core.bean.product.Sku;
import cn.yy.core.service.product.SkuService;
import cn.yy.core.service.user.SessionProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private SessionProvider sessionProvider;
    //添加购物车
    @RequestMapping("/addCart")
    private String addCart(Long skuId, Integer amount, Model model,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        //不要NULL 不要转了
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //创建购物车对象
        BuyerCart buyerCart =null;
//        1：从Request中取Cookies、遍历Cookie 取出之前的购物车
        Cookie[] cookies = request.getCookies();
        //判断cookie是否为空
        if (null!=cookies&&cookies.length!=0)
        {
            //遍历cookie 找到购物车
            for (Cookie cookie: cookies) {
//        2：判断Cookie中没有购物车
                if (Constants.BUYER_CART.equals(cookie.getName()))
                {
//        3：有
                    String value = cookie.getValue();
                    //将得到的购物车字符串转化为购物车对象
                    buyerCart = om.readValue(value,BuyerCart.class);
                    break;
                }
            }
        }
//        4：没有 创建购物车
        if (null==buyerCart)
        {
            buyerCart = new BuyerCart();
        }
//        5：追加当前商品到购物车
        Sku sku = new Sku();
        sku.setId(skuId);
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku(sku);
        buyerItem.setAmount(amount);
        buyerCart.addItem(buyerItem);
        //判断用户是否登录
        String username = sessionProvider.getAttribuerForUsername(RequestUtils.getCSESSIONID(request,response));
        //用户登录
        if (null!=username)
        {
            if (buyerCart!=null)
            {
//            3：有  把购物车中商品添加到Redis的购物车中
                skuService.insertBuyerCartToRedis(buyerCart,username);
//                清理之前Cookie
                Cookie cookie = new Cookie(Constants.BUYER_CART,null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
//            4：没有
//            5：直接添加当前商品到Redis中的购物车里

        }else//用户未登录
        {
//        6：创建Cookie 把新购物车放进去
            //将购物车对象转化为json字符串
            StringWriter w   = new StringWriter();
            om.writeValue(w, buyerCart);
            Cookie cookie = new Cookie(Constants.BUYER_CART,w.toString());
            //设置cookie时间
            cookie.setMaxAge(60*60*24);
            //设置路径
            cookie.setPath("/");
//        7：保存Cookie写回浏览器
            response.addCookie(cookie);
        }
        return "redirect:/toCart";
    }
    @Autowired
    private SkuService skuService;
    //去购物车界面
    @RequestMapping("/toCart")
    private String toCart(Long skuId, Integer amount, Model model,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        //不要NULL 不要转了
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //创建购物车对象
        BuyerCart buyerCart =null;
//        1：从Request中取Cookies、遍历Cookie 取出之前的购物车
        Cookie[] cookies = request.getCookies();
        //判断cookie是否为空
        if (null!=cookies&&cookies.length!=0)
        {
            //遍历cookie 找到购物车
            for (Cookie cookie: cookies) {
//        2：判断Cookie中没有购物车
                if (Constants.BUYER_CART.equals(cookie.getName()))
                {
//        3：有
//                    String value = cookie.getValue();
                    //将得到的购物车字符串转化为购物车对象
                    buyerCart = om.readValue(cookie.getValue(),BuyerCart.class);
                    break;
                }
            }
        }
        //判断用户是否登录
        String username = sessionProvider.getAttribuerForUsername(RequestUtils.getCSESSIONID(request,response));
        if (null!=username)
        {
            if (buyerCart!=null) {
//            3：有  把购物车中商品添加到Redis的购物车中
                skuService.insertBuyerCartToRedis(buyerCart,username);
//                清理之前Cookie
                Cookie cookie = new Cookie(Constants.BUYER_CART, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
//            3：从Redis中取出所有的购物车
            buyerCart = skuService.selectBuyerCartFromRedis(username);
        }

        if (null!=buyerCart)
        {
//        把购物车装满
            List<BuyerItem> items = buyerCart.getItems();
            for (BuyerItem item:items) {
                item.setSku(skuService.selectSkuById(item.getSku().getId()));
            }
        }
//        跳转到购物车页面
//        回显购物车内容
        return "cart";
    }
}
