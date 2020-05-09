package com.shine.integrationtestcover.controller;

import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.domain.JarInfo;
import com.shine.integrationtestcover.service.ProgramInstrumentService;
import com.shine.integrationtestcover.service.jarOpt.JarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author: Shine
 * @Date: 2019/3/17
 */
@RestController
public class FileUploadController {
    @Autowired
    private BaseConfig baseConfig;
    @Autowired
    private JarInfoService jarInfoService;

    @RequestMapping(value = "/uploadJar")
    @ResponseBody
    public String uploadJar(JarInfo jarInfo, @RequestPart("file") MultipartFile file){
        String result = "";
        if (!file.isEmpty()) {
            try {
                //新建一个空文件，并创建输出流。pathname值为：
                String pathname = baseConfig.getUploadedFilePath() + file.getOriginalFilename();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(pathname)));
                //将前端传来的文件写入新建的文件
                out.write(file.getBytes());
                //清空并关闭输入流
                out.flush();
                out.close();

                //将jar包信息存入数据库
                jarInfo.setTime(new Date());
                jarInfoService.insert(jarInfo);

                result = "上传成功";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result =  "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                result =  "上传失败," + e.getMessage();
            }
        } else {
            result =  "上传失败，因为文件是空的.";
        }
        return result;
    }
    @RequestMapping(value = "/uploadRegressiveJar")
    @ResponseBody
    public String uploadRegressiveJar(@RequestParam("file") MultipartFile file){
        String result = "";
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(baseConfig.getRegressionFilePath() + file.getOriginalFilename())));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result =  "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                result =  "上传失败," + e.getMessage();
            }
            result = "上传成功";
        } else {
            result =  "上传失败，因为文件是空的.";
        }
        return result;
    }
    @RequestMapping(value = "/fileList", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> getFileList(){
        File uploadedDirectory = new File(baseConfig.getUploadedFilePath());
        ArrayList filenames = new ArrayList();
        if(uploadedDirectory.isDirectory()) {
            File[] files = uploadedDirectory.listFiles();
            for (File file : files) {
                if(!file.isDirectory()) {
                    filenames.add(file.getName());
                }
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", filenames);
        return result;
    }

    //删除一个 jar 包信息
    @DeleteMapping(value = "/deleteJar")
    public String deleteJar(String name){
        String result;
        if(name != null) {
            //要删除文件的url
            String pathname = baseConfig.getUploadedFilePath() + name;
            //删除jar文件

            //在数据库中删除JarInfo
            jarInfoService.deleteByName(name);

            result = "删除成功";
        }
        else{
            result = "请指明要删除文件的名字！";
        }
        return "删除的结果是："+result;
    }
}
