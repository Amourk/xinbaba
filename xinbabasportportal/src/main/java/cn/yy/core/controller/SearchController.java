package cn.yy.core.controller;

import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Brand;
import cn.yy.core.service.SearchService;
import cn.yy.core.service.product.BrandService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    //前端入口
    @RequestMapping(value = "/")
    private String index()
    {
        return "index";
    }

    @Autowired
    private SearchService searchService;
    @Autowired
    private BrandService brandService;
    @RequestMapping(value = "/search")
    private String search(Integer pageNo,Long brandId,String price,String keyword, Model model) throws SolrServerException {
        //将从redis中查询的品牌添加到页面
        List<Brand> brands = brandService.selectBrandListFromRedis();
        model.addAttribute("brands",brands);
        Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword,brandId,price);
        model.addAttribute("pagination",pagination);
        model.addAttribute("brandId",brandId);
        model.addAttribute("price",price);
        //已选条件容器
        Map<String,String> map = new HashMap<String,String>();
        if(null!=brandId)
        {
            for (Brand brand:brands) {
                if (brandId==brand.getId()) {
                    map.put("品牌", brand.getName());
                }
            }
        }
        if (null!=price)
        {
            if (price.contains("-"))
            {
            map.put("价格",price);
            }else
            {
                map.put("价格",price+"以上");
            }
        }
        model.addAttribute("map",map);
        return "search";
    }
}
