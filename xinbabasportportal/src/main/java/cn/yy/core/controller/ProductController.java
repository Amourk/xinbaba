package cn.yy.core.controller;

import cn.yy.core.bean.product.Color;
import cn.yy.core.bean.product.Product;
import cn.yy.core.bean.product.Sku;
import cn.yy.core.service.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController {
    //去商品详细页面
    @Autowired
    private CmsService cmsService;
    @RequestMapping(value = "/product/detail")
    public String detail(Long id, Model model)
    {
        //查询商品
        Product product = cmsService.selecrProductById(id);
        model.addAttribute("product",product);
        //查询sku结果集
        List<Sku> skus = cmsService.selectSkuByProductId(id);
        //查找颜色结果集
        Set<Color> colors = new HashSet<>();
        for (Sku sku :skus) {
            colors.add(sku.getColor());
        }
        model.addAttribute("skus",skus);
        model.addAttribute("colors",colors);
        return "product";
    }
}
