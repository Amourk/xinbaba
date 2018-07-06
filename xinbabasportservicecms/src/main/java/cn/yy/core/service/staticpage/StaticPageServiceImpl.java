package cn.yy.core.service.staticpage;

import cn.yy.core.service.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Map;

public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {
    //声明
    //注入
    private Configuration conf;

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.conf = freeMarkerConfigurer.getConfiguration();
    }

    //静态化商品详情界面
    public void productStaticPage(Map<String,Object> root,String id)
    {
        //输出的路径
        String path = getPath("/html/product/"+id+".html");
        File file = new File(path);//得到全路径
        File parentFile = file.getParentFile();
        //检查proudct是否创建
        if (!parentFile.exists())
        {
            parentFile.mkdirs();
        }
        Writer out=null;
        //模板对象
        try {
            Template template = conf.getTemplate("product.html");
            //输出流  最终成文件
            out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            template.process(root, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=out)
            {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //获取路径
    public String getPath(String name){
        return servletContext.getRealPath(name);
    }
    //声明
    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        // TODO Auto-generated method stub
        this.servletContext = servletContext;
    }
}
