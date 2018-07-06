package cn.yy.core.service.product;

import cn.yy.core.bean.BuyerCart;
import cn.yy.core.bean.BuyerItem;
import cn.yy.core.bean.product.Color;
import cn.yy.core.bean.product.Product;
import cn.yy.core.bean.product.Sku;
import cn.yy.core.bean.product.SkuQuery;
import cn.yy.core.dao.product.ColorDao;
import cn.yy.core.dao.product.ProductDao;
import cn.yy.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService{

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;

    public List<Sku> selectSkuListByProductId(Long productId)
    {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId);
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        for (Sku sku:skus) {
            //遍历集合 通过颜色id 找到对应的颜色的名字
            sku.setColor(colorDao.selectByPrimaryKey(sku.getId()));
        }
        return skus;
    }
    //修改
    public void updateSkuById(Sku sku)
    {
        skuDao.updateByPrimaryKeySelective(sku);
    }
    @Autowired
    private ProductDao productDao;
    //通过skuId找到sku对象
    public Sku selectSkuById(Long skuId)
    {
        Sku sku = skuDao.selectByPrimaryKey(skuId);
        Product product = productDao.selectByPrimaryKey(sku.getProductId());
        Color color = colorDao.selectByPrimaryKey(sku.getColorId());
        sku.setProduct(product);
        sku.setColor(color);
        return sku;
    }
    @Autowired
    private Jedis jedis;
    //保存商品到 redis中
    public void insertBuyerCartToRedis(BuyerCart buyerCart,String username)
    {
        List<BuyerItem> items = buyerCart.getItems();
        for (BuyerItem item: items) {
            //判断redis中是否有该商品 没有添加 有追加
            if (jedis.hexists("buyerCart:"+username,String.valueOf(item.getSku().getId())))
            {
//                追加
                jedis.hincrBy("buyerCart:"+username,String.valueOf(item.getSku().getId()),
                        item.getAmount());
            }else
            {
                jedis.hset("buyerCart:"+username,String.valueOf(item.getSku().getId()),
                        String.valueOf(item.getAmount()));
            }
        }
    }
    //从redis中取出购物车
    public BuyerCart selectBuyerCartFromRedis(String username)
    {
        BuyerCart buyerCart = new BuyerCart();
        Map<String, String> map = jedis.hgetAll("buyerCart:" + username);
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry: entries) {
//            5：追加当前商品到购物车
            Sku sku = new Sku();
            sku.setId(Long.parseLong(entry.getKey()));
            BuyerItem buyerItem = new BuyerItem();
            buyerItem.setSku(sku);
            buyerItem.setAmount(Integer.parseInt(entry.getValue()));
            buyerCart.addItem(buyerItem);
        }
        return buyerCart;
    }
}
