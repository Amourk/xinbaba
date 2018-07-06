package cn.yy.core.service.user;

import cn.yy.core.bean.user.Buyer;
import cn.yy.core.bean.user.BuyerQuery;
import cn.yy.core.dao.user.BuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{
    @Autowired
    private BuyerDao buyerDao;
    //验证用户名是否正确(通过用户名查找用户对象)
    public Buyer selectBuyerByUsername(String username)
    {
        BuyerQuery buyerQuery = new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(username);
        List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
        if (null!=buyers&&buyers.size()>0) {
            return buyers.get(0);
        }
        return null;
    }
}
