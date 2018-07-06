package cn.yy.core.service.user;

import cn.yy.common.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service("sessionProvider")
public class SessionProviderImpl implements SessionProvider {
    private int exp =30;
    public void setExp(int exp) {
        this.exp = exp;
    }
    @Autowired
    private Jedis jedis;
    //保存用户名到redis中
    public void setAttribuerForUsername(String name,String value)
    {
//        　K　：　CSESSIONID:Constants.USER_NAME   == name
        jedis.set(name+Constants.USER_NAME,value);
        //设置存活时间
        jedis.expire(name+Constants.USER_NAME,30*exp);
    }
    //从redis中取出用户名
    public String getAttribuerForUsername(String name)
    {
        String value = jedis.get(name + Constants.USER_NAME);
        if (null!=value)
        {
            //设置存活时间
            jedis.expire(name+Constants.USER_NAME,30*exp);
        }
        return value;
    }
}
