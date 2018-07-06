package cn.yy.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.*;
import cn.yy.core.dao.product.ColorDao;
import cn.yy.core.dao.product.ProductDao;
import cn.yy.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow)
    {
        ProductQuery productQuery = new ProductQuery();
        //设置当前页
        productQuery.setPageNo(Pagination.cpn(pageNo));
        StringBuffer params = new StringBuffer();
        //条件
        ProductQuery.Criteria criteria = productQuery.createCriteria();

        if (null!=name)
        {
            criteria.andNameLike("%"+name+"%");
            params.append("name=").append(name);
        }
        if (null!=brandId)
        {
            criteria.andBrandIdEqualTo(brandId);
            params.append("&brandId=").append(brandId);
        }
        if (null!=isShow)
        {
            criteria.andIsShowEqualTo(isShow);
            params.append("&isShow=").append(isShow);
        }else {
            criteria.andIsShowEqualTo(false);
            params.append("&isShow=").append(false);
        }

        Pagination pagination = new Pagination(
                productQuery.getPageNo(),
                productQuery.getPageSize(),
                productDao.countByExample(productQuery),
                productDao.selectByExample(productQuery)
        );
        //分页展示
        String url="/product/list.do";
        pagination.pageView(url,params.toString());
        return pagination;
    }
    @Autowired
    private ColorDao colorDao;
    //颜色结果集
    public List<Color> selectColotList()
    {
        ColorQuery colorQuery = new ColorQuery();
        colorQuery.createCriteria().andParentIdNotEqualTo(0L);
        return colorDao.selectByExample(colorQuery);
    }

    @Autowired
    private SkuDao skuDao;
    //redis
    @Autowired
    private Jedis jedis;
    //保存商品
    public void insertProduct(Product product)
    {
        //下架状态
        //使用redis添加商品id
        Long id = jedis.incr("pno");
        product.setId(id);
        product.setIsShow(false);
        //是否删除
        product.setIsDel(true);
        //添加时间
        product.setCreateTime(new Date());
        productDao.insertSelective(product);
        //保存SKU
        //颜色
        String[] colors = product.getColors().split(",");
        //尺码
        String[] sizes = product.getSizes().split(",");

        for (String color: colors) {
            for (String size: sizes) {
                Sku sku = new Sku();
                //商品id
                sku.setProductId(product.getId());
                //颜色
                sku.setColorId(Long.parseLong(color));
                //尺码
                sku.setSize(size);
                //市场价
                sku.setMarketPrice(998f);
                //售价
                sku.setPrice(666f);
                //运费
                sku.setDeliveFee(8f);
                //库存
                sku.setStock(0);
                //限制
                sku.setUpperLimit(200);
                //时间
                sku.setCreateTime(new Date());
                skuDao.insertSelective(sku);
            }
            
        }
    }
    //solr
    @Autowired
    private JmsTemplate jmsTemplate;
    //上架商品
    public void isShow(Long[] ids)
    {
        Product product = new Product();
        product.setIsShow(true);
        for (final Long id:ids) {
            product.setId(id);
            productDao.updateByPrimaryKeySelective(product);
            //将productId发送信息到ActiveMQ中
            jmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });
        }
    }
}
