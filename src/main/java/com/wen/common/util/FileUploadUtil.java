package com.wen.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName FileUploadUtil
 * @Description
 * @Author 黄文聪
 * @Date 2019-11-25 14:14
 * @Version 1.0
 **/
@Slf4j
public class FileUploadUtil {
    /**
     * 基础的文件上传
     * @param file  上传的文件
     * @param request
     * @param localPath  上传文件的路径，没有的话会新建
     * @return
     */
    public static String FileUpload(MultipartFile file, HttpServletRequest request,String localPath){
        if (null!=file&&!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            localPath = localPath +"/"+ fileName + suffix;
            File files = new File(localPath);
            if (!files.getParentFile().exists()){
                files.getParentFile().mkdirs();
                try {
                    file.transferTo(files);
                    return localPath;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     *   ftp 文件上传  完整的文件路径需要自己拼写
     * @param host  主机地址
     * @param ftpPort   主机端口
     * @param username  ftp用户名
     * @param password  ftp密码
     * @param basePath  ftp服务器基础目录
     * @param filePath  ftp服务器文件存放路径
     * @param filename  文件名称
     * @param input     输入流
     * @return  上传成功返回文件ftp路径
     */
    public static boolean ftpFileUpload(String host, String ftpPort, String username, String password, String basePath,
                                       String filePath, String filename, InputStream input){

        Integer port = Integer.valueOf(ftpPort);
        FTPClient ftp = new FTPClient();
        Integer reply ;
        boolean result = false;
        try {
            ftp.connect(host,port);
            ftp.login(username,password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)){
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(basePath);
            String path = filePath;
            String[] pah = path.split("/");
            // 分层创建目录
            for (String pa : pah) {
                log.info(pa);
                ftp.makeDirectory(pa);
                // 切到到对应目录
                ftp.changeWorkingDirectory(pa);
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType( FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();//这个设置允许被动连接--访问远程ftp时需要
            //设置缓存区
            ftp.setBufferSize(1024*1024);
            BufferedInputStream inputStream = new BufferedInputStream(input);
            System.out.println(inputStream);
            //上传文件
            if (!ftp.storeFile(filename, inputStream)) {

                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }



}
