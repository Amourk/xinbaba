package cn.yy.core.service;

import cn.yy.core.bean.product.Product;
import cn.yy.core.bean.product.Sku;

import java.util.List;

public interface CmsService {
    //   查询商品
    public Product selecrProductById(Long id);
    //查询sku结果集（颜色）
    public List<Sku> selectSkuByProductId(Long id);
}
