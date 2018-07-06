package cn.yy.core.service.product;


import cn.itcast.common.page.Pagination;
import cn.yy.core.bean.product.Brand;
import cn.yy.core.bean.product.BrandQuery;
import cn.yy.core.dao.product.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;
    public Pagination selectPaginationByQuery(String name,Integer isDisplay,Integer pageNo)
    {
        BrandQuery brandQuery = new BrandQuery();
        //设置当前页
        brandQuery.setPageNo(Pagination.cpn(pageNo));
        //设置每页数
        brandQuery.setPageSize(3);
        //条件

        StringBuffer params = new StringBuffer();

        if (null!=name)
        {
            brandQuery.setName(name);
            params.append("name=").append(name);
        }
        if (null!=isDisplay)
        {
            brandQuery.setIsDisplay(isDisplay);
            params.append("&isDisplay=").append(isDisplay);

        }else
        {
            brandQuery.setIsDisplay(1);
            params.append("&isDisplay=").append(1);
        }

        Pagination pagination = new Pagination(
                brandQuery.getPageNo(),
                brandQuery.getPageSize(),
                brandDao.selectCount(brandQuery)
        );
        //设置结果集
        pagination.setList(brandDao.selectBrandListByQuery(brandQuery));
        //分页展示
        String url = "/brand/list.do";
        pagination.pageView(url,params.toString());
        return pagination;
    }

    @Override
    public Brand selectBrandById(Long id) {
        return brandDao.selectBrandById(id);
    }
    //修改
    @Autowired
    private Jedis jedis;
    @Override
    public void updateBrandById(Brand brand) {
        //将品牌id和品牌name保存到redis中
        jedis.hset("brand",String.valueOf(brand.getId()),brand.getName());
        brandDao.updateBrandById(brand);
    }
    //从redis中查询
    public List<Brand> selectBrandListFromRedis()
    {
        List<Brand> brands = new ArrayList<Brand>();
        Map<String, String> map = jedis.hgetAll("brand");
        //遍历map
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry:entrySet) {
            Brand brand = new Brand();
            brand.setId(Long.parseLong(entry.getKey()));
            brand.setName(entry.getValue());
            brands.add(brand);
        }
        return brands;
    }
    //删除
    @Override
    public void deletes(Long[] ids) {
        brandDao.deletes(ids);
    }

    public List<Brand> selectBrandListByQuery(Integer isDisplay) {
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setIsDisplay(isDisplay);
        return brandDao.selectBrandListByQuery(brandQuery);
    }

}
