package cn.yy.core.controller;

import cn.yy.core.bean.product.Sku;
import cn.yy.core.service.product.SkuService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class SkuController {

    @Autowired
    private SkuService skuService;
    //去到修该界面
    @RequestMapping("/sku/list.do")
    private String list(Long productId, Model model)
    {
        List<Sku> skus =skuService.selectSkuListByProductId(productId);
        model.addAttribute("skus",skus);
        return "sku/list";
    }
    @RequestMapping("/sku/addSku.do")
    private void addSku(Sku sku, HttpServletResponse response) throws IOException {
        skuService.updateSkuById(sku);
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("message","保存成功！！！");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonObject.toString());
    }
}
