package cn.yy.core.service.message;

import cn.yy.core.bean.product.Color;
import cn.yy.core.bean.product.Product;
import cn.yy.core.bean.product.Sku;
import cn.yy.core.service.CmsService;
import cn.yy.core.service.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

public class CustomMessageListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService;
    @Autowired
    private CmsService cmsService;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage tm = (ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMq中的消息是-cms:" + tm.getText());
            String id=tm.getText();
            //设置静态化界面的数据
            Map<String,Object> root = new HashMap<>();
            //查询商品
            Product product = cmsService.selecrProductById(Long.parseLong(id));
            //查询sku结果集
            List<Sku> skus = cmsService.selectSkuByProductId(Long.parseLong(id));
            //查找颜色结果集
            Set<Color> colors = new HashSet<>();
            for (Sku sku :skus) {
                colors.add(sku.getColor());
            }
            root.put("product",product);
            root.put("skus",skus);
            root.put("colors",colors);
            staticPageService.productStaticPage(root,id);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
