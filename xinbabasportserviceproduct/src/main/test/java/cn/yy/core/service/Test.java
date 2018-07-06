package cn.yy.core.service;

import cn.yy.core.bean.product.Sku;
import cn.yy.core.bean.product.SkuQuery;
import cn.yy.core.bean.test;
import cn.yy.core.dao.product.ProductDao;
import cn.yy.core.dao.product.SkuDao;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class Test {

//    @Autowired
//    private TestDao testDao;
    @Autowired
    private TestService testService;


     @org.junit.Test
    public void test()
     {
         test test = new test();
         test.setName("小敏");
         test.setId(18);
         testService.insertTest(test);
     }

     /////////////////////////////////////////////////////////////////////////////////////////////


    public String[] getName ()//得到目录名字
     {
         String name[]= new String[2];
         String path = "G:\\demo"; // 路径
         File f = new File(path);
         if (!f.exists()) {
             System.out.println(path + " not exists");
             return null;
         }

         File fa[] = f.listFiles();
         for (int i = 0; i < fa.length; i++) {
             File fs = fa[i];
             if (fs.isDirectory()) {
                 name[i]=fs.getName();
//                 System.out.println(fs.getName() + " [目录]");
             } else {
//                 System.out.println(fs.getName());
             }
         }
         return name;
     }

    private static ArrayList<String> filelist = new ArrayList<String>();
    private static ArrayList<String> filelist1 = new ArrayList<String>();

     @org.junit.Test
    public void file()
     {
         String filePath = "G:\\demo\\demo1";
         getFiles(filePath);
     }
    static void getFiles(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
      /*
       * 递归调用
       */
                getFiles(file.getAbsolutePath());
                filelist.add(file.getAbsolutePath());
//                System.out.println("显示" + filePath + "下所有子目录及其文件" + file.getAbsolutePath());//dmeo11
            } else {
                filelist1.add(file.getAbsolutePath());
//                System.out.println("显示" + filePath + "下所有子目录" + file.getAbsolutePath());//a.txt
            }
        }
    }

    @org.junit.Test
    public void main()
    {
        String[] name = getName();//得到了两个目录的名字
        String filePath = name[0];
        getFiles("G:\\demo\\"+filePath);
        for (int i =0 ;i<filelist.size();i++)
        {
            System.out.println(filelist.get(i));
        }

    }
    //测试逆向工程

    @Autowired
    private ProductDao productDao;
     @org.junit.Test
    public void testAdd()
     {
         //按id查询
//         Product product = productDao.selectByPrimaryKey(441l);
//         System.out.println(product);
         //按条件
//         ProductQuery productQuery = new ProductQuery();
//         productDao.selectByExample(productQuery.createCriteria())
     }

     @org.junit.Test
    public void testRedis()
     {
         Jedis jedis =new Jedis("192.168.200.128",6379);
         Long pno = jedis.incr("pno");
         System.out.println(pno);
     }

     @Autowired
    private Jedis jedis;
     @org.junit.Test
    public void testRedisSpring()
     {
         jedis.set("aaa","aaaaa");
     }
     @org.junit.Test
    public void testSolr() throws IOException, SolrServerException {
         String url = "http://192.168.200.128:8080/solr";
         SolrServer solrServer = new HttpSolrServer(url);
         SolrInputDocument solrInputFields = new SolrInputDocument();
         solrInputFields.setField("id","1");
         solrInputFields.setField("name","林元彬");
         solrServer.add(solrInputFields);
         solrServer.commit();
     }
     @Autowired
     private SolrServer solrServer;
     @org.junit.Test
    public void TestSolrjSpring() throws IOException, SolrServerException {
         SolrInputDocument solrInputFields = new SolrInputDocument();
         solrInputFields.setField("id","2");
         solrInputFields.setField("name","林元彬2");
         solrServer.add(solrInputFields);
         solrServer.commit();
     }
     @Autowired
     private SkuDao skuDao;
     @org.junit.Test
    public void TestGetPrice()
     {
//         SkuQuery skuQuery = new SkuQuery();
//         skuQuery.createCriteria().andProductIdEqualTo(381l);
//         skuQuery.setOrderByClause("price asc");
//         skuQuery.setPageNo(1);
//         skuQuery.setPageSize(1);
//         skuQuery.setFields("price");
//         List<Sku> skus = skuDao.selectByExample(skuQuery);
//         Float price = skus.get(0).getPrice();
         SkuQuery skuQuery = new SkuQuery();
         skuQuery.createCriteria().andProductIdEqualTo(381l);
         skuQuery.setOrderByClause("price asc");
         skuQuery.setPageNo(1);
         skuQuery.setPageSize(1);
         skuQuery.setFields("price");
         List<Sku> skus = skuDao.selectByExample(skuQuery);
         Float price = skus.get(0).getPrice();
         System.out.println("aaaaaaaaa"+price);
     }
    int[] nums= new int[]{2, 3, 0, 1, 4, 5};
    int[] duplication;

    @org.junit.Test
    public void testSHuju()
    {
        duplicate(nums,nums.length,duplication);
    }

     public boolean duplicate(int[] nums, int length, int[] duplication) {
         if (nums == null || length <= 0)
             return false;
         for (int i = 0; i < length; i++) {
             while (nums[i] != i) {
                 if (nums[i] == nums[nums[i]]) {
                     duplication[0] = nums[i];
                     return true;
                 }
                 swap(nums, i, nums[i]);
//                 System.out.println("i=" + nums[i]);
             }
//             System.out.println(nums[0]+" "+nums[1]+" "+nums[2]+" "+nums[3]+" "+nums[4]+nums[5]);
//             return true;
         }
         return false;
     }
    private void swap(int[] nums, int i, int j) {
        int t = nums[i]; nums[i] = nums[j]; nums[j] = t;
    }

}
