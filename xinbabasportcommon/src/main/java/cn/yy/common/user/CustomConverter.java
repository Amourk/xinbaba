package cn.yy.common.user;

import org.springframework.core.convert.converter.Converter;

public class CustomConverter implements Converter<String,String> {
    //去掉前后空格
    @Override
    public String convert(String source) {
        try{
            if (null!=source)
            {
                source=source.trim();
                if (!"".equals(source))
                {
                    return source;
                }
            }
        }catch (Exception e) {
        }
        return null;
    }
}
