package cn.yy.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Brand;

import java.util.List;

public interface BrandService {
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);
    //通过id查找所有信息
    public Brand selectBrandById(Long id);
    //修改商品信息
    public void updateBrandById(Brand brand);
    //批量删除
    public void deletes(Long[] ids);
    //查询结果集
    public List<Brand> selectBrandListByQuery(Integer isDisplay);
    //从redis中查询
    public List<Brand> selectBrandListFromRedis();
}
