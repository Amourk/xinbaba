package cn.yy.core.controller;

import cn.yy.common.web.Constants;
import cn.yy.core.service.product.UploadService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class UploadPicController {

    @Autowired
    private UploadService uploadService;
    //上传图片
    @RequestMapping(value = "/upload/uploadPic.do")
    private void uploadPic(@RequestParam(required = false) MultipartFile pic, HttpServletResponse response) throws IOException {

        String path = uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize());
        String url = Constants.IMG_URL+path;
        System.out.println(url.toString());
        //将路径变成json传到前端
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",url);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonObject.toString());
    }
    //同时上传多张图片
    @RequestMapping(value = "/upload/uploadPics.do")
    private @ResponseBody
    List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics, HttpServletResponse response) throws IOException {

        List<String> urls = new ArrayList<>();
        for (MultipartFile pic:pics) {
            String path = uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize());
            String url = Constants.IMG_URL+path;
            urls.add(url);
        }
        return urls;
    }
    //富文本编辑器上传图片
    //上传图片
    @RequestMapping(value = "/upload/uploadFck.do")
    private void uploadFck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //spring 提供MultiparRequert强转
        MultipartRequest mp = (MultipartRequest) request;
        Map<String,MultipartFile> fileMap = mp.getFileMap();
        Set<Map.Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
        for (Map.Entry<String, MultipartFile> entry:entrySet) {
            MultipartFile pic = entry.getValue();
            String path = uploadService.uploadPic(pic.getBytes(),pic.getOriginalFilename(),pic.getSize());
            String url = Constants.IMG_URL+path;
//            System.out.println(url.toString());
            //将路径变成json传到前端
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error",0);
            jsonObject.put("url",url);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonObject.toString());
        }
    }
}
