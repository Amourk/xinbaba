package cn.yy.core.service;

import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Product;
import cn.yy.core.bean.product.ProductQuery;
import cn.yy.core.bean.product.Sku;
import cn.yy.core.bean.product.SkuQuery;
import cn.yy.core.dao.product.ProductDao;
import cn.yy.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("searchService")
public class SearchServiceImpl implements SearchService{

    @Autowired
    private SolrServer solrServer;
    public Pagination selectPaginationByQuery(Integer pageNo,String keyword,Long brandId,String price) throws SolrServerException {
        //创建包装类
        ProductQuery productQuery = new ProductQuery();
        //设置当前页
        productQuery.setPageNo(Pagination.cpn(pageNo));
        //每页显示15条
        productQuery.setPageSize(15);
        //拼接条件
        StringBuffer params = new StringBuffer();

        List<Product> products = new ArrayList<Product>();
        SolrQuery solrQuery = new SolrQuery();
        //关键词
        solrQuery.set("q","name_ik:"+keyword);
//        solrQuery.set("q","name_ik:风");
        params.append("keyword=").append(keyword);
        //排序
        solrQuery.addSort("price", SolrQuery.ORDER.asc);
        //solr分页
        solrQuery.setStart(productQuery.getStartRow());
        solrQuery.setRows(productQuery.getPageSize());
        //高亮
        solrQuery.setHighlight(true);//打开开关
        solrQuery.addHighlightField("name_ik");//设置字段
        //设置样式
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");
        //查询条件
        //品牌id
        if (null!=brandId) {
            solrQuery.addFilterQuery("brandId:" + brandId);
        }
        //价格
        if (null!=price)
        {
            String[] p = price.split("-");
            if (p.length==2)
            {
                solrQuery.addFilterQuery("price:[" + p[0] +" TO " + p[1] + "]");
            }else
            {
                solrQuery.addFilterQuery("price:[" + p[0] + " TO *]");
            }
        }
        //执行查询
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //取出高亮
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        // Map K : V    442 : Map
        // Map K : V    name_ik : List<String>
        // List<String> list   2016年最新款191期卖的瑜伽服最新限量纯手工制作细心打造商户经典买一送一清 list.get(0);
        //结果集
        SolrDocumentList docs = queryResponse.getResults();
        //发现的总条数
        long numFound = docs.getNumFound();
        //遍历结果集 返回集合
        for ( SolrDocument doc:docs) {
            //创建商品对象
            Product product =new Product();
            //商品Id
            String id = (String) doc.get("id");//id是在solr的schema.xml的类型
            product.setId(Long.parseLong(id));
            //商品名称
            Map<String, List<String>> map = highlighting.get(id);
            List<String> list = map.get("name_ik");
            product.setName(list.get(0));
//            String name = (String) doc.get("name_ik");
            //图片
            String url = (String) doc.get("url");
            product.setImgUrl(url);
            //价格
//            Float price = (Float) doc.get("price");
            product.setPrice((Float) doc.get("price"));
            //品牌ID
//            Integer brandId = (Integer) doc.get("brandId");
            product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));
            //添加到集合中去

            products.add(product);
        }
        //创建分页对象
        Pagination pagination = new Pagination(
                productQuery.getPageNo(),//当前页
                productQuery.getPageSize(),//每页显示12条
                (int) numFound,//总条数
                products//结果集
        );
        //分页展示
        String url = "/search";
        pagination.pageView(url,params.toString());
        return pagination;
    }
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;

    //保存商品到solr服务器中
    public void insertProductToSolr(Long id) throws IOException, SolrServerException {
        //通过商品id找到商品的其他属性
        Product p = productDao.selectByPrimaryKey(id);
        //将商品信息保存到solr中
        SolrInputDocument doc = new SolrInputDocument();
        //商品id
        doc.setField("id",id);
        //商品名称
        doc.setField("name_ik",p.getName());
        //图片
        doc.setField("url",p.getImages()[0]);
        //价格   select price from bbs_sku where product_id =442 order by price asc limit 0,1
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(id);
        skuQuery.setOrderByClause("price asc");
        skuQuery.setPageNo(1);
        skuQuery.setPageSize(1);
        skuQuery.setFields("price");
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        doc.setField("price",skus.get(0).getPrice());
        //品牌id
        doc.setField("brandId",p.getBrandId());
        //图片
        solrServer.add(doc);
        solrServer.commit();
    }

}
