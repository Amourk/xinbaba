package cn.yy.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Color;
import cn.yy.core.bean.product.Product;

import java.util.List;

public interface ProductService {
    public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow);
    //颜色结果集
    public List<Color> selectColotList();
    //保存商品
    public void insertProduct(Product product);
    //上架商品
    public void isShow(Long[] ids);
}
