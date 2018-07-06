package cn.yy.common.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

//上传图片到fast
public class FastDFSUtils {
    public static String UploadPic(byte[] pic ,String name ,long size)
    {
        String path ="";//图片在服务器中地址（小弟返回给客户端的地址）
        ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
        //ClientGloble 读配置文件
        try {
            ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
            //老大
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            //小弟
            StorageServer storageServer = null;
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);

            String ext = FilenameUtils.getExtension(name);//封装图片的地址
            //图片的描述
            NameValuePair[] meta_list = new NameValuePair[3];
            meta_list[0] = new NameValuePair("fileName",name);
            meta_list[1] = new NameValuePair("fileExt",ext);
            meta_list[2] = new NameValuePair("fileSize",String.valueOf(size));
            //传入图片二进制，图片的地址，图片的描述
            path = storageClient1.upload_appender_file1(pic,ext,meta_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
