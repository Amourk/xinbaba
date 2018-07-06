package cn.yy.core.service.user;

import cn.yy.core.bean.user.Buyer;

public interface BuyerService {
    //验证用户名是否正确(通过用户名查找用户对象)
     Buyer selectBuyerByUsername(String username);
}
