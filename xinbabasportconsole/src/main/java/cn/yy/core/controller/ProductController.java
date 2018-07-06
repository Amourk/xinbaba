package cn.yy.core.controller;

import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Brand;
import cn.yy.core.bean.product.Color;
import cn.yy.core.bean.product.Product;
import cn.yy.core.service.product.BrandService;
import cn.yy.core.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
//商品管理
//列表
//上架
//添加

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @RequestMapping(value = "/product/list.do")
    //商品结果集
    private String list(String name,Integer pageNo,Boolean isShow,Long brandId,Model model)
    {
        //品牌结果集
        List<Brand> brands = brandService.selectBrandListByQuery(1);
        model.addAttribute("brands",brands);
        Pagination pagination = productService.selectPaginationByQuery(pageNo,name,brandId,isShow);
        model.addAttribute("pagination",pagination);
        model.addAttribute("name",name);
        model.addAttribute("brandId",brandId);
        if (null!=isShow) {
            model.addAttribute("isShow", isShow);
        }else {
            model.addAttribute("isShow", false);
        }
        return "product/list";
    }
    @RequestMapping(value = "/product/toAdd.do")
    private String toAdd(Model model)
    {
        //品牌结果集
        List<Brand> brands = brandService.selectBrandListByQuery(1);
        model.addAttribute("brands",brands);
        //颜色结果集
        List<Color> colors = productService.selectColotList();
        model.addAttribute("colors",colors);
        return "product/add";
    }
    //商品添加
    @RequestMapping(value = "/product/add.do")
    private String add(Product product)
    {
        productService.insertProduct(product);
        return "redirect:/product/list.do";
    }
    //上架商品
    @RequestMapping(value = "/product/isShow.do")
    private String isShow(Long[] ids)
    {
        productService.isShow(ids);
        return "forward:/product/list.do";
    }
}
