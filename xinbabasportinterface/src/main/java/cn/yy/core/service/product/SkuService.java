package cn.yy.core.service.product;

import cn.yy.core.bean.BuyerCart;
import cn.yy.core.bean.product.Sku;

import java.util.List;

public interface SkuService {
    public List<Sku> selectSkuListByProductId(Long productId);
    //修改
    public void updateSkuById(Sku sku);
    //通过skuId找到sku对象
    public Sku selectSkuById(Long skuId);
    //保存商品到 redis中
    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username);
    //从redis中取出购物车
    public BuyerCart selectBuyerCartFromRedis(String username);
}
