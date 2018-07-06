package cn.yy.core.service.user;

public interface SessionProvider {
    //保存用户名到redis中
    public void setAttribuerForUsername(String name,String value);
    //从redis中取出用户名
    public String getAttribuerForUsername(String name);
}
