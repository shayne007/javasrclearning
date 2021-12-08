package com.feng.fdfs.controller;

import com.feng.fdfs.FastDfsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/1/21
 */

@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    private FastDfsClient fastDFSClient;

    /**
     * 上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        return fastDFSClient.uploadFile(file);
    }

    @RequestMapping("/download")
    public void downloadFile(String fileUrl, HttpServletResponse response) throws IOException {
        byte[] bytes = fastDFSClient.downloadFile(fileUrl);
        /** TODO 这里只是为了整合fastdfs，所以写死了文件格式。需要在上传的时候保存文件名。下载的时候使用对应的格式 **/
        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode("sb.xlsx", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}