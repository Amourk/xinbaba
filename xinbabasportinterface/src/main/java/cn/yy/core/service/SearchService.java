package cn.yy.core.service;

import cn.itcast.common.page.Pagination;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

public interface SearchService {
    public Pagination selectPaginationByQuery(Integer pageNo, String keyword,Long brandId,String price) throws SolrServerException;
    //保存商品到solr服务器中
    public void insertProductToSolr(Long id) throws IOException, SolrServerException;
}
