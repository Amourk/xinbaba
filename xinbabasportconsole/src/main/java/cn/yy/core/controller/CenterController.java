package cn.yy.core.controller;

//import cn.yy.core.

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/control/")
public class CenterController {

//    @Autowired
//    private TestService testService;

//    入口
    @RequestMapping(value="index.do")
    public String index(Model model)
    {
        return "index";
    }
    // 头
    @RequestMapping(value="top.do")
    public String top(Model model)
    {
        return "top";
    }
    // 身体
    @RequestMapping(value="main.do")
    public String main(Model model)
    {
        return "main";
    }
    // 左部
    @RequestMapping(value="left.do")
    public String left(Model model)
    {
        return "left";
    }
    //  右部
    @RequestMapping(value="right.do")
    public String right(Model model)
    {
        return "right";
    }
//    frame/product_main.do
    // 商品
    @RequestMapping(value="frame/product_main.do")
    public String product_main(Model model)
    {
        return "frame/product_main";
    }
    // 商品----左部
    @RequestMapping(value="frame/product_left.do")
    public String product_left(Model model)
    {
        return "frame/product_left";
    }
    @RequestMapping(value="brand/list.do")
    public String list(Model model)
    {
        return "brand/list";
    }

}
