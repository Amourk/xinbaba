package cn.yy.core.service.message;

import cn.yy.core.service.SearchService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.IOException;

public class CustomMessageListener implements MessageListener {

    @Autowired
    private SearchService searchService;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage tm = (ActiveMQTextMessage) message;
        try {
//            System.out.println("ActiveMq中的消息是:" + tm.getText());
            ////保存商品信息到Solr服务器
            try {
                searchService.insertProductToSolr(Long.parseLong(tm.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
