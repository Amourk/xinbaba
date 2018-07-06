package cn.yy.core.controller;


import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Brand;
import cn.yy.core.service.product.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;
    @RequestMapping(value = "/brand/list.do")
    private String list(String name, Integer pageNo, Integer isDisplay, Model model)
    {
        Pagination pagination = brandService.selectPaginationByQuery(name,isDisplay,pageNo);//查询出结果集
        model.addAttribute("pagination",pagination);
        model.addAttribute("name",name);
        if (null!=isDisplay) {
            model.addAttribute("isDisplay",isDisplay);
        }else
        {
            model.addAttribute("isDisplay",1);
        }
        return "brand/list";
    }
    @RequestMapping(value = "/brand/toEdit.do")
    private String toEdit(Model model,Long id)
    {
        Brand brand = brandService.selectBrandById(id);
//        System.out.println(brand.getIsDisplay());
        model.addAttribute("brand",brand);
        return "brand/edit";
    }
    //修改商品信息
    @RequestMapping(value = "/brand/edit.do")
    private String edit(Brand brand)
    {
        brandService.updateBrandById(brand);
        return "redirect:/brand/list.do";//重定向
    }
    //删除商品
    @RequestMapping(value = "/brand/deletes.do")
    private String delets(Long[] ids)
    {
        brandService.deletes(ids);
        return "forward:/brand/list.do";
    }
}
