package cn.yy.core.service;

import cn.yy.core.bean.product.Product;
import cn.yy.core.bean.product.Sku;
import cn.yy.core.bean.product.SkuQuery;
import cn.yy.core.dao.product.ColorDao;
import cn.yy.core.dao.product.ProductDao;
import cn.yy.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("cmsService")
public class CmsServiceImpl implements CmsService{
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;
    //   查询商品
    public Product selecrProductById(Long id)
    {
        return productDao.selectByPrimaryKey(id);
    }
    //查询sku结果集（颜色）
    public List<Sku> selectSkuByProductId(Long id)
    {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(id);
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        //设置颜色
        for (Sku sku:skus) {
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }
}
